package pl.dabkowski.edp.controllers;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import pl.dabkowski.edp.api.UmAPI;
import pl.dabkowski.edp.database.entities.ZtmRide;

import java.net.URL;
import java.sql.RowId;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ScheduleForLineFromBusstopController implements Initializable {

    public ListView listView;
    private String line;
    private String streetName;
    private String stopId;
    private String stopNr;

    public ScheduleForLineFromBusstopController(String line, String streetName, String stopId, String stopNr){
        this.line = line;
        this.streetName = streetName;
        this.stopId = stopId;
        this.stopNr = stopNr;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(()->{
            UmAPI.getInstance().loadLineScheduleForBusstop(stopId,stopNr, line);
            Platform.runLater(()->{
                for (ZtmRide ztmRide : ZtmRide.getDepartures()){
                    HBox hBox = new HBox();

                    hBox.setAlignment(Pos.CENTER);
                    Button button = new Button(ztmRide.getTime().toString());
                    Button notify = new Button("OUT");
                    Button notify5 = createButton("5");
                    Button notify15 = createButton("15");
                    Button notify20 = createButton("20");
                    Button notify30 = createButton("30");
                    hBox.getChildren().add(button);
                    //SPACER
                    final Pane spacer = new Pane();
                    HBox.setHgrow(spacer, Priority.ALWAYS);
                    spacer.setMinSize(10, 1);
                    hBox.getChildren().add(spacer);
                    if (ztmRide.getTime().toLocalTime().isAfter(LocalTime.now())){
                        hBox.getChildren().add(notify);
                        if (ztmRide.getTime().toLocalTime().isAfter(LocalTime.now().plus(5, ChronoUnit.MINUTES))){
                            hBox.getChildren().add(notify5);
                        }
                        if (ztmRide.getTime().toLocalTime().isAfter(LocalTime.now().plus(15, ChronoUnit.MINUTES))){
                            hBox.getChildren().add(notify15);
                        }
                        if (ztmRide.getTime().toLocalTime().isAfter(LocalTime.now().plus(20, ChronoUnit.MINUTES))){
                            hBox.getChildren().add(notify20);
                        }
                        if (ztmRide.getTime().toLocalTime().isAfter(LocalTime.now().plus(30, ChronoUnit.MINUTES))){
                            hBox.getChildren().add(notify30);
                        }
                    }
                    listView.getItems().addAll(hBox);
                }
            });
        }).start();
    }

    public Button createButton(String time){
        Button button = new Button("+" + time);
        button.setOnMouseClicked(mouseEvent->{

        });
        return button;
    }
}
