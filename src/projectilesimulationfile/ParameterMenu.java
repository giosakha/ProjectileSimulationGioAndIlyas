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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.function.UnaryOperator;
import javafx.scene.Node;
import javafx.scene.layout.Region;

public class ParameterMenu extends VBox {
    private SimulationController controller;
    private GridPane ballParameterGrid;
    private HBox controlsBox;
    
    private TextField initialVelocityTextField;
    private ComboBox<String> initialVelocityUnitComboBox;
    private TextField launchAngleTextField;
    private ComboBox<String> launchAngleUnitComboBox;
    private TextField launchHeightTextField;
    private ComboBox<String> launchHeightUnitComboBox;
    private TextField weightTextField;
    private ComboBox<String> weightUnitComboBox;
    private TextField airResistanceTextField;
    private ComboBox<String> airResistanceUnitComboBox;
    
    public ParameterMenu(int ballNumber, Color ballColor, SimulationController controller) {
        this.controller = controller; // Initialize the controller reference
        setSpacing(10);
        setPadding(new Insets(10));
        setPrefWidth(400);
        setMaxHeight(Region.USE_PREF_SIZE);

        // Title for the ball parameter menu
        Label title = new Label("Ball " + ballNumber + " (" + getBallColor(ballNumber) + ")");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Separator for visual clarity
        Separator separator = new Separator();

        // GridPane for ball-specific parameters
        ballParameterGrid = new GridPane();
        ballParameterGrid.setHgap(10);
        ballParameterGrid.setVgap(5);

        // Add specific fields for ball parameters
        createBallParameterFields();

        // Add listeners to validate inputs
        addValidationListeners();

        // Add components to the layout
        getChildren().addAll(title, separator, ballParameterGrid);
    }

    private void addValidationListeners() {
        initialVelocityTextField.textProperty().addListener((observable, oldValue, newValue) -> validateMenu());
        launchAngleTextField.textProperty().addListener((observable, oldValue, newValue) -> validateMenu());
        launchAngleUnitComboBox.setOnAction(e -> validateMenu());
        launchHeightTextField.textProperty().addListener((observable, oldValue, newValue) -> validateMenu());
        weightTextField.textProperty().addListener((observable, oldValue, newValue) -> validateMenu());
        airResistanceTextField.textProperty().addListener((observable, oldValue, newValue) -> validateMenu());
    }

    private void validateMenu() {
        // Validate launch angle
        validateLaunchAngle(launchAngleTextField, launchAngleUnitComboBox.getValue());

        // Notify controller to re-validate simulate button
        controller.validateInputs(); // Use the controller reference for validation
    }

    public boolean isValid() {
        // Check if all fields have valid values
        try {
            getInitialVelocity();
            getLaunchAngle();
            getLaunchHeight();
            getWeight();
            getAirResistance();
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    private void createBallParameterFields() {
        // Initial Velocity
        initialVelocityTextField = createNumericTextField();
        initialVelocityUnitComboBox = createComboBox("m/s", "km/h", "ft/s");
        
        addRowToGrid("Initial Velocity:", initialVelocityTextField, initialVelocityUnitComboBox, 0);
        
        // Launch Angle
        launchAngleTextField = createValidatedNumericTextField("degrees"); // Default unit is degrees
        launchAngleUnitComboBox = createComboBox("degrees", "radians");
        launchAngleUnitComboBox.setOnAction(e -> {
            String selectedUnit = launchAngleUnitComboBox.getValue();
            validateLaunchAngle(launchAngleTextField, selectedUnit); // Apply validation
        });
        
        addRowToGrid("Launch Angle:", launchAngleTextField, launchAngleUnitComboBox, 1);
        // Launch Height
        launchHeightTextField = createNumericTextField();
        launchHeightUnitComboBox = createComboBox("m", "ft", "cm");
        
        addRowToGrid("Launch Height:", launchHeightTextField, launchHeightUnitComboBox, 2);
        
        // Weight
        weightTextField = createNumericTextField();
        weightUnitComboBox = createComboBox("kg", "g", "lb");
        
        addRowToGrid("Weight:", weightTextField, weightUnitComboBox, 3);
        
        // Air Resistance
        airResistanceTextField = createNumericTextField();
        airResistanceUnitComboBox = createComboBox("N", "kg/m", "g/cm", "lb/ft");
        
        addRowToGrid("Air Resistance:", airResistanceTextField, airResistanceUnitComboBox, 4);
    }
    
    private void addRowToGrid(String labelText, TextField textField, ComboBox<String> comboBox, int row) {
        ballParameterGrid.add(new Label(labelText), 0, row);
        ballParameterGrid.add(textField, 1, row);
        ballParameterGrid.add(comboBox, 2, row);
    }
    
    private TextField createNumericTextField() {
        TextField textField = new TextField();
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d*\\.?\\d*") ? change : null; // Allow only numbers and a decimal point
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
        return textField;
    }
    
    private TextField createValidatedNumericTextField(String unit) {
        TextField textField = new TextField();
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateLaunchAngle(textField, unit);
        });
        return textField;
    }
    
