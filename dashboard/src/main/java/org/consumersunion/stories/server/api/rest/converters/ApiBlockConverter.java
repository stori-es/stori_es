package org.consumersunion.stories.server.api.rest.converters;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.dto.ApiBlock;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockAudio;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockCollection;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockConstraints;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockDocument;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockImage;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockLabel;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockNextDocument;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockOption;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockOptions;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockStory;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockStyles;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockVideo;
import org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockFormat;
import org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockStylePosition;
import org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockStyleSize;
import org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockType;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.model.document.Content.TextType;
import org.consumersunion.stories.common.shared.model.document.DocumentBlock;
import org.consumersunion.stories.common.shared.model.document.ImageBlock;
import org.consumersunion.stories.common.shared.model.document.MediaBlock;
import org.consumersunion.stories.common.shared.model.document.SubmitBlock;
import org.consumersunion.stories.common.shared.model.document.SubmitBlock.Position;
import org.consumersunion.stories.common.shared.model.document.SubmitBlock.Size;
import org.consumersunion.stories.common.shared.model.document.TextImage;
import org.consumersunion.stories.common.shared.model.document.TextImageBlock;
import org.consumersunion.stories.common.shared.model.questionnaire.ContactBlock;
import org.consumersunion.stories.common.shared.model.questionnaire.Option;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;
import org.consumersunion.stories.common.shared.model.questionnaire.RatingQuestion;
import org.consumersunion.stories.common.shared.model.questionnaire.RatingQuestion.StepType;
import org.consumersunion.stories.common.shared.model.type.ContactType;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.api.rest.util.ResourceLinksHelper;
import org.consumersunion.stories.server.api.rest.util.ResourceLinksHelperImpl;
import org.consumersunion.stories.server.exception.BadRequestException;
import org.springframework.stereotype.Component;

import com.google.common.base.Converter;
import com.google.common.base.Objects;

import static org.consumersunion.stories.common.shared.api.EndPoints.COLLECTIONS;
import static org.consumersunion.stories.common.shared.api.EndPoints.DOCUMENTS;
import static org.consumersunion.stories.common.shared.api.EndPoints.STORIES;
import static org.consumersunion.stories.common.shared.api.EndPoints.endsWithId;
import static org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockFormat.MULTI_LINES_PLAIN_TEXT;
import static org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockFormat.MULTI_LINES_RICH_TEXT;
import static org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockFormat.NUMBERS;
import static org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockFormat.STARS;
import static org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockInteraction.DISCRETE;
import static org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockInteraction.HALF;
import static org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockType.EMAIL_QUESTION_BLOCK;
import static org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockType.PHONE_QUESTION_BLOCK;

@Component
public class ApiBlockConverter extends Converter<ApiBlock, Block> {
    ResourceLinksHelper resourceLinksHelper;

    @Inject
    ApiBlockConverter(ResourceLinksHelper resourceLinksHelper) {
        this.resourceLinksHelper = resourceLinksHelper;
    }

