package org.consumersunion.stories.server.export.renderers;

import java.util.Date;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public abstract class DateRenderer<T> implements ColumnRenderer<T> {
    private static final DateTimeFormatter dateFormat = ISODateTimeFormat.dateTimeNoMillis();

    @Override
    public final String render(T data) {
        return dateFormat.print(getDate(data).getTime());
    }

    protected abstract Date getDate(T data);
}
