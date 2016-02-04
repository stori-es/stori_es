package org.consumersunion.stories.common.shared.model.document;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TextImageTest {
    @Test
    public void defaultConstructor_initializesDefaults() {
        // When
        TextImage block = new TextImage();

        // Then
        assertThat(block.getAltText()).isEmpty();
        assertThat(block.getCaption()).isEmpty();
        assertThat(block.getUrl()).isEmpty();
        assertThat(block.getPosition()).isEqualTo(TextImage.Position.LEFT);
        assertThat(block.getSize()).isEqualTo(TextImage.Size.MEDIUM);
    }
}
