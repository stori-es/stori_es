package org.consumersunion.stories.common.client.ui.block.question;

import org.consumersunion.stories.common.client.ui.form.Validator;
import org.consumersunion.stories.common.client.ui.form.validators.RequiredValidator;
import org.consumersunion.stories.common.client.ui.form.validators.ZipCodeValidator;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;

import java.util.ArrayList;
import java.util.List;

public class ValidatorsFactory {
    public List<Validator> create(Question question) {
        List<Validator> validators = new ArrayList<Validator>();

        if (question.isRequired()) {
            validators.add(new RequiredValidator());
        }

        switch (question.getBlockType()) {
            case ZIP_CODE:
                validators.add(new ZipCodeValidator());
                break;
        }

        return validators;
    }
}
