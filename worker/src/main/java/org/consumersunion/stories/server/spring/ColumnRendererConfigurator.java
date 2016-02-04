package org.consumersunion.stories.server.spring;

import java.util.Date;

import org.consumersunion.stories.server.annotations.DateCreated;
import org.consumersunion.stories.server.annotations.DateModified;
import org.consumersunion.stories.server.export.BaseStoryExportCsv;
import org.consumersunion.stories.server.export.renderers.DateRenderer;
import org.consumersunion.stories.server.util.ServerUrlUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gwtplatform.mvp.shared.proxy.ParameterTokenFormatter;
import com.gwtplatform.mvp.shared.proxy.TokenFormatter;

@Configuration
public class ColumnRendererConfigurator {
    @Bean
    @DateCreated
    public DateRenderer<BaseStoryExportCsv> storyDateCreatedRenderer() {
        return new DateRenderer<BaseStoryExportCsv>() {
            @Override
            protected Date getDate(BaseStoryExportCsv data) {
                return data.getCreatedDate();
            }
        };
    }

    @Bean
    @DateModified
    public DateRenderer<BaseStoryExportCsv> storyDateModifiedRenderer() {
        return new DateRenderer<BaseStoryExportCsv>() {
            @Override
            protected Date getDate(BaseStoryExportCsv data) {
                return data.getModifiedDate();
            }
        };
    }

    @Bean
    public TokenFormatter tokenFormatter() {
        return new ParameterTokenFormatter(new ServerUrlUtils());
    }
}
