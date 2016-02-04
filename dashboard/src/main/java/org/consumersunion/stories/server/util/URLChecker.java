package org.consumersunion.stories.server.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class URLChecker {
    private static final String YOUTUBE_HOST = "youtube.com";
    private static final String SOUNDCLOUD_HOST = "soundcloud.com";

    public static boolean verifyURL(String url) {
        try {
            HttpURLConnection con = openConnection(url);

            return con.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (MalformedURLException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean verifyPDFDocument(String url) {
        if (verifyURL(url)) {
            try {
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con = (HttpURLConnection) (new URL(url).openConnection());
                con.setRequestMethod("GET");

                return con.getContentType().contains("pdf");
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    public static boolean verifyImage(String url) {
        if (verifyURL(url)) {
            try {
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con = (HttpURLConnection) (new URL(url).openConnection());
                con.setRequestMethod("GET");

                return con.getContentType().contains("image");
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    public static boolean verifyVideo(String url) {
        try {
            HttpURLConnection connection = openConnection(url);
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String host = connection.getURL().getHost();

                return host.endsWith(YOUTUBE_HOST);
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean verifyAudio(String url) {
        try {
            HttpURLConnection connection = openConnection(url);
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String host = connection.getURL().getHost();

                return host.endsWith(SOUNDCLOUD_HOST);
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    private static HttpURLConnection openConnection(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) (new URL(url).openConnection());

        connection.setInstanceFollowRedirects(true);
        connection.setRequestMethod("HEAD");

        return followRedirects(connection);
    }

    private static HttpURLConnection followRedirects(HttpURLConnection connection) throws IOException {
        int statusCode = connection.getResponseCode();

        if (statusCode >= 300 && statusCode <= 399) {
            String newUrl = connection.getHeaderField("Location");
            String cookies = connection.getHeaderField("Set-Cookie");

            HttpURLConnection newConnection = (HttpURLConnection) (new URL(newUrl).openConnection());
            newConnection.setRequestProperty("Cookie", cookies);
            newConnection.setInstanceFollowRedirects(true);
            newConnection.setRequestMethod("HEAD");

            return followRedirects(newConnection);
        }

        return connection;
    }
}
