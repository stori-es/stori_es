package org.consumersunion.stories.server.api.rest;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.consumersunion.stories.common.shared.dto.ThemeResponse;
import org.consumersunion.stories.common.shared.dto.ThemesApiResponse;
import org.consumersunion.stories.common.shared.model.Theme;
import org.consumersunion.stories.server.api.rest.converters.AbstractConverter.PendingConverter;
import org.consumersunion.stories.server.api.rest.converters.ThemeConverter;
import org.consumersunion.stories.server.business_logic.ThemeService;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;

@RunWith(JukitoRunner.class)
public class ThemeResourceTest {
    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(ThemeConverter.class);
        }
    }

    private static final int THEME_ID = 400;

    @Inject
    private ThemeResource themeResource;
    @Inject
    private ThemeService themeService;
    @Inject
    private ThemeConverter themeConverter;

    @Test
    public void getTheme() throws Exception {
        Theme theme = new Theme(THEME_ID, 1);
        PendingConverter pendingConverter = mock(PendingConverter.class);
        given(themeService.getTheme(THEME_ID)).willReturn(theme);
        given(pendingConverter.asList()).willReturn(Lists.newArrayList(ThemeResponse.builder().withId(THEME_ID).build()));
        given(themeConverter.convertAll(same(theme))).willReturn(pendingConverter);

        Response response = themeResource.getTheme(THEME_ID);

        ThemesApiResponse apiResponse = (ThemesApiResponse) response.getEntity();
        assertThat(apiResponse.getData().get(0).getId()).isEqualTo(THEME_ID);
    }
}
