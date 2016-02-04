package org.consumersunion.stories.dashboard.client.application.administration.account;

import java.util.List;

import org.consumersunion.stories.common.client.ui.form.Form;
import org.consumersunion.stories.common.client.ui.form.controls.CheckBoxInput;
import org.consumersunion.stories.common.client.ui.form.controls.PasswordInput;
import org.consumersunion.stories.common.client.ui.form.controls.RadioInput;
import org.consumersunion.stories.common.client.ui.form.controls.TextInput;
import org.consumersunion.stories.common.client.util.MessageDispatcher;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.service.datatransferobject.OrganizationSummary;
import org.consumersunion.stories.dashboard.client.application.administration.AdminManagerUiHandlers;

import com.google.common.base.Strings;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import static org.consumersunion.stories.common.client.ui.form.Form.OPTIONAL;
import static org.consumersunion.stories.common.client.ui.form.Form.REQUIRED;

public class CreateAccountView extends ViewWithUiHandlers<AdminManagerUiHandlers>
        implements CreateAccountPresenter.MyView {
    interface Binder extends UiBinder<Widget, CreateAccountView> {
    }

    @UiField
    Label createAccountLbl;
    @UiField
    Form form;
    @UiField
    Button buttonSave;

    public final String USER_NAME_FIELD_NAME = "userNameTxt";
    public final String FIRST_NAME_FIELD_NAME = "firstNameTxt";
    public final String LAST_NAME_FIELD_NAME = "lastNameTxt";
    public final String PASSWORD_FIELD_NAME = "passwordTxt";
    public final String PASSWORD_CONFIRM_FIELD_NAME = "passwordConfirmationTxt";
    public final String RESET_QUESTION_FIELD_NAME = "resetQuestionTxt";
    public final String RESET_ANSWER_FIELD_NAME = "resetAnswerTxt";
    public final String ORGANIZATIONS_FIELD_NAME = "organizationsSelect";
    public final String DEFAULT_ORGANIZATION_FIELD_NAME = "organizationSelect";

    private final CommonI18nLabels labels;
    private final MessageDispatcher messageDispatcher;

    private CheckBoxInput organizationInput;
    private RadioInput defaultOrgInput;
    private TextInput username;
    private boolean usernameAvailable;

    @Inject
    CreateAccountView(
            Binder uiBinder,
            CommonI18nLabels labels,
            MessageDispatcher messageDispatcher) {
        this.labels = labels;
        this.messageDispatcher = messageDispatcher;

        initWidget(uiBinder.createAndBindUi(this));
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
        removeUsernameStyles();
    }

    @Override
    public void resetForm() {
        form.clear();
        setupForm();
        setupEvent();
    }

    @UiHandler("buttonSave")
    void onClick(final ClickEvent e) {
        if (validate()) {
            if (usernameAvailable) {
                createAccount();
                removeUsernameStyles();
            }
        } else {
            // TODO Display global errors messages
        }
    }

    private void createAccount() {
        User user = new User();
        user.setActive(true);
        user.setHandle(form.getValue(USER_NAME_FIELD_NAME));
        List<String> organizations = form.getMultiValue(ORGANIZATIONS_FIELD_NAME);
        String defaultOrg = form.getValue(DEFAULT_ORGANIZATION_FIELD_NAME);

        if (organizations.contains(defaultOrg)) {
            getUiHandlers().saveNewAccount(user, form.getValue(PASSWORD_FIELD_NAME),
                    form.getValue(RESET_QUESTION_FIELD_NAME), form.getValue(RESET_ANSWER_FIELD_NAME),
                    form.getValue(FIRST_NAME_FIELD_NAME), form.getValue(LAST_NAME_FIELD_NAME), organizations,
                    defaultOrg);
        } else {
            Window.alert("The default organization is not in the selected organizations");
        }
    }

    private boolean validate() {
        boolean validationResult = form.validate();

        if (validationResult && !form.getValue(PASSWORD_FIELD_NAME).equals(
                form.getValue(PASSWORD_CONFIRM_FIELD_NAME))) {
            messageDispatcher.displayMessage(MessageStyle.ERROR, "Confirmation password doesn't match");
            validationResult = false;
        }

        return validationResult;
    }

    private void setupForm() {
        buttonSave.setText(labels.createAccount());
        createAccountLbl.setText(labels.createAccount());

        username = form.add(new TextInput(labels.userName(), USER_NAME_FIELD_NAME, REQUIRED));
        form.add(new TextInput(labels.firstName(), FIRST_NAME_FIELD_NAME, OPTIONAL));
        form.add(new TextInput(labels.lastName(), LAST_NAME_FIELD_NAME, OPTIONAL));
        form.add(new PasswordInput(labels.password(), PASSWORD_FIELD_NAME, REQUIRED));
        form.add(new PasswordInput(labels.passwordConfirmation(), PASSWORD_CONFIRM_FIELD_NAME, REQUIRED));
        form.add(new TextInput(labels.resetQuestion(), RESET_QUESTION_FIELD_NAME, REQUIRED));
        form.add(new TextInput(labels.resetAnswer(), RESET_ANSWER_FIELD_NAME, REQUIRED));

        form.setMinLength(PASSWORD_FIELD_NAME, 6);
        form.setMinLength(PASSWORD_CONFIRM_FIELD_NAME, 6);

        organizationInput = new CheckBoxInput(labels.organizations(), ORGANIZATIONS_FIELD_NAME, OPTIONAL);
        defaultOrgInput = new RadioInput(labels.defaultOrganization(), DEFAULT_ORGANIZATION_FIELD_NAME,
                DEFAULT_ORGANIZATION_FIELD_NAME, OPTIONAL);
        form.add(organizationInput);
        form.add(defaultOrgInput);
    }

    private void setupEvent() {
        username.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                removeUsernameStyles();
                if (!Strings.isNullOrEmpty(form.getValue(USER_NAME_FIELD_NAME).trim())) {
                    getUiHandlers().checkUsernameExists(form.getValue(USER_NAME_FIELD_NAME));
                }
            }
        });
    }

    private void removeUsernameStyles() {
        usernameAvailable = true;
        username.removeStyleName("cu-success-availability-text-field");
        username.removeStyleName("cu-error-text-field");
    }
}
