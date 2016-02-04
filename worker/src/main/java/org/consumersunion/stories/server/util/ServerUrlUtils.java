package org.consumersunion.stories.server.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.gwtplatform.common.shared.UrlUtils;

public class ServerUrlUtils implements UrlUtils {
    @Override
    public String decodeQueryString(String encodedUrlComponent) {
        return decode(encodedUrlComponent);
    }

    @Override
    public String encodeQueryString(String decodedUrlComponent) {
        return encode(decodedUrlComponent);
    }

    @Override
    public String decodePathSegment(String encodedPathSegment) {
        return decode(encodedPathSegment);
    }

    @Override
    public String encodePathSegment(String decodedPathSegment) {
        return encode(decodedPathSegment);
    }

    private String decode(String encodedUrlComponent) {
        try {
            return URLDecoder.decode(encodedUrlComponent, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private String encode(String decodedUrlComponent) {
        try {
            return URLEncoder.encode(decodedUrlComponent, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
