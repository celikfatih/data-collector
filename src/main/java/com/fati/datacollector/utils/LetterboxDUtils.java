package com.fati.datacollector.utils;

import org.jsoup.nodes.Element;

import java.util.function.IntFunction;
import java.util.function.UnaryOperator;

import static com.fati.datacollector.constants.Constants.HREF;
import static com.fati.datacollector.constants.Constants.LETTERBOX_D_POPULAR_MEMBERS_BASE_URL;
import static com.fati.datacollector.constants.Constants.PAGE;
import static com.fati.datacollector.constants.Constants.TABLE_USERNAME_QUERY;

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
                .select(TABLE_USERNAME_QUERY).first().attr(HREF);
    }

    public static String popularMembersUrlSetPageNumber(int pageNumber) {
        return pageNumber == 0 ? LETTERBOX_D_POPULAR_MEMBERS_BASE_URL
                : LETTERBOX_D_POPULAR_MEMBERS_BASE_URL + PAGE + pageNumber;
    }
}
