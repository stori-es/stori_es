package org.consumersunion.stories.dashboard.client.application.collection.widget.chart;

import java.util.Date;
import java.util.Map;

import org.consumersunion.stories.common.client.i18n.CommonI18nMessages;
import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.shared.dto.StoriesCountByDate;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.inject.Inject;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataColumn;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.RoleType;
import com.googlecode.gwt.charts.client.corechart.LineChart;
import com.googlecode.gwt.charts.client.corechart.LineChartOptions;
import com.googlecode.gwt.charts.client.options.AnnotationTextStyle;
import com.googlecode.gwt.charts.client.options.Annotations;
import com.googlecode.gwt.charts.client.options.ColorAxis;
import com.googlecode.gwt.charts.client.options.Legend;
import com.googlecode.gwt.charts.client.options.LegendPosition;
import com.gwtplatform.mvp.client.ViewImpl;

public class TimelineView extends ViewImpl implements TimelinePresenter.MyView {
    interface Binder extends UiBinder<Widget, TimelineView> {
    }

    @UiField
    LineChart chart;

    private final DataTable dataTable;
    private final LineChartOptions chartOptions;
    private final CommonI18nMessages messages;
    private final StoryTellerDashboardI18nLabels labels;

    @Inject
    TimelineView(
            Binder binder,
            StoryTellerDashboardI18nLabels labels,
            CommonI18nMessages messages) {
        initWidget(binder.createAndBindUi(this));

        this.messages = messages;
        this.labels = labels;

        dataTable = DataTable.create();
        dataTable.addColumn(ColumnType.STRING, "Date");
        dataTable.addColumn(ColumnType.NUMBER, "Stories");
        dataTable.addColumn(DataColumn.create(ColumnType.STRING, RoleType.ANNOTATION));
        dataTable.addColumn(DataColumn.create(ColumnType.STRING, RoleType.ANNOTATIONTEXT));

        chartOptions = createOptions();
        chart.draw(dataTable, chartOptions);
    }

    @Override
    public void displayData(Date publishedDate, Date modifiedDate, StoriesCountByDate countByDate) {
        if (dataTable.getNumberOfRows() > 0) {
            dataTable.removeRows(0, dataTable.getNumberOfRows() - 1);
        }

        Map<Date, Integer> countByDateMap = countByDate.getCountByDate();
        ensureDatePresent(countByDateMap, publishedDate);
        ensureDatePresent(countByDateMap, modifiedDate);

        dataTable.addRows(countByDateMap.size());

        int row = 0;

        for (Map.Entry<Date, Integer> dateEntrySet : countByDateMap.entrySet()) {
            Date date = dateEntrySet.getKey();
            setBasicValues(row, date, dateEntrySet.getValue());

            if (isSameDate(date, publishedDate)) {
                setAnnotationValue(row, labels.published(), messages.publishedDateTime(publishedDate));
            }
            if (isSameDate(date, modifiedDate)) {
                setAnnotationValue(row, labels.modified(), messages.modifiedDateTime(modifiedDate));
            }
            row++;
        }

        chart.draw(dataTable, chartOptions);
    }

    private boolean isSameDate(Date date1, Date date2) {
        return date1 != null && date2 != null && CalendarUtil.isSameDate(date1, date2);
    }

    private void ensureDatePresent(final Map<Date, Integer> countByDateMap, final Date date) {
        if (date != null) {
            boolean present = Iterables.tryFind(countByDateMap.keySet(), new Predicate<Date>() {
                @Override
                public boolean apply(Date input) {
                    return isSameDate(date, input);
                }
            }).isPresent();

            if (!present) {
                countByDateMap.put(date, 0);
            }
        }
    }

    private void setAnnotationValue(int row, String label, String tooltip) {
        dataTable.setValue(row, 2, label);
        dataTable.setValue(row, 3, tooltip);
    }

    private void setBasicValues(int row, Date date, Integer value) {
        dataTable.setValue(row, 0, messages.shortDate(date));
        dataTable.setValue(row, 1, value);
    }

    private LineChartOptions createOptions() {
        LineChartOptions chartOptions = LineChartOptions.create();
        chartOptions.setHeight(ChartModule.CHART_HEIGHT);
        chartOptions.setWidth(ChartModule.CHART_WIDTH);

        chartOptions.setLegend(createLegend());

        AnnotationTextStyle annotationTextStyle = AnnotationTextStyle.create();
        annotationTextStyle.setFontSize(12);
        annotationTextStyle.setColor("#ff7a22");
        annotationTextStyle.setBold(true);

        Annotations annotations = Annotations.create();
        annotations.setTextStyle(annotationTextStyle);
        chartOptions.setAnnotations(annotations);

        ColorAxis colorAxis = ColorAxis.create();
        colorAxis.setColors("#7cb5ec");

        return chartOptions;
    }

    private Legend createLegend() {
        Legend legend = Legend.create();
        legend.setPosition(LegendPosition.NONE);

        return legend;
    }
}
