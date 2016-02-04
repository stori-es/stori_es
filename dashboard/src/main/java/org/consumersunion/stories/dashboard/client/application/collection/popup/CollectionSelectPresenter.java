package org.consumersunion.stories.dashboard.client.application.collection.popup;

import java.util.List;

import org.consumersunion.stories.common.client.event.HideLoadingEvent;
import org.consumersunion.stories.common.client.event.ShowLoadingEvent;
import org.consumersunion.stories.common.client.service.RpcCollectionServiceAsync;
import org.consumersunion.stories.common.client.service.RpcOrganizationServiceAsync;
import org.consumersunion.stories.common.client.service.RpcQuestionnaireServiceAsync;
import org.consumersunion.stories.common.client.service.response.CollectionDataPagedResponse;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;

/**
 * Abstract, foundation presenter for displaying a modal pop up to select or create a
 * {@link org.consumersunion.stories.common.shared.model.Collection}. Children then do whatever it is they do with
 * the selected or created {@link org.consumersunion.stories.common.shared.model.Collection}.
 */
public abstract class CollectionSelectPresenter
        extends PresenterWidget<CollectionSelectPresenter.MyView>
        implements CollectionSelectUiHandlers {

    public interface MyView extends PopupView, HasUiHandlers<CollectionSelectUiHandlers> {
        void setData(List<CollectionData> data);

        void linkActionTitle(Boolean forQuestionnaire);

        void restoreAssignmentTitle();
    }

    protected final RpcCollectionServiceAsync collectionService;
    protected final RpcOrganizationServiceAsync organizationService;
    protected final RpcQuestionnaireServiceAsync questionnaireService;

    protected List<Integer> collectionIds;

    protected CollectionSelectPresenter(
            EventBus eventBus,
            MyView view,
            RpcCollectionServiceAsync collectionService,
            RpcOrganizationServiceAsync organizationService,
            RpcQuestionnaireServiceAsync questionnaireService) {
        super(eventBus, view);

        this.collectionService = collectionService;
        this.organizationService = organizationService;
        this.questionnaireService = questionnaireService;

        getView().setUiHandlers(this);
    }

    @Override
    public void associateNotification(List<String> collectionIds) {
        this.collectionIds = Lists.transform(collectionIds, new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return Integer.valueOf(input);
            }
        });
        associateCollections(collectionIds);
    }

    @Override
    public void createAndAssociateNotification(String title) {
        createAndAssociate(title);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        ShowLoadingEvent.fire(this);
        getView().setData(Lists.<CollectionData>newArrayList());

        loadCollections(new ResponseHandler<CollectionDataPagedResponse>() {
            @Override
            public void onFailure(Throwable e) {
                super.onFailure(e);

                hideLoading();
            }

            @Override
            public void handleSuccess(CollectionDataPagedResponse result) {
                getView().setData(result.getData());
                hideLoading();
            }
        });
    }

    protected abstract void createAndAssociate(String title);

    protected abstract void associateCollections(List<String> collectionIds);

    protected abstract void loadCollections(ResponseHandler<CollectionDataPagedResponse> callback);

    private void hideLoading() {
        HideLoadingEvent.fire(this);
    }
}