    @Override
    protected ApiBlock doBackward(Block block) {
        ApiBlock apiBlock = new ApiBlock();

        if (block instanceof Question) {
            Question question = (Question) block;
            apiBlock.setValue(((Question) block).getText());

            ApiBlockLabel labels = new ApiBlockLabel();
            labels.setValue(question.getText());
            labels.setHelp(question.getHelpText());
            apiBlock.setLabels(labels);

            ApiBlockConstraints constraints = new ApiBlockConstraints();
            constraints.setRequired(question.isRequired());
            constraints.setMinimumLength(question.getMinLength());
            constraints.setMaximumLength(question.getMaxLength());
            apiBlock.setConstraints(constraints);
        }

        if (block.getFormType() == BlockType.ATTACHMENTS) {
            apiBlock.setBlockType(ApiBlockType.ATTACHMENTS_QUESTION_BLOCK);
        } else if (block.getFormType() == BlockType.AUDIO) {
            // TODO: merge the audio and media; it's really just media. consider merging image as well. See TASK-1721
            apiBlock.setBlockType(ApiBlockType.AUDIO_CONTENT_BLOCK);
            apiBlock.setAudio(new ApiBlockAudio());
            apiBlock.getAudio().setTitle(((MediaBlock) block).getTitle());
            apiBlock.getAudio().setHref(((MediaBlock) block).getUrl());
            // TODO: get the field names to match TASK-1722
            apiBlock.getAudio().setCaption(((MediaBlock) block).getDescription());
        } else if (block.getFormType() == BlockType.VIDEO) {
            apiBlock.setBlockType(ApiBlockType.VIDEO_CONTENT_BLOCK);
            apiBlock.setVideo(new ApiBlockVideo());
            apiBlock.getVideo().setTitle(((MediaBlock) block).getTitle());
            apiBlock.getVideo().setHref(((MediaBlock) block).getUrl());
            // TODO: get field names to match TASK-1722
            apiBlock.getVideo().setCaption(((MediaBlock) block).getDescription());
        } else if (block.getFormType() == BlockType.SUBMIT) {
            apiBlock.setBlockType(ApiBlockType.BUTTON_BLOCK);
            String nextUrl =
                    resourceLinksHelper.replaceId(endsWithId(DOCUMENTS),
                            ((SubmitBlock) block).getNextDocument().getDocumentId())
                            .getHref();
            ApiBlockNextDocument nextDocument = new ApiBlockNextDocument();
            nextDocument.setHref(nextUrl);
            apiBlock.setNextDocument(nextDocument);

            forwardStyleData(apiBlock, (SubmitBlock) block);
        } else if (block.getFormType() == BlockType.TEXT_INPUT) {
            // TODO: If we confirm the 'type' constants across the model POJOs and API DTOs, we can just use sets and
            // avoid all this. TASK-1723
            if (block.getStandardMeaning() == BlockType.CITY) {
                forwardTextInputAttributes(apiBlock, (Question) block, ApiBlockFormat.SINGLE_LINE,
                        ApiBlockType.CITY_QUESTION_BLOCK);
            } else if (block.getStandardMeaning() == BlockType.FIRST_NAME) {
                forwardTextInputAttributes(apiBlock, (Question) block, ApiBlockFormat.SINGLE_LINE,
                        ApiBlockType.FIRST_NAME_QUESTION_BLOCK);
            } else if (block.getStandardMeaning() == BlockType.LAST_NAME) {
                forwardTextInputAttributes(apiBlock, (Question) block, ApiBlockFormat.SINGLE_LINE,
                        ApiBlockType.LAST_NAME_QUESTION_BLOCK);
            } else if (block.getStandardMeaning() == BlockType.STORY_TITLE) {
                forwardTextInputAttributes(apiBlock, (Question) block, ApiBlockFormat.SINGLE_LINE,
                        ApiBlockType.STORY_TITLE_QUESTION_BLOCK);
            } else if (block.getStandardMeaning() == BlockType.STREET_ADDRESS_1) {
                forwardTextInputAttributes(apiBlock, (Question) block, ApiBlockFormat.SINGLE_LINE,
                        ApiBlockType.STREET_ADDRESS_QUESTION_BLOCK);
            } else if (block.getStandardMeaning() == BlockType.ZIP_CODE) {
                forwardTextInputAttributes(apiBlock, (Question) block, ApiBlockFormat.SINGLE_LINE,
                        ApiBlockType.ZIP_CODE_QUESTION_BLOCK);
            } else if (block.getStandardMeaning() == null) { // generic text input
                forwardTextInputAttributes(apiBlock, (Question) block, ApiBlockFormat.SINGLE_LINE);
            } else {
                throw new GeneralException(
                        "Unknown standard meaning for 'text input' block: " + block.getStandardMeaning());
            }
        } else if (block.getFormType() == BlockType.TEXT_AREA) {
            if (block.getStandardMeaning() == BlockType.STORY_ASK) {
                forwardTextInputAttributes(apiBlock, (Question) block, MULTI_LINES_PLAIN_TEXT,
                        ApiBlockType.STORY_ASK_QUESTION_BLOCK);
            } else {
                forwardTextInputAttributes(apiBlock, (Question) block, MULTI_LINES_PLAIN_TEXT);
            }
        } else if (block.getFormType() == BlockType.RICH_TEXT_AREA) {
            if (block.getStandardMeaning() == BlockType.STORY_ASK) {
                forwardTextInputAttributes(apiBlock, (Question) block, MULTI_LINES_RICH_TEXT,
                        ApiBlockType.STORY_ASK_QUESTION_BLOCK);
            } else {
                forwardTextInputAttributes(apiBlock, (Question) block, MULTI_LINES_RICH_TEXT);
            }
        } else if (block.getFormType() == BlockType.COLLECTION || block.getFormType() == BlockType.STORY) {
            // TODO: These are really 'reference blocks. We're shoe-horning the 'Collection Block' on top of the
            // 'Content Block' and it's causing data type follishness up and down the stack; need to
            // fix this, and get a DB table with an FK. See TASK-1724
            // TODO: also, these should use the same 'ApiBlockReference' data structure. See TASK-1735
            if (block.getFormType() == BlockType.STORY) {
                apiBlock.setBlockType(ApiBlockType.STORY_CONTENT_BLOCK);
                String href =
                        resourceLinksHelper.replaceId(endsWithId(STORIES),
                                Integer.parseInt(((Content) block).getContent())).getHref();
                apiBlock.setStory(new ApiBlockStory());
                apiBlock.getStory().setHref(href);
            } else {
                apiBlock.setBlockType(ApiBlockType.COLLECTION_CONTENT_BLOCK);
                String href =
                        resourceLinksHelper.replaceId(endsWithId(COLLECTIONS),
                                Integer.parseInt(((Content) block).getContent())).getHref();
                apiBlock.setCollection(new ApiBlockCollection());
                apiBlock.getCollection().setHref(href);
            }
        } else if (block.getFormType() == BlockType.DATE) {
            apiBlock.setBlockType(ApiBlockType.DATE_QUESTION_BLOCK);
        } else if (block.getFormType() == BlockType.DOCUMENT) {
            apiBlock.setBlockType(ApiBlockType.DOCUMENT_CONTENT_BLOCK);
            ApiBlockDocument apiBlockDocument = new ApiBlockDocument();
            apiBlockDocument.setTitle(((DocumentBlock) block).getTitle());
            // TODO: settle on 'href'. TASK-1725
            apiBlockDocument.setHref(((DocumentBlock) block).getUrl());
            apiBlock.setDocument(apiBlockDocument);
        } else if (block.getFormType() == BlockType.SELECT || block.getFormType() == BlockType.CHECKBOX
                || block.getFormType() == BlockType.RADIO) {
            if (block.getStandardMeaning() == BlockType.PREFERRED_EMAIL_FORMAT) {
                apiBlock.setBlockType(ApiBlockType.EMAIL_FORMAT_QUESTION_BLOCK);
            } else if (block.getStandardMeaning() == BlockType.STATE) {
                apiBlock.setBlockType(ApiBlockType.STATE_QUESTION_BLOCK);
            } else if (block.getStandardMeaning() == BlockType.UPDATES_OPT_IN) {
                apiBlock.setBlockType(ApiBlockType.SUBSCRIPTION_QUESTION_BLOCK);
            } else if (block.getStandardMeaning() == null) {
                apiBlock.setBlockType(ApiBlockType.MULTIPLE_CHOICE_QUESTION_BLOCK);
            } else {
                throw new GeneralException(
                        "Unknown standard meaning for 'select' block: " + block.getStandardMeaning());
            }
            forwardPopulateSelectData(apiBlock, (Question) block);
        } else if (block.getFormType() == BlockType.CONTACT) {
            ContactBlock contactBlock = (ContactBlock) block;
            // TODO: We'd like to set the API type off the ContactBlock#dataType, but it's not consistent and we may
            // end up reworking it all a bit. See TASK-1736, TASK-1737
            if (BlockType.emailElements().contains(block.getStandardMeaning())) {
                apiBlock.setBlockType(EMAIL_QUESTION_BLOCK);
            } else if (BlockType.phoneElements().contains(block.getStandardMeaning())) {
                apiBlock.setBlockType(PHONE_QUESTION_BLOCK);
            }

            ContactType contactType = ContactType.fromBlockType(block.getStandardMeaning());
            apiBlock.getLabels().setValue(contactType.code());
        } else if (block.getFormType() == BlockType.TEXT_IMAGE) {
            apiBlock.setBlockType(ApiBlockType.TEXT_CONTENT_BLOCK);
            TextImageBlock imageBlock = (TextImageBlock) block;
            apiBlock.setValue(imageBlock.getText());
            apiBlock.setFormat(MULTI_LINES_RICH_TEXT);
            setTextImageBlockData(apiBlock, imageBlock.getImage());
        } else if (block.getFormType() == BlockType.IMAGE) {
            apiBlock.setBlockType(ApiBlockType.IMAGE_CONTENT_BLOCK);
            ApiBlockImage apiImage = new ApiBlockImage();
            apiImage.setAltText(((ImageBlock) block).getAltText());
            apiImage.setCaption(((ImageBlock) block).getCaption());
            apiImage.setHref(((ImageBlock) block).getUrl());
            // TODO: the data model image has neither size nor position. TASK-1726
            apiBlock.setImage(apiImage);
        } else if (block.getFormType() == BlockType.CONTENT) {
            if (block.getStandardMeaning() == BlockType.CUSTOM_PERMISSIONS) {
                apiBlock.setBlockType(ApiBlockType.PERMISSIONS_BLOCK);
            } else if (block.getStandardMeaning() == null) {
                apiBlock.setBlockType(ApiBlockType.TEXT_CONTENT_BLOCK);
            } else {
                throw new GeneralException("Unexpected standard meaning '" + block.getStandardMeaning()
                        + "' for content block");
            }

            apiBlock.setValue(((Content) block).getContent());

            TextType textType = ((Content) block).getTextType();
            if (TextType.HTML.equals(textType)) {
                apiBlock.setFormat(MULTI_LINES_RICH_TEXT);
            } else {
                apiBlock.setFormat(MULTI_LINES_PLAIN_TEXT);
            }
        } else if (block.getStandardMeaning() == BlockType.RATING) {
            RatingQuestion ratingQuestion = (RatingQuestion) block;
            apiBlock.setBlockType(ApiBlockType.RATING_QUESTION_BLOCK);
            apiBlock.setFormat(BlockType.STARS.equals(ratingQuestion.getFormType()) ? STARS : NUMBERS);
            apiBlock.getLabels().setLeft(ratingQuestion.getStartLabel());
            apiBlock.getLabels().setRight(ratingQuestion.getEndLabel());
            apiBlock.setInteraction(StepType.DISCRETE.equals(ratingQuestion.getStepType()) ? DISCRETE : HALF);
        } else {
            throw new GeneralException("Unknown block type value: '" + block.getFormType() + "'.");
        }

        return apiBlock;
    }

