package org.coding.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TapInfo {
    private Long id;
    private String dateTimeUtc;
    private TapType tapType;
    private String stopId;
    private String companyId;
    private String busId;
    private String pan;
}
