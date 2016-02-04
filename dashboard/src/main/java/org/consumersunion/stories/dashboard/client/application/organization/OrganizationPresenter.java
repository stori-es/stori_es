package org.consumersunion.stories.dashboard.client.application.organization;

import java.util.List;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.client.service.RpcDocumentServiceAsync;
import org.consumersunion.stories.common.client.service.RpcOrganizationServiceAsync;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.util.HtmlSanitizerUtil;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.client.util.ResponseHandlerLoader;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.service.datatransferobject.OrganizationSummary;
import org.consumersunion.stories.dashboard.client.application.StoriesDashboardPresenter;
import org.consumersunion.stories.dashboard.client.application.profile.widget.NotesManagerPresenter;
import org.consumersunion.stories.dashboard.client.application.story.widget.AttachmentsPresenter;
import org.consumersunion.stories.dashboard.client.application.widget.StoriesGrowthPresenter;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

/**
 * Presenter for the {@link Organization} detail page. Contains the stories growth graph
 */
public class OrganizationPresenter extends Presenter<OrganizationPresenter.MyView, OrganizationPresenter.MyProxy>
        implements OrganizationUiHandlers {

    interface MyView extends View, HasUiHandlers<OrganizationUiHandlers> {
        void setupOrganization(Organization organization);

        void setOrganizationNameDisponibility(boolean available);

        void loadPermissions(String text);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.organization)
    interface MyProxy extends ProxyPlace<OrganizationPresenter> {
    }

    static final Object TYPE_storiesGrowthContent = new Object();
    static final Object TYPE_NotesManagerContent = new Object();
    static final Object TYPE_AttachmentsContent = new Object();

    private final StoriesGrowthPresenter storiesGrowthPresenter;
    private final NotesManagerPresenter notesManagerPresenter;
    private final PlaceManager placeManager;
    private final RpcOrganizationServiceAsync organizationService;
    private final RpcDocumentServiceAsync documentService;
    private final AttachmentsPresenter attachmentsPresenter;
    private final HtmlSanitizerUtil htmlSanitizerUtil;
    private final StoryTellerDashboardI18nLabels storyTellerLabels;

    private Organization currentOrg;
    private Document defaultPermissions;

    @Inject
    OrganizationPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            PlaceManager placeManager,
            RpcOrganizationServiceAsync organizationService,
            StoriesGrowthPresenter storiesGrowthPresenter,
            NotesManagerPresenter notesManagerPresenter,
            RpcDocumentServiceAsync documentService,
            AttachmentsPresenter attachmentsPresenter,
            HtmlSanitizerUtil htmlSanitizerUtil,
            StoryTellerDashboardI18nLabels storyTellerLabels) {
        super(eventBus, view, proxy, StoriesDashboardPresenter.SLOT_MAIN_CONTENT);

        this.placeManager = placeManager;
        this.organizationService = organizationService;
        this.storiesGrowthPresenter = storiesGrowthPresenter;
        this.notesManagerPresenter = notesManagerPresenter;
        this.documentService = documentService;
        this.attachmentsPresenter = attachmentsPresenter;
        this.htmlSanitizerUtil = htmlSanitizerUtil;
        this.storyTellerLabels = storyTellerLabels;

        getView().setUiHandlers(this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        int currentOrgId = Integer.valueOf(request.getParameter("id", "-1"));
        if (currentOrgId > -1) {
            revealSpecificOrganization(currentOrgId);
        } else {
            placeManager.revealDefaultPlace();
        }
    }

    @Override
    public void saveOrganization(Organization editedorg) {
        organizationService.updateOrganization(editedorg, null, null, null, null,
                new ResponseHandler<DatumResponse<OrganizationSummary>>() {
                    @Override
                    public void handleSuccess(DatumResponse<OrganizationSummary> result) {
                        messageDispatcher.displayMessage(MessageStyle.SUCCESS, storyTellerLabels.organizationSaved());
                        currentOrg = result.getDatum().getOrganization();
                        loadOrganizationPermissions(currentOrg.getId());
                    }
                });
    }

    @Override
    public void saveOrganizationPermissions(String text) {
        if (defaultPermissions == null) {
            defaultPermissions = new Document();
            defaultPermissions.setEntity(currentOrg.getId());
            defaultPermissions.setSystemEntityRelation(SystemEntityRelation.DEFAULT_PERMISSIONS);
            defaultPermissions.addBlock(new Content(BlockType.CONTENT, "", Content.TextType.HTML));
        }
        defaultPermissions.setOnlyContent(htmlSanitizerUtil.sanitize(text).asString());

        ResponseHandler<DatumResponse<Document>> callback = new ResponseHandler<DatumResponse<Document>>() {
            @Override
            public void handleSuccess(DatumResponse<Document> result) {
                messageDispatcher.displayMessage(MessageStyle.SUCCESS, storyTellerLabels.permissionsSaved());
                defaultPermissions = result.getDatum();
                getView().loadPermissions(defaultPermissions.getOnlyContent());
            }
        };

        if (defaultPermissions.isNew()) {
            documentService.createUntitledDocument(defaultPermissions, callback);
        } else {
            documentService.saveDocument(defaultPermissions, callback);
        }
    }

    @Override
    public void checkOrganizationDisponibility(String organizationName) {
        organizationService.checkOrganizationAvailability(organizationName.trim(),
                new ResponseHandler<ActionResponse>() {
                    @Override
                    public void handleSuccess(ActionResponse result) {
                        if (result.getGlobalErrorMessages().size() > 0) {
                            getView().setOrganizationNameDisponibility(false);
                        } else {
                            getView().setOrganizationNameDisponibility(true);
                        }
                    }
                });
    }

    @Override
    protected void onBind() {
        setInSlot(TYPE_storiesGrowthContent, storiesGrowthPresenter);
        setInSlot(TYPE_NotesManagerContent, notesManagerPresenter);
        setInSlot(TYPE_AttachmentsContent, attachmentsPresenter);
    }

    private void revealSpecificOrganization(Integer orgId) {
        organizationService.retrieveOrganization(orgId, new ResponseHandlerLoader<DatumResponse<Organization>>() {
            @Override
            public void handleSuccess(DatumResponse<Organization> result) {
                currentOrg = result.getDatum();
                loadOrganizationPermissions(currentOrg.getId());
            }
        });
    }

    private void loadOrganizationPermissions(Integer orgId) {
        defaultPermissions = null;
        documentService.getByEntityAndRelation(orgId, SystemEntityRelation.DEFAULT_PERMISSIONS,
                new ResponseHandler<DataResponse<Document>>() {
                    @Override
                    public void handleSuccess(DataResponse<Document> result) {
                        final List<Document> permissions = result.getData();
                        // really should be only one at the moment
                        defaultPermissions = permissions.get(0);
                        revealCurrentOrganization();
                    }
                });
    }

    private void revealCurrentOrganization() {
        storiesGrowthPresenter.createChart();
        notesManagerPresenter.initPresenter(currentOrg);
        attachmentsPresenter.initPresenter(currentOrg);

        getView().setupOrganization(currentOrg);
        if (defaultPermissions != null) {
            loadPermissions();
        } else {
            getView().loadPermissions("");
        }
    }

    private void loadPermissions() {
        String text = htmlSanitizerUtil.sanitize(defaultPermissions.getOnlyContent()).asString();
        getView().loadPermissions(text);
    }
}
