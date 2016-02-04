package org.consumersunion.stories.server.api.rest.converters;

import org.consumersunion.stories.common.shared.dto.DocumentType;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.springframework.stereotype.Component;

import com.google.common.base.Converter;

@Component
public class DocumentTypeConverter extends Converter<DocumentType, SystemEntityRelation> {
    @Override
    protected SystemEntityRelation doForward(DocumentType documentType) {
        return typeToRelation(documentType);
    }

    @Override
    protected DocumentType doBackward(SystemEntityRelation systemEntityRelation) {
        return convertDocumentType(systemEntityRelation);
    }

    private DocumentType convertDocumentType(SystemEntityRelation systemEntityRelation) {
        switch (systemEntityRelation) {
            case ANSWER_SET:
                return DocumentType.RESPONSE;
            case ATTACHMENT:
                return DocumentType.ATTACHMENT;
            case BODY:
            case CONTENT:
                return DocumentType.CONTENT;
            case DEFAULT_PERMISSIONS:
                return DocumentType.DEFAULT_PERMISSIONS;
            case NOTE:
                return DocumentType.NOTE;
            case STYLE:
                return DocumentType.STYLE;
            case SURVEY:
                return DocumentType.FORM;
            default:
                throw new GeneralException(
                        "Unknown 'systemEntityRelation' value: '" + systemEntityRelation.toString() + "'.");
        }
    }

    private SystemEntityRelation typeToRelation(DocumentType documentType) {
        switch (documentType) {
            case ATTACHMENT:
                return SystemEntityRelation.ATTACHMENT;
            case BODY:
                return SystemEntityRelation.BODY;
            case CONTENT:
                return SystemEntityRelation.CONTENT;
            case DEFAULT_PERMISSIONS:
                return SystemEntityRelation.DEFAULT_PERMISSIONS;
            case NOTE:
                return SystemEntityRelation.NOTE;
            case RESPONSE:
                return SystemEntityRelation.ANSWER_SET;
            case FORM:
                return SystemEntityRelation.SURVEY;
            default:
                throw new GeneralException("Unknown document type: '" + documentType.toString() + "'.");
        }
    }
}