    @Override
    protected Block doForward(ApiBlock apiBlock) {
        Block block;
        switch (apiBlock.getBlockType()) {
            // begin: text inputs
            case CITY_QUESTION_BLOCK:
                block = backwardTextInputAttributes(apiBlock, BlockType.CITY);
                break;
            case FIRST_NAME_QUESTION_BLOCK:
                block = backwardTextInputAttributes(apiBlock, BlockType.FIRST_NAME);
                break;
            case LAST_NAME_QUESTION_BLOCK:
                block = backwardTextInputAttributes(apiBlock, BlockType.LAST_NAME);
                break;
            case ZIP_CODE_QUESTION_BLOCK:
                block = backwardTextInputAttributes(apiBlock, BlockType.ZIP_CODE);
                break;
            case STORY_TITLE_QUESTION_BLOCK:
                block = backwardTextInputAttributes(apiBlock, BlockType.STORY_TITLE);
                break;
            case STREET_ADDRESS_QUESTION_BLOCK:
                block = backwardTextInputAttributes(apiBlock, BlockType.STREET_ADDRESS_1);
                break;
            case TEXT_BOX_QUESTION_BLOCK:
                switch (apiBlock.getFormat()) {
                    case MULTI_LINES_PLAIN_TEXT:
                        block = backwardTextInputAttributes(apiBlock, BlockType.PLAIN_TEXT);
                        break;
                    case MULTI_LINES_RICH_TEXT:
                        block = backwardTextInputAttributes(apiBlock, BlockType.RICH_TEXT_AREA);
                        break;
                    case SINGLE_LINE:
                        block = backwardTextInputAttributes(apiBlock, BlockType.TEXT_INPUT);
                        break;
                    default:
                        throw new GeneralException("Uknown block format '" + apiBlock.getFormat());
                }
                break;
            // end: text inputs
            // begin: story reference blocks
            case COLLECTION_CONTENT_BLOCK:
                block = new Content();
                block.setFormType(BlockType.COLLECTION);
                ((Content) block).setContent(
                        "" + ResourceLinksHelperImpl.extractId(apiBlock.getCollection().getHref()));
                break;
            case STORY_CONTENT_BLOCK:
                block = new Content();
                block.setFormType(BlockType.STORY);
                ((Content) block).setContent("" + ResourceLinksHelperImpl.extractId(apiBlock.getStory().getHref()));
                break;
            // end: story reference blocks
            // begin: select questions
            case EMAIL_FORMAT_QUESTION_BLOCK:
                block = backwardMultiChoice(apiBlock, BlockType.CHECKBOX, BlockType.PREFERRED_EMAIL_FORMAT, false);
                break;
            case STATE_QUESTION_BLOCK:
                block = backwardMultiChoice(apiBlock, BlockType.SELECT, BlockType.STATE, false);
                break;
            case SUBSCRIPTION_QUESTION_BLOCK:
                block = backwardMultiChoice(apiBlock, BlockType.CHECKBOX, BlockType.UPDATES_OPT_IN, false);
                break;
            case MULTIPLE_CHOICE_QUESTION_BLOCK:
                BlockType formType;
                boolean isMultiselect = false;
                switch (apiBlock.getFormat()) {
                    case SINGLE_SELECT_RADIO:
                        formType = BlockType.RADIO;
                        break;
                    case SINGLE_SELECT_DROPDOWN:
                        formType = BlockType.SELECT;
                        break;
                    case MULTI_SELECT_LIST:
                        formType = BlockType.SELECT;
                        isMultiselect = true;
                        break;
                    case MULTI_SELECT_CHECKBOX:
                        formType = BlockType.CHECKBOX;
                        break;
                    default:
                        formType = BlockType.SELECT;
                        break;
                }
                block = backwardMultiChoice(apiBlock, formType, null, isMultiselect);
                break;
            // end: select questions
            // begin: contact blocks
            case EMAIL_QUESTION_BLOCK:
            case PHONE_QUESTION_BLOCK:
                block = backwardContactBlock(apiBlock);
                break;
            // end: contact blocks
            // begin: media blocks
            case AUDIO_CONTENT_BLOCK:
                block = new MediaBlock();
                block.setFormType(BlockType.AUDIO);
                ((MediaBlock) block).setTitle(apiBlock.getAudio().getTitle());
                ((MediaBlock) block).setUrl(apiBlock.getAudio().getHref());
                ((MediaBlock) block).setDescription(apiBlock.getAudio().getCaption());
                break;
            case VIDEO_CONTENT_BLOCK:
                block = new MediaBlock();
                block.setFormType(BlockType.AUDIO);
                ((MediaBlock) block).setTitle(apiBlock.getVideo().getTitle());
                ((MediaBlock) block).setUrl(apiBlock.getVideo().getHref());
                ((MediaBlock) block).setDescription(apiBlock.getVideo().getCaption());
                break;
            // end: media blocks
            // begin: content blocks
            case PERMISSIONS_BLOCK:
                block = backwardContentBlock(apiBlock, BlockType.CUSTOM_PERMISSIONS);
                break;
            case TEXT_CONTENT_BLOCK:
                block = backwardContentBlock(apiBlock, null);
                break;
            // end: content blocks
            // others
            case ATTACHMENTS_QUESTION_BLOCK:
                block = new Question();
                block.setFormType(BlockType.ATTACHMENTS);
                break;
            case BUTTON_BLOCK:
                if (apiBlock.getNextDocument() == null) {
                    throw new BadRequestException("'ButtonBlock' types must define 'nextDocument' property.");
                }
                block = new SubmitBlock();
                block.setFormType(BlockType.SUBMIT);
                SubmitBlock.NextDocument nextDocument = new SubmitBlock.NextDocument();
                nextDocument.setDocumentId(ResourceLinksHelperImpl.extractId(apiBlock.getNextDocument().getHref()));
                backwardStyleData((SubmitBlock) block, apiBlock);
                break;
            case DATE_QUESTION_BLOCK:
                block = new Question();
                block.setFormType(BlockType.DATE);
                break;
            case DOCUMENT_CONTENT_BLOCK:
                if (apiBlock.getDocument() == null) {
                    throw new BadRequestException("'DoumentContentBlock' types must define 'document' property.");
                }
                block = new DocumentBlock();
                block.setFormType(BlockType.DOCUMENT);
                ((DocumentBlock) block).setTitle(apiBlock.getDocument().getTitle());
                ((DocumentBlock) block).setUrl(apiBlock.getDocument().getHref());
                break;
            case IMAGE_CONTENT_BLOCK:
                block = new ImageBlock();
                block.setFormType(BlockType.IMAGE);
                ((ImageBlock) block).setAltText(apiBlock.getImage().getAltText());
                ((ImageBlock) block).setCaption(apiBlock.getImage().getCaption());
                ((ImageBlock) block).setUrl(apiBlock.getImage().getHref());
                break;
            case RATING_QUESTION_BLOCK:
                block = new RatingQuestion();
                // TODO: see TASK-1764
                block.setFormType(BlockType.RATING);
                block.setStandardMeaning(NUMBERS.equals(apiBlock.getFormat()) ? BlockType.NUMBERS : BlockType.STARS);
                break;
            case STORY_ASK_QUESTION_BLOCK:
                block = new Question();
                block.setFormType(apiBlock.getFormat() == MULTI_LINES_PLAIN_TEXT ?
                        BlockType.PLAIN_TEXT : BlockType.RICH_TEXT_AREA);
                block.setStandardMeaning(BlockType.STORY_ASK);
                break;
            default:
                throw new GeneralException("Unknown block type '" + apiBlock.getBlockType() + "'.");
        }

        if (block instanceof Question) {
            Question question = (Question) block;

            ApiBlockLabel labels = apiBlock.getLabels();
            if (labels != null) {
                question.setText(labels.getPrimary());
                question.setHelpText(labels.getHelp());
            }

            ApiBlockConstraints constraints = apiBlock.getConstraints();
            if (constraints != null) {
                question.setRequired(Objects.firstNonNull(constraints.isRequired(), false));
                question.setMinLength(constraints.getMinimumLength());
                question.setMaxLength(constraints.getMaximumLength());
            }
        }

        return block;
    }

