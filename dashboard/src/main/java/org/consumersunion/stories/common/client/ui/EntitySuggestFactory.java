package org.consumersunion.stories.common.client.ui;

import org.consumersunion.stories.common.shared.model.HasTitle;

import com.google.gwt.user.client.ui.SuggestOracle;

public interface EntitySuggestFactory<T extends HasTitle> {
    EntitySuggest<T> create(SuggestOracle suggestOracle);
}