    private void validateLaunchAngle(TextField textField, String unit) {
        String text = textField.getText();
        try {
            if (!text.isEmpty()) {
                double value = Double.parseDouble(text);
                if ((unit.equals("degrees") && (value < 0 || value > 90)) ||
                        (unit.equals("radians") && (value < 0 || value > Math.PI / 2))) {
                    textField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                    textField.setTooltip(new javafx.scene.control.Tooltip("Angle must be between 0 and 90 degrees or 0 and π/2 radians."));
                } else {
                    textField.setStyle(""); // Reset the style if the value is valid
                    textField.setTooltip(null); // Remove the tooltip
                }
            }
        } catch (NumberFormatException e) {
            textField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            textField.setTooltip(new javafx.scene.control.Tooltip("Please enter a valid numeric value."));
        }
    }
    
    private ComboBox<String> createComboBox(String... items) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(items);
        comboBox.setValue(items[0]);
        return comboBox;
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
        // Update text colors for all labels and variable-specific text
        for (Node node : getChildren()) {
            if (node instanceof Label) {
                ((Label) node).setTextFill(textColor);
            }
        }
        
        for (Node node : ballParameterGrid.getChildren()) {
            if (node instanceof Label) {
                ((Label) node).setTextFill(textColor);
            }
        }
    }
    
    // Helper method to convert Color to Hex
    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
    
    public void resetFields() {
        initialVelocityTextField.clear();
        launchAngleTextField.clear();
        launchHeightTextField.clear();
        weightTextField.clear();
        airResistanceTextField.clear();
        
        initialVelocityUnitComboBox.setValue("m/s");
        launchAngleUnitComboBox.setValue("degrees");
        launchHeightUnitComboBox.setValue("m");
        weightUnitComboBox.setValue("kg");
        airResistanceUnitComboBox.setValue("N");
    }
    
    public double getInitialVelocity() {
        String text = initialVelocityTextField.getText();
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Initial velocity is not set.");
        }
        return Double.parseDouble(text);
    }
    
    public String getInitialVelocityUnit() {
        return initialVelocityUnitComboBox.getValue();
    }
    
    public double getLaunchAngle() {
        String text = launchAngleTextField.getText();
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Launch angle is not set.");
        }
        return Double.parseDouble(text);
    }
    
    public String getLaunchAngleUnit() {
        return launchAngleUnitComboBox.getValue();
    }
    
    public double getLaunchHeight() {
        String text = launchHeightTextField.getText();
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Launch height is not set.");
        }
        return Double.parseDouble(text);
    }
    
    public String getLaunchHeightUnit() {
        return launchHeightUnitComboBox.getValue();
    }
    
    public double getWeight() {
        String text = weightTextField.getText();
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Weight is not set.");
        }
        return Double.parseDouble(text);
    }
    
    public String getWeightUnit() {
        return weightUnitComboBox.getValue();
    }
    
    public double getAirResistance() {
        String text = airResistanceTextField.getText();
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Air resistance is not set.");
        }
        return Double.parseDouble(text);
    }
    
    public String getAirResistanceUnit() {
        return airResistanceUnitComboBox.getValue();
    }
}
