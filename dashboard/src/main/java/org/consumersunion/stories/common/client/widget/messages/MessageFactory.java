package org.consumersunion.stories.common.client.widget.messages;

import java.util.List;
import java.util.Set;

import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.client.widget.messages.primaryemail.AddPrimaryEmailConfirmationMessage;
import org.consumersunion.stories.common.client.widget.messages.primaryemail.AddPrimaryEmailMessage;
import org.consumersunion.stories.common.client.widget.messages.primaryemail.ChoosePrimaryEmailMessage;
import org.consumersunion.stories.common.client.widget.messages.primaryemail.PrimaryEmailChosenMessage;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionSummary;

public interface MessageFactory {
    SimpleMessage createSimpleMessage(MessageStyle messageStyle, String message);

    InteractiveMessage createInteractiveMessage(InteractiveMessageContent content);

    AddToCollectionsMessage createAddToCollections(int taskId, Set<CollectionSummary> collectionSummaries);

    ExportMessage createExportMessage(int taskId, CollectionSummary collectionSummary);

    ChoosePrimaryEmailMessage createPrimaryEmail(List<Contact> contacts);

    PrimaryEmailChosenMessage createPrimaryEmailChosen(Contact contact);

    AddPrimaryEmailMessage createAddPrimaryEmail();

    AddPrimaryEmailConfirmationMessage createAddPrimaryEmailConfirmation(Contact contact);
}