    // forward helpers
    private void setTextImageBlockData(ApiBlock blockResponse, TextImage textImage) {
        if (textImage == null) {
            return;
        }

        ApiBlockImage imageData = new ApiBlockImage();
        blockResponse.setImage(imageData);

        // Since 'TextImage.Size' define's it's own DEFAULT, we'll use it.
        TextImage.Size testSize;
        if (textImage.getSize() == null) {
            testSize = TextImage.Size.DEFAULT;
        } else {
            testSize = textImage.getSize();
        }

        if (testSize == TextImage.Size.SMALL) {
            imageData.setSize(ApiBlockStyleSize.SMALL);
        } else if (testSize == TextImage.Size.MEDIUM) {
            imageData.setSize(ApiBlockStyleSize.MEDIUM);
        } else if (testSize == TextImage.Size.LARGE) {
            imageData.setSize(ApiBlockStyleSize.LARGE);
        }

        // Same deal with position.
        TextImage.Position testPosition;
        if (textImage.getPosition() == null) {
            testPosition = TextImage.Position.DEFAULT;
        } else {
            testPosition = textImage.getPosition();
        }

        if (testPosition == TextImage.Position.LEFT) {
            imageData.setHorizontalPosition(ApiBlockStylePosition.LEFT);
        } else if (testPosition == TextImage.Position.RIGHT) {
            imageData.setHorizontalPosition(ApiBlockStylePosition.RIGHT);
        }

        imageData.setAltText(textImage.getAltText());
        imageData.setCaption(textImage.getCaption());
        imageData.setHref(textImage.getUrl());
    }

