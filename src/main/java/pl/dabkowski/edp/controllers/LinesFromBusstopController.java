package pl.dabkowski.edp.controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pl.dabkowski.edp.Main;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LinesFromBusstopController implements Initializable {

    public static Stage scheduleStage;
    public ScrollPane scrollPane;
    private final List<String> lines;
    private final String streetName;
    private final String stopId;
    private final String stopNr;


    public LinesFromBusstopController(List<String> lines, String streetName, String stopId, String stopNr) {
        this.lines = lines;
        this.streetName = streetName;
        this.stopId = stopId;
        this.stopNr = stopNr;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadItems(lines);
    }

    public void loadItems(List<String> list) {
        System.out.println(list);
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        scrollPane.setContent(vBox);
        if (list.size() == 0) {
            vBox.getChildren().add(new Text("BRAK LINII DLA DANEGO PRZYSTANKU"));
            return;
        }

        int iterator = 0;
        HBox hBox = null;
        while (iterator != list.size()) {
            Button b = new Button(list.get(iterator));
            b.setOnMouseClicked(mouseEvent -> {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("line-from-busstop.fxml"));
                //Parent root = fxmlLoader.load();
                ScheduleForLineFromBusstopController controller = new ScheduleForLineFromBusstopController(b.getText(), streetName, stopId, stopNr);
                fxmlLoader.setController(controller);
                scheduleStage = new Stage();
                scheduleStage.setTitle("Lista odjazdow " + b.getText() + " z " + streetName + " (" + stopNr + ")");
                try {
                    scheduleStage.setScene(new Scene(fxmlLoader.load()));
                    scheduleStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            b.setMinWidth(70);
            if (iterator % 3 == 0) {
                hBox = new HBox();
                hBox.setAlignment(Pos.CENTER);
                hBox.setPadding(new Insets(5, 5, 5, 5));
                vBox.getChildren().add(hBox);
            }
            hBox.getChildren().add(b);
            iterator++;
        }
    }
}
