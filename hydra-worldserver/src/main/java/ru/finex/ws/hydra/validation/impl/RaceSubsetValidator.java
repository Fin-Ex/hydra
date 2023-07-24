package ru.finex.ws.hydra.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ArrayUtils;
import ru.finex.ws.hydra.model.enums.Race;
import ru.finex.ws.hydra.validation.RaceSubset;

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
