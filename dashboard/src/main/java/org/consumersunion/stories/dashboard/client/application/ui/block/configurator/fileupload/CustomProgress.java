package org.consumersunion.stories.dashboard.client.application.ui.block.configurator.fileupload;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import gwtupload.client.HasProgress;

public class CustomProgress extends FlowPanel implements HasProgress {
    private final SimplePanel statusBar;

    public CustomProgress() {
        statusBar = new SimplePanel();
        this.setStyleName("prgbar-back");
        this.add(statusBar);
        statusBar.setStyleName("prgbar-done");
        statusBar.setWidth("0px");
    }

    @Override
    public void setProgress(long done, long total) {
        int width = this.getElement().getClientWidth();
        double percentage = total == 0 ? 0 : (double) done / (double) total;
        int pixel = (int) (percentage * (double) width);
        statusBar.setWidth(pixel + "px");
    }
}
