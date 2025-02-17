package org.coding.services;

import org.coding.models.TapInfo;
import org.coding.models.TapType;
import org.coding.services.CsvService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CsvServiceTest {

    private static final String TEST_INPUT_FILE = "test_input.csv";

    private CsvService csvService;

    @BeforeEach
    void setUp() throws IOException {
        csvService = new CsvService();
        FileWriter writer = new FileWriter(TEST_INPUT_FILE);
        writer.write("ID,DateTimeUTC,TapType,StopId,CompanyId,BusID,PAN\n");
        writer.write("1,22-01-2023 13:00:00,ON,Stop1,Company1,Bus37,5500005555555559\n");
        writer.write("2,22-01-2023 13:05:00,OFF,Stop2,Company1,Bus37,5500005555555559\n");
        writer.close();
    }

    @Test
    void testReadTapsFromFile() {
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
}
