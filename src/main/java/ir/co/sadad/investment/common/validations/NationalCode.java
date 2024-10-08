package ir.co.sadad.investment.common.validations;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(NationalCode.List.class)
@Documented
@Constraint(validatedBy = {NationalCodeValidator.class})
public @interface NationalCode {

    String message() default "VALIDATION_SSN_PATTERN_NOT_VALID";

    String messageNotBlank() default "VALIDATION_SSN_MUST_NOT_BE_NULL";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value() default "";

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
       NationalCode[] value();
    }
}