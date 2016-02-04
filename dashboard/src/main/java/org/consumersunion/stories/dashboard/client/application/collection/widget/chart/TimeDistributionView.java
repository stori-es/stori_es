package org.consumersunion.stories.dashboard.client.application.collection.widget.chart;

import org.consumersunion.stories.common.shared.dto.StoriesCountByDayAndTime;

import com.google.common.base.MoreObjects;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.corechart.BubbleChart;
import com.googlecode.gwt.charts.client.corechart.BubbleChartOptions;
import com.googlecode.gwt.charts.client.options.ColorAxis;
import com.googlecode.gwt.charts.client.options.HAxis;
import com.googlecode.gwt.charts.client.options.Legend;
import com.googlecode.gwt.charts.client.options.LegendPosition;
import com.googlecode.gwt.charts.client.options.SizeAxis;
import com.googlecode.gwt.charts.client.options.Tick;
import com.googlecode.gwt.charts.client.options.VAxis;
import com.gwtplatform.mvp.client.ViewImpl;

public class TimeDistributionView extends ViewImpl implements TimeDistributionPresenter.MyView {
    interface Binder extends UiBinder<Widget, TimeDistributionView> {
    }

    // Map the day of week to the correct index for the chart
    private static final Integer[] DAYS_OF_WEEK = new Integer[]{1, 7, 6, 5, 4, 3, 2};
    private static final String[] DAYS_OF_WEEK_LABEL = new String[]{"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
    private static final String AM = "AM";
    private static final String PM = "PM";

    @UiField
    BubbleChart chart;

    private final DataTable dataTable;
    private final BubbleChartOptions chartOptions;

    @Inject
    TimeDistributionView(Binder binder) {
        initWidget(binder.createAndBindUi(this));

        dataTable = DataTable.create();
        dataTable.addColumn(ColumnType.STRING, "ID");
        dataTable.addColumn(ColumnType.NUMBER, "Time of day");
        dataTable.addColumn(ColumnType.NUMBER, "Day of week");
        dataTable.addColumn(ColumnType.STRING, "");
        dataTable.addColumn(ColumnType.NUMBER, "Stories");

        chartOptions = createOptions();
        chart.draw(dataTable, chartOptions);
    }

    @Override
    public void displayData(StoriesCountByDayAndTime countByDayAndTime) {
        if (dataTable.getNumberOfRows() > 0) {
            dataTable.removeRows(0, dataTable.getNumberOfRows() - 1);
        }

        dataTable.addRows(168);

        int maxValue = 0;
        for (int dayOfWeek = 1; dayOfWeek <= 7; dayOfWeek++) {
            int offset = (dayOfWeek - 1) * 24;
            for (int hourOfDay = 1; hourOfDay <= 24; hourOfDay++) {
                int rowIndex = offset + hourOfDay - 1;
                dataTable.setValue(rowIndex, 0, "");
                dataTable.setValue(rowIndex, 1, hourOfDay);
                dataTable.setFormattedValue(rowIndex, 1, formatTime(hourOfDay - 1));
                dataTable.setValue(rowIndex, 2, DAYS_OF_WEEK[dayOfWeek - 1]);
                dataTable.setFormattedValue(rowIndex, 2, DAYS_OF_WEEK_LABEL[dayOfWeek - 1]);
                dataTable.setValue(rowIndex, 3, "");

                Integer value = MoreObjects.firstNonNull(countByDayAndTime.get(dayOfWeek, hourOfDay), 0);
                dataTable.setValue(rowIndex, 4, value);

                if (value > maxValue) {
                    maxValue = value;
                }
            }
        }

        SizeAxis sizeAxis = SizeAxis.create();
        sizeAxis.setMinValue(0);
        sizeAxis.setMaxValue(maxValue);
        sizeAxis.setMinSize(1);
        sizeAxis.setMaxSize(13);
        chartOptions.setSizeAxis(sizeAxis);
        chart.draw(dataTable, chartOptions);
    }

    private String formatTime(int hourOfDay) {
        String suffix;
        if (hourOfDay < 12) {
            if (hourOfDay == 0) {
                hourOfDay = 12;
            }
            suffix = AM;
        } else {
            if (hourOfDay > 12) {
                hourOfDay -= 12;
            }
            suffix = PM;
        }

        return String.valueOf(hourOfDay) + suffix;
    }

    private BubbleChartOptions createOptions() {
        BubbleChartOptions chartOptions = BubbleChartOptions.create();
        chartOptions.setHeight(ChartModule.CHART_HEIGHT);
        chartOptions.setWidth(ChartModule.CHART_WIDTH);

        chartOptions.setLegend(createLegend());
        chartOptions.setHAxis(createHAxis());
        chartOptions.setVAxis(createVAxis());

        ColorAxis colorAxis = ColorAxis.create();
        colorAxis.setColors("red");
        chartOptions.setColorAxis(colorAxis);

        return chartOptions;
    }

    private Legend createLegend() {
        Legend legend = Legend.create();
        legend.setPosition(LegendPosition.NONE);

        return legend;
    }

    private VAxis createVAxis() {
        VAxis vAxis = VAxis.create();

        vAxis.setTitle("Day of Week");
        vAxis.setMinValue(0.5);
        vAxis.setMaxValue(7.5);
        createVAxisTicks(vAxis);

        return vAxis;
    }

    private void createVAxisTicks(VAxis vAxis) {
        Tick[] ticks = new Tick[7];
        for (int i = 0; i < 7; i++) {
            ticks[i] = createTick(i + 1, DAYS_OF_WEEK_LABEL[DAYS_OF_WEEK[i] - 1]);
        }
        vAxis.setTicks(ticks);
    }

    private HAxis createHAxis() {
        HAxis hAxis = HAxis.create();

        hAxis.setTitle("Time of Day");
        hAxis.setMinValue(0);
        hAxis.setMaxValue(24.5);
        createHAxisTicks(hAxis);

        return hAxis;
    }

    private void createHAxisTicks(HAxis hAxis) {
        hAxis.setTicks(createTick(1, "12AM"), createTick(2, ""), createTick(3, ""), createTick(4, "3AM"),
                createTick(5, ""), createTick(6, ""), createTick(7, "6AM"), createTick(8, ""), createTick(9, ""),
                createTick(10, "9AM"), createTick(11, ""), createTick(12, ""), createTick(13, "12PM"),
                createTick(14, ""), createTick(15, ""), createTick(16, "3PM"), createTick(17, ""), createTick(18, ""),
                createTick(19, "6PM"), createTick(20, ""), createTick(21, ""), createTick(22, "9PM"),
                createTick(23, ""), createTick(24, ""));
    }

    private Tick createTick(int v, String f) {
        Tick tick = Tick.create();

        tick.setV(v);
        tick.setF(f);

        return tick;
    }
}
