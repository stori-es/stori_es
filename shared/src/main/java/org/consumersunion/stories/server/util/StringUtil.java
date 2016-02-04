package org.consumersunion.stories.server.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Random;
import java.util.regex.Pattern;

import org.consumersunion.stories.common.shared.service.GeneralException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class StringUtil {
    public static final int VARIABLE_CASE = 0;
    public static final int ALL_CAPS = 1;
    public static final int ALL_LOWER = 2;

    private static final Pattern charFilter = Pattern.compile("[^a-z0-9 -]");
    private static final Pattern specialFilter = Pattern.compile("\"");
    private static final Pattern multiSpaceFilter = Pattern.compile(" {2,}");
    private static final Pattern spaceFilter = Pattern.compile(" ");

    /**
     * Generates a random string of characters and digits. Excludes easily
     * mis-read characters like 'l' and '1'.
     */
    public static String generateRandomPassword() {
        return generateRandomPassword(VARIABLE_CASE);
    }

    public static String generateRandomPassword(final int caseMode) {
        return generateRandomPassword(caseMode, 8);
    }

    public static String generateRandomPassword(final int caseMode, final int length) {
        Random rn = new Random();
        final byte[] stringB = new byte[length];

        for (int i = 0; i < length; i += 1) {
            int mode = rn.nextInt(59);
            if (mode < 12) {
                stringB[i] = rangedRand(rn, (byte) 'a', (byte) 'k');
            } else if (mode < 25) {
                stringB[i] = rangedRand(rn, (byte) 'm', (byte) 'z');
            } else if (mode < 33) {
                stringB[i] = rangedRand(rn, (byte) 'A', (byte) 'H');
            } else if (mode < 38) {
                stringB[i] = rangedRand(rn, (byte) 'J', (byte) 'N');
            } else if (mode < 49) {
                stringB[i] = rangedRand(rn, (byte) 'P', (byte) 'Z');
            } else {
                stringB[i] = rangedRand(rn, (byte) '0', (byte) '9');
            }
        }

        if (caseMode == ALL_CAPS) {
            return new String(stringB).toUpperCase();
        } else if (caseMode == ALL_LOWER) {
            return new String(stringB).toLowerCase();
        } else {
            return new String(stringB);
        }
    }

    public static String generateSlug(String input) {
        String output = input.toLowerCase();
        output = specialFilter.matcher(output).replaceAll("");
        output = charFilter.matcher(output).replaceAll("");
        output = multiSpaceFilter.matcher(output).replaceAll(" ");
        output = spaceFilter.matcher(output).replaceAll("-");

        return output;
    }

    public static String truncateString(String data, int length) {
        Element body = Jsoup.parse(data).body();

        boolean isHtml = body.getAllElements().size() > 1;
        if (isHtml) {
            return truncateHtml(data, length);
        } else {
            return truncatePlainText(data, length);
        }
    }

    private static String truncateHtml(String html, int maxLength) {
        boolean inTag = false;
        int contentCount = 0;
        char[] content = html.toCharArray();

        int i = 0;
        for (; i < html.length(); i++) {
            if (content[i] == '<') {
                inTag = true;
            } else if (inTag && content[i] == '>') {
                inTag = false;
            }

            if (!inTag) {
                contentCount += 1;
                if (contentCount >= maxLength) {
                    while ((i < html.length()) &&
                            !html.substring(i, i + 1).equals(" ")) {
                        i++;
                    }
                    break;
                }
            }
        }

        return Jsoup.parseBodyFragment(html.substring(0, i).trim() + "...").body().html().replaceAll("\\n", "");
    }

    private static String truncatePlainText(String data, int length) {
        if (data.length() < length) {
            return data;
        } else {
            int index = length;
            while ((index < data.length()) &&
                    !data.substring(index, index + 1).equals(" ")) {
                index++;
            }
            return data.substring(0, index) + "...";
        }
    }

    private static byte rangedRand(Random rn, byte low, byte hi) {
        byte i = (byte) (rn.nextInt(hi - low + 1));
        if (i < 0) {
            i = (byte) -i;
        }
        return (byte) (low + i);
    }

    public static String cleanPermalink(String permalink) {
        if (permalink == null) {
            return null;
        }

        try {
            permalink = URLDecoder.decode(permalink, "UTF-8").toLowerCase()
                    .replaceAll("[ _]", "-");
            return permalink.replaceAll("[^a-z0-9 -/]", ""); // clean all non safe characters and allow
            // to treat space and dash as the same
        } catch (UnsupportedEncodingException e) {
            throw new GeneralException(e);
        }
    }
}
