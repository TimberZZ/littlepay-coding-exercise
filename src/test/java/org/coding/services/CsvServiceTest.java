package org.coding.services;

import org.coding.models.TapInfo;
import org.coding.models.TapType;
import org.coding.models.TripInfo;
import org.coding.models.TripStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CsvServiceTest {

    private static final String TEST_INPUT_FILE = "test_input.csv";
    private static final String TEST_OUTPUT_FILE = "test_output.csv";

    private CsvService csvService;

    @BeforeEach
    void setUp() {
        csvService = new CsvService();
    }

    @Test
    void testReadEmptyFile() throws IOException {
        FileWriter writer = new FileWriter(TEST_INPUT_FILE);
        writer.close();
        List<TapInfo> tapInfos = csvService.readFromInputFile(TEST_INPUT_FILE);
        assertEquals(0, tapInfos.size());
    }

    @Test
    void testReadTapsFromFile() throws IOException  {
        FileWriter writer = new FileWriter(TEST_INPUT_FILE);
        writer.write("ID,DateTimeUTC,TapType,StopId,CompanyId,BusID,PAN\n");
        writer.write("1,22-01-2023 13:00:00,ON,Stop1,Company1,Bus37,5500005555555559\n");
        writer.write("2,22-01-2023 13:05:00,OFF,Stop2,Company1,Bus37,5500005555555559\n");
        writer.close();

        List<TapInfo> tapInfos = csvService.readFromInputFile(TEST_INPUT_FILE);

        assertEquals(2, tapInfos.size());

        TapInfo tapInfo1 = tapInfos.get(0);
        assertEquals("22-01-2023 13:00:00", tapInfo1.getDateTimeUtc());
        assertEquals(TapType.ON, tapInfo1.getTapType());
        assertEquals("Stop1", tapInfo1.getStopId());
        assertEquals("5500005555555559", tapInfo1.getPan());

        TapInfo tapInfo2 = tapInfos.get(1);
        assertEquals("22-01-2023 13:05:00", tapInfo2.getDateTimeUtc());
        assertEquals(TapType.OFF, tapInfo2.getTapType());
        assertEquals("Stop2", tapInfo2.getStopId());
        assertEquals("5500005555555559", tapInfo2.getPan());
    }

    @Test
    void testWriteTripsToFile() throws IOException {
        List<TripInfo> trips = Arrays.asList(
                new TripInfo("22-01-2023 13:00:00", "22-01-2023 13:05:00", 300, "Stop1", "Stop2", 3.25, "Company1", "Bus37", "5500005555555559", TripStatus.COMPLETED),
                new TripInfo("22-01-2023 09:20:00", "", 0, "Stop3", "", 5.50, "Company1", "Bus36", "4111111111111111", TripStatus.INCOMPLETE)
        );

        csvService.writeTripsToFile(TEST_OUTPUT_FILE, trips);

        BufferedReader br = new BufferedReader(new FileReader(TEST_OUTPUT_FILE));
        String header = br.readLine();
        String row1 = br.readLine();
        String row2 = br.readLine();
        br.close();

        assertNotNull(header);
        assertEquals("Started, Finished, DurationSecs, FromStopId, ToStopId, ChargeAmount, CompanyId, BusID, PAN, Status", header);
        assertEquals("22-01-2023 13:00:00, 22-01-2023 13:05:00, 300, Stop1, Stop2, $3.25, Company1, Bus37, 5500005555555559, COMPLETED", row1);
        assertEquals("22-01-2023 09:20:00, , 0, Stop3, , $5.50, Company1, Bus36, 4111111111111111, INCOMPLETE", row2);
    }

    @AfterEach
    void cleanTestFile() {
        new File(TEST_INPUT_FILE).delete();
        new File(TEST_OUTPUT_FILE).delete();
    }
}
