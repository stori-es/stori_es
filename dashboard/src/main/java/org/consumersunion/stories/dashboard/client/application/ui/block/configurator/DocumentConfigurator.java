package org.consumersunion.stories.dashboard.client.application.ui.block.configurator;

import org.consumersunion.stories.common.client.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.common.client.service.RpcResourceCheckerServiceAsync;
import org.consumersunion.stories.common.shared.model.document.DocumentBlock;

import com.google.common.base.Strings;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class DocumentConfigurator extends AbstractConfigurator<DocumentBlock>
        implements BlockConfigurator<DocumentBlock> {
    interface Binder extends UiBinder<Widget, DocumentConfigurator> {
    }

    interface Driver extends SimpleBeanEditorDriver<DocumentBlock, DocumentConfigurator> {
    }

    @UiField
    TextBox url;
    @UiField
    @Ignore
    Label textError;
    @UiField
    TextBox title;

    private final CommonI18nErrorMessages errorMessages;
    private final RpcResourceCheckerServiceAsync resourceChecker;

    @Inject
    DocumentConfigurator(
            Binder uiBinder,
            Driver driver,
            CommonI18nErrorMessages errorMessages,
            RpcResourceCheckerServiceAsync resourceChecker,
            @Assisted DocumentBlock document) {
        super(driver, document);

        this.errorMessages = errorMessages;
        this.resourceChecker = resourceChecker;

        initWidget(uiBinder.createAndBindUi(this));
        driver.initialize(this);

        setErrorLabels(textError);
        init();
    }

    @Override
    public boolean validate() {
        if (!driver.hasErrors() && !Strings.isNullOrEmpty(url.getText().trim())) {
            resetErrors();
            return true;
        } else {
            textError.setText(messages.requiredField());
            return false;
        }
    }

    @Override
    public boolean isNew() {
        return Strings.isNullOrEmpty(getEditedValue().getUrl());
    }

    @Override
    protected void onDone() {
        DocumentBlock document = driver.flush();
        if (validate()) {
            verifyUrlValidity(document);
        }
    }

    private void verifyUrlValidity(final DocumentBlock document) {
        resourceChecker.checkURL(document.getUrl(), new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                Window.alert(errorMessages.resourceNotFound());
            }

            @Override
            public void onSuccess(Boolean valid) {
                if (valid) {
                    verifyDocumentValidity(document);
                } else {
                    textError.setText(errorMessages.documentUnavailable());
                }
            }
        });
    }

    private void verifyDocumentValidity(final DocumentBlock document) {
        resourceChecker.checkValidPDF(document.getUrl(), new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                Window.alert(errorMessages.resourceNotFound());
            }

            @Override
            public void onSuccess(Boolean valid) {
                if (valid) {
                    resetErrors();
                    doneCallback.onSuccess(document);
                    setEditedValue(document);
                } else {
                    textError.setText(errorMessages.documentInvalid());
                }
            }
        });
    }
}
