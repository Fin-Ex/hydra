package ru.finex.ws.hydra.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ArrayUtils;
import ru.finex.ws.hydra.model.enums.Gender;
import ru.finex.ws.hydra.validation.GenderSubset;

/**
 * @author m0nster.mind
 */
public class GenderSubsetValidator implements ConstraintValidator<GenderSubset, Gender> {

    private Gender[] subset;

    @Override
    public void initialize(GenderSubset constraintAnnotation) {
        subset = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Gender value, ConstraintValidatorContext context) {
        return value == null || ArrayUtils.contains(subset, value);
    }

}
