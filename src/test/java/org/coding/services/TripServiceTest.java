package org.coding.services;

import org.coding.models.TapInfo;
import org.coding.models.TapType;
import org.coding.models.TripInfo;
import org.coding.models.TripStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TripServiceTest {

    private TripService tripService;
    private List<TapInfo> tapInfos;

    @BeforeEach
    void setUp() {
        tripService = new TripService();
        tapInfos = new ArrayList<>();
    }

    @Test
    void testCreateCompletedTrip() {
        TapInfo tapOn = new TapInfo(1L, "22-01-2023 13:00:00", TapType.ON, "Stop1", "Company1", "Bus37", "5500005555555559");
        TapInfo tapOff = new TapInfo(2L, "22-01-2023 13:05:00", TapType.OFF, "Stop2", "Company1", "Bus37", "5500005555555559");
        tapInfos.add(tapOn);
        tapInfos.add(tapOff);

        List<TripInfo> tripInfos = tripService.convertTapInfosToTripInfos(tapInfos);
        assertEquals(1, tripInfos.size());
        TripInfo tripInfo = tripInfos.get(0);
        assertEquals(TripStatus.COMPLETED, tripInfo.getStatus());
        assertEquals(3.25, tripInfo.getChargeAmount(), 0.01);
        assertEquals(300, tripInfo.getDurationSecs());
    }

    @Test
    void testCreateInCompleteTapOnTrip() {
        TapInfo tapOn = new TapInfo(1L, "22-01-2023 13:00:00", TapType.ON, "Stop1", "Company1", "Bus37", "5500005555555559");
        tapInfos.add(tapOn);

        List<TripInfo> tripInfos = tripService.convertTapInfosToTripInfos(tapInfos);
        assertEquals(1, tripInfos.size());
        TripInfo tripInfo = tripInfos.get(0);
        assertEquals(TripStatus.INCOMPLETE, tripInfo.getStatus());
        assertEquals(7.30, tripInfo.getChargeAmount(), 0.01);
    }

    @Test
    void testCreateInCompleteTapOffTrip() {
        TapInfo tapOff = new TapInfo(1L, "22-01-2023 13:05:00", TapType.OFF, "Stop1", "Company1", "Bus37", "5500005555555559");
        tapInfos.add(tapOff);

        List<TripInfo> tripInfos = tripService.convertTapInfosToTripInfos(tapInfos);
        assertEquals(1, tripInfos.size());
        TripInfo tripInfo = tripInfos.get(0);
        assertEquals(TripStatus.INCOMPLETE, tripInfo.getStatus());
        assertEquals(7.30, tripInfo.getChargeAmount(), 0.01);
    }

    @Test
    void testCreateInCompleteTapOnTripWithStop2() {
        TapInfo tapOn = new TapInfo(1L, "22-01-2023 13:00:00", TapType.ON, "Stop2", "Company1", "Bus37", "5500005555555559");
        tapInfos.add(tapOn);

        List<TripInfo> tripInfos = tripService.convertTapInfosToTripInfos(tapInfos);
        assertEquals(1, tripInfos.size());
        TripInfo tripInfo = tripInfos.get(0);
        assertEquals(TripStatus.INCOMPLETE, tripInfo.getStatus());
        assertEquals(5.50, tripInfo.getChargeAmount(), 0.01);
    }

    @Test
    void testCreateCancelledTrip() {
        TapInfo tapOn = new TapInfo(1L, "22-01-2023 13:00:00", TapType.ON, "Stop1", "Company1", "Bus37", "5500005555555559");
        TapInfo tapOff = new TapInfo(2L, "22-01-2023 13:05:00", TapType.OFF, "Stop1", "Company1", "Bus37", "5500005555555559");
        tapInfos.add(tapOn);
        tapInfos.add(tapOff);

        List<TripInfo> tripInfos = tripService.convertTapInfosToTripInfos(tapInfos);
        assertEquals(1, tripInfos.size());
        TripInfo tripInfo = tripInfos.get(0);
        assertEquals(TripStatus.CANCELLED, tripInfo.getStatus());
        assertEquals(0, tripInfo.getChargeAmount(), 0.01);
    }

    @Test
    void testCreateMultipleTrips() {
        // 1 completed trip
        TapInfo tapOn1 = new TapInfo(1L, "22-01-2023 13:00:00", TapType.ON, "Stop1", "Company1", "Bus37", "5500005555555559");
        TapInfo tapOff1 = new TapInfo(2L, "22-01-2023 13:05:00", TapType.OFF, "Stop2", "Company1", "Bus37", "5500005555555559");
        // 1 incomplete tap on trip
        TapInfo tapOn2 = new TapInfo(3L, "23-01-2023 13:05:00", TapType.ON, "Stop1", "Company1", "Bus36", "4111111111111111");
        // 1 incomplete tap off trip
        TapInfo tapOff3 = new TapInfo(4L, "24-01-2023 13:05:00", TapType.OFF, "Stop2", "Company1", "Bus37", "4462030000000000");

        tapInfos.add(tapOn1);
        tapInfos.add(tapOff1);
        tapInfos.add(tapOn2);
        tapInfos.add(tapOff3);

        List<TripInfo> tripInfos = tripService.convertTapInfosToTripInfos(tapInfos);

        assertEquals(3, tripInfos.size());
        TripInfo tripInfo = tripInfos.get(0);
        assertEquals(TripStatus.COMPLETED, tripInfo.getStatus());
        assertEquals(3.25, tripInfo.getChargeAmount(), 0.01);

        tripInfo = tripInfos.get(1);
        assertEquals(TripStatus.INCOMPLETE, tripInfo.getStatus());
        assertEquals(7.30, tripInfo.getChargeAmount(), 0.01);

        tripInfo = tripInfos.get(2);
        assertEquals(TripStatus.INCOMPLETE, tripInfo.getStatus());
        assertEquals(5.50, tripInfo.getChargeAmount(), 0.01);
    }
}
