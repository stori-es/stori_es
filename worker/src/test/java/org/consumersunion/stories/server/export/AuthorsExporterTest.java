package org.consumersunion.stories.server.export;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;

import javax.inject.Inject;

import org.consumersunion.stories.server.business_logic.ProfileService;
import org.consumersunion.stories.server.export.renderers.storyteller.StoryTellerColumnsRenderer;
import org.consumersunion.stories.server.persistence.TaskPersister;
import org.consumersunion.stories.common.shared.dto.tasks.ExportTask;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.jukito.TestSingleton;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.supercsv.prefs.CsvPreference;

import com.google.common.collect.Lists;
import com.google.inject.Provides;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class AuthorsExporterTest {
    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(TaskPersister.class);
            forceMock(AmazonS3ExportService.class);
            forceMock(StoryTellerColumnsRenderer.class);
            forceMock(ExportTask.class);
            forceMock(CsvWriterFactory.class);
        }

        @Provides
        @TestSingleton
        AuthorsExporter authorsExporter(
                ProfileService profileService,
                CsvWriterFactory csvWriterFactory,
                TaskPersister taskPersister,
                AmazonS3ExportService amazonS3ExportService,
                ExportTask exportTask,
                StoryTellerColumnsRenderer renderer) throws IOException {
            return new AuthorsExporter(profileService, csvWriterFactory, taskPersister, amazonS3ExportService,
                    exportTask, true, renderer);
        }
    }

    private static final int TOTAL_COUNT = 1000;

    @Inject
    AuthorsExporter authorsExporter;
    @Inject
    TaskPersister taskPersister;

    @Before
    public void setUp(
            StoryTellerColumnsRenderer renderer,
            CsvWriterFactory csvWriterFactory) {
        given(renderer.getColumns()).willReturn(Lists.<String>newArrayList("a", "b", "c"));
        given(renderer.render(any(StoryTellerCsv.class))).willReturn(Lists.<String>newArrayList("a", "b", "c"));
        given(csvWriterFactory.createStoryTellerCsvWriter(any(Writer.class), any(CsvPreference.class),
                any(StoryTellerColumnsRenderer.class))).willReturn(mock(StoryTellerCsvWriter.class));
    }

    @Test
    public void testExportData(
            AmazonS3ExportService exportService,
            ProfileService profileService,
            ExportTask task) throws Exception {
        // given
        given(profileService.exportStoryTellers(anyInt(), anyInt(), anyInt(), anyInt())).willAnswer(new Answer<StoryExport>() {
            @Override
            public StoryExport answer(InvocationOnMock invocation) throws Throwable {
                int page = (Integer) invocation.getArguments()[3];
                int pageSize = Math.min(100, TOTAL_COUNT - page * 100);

                return new StoryExport<StoryTellerCsv>(createStoryTellers(pageSize), TOTAL_COUNT);
            }
        });

        // when
        authorsExporter.export();

        // then
        verify(taskPersister).setTotal(eq(task));
        verify(taskPersister, atLeast(2)).updateCount(eq(task));
        verify(exportService).upload(any(InputStream.class), anyString());
    }

    private ArrayList<StoryTellerCsv> createStoryTellers(int pageSize) {
        ArrayList<StoryTellerCsv> values = new ArrayList<StoryTellerCsv>();

        for (int i = 0; i < pageSize; i++) {
            values.add(mock(StoryTellerCsv.class));
        }

        return values;
    }
}
