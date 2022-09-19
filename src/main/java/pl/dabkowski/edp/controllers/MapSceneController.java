package pl.dabkowski.edp.controllers;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import pl.dabkowski.edp.Main;
import pl.dabkowski.edp.database.entities.Busstop;

import java.net.URL;
import java.util.ResourceBundle;

public class MapSceneController implements Initializable {

    public static WebEngine webEngine;

    @FXML
    private WebView mapView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
            long start = System.currentTimeMillis();
            Platform.runLater(() -> {
                webEngine = mapView.getEngine();
                mapView.setZoom(1);
                final URL urlGoogleMaps = Main.class.getResource("mapa.html");
                webEngine.load(urlGoogleMaps.toExternalForm());
                System.out.println((System.currentTimeMillis()) - start);
                webEngine.getLoadWorker().stateProperty().addListener(((obs, oldState, newState) -> {
                    if (newState == Worker.State.SUCCEEDED) {
                        for (int i = 0; i < Busstop.getBusstops().size(); i++) {
                            webEngine.executeScript("test(long, lat)".replace("long",
                                            String.valueOf(Busstop.getBusstops().get(i).getLocation().getLongitude()))
                                    .replace("lat", String.valueOf(Busstop.getBusstops().get(i).getLocation().getLatitude())));
                        }
                    }
                }));
            });
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void onBackButtonClick(MouseEvent mouseEvent) {
        Main.getMainStage().setScene(Main.getMainScene());
    }
}
