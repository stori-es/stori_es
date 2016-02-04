package org.consumersunion.stories.dashboard.client.application.ui.block.configurator;

import java.util.Arrays;
import java.util.List;

import org.consumersunion.stories.common.client.i18n.SizeAndPositionLabels;
import org.consumersunion.stories.common.client.resource.ChosenResources;
import org.consumersunion.stories.common.client.widget.ContentKind;
import org.consumersunion.stories.common.client.widget.RadioButtonGroup;
import org.consumersunion.stories.common.shared.model.document.SubmitBlock;
import org.consumersunion.stories.common.shared.model.document.SubmitBlock.NextDocument;
import org.consumersunion.stories.dashboard.client.application.util.NextDocumentRenderer;

import com.arcbees.chosen.client.ChosenOptions;
import com.arcbees.chosen.client.gwt.ChosenValueListBox;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import static com.google.gwt.query.client.GQuery.$;

public class SubmitBlockConfigurator extends AbstractConfigurator<SubmitBlock>
        implements BlockConfigurator<SubmitBlock> {
    interface Binder extends UiBinder<Widget, SubmitBlockConfigurator> {
    }

    interface Driver extends SimpleBeanEditorDriver<SubmitBlock, SubmitBlockConfigurator> {
    }

    @UiField(provided = true)
    final ValueListBox<SubmitBlock.Size> size;
    @UiField(provided = true)
    final RadioButtonGroup<SubmitBlock.Position> position;
    @UiField(provided = true)
    @Ignore
    final ChosenValueListBox<NextDocument> nextDocumentSelect;

    @UiField
    TextBox prompt;
    @UiField
    @Ignore
    Label textError;

    private final ChosenResources chosenResources;

    @Inject
    SubmitBlockConfigurator(
            Binder uiBinder,
            Driver driver,
            SizeAndPositionLabels labels,
            ChosenResources chosenResources,
            @Assisted SubmitBlock submitBlock) {
        super(driver, submitBlock);

        this.chosenResources = chosenResources;

        size = new ValueListBox<SubmitBlock.Size>(this.<SubmitBlock.Size>createRenderer(labels));
        size.setValue(submitBlock.getSize());
        size.setAcceptableValues(Arrays.asList(SubmitBlock.Size.values()));

        position = new RadioButtonGroup<SubmitBlock.Position>(this.<SubmitBlock.Position>createRenderer(labels),
                "position");
        position.add(SubmitBlock.Position.values());

        ChosenOptions chosenOptions = new ChosenOptions()
                .setDisableSearchThreshold(10)
                .setResources(chosenResources);
        nextDocumentSelect = new ChosenValueListBox<NextDocument>(new NextDocumentRenderer(), chosenOptions);
        nextDocumentSelect.addValueChangeHandler(new ValueChangeHandler<NextDocument>() {
            @Override
            public void onValueChange(ValueChangeEvent<NextDocument> event) {
                addIconToElement();
            }
        });

        initWidget(uiBinder.createAndBindUi(this));
        driver.initialize(this);

        setErrorLabels(textError);
        init();
    }

    @Override
    public boolean validate() {
        if (!driver.hasErrors() && !Strings.isNullOrEmpty(prompt.getText().trim())) {
            resetErrors();
            return true;
        } else {
            textError.setText(messages.requiredField());
            return false;
        }
    }

    @Override
    protected void onAttach() {
        super.onAttach();

        Element[] options = $("ul", nextDocumentSelect.getParent())
                .find("li")
                .elements();

        $("a." + chosenResources.css().chznSingle(), nextDocumentSelect.getParent())
                .prepend("<i></i>");
        addIconToElement();

        List<NextDocument> nextDocuments = getEditedValue().getNextDocuments();
        for (int i = 0; i < options.length; i++) {
            NextDocument nextDocument = nextDocuments.get(i);
            options[i].addClassName(getIconForRelation(nextDocument));
        }
    }

    @Override
    public boolean isNew() {
        return Strings.isNullOrEmpty(getEditedValue().getPrompt());
    }

    @Override
    protected void init() {
        SubmitBlock submitBlock = getEditedValue();
        setNextDocuments(submitBlock);

        super.init();
    }

    @Override
    protected void onDone() {
        SubmitBlock submitBlock = driver.flush();
        if (validate()) {
            submitBlock.setNextDocument(nextDocumentSelect.getValue());
            doneCallback.onSuccess(submitBlock);

            setEditedValue(submitBlock);
        }
    }

    private void addIconToElement() {
        NextDocument value = nextDocumentSelect.getValue();

        if (value != null) {
            String icon = getIconForRelation(value);
            $("a i", nextDocumentSelect.getParent())
                    .removeClass()
                    .addClass(icon);
        }
    }

    private void setNextDocuments(SubmitBlock submitBlock) {
        List<NextDocument> nextDocuments = Lists.newArrayList(submitBlock.getNextDocuments());
        nextDocuments.add(0, null);
        nextDocumentSelect.setAcceptableValues(nextDocuments);
        nextDocumentSelect.setValue(Iterables.tryFind(nextDocuments, findSelectedDocument(submitBlock)).orNull(), true);
    }

    private String getIconForRelation(NextDocument nextDocument) {
        return ContentKind.fromRelation(nextDocument.getRelation()).getAlternateIcon();
    }

    private Predicate<NextDocument> findSelectedDocument(final SubmitBlock submitBlock) {
        return new Predicate<NextDocument>() {
            @Override
            public boolean apply(NextDocument input) {
                return submitBlock.getNextDocument() != null && input != null
                        && input.getDocumentId() == submitBlock.getNextDocument().getDocumentId();
            }
        };
    }

    private <T extends Enum<T>> Renderer<T> createRenderer(final ConstantsWithLookup labels) {
        return new AbstractRenderer<T>() {
            @Override
            public String render(T object) {
                return labels.getString(object.name());
            }
        };
    }
}
