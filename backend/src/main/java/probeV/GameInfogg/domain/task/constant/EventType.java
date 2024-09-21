package probeV.GameInfogg.domain.task.constant;

public enum EventType {
    NORMAL, TIME, SPECIAL;

    public static EventType fromString(String event) {
        try{
            return EventType.valueOf(event.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid event: " + event);
        }
    }
}
