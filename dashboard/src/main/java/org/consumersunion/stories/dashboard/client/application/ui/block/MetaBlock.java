package org.consumersunion.stories.dashboard.client.application.ui.block;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.dashboard.client.application.ui.builder.MetaBlockFactory;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardToolbarButton;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardToolbarButtonFactory;
import org.consumersunion.stories.dashboard.client.application.widget.card.ToolbarButtonActionHandler;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.common.collect.Multimap;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.assistedinject.Assisted;

import static com.google.gwt.query.client.GQuery.$;

public class MetaBlock extends Composite {
    interface Binder extends UiBinder<Widget, MetaBlock> {
    }

    @UiField(provided = true)
    final CardToolbarButton trash;

    @UiField
    DivElement contentBlocks;
    @UiField
    DivElement questionBlocks;
    @UiField
    DivElement questionBankLeft;
    @UiField
    DivElement questionBankRight;
    @UiField
    Resources resources;
    @UiField
    HTMLPanel block;

    private final CommonI18nLabels labels;
    private final MetaBlockFactory metaBlockFactory;
    private final MetaBlockHandler handler;

    @Inject
    MetaBlock(
            Binder binder,
            CommonI18nLabels labels,
            MetaBlockFactory metaBlockFactory,
            CardToolbarButtonFactory cardToolbarButtonFactory,
            @Assisted MetaBlockHandler handler) {
        this.labels = labels;
        this.metaBlockFactory = metaBlockFactory;
        this.handler = handler;

        trash = cardToolbarButtonFactory.createDeleteButton(new ToolbarButtonActionHandler() {
            @Override
            public void executeAction() {
            }

            @Override
            public void onButtonClicked() {
                removeAndReplace();
            }
        });

        initWidget(binder.createAndBindUi(this));

        handler.setupMetaBlock(this);
    }

    public void removeAndReplace() {
        handler.replace(this, metaBlockFactory.createDrawer(handler));
    }

    public void setupQuestionBlocks() {
        block.removeStyleName(resources.builderStyleCss().contentBlocksOnly());

        GQuery.$(questionBlocks).empty()
                .append(createDiv(labels.text(), BlockType.TEXT_INPUT.code()))
                .append(createDiv(labels.multipleChoice(), BlockType.RADIO.code()))
                .append(createDiv(labels.rating(),
                        createCompositeBlockType(BlockType.RATING, BlockType.STARS)))
                .append(createDiv(labels.date(), BlockType.DATE.code()))
                .append(createDiv(labels.attachments(), BlockType.ATTACHMENTS.code()));
    }

    public void setupQuestionBank(Multimap<BlockType, BlockBuilder> existingBlockTypes) {
        block.removeStyleName(resources.builderStyleCss().contentBlocksOnly());

        GQuery leftBank = GQuery.$(questionBankLeft).empty();

        addToQuestionBank(existingBlockTypes, leftBank,
                createCompositeBlockType(BlockType.STORY_TITLE, BlockType.TEXT_INPUT),
                labels.standQuestStoryTitle());
        addToQuestionBank(existingBlockTypes, leftBank,
                createCompositeBlockType(BlockType.STORY_ASK, BlockType.TEXT_AREA),
                labels.standQuestStoryAsk());
        addToQuestionBank(existingBlockTypes, leftBank,
                createCompositeBlockType(BlockType.FIRST_NAME, BlockType.TEXT_INPUT),
                labels.standQuestFirstName());
        addToQuestionBank(existingBlockTypes, leftBank,
                createCompositeBlockType(BlockType.LAST_NAME, BlockType.TEXT_INPUT),
                labels.standQuestLastName());

        leftBank.append(createDiv(labels.standQuestEmail(),
                createCompositeBlockType(BlockType.EMAIL, BlockType.CONTACT)));

        leftBank.append(createDiv(labels.standQuestPhone(),
                createCompositeBlockType(BlockType.PHONE, BlockType.CONTACT)));

        String[] stdValues = getStandardValues();
        String[] stdLabels = getStandardLabels();

        GQuery rightBank = GQuery.$(questionBankRight).empty();
        for (int i = 0; i < stdValues.length; i++) {
            String code = stdValues[i];
            rightBank.append(createDiv(stdLabels[i], code,
                    !isBlockTypeAlreadyAdded(existingBlockTypes, BlockType.valueOfCode(code.split("/")[0]))));
        }
    }

    @Override
    protected void onAttach() {
        super.onAttach();

        $("[data-value]:not(." + resources.builderStyleCss().disabled() + ")")
                .on(BrowserEvents.CLICK, new Function() {
                    @Override
                    public void f(Element e) {
                        onElementClicked(e);
                    }
                });
    }

    private String createCompositeBlockType(BlockType blockType, BlockType formType) {
        return blockType.code() + "/" + formType.code();
    }

    private boolean isBlockTypeAlreadyAdded(
            Multimap<BlockType, BlockBuilder> existingBlockTypes,
            BlockType blockType) {
        return existingBlockTypes.containsKey(blockType);
    }

    private void onElementClicked(Element e) {
        String value = e.getAttribute("data-value");
        String[] values = value.split("/");

        BlockType blockType = BlockType.valueOfCode(values[0]);
        BlockType formType = values.length > 1 ? BlockType.valueOfCode(values[1]) : null;

        handler.createBlockAndReplace(blockType, formType, this);
    }

    private void addToQuestionBank(Multimap<BlockType, BlockBuilder> existingBlockTypes, GQuery element, String code,
            String label) {
        BlockType blockType = BlockType.valueOfCode(code.split("/")[0]);

        DivElement divElement = createDiv(label, code, !existingBlockTypes.containsKey(blockType));

        element.append(divElement);
    }

    private DivElement createDiv(String text, String code, boolean enabled) {
        DivElement divElement = Document.get().createDivElement();

        if (!enabled) {
            divElement.setClassName(resources.builderStyleCss().disabled());
        }

        divElement.setInnerText(text);
        divElement.setAttribute("data-value", code);

        return divElement;
    }

    private DivElement createDiv(String text, String code) {
        return createDiv(text, code, true);
    }

    private String[] getStandardLabels() {
        return new String[]{
                labels.standQuestStreet(),
                labels.standQuestCity(),
                labels.standQuestState(),
                labels.standQuestZip(),
                labels.subscription(),
                labels.standQuestEmailFormat()
        };
    }

    private String[] getStandardValues() {
        return new String[]{
                createCompositeBlockType(BlockType.STREET_ADDRESS_1, BlockType.PLAIN_TEXT),
                createCompositeBlockType(BlockType.CITY, BlockType.TEXT_INPUT),
                createCompositeBlockType(BlockType.STATE, BlockType.SELECT),
                createCompositeBlockType(BlockType.ZIP_CODE, BlockType.TEXT_INPUT),
                createCompositeBlockType(BlockType.UPDATES_OPT_IN, BlockType.CHECKBOX),
                createCompositeBlockType(BlockType.PREFERRED_EMAIL_FORMAT, BlockType.SELECT)
        };
    }
}
