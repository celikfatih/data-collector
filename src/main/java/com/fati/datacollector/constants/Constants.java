package com.fati.datacollector.constants;

/**
 * author @ fati
 * created @ 6.05.2021
 */

public class Constants {

    private Constants() {
    }

    public static final String LETTERBOX_D_BASE_URL = "https://letterboxd.com/";
    public static final String CSS_QUERY = "a[href*=twitter]";
    public static final String HREF = "href";
    public static final String LETTERBOX_D_POPULAR_MEMBERS_BASE_URL = LETTERBOX_D_BASE_URL + "members/popular/this/month/";
    public static final String PAGE = "page/";
    public static final int PAGE_RESULT_SIZE = 30;
    public static final String TABLE_USERNAME_QUERY = "td > div.person-summary > a";
    public static final String TABLE_SELECT_QUERY = "table > tbody";
    public static final int TWITTER_MAX_PAGE_SIZE = 2;
    public static final int TWITTER_PER_PAGE_TWEET_COUNT = 5;
}
