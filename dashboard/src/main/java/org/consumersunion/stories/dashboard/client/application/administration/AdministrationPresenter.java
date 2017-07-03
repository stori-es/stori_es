package org.consumersunion.stories.dashboard.client.application.administration;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.i18n.CommonI18nMessages;
import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.client.service.RpcOrganizationServiceAsync;
import org.consumersunion.stories.common.client.service.RpcThemeServiceAsync;
import org.consumersunion.stories.common.client.service.RpcUserServiceAsync;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.PagedDataResponse;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.client.util.ResponseHandlerLoader;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.CredentialedUser;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.Theme;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.common.shared.service.datatransferobject.OrganizationSummary;
import org.consumersunion.stories.dashboard.client.application.StoriesDashboardPresenter;
import org.consumersunion.stories.dashboard.client.application.administration.account.CreateAccountPresenter;
import org.consumersunion.stories.dashboard.client.application.administration.account.UpdateAccountPresenter;
import org.consumersunion.stories.dashboard.client.application.administration.organization.CreateOrganizationPresenter;
import org.consumersunion.stories.dashboard.client.application.administration.organization.UpdateOrganizationPresenter;
import org.consumersunion.stories.dashboard.client.event.AccountChangedEvent;
import org.consumersunion.stories.dashboard.client.util.AbstractAsyncCallback;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;