    private void forwardStyleData(ApiBlock blockResponse, SubmitBlock block) {
        ApiBlockStyles styles = new ApiBlockStyles();
        // TODO: Having two, necessarilly parallel enumaration constants is redonk. See TASK-1729
        // TODO: actually, it's even worse; there are multiple size definitions on the data model size, both submit 
        // and text image define 'SMALL', 'MEDIUM', and 'LARGE' in two different enumerations, plus the API def makes
        // three
        if (block.getSize() == SubmitBlock.Size.LARGE) {
            styles.setSize(ApiBlockStyleSize.LARGE);
        } else if (block.getSize() == SubmitBlock.Size.SMALL) {
            styles.setSize(ApiBlockStyleSize.SMALL);
        }
        // TODO: check whether to default or complain, not sure what the data model expects. See TASK-1730
        else { // default to SMALL following dashboard
            styles.setSize(ApiBlockStyleSize.SMALL);
        }

        if (block.getPosition() == SubmitBlock.Position.LEFT) {
            styles.setHorizontalPosition(ApiBlockStylePosition.LEFT);
        } else if (block.getPosition() == SubmitBlock.Position.RIGHT) {
            styles.setHorizontalPosition(ApiBlockStylePosition.RIGHT);
        }
        // TODO: check whether to default or complain, not sure what the data model expects; See TASK-1730
        else { // default to CENTER
            styles.setHorizontalPosition(ApiBlockStylePosition.CENTER);
        }

        blockResponse.setStyles(styles);
    }

