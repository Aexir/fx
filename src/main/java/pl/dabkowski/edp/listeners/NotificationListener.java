package pl.dabkowski.edp.listeners;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.greenrobot.eventbus.Subscribe;
import pl.dabkowski.edp.database.entities.Notification;

public class NotificationListener {


    public static void display() {
        Stage popupwindow = new Stage();
        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("This is a pop up window");
        Label label1 = new Label("Pop up window now displayed");
        Button button1 = new Button("Close this pop up window");
        button1.setOnAction(e -> popupwindow.close());
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label1, button1);
        layout.setAlignment(Pos.CENTER);
        Scene scene1 = new Scene(layout, 300, 250);
        popupwindow.setScene(scene1);
        popupwindow.showAndWait();
    }

    @Subscribe
    public void receivedEvent(Notification notification) {
        Platform.runLater(()->{
            display();
        });
    }

}
