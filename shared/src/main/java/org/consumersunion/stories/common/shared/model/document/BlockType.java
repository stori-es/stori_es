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
    STARS("STARS", "Stars"),
    NUMBERS("NUMBERS", "Stars"),
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
    STORY_ASK("STORY_ASK", "Question Block - Story Ask"),
    CUSTOM_PERMISSIONS("CUSTOM_PERMISSIONS", "Permissions Block"),
    UPDATES_OPT_IN("UPDATES_OPT_IN", "Question Block - Subscription"),
    STORY_TITLE("STORY_TITLE", "Question Block - Story Title"),
    RATING("RATING", "Rating");

    private final static List<BlockType> customElements;
    private final static List<BlockType> standardElements;
    private final static List<BlockType> textTypes;

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
                STARS,
                NUMBERS,
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
                STORY_ASK,
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

    public static List<BlockType> standardElements() {
        return standardElements;
    }

    public static List<BlockType> emailElements() {
        return Arrays.asList(
                EMAIL_WORK,
                EMAIL,
                EMAIL_OTHER
        );
    }

    public static List<BlockType> phoneElements() {
        return Arrays.asList(
                PHONE,
                PHONE_MOBILE,
                PHONE_WORK,
                PHONE_OTHER
        );
    }

    public static BlockType valueOfCode(String code) {
        for (BlockType blockType : values()) {
            if (blockType.code().equals(code)) {
                return blockType;
            }
        }

        return null;
    }

    public static Boolean isRequired(BlockType blockType) {
        return !(blockType == BlockType.UPDATES_OPT_IN ||
                blockType == BlockType.PREFERRED_EMAIL_FORMAT ||
                blockType == BlockType.PHONE ||
                blockType == BlockType.RADIO ||
                blockType == BlockType.EMAIL);
    }

    public static boolean isText(BlockType formType) {
        return formType != null && textTypes.contains(formType);
    }

    public String code() {
        return code;
    }

    public String label() {
        return label;
    }

    public boolean isStandard() {
        return standardElements().contains(this);
    }

    public boolean isCustom() {
        return customElements().contains(this);
    }
}
