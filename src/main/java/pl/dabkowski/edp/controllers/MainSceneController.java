package pl.dabkowski.edp.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import pl.dabkowski.edp.Main;
import pl.dabkowski.edp.api.UmAPI;
import pl.dabkowski.edp.database.SqlManager;
import pl.dabkowski.edp.database.entities.Busstop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainSceneController {
    public ImageView notificationIcon;
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
        SqlManager.getInst().scheduleNotifications();
        loadBusstops();
        loadLines();
        loadMap();
    }

    public void waitForAllFinished() {
        new Thread(() -> {
            while (true) {
                if (!(mapButton.isDisabled() || busstopScheduleButton.isDisabled() || lineScheduleButton.isDisabled())) {
                    Platform.runLater(() -> {
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
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                lineScheduleButton.setDisable(false);
            });
        }).start();
    }

    public void loadMap() {
        new Thread(() -> {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                mapButton.setDisable(false);
            });
        }).start();
    }

    @FXML
    public void onIconClick(MouseEvent mouseEvent) {
        long s1 = System.currentTimeMillis();
        List<String> stop = new ArrayList<>();
        List<String> test = new ArrayList<>();
        List<Busstop> busstops = UmAPI.getInstance().getAllStops();
        String line = "500";
        new Thread(() -> {
            for (int i = 0; i < busstops.size(); i++) {
                int finalI = i;
                Thread t = new Thread(() -> {
                    String id = busstops.get(finalI).getStop_id();
                    String nr = busstops.get(finalI).getStop_nr();
                    long start = System.currentTimeMillis();
                    List<String> lines = UmAPI.getInstance().getLinesForStop(id, nr);
                    for (String l2 : lines) {
                        if (l2.contains(line)) {
                            stop.add(id + ";" + nr);
                        }
                    }
                    test.add("NIGGER");
                    long time = (System.currentTimeMillis() - start);
                    System.out.println(finalI + ": " + time + "ms.");

                });
                t.start();
                if (i != 0) {
                    try {
                        if (i % 10 == 0) {
                            Thread.sleep(330);
                        }
                        if (i % 100 == 0) {
                            Thread.sleep(50);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
/*                if (i%500 == 0){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }*/

//                if (i%1000 == 0){
//                    try {
//                        Thread.sleep(3000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
                if (i == busstops.size() - 1) {
                    long s2 = System.currentTimeMillis();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("TEST" + test.size());
                    System.out.println((s2 - s1) + " KONIEC ");
                    try {
                        Thread.sleep(5000);
                        for (String s : stop) {
                            String[] xd = s.split(";");
                            System.out.println(UmAPI.getInstance().getNameFromId(xd[0], xd[1]));
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();
    }
}
