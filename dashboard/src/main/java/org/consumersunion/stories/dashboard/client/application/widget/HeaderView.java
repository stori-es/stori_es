package org.consumersunion.stories.dashboard.client.application.widget;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.client.service.datatransferobject.ProfileDropDownItem;
import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.dashboard.client.application.ui.ClickableDropDown;
import org.consumersunion.stories.dashboard.client.application.ui.NavigationDropDownHover;
import org.consumersunion.stories.dashboard.shared.MenuItem;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import static com.google.gwt.query.client.GQuery.$;

public class HeaderView extends ViewWithUiHandlers<HeaderUiHandlers> implements HeaderPresenter.MyView {
    interface Binder extends UiBinder<Widget, HeaderView> {
    }

    private static final int MENU_WIDTH = 30;

    @UiField(provided = true)
    final ClickableDropDown<ProfileDropDownItem> profileDropDown;
    @UiField(provided = true)
    final NavigationDropDownHover<MenuItem> adminDropDown;

    @UiField
    Label username;
    @UiField
    Anchor logout;
    @UiField
    Label selectedMenu;
    @UiField
    Label organizationLabel;
    @UiField
    DivElement stories;
    @UiField
    DivElement collections;

    private final StoryTellerDashboardI18nLabels labels;

    private int itemSize;
    private boolean showAdminMenu = false;
    private List<MenuItem> menuItems;

    @Inject
    HeaderView(
            Binder uiBinder,
            StoryTellerDashboardI18nLabels labels,
            ClickableDropDown<ProfileDropDownItem> profileDropDown,
            NavigationDropDownHover<MenuItem> adminDropDown) {
        this.labels = labels;
        this.profileDropDown = profileDropDown;
        this.adminDropDown = adminDropDown;

        initWidget(uiBinder.createAndBindUi(this));
        setupI18nLabelsAndIds();
        setupDropDownEvents();

        this.adminDropDown.setVisible(false);
        organizationLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().gotoOrganizationDetail();
            }
        });
    }

    @Override
    public void reloadInfo(String login, boolean isAdmin) {
        username.setText(login);
        setupAdminDropDown(isAdmin);
    }

    @Override
    public void displayOrganizationTitle(String title) {
        profileDropDown.setTitle(title);
    }

    @Override
    public void loadProfiles(List<ProfileSummary> roles) {
        if (roles.size() > 1) {
            profileDropDown.setVisible(true);
            organizationLabel.setVisible(false);
            profileDropDown.loadOptions(FluentIterable.from(roles).transform(
                    new Function<ProfileSummary, ProfileDropDownItem>() {
                        @Override
                        public ProfileDropDownItem apply(ProfileSummary input) {
                            return new ProfileDropDownItem(input);
                        }
                    }).toList(), false);
        } else if (roles.size() == 1) {
            profileDropDown.setVisible(false);
            organizationLabel.setVisible(true);
            organizationLabel.setText("(" + roles.get(0).getOrganizationName() + ")");
        }
    }

    @Override
    public void showAdminMenu(MenuItem item) {
        menuItems.add(item);
        if (menuItems.size() == itemSize) {
            adminDropDown.loadOptions(menuItems, false);
        }
        showAdminMenu = showAdminMenu || item.isVisible();
        adminDropDown.setVisible(showAdminMenu);
    }

    @Override
    public void selectStoriesMenu() {
        displaySelectedMenuAt(stories.getAbsoluteLeft(), stories.getOffsetWidth());
    }

    @Override
    public void selectCollectionsMenu() {
        displaySelectedMenuAt(collections.getAbsoluteLeft(), collections.getOffsetWidth());
    }

    @Override
    public void setLogin(String login) {
        username.setText(login);
    }

    @UiHandler("logo")
    void onLogoClick(ClickEvent event) {
        Window.open("https://stori.es/stories.jsp", "_self", null);
    }

    @UiHandler("logout")
    void onLogoutClick(ClickEvent event) {
        getUiHandlers().logout();
    }

    @UiHandler("username")
    void onUsernameClicked(ClickEvent event) {
        getUiHandlers().userAccount();
    }

    private void setupI18nLabelsAndIds() {
        logout.setText(labels.signOut());
        stories.setInnerText(labels.myStories());
        stories.setId("stories-header-view-stories");
        collections.setInnerText(labels.myCollections());
        adminDropDown.setTitle(labels.admin());
    }

    private void setupAdminDropDown(boolean admin) {
        if (admin) {
            menuItems = new ArrayList<MenuItem>();
            itemSize = MenuItem.Type.getActiveMenus().size();
            for (MenuItem.Type type : MenuItem.Type.getActiveMenus()) {
                getUiHandlers().authorizeMenu(new MenuItem(type));
            }
            adminDropDown.setVisible(true);
        } else {
            adminDropDown.setVisible(false);
        }
    }

    private void setupDropDownEvents() {
        profileDropDown.setDropDownHandler(new ClickableDropDown.DropDownHandler<ProfileDropDownItem>() {
            public void onLoadSpecificItem(ProfileDropDownItem item) {
                ProfileSummary profileSummary = item.getProfileSummary();
                getUiHandlers().specificProfile(profileSummary);
                profileDropDown.setTitle(profileSummary.getOrganizationName());
            }
        });

        $(stories).click(new com.google.gwt.query.client.Function() {
            @Override
            public void f() {
                getUiHandlers().allStories();
                displaySelectedMenuAt(stories.getAbsoluteLeft(), stories.getOffsetWidth());
            }
        });

        $(collections).click(new com.google.gwt.query.client.Function() {
            @Override
            public void f() {
                getUiHandlers().allCollections();
                displaySelectedMenuAt(collections.getAbsoluteLeft(), collections.getOffsetWidth());
            }
        });

        adminDropDown.setDropDownHandler(new NavigationDropDownHover.DropDownHandler<MenuItem>() {
            @Override
            public void onLoadAll() {
                selectedMenu.setVisible(false);
            }

            @Override
            public void onLoadSpecificItem(MenuItem item) {
                getUiHandlers().administration(item);
                displaySelectedMenuAt(adminDropDown.getAbsoluteLeft(), adminDropDown.getOffsetWidth());
            }
        });
    }

    private void displaySelectedMenuAt(int offset, int width) {
        int translateX = offset + (width / 2) - MENU_WIDTH;

        selectedMenu.setVisible(true);
        selectedMenu.getElement().getStyle().setLeft(translateX, Style.Unit.PX);
    }
}
