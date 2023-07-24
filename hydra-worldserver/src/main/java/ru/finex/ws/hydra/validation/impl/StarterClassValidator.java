package ru.finex.ws.hydra.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.finex.ws.hydra.model.enums.ClassId;
import ru.finex.ws.hydra.validation.ValidStarterClass;

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
