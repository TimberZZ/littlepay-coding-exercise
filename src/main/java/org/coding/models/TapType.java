package org.coding.models;

import lombok.extern.slf4j.Slf4j;

import java.util.logging.Logger;

public enum TapType {
    ON,
    OFF,
    ;

    /**
     * Convert the input string of tap type to an enum type
     * @param tapType input string from CSV file
     * @return converted tap type
     */
    public static TapType fromTapString(String tapType) {
        if (tapType.isBlank()) {
            return null;
        }
        return switch (tapType) {
            case "ON" -> TapType.ON;
            case "OFF" -> TapType.OFF;
            default -> null;
        };
    }
}
