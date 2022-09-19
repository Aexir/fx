package pl.dabkowski.edp.database.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class ZtmRide {

    private static ArrayList<ZtmRide> departures = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String line;
    private String symbol_1;
    private String symbol_2;
    private String brigade;
    private String direction;
    private String path;
    private Time time;

    public static void addDepartureToList(ZtmRide ztmRide) {
        departures.add(ztmRide);
    }

    public static List<ZtmRide> getDepartures() {
        return new ArrayList<>(departures);
    }

}