    private void forwardTextInputAttributes(ApiBlock blockResponse, Question question, ApiBlockFormat blockFormat) {
        forwardTextInputAttributes(blockResponse, question, blockFormat, ApiBlockType.TEXT_BOX_QUESTION_BLOCK);
    }

    private void forwardTextInputAttributes(ApiBlock blockResponse, Question question, ApiBlockFormat blockFormat,
            ApiBlockType apiBlockType) {
        blockResponse.setBlockType(apiBlockType);
        blockResponse.setFormat(blockFormat);
    }

    // TODO: Example of really verbose settings. See TASK-1731
    private void forwardPopulateSelectData(ApiBlock blockResponse, Question select) {
        ApiBlockOptions apiBlockOptions = new ApiBlockOptions();
        blockResponse.setOptions(apiBlockOptions);
        List<ApiBlockOption> options = new ArrayList<ApiBlockOption>();
        apiBlockOptions.setOptions(options);
        for (Option option : select.getOptions()) {
            ApiBlockOption apiOption = new ApiBlockOption();
            apiOption.setId(option.getReportValue());
            apiOption.setValue(option.getDisplayValue());
            // TODO: I don't believe 'selected' is supported in data model or UI. See PRODUCT-1940
            apiOption.setSelected(false);
            options.add(apiOption);
        }

        BlockType formType = select.getFormType();
        ApiBlockFormat format = null;
        if (BlockType.RADIO.equals(formType)) {
            format = ApiBlockFormat.SINGLE_SELECT_RADIO;
        } else if (BlockType.CHECKBOX.equals(formType)) {
            format = ApiBlockFormat.MULTI_SELECT_CHECKBOX;
        } else if (BlockType.SELECT.equals(formType)) {
            if (select.isMultiselect()) {
                format = ApiBlockFormat.MULTI_SELECT_LIST;
            } else {
                format = ApiBlockFormat.SINGLE_SELECT_DROPDOWN;
            }
        }
        blockResponse.setFormat(format);
    }

