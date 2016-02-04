package org.consumersunion.stories.common.shared.model.document;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TextImageBlockTest {
    @Test
    public void defaultConstructor_initializesDefaults() {
        // When
        TextImageBlock block = new TextImageBlock();

        // Then
        assertThat(block.getText()).isEmpty();
        assertThat(block.getImage()).isNull();
    }
}
