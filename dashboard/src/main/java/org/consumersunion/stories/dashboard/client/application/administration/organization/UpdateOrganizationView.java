package org.consumersunion.stories.dashboard.client.application.administration.organization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.consumersunion.stories.common.client.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.common.client.ui.TextPager;
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
import org.consumersunion.stories.common.shared.model.StorySortField;
import org.consumersunion.stories.common.shared.model.Theme;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.common.shared.service.datatransferobject.OrganizationSummary;
import org.consumersunion.stories.common.shared.util.StatesUtil;
import org.consumersunion.stories.dashboard.client.application.administration.AdminManagerUiHandlers;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortList;
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

public class UpdateOrganizationView extends ViewWithUiHandlers<AdminManagerUiHandlers>
        implements UpdateOrganizationPresenter.MyView {
    interface Binder extends UiBinder<Widget, UpdateOrganizationView> {
    }

    private static final String NAME_FIELD_NAME = "txtName";
    private static final String SHORTNAME_FIELD_NAME = "txtShortname";
    private static final String URL_FIELD_NAME = "txtUrl";
    private static final String ADMIN_USER_FIELD_NAME = "txtUser";
    private static final String STREET_ADDRESS_1_FIELD_NAME = "txtAddress1";
    private static final String STREET_ADDRESS_2_FIELD_NAME = "txtAddress2";
    private static final String CITY_FIELD_NAME = "txtCity";
    private static final String STATE_FIELD_NAME = "lbxState";
    private static final String ZIP_FIELD_NAME = "txtZipCode";
    private static final String PHONE_FIELD_NAME = "txtPhone";
    private static final String EMAIL_FIELD_NAME = "txtEmail";
    private static final String THEMES_FIELD_NAME = "listThemes";
    private static final String DEFAULT_THEME_FIELD_NAME = "defaultTheme";

    @UiField(provided = true)
    final CellTable<OrganizationSummary> orgsTable;
    @UiField
    HTMLPanel mainPanel;
    @UiField(provided = true)
    final TextPager pager;
    @UiField
    IntegerBox pageSize;
    @UiField
    Label displayLabel;
    @UiField
    Label rowsLabel;
    @UiField
    Label formTitle;
    @UiField
    OrganizationForm form;
    @UiField
    Button buttonSave;

    private final CommonI18nLabels labels;
    private final CommonI18nErrorMessages errorMessages;
    private final MessageDispatcher messageDispatcher;
    private final ProvidesKey<OrganizationSummary> keyProvider;
    private final SelectionModel<OrganizationSummary> selectionModel;
    private final HashMap<Column<OrganizationSummary, ?>, StorySortField> columnSortFieldMap;

    private Organization currentOrg;
    private TextInput organizationName;
    private CheckBoxInput themesSelector;
    private ListInput defaultThemeSelector;
    private InputInlineGroup adminsGroup1;
    private List<TextInput> adminInputs;
    private TextInput selectedAdminInput;
    private boolean usernameAvailable;
    private boolean organizationNameAvailable;
    private List<String> selectedThemes = new ArrayList<String>();
    private final AsyncDataProvider<OrganizationSummary> dataProvider;
    private String errorMessage;

    @Inject
    UpdateOrganizationView(
            Binder uiBinder,
            CommonI18nLabels labels,
            CommonI18nErrorMessages errorMessages,
            MessageDispatcher messageDispatcher,
            TextPager pager) {
        this.labels = labels;
        this.errorMessages = errorMessages;
        this.messageDispatcher = messageDispatcher;
        this.pager = pager;
        keyProvider = setupKeyProvider();
        dataProvider = setupDataProvider();
        orgsTable = new CellTable<OrganizationSummary>(UpdateOrganizationPresenter.PAGE_SIZE, keyProvider);
        selectionModel = new SingleSelectionModel<OrganizationSummary>(keyProvider);
        columnSortFieldMap = new HashMap<Column<OrganizationSummary, ?>, StorySortField>();

        initWidget(uiBinder.createAndBindUi(this));

        pager.setDisplay(orgsTable);
        pageSize.setValue(UpdateOrganizationPresenter.PAGE_SIZE);
        orgsTable.setPageSize(pageSize.getValue());
        orgsTable.setSelectionModel(selectionModel);
        orgsTable.addColumnSortHandler(new ColumnSortEvent.AsyncHandler(orgsTable));
        dataProvider.addDataDisplay(orgsTable);
        setupColumns();
        setupForm();
    }

    @Override
    public void reloadData() {
        orgsTable.setVisibleRangeAndClearData(orgsTable.getVisibleRange(), true);
    }

    @Override
    public void setCurrentOrganization(OrganizationSummary organizationSummary) {
        currentOrg = organizationSummary.getOrganization();
        List<OrganizationSummary> updated = new ArrayList<OrganizationSummary>();
        for (OrganizationSummary summary : orgsTable.getVisibleItems()) {
            if (summary.getOrganization().getId() == currentOrg.getId()) {
                updated.add(organizationSummary);
            } else {
                updated.add(summary);
            }
        }
        dataProvider.updateRowData(0, updated);
        setSelectedOrg(organizationSummary);
    }

    @Override
    public void resetForm() {
        form.clear();
        setupForm();
    }

    @Override
    public void setData(List<OrganizationSummary> data, int start, int totalCount) {
        dataProvider.updateRowCount(totalCount, true);
        dataProvider.updateRowData(start, data);
    }

    @Override
    public void setThemeList(List<Theme> themes) {
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
    public void displayOrganizationThemes(List<String> selectedThemes) {
        // clear all checks
        themesSelector.resetValues();
        // then set the proper checks
        if (selectedThemes.size() > 0) {
            this.selectedThemes = selectedThemes;
            themesSelector.setValues(selectedThemes);
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

    private AsyncDataProvider<OrganizationSummary> setupDataProvider() {
        return new AsyncDataProvider<OrganizationSummary>(keyProvider) {
            @Override
            protected void onRangeChanged(HasData<OrganizationSummary> display) {
                fetchData(display);
            }
        };
    }

    private void setupColumns() {
        Column<OrganizationSummary, String> defaultSortColumn = setupOrgNameColumn();

        orgsTable.addColumn(setupOrgNameColumn(), labels.organizationName());
        orgsTable.getColumnSortList().push(defaultSortColumn);
    }

    private Column<OrganizationSummary, String> setupOrgNameColumn() {
        ClickableTextCell orgName = new ClickableTextCell();
        Column<OrganizationSummary, String> orgNameColumn = new Column<OrganizationSummary, String>(orgName) {
            @Override
            public String getValue(OrganizationSummary summary) {
                return summary.getOrganization().getName();
            }
        };
        orgNameColumn.setSortable(true);
        orgNameColumn.setFieldUpdater(createStaticColumnFieldUpdater(orgNameColumn));

        return orgNameColumn;
    }

    private <C> FieldUpdater<OrganizationSummary, C> createStaticColumnFieldUpdater(
            Column<OrganizationSummary, C> column) {
        return new FieldUpdater<OrganizationSummary, C>() {
            @Override
            public void update(int index, OrganizationSummary organization, C updated) {
                currentOrg = organization.getOrganization();
                selectionModel.setSelected(organization, true);
                setSelectedOrg(organization);
            }
        };
    }

    private void setSelectedOrg(OrganizationSummary orgSummary) {
        resetFormValues();
        getUiHandlers().loadOrganizationThemes(currentOrg);

        form.setValue(NAME_FIELD_NAME, currentOrg.getName());
        form.setValue(SHORTNAME_FIELD_NAME, currentOrg.getShortName());
        form.setValue(URL_FIELD_NAME, currentOrg.getPermalink());

        for (Integer key : orgSummary.getAdmins().keySet()) {
            String adminValue = orgSummary.getAdmins().get(key).getHandle();
            adminInputs.get(key).setValue(adminValue);
        }

        if (orgSummary.getAddress() != null) {
            form.setValue(STREET_ADDRESS_1_FIELD_NAME, orgSummary.getAddress().getAddress1());
            form.setValue(STREET_ADDRESS_2_FIELD_NAME, orgSummary.getAddress().getAddress2());
            form.setValue(CITY_FIELD_NAME, orgSummary.getAddress().getCity());
            form.setValue(ZIP_FIELD_NAME, orgSummary.getAddress().getPostalCode());
            form.setValue(STATE_FIELD_NAME, MoreObjects.firstNonNull(orgSummary.getAddress().getState(), ""));
        }

        if (orgSummary.getPhoneContact() != null) {
            form.setValue(PHONE_FIELD_NAME, orgSummary.getPhoneContact().getValue());
        }
        if (orgSummary.getEmailContact() != null) {
            form.setValue(EMAIL_FIELD_NAME, orgSummary.getEmailContact().getValue());
        }
        form.setMultiValues(THEMES_FIELD_NAME, selectedThemes);
        form.setValue(DEFAULT_THEME_FIELD_NAME, MoreObjects.firstNonNull(currentOrg.getDefaultTheme().toString(), ""));
    }

    private void fetchData(HasData<OrganizationSummary> display) {
        Range range = display.getVisibleRange();
        StorySortField sortField = getSortField();
        boolean ascending = isAscendingSort();

        if (getUiHandlers() != null) {
            getUiHandlers().loadAdminOrganizations(range.getStart(), range.getLength(), sortField, ascending);
        }
    }

    private boolean isAscendingSort() {
        if (orgsTable.getColumnSortList().size() > 0) {
            ColumnSortList.ColumnSortInfo sortInfo = orgsTable.getColumnSortList().get(0);
            return sortInfo.isAscending();
        }
        return true;
    }

    private StorySortField getSortField() {
        if (orgsTable.getColumnSortList().size() > 0) {
            Column<?, ?> column = orgsTable.getColumnSortList().get(0).getColumn();
            StorySortField sortField = columnSortFieldMap.get(column);
            if (sortField != null) {
                return sortField;
            }
        }
        return StorySortField.ID;
    }

    private ProvidesKey<OrganizationSummary> setupKeyProvider() {
        return new ProvidesKey<OrganizationSummary>() {
            @Override
            public Object getKey(OrganizationSummary organizationSummary) {
                return (organizationSummary == null) ? null : organizationSummary.getOrganization().getId();
            }
        };
    }

    @UiHandler("buttonSave")
    void onClick(ClickEvent e) {
        errorMessage = "";
        if (validate()) {
            currentOrg.setName(form.getValue(NAME_FIELD_NAME));
            currentOrg.setShortName(form.getValue(SHORTNAME_FIELD_NAME));
            currentOrg.setPermalink(form.getValue(URL_FIELD_NAME));
            currentOrg.setDefaultTheme(Integer.parseInt(form.getValue(DEFAULT_THEME_FIELD_NAME)));

            List<Contact> contacts = getContactInformation();
            List<Address> addresses = getAddressInformation();
            List<Integer> themes = getThemeInformation();
            List<String> admins = getAdminValues();

            removeStyles();
            getUiHandlers().updateOrganization(currentOrg, contacts, addresses, themes, admins);
            resetFormValues();
        } else {
            messageDispatcher.displayMessage(MessageStyle.ERROR, errorMessage);
        }
    }

    @UiHandler("pageSize")
    void onPageSizeKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            if (pageSize.getValue() == null) {
                messageDispatcher.displayMessage(MessageStyle.ERROR, "Please enter a integer value greater than 0.");
            } else {
                orgsTable.setPageSize(pageSize.getValue());
                orgsTable.setPageStart(0);

                fetchData(orgsTable);
            }
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
        address.setState(form.getValue(STATE_FIELD_NAME));
        address.setPostalCode(form.getValue(ZIP_FIELD_NAME));
        address.setRelation(Address.RELATION_WORK);
        addresses.add(address);
        return addresses;
    }

    private List<Integer> getThemeInformation() {
        List<Integer> themes = new ArrayList<Integer>();
        List<String> rawValues = form.getMultiValue(THEMES_FIELD_NAME);
        selectedThemes = rawValues;

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
        buttonSave.setText(labels.updateOrganization());
        formTitle.setText(labels.updateOrganization());

        orgsTable.getElement().getStyle().setProperty("border", "1px solid black");
        orgsTable.setWidth("100%");

        organizationName = form.add(new TextInput(labels.organizationName(), NAME_FIELD_NAME, true));
        organizationName.setEnabled(false);

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
                if (!Strings.isNullOrEmpty(adminInput.getValue().trim())) {
                    getUiHandlers().checkAdminNameExists(adminInput.getValue());
                } else {
                    removeStyles();
                }
            }
        });
    }

    private CheckBoxInput createThemesSelector() {
        return new CheckBoxInput(labels.themes(), THEMES_FIELD_NAME, true);
    }

    private ListInput createDefaultThemeSelector() {
        return new ListInput(labels.defaultTheme(), DEFAULT_THEME_FIELD_NAME, true, true, false);
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
        selectedAdminInput.removeStyleName("cu-success-availability-text-field");
        selectedAdminInput.removeStyleName("cu-error-text-field");
    }
}
