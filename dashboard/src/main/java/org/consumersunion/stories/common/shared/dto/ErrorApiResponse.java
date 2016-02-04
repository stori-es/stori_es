package org.consumersunion.stories.common.shared.dto;

import java.util.List;

import com.google.common.collect.Lists;

public class ErrorApiResponse extends AbstractApiResponse<BasicResponse> {
    /**
     * For deserialization only
     */
    private ErrorApiResponse() {
    }

    public ErrorApiResponse(String message) {
        this(Lists.newArrayList(new Message(message, "")));
    }

    public ErrorApiResponse(List<Message> messages) {
        Metadata metadata = new Metadata();
        metadata.setMessages(Lists.newArrayList(messages));
        setMetadata(metadata);
    }
}
