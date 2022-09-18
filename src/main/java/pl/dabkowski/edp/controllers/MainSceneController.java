package pl.dabkowski.edp.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import pl.dabkowski.edp.Main;
import pl.dabkowski.edp.api.UmAPI;

import java.io.IOException;

public class MainSceneController {
    @FXML
    @Getter
    private Button mapButton;

    @FXML
    @Getter
    private Button lineScheduleButton;

    @FXML
    @Getter
    private Button refreshButton;

    @FXML
    @Getter
    private Button busstopScheduleButton;

    //Line-schedule -> street list
    @FXML
    public void onMouseClick(MouseEvent mouseEvent) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(Main.class.getResource("Line-schedule.fxml"));
        loadFxml(fxmlLoader);
    }

    @FXML
    private void onMapButtonClick(MouseEvent event) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(Main.class.getResource("map-view.fxml"));
        loadFxml(fxmlLoader);
    }


    @FXML
    private void onBusstopScheduleClick(MouseEvent mouseEvent) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(Main.class.getResource("street-list.fxml"));
        loadFxml(fxmlLoader);
    }

    private void loadFxml(Parent fxmlLoader) {
        Scene scene = new Scene(fxmlLoader);
        Main.getMainStage().setScene(scene);
    }

    @FXML
    public void onRefreshButtonClick(MouseEvent mouseEvent) {
        refreshButton.setDisable(true);
        busstopScheduleButton.setDisable(true);
        lineScheduleButton.setDisable(true);
        mapButton.setDisable(true);
        loadAll();

    }

    public void loadAll() {
        waitForAllFinished();
        loadBusstops();
        loadLines();
        loadMap();

    }

    public void waitForAllFinished() {
        new Thread(() -> {
            while (true) {
                if (!(mapButton.isDisabled() || busstopScheduleButton.isDisabled() || lineScheduleButton.isDisabled())) {
                    Platform.runLater(()-> {
                        refreshButton.setDisable(false);
                    });
                    break;
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    public void loadBusstops() {
        new Thread(() -> {
            UmAPI.getInstance().loadAllBusstops();
            Platform.runLater(() -> {
                getBusstopScheduleButton().setDisable(false);
            });
        }).start();
    }

    public void loadLines() {
        new Thread(()->{
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(()->{
                lineScheduleButton.setDisable(false);
            });
        }).start();
    }

    public void loadMap() {
        new Thread(()->{
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(()->{
                mapButton.setDisable(false);
            });
        }).start();
    }
}
