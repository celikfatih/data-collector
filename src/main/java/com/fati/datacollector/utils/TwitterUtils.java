package com.fati.datacollector.utils;

import twitter4j.Paging;

import static com.fati.datacollector.constants.Constants.TWITTER_PER_PAGE_TWEET_COUNT;

/**
 * author @ fati
 * created @ 8.05.2021
 */

public class TwitterUtils {

    private TwitterUtils() {
    }

    public static Paging preparePaging(int i) {
        Paging p = new Paging();
        p.setCount(TWITTER_PER_PAGE_TWEET_COUNT);
        p.setPage(i);
        return p;
    }
}
