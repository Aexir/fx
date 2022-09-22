package pl.dabkowski.edp.gui;

import javafx.scene.control.Button;
import pl.dabkowski.edp.Main;

public class CustomButton extends Button {

    public CustomButton(String text){
        super(text);
        getStylesheets().add(Main.class.getResource("custombutton.css").toExternalForm());
        getStyleClass().setAll("customButton");

    }
}
