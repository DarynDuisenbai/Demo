package com.example.demo.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class UTCFormatter {
    public LocalDateTime convertUTCToUTCPlus5(LocalDateTime dateTime) {
        ZoneId localZoneId = ZoneId.systemDefault();

        ZonedDateTime localZonedDateTime = dateTime.atZone(localZoneId);
        ZonedDateTime utcPlus5ZonedDateTime = localZonedDateTime.withZoneSameInstant(ZoneOffset.ofHours(5));

        return utcPlus5ZonedDateTime.toLocalDateTime()
                .withNano(0)
                .truncatedTo(ChronoUnit.MINUTES);
    }
}
