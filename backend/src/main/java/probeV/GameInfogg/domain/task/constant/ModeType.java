package probeV.GameInfogg.domain.task.constant;

public enum ModeType {
    PVP, PVE;

    public static ModeType fromString(String mode) {
        try{
            return ModeType.valueOf(mode.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid mode: " + mode);
        }
    }
}
