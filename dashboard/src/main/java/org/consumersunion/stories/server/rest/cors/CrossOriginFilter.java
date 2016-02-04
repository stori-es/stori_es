package org.consumersunion.stories.server.rest.cors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Joiner;

public class CrossOriginFilter implements Filter {
    private static final String ORIGIN_HEADER = "Origin";
    private static final String ACCESS_CONTROL_REQUEST_METHOD_HEADER = "Access-Control-Request-Method";
    private static final String ACCESS_CONTROL_REQUEST_HEADERS_HEADER = "Access-Control-Request-Headers";

    private static final String ACCESS_CONTROL_ALLOW_ORIGIN_HEADER = "Access-Control-Allow-Origin";
    private static final String ACCESS_CONTROL_ALLOW_METHODS_HEADER = "Access-Control-Allow-Methods";
    private static final String ACCESS_CONTROL_ALLOW_HEADERS_HEADER = "Access-Control-Allow-Headers";
    private static final String ACCESS_CONTROL_MAX_AGE_HEADER = "Access-Control-Max-Age";
    private static final String ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER = "Access-Control-Allow-Credentials";

    private static final String ALLOWED_METHODS_PARAM = "allowedMethods";
    private static final String ALLOWED_HEADERS_PARAM = "allowedHeaders";
    private static final String PREFLIGHT_MAX_AGE_PARAM = "preflightMaxAge";
    private static final String ALLOWED_CREDENTIALS_PARAM = "allowCredentials";
    private static final List<String> SIMPLE_HTTP_METHODS = Arrays.asList("GET", "POST", "HEAD");

    private final List<String> allowedMethods = new ArrayList<String>();
    private final List<String> allowedHeaders = new ArrayList<String>();

    private int preflightMaxAge = 0;
    private boolean allowCredentials = false;

    @Override
    public void init(FilterConfig config) throws ServletException {
        String allowedMethodsConfig = config.getInitParameter(ALLOWED_METHODS_PARAM);
        if (allowedMethodsConfig == null) {
            allowedMethodsConfig = "GET,POST";
        }
        allowedMethods.addAll(Arrays.asList(allowedMethodsConfig.split(",")));

        String allowedHeadersConfig = config.getInitParameter(ALLOWED_HEADERS_PARAM);
        if (allowedHeadersConfig == null) {
            allowedHeadersConfig = "X-Requested-With";
        }
        allowedHeaders.addAll(Arrays.asList(allowedHeadersConfig.split(",")));

        String preflightMaxAgeConfig = config.getInitParameter(PREFLIGHT_MAX_AGE_PARAM);
        if (preflightMaxAgeConfig == null) {
            preflightMaxAgeConfig = "1800"; // Default is 30 minutes
        }
        try {
            preflightMaxAge = Integer.parseInt(preflightMaxAgeConfig);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        String allowedCredentialsConfig = config.getInitParameter(ALLOWED_CREDENTIALS_PARAM);
        if (allowedCredentialsConfig == null) {
            allowedCredentialsConfig = "false";
        }
        allowCredentials = Boolean.parseBoolean(allowedCredentialsConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        handle((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void handle(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String origin = request.getHeader(ORIGIN_HEADER);

        if (isSimpleRequest(request)) {
            handleSimpleResponse(response, origin);
        } else {
            handlePreflightResponse(request, response, origin);
        }

        chain.doFilter(request, response);
    }

    private boolean isSimpleRequest(HttpServletRequest request) {
        return SIMPLE_HTTP_METHODS.contains(request.getMethod())
                && request.getHeader(ACCESS_CONTROL_REQUEST_METHOD_HEADER) == null;
    }

    private void handleSimpleResponse(HttpServletResponse response, String origin) {
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, origin);
        if (allowCredentials) {
            response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER, "true");
        }
    }

    private void handlePreflightResponse(HttpServletRequest request, HttpServletResponse response, String origin) {
        if (!isMethodAllowed(request) || !areHeadersAllowed(request)) {
            return;
        }

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, origin);
        if (allowCredentials) {
            response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER, "true");
        }

        if (preflightMaxAge > 0) {
            response.setHeader(ACCESS_CONTROL_MAX_AGE_HEADER, String.valueOf(preflightMaxAge));
        }

        response.setHeader(ACCESS_CONTROL_ALLOW_METHODS_HEADER, commify(allowedMethods));
        if (allowedHeaders.contains("*")) {
            response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS_HEADER,
                    request.getHeader(ACCESS_CONTROL_REQUEST_HEADERS_HEADER));
        } else {
            response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS_HEADER, commify(this.allowedHeaders));
        }
    }

    private String commify(List<String> strings) {
        return Joiner.on(",").join(strings);
    }

    private boolean isMethodAllowed(HttpServletRequest request) {
        String accessControlRequestMethod = request.getHeader(ACCESS_CONTROL_REQUEST_METHOD_HEADER);

        boolean result = false;
        if (accessControlRequestMethod != null) {
            result = allowedMethods.contains("*") || allowedMethods.contains(accessControlRequestMethod);
        }

        return result;
    }

    private boolean areHeadersAllowed(HttpServletRequest request) {
        String accessControlRequestHeaders = request.getHeader(ACCESS_CONTROL_REQUEST_HEADERS_HEADER);

        boolean result = false;
        if (accessControlRequestHeaders != null) {
            result = true;

            if (!allowedHeaders.contains("*")) {
                String[] headers = accessControlRequestHeaders.split(",");
                for (String header : headers) {
                    boolean headerAllowed = false;
                    for (String allowedHeader : allowedHeaders) {
                        if (header.equalsIgnoreCase(allowedHeader)) {
                            headerAllowed = true;
                            break;
                        }
                    }
                    if (!headerAllowed) {
                        return false;
                    }
                }
            }
        }

        return result;
    }

    public void destroy() {
        allowedMethods.clear();
        allowedHeaders.clear();
        preflightMaxAge = 0;
        allowCredentials = false;
    }
}
