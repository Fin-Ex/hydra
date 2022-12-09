package ru.finex.ws.l2.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.finex.ws.l2.model.enums.Race;
import ru.finex.ws.l2.validation.impl.RaceSubsetValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author m0nster.mind
 */
@Constraint(validatedBy = RaceSubsetValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RaceSubset {

    Race[] value();
    String message() default "must be {value}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
