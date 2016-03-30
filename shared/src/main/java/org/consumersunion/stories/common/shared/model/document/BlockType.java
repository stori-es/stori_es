package org.consumersunion.stories.common.shared.model.document;

import java.util.Arrays;
import java.util.List;

public enum BlockType {
    // input controls
    TEXT_INPUT("TEXT_INPUT", "Question Block - Text Box"),
    TEXT_AREA("TEXT_AREA", "Paragraph Area"),
    RICH_TEXT_AREA("RICH_TEXT_AREA", "Rich Text"),
    RADIO("RADIO", "Question Block - Multiple Choice"),
    SELECT("SELECT", "Question Block - Multiple Choice"),
    ATTACHMENTS("ATTACHMENTS", "Question Block - Attachment"),
    CHECKBOX("CHECKBOX", "Question Block - Multiple Choice"),
    SUBHEADER("SUBHEADER", "Header"),
    PLAIN_TEXT("PLAIN_TEXT", "Text"),
    // content types
    TEXT_IMAGE("TEXT_IMAGE", "Content Block - Text"),
    CONTENT("CONTENT", "Content Block - Text"),
    DATE("DATE", "Question Block - Date"),
    IMAGE("IMAGE", "Content Block - Image"),
    AUDIO("AUDIO", "Content Block - Audio"),
    VIDEO("VIDEO", "Content Block - Video"),
    DOCUMENT("DOCUMENT", "Content Block - Document"),
    CONTACT("CONTACT", "Contact"),
    COLLECTION("COLLECTION", "Content Block - Collection"),
    STORY("STORY", "Content Block - Story"),
    SUBMIT("SUBMIT", "Submit Button Block"),
    // Standard blocks.
    FIRST_NAME("FIRST_NAME", "Question Block - First Name"),
    LAST_NAME("LAST_NAME", "Question Block - Last Name"),
    EMAIL("EMAIL", "Question Block - Email"),
    EMAIL_WORK("EMAIL_WORK", "Email Work"),
    EMAIL_OTHER("EMAIL_OTHER", "Email Other"),
    STREET_ADDRESS_1("STREET_ADDRESS_1", "Question Block - Street Address"),
    CITY("CITY", "Question Block - City"),
    STATE("STATE", "Question Block - State"),
    ZIP_CODE("ZIP_CODE", "Question Block - Zip code"),
    PHONE("PHONE", "Question Block - Phone"),
    PHONE_MOBILE("PHONE_MOBILE", "Phone Mobile"),
    PHONE_WORK("PHONE_WORK", "Phone Work"),
    PHONE_OTHER("PHONE_OTHER", "Phone Other"),
    MAILING_OPT_IN("MAILING_OPT_IN", "Question Block - Subscription"),
    PREFERRED_EMAIL_FORMAT("PREFERRED_EMAIL_FORMAT", "Question Block - Email Format"),
    STORY_ASK_RICH("STORY_ASK_RICH", "Question Block - Story Ask"),
    STORY_ASK_PLAIN("STORY_ASK_PLAIN", "Question Block - Story Ask"),
    CUSTOM_PERMISSIONS("CUSTOM_PERMISSIONS", "Permissions Block"),
    UPDATES_OPT_IN("UPDATES_OPT_IN", "Question Block - Subscription"),
    STORY_TITLE("STORY_TITLE", "Question Block - Story Title"),
    RATING_STARS("RATING_STARS", "Rating"),
    RATING_NUMBERS("RATING_NUMBERS", "Rating");

    private static final List<BlockType> customElements;
    private static final List<BlockType> standardElements;
    private static final List<BlockType> textTypes;
    private static final List<BlockType> emailElements = Arrays.asList(
            EMAIL,
            EMAIL_WORK,
            EMAIL_OTHER
    );
    private static List<BlockType> phoneElements = Arrays.asList(
            PHONE,
            PHONE_MOBILE,
            PHONE_WORK,
            PHONE_OTHER
    );

