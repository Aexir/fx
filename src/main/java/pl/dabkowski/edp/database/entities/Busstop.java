package pl.dabkowski.edp.database.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@ToString
public class Busstop {

    private static ArrayList<Busstop> busstops = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String street;
    private String street_id;
    private String stop_id;
    private String stop_nr;
    private String direction;
    @OneToOne(cascade = CascadeType.ALL)
    private Location location;

    public static void addBusstopToList(Busstop busstop) {
        busstops.add(busstop);
    }

    public static List<Busstop> getBusstops() {
        return new ArrayList<>(busstops);
    }
}
