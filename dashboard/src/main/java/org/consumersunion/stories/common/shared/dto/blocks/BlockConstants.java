package org.consumersunion.stories.common.shared.dto.blocks;

import com.fasterxml.jackson.annotation.JsonValue;

public class BlockConstants {
    public enum ApiBlockType {
        // TODO: for now we follow the API 'block_type' values; I woud like to settle on xxx_input_block and
        // xxx_content_block. See TASK-1720
        ATTACHMENTS_QUESTION_BLOCK("AttachmentsQuestionBlock"),
        AUDIO_CONTENT_BLOCK("AudioContentBlock"),
        BUTTON_BLOCK("ButtonBlock"),
        TEXT_BOX_QUESTION_BLOCK("TextBoxQuestionBlock"),
        CITY_QUESTION_BLOCK("CityQuestionBlock"),
        FIRST_NAME_QUESTION_BLOCK("FirstNameQuestionBlock"),
        LAST_NAME_QUESTION_BLOCK("LastNameQuestionBlock"),
        COLLECTION_CONTENT_BLOCK("CollectionContentBlock"),
        DATE_QUESTION_BLOCK("DateQuestionBlock"),
        STORY_ASK_QUESTION_BLOCK("StoryAskQuestionBlock"),
        STORY_TITLE_QUESTION_BLOCK("StoryTitleQuestionBlock"),
        STREET_ADDRESS_QUESTION_BLOCK("StreetAddressQuestionBlock"),
        DOCUMENT_CONTENT_BLOCK("DocumentContentBlock"),
        EMAIL_FORMAT_QUESTION_BLOCK("EmailFormatQuestionBlock"),
        MULTIPLE_CHOICE_QUESTION_BLOCK("MultipleChoiceQuestionBlock"),
        STATE_QUESTION_BLOCK("StateQuestionBlock"),
        EMAIL_QUESTION_BLOCK("EmailQuestionBlock"),
        PHONE_QUESTION_BLOCK("PhoneQuestionBlock"),
        IMAGE_CONTENT_BLOCK("ImageContentBlock"),
        TEXT_CONTENT_BLOCK("TextContentBlock"),
        PERMISSIONS_BLOCK("PermissionBlock"),
        RATING_QUESTION_BLOCK("RatingQuestionBlock"),
        STORY_CONTENT_BLOCK("StoryContentBlock"),
        SUBSCRIPTION_QUESTION_BLOCK("SubscriptionQuestionBlock"),
        VIDEO_CONTENT_BLOCK("VideoContentBlock"),
        ZIP_CODE_QUESTION_BLOCK("ZipCodeQuestionBlock");

        private String name;

        private ApiBlockType(String name) {
            this.name = name;
        }

        @JsonValue
        @Override
        public String toString() {
            return name;
        }
    }

    public enum ApiBlockFormat {
        SINGLE_LINE("single_line"),
        MULTI_LINES_PLAIN_TEXT("multiple_lines_plain_text"),
        MULTI_LINES_RICH_TEXT("multiple_lines_rich_text"),
        SINGLE_SELECT_RADIO("single_select_radio"),
        SINGLE_SELECT_DROPDOWN("single_select_dropdown"),
        MULTI_SELECT_LIST("multi_select_list"),
        MULTI_SELECT_CHECKBOX("multi_select_checkbox"),
        NUMBERS("numbers"),
        STARS("stars");

        private String name;

        private ApiBlockFormat(String name) {
            this.name = name;
        }

        @JsonValue
        @Override
        public String toString() {
            return name;
        }
    }

    public enum ApiBlockStylePosition {
        LEFT("left"),
        RIGHT("right"),
        CENTER("center");

        private String name;

        private ApiBlockStylePosition(String name) {
            this.name = name;
        }

        @JsonValue
        @Override
        public String toString() {
            return name;
        }
    }

    public enum ApiBlockStyleSize {
        SMALL("small"),
        MEDIUM("medium"),
        LARGE("large");

        private String name;

        private ApiBlockStyleSize(String name) {
            this.name = name;
        }

        @JsonValue
        @Override
        public String toString() {
            return name;
        }
    }

    public enum ApiBlockInteraction {
        DISCRETE("discrete"),
        HALF("half");

        private String name;

        private ApiBlockInteraction(String name) {
            this.name = name;
        }

        @JsonValue
        @Override
        public String toString() {
            return name;
        }
    }
}
