package org.consumersunion.stories.dashboard.client.application.ui.token;

import com.google.gwt.cell.client.ActionCell.Delegate;

public interface TokenCellFactory {
    TokenCell create(Delegate<String> delegate);
}
