package ru.finex.ws.l2.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ArrayUtils;
import ru.finex.ws.l2.model.enums.Race;
import ru.finex.ws.l2.validation.RaceSubset;

/**
 * @author m0nster.mind
 */
public class RaceSubsetValidator implements ConstraintValidator<RaceSubset, Race> {

    private Race[] subset;

    @Override
    public void initialize(RaceSubset constraintAnnotation) {
        subset = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Race value, ConstraintValidatorContext context) {
        return value == null || ArrayUtils.contains(subset, value);
    }

}
