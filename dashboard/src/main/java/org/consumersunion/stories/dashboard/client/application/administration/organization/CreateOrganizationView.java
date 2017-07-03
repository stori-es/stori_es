package org.consumersunion.stories.dashboard.client.application.administration.organization;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.common.client.ui.form.OrganizationForm;
import org.consumersunion.stories.common.client.ui.form.controls.CheckBoxInput;
import org.consumersunion.stories.common.client.ui.form.controls.InputInlineGroup;
import org.consumersunion.stories.common.client.ui.form.controls.ListInput;
import org.consumersunion.stories.common.client.ui.form.controls.TextInput;
import org.consumersunion.stories.common.client.ui.form.validators.EmailValidator;
import org.consumersunion.stories.common.client.ui.form.validators.PhoneNumberValidator;
import org.consumersunion.stories.common.client.ui.form.validators.ZipCodeValidator;
import org.consumersunion.stories.common.client.util.MessageDispatcher;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.Theme;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.common.shared.util.StatesUtil;
import org.consumersunion.stories.dashboard.client.application.administration.AdminManagerUiHandlers;

import com.google.common.base.Strings;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class CreateOrganizationView extends ViewWithUiHandlers<AdminManagerUiHandlers>
        implements CreateOrganizationPresenter.MyView {
    interface Binder extends UiBinder<Widget, CreateOrganizationView> {
    }

    public static final String NAME_FIELD_NAME = "txtName";
    public static final String SHORTNAME_FIELD_NAME = "txtShortname";
    public static final String URL_FIELD_NAME = "txtUrl";
    public static final String ADMIN_USER_FIELD_NAME = "txtUser";
    public static final String STREET_ADDRESS_1_FIELD_NAME = "txtAddress1";
    public static final String STREET_ADDRESS_2_FIELD_NAME = "txtAddress2";
    public static final String CITY_FIELD_NAME = "txtCity";
    public static final String STATE_FIELD_NAME = "lbxState";
    public static final String ZIP_FIELD_NAME = "txtZipCode";
    public static final String PHONE_FIELD_NAME = "txtPhone";
    public static final String EMAIL_FIELD_NAME = "txtEmail";
    public static final String THEMES_FIELD_NAME = "listThemes";
    public static final String DEFAULT_THEME_FIELD_NAME = "defaultTheme";

    @UiField
    Label createOrganizationLbl;
    @UiField
    OrganizationForm form;
    @UiField
    Button buttonSave;

    private final CommonI18nLabels labels;
    private final CommonI18nErrorMessages errorMessages;
    private final MessageDispatcher messageDispatcher;

    private TextInput organizationName;
    private CheckBoxInput themesSelector;
    private ListInput defaultThemeSelector;
    private InputInlineGroup adminsGroup1;
    private List<TextInput> adminInputs;
    private TextInput selectedAdminInput;
    private boolean usernameAvailable;
    private boolean organizationNameAvailable;
    private String errorMessage;

    @Inject
    CreateOrganizationView(
            Binder uiBinder,
            CommonI18nLabels labels,
            CommonI18nErrorMessages errorMessages,
            MessageDispatcher messageDispatcher) {
        this.labels = labels;
        this.errorMessages = errorMessages;
        this.messageDispatcher = messageDispatcher;

        initWidget(uiBinder.createAndBindUi(this));
        setupForm();
        setupOrgInputEvent();
    }

    @Override
    public void displayThemes(List<Theme> themes) {
        String[] labels = new String[themes.size()];
        String[] options = new String[themes.size()];

        for (int i = 0; i < themes.size(); i++) {
            labels[i] = themes.get(i).getName();
            options[i] = Integer.toString(themes.get(i).getId());
        }

        themesSelector.setLabelsAndOptions(labels, options);
        defaultThemeSelector.setLabelsAndOptions(labels, options);
        removeStyles();
    }

    @Override
    public void setUsernameDisponibility(boolean available) {
        this.usernameAvailable = available;

        if (usernameAvailable) {
            selectedAdminInput.removeStyleName("cu-error-text-field");
            selectedAdminInput.addStyleName("cu-success-availability-text-field");
        } else {
            selectedAdminInput.removeStyleName("cu-success-availability-text-field");
            selectedAdminInput.addStyleName("cu-error-text-field");
        }
    }

    @Override
    public void setOrganizationNameDisponibility(boolean available) {
        this.organizationNameAvailable = available;

        if (organizationNameAvailable) {
            organizationName.removeStyleName("cu-error-text-field");
            organizationName.addStyleName("cu-success-availability-text-field");
        } else {
            organizationName.removeStyleName("cu-success-availability-text-field");
            organizationName.addStyleName("cu-error-text-field");
        }
    }

    @UiHandler("buttonSave")
    void onClick(ClickEvent e) {
        if (organizationNameAvailable) {
            errorMessage = "";
            if (validate()) {
                Organization org = new Organization();
                org.setName(form.getValue(NAME_FIELD_NAME));
                org.setShortName(form.getValue(SHORTNAME_FIELD_NAME));
                org.setPermalink(form.getValue(URL_FIELD_NAME));
                org.setDefaultTheme(Integer.parseInt(form.getValue(DEFAULT_THEME_FIELD_NAME)));

                List<Contact> contacts = getContactInformation();
                List<Address> addresses = getAddressInformation();
                List<Integer> themes = getThemeInformation();
                List<String> admins = getAdminValues();

                removeStyles();
                getUiHandlers().createOrganization(org, contacts, addresses, admins,
                        themes);
                resetFormValues();
            } else {
                messageDispatcher.displayMessage(MessageStyle.ERROR, errorMessage);
            }
        } else {
            messageDispatcher.displayMessage(MessageStyle.ERROR, "The given credentials are not allowed");
        }
    }

    private List<Contact> getContactInformation() {
        List<Contact> contacts = new ArrayList<Contact>();
        Contact phone = new Contact();
        phone.setType(Contact.TYPE_WORK);
        phone.setMedium(Contact.MediumType.PHONE.name());
        phone.setValue(form.getValue(PHONE_FIELD_NAME));
        contacts.add(phone);

        Contact email = new Contact();
        email.setType(Contact.TYPE_WORK);
        email.setMedium(Contact.MediumType.EMAIL.name());
        email.setValue(form.getValue(EMAIL_FIELD_NAME));
        contacts.add(email);

        return contacts;
    }

    private List<Address> getAddressInformation() {
        List<Address> addresses = new ArrayList<Address>();
        Address address = new Address();
        address.setAddress1(form.getValue(STREET_ADDRESS_1_FIELD_NAME));
        address.setAddress2(form.getValue(STREET_ADDRESS_2_FIELD_NAME));
        address.setCity(form.getValue(CITY_FIELD_NAME));
        address.setCountry("US");
        address.setPostalCode(form.getValue(ZIP_FIELD_NAME));
        address.setState(form.getValue(STATE_FIELD_NAME));
        address.setRelation(Address.RELATION_WORK);
        addresses.add(address);

        return addresses;
    }

    private List<Integer> getThemeInformation() {
        List<Integer> themes = new ArrayList<Integer>();
        List<String> rawValues = form.getMultiValue(THEMES_FIELD_NAME);

        for (String raw : rawValues) {
            int id = Integer.parseInt(raw);
            themes.add(id);
        }

        return themes;
    }

    private List<String> getAdminValues() {
        List<String> admins = new ArrayList<String>();

        for (TextInput input : adminInputs) {
            String value = input.getValue();
            if (!Strings.isNullOrEmpty(value)) {
                admins.add(value);
            }
        }
        return admins;
    }

    private boolean validate() {
        // Only the first admin input is required
        selectedAdminInput = (TextInput) adminsGroup1.getWidget(1);
        if (Strings.isNullOrEmpty(selectedAdminInput.getValue())) {
            errorMessage = errorMessages.onAdminRequired();
            return false;
        }

        errorMessage = form.validate(DEFAULT_THEME_FIELD_NAME, THEMES_FIELD_NAME, errorMessage);

        return Strings.isNullOrEmpty(errorMessage);
    }

    private void setupForm() {
        buttonSave.setText(labels.createOrganization());
        createOrganizationLbl.setText(labels.createOrganization());

        organizationName = form.add(new TextInput(labels.organizationName(), NAME_FIELD_NAME, true));

        adminInputs = new ArrayList<TextInput>();
        adminsGroup1 = createAdminsInputLine(true, 1);
        InputInlineGroup adminsGroup2 = createAdminsInputLine(false, 5);
        selectedAdminInput = (TextInput) adminsGroup1.getWidget(1);

        form.addSubWidget(4, new Label(labels.organizationAdminUsers()));
        form.addSubWidget(4, adminsGroup1);
        form.addSubWidget(4, adminsGroup2);

        form.add(new TextInput(labels.organizationShortname(), SHORTNAME_FIELD_NAME, true));
        form.setMinLength(SHORTNAME_FIELD_NAME, 2);
        form.setMaxLength(SHORTNAME_FIELD_NAME, 15);
        form.add(new TextInput(labels.url(), URL_FIELD_NAME, true));
        form.add(new TextInput(labels.standQuestStreet(), STREET_ADDRESS_1_FIELD_NAME, true));
        form.add(new TextInput("", STREET_ADDRESS_2_FIELD_NAME, false));
        form.add(new TextInput(labels.standQuestCity(), CITY_FIELD_NAME, true));

        String[] states = StatesUtil.STATE_OPTIONS.concat(", " + StatesUtil.usTerritories)
                .concat(", " + StatesUtil.armedForces)
                .split(",\\s*");
        form.add(new ListInput(labels.standQuestState(), STATE_FIELD_NAME, states, states, true, true, false, null));

        form.add(new TextInput(labels.standQuestZip(), ZIP_FIELD_NAME, true));
        form.setValidator(ZIP_FIELD_NAME, new ZipCodeValidator());
        form.add(new TextInput(labels.standQuestPhone(), PHONE_FIELD_NAME, true));
        form.setValidator(PHONE_FIELD_NAME, new PhoneNumberValidator());
        form.add(new TextInput(labels.standQuestEmail(), EMAIL_FIELD_NAME, true));
        form.setValidator(EMAIL_FIELD_NAME, new EmailValidator());

        themesSelector = createThemesSelector();
        defaultThemeSelector = createDefaultThemeSelector();

        form.add(themesSelector);
        form.add(defaultThemeSelector);
    }

    private void setupEvent(final TextInput adminInput) {
        adminInputs.add(adminInput);
        adminInput.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                selectedAdminInput = adminInput;
                if (!Strings.isNullOrEmpty(adminInput.getValue().trim())) {
                    getUiHandlers().checkAdminNameExists(adminInput.getValue());
                } else {
                    removeStyles();
                }
            }
        });
    }

    private void setupOrgInputEvent() {
        organizationName.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (!Strings.isNullOrEmpty(form.getValue(NAME_FIELD_NAME).trim())) {
                    getUiHandlers().checkOrganizationDisponibility(form.getValue(NAME_FIELD_NAME));
                }
            }
        });
    }

    private CheckBoxInput createThemesSelector() {
        return new CheckBoxInput(labels.themes(), THEMES_FIELD_NAME, true);
    }

    private ListInput createDefaultThemeSelector() {
        return new ListInput(labels.defaultTheme(), DEFAULT_THEME_FIELD_NAME, true, true,
                false);
    }

    private InputInlineGroup createAdminsInputLine(Boolean firstInput, Integer startIndex) {
        TextInput admin1 = new TextInput("", generateKey(++startIndex), firstInput);
        setupEvent(admin1);
        TextInput admin2 = new TextInput("", generateKey(++startIndex), false);
        setupEvent(admin2);
        TextInput admin3 = new TextInput("", generateKey(++startIndex), false);
        setupEvent(admin3);
        TextInput admin4 = new TextInput("", generateKey(++startIndex), false);
        setupEvent(admin4);

        InputInlineGroup inputGroup = new InputInlineGroup(new Label(""), admin1, null);
        inputGroup.getLayout().add(admin2);
        inputGroup.getLayout().add(admin3);
        inputGroup.getLayout().add(admin4);

        return inputGroup;
    }

    private String generateKey(Integer startIndex) {
        return ADMIN_USER_FIELD_NAME + startIndex;
    }

    private void resetFormValues() {
        form.setValue(NAME_FIELD_NAME, "");
        form.setValue(SHORTNAME_FIELD_NAME, "");
        form.setValue(URL_FIELD_NAME, "");
        form.setValue(ADMIN_USER_FIELD_NAME, "");
        form.setValue(STREET_ADDRESS_1_FIELD_NAME, "");
        form.setValue(STREET_ADDRESS_2_FIELD_NAME, "");
        form.setValue(CITY_FIELD_NAME, "");
        form.setValue(ZIP_FIELD_NAME, "");
        form.setValue(PHONE_FIELD_NAME, "");
        form.setValue(EMAIL_FIELD_NAME, "");
        form.setValue(STATE_FIELD_NAME, ListInput.BLANK_VALUE);
        form.setValue(THEMES_FIELD_NAME, "");
        form.setValue(DEFAULT_THEME_FIELD_NAME, "");
        usernameAvailable = true;
        organizationNameAvailable = true;
        resetFormAdminInputs();
    }

    private void resetFormAdminInputs() {
        for (TextInput input : adminInputs) {
            input.setValue("");
        }
    }

    private void removeStyles() {
        usernameAvailable = true;
        organizationNameAvailable = true;
        organizationName.removeStyleName("cu-success-availability-text-field");
        organizationName.removeStyleName("cu-error-text-field");
        selectedAdminInput.removeStyleName("cu-success-availability-text-field");
        selectedAdminInput.removeStyleName("cu-error-text-field");
    }
}
