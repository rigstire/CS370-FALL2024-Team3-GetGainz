package app.getgainz.annotation.validator;

import app.getgainz.annotation.GetGainzDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class GetGainzDateValidator implements ConstraintValidator<GetGainzDate, String> {

	private final DateTimeFormatter dateTimeFormatter;

	@Autowired
	public GetGainzDateValidator(@Value("${getgainz.validation.pattern.date:yyyy-MM-dd}") String datePattern) {
		dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern);
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		try {
			LocalDate.parse(value, dateTimeFormatter);
			return true;
		} catch (DateTimeParseException parseException) {
			return false;
		}
	}
}