import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class AdministrationPresenter extends Presenter<AdministrationPresenter.MyView, AdministrationPresenter.MyProxy>
        implements AdminManagerUiHandlers {

    public interface MyView extends View {
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.administration)
    public interface MyProxy extends ProxyPlace<AdministrationPresenter> {
    }

    public static final Object TYPE_SetMainContent = new Object();

    private final CreateAccountPresenter createAcctPresenter;
    private final UpdateAccountPresenter updateAcctPresenter;
    private final CreateOrganizationPresenter createOrgPresenter;
    private final UpdateOrganizationPresenter updateOrgPresenter;
    private final RpcUserServiceAsync userService;
    private final RpcOrganizationServiceAsync organizationService;
    private final RpcThemeServiceAsync themeService;
    private final StoryTellerDashboardI18nLabels labels;
    private final CommonI18nMessages messages;
    private final List<String> selectedThemes;

    @Inject
    AdministrationPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            RpcUserServiceAsync userService,
            RpcOrganizationServiceAsync organizationService,
            CreateOrganizationPresenter createOrgPresenter,
            CreateAccountPresenter createAcctPresenter,
            UpdateAccountPresenter updateAcctPresenter,
            UpdateOrganizationPresenter updateOrgPresenter,
            RpcThemeServiceAsync themeService,
            StoryTellerDashboardI18nLabels labels,
            CommonI18nMessages messages) {
        super(eventBus, view, proxy, StoriesDashboardPresenter.SLOT_MAIN_CONTENT);

        this.userService = userService;
        this.organizationService = organizationService;
        this.createOrgPresenter = createOrgPresenter;
        this.createAcctPresenter = createAcctPresenter;
        this.updateAcctPresenter = updateAcctPresenter;
        this.updateOrgPresenter = updateOrgPresenter;
        this.themeService = themeService;
        this.labels = labels;
        this.messages = messages;
        selectedThemes = new ArrayList<String>();

        createAcctPresenter.setUiHandlers(this);
        createOrgPresenter.setUiHandlers(this);
        updateAcctPresenter.setUiHandlers(this);
        updateOrgPresenter.setUiHandlers(this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest placeRequest) {
        int menuIndex = Integer.parseInt(placeRequest.getParameter(ParameterTokens.menuItem, "0"));
        switch (menuIndex) {
            case 0:
                setInSlot(TYPE_SetMainContent, createAcctPresenter);
                break;
            case 1:
                setInSlot(TYPE_SetMainContent, updateAcctPresenter);
                break;
            case 2:
                setInSlot(TYPE_SetMainContent, createOrgPresenter);
                break;
            case 3:
                setInSlot(TYPE_SetMainContent, updateOrgPresenter);
                break;
        }
    }

    @Override
    public void saveNewAccount(User user, String userPassword, String userResetQuestion, String userResetAnswer,
            String givenName, String surname, List<String> organizations, String defaultOrg) {
        userService.createAccount(user, userPassword, userResetQuestion, userResetAnswer, givenName, surname,
                organizations, defaultOrg,
                new ResponseHandlerLoader<ActionResponse>() {
                    @Override
                    public void handleSuccess(final ActionResponse result) {
                        createAcctPresenter.resetForm();
                        createAcctPresenter.loadOrganizations();
                        if (!result.isError()) {
                            Window.alert("We are working on creating this new stori.es account. This process can " +
                                    "take a while. If the new user is unable to sign in to stori.es immediately, " +
                                    "have them try again, 20 minutes later.\n" +
                                    "\n" +
                                    "Thank you!");
                        }
                    }
                });
    }

    @Override
    public void loadUsers(int start, int length, SortField sort, boolean ascending) {
        ResponseHandler<PagedDataResponse<CredentialedUser>> callback =
                new ResponseHandler<PagedDataResponse<CredentialedUser>>() {
                    @Override
                    public void handleSuccess(PagedDataResponse<CredentialedUser> result) {
                        updateAcctPresenter.setData(result.getData(), result.getStart(), result.getTotalCount());
                    }
                };
        userService.getUsersByPermissionMask(start, length, sort, ascending, 16, callback);
    }

    @Override
    public void updateUserAccount(String newPassword, CredentialedUser user, String givenName,
            String surname, List<String> organizations, String defaultOrg) {
        userService.updateUserAndPassword(newPassword, user, givenName, surname, organizations, defaultOrg,
                new ResponseHandlerLoader<ActionResponse>() {
                    @Override
                    public void handleSuccess(final ActionResponse result) {
                        if (result.getGlobalErrorMessages().isEmpty()) {
                            messageDispatcher.displayMessage(MessageStyle.SUCCESS, messages.accountUpdated());
                            updateAcctPresenter.reset();
                            Window.alert("We are working on updating this stori.es account. The process to modify " +
                                    "organization permissions can take a while. If the new user experiences any " +
                                    "permission issues, have them try again, 20 minutes later.\n" +
                                    "\n" +
                                    "Thank you!");
                        } else {
                            messageDispatcher
                                    .displayMessage(MessageStyle.ERROR, result.getGlobalErrorMessages().get(0));
                        }
                    }
                });
    }

    @Override
    public void updateAccountStatus(CredentialedUser user) {
        userService.changeAccountStatus(user, new ResponseHandler<DatumResponse<CredentialedUser>>() {
            @Override
            public void handleSuccess(final DatumResponse<CredentialedUser> result) {
                if (result.getGlobalErrorMessages().size() == 0) {
                    User updatedUser = result.getDatum().getUser();
                    AccountChangedEvent.fire(this, updatedUser);
                    if (updatedUser.isActive()) {
                        messageDispatcher.displayMessage(MessageStyle.SUCCESS, messages.accountEnabled());
                    } else {
                        messageDispatcher.displayMessage(MessageStyle.SUCCESS, messages.accountDisabled());
                    }
                    updateAcctPresenter.setCurrentUser(result.getDatum());
                } else {
                    messageDispatcher
                            .displayMessage(MessageStyle.ERROR, result.getGlobalErrorMessages().get(0));
                }
            }
        });
    }

    @Override
    public void loadOrganizationThemes(Organization organization) {
        themeService.getThemesForOrganization(organization.getId(), new ResponseHandler<DataResponse<Theme>>() {
            @Override
            public void handleSuccess(DataResponse<Theme> result) {
                selectedThemes.clear();
                for (Theme orgTheme : result.getData()) {
                    selectedThemes.add(Integer.toString(orgTheme.getId()));
                }
                updateOrgPresenter.displayOrganizationThemes(selectedThemes);
            }
        });
    }

    @Override
    public void loadAdminOrganizations(int start, int length, SortField sort, boolean ascending) {
        ResponseHandler<PagedDataResponse<OrganizationSummary>> callback = new
                ResponseHandler<PagedDataResponse<OrganizationSummary>>() {
                    @Override
                    public void handleSuccess(PagedDataResponse<OrganizationSummary> result) {
                        updateOrgPresenter.setData(result.getData(), result.getStart(), result.getTotalCount());
                    }
                };
        organizationService.getAdminOrganizations(start, length, sort, ascending, callback);
    }

    @Override
    public void checkOrganizationDisponibility(String organizationName) {
        organizationService.checkOrganizationAvailability(organizationName.trim(),
                new ResponseHandler<ActionResponse>() {
                    @Override
                    public void handleSuccess(ActionResponse result) {
                        if (result.getGlobalErrorMessages().size() > 0) {
                            createOrgPresenter.setOrganizationNameDisponibility(false);
                            updateOrgPresenter.setOrganizationNameDisponibility(false);
                        } else {
                            createOrgPresenter.setOrganizationNameDisponibility(true);
                            updateOrgPresenter.setOrganizationNameDisponibility(true);
                        }
                        result.getGlobalErrorMessages().clear();
                    }
                });
    }

    @Override
    public void checkUsernameExists(String userName) {
        userService.checkHandle(userName, new AbstractAsyncCallback<ActionResponse>() {
            @Override
            public void onSuccess(ActionResponse result) {
                if (result.getGlobalErrorMessages().size() > 0) {
                    createOrgPresenter.setUsernameDisponibility(false);
                    updateOrgPresenter.setUsernameDisponibility(false);
                } else {
                    createOrgPresenter.setUsernameDisponibility(true);
                    updateOrgPresenter.setUsernameDisponibility(true);
                }
            }
        });
    }

    @Override
    public void checkAdminNameExists(String userName) {
        userService.checkAssignableHandle(userName, new AbstractAsyncCallback<ActionResponse>() {
            @Override
            public void onSuccess(ActionResponse result) {
                if (result.getGlobalErrorMessages().size() > 0) {
                    createOrgPresenter.setUsernameDisponibility(false);
                    updateOrgPresenter.setUsernameDisponibility(false);
                } else {
                    createOrgPresenter.setUsernameDisponibility(true);
                    updateOrgPresenter.setUsernameDisponibility(true);
                }
            }
        });
    }

    @Override
    public void createOrganization(
            Organization org,
            List<Contact> contacts,
            List<Address> addresses,
            List<String> adminUserNames,
            List<Integer> themes) {
        organizationService.createOrganization(org, contacts, addresses, adminUserNames, themes,
                new ResponseHandler<ActionResponse>() {
                    @Override
                    public void handleSuccess(final ActionResponse result) {
                        if (result.getGlobalErrorMessages().size() == 0) {
                            messageDispatcher.displayMessage(MessageStyle.SUCCESS, labels.organizationSaved());
                        } else {
                            String errors = "";
                            for (final String error : result.getGlobalErrorMessages()) {
                                errors += error;
                            }
                            messageDispatcher.displayMessage(MessageStyle.ERROR, errors);
                        }
                    }
                });
    }

    @Override
    public void updateOrganization(
            Organization org,
            List<Contact> contacts,
            List<Address> addresses,
            List<Integer> themes,
            List<String> adminUsers) {
        organizationService.updateOrganization(org, contacts, addresses, themes, adminUsers,
                new ResponseHandlerLoader<DatumResponse<OrganizationSummary>>() {
                    @Override
                    public void handleSuccess(DatumResponse<OrganizationSummary> result) {
                        messageDispatcher.displayMessage(MessageStyle.SUCCESS, labels.organizationSaved());
                        updateOrgPresenter.setCurrentOrganization(result.getDatum());
                    }
                });
    }
}
