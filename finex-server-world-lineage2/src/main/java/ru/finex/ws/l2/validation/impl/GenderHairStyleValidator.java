package ru.finex.ws.l2.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.finex.ws.l2.validation.GenderHairStyle;
import ru.finex.ws.l2.validation.ValidGenderHairStyle;

/**
 * @author m0nster.mind
 */
public class GenderHairStyleValidator implements ConstraintValidator<ValidGenderHairStyle, GenderHairStyle> {

    @Override
    public boolean isValid(GenderHairStyle value, ConstraintValidatorContext context) {
        return switch (value.getGender()) {
            case MALE -> value.getHairStyle() <= 4;
            case FEMALE -> value.getHairStyle() <= 6;
            default -> false;
        };
    }

}
