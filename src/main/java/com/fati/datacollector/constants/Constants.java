package com.fati.datacollector.constants;

/**
 * author @ fati
 * created @ 6.05.2021
 */

public class Constants {

    private Constants() {
    }

    public static final String LETTERBOX_D_BASE_URL = "https://letterboxd.com/";
    public static final String TWITTER_URL_QUERY = "a[href*=twitter]";
    public static final String HREF = "href";
    public static final String LETTERBOX_D_POPULAR_MEMBERS_BASE_URL = LETTERBOX_D_BASE_URL + "members/popular/this/month/";
    public static final String PAGE = "page/";
    public static final int PAGE_RESULT_SIZE = 30;
    public static final String TABLE_USERNAME_QUERY = "td > div.person-summary > a";
    public static final String TABLE_SELECT_QUERY = "table > tbody";
    public static final int TWITTER_MAX_PAGE_SIZE = 60;
    public static final int TWITTER_PER_PAGE_TWEET_COUNT = 50;
    public static final String LETTERBOX_D_USER_FILM_RATINGS_URL = "/films/ratings/";
    public static final String RATED_FILM_NAME_QUERY = "ul.poster-list > li > div > img";
    public static final String RATED_FILM_RATING_QUERY = "ul.poster-list > li > p";
    public static final int LETTERBOX_D_RATING_PAGE_SIZE = 100;
}
