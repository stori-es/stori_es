package org.consumersunion.stories.dashboard.client.application.ui.block;

import java.util.Collection;

import org.consumersunion.stories.common.client.ui.block.QuestionElement;
import org.consumersunion.stories.common.client.util.Callback;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.questionnaire.ContactBlock;
import org.consumersunion.stories.common.shared.model.type.ContactType;
import org.consumersunion.stories.dashboard.client.application.ui.block.configurator.BlockConfigurator;
import org.consumersunion.stories.dashboard.client.application.ui.block.configurator.ContactConfigurator;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class ContactBlockBuilder extends BlockBuilder {
    private final QuestionElement<ContactBlock> preview;
    private final ContactConfigurator contactConfigurator;

    private ContactBlock value;

    @Inject
    ContactBlockBuilder(
            Binder uiBinder,
            @Assisted QuestionElement<ContactBlock> questionElement,
            @Assisted BlockConfigurator<ContactBlock> questionConfigurator,
            @Assisted("editMode") boolean editMode,
            @Assisted("readOnly") boolean readOnly) {
        super(uiBinder, questionElement, questionConfigurator, editMode, readOnly);

        this.preview = questionElement;
        this.value = questionConfigurator.getEditedValue();
        this.contactConfigurator = (ContactConfigurator) questionConfigurator;

        setShowDuplicate(value.getStandardMeaning() == null);

        questionConfigurator.setDoneCallback(new Callback<ContactBlock>() {
            @Override
            public void onSuccess(ContactBlock question) {
                value = question;
                preview.display(value);
                handler.onBlockEdited(ContactBlockBuilder.this);
                switchTo(PREVIEW);
            }

            @Override
            public void onCancel() {
                switchTo(PREVIEW);
            }
        });
    }

    @Override
    public Block getValue() {
        return value;
    }

    public void setOptions(Collection<ContactType> options) {
        contactConfigurator.setOptions(options);
    }
}
