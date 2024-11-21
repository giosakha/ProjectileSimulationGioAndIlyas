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
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class ParameterMenu extends GridPane {
    private int ballNumber;
    private Color ballColor;

    public ParameterMenu(int ballNumber, Color ballColor) {
        this.ballNumber = ballNumber;
        this.ballColor = ballColor;

        setPadding(new Insets(10));
        setVgap(5);
        setHgap(10);

        Label title = new Label("Ball " + ballNumber + " (" + getBallColor(ballNumber) + ")");
        title.setStyle("-fx-font-weight: bold;");
        add(title, 0, 0, 2, 1); 

        TextField initialVelocityTextField = new TextField("");
        TextField launchAngleTextField = new TextField("");
        TextField launchHeightTextField = new TextField("");

        add(new Label("Initial Velocity:"), 0, 1);
        add(initialVelocityTextField, 1, 1);
        add(new ComboBox<String>(), 2, 1);

        add(new Label("Launch Angle:"), 0, 2);
        add(launchAngleTextField, 1, 2);
        add(new ComboBox<String>(), 2, 2);

        add(new Label("Launch Height:"), 0, 3);
        add(launchHeightTextField, 1, 3);
        add(new ComboBox<String>(), 2, 3);
        // Play and Stop buttons
        Button playButton = new Button("Play");
        Button stopButton = new Button("Stop");
        add(playButton, 0, 4);
        add(stopButton, 1, 4);
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