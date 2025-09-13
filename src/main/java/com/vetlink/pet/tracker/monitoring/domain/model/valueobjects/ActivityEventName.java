package com.vetlink.pet.tracker.monitoring.domain.model.valueobjects;

public enum ActivityEventName {
    HIGH_HEART_RATE,
    LOW_HEART_RATE,
    HIGH_SPO2,
    LOW_SPO2,
    GEOFENCE_EXIT;

    @Override
    public String toString() {
        String[] words = name().replace("_", " ").toLowerCase().split(" ");
        StringBuilder formattedName = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                formattedName.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }
        return formattedName.toString().trim();
    }

}