package pl.dabkowski.edp.controllers;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import org.greenrobot.eventbus.EventBus;
import pl.dabkowski.edp.api.UmAPI;
import pl.dabkowski.edp.database.SqlManager;
import pl.dabkowski.edp.database.entities.Notification;
import pl.dabkowski.edp.database.entities.ZtmRide;

import java.net.URL;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduleForLineFromBusstopController implements Initializable {

    public ListView listView;
    private final String line;
    private final String streetName;
    private final String stopId;
    private final String stopNr;

    public ScheduleForLineFromBusstopController(String line, String streetName, String stopId, String stopNr) {
        this.line = line;
        this.streetName = streetName;
        this.stopId = stopId;
        this.stopNr = stopNr;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
            UmAPI.getInstance().loadLineScheduleForBusstop(stopId, stopNr, line);
            Platform.runLater(() -> {
                for (ZtmRide ztmRide : ZtmRide.getDepartures()) {
                    HBox hBox = new HBox();

                    hBox.setAlignment(Pos.CENTER);
                    Button button = new Button(ztmRide.getTime().toString());
                    Button notify5 = createButton("5", ztmRide);
                    Button notify15 = createButton("15", ztmRide);
                    Button notify20 = createButton("20", ztmRide);
                    Button notify30 = createButton("30", ztmRide);
                    hBox.getChildren().add(button);
                    //SPACER
                    final Pane spacer = new Pane();
                    HBox.setHgrow(spacer, Priority.ALWAYS);
                    spacer.setMinSize(10, 1);
                    hBox.getChildren().add(spacer);
                    if (ztmRide.getTime().toLocalTime().isAfter(LocalTime.now())) {
                        if (ztmRide.getTime().toLocalTime().isAfter(LocalTime.now().plus(5, ChronoUnit.MINUTES))) {
                            hBox.getChildren().add(notify5);
                        }
                        if (ztmRide.getTime().toLocalTime().isAfter(LocalTime.now().plus(15, ChronoUnit.MINUTES))) {
                            hBox.getChildren().add(notify15);
                        }
                        if (ztmRide.getTime().toLocalTime().isAfter(LocalTime.now().plus(20, ChronoUnit.MINUTES))) {
                            hBox.getChildren().add(notify20);
                        }
                        if (ztmRide.getTime().toLocalTime().isAfter(LocalTime.now().plus(30, ChronoUnit.MINUTES))) {
                            hBox.getChildren().add(notify30);
                        }
                    }
                    listView.getItems().addAll(hBox);
                }
            });
        }).start();
    }

    public Button createButton(String time, ZtmRide ztmRide) {
        Button button = new Button("+" + time);
        button.setOnMouseClicked(mouseEvent -> {
            Notification notification = new Notification();
            notification.setLine(line);
            notification.setDeparture(ztmRide.getTime());
            notification.setTime(Time.valueOf(LocalTime.now().plus(Long.parseLong(time), ChronoUnit.MINUTES)));
            TimerTask task = new TimerTask() {
                public void run() {
                    System.out.println("Task performed on: " + notification.getTime() + "n" +
                            "Thread's name: " + Thread.currentThread().getName());
                    EventBus.getDefault().post(notification);
                    SqlManager.getInst().removeObject(notification);
                }
            };
            Timer timer = new Timer("Notification");
            long timed = Integer.parseInt(time)* 1000L;
            timer.schedule(task,timed);
            SqlManager.getInst().saveObject(notification);
        });
        return button;
    }
}
