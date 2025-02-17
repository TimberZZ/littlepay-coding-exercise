package org.coding.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilsTest {

    @Test
    void testCalculateChargeBetweenStops() {
        assertEquals(3.25, ChargeCalculator.calculateCharge("Stop1", "Stop2"));
        assertEquals(7.30, ChargeCalculator.calculateCharge("Stop1", "Stop3"));
        assertEquals(5.50, ChargeCalculator.calculateCharge("Stop2", "Stop3"));
        assertEquals(7.30, ChargeCalculator.calculateCharge("Stop3", "Stop1"));
    }

    @Test
    void testCalculateHighestCharge() {
        assertEquals(7.30, ChargeCalculator.getMaxCharge("Stop1"));
        assertEquals(5.50, ChargeCalculator.getMaxCharge("Stop2"));
        assertEquals(7.30, ChargeCalculator.getMaxCharge("Stop3"));
    }

    @Test
    void testCalculateDuration() {
        assertEquals(300, TimeUtils.calculateDuration("22-01-2023 13:00:00", "22-01-2023 13:05:00"));
        assertEquals(3600, TimeUtils.calculateDuration("22-01-2023 12:00:00", "22-01-2023 13:00:00"));
        assertEquals(0, TimeUtils.calculateDuration("22-01-2023 12:00:00", "22-01-2023 12:00:00"));
    }

}
