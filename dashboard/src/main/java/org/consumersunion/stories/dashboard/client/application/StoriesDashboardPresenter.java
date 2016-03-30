package org.consumersunion.stories.dashboard.client.application;

import java.util.Map;

import org.consumersunion.stories.common.client.event.ClearContentEvent;
import org.consumersunion.stories.common.client.service.RpcProfileServiceAsync;
import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.dashboard.client.application.widget.HeaderPresenter;
import org.consumersunion.stories.dashboard.client.event.CloseCardEvent;
import org.consumersunion.stories.dashboard.client.event.ReloadProfileEvent;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;

import com.google.common.collect.Maps;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.NavigationEvent;
import com.gwtplatform.mvp.client.proxy.NavigationHandler;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.ResetPresentersEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class StoriesDashboardPresenter
        extends Presenter<StoriesDashboardPresenter.MyView, StoriesDashboardPresenter.MyProxy>
        implements ReloadProfileEvent.ProfileHandler, CloseCardEvent.CloseCardHandler, NavigationHandler,
        ClearContentEvent.ClearContentHandler {

    interface MyView extends View {
    }

    @ProxyStandard
    @NameToken(NameTokens.dashboard)
    interface MyProxy extends ProxyPlace<StoriesDashboardPresenter> {
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> SLOT_MAIN_CONTENT = new Type<RevealContentHandler<?>>();
    public static final Object TYPE_SetHeaderContent = new Object();

    private final PlaceManager placeManager;
    private final HeaderPresenter headerPresenter;
    private final RpcProfileServiceAsync profileService;
    private final Map<PresenterWidget, String> contents;

    @Inject
    StoriesDashboardPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            PlaceManager placeManager,
            HeaderPresenter headerPresenter,
            RpcProfileServiceAsync profileService) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

        this.placeManager = placeManager;
        this.headerPresenter = headerPresenter;
        this.profileService = profileService;
        contents = Maps.newHashMap();
    }

    @Override
    public void onProfileReload(ReloadProfileEvent event) {
        profileService.retrieveProfile(event.getProfileId(),
                new ResponseHandler<DatumResponse<ProfileSummary>>() {
                    @Override
                    public void handleSuccess(DatumResponse<ProfileSummary> result) {
                        headerPresenter.reloadProfileData(result.getDatum());
                    }
                }
        );
    }

    @Override
    public void setInSlot(Object slot, PresenterWidget<?> child, boolean performReset) {
        if (slot == SLOT_MAIN_CONTENT && placeManager.getHierarchyDepth() > 1) {
            handleAddToSlot(slot, child);
            if (performReset) {
                ResetPresentersEvent.fire(this);
            }
        } else if (slot == SLOT_MAIN_CONTENT && placeManager.getHierarchyDepth() == 1) {
            contents.clear();
            clearSlot(slot);
            super.setInSlot(slot, child, performReset);
        } else {
            super.setInSlot(slot, child, performReset);
        }

        if (slot == SLOT_MAIN_CONTENT) {
            for (Map.Entry<PresenterWidget, String> entry : contents.entrySet()) {
                if (!isInHierarchy(entry.getValue())) {
                    removeFromSlot(SLOT_MAIN_CONTENT, entry.getKey());
                }
            }

            contents.put(child, ((ProxyPlace) ((Presenter) child).getProxy()).getNameToken());
        }
    }

    @Override
    public void clearSlot(Object slot) {
        if (slot == SLOT_MAIN_CONTENT) {
            contents.clear();
        }

        super.clearSlot(slot);
    }

    @Override
    public void removeFromSlot(Object slot, PresenterWidget<?> child) {
        contents.remove(child);
        super.removeFromSlot(slot, child);
    }

    @Override
    public void onCardClosed(CloseCardEvent event) {
        if (placeManager.getHierarchyDepth() > 1) {
            placeManager.revealRelativePlace(-1);
        } else {
            placeManager.revealPlace(event.getPlaceRequest());
        }

        if (placeManager.getHierarchyDepth() > 1) {
            removeFromSlot(event.getSlot(), event.getPresenterWidget());
        }
    }

    @Override
    public void onNavigation(NavigationEvent navigationEvent) {
        placeManager.unlock();
    }

    @Override
    public void onClearContent(ClearContentEvent event) {
        clearSlot(SLOT_MAIN_CONTENT);
    }

    @Override
    protected void onBind() {
        super.onBind();

        setInSlot(TYPE_SetHeaderContent, headerPresenter);

        addRegisteredHandler(ReloadProfileEvent.TYPE, this);
        addRegisteredHandler(CloseCardEvent.TYPE, this);
        addRegisteredHandler(NavigationEvent.getType(), this);
        addRegisteredHandler(ClearContentEvent.TYPE, this);
    }

    private void handleAddToSlot(Object slot, PresenterWidget<?> child) {
        if (contents.containsKey(child)) {
            removeFromSlot(slot, child);
        }
        addToSlot(slot, child);
    }

    private boolean isInHierarchy(String nameToken) {
        for (PlaceRequest placeRequest : placeManager.getCurrentPlaceHierarchy()) {
            if (placeRequest.matchesNameToken(nameToken)) {
                return true;
            }
        }

        return false;
    }
}
