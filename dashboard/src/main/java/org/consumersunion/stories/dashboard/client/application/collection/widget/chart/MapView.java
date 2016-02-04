package org.consumersunion.stories.dashboard.client.application.collection.widget.chart;

import java.util.Map;

import org.consumersunion.stories.common.shared.dto.StoriesCountByState;
import org.consumersunion.stories.common.shared.util.StatesUtil;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.geochart.GeoChart;
import com.googlecode.gwt.charts.client.geochart.GeoChartColorAxis;
import com.googlecode.gwt.charts.client.geochart.GeoChartLegend;
import com.googlecode.gwt.charts.client.geochart.GeoChartOptions;
import com.googlecode.gwt.charts.client.options.DisplayMode;
import com.googlecode.gwt.charts.client.options.Legend;
import com.googlecode.gwt.charts.client.options.Resolution;
import com.googlecode.gwt.charts.client.options.TextStyle;
import com.gwtplatform.mvp.client.ViewImpl;

public class MapView extends ViewImpl implements MapPresenter.MyView {
    interface Binder extends UiBinder<Widget, MapView> {
    }

    @UiField
    GeoChart chart;

    private final DataTable dataTable;
    private final GeoChartOptions chartOptions;

    @Inject
    MapView(Binder binder) {
        initWidget(binder.createAndBindUi(this));

        dataTable = DataTable.create();
        dataTable.addColumn(ColumnType.STRING, "State");
        dataTable.addColumn(ColumnType.NUMBER, "Stories");

        chartOptions = createOptions();
        chart.draw(dataTable, chartOptions);
    }

    @Override
    public void displayData(StoriesCountByState countByState) {
        if (dataTable.getNumberOfRows() > 0) {
            dataTable.removeRows(0, dataTable.getNumberOfRows() - 1);
        }

        initStates(countByState);

        Map<String, Integer> countByStateMap = countByState.getCountByState();
        dataTable.addRows(countByStateMap.size());

        int row = 0;

        for (Map.Entry<String, Integer> dateEntrySet : countByStateMap.entrySet()) {
            String state = StatesUtil.getStateName(dateEntrySet.getKey());
            dataTable.setValue(row, 0, state);
            dataTable.setValue(row, 1, dateEntrySet.getValue());
            row++;
        }

        chart.draw(dataTable, chartOptions);
    }

    private void initStates(StoriesCountByState countByState) {
        for (String code : StatesUtil.getCodes()) {
            countByState.add(code, 0);
        }
    }

    private GeoChartOptions createOptions() {
        GeoChartOptions chartOptions = GeoChartOptions.create();
        chartOptions.setHeight(ChartModule.CHART_HEIGHT);
        chartOptions.setWidth(ChartModule.CHART_WIDTH);

        chartOptions.setLegend(createLegend().<GeoChartLegend>cast());
        chartOptions.setRegion("US");
        chartOptions.setDisplayMode(DisplayMode.REGIONS);
        chartOptions.setResolution(Resolution.PROVINCES);

        GeoChartColorAxis colorAxis = GeoChartColorAxis.create();
        colorAxis.setColors("#7cb5ec");
        chartOptions.setColorAxis(colorAxis);

        return chartOptions;
    }

    private Legend createLegend() {
        Legend legend = Legend.create();

        TextStyle textStyle = TextStyle.create();
        textStyle.setColor("black");
        textStyle.setFontSize(16);
        textStyle.setBold(true);

        legend.setTextStyle(textStyle);

        return legend;
    }
}
