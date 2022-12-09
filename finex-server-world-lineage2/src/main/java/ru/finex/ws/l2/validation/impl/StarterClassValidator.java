package ru.finex.ws.l2.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ArrayUtils;
import ru.finex.ws.l2.model.ClassId;
import ru.finex.ws.l2.validation.ValidStarterClass;

/**
 * @author m0nster.mind
 */
public class StarterClassValidator implements ConstraintValidator<ValidStarterClass, Integer> {

    private static final int[] STARTER_CLASSES = {
        ClassId.HUMAN_FIGHTER.getId(),
        ClassId.ElvenFighter.getId(),
        ClassId.DarkFighter.getId(),
        ClassId.OrcFighter.getId(),
        ClassId.DwarvenFighter.getId(),
        ClassId.MALE_SOLDIER.getId(),
        ClassId.FEMALE_SOLDIER.getId(),
        ClassId.ERTHEIA_FIGHTER.getId(),
        ClassId.HumanWizard.getId(),
        ClassId.ElvenWizard.getId(),
        ClassId.DarkWizard.getId(),
        ClassId.OrcMystic.getId(),
        ClassId.DwarvenFighter.getId(),
        ClassId.ERTHEIA_WIZARD.getId()
    };

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return ArrayUtils.contains(STARTER_CLASSES, value);
    }
}
