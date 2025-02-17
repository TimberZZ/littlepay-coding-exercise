package org.coding.services;

import org.coding.models.TapInfo;
import org.coding.models.TapType;
import org.coding.models.TripInfo;
import org.coding.models.TripStatus;
import org.coding.utils.ChargeCalculator;
import org.coding.utils.TimeUtils;

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
        TapInfo currentTapOn = null;

        for (TapInfo tapInfo : tapInfos) {
            if (tapInfo.getTapType() == TapType.ON) {
                // check if previous tap on exist, if so, create an incomplete trip for it.
                checkIfCreateIncompleteTrip(currentTapOn, tripInfos);
                currentTapOn = tapInfo;
            } else if (tapInfo.getTapType() == TapType.OFF) {
                if (checkIfHaveValidTapOn(currentTapOn, tapInfo)) {
                    // check if previous tap on can be paired with this tap off. If so, create a completed/cancelled trip.
                    tripInfos.add(createCompletedOrCancelledTrip(currentTapOn, tapInfo));
                    currentTapOn = null;
                } else {
                    // if not, create an incomplete trip for previous tap on if it exists
                    // and create an error trip for single tap off.
                    checkIfCreateIncompleteTrip(currentTapOn, tripInfos);
                    currentTapOn = null;
                    tripInfos.add(createIncompleteTapOffTrip(tapInfo));
                }
            }
        }
        // check if there's still unpaired tap on, process as incomplete trip.
        checkIfCreateIncompleteTrip(currentTapOn, tripInfos);

        return tripInfos;
    }

    /**
     * As mentioned in the exercise description, a trip with tap on and no tap off will be seemed as incomplete.
     * If we assume that people use same pad to tap on/off, they need to have a tap on before tap off,
     * otherwise it will be a tap on when user first tap it, even it happens when user off-boarding the bus.
     * Or this situation can be assumed as incomplete trip if we have different pad use to tap on/off,
     * as we do not have enough information here, we simply assume that this case is an incomplete case.
     *
     * @param tapInfo tap off detail
     * @return single tap off tripInfo
     */
    private TripInfo createIncompleteTapOffTrip(TapInfo tapInfo) {
        return TripInfo.builder()
                .started("")
                .finished(tapInfo.getDateTimeUtc())
                .durationSecs(0)
                .fromStopId("")
                .toStopId(tapInfo.getStopId())
                .companyId(tapInfo.getCompanyId())
                .busId(tapInfo.getBusId())
                .pan(tapInfo.getPan())
                .chargeAmount(ChargeCalculator.getMaxCharge(tapInfo.getStopId()))
                .status(TripStatus.INCOMPLETE).build();
    }

    /**
     * Create trip info by using tap on and off infos.
     *
     * @param currentTapOn tap on info
     * @param tapOffInfo tap off info
     * @return trip info created by on/off infos.
     */
    private TripInfo createCompletedOrCancelledTrip(TapInfo currentTapOn, TapInfo tapOffInfo) {
        Double chargeAmount = 0.0;
        TripStatus tripStatus;

        if (currentTapOn.getStopId().equals(tapOffInfo.getStopId())) {
            tripStatus = TripStatus.CANCELLED;
        } else {
            chargeAmount = ChargeCalculator.calculateCharge(currentTapOn.getStopId(), tapOffInfo.getStopId());
            tripStatus = TripStatus.COMPLETED;
        }

        return TripInfo.builder()
                .started(currentTapOn.getDateTimeUtc())
                .finished(tapOffInfo.getDateTimeUtc())
                .durationSecs(TimeUtils.calculateDuration(currentTapOn.getDateTimeUtc(), tapOffInfo.getDateTimeUtc()))
                .fromStopId(currentTapOn.getStopId())
                .toStopId(tapOffInfo.getStopId())
                .companyId(currentTapOn.getCompanyId())
                .busId(currentTapOn.getBusId())
                .chargeAmount(chargeAmount)
                .status(tripStatus)
                .pan(currentTapOn.getPan()).build();
    }

    /**
     * Check if this tap off have valid tap on stored in currentTapOn.
     *
     * @param currentTapOn tap on info
     * @param tapInfo tap off info
     * @return boolean true/false
     */
    private boolean checkIfHaveValidTapOn(TapInfo currentTapOn, TapInfo tapInfo) {
        return currentTapOn != null && tapInfo != null && !currentTapOn.getPan().isBlank()
                && currentTapOn.getPan().equals(tapInfo.getPan());
    }

    /**
     * Check if currentTapOn is not null, if so, create an incomplete trip for this tap on.
     * The reason is that the following tap is still tap on, so if the previous tap on exist, it should be incomplete.
     *
     * @param currentTapOn tap on info
     * @param tripInfos list of trip infos
     */
    private void checkIfCreateIncompleteTrip(TapInfo currentTapOn, List<TripInfo> tripInfos) {
        if (currentTapOn != null) {
            tripInfos.add(createIncompleteTapOnTrip(currentTapOn));
        }
    }

    /**
     * Use single tap on info to create the incomplete trip
     * @param tapInfo tap on info
     * @return trip info
     */
    private TripInfo createIncompleteTapOnTrip(TapInfo tapInfo) {
        return TripInfo.builder()
                .started(tapInfo.getDateTimeUtc())
                .finished("")
                .durationSecs(0)
                .fromStopId(tapInfo.getStopId())
                .toStopId("")
                .companyId(tapInfo.getCompanyId())
                .busId(tapInfo.getBusId())
                .pan(tapInfo.getPan())
                .chargeAmount(ChargeCalculator.getMaxCharge(tapInfo.getStopId()))
                .status(TripStatus.INCOMPLETE).build();
    }

}
