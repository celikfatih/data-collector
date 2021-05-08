package com.fati.datacollector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fati.datacollector.models.TwitterUser;
import com.fati.datacollector.repository.TwitterUserRepository;
import com.fati.datacollector.utils.TwitterUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.fati.datacollector.constants.Constants.TWITTER_MAX_PAGE_SIZE;

/**
 * author @ fati
 * created @ 8.05.2021
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class TwitterService {

    private final Twitter twitter;
    private final ObjectMapper objectMapper;
    private final TwitterUserRepository twitterUserRepository;

    public void getTweetsForUser(String username) {
        List<TwitterUser> tweets = IntStream.range(1, TWITTER_MAX_PAGE_SIZE)
                .mapToObj(getTweetsInPageByUsername(username))
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .map(convertStatus())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        saveTweetsForUser(tweets);
    }

    private Function<Status, Optional<TwitterUser>> convertStatus() {
        return s -> {
            try {
                return Optional.of(TwitterUser.builder()
                        .id(UUID.randomUUID().toString())
                        .username(s.getUser().getScreenName())
                        .tweet(objectMapper.writeValueAsString(s))
                        .build());
            } catch (JsonProcessingException e) {
                log.error("Json exception.");
                return Optional.empty();
            }
        };
    }

    @Transactional
    public void saveTweetsForUser(List<TwitterUser> tweets) {
        twitterUserRepository.saveAll(tweets);
    }

    private IntFunction<ResponseList<Status>> getTweetsInPageByUsername(String username) {
        return i -> {
            try {
                return twitter.getUserTimeline(username, TwitterUtils.preparePaging(i));
            } catch (TwitterException e) {
                log.error("Twitter connection not provided. Username -> {}", username);
            }
            return null;
        };
    }
}
