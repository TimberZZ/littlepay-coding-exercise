package org.coding.services;

import lombok.extern.slf4j.Slf4j;
import org.coding.models.TapInfo;
import org.coding.models.TapType;
import org.coding.models.TripInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *  Operations with CSV file
 */
@Slf4j
public class CsvService {

    /**
     * Read CSV file and convert each line into a TapInfo object.
     *
     * @param filePath The input CSV file path that need to be read.
     * @return List of TabInfos
     */
    public List<TapInfo> readFromInputFile(String filePath) {
        List<TapInfo> tapInfos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] lineElements = line.split(",");
                if (lineElements.length != 7) {
                    log.error("Invalid CSV format: " + line);
                    continue;
                }

                try {
                    TapInfo tapInfo = TapInfo.builder()
                            .id(Long.parseLong(lineElements[0]))
                            .dateTimeUtc(lineElements[1])
                            .tapType(TapType.fromTapString(lineElements[2]))
                            .stopId(lineElements[3])
                            .companyId(lineElements[4])
                            .busId(lineElements[5])
                            .pan(lineElements[6]).build();
                    tapInfos.add(tapInfo);
                } catch (Exception e) {
                    log.error("Error parsing tap record: " + line, e);
                }
            }
        } catch (IOException e) {
            log.error("Error: Input file not found - " + filePath, e);
        }
        return tapInfos;
    }


    /**
     * write the prepared tripInfos into an output CSV file.
     *
     * @param filePath output file path
     * @param tripInfos list of tripInfos
     */
    public void writeTripsToFile(String filePath, List<TripInfo> tripInfos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("Started, Finished, DurationSecs, FromStopId, ToStopId, ChargeAmount, CompanyId, BusID, PAN, Status\n");
            for (TripInfo tripInfo : tripInfos) {
                bw.write(String.format("%s, %s, %d, %s, %s, $%.2f, %s, %s, %s, %s\n",
                        tripInfo.getStarted(), tripInfo.getFinished(), tripInfo.getDurationSecs(), tripInfo.getFromStopId(),
                        tripInfo.getToStopId(), tripInfo.getChargeAmount(), tripInfo.getCompanyId(), tripInfo.getBusId(),
                        tripInfo.getPan(), tripInfo.getStatus()));
            }
        } catch (IOException e) {
            log.error("Error writing output file: " + filePath, e);
        }
    }

}

