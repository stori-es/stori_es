package org.consumersunion.stories.common.client.widget.messages;

import java.util.Set;

import javax.inject.Provider;

import org.consumersunion.stories.common.client.api.TaskService;
import org.consumersunion.stories.common.client.widget.CollectionListItemFactory;
import org.consumersunion.stories.common.client.widget.ListItemHandler;
import org.consumersunion.stories.common.shared.dto.tasks.Task;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionSummary;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

import static com.google.gwt.query.client.GQuery.$;

public class AddToCollectionsMessage extends ProgressMessage
        implements ListItemHandler<CollectionSummary>, AttachEvent.Handler {
    interface Binder extends UiBinder<Widget, AddToCollectionsMessage> {
    }

    @UiField
    SpanElement count;
    @UiField
    SpanElement total;
    @UiField
    FlowPanel collections;
    @UiField
    SpanElement text;
    @UiField
    Button stop;
    @UiField
    SpanElement buttonText;

    private final Timer taskTimer;
    private final PlaceManager placeManager;
    private final CommonI18nLabels labels;
    private final ResourceDelegate<TaskService> taskServiceDelegate;
    private final int taskId;

    @Inject
    AddToCollectionsMessage(
            Binder binder,
            PlaceManager placeManager,
            final CommonI18nLabels labels,
            MessageFactory messageFactory,
            CollectionListItemFactory factory,
            Provider<AddToCollectionsMinimizedMessage> minimizedMessagesProvider,
            final ResourceDelegate<TaskService> taskServiceDelegate,
            @Assisted final int taskId,
            @Assisted Set<CollectionSummary> collectionSummaries) {
        super(binder, minimizedMessagesProvider, messageFactory);

        this.placeManager = placeManager;
        this.labels = labels;
        this.taskServiceDelegate = taskServiceDelegate;
        this.taskId = taskId;

        createAndBindUi();

        for (CollectionSummary collectionSummary : collectionSummaries) {
            collections.add(factory.create(collectionSummary, this, false));
        }

        taskTimer = new Timer() {
            @Override
            public void run() {
                taskServiceDelegate.withCallback(new AsyncCallback<Task>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(Task task) {
                        updateProgress(task);

                        if (task.isDone()) {
                            cancel();
                            setToDone();
                        } else if (task.isCanceled()) {
                            buttonText.setInnerText(labels.stopped());
                            setToStopped();
                            cancel();
                        }
                    }
                }).getTask(taskId);
            }
        };
        taskTimer.scheduleRepeating(3000);

        asWidget().addAttachHandler(this);
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            $("[data-tooltip]", collections).removeClass("simptip-position-top").addClass(
                    "simptip-position-left");
        }
    }

    @Override
    public void onListItemClicked(CollectionSummary collection) {
        Integer collectionId = collection.getId();
        PlaceRequest place = new PlaceRequest.Builder()
                .nameToken("collection")
                .with("id", String.valueOf(collectionId))
                .build();

        PlaceRequest currentPlaceRequest = placeManager.getCurrentPlaceRequest();
        if (!currentPlaceRequest.hasSameNameToken(place)
                || !String.valueOf(collectionId).equals(currentPlaceRequest.getParameter("id", ""))) {
            placeManager.revealRelativePlace(place);
        }
    }

    @Override
    public void onListItemRemoved(CollectionSummary collection) {
        // Do nothing
    }

    @Override
    protected void setToDone() {
        super.setToDone();

        setToStopped();
        stop.removeFromParent();
    }

    @Override
    public void updateProgress(int count, int total) {
        super.updateProgress(count, total);

        this.count.setInnerText(String.valueOf(count));
        this.total.setInnerText(String.valueOf(total));
    }

    @UiHandler("stop")
    void onStopClicked(ClickEvent e) {
        stop.setEnabled(false);
        buttonText.setInnerText(labels.stopping());

        taskServiceDelegate.withCallback(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(Void result) {
            }
        }).cancelTask(taskId);
    }

    private void setToStopped() {
        stop.setEnabled(false);
        text.setInnerText(labels.added());
        $(progress).addClass(resources.generalStyleCss().progressDone());
    }

    private void updateProgress(Task result) {
        int count = result.getCount();
        int total = result.getTotal();

        updateProgress(count, total);
    }
}
