package org.consumersunion.stories.dashboard.client.application.util;

public class StringUtil {
    /**
     * This methods replace the \n by a \A to be used in the css content attribute because it won't compile in java
     *
     * @param timestamp
     * @return The string with a correct content line-break
     */
    public static native String lineBreak(String timestamp) /*-{
        return timestamp.replace('\n', '\u000A');
    }-*/;
}
