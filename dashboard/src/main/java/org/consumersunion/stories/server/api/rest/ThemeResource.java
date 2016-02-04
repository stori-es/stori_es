package org.consumersunion.stories.server.api.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.consumersunion.stories.common.shared.api.EndPoints;
import org.consumersunion.stories.common.shared.api.UrlParameters;
import org.consumersunion.stories.common.shared.dto.ThemesApiResponse;
import org.consumersunion.stories.common.shared.model.Theme;
import org.consumersunion.stories.server.api.rest.converters.ThemeConverter;
import org.consumersunion.stories.server.business_logic.ThemeService;
import org.springframework.stereotype.Component;

@Component
@Path(EndPoints.THEMES)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ThemeResource {
    private final ThemeService themeService;
    private final ThemeConverter themeConverter;

    @Inject
    ThemeResource(
            ThemeService themeService,
            ThemeConverter themeConverter) {
        this.themeService = themeService;
        this.themeConverter = themeConverter;
    }

    @GET
    @Path(EndPoints.ID)
    public Response getTheme(@PathParam(UrlParameters.ID) int id) {
        Theme theme = themeService.getTheme(id);

        ThemesApiResponse themesApiResponse = new ThemesApiResponse();
        themesApiResponse.setData(themeConverter.convertAll(theme).asList());

        return Response.ok(themesApiResponse).build();
    }
}
