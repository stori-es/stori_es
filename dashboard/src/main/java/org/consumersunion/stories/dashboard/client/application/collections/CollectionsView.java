package org.consumersunion.stories.dashboard.client.application.collections;

import org.consumersunion.stories.common.client.ui.DefaultTextPagerHandler;
import org.consumersunion.stories.common.client.ui.EnhancedPager;
import org.consumersunion.stories.common.client.ui.LoadingIndicator;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class CollectionsView extends ViewWithUiHandlers<CollectionsUiHandlers>
        implements CollectionsPresenter.MyView, EnhancedPager.Handler {
    interface Binder extends UiBinder<Widget, CollectionsView> {
    }

    @UiField(provided = true)
    final EnhancedPager pager;
    @UiField(provided = true)
    final LoadingIndicator loadingIndicator;

    @UiField
    HTMLPanel collectionsContainer;
    @UiField
    SimplePanel searchWidget;
    @UiField
    SimplePanel newPanel;
    @UiField
    Button createNewCollection;
    @UiField
    Button createNewQuestionnaire;

    private HandlerRegistration handlerRegistration;

    @Inject
    CollectionsView(
            Binder uiBinder,
            LoadingIndicator loadingIndicator,
            EnhancedPager pager) {
        this.loadingIndicator = loadingIndicator;
        this.pager = pager;

        initWidget(uiBinder.createAndBindUi(this));

        createNewCollection.getElement()
                .setAttribute("data-tooltip", "Create a group of related stories and information on specific topics");
        createNewQuestionnaire.getElement()
                .setAttribute("data-tooltip",
                        "Collect information from others who have had experiences about specific topics");
    }

    @Override
    public void hideLoading() {
        loadingIndicator.setVisible(false);
        collectionsContainer.setVisible(true);
    }

    @Override
    public void onGoToPage(Integer pageNumber) {
        getUiHandlers().goToPage(pageNumber);
    }

    @Override
    public void clearCollections() {
        collectionsContainer.clear();
        collectionsContainer.setVisible(false);
        loadingIndicator.setVisible(true);
    }

    @Override
    public void setupPager() {
        pager.getPager().setHandler(new DefaultTextPagerHandler() {
            @Override
            public void onPageChanged() {
                onGoToPage(pager.getPager().getPage() + 1);
            }
        });
        pager.setHandler(this);

        pager.getPager().setDisplay(getUiHandlers().getRowHandler());
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == CollectionsPresenter.SLOT_SEARCH) {
            searchWidget.setWidget(content);
        } else if (slot == CollectionsPresenter.SLOT_NEW) {
            newPanel.setWidget(content);
            handleCardsVisibility(content);
        }
    }

    @Override
    public void addToSlot(Object slot, IsWidget content) {
        if (slot == CollectionsPresenter.SLOT_COLLECTIONS) {
            collectionsContainer.add(content);
        }
    }

    @UiHandler("createNewCollection")
    void onCreateNewCollection(ClickEvent event) {
        getUiHandlers().addNewCollection();
    }

    @UiHandler("createNewQuestionnaire")
    void onCreateNewQuestionnaire(ClickEvent event) {
        getUiHandlers().addNewQuestionnaire();
    }

    private void handleCardsVisibility(IsWidget content) {
        loadingIndicator.setVisible(false);
        collectionsContainer.setVisible(false);
        handlerRegistration = content.asWidget().addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent event) {
                if (!event.isAttached()) {
                    handlerRegistration.removeHandler();
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            newPanel.setWidget(null);
                            collectionsContainer.setVisible(true);
                        }
                    });
                }
            }
        });
    }
}
