package org.coding.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ChargeCalculator {
    private static final Map<String, Map<String, Double>> chargeMap = new HashMap<>();
    private static final Map<String, Double> maxChargeMap = new HashMap<>();

    static {
        chargeMap.put("Stop1", new HashMap<>());
        chargeMap.put("Stop2", new HashMap<>());
        chargeMap.put("Stop3", new HashMap<>());

        chargeMap.get("Stop1").put("Stop2", 3.25);
        chargeMap.get("Stop1").put("Stop3", 7.30);
        chargeMap.get("Stop2").put("Stop1", 3.25);
        chargeMap.get("Stop2").put("Stop3", 5.50);
        chargeMap.get("Stop3").put("Stop1", 7.30);
        chargeMap.get("Stop3").put("Stop2", 5.50);

        // record highest charge for each stop into map
        for (String stop : chargeMap.keySet()) {
            double maxFare = chargeMap.get(stop).values().stream().max(Double::compare).orElse(0.0);
            maxChargeMap.put(stop, maxFare);
        }
    }

    /**
     * Calculate the charge between two stops.
     *
     * @param fromStop the origin stop
     * @param toStop   the destination stop
     * @return the charge amount, or 0.0 if not found and log an error
     */
    public static Double calculateCharge(String fromStop, String toStop) {
        Double charge = chargeMap.getOrDefault(fromStop, new HashMap<>()).getOrDefault(toStop, 0.0);
        if (charge == 0) {
            log.error("calculateCharge do not find result, from:{}, to:{}", fromStop, toStop);
        }
        return charge;
    }

    /**
     * Get the maximum charge from a given stop (for incomplete trips).
     *
     * @param fromStop the origin stop
     * @return the highest charge from this stop, or 0.0 if not found and log an error
     */
    public static double getMaxCharge(String fromStop) {
        Double charge = maxChargeMap.getOrDefault(fromStop, 0.0);
        if (charge == 0) {
            log.error("getMaxCharge do not find result, from:{}", fromStop);
        }
        return charge;
    }
}
