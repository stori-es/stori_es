package org.consumersunion.stories.dashboard.client.application.collection.widget.collectionsbyquestionnaire;

import java.util.List;

import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionSummary;
import org.consumersunion.stories.dashboard.client.application.collection.ui.AttachedCollectionCellFactory;
import org.consumersunion.stories.dashboard.client.application.ui.ConfirmationModal;
import org.consumersunion.stories.dashboard.client.resource.QuestionnaireListStyle;

import com.google.common.base.Function;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class CollectionsByQuestionnaireView extends ViewWithUiHandlers<CollectionsByQuestionnaireUiHandlers>
        implements CollectionsByQuestionnairePresenter.MyView {
    interface Binder extends UiBinder<Widget, CollectionsByQuestionnaireView> {
    }

    @UiField(provided = true)
    final CellList<CollectionSummary> collectionsList;

    private final ListDataProvider<CollectionSummary> dataProvider;

    @Inject
    CollectionsByQuestionnaireView(
            Binder uiBinder,
            ListDataProvider<CollectionSummary> dataProvider,
            AttachedCollectionCellFactory attachedCollectionCellFactory,
            QuestionnaireListStyle cellListStyle) {
        this.dataProvider = dataProvider;
        this.collectionsList = new CellList<CollectionSummary>(attachedCollectionCellFactory.create(setupDetailAction(),
                setupRemoveAction(), setupCanDelete()), cellListStyle);

        initWidget(uiBinder.createAndBindUi(this));
        dataProvider.addDataDisplay(collectionsList);
    }

    @Override
    public void setData(List<CollectionSummary> data) {
        dataProvider.getList().clear();
        dataProvider.getList().addAll(data);
        dataProvider.refresh();
    }

    @Override
    public void clear() {
        collectionsList.setRowCount(0);
    }

    @Override
    public void onTargetCollectionRemoved(CollectionSummary collection) {
        dataProvider.getList().remove(collection);
        dataProvider.refresh();
    }

    @Override
    public void updateCollection(Collection collection) {
        int i = 0;
        List<CollectionSummary> data = dataProvider.getList();
        for (CollectionSummary collectionSummary : data) {
            if (collectionSummary.getId() == collection.getId()) {
                data.set(i, new CollectionSummary(collection));
                dataProvider.refresh();
                break;
            }
            i++;
        }
    }

    private ActionCell.Delegate<CollectionSummary> setupDetailAction() {
        return new ActionCell.Delegate<CollectionSummary>() {
            @Override
            public void execute(CollectionSummary collectionSummary) {
                getUiHandlers().collectionDetails(collectionSummary.getId());
            }
        };
    }

    private ActionCell.Delegate<CollectionSummary> setupRemoveAction() {
        return new ActionCell.Delegate<CollectionSummary>() {
            @Override
            public void execute(CollectionSummary collectionSummary) {
                removeTargetCollection(collectionSummary);
            }
        };
    }

    private void removeTargetCollection(final CollectionSummary collection) {
        final ConfirmationModal confirmationModal = new ConfirmationModal("Confirm Deletion",
                "Are you sure you would like to remove the target " + getCollectionOrQuestionnaire(collection) + " \""
                        + collection.getTitle() + "\"?") {
            @Override
            protected void handleConfirm() {
                getUiHandlers().removeTargetCollection(collection);
            }
        };
        confirmationModal.center();
    }

    private String getCollectionOrQuestionnaire(CollectionSummary collection) {
        return collection.isQuestionnaire() ? "questionnaire" : "collection";
    }

    private Function<CollectionSummary, Boolean> setupCanDelete() {
        return new Function<CollectionSummary, Boolean>() {
            @Override
            public Boolean apply(CollectionSummary input) {
                return getUiHandlers().canRemoveCollection(input);
            }
        };
    }
}
