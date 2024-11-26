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
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.function.UnaryOperator;
import javafx.scene.Node;

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

        TextField initialVelocityTextField = createNumericTextField();
        TextField launchAngleTextField = createNumericTextField();
        TextField launchHeightTextField = createNumericTextField();
        TextField ballWeightTextField = createNumericTextField();
        TextField airResistanceTextField = createNumericTextField();

        add(new Label("Initial Velocity:"), 0, 2);
        add(initialVelocityTextField, 1, 2);
        ComboBox<String> initialVelocityUnitComboBox = new ComboBox<>();
        initialVelocityUnitComboBox.getItems().addAll("m/s", "km/h", "ft/s");
        initialVelocityUnitComboBox.setValue("m/s");
        add(initialVelocityUnitComboBox, 2, 2);

        add(new Label("Launch Angle:"), 0, 3);
        add(launchAngleTextField, 1, 3);
        ComboBox<String> launchAngleUnitComboBox = new ComboBox<>();
        launchAngleUnitComboBox.getItems().addAll("degrees", "radians");
        launchAngleUnitComboBox.setValue("degrees");
        add(launchAngleUnitComboBox, 2, 3);

        add(new Label("Launch Height:"), 0, 4);
        add(launchHeightTextField, 1, 4);
        ComboBox<String> launchHeightUnitComboBox = new ComboBox<>();
        launchHeightUnitComboBox.getItems().addAll("m", "ft", "cm");
        launchHeightUnitComboBox.setValue("m");
        add(launchHeightUnitComboBox, 2, 4);

        add(new Label("Weight:"), 0, 5);
        add(ballWeightTextField, 1, 5);
        ComboBox<String> ballWeightUnitComboBox = new ComboBox<>();
        ballWeightUnitComboBox.getItems().addAll("kg", "g", "lb");
        ballWeightUnitComboBox.setValue("kg");
        add(ballWeightUnitComboBox, 2, 5);

        add(new Label("Air Resistance:"), 0, 6);
        add(airResistanceTextField, 1, 6);
        ComboBox<String> airResistanceUnitComboBox = new ComboBox<>();
        airResistanceUnitComboBox.getItems().addAll("N","kg/m", "g/cm", "lb/ft");
        airResistanceUnitComboBox.setValue("N");
        add(airResistanceUnitComboBox, 2, 6);

        Button playButton = new Button("Play");
        Button stopButton = new Button("Stop");
        add(playButton, 0, 7);
        add(stopButton, 1, 7);
    }

    private TextField createNumericTextField() {
        TextField textField = new TextField();
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
        return textField;
    }

    private String getBallColor(int ballNumber) {
        switch (ballNumber) {
            case 1:
                return "Blue";
            case 2:
                return "Red";
            case 3:
                return "Green";
            default:
                return "";
        }
    }

    public void updateLabelColors(Color textColor) {
        for (Node node : getChildren()) {
            if (node instanceof Label) {
                ((Label) node).setTextFill(textColor);
            }
        }
    }
}