package com.example.demo.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class UTCFormatter {
    public LocalDateTime convertUTCToUTCPlus5(LocalDateTime dateTime) {
        return dateTime.atOffset(ZoneOffset.UTC)
                .withOffsetSameInstant(ZoneOffset.ofHours(5))
                .toLocalDateTime();
    }
}
