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

        public BlockType toBlockType() {
            switch (this) {
	    case NUMBERS:
		return BlockType.RATING_NUMBERS;
	    case STARS:
		return BlockType.RATING_STARS;
            }
            return null;
        }

        public static DisplayType fromBlockType(BlockType blockType) {
            if (BlockType.RATING_NUMBERS.equals(blockType)) {
                return DisplayType.NUMBERS;
            } else {
                return DisplayType.STARS;
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
            BlockType blockType,
            StepType stepType) {
        this.stepType = stepType;

        setBlockType(blockType);
    }

    public RatingQuestion() {
        this(BlockType.RATING_STARS, StepType.DISCRETE);
    }

    public boolean withStars() {
        return BlockType.RATING_STARS.equals(getBlockType());
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
