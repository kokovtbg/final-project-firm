package bg.sirma.project.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class MyDateValidator implements DateValidator {

    @Override
    public boolean isValid(String dateStr, String pattern) {
        try {
            LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException e) {
            return false;
        }

        return true;
    }
}
