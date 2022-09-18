package pl.dabkowski.edp.database.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import java.sql.Time;

@Entity
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Time time;
    private String line;
    private Time departure;
}
