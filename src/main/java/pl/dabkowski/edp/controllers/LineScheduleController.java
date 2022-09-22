package pl.dabkowski.edp.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.dabkowski.edp.Main;
import pl.dabkowski.edp.api.UmAPI;
import pl.dabkowski.edp.database.SqlManager;
import pl.dabkowski.edp.gui.CustomButton;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LineScheduleController implements Initializable {

    @FXML
    public TextField searchInput;
    private List<String> allLines;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane anchorPane;

    public void onBackButtonClick(MouseEvent mouseEvent) throws IOException {
        Main.getMainStage().setScene(Main.getMainScene());
    }

    public void onSearchButtonClick(MouseEvent mouseEvent) throws IOException {
        String searching = searchInput.getText();
        List<String> tmpList = new ArrayList<>();

        if (!searching.isEmpty()) {
            scrollPane.setContent(null);
            for (String s : allLines) {
                if (s.contains(searching)) {
                    tmpList.add(s);
                }
            }
            loadItems(tmpList);
        } else {
            scrollPane.setContent(null);
            loadItems(allLines);
        }
    }

    private void loadFxml(Parent fxmlLoader) {
        Scene scene = new Scene(fxmlLoader);
        //scene.getStylesheets().add(String.valueOf(Main.class.getResource("custom-button.css")));
        Main.getMainStage().setScene(scene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        new Thread(() -> {
            Platform.runLater(() -> {
                List<String> buses = UmAPI.getInstance().getAllBusLines();
                List<String> trams = UmAPI.getInstance().getAllTramLines();
                List<String> all = new ArrayList<>(buses);
                all.addAll(trams);
                loadItems(all);
            });
        }).start();
    }

    public void loadItems(List<String> list) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        scrollPane.setContent(vBox);
        int xd = 0;
        for (int i = 0; i < list.size() / 5; i++) {
            HBox hBox = new HBox();
            hBox.setSpacing(3);
            hBox.setAlignment(Pos.CENTER);
            hBox.setPadding(new Insets(3, 0, 3, 0));
            vBox.getChildren().add(hBox);
            for (int j = 0; j < 5; j++) {
                CustomButton b = new CustomButton(list.get(xd));
                b.setMinWidth(70);
                hBox.getChildren().add(b);
                b.setOnMouseClicked(event -> {
                    String line = b.getText();
                    FXMLLoader fxmlLoader = null;
                    fxmlLoader = new FXMLLoader(Main.class.getResource("line-direction.fxml"));

                    Scene scene = null;

                    try {
                        scene = new Scene(fxmlLoader.load());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //scene.getStylesheets().add(String.valueOf(Main.class.getResource("custom-button.css")));
/*                    LineDirectionController controller = fxmlLoader.getController();
                    controller.getLineNumber().setText(b.getText());
                    controller.getFirstDirectionButton().setText("k");
                    controller.getSecondDirectionButton().setText("DD");*/

                    Main.getMainStage().setScene(scene);

                });
                xd++;
            }
        }
    }
}
