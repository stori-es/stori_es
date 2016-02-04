package org.consumersunion.stories.dashboard.client.util;

public class GeoUtils {
    public static final String PATTERN = "near:([a-zA-Z0-9, ]+)( within:([0-9]+)){0,1}";
    public static final String PATTERN_BOTH = "near:([a-zA-Z0-9, ]*?) within:([0-9]*)";
    public static final String PATTERN_NEAR = "near:([a-zA-Z0-9, ]*?)";
    public static final String DEFAULT_DISTANCE = "5";

    public static boolean isLocationToken(String location) {
        return location.matches(PATTERN);
    }

    public static String extractNear(String location) {
        if (location.matches(PATTERN_BOTH)) {
            return location.replaceAll(PATTERN_BOTH, "$1");
        } else {
            return location.replaceAll(PATTERN_NEAR, "$1");
        }
    }

    public static String extractWithin(String location) {
        if (location.matches(PATTERN_BOTH)) {
            return location.replaceAll(PATTERN_BOTH, "$2");
        }

        return DEFAULT_DISTANCE;
    }
}
