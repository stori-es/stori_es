package org.consumersunion.stories.common.shared.model.questionnaire;

import java.io.Serializable;

import org.consumersunion.stories.common.shared.model.document.BlockType;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("rating")
@org.codehaus.jackson.annotate.JsonTypeName("rating")
public class RatingQuestion extends Question {
    public enum DisplayType implements Serializable {
        NUMBERS,
        STARS;

        public BlockType toFormType() {
            switch (this) {
                case NUMBERS:
                    return BlockType.NUMBERS;
                case STARS:
                    return BlockType.STARS;
            }
            return null;
        }

        public static DisplayType fromFormType(BlockType formType) {
            if (BlockType.NUMBERS.equals(formType)) {
                return NUMBERS;
            } else {
                return STARS;
            }
        }
    }

    public enum StepType implements Serializable {
        DISCRETE("DISCRETE"),
        HALF_STEP("HALF-STEP");

        private final String code;

        StepType(String code) {
            this.code = code;
        }

        public static StepType fromCode(String code) {
            for (StepType stepType : values()) {
                if (stepType.code().equals(code)) {
                    return stepType;
                }
            }

            return null;
        }

        public String code() {
            return code;
        }
    }

    private StepType stepType;
    private String startLabel;
    private String endLabel;

    public RatingQuestion(
            BlockType formType,
            StepType stepType) {
        this.stepType = stepType;

        setStandardMeaning(BlockType.RATING);
        setFormType(formType);
    }

    public RatingQuestion() {
        this(BlockType.STARS, StepType.DISCRETE);
    }

    public boolean withStars() {
        return BlockType.STARS.equals(getFormType());
    }

    public boolean withLabels() {
        return startLabel != null && endLabel != null;
    }

    public StepType getStepType() {
        return stepType;
    }

    public void setStepType(StepType stepType) {
        this.stepType = stepType;
    }

    public String getStartLabel() {
        return startLabel;
    }

    public void setStartLabel(String startLabel) {
        this.startLabel = startLabel;
    }

    public String getEndLabel() {
        return endLabel;
    }

    public void setEndLabel(String endLabel) {
        this.endLabel = endLabel;
    }

    @Override
    public Object clone() {
        RatingQuestion question = new RatingQuestion();
        question.setStepType(getStepType());
        question.setStartLabel(getStartLabel());
        question.setEndLabel(getEndLabel());

        return super.clone(question);
    }
}
