package org.coding.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class TimeUtils {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    /**
     * Calculates the duration in seconds between startTime and EndTime in String.
     *
     * @param startTimeStr Start time as a string
     * @param endTimeStr   End time as a string
     * @return Duration in seconds or -1 if exception occurs.
     */
    public static Integer calculateDuration(String startTimeStr, String endTimeStr) {
        try {
            LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
            LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);

            return (int) Duration.between(startTime, endTime).getSeconds();
        } catch (Exception e) {
            log.error("calculateDuration error", e);
            return -1;
        }
    }
}
