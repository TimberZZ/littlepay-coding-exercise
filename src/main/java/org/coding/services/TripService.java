package org.coding.services;

import org.coding.models.TapInfo;
import org.coding.models.TripInfo;

import java.util.ArrayList;
import java.util.List;

/**
 *  Functions related to prepare the trip infos.
 */
public class TripService {

    /**
     * Convert the tap infos read from the input CSV file into trip infos.
     *
     * @param tapInfos info for each tap.
     * @return list of tripInfos.
     */
    public List<TripInfo> convertTapInfosToTripInfos(List<TapInfo> tapInfos) {
        List<TripInfo> tripInfos = new ArrayList<>();

        return tripInfos;
    }

}