    // backward helpers
    private Question backwardTextInputAttributes(ApiBlock blockResponse, BlockType blockType) {
        Question block = new Question();
        block.setFormType(BlockType.TEXT_INPUT);
        block.setStandardMeaning(blockType);
        block.setText(blockResponse.getValue());

        return block;
    }

    private Question backwardContactBlock(ApiBlock apiBlock) {
        ContactBlock block = new ContactBlock();
        block.setFormType(BlockType.CONTACT);

        ContactType contactType = ContactType.valueOfCode(apiBlock.getLabels().getValue());
        block.setType(contactType.name());

        BlockType standardMeaning = null;
        if (EMAIL_QUESTION_BLOCK.equals(apiBlock.getBlockType())) {
            switch (contactType) {
                case HOME:
                    standardMeaning = BlockType.EMAIL;
                    break;
                case OTHER:
                    standardMeaning = BlockType.EMAIL_OTHER;
                    break;
                case WORK:
                    standardMeaning = BlockType.EMAIL_WORK;
                    break;
            }
        } else if (PHONE_QUESTION_BLOCK.equals(apiBlock.getBlockType())) {
            switch (contactType) {
                case HOME:
                    standardMeaning = BlockType.PHONE;
                    break;
                case MOBILE:
                    standardMeaning = BlockType.PHONE_MOBILE;
                    break;
                case OTHER:
                    standardMeaning = BlockType.PHONE_OTHER;
                    break;
                case WORK:
                    standardMeaning = BlockType.PHONE_WORK;
                    break;
            }
        }

        block.setStandardMeaning(standardMeaning);
        block.setType(apiBlock.getLabels().getValue());

        return block;
    }

