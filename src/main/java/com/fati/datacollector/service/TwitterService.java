package com.fati.datacollector.service;

import com.fati.datacollector.model.TwitterTweet;
import com.fati.datacollector.model.TwitterUser;
import com.fati.datacollector.projection.OnlyTwitterUsername;
import com.fati.datacollector.repository.TwitterTweetRepository;
import com.fati.datacollector.repository.TwitterUserRepository;
import com.fati.datacollector.utils.TwitterUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
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
    private final TwitterUserRepository twitterUserRepository;
    private final TwitterTweetRepository twitterTweetRepository;
    private final LetterboxDService letterboxDService;

    public void extractAndSaveTweets() {
        List<OnlyTwitterUsername> usernames = letterboxDService.getAllTwitterUsernames();
        usernames.stream()
                .map(OnlyTwitterUsername::getTwitterUsername)
                .forEach(this::saveUserAndTweets);
    }

    private void saveUserAndTweets(String username) {
        saveTwitterUserByUsername(username);
        log.info("Saved username: {} and now extract all tweets...", username);
        List<TwitterTweet> tweets = IntStream.range(1, TWITTER_MAX_PAGE_SIZE)
                .mapToObj(getTweetsInPageByUsername(username))
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .map(TwitterTweet::new)
                .collect(Collectors.toList());
        saveTweetsForUser(tweets);
    }

    public synchronized boolean isAccessibleTwitterProfile(String username) {
        try {
            ResponseList<User> user = twitter.lookupUsers(username);
            return !user.get(0).isProtected();
        } catch (TwitterException e) {
            return false;
        }
    }

    @Transactional
    public void saveTwitterUserByUsername(String username) {
        try {
            User user = twitter.showUser(username);
            twitterUserRepository.save(new TwitterUser(user));
        } catch (TwitterException e) {
            log.error("User not saved, with username: {}", username);
        }
    }

    @Transactional
    public void saveTweetsForUser(List<TwitterTweet> tweets) {
        twitterTweetRepository.saveAll(tweets);
    }

    private synchronized IntFunction<ResponseList<Status>> getTweetsInPageByUsername(String username) {
        return i -> {
            try {
                ResponseList<Status> timeline = twitter.getUserTimeline(username, TwitterUtils.preparePaging(i));
                if (Objects.nonNull(timeline.getRateLimitStatus())) handleRateLimit(timeline.getRateLimitStatus());
                return timeline;
            } catch (TwitterException e) {
                log.error("Twitter connection not provided. Username -> {}", username);
            }
            return null;
        };
    }

    private void handleRateLimit(RateLimitStatus rateLimitStatus) {
        int remaining = rateLimitStatus.getRemaining();
        if (remaining == 0) {
            int resetTime = rateLimitStatus.getSecondsUntilReset() + 5;
            int sleep = (resetTime * 1000);
            try {
                Thread.sleep(Math.max(sleep, 0));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
