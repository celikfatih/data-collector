package com.fati.datacollector.service;

import com.fati.datacollector.models.LetterboxDUser;
import com.fati.datacollector.repository.LetterboxDUserRepository;
import com.fati.datacollector.utils.LetterboxDUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.fati.datacollector.constants.Constants.*;
import static com.fati.datacollector.constants.Constants.HREF;

/**
 * author @ fati
 * created @ 6.05.2021
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class LetterboxDService {

    private final LetterboxDUserRepository letterboxDUserRepository;

    public void extractAndSaveLetterboxDUsers(int pageSizeLimit)  {
        List<LetterboxDUser> users = this.getPopularMembersThisMonth(pageSizeLimit);
        saveLetterboxDUsers(users);
    }

    @Transactional
    public void saveLetterboxDUsers(List<LetterboxDUser> users) {
        letterboxDUserRepository.saveAll(users);
    }

    private List<LetterboxDUser> getPopularMembersThisMonth(int pageSizeLimit) {
        return IntStream.rangeClosed(0, pageSizeLimit)
                .mapToObj(getUsersPerPage())
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .map(this::getLetterboxDUser)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private IntFunction<List<String>> getUsersPerPage() {
        return i -> {
            if (i == 0) {
                return getPopularMemberUsernamesThisMonth(LETTERBOX_D_POPULAR_MEMBERS_BASE_URL);
            } else {
                String url = LetterboxDUtils.popularMembersUrlSetPageNumber(i);
                return getPopularMemberUsernamesThisMonth(url);
            }
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
            Elements elements = userPage.select(CSS_QUERY);
            if (elements.size() < 2) {
                return empty;
            }
            String twitterUsername = elements.first().getElementsByAttribute(HREF).text();
            return Optional.of(LetterboxDUser.builder().username(username).twitterUsername(twitterUsername).build());
        } catch (IOException e) {
            log.error("User profile can not load. User: {}", username);
            return empty;
        }
    }
}
