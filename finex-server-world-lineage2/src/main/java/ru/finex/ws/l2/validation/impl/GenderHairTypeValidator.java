package ru.finex.ws.l2.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.finex.ws.l2.validation.GenderHairType;
import ru.finex.ws.l2.validation.ValidGenderHairType;

/**
 * @author m0nster.mind
 */
public class GenderHairTypeValidator implements ConstraintValidator<ValidGenderHairType, GenderHairType> {

    @Override
    public boolean isValid(GenderHairType value, ConstraintValidatorContext context) {
        return switch (value.getGender()) {
            case MALE -> value.getHairType() <= 4;
            case FEMALE -> value.getHairType() <= 6;
            default -> false;
        };
    }

}
