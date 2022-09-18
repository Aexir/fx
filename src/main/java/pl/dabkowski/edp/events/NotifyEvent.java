package pl.dabkowski.edp.events;

import java.sql.Time;

public class NotifyEvent {
    public final String message;
    private final Time time;
    private final String line;
    private final Time departure;

    public NotifyEvent(String message, Time time, String line, Time departure){
        this.message = message;
        this.time = time;
        this.line = line;
        this.departure = departure;
    }
}
