package ru.finex.ws.hydra.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.finex.ws.hydra.validation.impl.StarterClassValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author m0nster.mind
 */
@Constraint(validatedBy = StarterClassValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidStarterClass {

    String message() default "invalid starter class";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
