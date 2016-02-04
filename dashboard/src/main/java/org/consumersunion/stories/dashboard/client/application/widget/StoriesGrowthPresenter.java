package org.consumersunion.stories.dashboard.client.application.widget;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.consumersunion.stories.common.client.event.HideLoadingEvent;
import org.consumersunion.stories.common.client.event.ShowLoadingEvent;
import org.consumersunion.stories.common.client.service.RpcMetricsServiceAsync;
import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.metrics.ItemCountByDate;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.corechart.AreaChart;
import com.googlecode.gwt.charts.client.corechart.AreaChartOptions;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class StoriesGrowthPresenter extends PresenterWidget<StoriesGrowthPresenter.MyView> {
    interface MyView extends View {
        void displayChart(AreaChart chart);
    }

    private final RpcMetricsServiceAsync metricsService;
    private final CommonI18nLabels labels;
    private final Map<String, List<ItemCountByDate>> rawData;

    @Inject
    StoriesGrowthPresenter(
            EventBus eventBus,
            MyView view,
            CommonI18nLabels labels,
            RpcMetricsServiceAsync metricsService) {
        super(eventBus, view);

        this.labels = labels;
        this.metricsService = metricsService;
        rawData = new HashMap<String, List<ItemCountByDate>>();
    }

    public void createChart() {
        launchGraphVisualization();
    }

    private void launchGraphVisualization() {
        ChartLoader chartLoader = new ChartLoader(ChartPackage.CORECHART);
        chartLoader.loadApi(new Runnable() {
            @Override
            public void run() {
                fetchData();
            }
        });
    }

    private void fetchData() {
        rawData.clear();
        ShowLoadingEvent.fire(this);

        metricsService.getStoryCountByCreationDate(new Date(), new Date(),
                new ResponseHandler<DataResponse<ItemCountByDate>>() {
                    @Override
                    public void handleSuccess(final DataResponse<ItemCountByDate> result) {
                        rawData.put(labels.stories(), result.getData());
                        processResponse();
                    }
                });

        metricsService.getUpdatesOptInCountByCreationDate(new Date(), new Date(),
                new ResponseHandler<DataResponse<ItemCountByDate>>() {
                    @Override
                    public void handleSuccess(final DataResponse<ItemCountByDate> result) {
                        rawData.put(labels.optIn(), result.getData());
                        processResponse();
                    }
                });
    }

    private void processResponse() {
        if (rawData.size() == 2) {
            drawGraph(rawData);
        }
    }

    private void drawChart(AreaChart chart) {
        getView().displayChart(chart);
        HideLoadingEvent.fire(this);
    }

    private void drawGraph(Map<String, List<ItemCountByDate>> rawData) {
        DataTable dataTable = DataTable.create();
        dataTable.addColumn(ColumnType.DATE, labels.date());

        for (Map.Entry<String, List<ItemCountByDate>> entry : rawData.entrySet()) {
            addDataSet(entry.getKey(), entry.getValue(), dataTable);
        }

        AreaChart chart = new AreaChart();
        chart.draw(dataTable, createOptions());
        drawChart(chart);
    }

    private void addDataSet(String label, List<ItemCountByDate> metrics, DataTable dataTable) {
        int colIndex = dataTable.addColumn(ColumnType.NUMBER, label);

        for (ItemCountByDate metric : metrics) {
            int i = dataTable.addRow();
            dataTable.setValue(i, 0, metric.getStartDate());
            dataTable.setValue(i, colIndex, metric.getItemCount());
        }
    }

    private AreaChartOptions createOptions() {
        AreaChartOptions opt = AreaChartOptions.create();
        opt.setHeight(400);
        opt.setWidth(600);

        return opt;
    }
}
