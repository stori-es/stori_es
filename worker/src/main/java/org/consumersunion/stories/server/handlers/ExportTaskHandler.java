package org.consumersunion.stories.server.handlers;

import javax.inject.Inject;

import org.consumersunion.stories.server.export.CsvExporter;
import org.consumersunion.stories.server.export.CsvExporterFactory;
import org.consumersunion.stories.common.shared.dto.tasks.ExportTask;
import org.springframework.stereotype.Component;

@Component
public class ExportTaskHandler implements Handler<ExportTask> {
    private final CsvExporterFactory exporterFactory;

    @Inject
    ExportTaskHandler(CsvExporterFactory exporterFactory) {
        this.exporterFactory = exporterFactory;
    }

    @Override
    public boolean canHandle(Object element) {
        return element instanceof ExportTask;
    }

    @Override
    public void handle(ExportTask exportTask) throws Exception {
        CsvExporter exporter = exporterFactory.create(exportTask);

        exporter.export();
    }
}
