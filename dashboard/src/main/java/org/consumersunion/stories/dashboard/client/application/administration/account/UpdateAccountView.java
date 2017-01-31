package org.consumersunion.stories.dashboard.client.application.administration.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.client.ui.TextPager;
import org.consumersunion.stories.common.client.ui.form.Form;
import org.consumersunion.stories.common.client.ui.form.controls.CheckBoxInput;
import org.consumersunion.stories.common.client.ui.form.controls.PasswordInput;
import org.consumersunion.stories.common.client.ui.form.controls.RadioInput;
import org.consumersunion.stories.common.client.ui.form.controls.TextInput;
import org.consumersunion.stories.common.client.util.MessageDispatcher;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.CredentialedUser;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.StorySortField;
import org.consumersunion.stories.common.shared.service.datatransferobject.OrganizationSummary;
import org.consumersunion.stories.dashboard.client.application.administration.AdminManagerUiHandlers;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class UpdateAccountView extends ViewWithUiHandlers<AdminManagerUiHandlers>
        implements UpdateAccountPresenter.MyView {
    interface Binder extends UiBinder<Widget, UpdateAccountView> {
    }

    @UiField(provided = true)
    final CellTable<CredentialedUser> usersTable;
    @UiField(provided = true)
    final TextPager pager;

    @UiField
    HTMLPanel mainPanel;
    @UiField
    Form form;
    @UiField
    IntegerBox pageSize;
    @UiField
    Label displayLabel;
    @UiField
    Label rowsLabel;
    @UiField
    Label formTitle;
    @UiField
    Button submit;
    @UiField
    Button changeStatus;
    @UiField
    Button cancel;

    public final String USERNAME_FIELD_NAME = "usernameTxt";
    public final String FIRST_NAME_FIELD_NAME = "firstNameTxt";
    public final String LAST_NAME_FIELD_NAME = "lastNameTxt";
    public final String EMAIL_FIELD_NAME = "emailTxt";
    public final String NEW_PASSWORD_FIELD_NAME = "newPasswordTxt";
    public final String NEW_PASSWORD_CONFIRM_FIELD_NAME = "newPasswordConfirmTxt";
    public final String RESET_QUESTION_FIELD_NAME = "resetQuestionTxt";
    public final String RESET_ANSWER_FIELD_NAME = "resetAnswerTxt";
    public final String ORGANIZATIONS_FIELD_NAME = "organizationsSelect";
    public final String DEFAULT_ORGANIZATION_FIELD_NAME = "organizationSelect";

    private final CommonI18nLabels labels;
    private final MessageDispatcher messageDispatcher;
    private final ProvidesKey<CredentialedUser> keyProvider;
    private final SelectionModel<CredentialedUser> selectionModel;
    private final HashMap<Column<CredentialedUser, ?>, StorySortField> columnSortFieldMap;

    private CredentialedUser user;
    private List<String> selectedOrganizations = new ArrayList<String>();
    private final AsyncDataProvider<CredentialedUser> dataProvider;
    private CheckBoxInput organizationInput;
    private RadioInput defaultOrgInput;
    private TextInput username;
    private boolean usernameAvailable;

    @Inject
    UpdateAccountView(
            Binder uiBinder,
            CommonI18nLabels labels,
            MessageDispatcher messageDispatcher,
            TextPager pager) {
        this.labels = labels;
        this.messageDispatcher = messageDispatcher;
        this.pager = pager;
        keyProvider = setupKeyProvider();
        dataProvider = setupDataProvider();
        usersTable = new CellTable<CredentialedUser>(UpdateAccountPresenter.PAGE_SIZE, keyProvider);
        selectionModel = new SingleSelectionModel<CredentialedUser>(keyProvider);
        columnSortFieldMap = new HashMap<Column<CredentialedUser, ?>, StorySortField>();

        initWidget(uiBinder.createAndBindUi(this));

        pager.setDisplay(usersTable);
        pageSize.setValue(UpdateAccountPresenter.PAGE_SIZE);
        usersTable.setSelectionModel(selectionModel);
        usersTable.addColumnSortHandler(new ColumnSortEvent.AsyncHandler(usersTable));
        dataProvider.addDataDisplay(usersTable);
        setupColumns();
        setupForm();
        setupEvent();
    }

    @Override
    public void setUsernameDisponibility(boolean available) {
        this.usernameAvailable = available;

        if (usernameAvailable) {
            username.removeStyleName("cu-error-text-field");
            username.addStyleName("cu-success-availability-text-field");
        } else {
            username.removeStyleName("cu-success-availability-text-field");
            username.addStyleName("cu-error-text-field");
        }
    }

    @Override
    public void setData(List<CredentialedUser> data, int start, int totalCount) {
        dataProvider.updateRowCount(totalCount, true);
        dataProvider.updateRowData(start, data);
    }

    @Override
    public void reloadData() {
        usersTable.setVisibleRangeAndClearData(usersTable.getVisibleRange(), true);
    }

    @Override
    public void resetForm() {
        form.clear();
        setupForm();
        setupEvent();
    }

    @Override
    public void setCurrentUser(CredentialedUser user) {
        int relativeIndex = usersTable.getKeyboardSelectedRow();
        usersTable.setRowData(relativeIndex + usersTable.getVisibleRange().getStart(), Lists.newArrayList(user));
    }

    @Override
    public void displayOrganizations(List<OrganizationSummary> organizationSummaries) {
        final String[] labels = new String[organizationSummaries.size()];
        final String[] options = new String[organizationSummaries.size()];

        int i = 0;
        for (final OrganizationSummary os : organizationSummaries) {
            labels[i] = os.getOrganization().getName();
            options[i] = Integer.toString(os.getOrganization().getId());
            i++;
        }
        organizationInput.setLabelsAndOptions(labels, options);
        defaultOrgInput.setLabelsAndOptions(labels, options);
    }

    @Override
    public void displayUserOrganizations(List<String> selectedOrganizations) {
        // clear all checks
        organizationInput.resetValues();
        // then check the proper checks
        if (selectedOrganizations.size() > 0) {
            this.selectedOrganizations = selectedOrganizations;
            organizationInput.setValues(selectedOrganizations);
        }
    }

    @UiHandler("submit")
    void submitClickHandler(final ClickEvent e) {
        updateAccount();
    }

    @UiHandler("changeStatus")
    void onChangeStatusClick(final ClickEvent e) {
        if (user != null) {
            if (user.getUser().isActive()) {
                disableAccount();
            } else {
                enableAccount();
            }
        }
    }

    @UiHandler("cancel")
    void cancelClickHandler(final ClickEvent e) {
        resetFormValues();
        removeUsernameStyles();
    }

    @UiHandler("pageSize")
    void onPageSizeKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            if (pageSize.getValue() == null) {
                messageDispatcher.displayMessage(MessageStyle.ERROR, "Please enter a integer value greater than 0.");
            } else {
                usersTable.setPageSize(pageSize.getValue());
                usersTable.setPageStart(0);

                fetchData(usersTable);
            }
        }
    }

    private void resetFormValues() {
        form.setValue(FIRST_NAME_FIELD_NAME, "");
        form.setValue(USERNAME_FIELD_NAME, "");
        form.setValue(LAST_NAME_FIELD_NAME, "");
        form.setValue(NEW_PASSWORD_FIELD_NAME, "");
        form.setValue(NEW_PASSWORD_CONFIRM_FIELD_NAME, "");
        form.setValue(RESET_QUESTION_FIELD_NAME, "");
        form.setValue(RESET_ANSWER_FIELD_NAME, "");
        form.setValue(EMAIL_FIELD_NAME, "");
        form.setValue(ORGANIZATIONS_FIELD_NAME, "");
        defaultOrgInput.resetValues();
        organizationInput.resetValues();
        usernameAvailable = true;
        changeStatus.setText(labels.disableOrEnable());

        submit.setEnabled(true);
        cancel.setEnabled(true);
        changeStatus.setEnabled(true);
    }

    private void setupForm() {
        submit.setText(labels.submit());
        cancel.setText(labels.cancel());
        changeStatus.setText(labels.disableOrEnable());
        formTitle.setText(labels.updateAccount());

        usersTable.getElement().getStyle().setProperty("border", "1px solid black");
        usersTable.setWidth("100%");

        username = form.add(new TextInput(labels.userName(), USERNAME_FIELD_NAME, true));
        username.setEnabled(false);
        form.add(new TextInput(labels.firstName(), FIRST_NAME_FIELD_NAME, true));
        form.add(new TextInput(labels.lastName(), LAST_NAME_FIELD_NAME, true));
        form.add(new PasswordInput(labels.newPassword(), NEW_PASSWORD_FIELD_NAME, false));
        form.add(new PasswordInput(labels.newPasswordConfirmation(), NEW_PASSWORD_CONFIRM_FIELD_NAME, false));
        form.add(new TextInput(labels.resetQuestion(), RESET_QUESTION_FIELD_NAME, true));
        form.add(new TextInput(labels.resetAnswer(), RESET_ANSWER_FIELD_NAME, true));
        organizationInput = new CheckBoxInput(labels.organizations(), ORGANIZATIONS_FIELD_NAME, true);
        defaultOrgInput = new RadioInput(labels.defaultOrganization(), DEFAULT_ORGANIZATION_FIELD_NAME,
                DEFAULT_ORGANIZATION_FIELD_NAME, true);
        form.add(organizationInput);
        form.add(defaultOrgInput);

        form.setMaxLength(USERNAME_FIELD_NAME, 64);
        form.setMaxLength(FIRST_NAME_FIELD_NAME, 255);
        form.setMaxLength(LAST_NAME_FIELD_NAME, 255);
        form.setMaxLength(NEW_PASSWORD_FIELD_NAME, 255);
        form.setMaxLength(NEW_PASSWORD_CONFIRM_FIELD_NAME, 255);
        form.setMaxLength(RESET_QUESTION_FIELD_NAME, 255);
        form.setMaxLength(RESET_ANSWER_FIELD_NAME, 255);
    }

    private void updateAccount() {
        if (!usernameAvailable) {
            messageDispatcher.displayMessage(MessageStyle.ERROR, "The selected username is not available");
            return;
        } else if (!validateFields(form)) {
            messageDispatcher.displayMessage(MessageStyle.ERROR, "Please fill all the required fields");
            return;
        } else if (user != null) {
            user.getUser().setHandle(form.getValue(USERNAME_FIELD_NAME));
            user.setResetQuestion(form.getValue(RESET_QUESTION_FIELD_NAME));
            user.setResetAnswer(form.getValue(RESET_ANSWER_FIELD_NAME));
            user.getUser().setActive(true);
            final String newPassword = form.getValue(NEW_PASSWORD_FIELD_NAME);
            final List<String> organizations = form.getMultiValue(ORGANIZATIONS_FIELD_NAME);
            selectedOrganizations = organizations;
            getUiHandlers().updateUserAccount(newPassword, user, form.getValue(FIRST_NAME_FIELD_NAME),
                    form.getValue(LAST_NAME_FIELD_NAME), organizations, defaultOrgInput.getValue());
            resetFormValues();
            removeUsernameStyles();
            user = null;
        }
    }

    private void disableAccount() {
        user.getUser().setActive(false);
        getUiHandlers().updateAccountStatus(user);
        resetFormValues();
        removeUsernameStyles();
        user = null;
    }

    private void enableAccount() {
        user.getUser().setActive(true);
        getUiHandlers().updateAccountStatus(user);
        resetFormValues();
        removeUsernameStyles();
        user = null;
    }

    private boolean validateFields(final Form form) {
        if (!Strings.isNullOrEmpty(username.getValue()) && form.validate()) {
            if (!form.getValue(NEW_PASSWORD_FIELD_NAME).equals(form.getValue(NEW_PASSWORD_CONFIRM_FIELD_NAME))) {
                messageDispatcher.displayMessage(MessageStyle.ERROR, "Confirmation password doesn't match");
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private void setupColumns() {
        Column<CredentialedUser, String> defaultSortColumn = setupUserNameColumn();

        usersTable.addColumn(setupUserNameColumn(), labels.userName());
        usersTable.getColumnSortList().push(defaultSortColumn);
    }

    private Column<CredentialedUser, String> setupUserNameColumn() {
        final ClickableTextCell username = new ClickableTextCell();
        final Column<CredentialedUser, String> usernameColumn = new Column<CredentialedUser, String>(username) {
            @Override
            public String getValue(final CredentialedUser cd) {
                return cd.getUser().getHandle();
            }
        };
        usernameColumn.setSortable(true);
        usernameColumn.setFieldUpdater(createStaticColumnFieldUpdater(usernameColumn));
        columnSortFieldMap.put(usernameColumn, StorySortField.HANDLE);

        return usernameColumn;
    }

    private <C> FieldUpdater<CredentialedUser, C> createStaticColumnFieldUpdater(
            final Column<CredentialedUser, C> column) {
        return new FieldUpdater<CredentialedUser, C>() {
            @Override
            public void update(final int index, final CredentialedUser cd, final C updated) {
                selectionModel.setSelected(cd, true);
                user = cd;
                loadUserSelected();
            }
        };
    }

    private void loadUserSelected() {
        resetFormValues();
        form.setValue(USERNAME_FIELD_NAME, this.user.getUser().getHandle());
        form.setValue(RESET_QUESTION_FIELD_NAME, this.user.getResetQuestion());
        form.setValue(RESET_ANSWER_FIELD_NAME, this.user.getResetAnswer());
        form.setValue(NEW_PASSWORD_FIELD_NAME, "");
        form.setValue(NEW_PASSWORD_CONFIRM_FIELD_NAME, "");
        form.setMultiValues(ORGANIZATIONS_FIELD_NAME, selectedOrganizations);

        if (user.getUser().isActive()) {
            changeStatus.setText(labels.disable());
        }

        if (!user.getUser().isActive()) {
            changeStatus.setText(labels.enable());
        }

        List<ProfileSummary> profiles = user.getUser().getProfiles();
        List<String> selectedOrganizations = Lists.transform(profiles, new Function<ProfileSummary, String>() {
            @Override
            public String apply(ProfileSummary input) {
                int organizationId = input.getProfile().getOrganizationId();
                return String.valueOf(organizationId);
            }
        });
        displayUserOrganizations(selectedOrganizations);

        final int defaultProfile = user.getUser().getDefaultProfile();
        ProfileSummary profileSummary = Iterables.tryFind(profiles, new Predicate<ProfileSummary>() {
            @Override
            public boolean apply(ProfileSummary input) {
                return input.getProfile().getId() == defaultProfile;
            }
        }).orNull();

        if (profileSummary != null) {
            Profile profile = profileSummary.getProfile();
            defaultOrgInput.setValue(String.valueOf(profile.getOrganizationId()));
            form.setValue(FIRST_NAME_FIELD_NAME, profile.getGivenName());
            form.setValue(LAST_NAME_FIELD_NAME, profile.getSurname());
        }

        if (profileSummary == null && !selectedOrganizations.isEmpty()) {
            Window.alert(labels.accountNotReady());
            submit.setEnabled(false);
            changeStatus.setEnabled(false);
        }
    }

    private void setupEvent() {
        username.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                removeUsernameStyles();
                if (!Strings.isNullOrEmpty(form.getValue(USERNAME_FIELD_NAME).trim())) {
                    getUiHandlers().checkUsernameExists(form.getValue(USERNAME_FIELD_NAME));
                }
            }
        });
    }

    private AsyncDataProvider<CredentialedUser> setupDataProvider() {
        return new AsyncDataProvider<CredentialedUser>(keyProvider) {
            @Override
            protected void onRangeChanged(HasData<CredentialedUser> display) {
                fetchData(display);
            }
        };
    }

    private void fetchData(HasData<CredentialedUser> display) {
        Range range = display.getVisibleRange();
        StorySortField sortField = getSortField();
        boolean ascending = isAscendingSort();

        if (getUiHandlers() != null) {
            getUiHandlers().loadUsers(range.getStart(), range.getLength(), sortField, ascending);
        }
    }

    private boolean isAscendingSort() {
        if (usersTable.getColumnSortList().size() > 0) {
            final ColumnSortList.ColumnSortInfo sortInfo = usersTable.getColumnSortList().get(0);
            return sortInfo.isAscending();
        }

        return true;
    }

    private StorySortField getSortField() {
        if (usersTable.getColumnSortList().size() > 0) {
            Column<?, ?> column = usersTable.getColumnSortList().get(0).getColumn();

            StorySortField sortField = columnSortFieldMap.get(column);
            if (sortField != null) {
                return sortField;
            }
        }
        return StorySortField.HANDLE;
    }

    private void removeUsernameStyles() {
        usernameAvailable = true;
        username.removeStyleName("cu-success-availability-text-field");
        username.removeStyleName("cu-error-text-field");
    }

    private ProvidesKey<CredentialedUser> setupKeyProvider() {
        return new ProvidesKey<CredentialedUser>() {
            @Override
            public Object getKey(final CredentialedUser cd) {
                return (cd == null) ? null : cd.getUser().getId();
            }
        };
    }
}
