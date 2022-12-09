package ru.finex.ws.l2.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.finex.ws.l2.validation.impl.GenderHairStyleValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author m0nster.mind
 */
@Constraint(validatedBy = GenderHairStyleValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidGenderHairStyle {

    String message() default "invalid hair style for selected gender";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