    private Content backwardContentBlock(ApiBlock apiBlock, BlockType standardMeaning) {
        Content block = new Content();
        block.setFormType(BlockType.CONTENT);
        block.setStandardMeaning(standardMeaning);
        block.setContent(apiBlock.getValue());

        TextType textType = null;
        if (MULTI_LINES_RICH_TEXT.equals(apiBlock.getFormat())) {
            textType = TextType.PLAIN;
        } else {
            textType = TextType.HTML;
        }
        block.setTextType(textType);

        return block;
    }

    private Question backwardMultiChoice(
            ApiBlock apiBlock,
            BlockType formType,
            BlockType standardMeaning,
            boolean isMultiselect) {

        Question block = new Question();
        block.setMultiselect(isMultiselect);
        block.setFormType(formType);
        block.setStandardMeaning(standardMeaning);
        List<Option> options = new ArrayList<Option>();
        for (ApiBlockOption apiOption : apiBlock.getOptions().getOptions()) {
            Option option = new Option();
            option.setDisplayValue(apiOption.getValue());
            option.setReportValue(apiOption.getId());
            options.add(option);
        }
        block.setOptions(options);

        return block;
    }

    private void backwardStyleData(SubmitBlock block, ApiBlock blockResponse) {
        switch (blockResponse.getStyles().getSize()) {
            case LARGE:
                block.setSize(Size.LARGE);
                break;
            case MEDIUM:
                block.setSize(Size.MEDIUM);
            case SMALL:
                block.setSize(Size.SMALL);
            default:
                throw new GeneralException(
                        "Unknown size style: '" + blockResponse.getStyles().getSize().toString() + "'.");
        }

        switch (blockResponse.getStyles().getHorizontalPosition()) {
            case LEFT:
                block.setPosition(Position.LEFT);
                break;
            case CENTER:
                block.setPosition(Position.CENTER);
                break;
            case RIGHT:
                block.setPosition(Position.RIGHT);
                break;
            default:
                throw new GeneralException(
                        "Unknown position style: '" + blockResponse.getStyles().getHorizontalPosition().toString() +
                                "'.");
        }
    }
}
