package org.consumersunion.stories.server.persistence;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextHelper {
    static final String dateConverter = "(\\d\\d\\d\\d[-\\\\.]\\d\\d[-\\\\.]\\d\\d)([^T])?";
    static final Pattern beforeConverter = Pattern.compile("before:\\(?([-\\d:\\\\.TZ]+)\\)?");
    static final Pattern afterConverter = Pattern.compile("after:\\(?([-\\d:\\\\.TZ]+)\\)?");
    static final Pattern geoConverter = Pattern.compile(
            "near:(([-+]?[0-9]*\\.?[0-9]+)?,([-+]?[0-9]*\\.?[0-9]+)?) within:([0-9]?)");
    static final Pattern parenFix = Pattern.compile("\\(\\s*(" + dateConverter + "\\s+TO\\s+" + dateConverter + ")\\s*\\)");

    public static String processSearchText(String searchText) {
        if (searchText == null) {
            return null;
        }

        searchText = beforeConverter.matcher(searchText).replaceAll("[* TO $1}");
        searchText = afterConverter.matcher(searchText).replaceAll("{$1 TO *]");
        searchText = parenFix.matcher(searchText).replaceAll("[$1]");
        searchText = geoConverter.matcher(searchText).replaceFirst("");

        return searchText;
    }

    public static Boolean isGeoSearchToken(String searchToken) {
        return searchToken != null && geoConverter.matcher(searchToken).matches();
    }

    public static String extractLocalisation(String searchToken) {
        Matcher matcher = geoConverter.matcher(searchToken);
        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    public static String extractDistance(String searchToken) {
        Matcher matcher = geoConverter.matcher(searchToken);
        if (matcher.matches()) {
            return matcher.group(4);
        } else {
            return null;
        }
    }
}