    static {
        BlockType[] customElementsType = new BlockType[]{
                TEXT_INPUT,
                TEXT_AREA,
                RICH_TEXT_AREA,
                RADIO,
                SELECT,
                CHECKBOX,
                SUBHEADER,
                PLAIN_TEXT,
                TEXT_IMAGE,
                CONTENT,
                DATE,
                IMAGE,
                AUDIO,
                VIDEO,
                DOCUMENT,
                CONTACT,
                ATTACHMENTS,
                COLLECTION,
                STORY,
                SUBMIT
        };
        customElements = Arrays.asList(customElementsType);

        BlockType[] elements = new BlockType[]{
                FIRST_NAME,
                LAST_NAME,
                EMAIL,
                STREET_ADDRESS_1,
                CITY,
                STATE,
                ZIP_CODE,
                PHONE,
                MAILING_OPT_IN,
                PREFERRED_EMAIL_FORMAT,
                STORY_ASK_RICH,
                STORY_ASK_PLAIN,
                CUSTOM_PERMISSIONS,
                UPDATES_OPT_IN,
                STORY_TITLE,
                SUBMIT
        };
        standardElements = Arrays.asList(elements);

        BlockType[] textElements = new BlockType[]{
                CONTENT,
                TEXT_IMAGE,
                RICH_TEXT_AREA,
                PLAIN_TEXT,
                TEXT_AREA,
                TEXT_INPUT
        };
        textTypes = Arrays.asList(textElements);
    }

    private final String code;
    private final String label;

    BlockType(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public static List<BlockType> customElements() {
        return customElements;
    }

    public static List<BlockType> emailElements() {
        return emailElements;
    }

    public static List<BlockType> phoneElements() {
        return phoneElements;
    }

    public static BlockType valueOfCode(String code) {
        for (BlockType blockType : values()) {
            if (blockType.code().equals(code)) {
                return blockType;
            }
        }

        return null;
    }

    public Boolean isRequired() {
        return !(this == BlockType.UPDATES_OPT_IN ||
                this == BlockType.PREFERRED_EMAIL_FORMAT ||
                this == BlockType.PHONE ||
                this == BlockType.RADIO ||
                this == BlockType.EMAIL);
    }

    public boolean isText() {
        return getRenderType() != null && textTypes.contains(getRenderType());
    }

    public String code() {
        return code;
    }

    public BlockType getRenderType() {
        // The standard types all map to a standard 'render' type which is equivalent to a custom input type.
        switch (this) {
            case FIRST_NAME:
            case LAST_NAME:
            case STORY_TITLE:
            case STREET_ADDRESS_1:
            case CITY:
            case ZIP_CODE:
                return TEXT_INPUT;
            case STORY_ASK_RICH:
                return RICH_TEXT_AREA;
            case STORY_ASK_PLAIN:
                return TEXT_AREA;
            case EMAIL:
            case EMAIL_WORK:
            case EMAIL_OTHER:
            case PHONE:
            case PHONE_MOBILE:
            case PHONE_WORK:
            case PHONE_OTHER:
                return CONTACT;
            case MAILING_OPT_IN:
            case UPDATES_OPT_IN:
                return CHECKBOX;
            case PREFERRED_EMAIL_FORMAT:
            case STATE:
                return SELECT;
            case CUSTOM_PERMISSIONS:
                return CONTENT;
            default: // it's not standard, therefore it is the render type.
                return this;
        }
    }

    public static boolean isContact(String documentType) {
        for (BlockType blockType : emailElements()) {
            if (blockType.code.equals(documentType)) {
                return true;
            }
        }

        for (BlockType blockType : phoneElements()) {
            if (blockType.code.equals(documentType)) {
                return true;
            }
        }

        return false;
    }

    public String label() {
        return label;
    }

    public boolean isStandard() {
        return standardElements.contains(this);
    }

    public boolean isCustom() {
        return customElements().contains(this);
    }

    public boolean isStoryAsk() {
        return this == STORY_ASK_RICH ||
                this == STORY_ASK_PLAIN;
    }

    public boolean isEmail() {
        return emailElements.contains(this);
    }

    public boolean isPhone() {
        return phoneElements.contains(this);
    }
}
