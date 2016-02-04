package org.consumersunion.stories.common.client.ui;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.shared.model.HasTitle;

import com.google.gwt.user.client.ui.SuggestOracle;

public abstract class EntitySuggestionOracle<T extends HasTitle> extends SuggestOracle {
    public interface EntitySuggestionHandler<T extends HasTitle> {
        void onEntitySelected(T entity);
    }

    public static class EntitySuggestion<T extends HasTitle> implements Suggestion {
        private String displayString;
        private String replacementString;
        private T entity;

        EntitySuggestion() {
        }

        public EntitySuggestion(T entity) {
            displayString = entity.getTitle();
            replacementString = entity.getTitle();

            this.entity = entity;
        }

        public T getEntity() {
            return entity;
        }

        @Override
        public String getDisplayString() {
            return displayString;
        }

        @Override
        public String getReplacementString() {
            return replacementString;
        }
    }

    private Request lastRequest;
    private boolean pendingRequest;

    @Override
    public final void requestSuggestions(Request request, Callback callback) {
        lastRequest = request;

        if (!pendingRequest) {
            getSuggestions(callback);
        }
    }

    protected abstract void sendRequest(Request request, Callback callback);

    protected final void addSuggestions(List<EntitySuggestion<T>> entities, Request request, Callback callback) {
        List<EntitySuggestion<T>> lastSuggestions = entities;
        pendingRequest = false;

        callback.onSuggestionsReady(request, new Response(lastSuggestions));
    }

    private void getSuggestions(final Callback callback) {
        String query = lastRequest.getQuery();

        if (query.length() < 1) {
            callback.onSuggestionsReady(lastRequest, new Response(new ArrayList<EntitySuggestion>()));
        } else {
            sendRequest(lastRequest, callback);
        }
    }
}
