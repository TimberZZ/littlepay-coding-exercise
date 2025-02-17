package org.coding.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TapInfo {
    private Long id;
    private String dateTimeUtc;
    private TapType tapType;
    private String stopId;
    private String companyId;
    private String busId;
    private String pan;
}
