package com.example.demo.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Component
public class UTCFormatter {
    public LocalDateTime convertUTCToUTCPlus5(LocalDateTime dateTime) {
        ZoneId utcZoneId = ZoneId.of("UTC");
        ZonedDateTime utcZonedDateTime = dateTime.atZone(utcZoneId);

        ZonedDateTime utcPlus5ZonedDateTime = utcZonedDateTime.withZoneSameInstant(ZoneOffset.ofHours(5));

        return utcPlus5ZonedDateTime.toLocalDateTime()
                .withNano(0)
                .truncatedTo(ChronoUnit.MINUTES);
    }
    public String formatDateInRussian(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy 'г.'", new Locale("ru"));
        return dateTime.format(formatter);
    }
}
