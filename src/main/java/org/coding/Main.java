package org.coding;

import org.coding.models.TapInfo;
import org.coding.models.TripInfo;
import org.coding.services.CsvService;
import org.coding.services.TripService;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        String inputFilePath = "";
        String outputFilePath = "";

        CsvService csvService = new CsvService();
        TripService tripService = new TripService();

        // read from csv file
        List<TapInfo> tapInfos = csvService.readFromInputFile(inputFilePath);

        // process taps to trips
        List<TripInfo> tripInfos = tripService.convertTapInfosToTripInfos(tapInfos);

        // output result to csv file
        csvService.writeTripsToFile(outputFilePath, tripInfos);
    }
}
