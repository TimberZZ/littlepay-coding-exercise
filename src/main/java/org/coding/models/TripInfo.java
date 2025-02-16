package org.coding.models;

import lombok.Data;

@Data
public class TripInfo {
    private String started;
    private String finished;
    private Integer durationSecs;
    private String fromStopId;
    private String toStopId;
    private Double chargeAmount;
    private String companyId;
    private String busId;
    private String pan;
    private TripStatus status;
}
