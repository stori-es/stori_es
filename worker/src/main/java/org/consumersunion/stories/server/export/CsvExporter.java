package org.consumersunion.stories.server.export;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import org.apache.commons.io.input.ReaderInputStream;
import org.consumersunion.stories.common.shared.dto.tasks.ExportTask;
import org.consumersunion.stories.common.shared.dto.tasks.TaskStatus;
import org.consumersunion.stories.server.persistence.TaskPersister;
import org.joda.time.DateTime;
import org.supercsv.io.ICsvWriter;
import org.supercsv.prefs.CsvPreference;

import com.Ostermiller.util.CircularCharBuffer;

public abstract class CsvExporter<W extends ICsvWriter> {
    protected static final CsvPreference CSV_PREFERENCE = new CsvPreference.Builder('"', ',', "\r\n").build();

    private static final int EXPIRE_DAYS = 7;

    protected final CircularCharBuffer byteBuffer;
    protected final Writer writer;
    protected final ExportTask exportTask;
    protected final int objectId;
    protected final CsvWriterFactory csvWriterFactory;
    protected final TaskPersister taskPersister;
    protected final int profileId;

    private final String exportType;
    private final AmazonS3ExportService s3ExportService;

    protected CsvExporter(
            CsvWriterFactory csvWriterFactory,
            TaskPersister taskPersister,
            AmazonS3ExportService s3ExportService,
            ExportTask exportTask,
            String exportType) throws IOException {
        this.csvWriterFactory = csvWriterFactory;
        this.taskPersister = taskPersister;
        this.s3ExportService = s3ExportService;
        this.byteBuffer = new CircularCharBuffer(CircularCharBuffer.INFINITE_SIZE);
        this.writer = byteBuffer.getWriter();
        this.profileId = exportTask.getProfileId();
        this.exportTask = exportTask;
        this.exportType = exportType;
        this.objectId = exportTask.getObjectId();
    }

    public void export() throws IOException {
        String[] headers = getHeaders();

        W writer = prepResponse(headers);
        try {
            if (headers.length > 0) {
                taskPersister.changeStatus(exportTask, TaskStatus.RUNNING);

                exportData(writer);
                writer.close();

                String url = s3ExportService.upload(new ReaderInputStream(byteBuffer.getReader()), getFileName());
                exportTask.setUrl(url);
                exportTask.setStatus(TaskStatus.DONE);
                exportTask.setExpires(new DateTime().plusDays(EXPIRE_DAYS).toDate());

                taskPersister.update(exportTask);
            }
        } catch (Exception ignored) {
            if (!exportTask.isCanceled()) {
                exportTask.setStatus(TaskStatus.CANCELED);
                taskPersister.update(exportTask);
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    protected abstract void exportData(W writer) throws IOException;

    protected abstract String[] getHeaders();

    protected abstract W createWriter() throws IOException;

    private String getFileName() {
        return String.format("exports/stori.es-%d-%s-%3$tY_%3$tm_%3$td_%3$tH_%3$tM_%3$tS.csv", objectId, exportType,
                new Date());
    }

    private W prepResponse(String[] headers) throws IOException {
        W writer = createWriter();

        if (headers.length > 0) {
            writer.writeHeader(headers);
        }

        return writer;
    }
}
