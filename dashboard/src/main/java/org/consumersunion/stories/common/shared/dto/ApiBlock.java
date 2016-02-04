package org.consumersunion.stories.common.shared.dto;

import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockAudio;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockCollection;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockConstraints;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockDocument;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockImage;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockLabel;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockNextDocument;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockOptions;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockPermission;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockStory;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockStyles;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockVideo;
import org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockInteraction;

import com.fasterxml.jackson.annotation.JsonProperty;

import static org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockFormat;
import static org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockType;

public class ApiBlock {
    @JsonProperty("block_type")
    private ApiBlockType blockType;
    private ApiBlockLabel labels;
    private ApiBlockFormat format;
    private String value;
    private ApiBlockOptions options;
    private ApiBlockStyles styles;
    private ApiBlockConstraints constraints;
    private ApiBlockAudio audio;
    private ApiBlockCollection collection;
    private ApiBlockDocument document;
    private ApiBlockImage image;
    private ApiBlockPermission permission;
    private ApiBlockStory story;
    private ApiBlockVideo video;
    @JsonProperty("next_document")
    private ApiBlockNextDocument nextDocument;
    private ApiBlockInteraction interaction;

    public ApiBlockType getBlockType() {
        return blockType;
    }

    public void setBlockType(ApiBlockType blockType) {
        this.blockType = blockType;
    }

    public ApiBlockLabel getLabels() {
        return labels;
    }

    public void setLabels(ApiBlockLabel labels) {
        this.labels = labels;
    }

    public ApiBlockFormat getFormat() {
        return format;
    }

    public void setFormat(ApiBlockFormat format) {
        this.format = format;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ApiBlockOptions getOptions() {
        return options;
    }

    public void setOptions(ApiBlockOptions options) {
        this.options = options;
    }

    public ApiBlockStyles getStyles() {
        return styles;
    }

    public void setStyles(ApiBlockStyles styles) {
        this.styles = styles;
    }

    public ApiBlockConstraints getConstraints() {
        return constraints;
    }

    public void setConstraints(ApiBlockConstraints constraints) {
        this.constraints = constraints;
    }

    public ApiBlockAudio getAudio() {
        return audio;
    }

    public void setAudio(ApiBlockAudio audio) {
        this.audio = audio;
    }

    public ApiBlockCollection getCollection() {
        return collection;
    }

    public void setCollection(ApiBlockCollection collection) {
        this.collection = collection;
    }

    public ApiBlockDocument getDocument() {
        return document;
    }

    public void setDocument(ApiBlockDocument document) {
        this.document = document;
    }

    public ApiBlockImage getImage() {
        return image;
    }

    public void setImage(ApiBlockImage image) {
        this.image = image;
    }

    public ApiBlockPermission getPermission() {
        return permission;
    }

    public void setPermission(ApiBlockPermission permission) {
        this.permission = permission;
    }

    public ApiBlockStory getStory() {
        return story;
    }

    public void setStory(ApiBlockStory story) {
        this.story = story;
    }

    public ApiBlockVideo getVideo() {
        return video;
    }

    public void setVideo(ApiBlockVideo video) {
        this.video = video;
    }

    public ApiBlockNextDocument getNextDocument() {
        return nextDocument;
    }

    public void setNextDocument(ApiBlockNextDocument nextDocument) {
        this.nextDocument = nextDocument;
    }

    public ApiBlockInteraction getInteraction() {
        return interaction;
    }

    public void setInteraction(
            ApiBlockInteraction interaction) {
        this.interaction = interaction;
    }
}
