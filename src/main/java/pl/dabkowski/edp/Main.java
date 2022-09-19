package pl.dabkowski.edp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.greenrobot.eventbus.EventBus;
import pl.dabkowski.edp.controllers.MainSceneController;
import pl.dabkowski.edp.listeners.NotificationListener;

import java.io.IOException;

public class Main extends Application {

    @Getter
    private static Stage mainStage;
    @Getter
    private static Scene mainScene;

    public static void main(String[] args) {
        EventBus eventBus = EventBus.getDefault();
        eventBus.register(new NotificationListener());
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        mainStage = stage;
        mainStage.setTitle("Warszawskie MPK");
        mainScene = new Scene(fxmlLoader.load(), 600, 400);
        MainSceneController controller = fxmlLoader.getController();
        controller.getBusstopScheduleButton().setDisable(true);
        controller.getLineScheduleButton().setDisable(true);
        controller.getMapButton().setDisable(true);
        controller.getRefreshButton().setDisable(true);
        controller.loadAll();

        mainStage.setScene(mainScene);
        stage.setResizable(false);
        mainStage.show();


        mainStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

    }
}