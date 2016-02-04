package org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab;

import java.util.List;

import org.consumersunion.stories.common.client.service.RpcThemeServiceAsync;
import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.Theme;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.dashboard.client.application.collection.CollectionObserver;
import org.consumersunion.stories.dashboard.client.event.DisplayEvent;
import org.consumersunion.stories.dashboard.client.event.SavedCollectionEvent;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

/**
 * Presenter handling the edit of a Questionnaire's questions and theme
 */
public class ContentTabPresenter extends PresenterWidget<ContentTabPresenter.MyView>
        implements ContentTabUiHandlers, CollectionObserver.CollectionHandler<Collection> {

    public interface MyView extends View, HasUiHandlers<ContentTabUiHandlers> {
        void setContent(Collection collection, boolean readOnly);

        void setThemeList(List<Theme> themes);

        void setSelectedTheme(int themeId);

        void refreshView(Collection collection, boolean readOnly);
    }

    private final CollectionObserver collectionObserver;
    private final RpcThemeServiceAsync themeService;

    private Integer organizationId;

    @Inject
    ContentTabPresenter(
            EventBus eventBus,
            MyView view,
            CollectionObserver collectionObserver,
            RpcThemeServiceAsync themeService) {
        super(eventBus, view);

        this.collectionObserver = collectionObserver;
        this.themeService = themeService;

        getView().setUiHandlers(this);
    }

    @Override
    public void onDisplay(DisplayEvent<Collection> event) {
        Collection collection = event.get();
        init(collection);
    }

    @Override
    public void onCollectionSaved(SavedCollectionEvent event) {
        Collection collection = event.get();
        getView().refreshView(collection, collection.isPublished());
    }

    @Override
    public void save(List<Block> blocks) {
        Collection collection = collectionObserver.getCollection();
        collection.setBlocks(blocks);
        collectionObserver.save();
    }

    @Override
    public void updateTheme(int theme) {
        Collection collection = collectionObserver.getCollection();
        collection.setTheme(theme);
        collectionObserver.save();
    }

    private void init(Collection collection) {
        organizationId = collection.getOwner();

        loadThemes();

        getView().setContent(collection, collection.isPublished());
    }

    private void loadThemes() {
        themeService.getThemesForOrganization(organizationId,
                new ResponseHandler<DataResponse<Theme>>() {
                    @Override
                    public void handleSuccess(DataResponse<Theme> result) {
                        getView().setThemeList(result.getData());

                        setSelectedTheme();
                    }
                }
        );
    }

    private void setSelectedTheme() {
        Collection collection = collectionObserver.getCollection();

        getView().setSelectedTheme(collection.getTheme());
    }
}
