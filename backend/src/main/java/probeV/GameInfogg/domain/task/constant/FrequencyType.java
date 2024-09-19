package probeV.GameInfogg.domain.task.constant;

public enum FrequencyType {
    WEEKLY, DAILY;

    public static FrequencyType fromString(String frequency) {
        try{
            return FrequencyType.valueOf(frequency.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid frequency: " + frequency);
        }
    }
}
