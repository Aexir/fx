package pl.dabkowski.edp.controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import pl.dabkowski.edp.Main;
import pl.dabkowski.edp.api.UmAPI;
import pl.dabkowski.edp.database.entities.Busstop;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

public class StreetListController implements Initializable {

    @Getter
    private static Stage infoStage;
    private final Set<String> busstopStreetNames = new TreeSet<>();
    public Button backButton;
    public TextField searchInput;
    public Button searchButton;
    public ListView listView;
    public AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
            for (Busstop busstop : UmAPI.getInstance().getAllStopsFromDatabase()) {
                busstopStreetNames.add(busstop.getStreet());
            }
            Platform.runLater(() -> listView.getItems().addAll(busstopStreetNames));
        }).start();
    }


    public void onBackButtonClick(MouseEvent mouseEvent) {
        Main.getMainStage().setScene(Main.getMainScene());
    }


    public void onSearchButtonClick(MouseEvent mouseEvent) {
        String searching = searchInput.getText();
        if (!searching.isEmpty()) {
            listView.getItems().clear();
            for (String s : busstopStreetNames) {
                if (s.toUpperCase().contains(searching.toUpperCase())) {
                    listView.getItems().add(s);
                }
            }
        } else {
            listView.getItems().clear();
            listView.getItems().addAll(busstopStreetNames);
        }
    }

    public void listViewOnMouseClickedEvent(MouseEvent mouseEvent) throws IOException {
        String streetName = listView.getSelectionModel().getSelectedItem().toString();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("busstop-list-for-street.fxml"));
        //Parent root = fxmlLoader.load();
        BusstopListForStreetController controller = new BusstopListForStreetController(streetName);
        fxmlLoader.setController(controller);
        infoStage = new Stage();
        infoStage.setTitle("Rozkład jazdy dla " + streetName);
        infoStage.setScene(new Scene(fxmlLoader.load()));
        infoStage.show();


    }
}
