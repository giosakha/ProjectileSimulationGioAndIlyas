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
        setVgap(2);
        setHgap(10);

        // Text fields for parameters
        TextField initialVelocityTextField = new TextField("");
        TextField launchAngleTextField = new TextField("");
        TextField launchHeightTextField = new TextField("");
        TextField ballWeightTextField = new TextField(""); // New text field for ball weight
        TextField airResistanceTextField = new TextField(""); // New text field for air resistance

        add(new Label("Initial Velocity:"), 0, 2);
        add(initialVelocityTextField, 1, 2);
        add(new ComboBox<String>(), 2, 2);

        add(new Label("Launch Angle:"), 0, 3);
        add(launchAngleTextField, 1, 3);
        add(new ComboBox<String>(), 2, 3);

        add(new Label("Launch Height:"), 0, 4);
        add(launchHeightTextField, 1, 4);
        add(new ComboBox<String>(), 2, 4);

        // Adding fields for ball weight and air resistance
        add(new Label("Weight:"), 0, 5);
        add(ballWeightTextField, 1, 5);
        add(new ComboBox<String>(), 2, 5); // ComboBox for ball weight (if needed)

        add(new Label("Air Resistance (kg/m):"), 0, 6);
        add(airResistanceTextField, 1, 6);
        add(new ComboBox<String>(), 2, 6); // ComboBox for air resistance (if needed)

        // Play and Stop buttons
        Button playButton = new Button("Play");
        Button stopButton = new Button("Stop");
        add(playButton, 0, 7);
        add(stopButton, 1, 7);
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