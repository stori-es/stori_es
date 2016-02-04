package org.consumersunion.stories.server.api.rest.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

import org.consumersunion.stories.common.shared.api.EndPoints;
import org.consumersunion.stories.common.shared.dto.ResourceLink;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

@Component
public class ResourceLinksHelperImpl implements ResourceLinksHelper {
    private final Provider<HttpServletRequest> httpRequestProvider;
    private final static Pattern idPattern = Pattern.compile("/([0-9]+)($|\\?)");

    @Inject
    ResourceLinksHelperImpl(Provider<HttpServletRequest> httpRequestProvider) {
        this.httpRequestProvider = httpRequestProvider;
    }

    @Override
    public List<ResourceLink> replaceIds(final String baseUrl, Iterable<? extends SystemEntity> entities) {
        final String host = getHostUrl();
        return FluentIterable.from(entities)
                .transform(new Function<SystemEntity, ResourceLink>() {
                    @Nullable
                    @Override
                    public ResourceLink apply(SystemEntity input) {
                        return replaceId(host, baseUrl, input.getId());
                    }
                }).toList();
    }

    @Override
    public List<ResourceLink> replaceIntIds(final String baseUrl, Iterable<Integer> ids) {
        final String host = getHostUrl();
        return FluentIterable.from(ids)
                .transform(new Function<Integer, ResourceLink>() {
                    @Nullable
                    @Override
                    public ResourceLink apply(Integer input) {
                        return replaceId(host, baseUrl, input);
                    }
                }).toList();
    }

    @Override
    public List<ResourceLink> replaceStringIds(final String baseUrl, Iterable<String> ids) {
        final String host = getHostUrl();
        return FluentIterable.from(ids)
                .transform(new Function<String, ResourceLink>() {
                    @Nullable
                    @Override
                    public ResourceLink apply(String id) {
                        return replaceId(host, baseUrl, String.valueOf(id));
                    }
                }).toList();
    }

    @Override
    @Nullable
    public ResourceLink replaceId(String baseUrl, Integer id) {
        if (id == null) {
            return null;
        } else {
            String host = getHostUrl();
            return replaceId(host, baseUrl, id);
        }
    }

    private ResourceLink replaceId(String host, String baseUrl, int id) {
        return replaceId(host, baseUrl, String.valueOf(id));
    }

    private ResourceLink replaceId(String host, String baseUrl, String id) {
        return new ResourceLink(host + baseUrl.replace(EndPoints.ID, id));
    }

    private String getHostUrl() {
        HttpServletRequest httpRequest = httpRequestProvider.get();

        if (httpRequest.getServerPort() == 80 || httpRequest.getServerPort() == 443) {
            return httpRequest.getScheme() + "://"
                    + httpRequest.getServerName();
        } else {
            return httpRequest.getScheme() + "://"
                    + httpRequest.getServerName() + ":"
                    + httpRequest.getServerPort();
        }
    }

    public static Integer extractId(String url) {
        Matcher m = idPattern.matcher(url);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        } else {
            return null;
        }
    }
}
