package app.getgainz.annotation;

import app.getgainz.annotation.validator.GetGainzDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = GetGainzDateValidator.class)
public @interface GetGainzDate {

	String message() default "{getgainz.validation.message.invaliddate}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}
