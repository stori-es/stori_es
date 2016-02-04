package org.consumersunion.stories.dashboard.client.application.widget.addto;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.View;

public interface AddToView<H extends AddToUiHandlers> extends View, HasUiHandlers<H> {
    void reset();

    boolean isVisible();
}
