package pl.dabkowski.edp.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;
import pl.dabkowski.edp.Main;
import pl.dabkowski.edp.api.UmAPI;
import pl.dabkowski.edp.database.SqlManager;
import pl.dabkowski.edp.database.entities.Busstop;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BusstopListForStreetController implements Initializable {
    public ScrollPane scrollPane;
    public VBox vBox;

   public String  streetName;

    public BusstopListForStreetController(String streetName) {
        this.streetName = streetName;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Busstop> busstopsForStreet = SqlManager.getInst().selectQuery(streetName);
        for (Busstop busstop : busstopsForStreet){
            Button button = new Button();
            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER);
            button.setMinHeight(45);
            button.setMinWidth(280);
            button.setContentDisplay(ContentDisplay.CENTER);

            new Thread(()->{
                List<String> busList = UmAPI.getInstance().getLinesForStop(busstop.getStop_id(), busstop.getStop_nr());

                button.setOnMouseClicked(mouseEvent -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("lines-from-busstop.fxml"));
                    //Parent root = fxmlLoader.load();
                    LinesFromBusstopController controller = new LinesFromBusstopController(busList, streetName, busstop.getStop_id(), busstop.getStop_nr());
                    fxmlLoader.setController(controller);
                    Stage infoStage = StreetListController.getInfoStage();
                    infoStage.setTitle("Linie odjeżdżające z " + streetName  + " (" + busstop.getStop_nr() + ")");
                    try {
                        infoStage.setScene(new Scene(fxmlLoader.load()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });


                Platform.runLater(()->{
                    vbox.getChildren().add(new Text(busstop.getStreet() + " (" + busstop.getStop_nr() + ")"));
                    vbox.getChildren().add(new Text(busList.toString()));
                    button.setGraphic(vbox);
                    vBox.getChildren().add(button);
                });





            }).start();


        }


//
//        busstopsForStreet.forEach(x -> {
//            Button button = new Button();
//            VBox vbox = new VBox();
//            vbox.setAlignment(Pos.CENTER);
//            button.setMinHeight(45);
//            button.setMinWidth(280);
//            button.setContentDisplay(ContentDisplay.CENTER);
//            List<String> xd = new ArrayList<>();
//            new Thread(() -> {
//                List<String> tmp = UmAPI.getInstance().getLinesForStop(x.getStop_id(), x.getStop_nr());
//                System.out.println(tmp);
//                xd.addAll(tmp);
//                Platform.runLater(() -> {
//                    vbox.getChildren().add(new Text(x.getStreet() + " (" + x.getStop_nr() + ")"));
//                    vbox.getChildren().add(new Text(tmp.toString()));
//                    button.setGraphic(vbox);
//                });
//            }).start();
//        });
    }
}
