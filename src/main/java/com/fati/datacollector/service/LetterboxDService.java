package com.fati.datacollector.service;

import com.fati.datacollector.model.LetterBoxDRating;
import com.fati.datacollector.model.LetterboxDUser;
import com.fati.datacollector.projection.OnlyTwitterUsername;
import com.fati.datacollector.repository.LetterboxDRatingRepository;
import com.fati.datacollector.repository.LetterboxDUserRepository;
import com.fati.datacollector.utils.LetterboxDUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.fati.datacollector.constants.Constants.*;

/**
 * author @ fati
 * created @ 6.05.2021
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class LetterboxDService {

    private final LetterboxDUserRepository letterboxDUserRepository;
    private final LetterboxDRatingRepository letterboxDRatingRepository;
    private TwitterService twitterService;

    @Autowired
    private void setTwitterService(TwitterService twitterService) {
        this.twitterService = twitterService;
    }

    public List<OnlyTwitterUsername> getAllTwitterUsernames() {
        return letterboxDUserRepository.findAllBy();
    }

    public void extractAndSaveLetterboxDData(int pageSizeLimit)  {
        List<LetterboxDUser> users = this.getPopularMembersThisMonth(pageSizeLimit);
        users.stream()
                .map(LetterboxDUser::getUsername)
                .forEach(this::extractAndSaveLetterboxDRatings);
        saveLetterboxDUsers(users);
    }

    private void extractAndSaveLetterboxDRatings(String username) {
        List<LetterBoxDRating> ratings = getLetterBoxDUserRatedFilms(username);
        saveLetterboxDRatings(ratings);
    }

    @Transactional
    public void saveLetterboxDUsers(List<LetterboxDUser> users) {
        letterboxDUserRepository.saveAll(users);
    }

    @Transactional
    public void saveLetterboxDRatings(List<LetterBoxDRating> ratings) {
        letterboxDRatingRepository.saveAll(ratings);
    }

    private List<LetterboxDUser> getPopularMembersThisMonth(int pageSizeLimit) {
        return IntStream.rangeClosed(0, pageSizeLimit)
                .mapToObj(getUsersPerPage())
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(Collection::parallelStream)
                .map(this::getLetterboxDUser)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private IntFunction<List<String>> getUsersPerPage() {
        return i -> {
            log.info("getUsersPerPage method invoking pageNumber: {}", i);
            return getPopularMemberUsernamesThisMonth(LetterboxDUtils.popularMembersUrlSetPageNumber(i));
        };
    }

    private List<String> getPopularMemberUsernamesThisMonth(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Element table = document.select(TABLE_SELECT_QUERY).first();
            return IntStream.range(0, PAGE_RESULT_SIZE)
                    .mapToObj(LetterboxDUtils.getUsername(table))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Popular members page can not load. Url -> {}", url);
        }
        return Collections.emptyList();
    }

    private Optional<LetterboxDUser> getLetterboxDUser(String username) {
        Optional<LetterboxDUser> empty = Optional.empty();
        try {
            Document userPage = Jsoup.connect(LETTERBOX_D_BASE_URL + username).get();
            Elements elements = userPage.select(TWITTER_URL_QUERY);
            if (elements.size() < 2) {
                return empty;
            }
            String twitterUsername = elements.first().getElementsByAttribute(HREF).text();
            if (letterboxDUserRepository.existsLetterboxDUserByUsername(username) ||
                    !twitterService.isAccessibleTwitterProfile(twitterUsername)) {
                return empty;
            }
            return Optional.of(LetterboxDUser.builder().username(username).twitterUsername(twitterUsername).build());
        } catch (IOException e) {
            log.error("User profile can not load. User: {}", username);
            return empty;
        }
    }

    private List<LetterBoxDRating> getLetterBoxDUserRatedFilms(String username) {
        return IntStream.rangeClosed(0, LETTERBOX_D_RATING_PAGE_SIZE)
                .mapToObj(i -> Pair.of(username, i))
                .map(getRatedFilmPerPage())
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(Collection::parallelStream)
                .collect(Collectors.toList());
    }

    private List<LetterBoxDRating> getLetterBoxDRatings(String url, String username) {
        try {
            Document ratedFilmPage = Jsoup.connect(url).get();
            Elements names = ratedFilmPage.select(RATED_FILM_NAME_QUERY);
            Elements ratings = ratedFilmPage.select(RATED_FILM_RATING_QUERY);

            List<String> filmNames = names.eachAttr("alt");
            List<String> filmRatings = ratings.eachText();

            return IntStream.range(0, filmNames.size())
                    .mapToObj(createFilmRating(username, filmRatings, filmNames))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("User ratings can not load. User: {} and url : {}", username, url);
            return Collections.emptyList();
        }
    }

    private Function<Pair<String, Integer>, List<LetterBoxDRating>> getRatedFilmPerPage() {
        return p -> {
            log.info("getRatedFilmPerPage method invoking pageNumber: {} and username: {}", p.getSecond(), p.getFirst());
            return getLetterBoxDRatings(LetterboxDUtils.filmRatingsUrlSetPageNumber(p.getFirst(), p.getSecond()), p.getFirst());
        };
    }

    private IntFunction<LetterBoxDRating> createFilmRating(String username, List<String> filmRatings, List<String> filmNames) {
        return i -> {
            String filmRating = filmRatings.get(i);
            float rating = Long.valueOf(filmRating.chars().filter(ch -> ch == '★').count()).floatValue();
            if (filmRating.contains("½")) {
                rating += 0.5f;
            }
            return LetterBoxDRating.builder()
                    .id(UUID.randomUUID().toString())
                    .username(username)
                    .film(filmNames.get(i))
                    .rating(rating)
                    .build();
        };
    }
}
