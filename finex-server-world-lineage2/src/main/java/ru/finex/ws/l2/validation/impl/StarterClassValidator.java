package ru.finex.ws.l2.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.finex.ws.l2.model.ClassId;
import ru.finex.ws.l2.validation.ValidStarterClass;

/**
 * @author m0nster.mind
 */
public class StarterClassValidator implements ConstraintValidator<ValidStarterClass, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        ClassId classId;
        try {
            classId = ClassId.ofId(value);
        } catch (Exception e) {
            return false;
        }

        return classId.getParent() == null;
    }
}
