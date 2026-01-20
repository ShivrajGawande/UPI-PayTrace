package com.upipaytrace.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class DateParserUtil {

    private static final List<DateTimeFormatter> FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
    );

    private DateParserUtil() {}

    public static LocalDate parse(String date) {

        if (date == null || date.trim().isEmpty())
            throw new IllegalArgumentException("Invalid date");

        String value = date.trim();

        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalDate.parse(value, formatter);
            } catch (DateTimeParseException e) {}
        }

        throw new IllegalArgumentException("Unsupported date format: " + date);
    }
}
