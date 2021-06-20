package com.fati.datacollector.utils;

import org.jsoup.nodes.Element;

import java.util.function.IntFunction;
import java.util.function.UnaryOperator;

import static com.fati.datacollector.constants.Constants.*;

/**
 * author @ fati
 * created @ 6.05.2021
 */

public class LetterboxDUtils {

    private LetterboxDUtils() {
    }

    public static UnaryOperator<String> TABLE_INDEX_QUERY = s -> "tr:eq(" + s + ")";

    public static IntFunction<String> getUsername(Element table) {
        return i -> table.select(TABLE_INDEX_QUERY.apply(String.valueOf(i))).first()
                .select(TABLE_USERNAME_QUERY).first().attr(HREF).replaceAll("/", "");
    }

    public static String popularMembersUrlSetPageNumber(int pageNumber) {
        return pageNumber == 0 ? LETTERBOX_D_POPULAR_MEMBERS_BASE_URL
                : LETTERBOX_D_POPULAR_MEMBERS_BASE_URL + PAGE + pageNumber;
    }

    public static String filmRatingsUrlSetPageNumber(String username, int pageNumber) {
        return pageNumber == 0 ? LETTERBOX_D_BASE_URL + username + LETTERBOX_D_USER_FILM_RATINGS_URL
                : LETTERBOX_D_BASE_URL + username + LETTERBOX_D_USER_FILM_RATINGS_URL + PAGE + pageNumber;
    }
}
