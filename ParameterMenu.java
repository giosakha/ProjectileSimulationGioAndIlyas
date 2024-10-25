/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectilesimulationfile;

/**
 *
 * @author ilyas
 */
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ParameterMenu extends VBox {
    private int ballNumber;
    private Color ballColor;

    public ParameterMenu(int ballNumber, Color ballColor) {
        this.ballNumber = ballNumber;
        this.ballColor = ballColor;

        setPadding(new Insets(10));
        setSpacing(5);

        Label title = new Label("Ball " + ballNumber + " (" + getBallColor(ballNumber) + ")");
        title.setStyle("-fx-font-weight: bold;");

        TextField initialVelocityTextField = new TextField("50");
        TextField launchAngleTextField = new TextField("45");
        TextField launchHeightTextField = new TextField("0");

        HBox initialVelocityBox = new HBox(5);
        initialVelocityBox.getChildren().addAll(new Label("Initial Velocity:"), initialVelocityTextField, new ComboBox<String>());
        HBox launchAngleBox = new HBox(5);
        launchAngleBox.getChildren().addAll(new Label("Launch Angle:"), launchAngleTextField, new ComboBox<String>());

        HBox launchHeightBox = new HBox(5);
        launchHeightBox.getChildren().addAll(new Label("Launch Height:"), launchHeightTextField, new ComboBox<String>());

        Button playButton = new Button("Play");
        Button stopButton = new Button("Stop");

        getChildren().addAll(title, initialVelocityBox, launchAngleBox, launchHeightBox, playButton, stopButton);
    }

    private String getBallColor(int ballNumber) {
        switch (ballNumber) {
            case 1:
                return "blue";
            case 2:
                return "red";
            case 3:
                return "green";
            default:
                return "";
        }
    }
}