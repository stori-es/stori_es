package org.consumersunion.stories.dashboard.client.application.ui.block.configurator;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.client.service.RpcCollectionServiceAsync;
import org.consumersunion.stories.common.client.service.RpcStoryServiceAsync;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.ui.EntitySuggest;
import org.consumersunion.stories.common.client.ui.EntitySuggestFactory;
import org.consumersunion.stories.common.client.ui.EntitySuggestionOracle;
import org.consumersunion.stories.common.client.ui.StorySuggestionOracle;
import org.consumersunion.stories.common.client.util.ResponseHandlerLoader;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.dashboard.client.resource.GeneralStyle;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import static com.google.gwt.query.client.GQuery.$;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;

public class CollectionStoryConfigurator extends AbstractConfigurator<Content>
        implements BlockConfigurator<Content> {
    interface Binder extends UiBinder<Widget, CollectionStoryConfigurator> {
    }

    interface Template extends SafeHtmlTemplates {
        @Template("<div class=\"{0}\"><span class=\"{4} {5}\"></span><span>{1}</span>&nbsp;" +
                "<span class=\"simptip-position-top simptip-movable {2}\" data-tooltip=\"{3}\"></span></div>")
        SafeHtml token(String mainClass, String title, String deleteStyle, String tooltip, String iconStyle,
                String icon);
    }

    @UiField(provided = true)
    final EntitySuggest<Collection> collectionBox;
    @UiField(provided = true)
    final EntitySuggest<StorySummary> storyBox;

    @UiField
    Label collectionError;
    @UiField
    HTML collectionToken;
    @UiField
    HTML storyToken;
    @UiField
    DivElement storyBoxContainer;
    @UiField
    Label storyError;
    @UiField
    Label storyLabel;

    private final StorySuggestionOracle storySuggestionOracle;
    private final RpcStoryServiceAsync storyServiceAsync;
    private final RpcCollectionServiceAsync collectionService;
    private final StoryTellerDashboardI18nLabels labels;
    private final CommonI18nLabels commonLabels;
    private final Template template;
    private final GeneralStyle generalStyle;
    private final boolean isStory;

    private Collection collection;
    private StorySummary story;

    @Inject
    CollectionStoryConfigurator(
            Binder uiBinder,
            Resources resources,
            StoryTellerDashboardI18nLabels labels,
            CommonI18nLabels commonLabels,
            Template template,
            EntitySuggestFactory<StorySummary> storySuggestFactory,
            EntitySuggest<Collection> collectionBox,
            StorySuggestionOracle storySuggestionOracle,
            RpcStoryServiceAsync storyServiceAsync,
            RpcCollectionServiceAsync collectionService,
            @Assisted Content content) {
        super(null, content);

        this.labels = labels;
        this.commonLabels = commonLabels;
        this.template = template;
        this.collectionBox = collectionBox;
        this.storySuggestionOracle = storySuggestionOracle;
        this.storyServiceAsync = storyServiceAsync;
        this.storyBox = storySuggestFactory.create(storySuggestionOracle);
        this.collectionService = collectionService;
        generalStyle = resources.generalStyleCss();
        isStory = BlockType.STORY.equals(content.getFormType());

        initSuggestBoxes();

        initWidget(uiBinder.createAndBindUi(this));

        if (!Strings.isNullOrEmpty(content.getContent())) {
            init();
        }

        if (isStory) {
            setErrorLabels(collectionError, storyError);
            setStoryBoxEnabled(false);
        } else {
            setErrorLabels(collectionError);
            storyBoxContainer.removeFromParent();
        }
    }

    @Override
    public boolean validate() {
        if (isStory && collection != null && story != null || !isStory && collection != null) {
            resetErrors();

            return true;
        } else {
            if (isStory) {
                if (collection == null) {
                    collectionError.setText(messages.requiredField());
                }
                if (story == null) {
                    storyError.setText(messages.requiredField());
                }
            } else {
                collectionError.setText(messages.requiredField());
            }

            return false;
        }
    }

    @Override
    public boolean isNew() {
        return Strings.isNullOrEmpty(getEditedValue().getContent());
    }

    @Override
    protected void init() {
        super.init();

        String content[] = getEditedValue().getContent().split("/");

        String collectionId = content[0];
        if (content.length == 2) {
            storyServiceAsync.getStorySummary(Integer.valueOf(content[1]),
                    new ResponseHandlerLoader<DatumResponse<StorySummary>>() {
                        @Override
                        public void handleSuccess(DatumResponse<StorySummary> result) {
                            onStorySelected(result.getDatum());
                        }
                    });
        }

        collectionService.getCollection(Integer.valueOf(collectionId), ROLE_READER,
                new ResponseHandlerLoader<DatumResponse<CollectionData>>() {
                    @Override
                    public void handleSuccess(DatumResponse<CollectionData> result) {
                        onCollectionSelected(result.getDatum().getCollection());
                    }
                });
    }

    @Override
    protected void onDone() {
        if (validate()) {
            Content oldContent = getEditedValue();
            Content content = (Content) oldContent.clone();
            content.setContent(getValue());
            content.setFormType(oldContent.getFormType());
            content.setStandardMeaning(oldContent.getStandardMeaning());
            doneCallback.onSuccess(content);

            setEditedValue(content);
        }
    }

    private String getValue() {
        if (isStory) {
            return collection.getId() + "/" + story.getStoryId();
        } else {
            return String.valueOf(collection.getId());
        }
    }

    private void onRemoveCollection() {
        collection = null;
        removeTokenAndShowBox(collectionToken, collectionBox);

        onRemoveStory();

        setStoryBoxEnabled(false);
    }

    private void onRemoveStory() {
        story = null;
        removeTokenAndShowBox(storyToken, storyBox);
    }

    private void removeTokenAndShowBox(
            HTML token,
            Widget suggestBox) {
        token.setHTML("");
        token.setVisible(false);
        setSuggestVisible(suggestBox, true);
    }

    private void onStorySelected(StorySummary story) {
        this.story = story;

        String title = MoreObjects.firstNonNull(story.getTitle(), commonLabels.untitled());

        showToken(storyToken, title, generalStyle.red(), "icon-comment", storyBox,
                new Function() {
                    @Override
                    public void f() {
                        onRemoveStory();
                    }
                });
    }

    private void onCollectionSelected(Collection collection) {
        this.collection = collection;

        showToken(collectionToken, collection.getTitle(), generalStyle.yellow(), "icon-folder-close", collectionBox,
                new Function() {
                    @Override
                    public void f() {
                        onRemoveCollection();
                    }
                });

        if (isStory) {
            storySuggestionOracle.setCollection(collection);
            setStoryBoxEnabled(true);
        }
    }

    private void showToken(
            HTML token,
            String title,
            String color,
            String icon,
            Widget suggestBox,
            final Function function) {

        SafeHtml tokenHtml = template.token(
                generalStyle.tokenBox(),
                title,
                generalStyle.delete(),
                labels.clickToDelete(),
                generalStyle.folderStyle() + " " + color,
                icon);
        token.setHTML(tokenHtml);

        final GQuery deleteSelector = $("." + generalStyle.delete(), token);
        deleteSelector.click(new Function() {
            @Override
            public void f() {
                function.f();
                deleteSelector.unbind(BrowserEvents.CLICK, this);
            }
        });

        token.setVisible(true);

        setSuggestVisible(suggestBox, false);
    }

    private void setSuggestVisible(Widget widget, boolean visible) {
        GQuery parent = $(widget).parent();
        if (visible) {
            parent.show();
        } else {
            parent.hide();
        }
    }

    private void initSuggestBoxes() {
        collectionBox.setHandler(new EntitySuggestionOracle.EntitySuggestionHandler<Collection>() {
            @Override
            public void onEntitySelected(Collection entity) {
                onCollectionSelected(entity);
            }
        });
        collectionBox.setPlaceHolder(labels.searchCollectionByTitle());

        if (isStory) {
            storyBox.setHandler(new EntitySuggestionOracle.EntitySuggestionHandler<StorySummary>() {
                @Override
                public void onEntitySelected(StorySummary story) {
                    onStorySelected(story);
                }
            });

            setStoryBoxEnabled(false);
            storyBox.setPlaceHolder(labels.searchStoryByTitle());
        }
    }

    private void setStoryBoxEnabled(boolean enabled) {
        storyBox.setEnabled(enabled);
        $(storyLabel).toggleClass(resources.builderStyleCss().disabled(), !enabled);
    }
}
