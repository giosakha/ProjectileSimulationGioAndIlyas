/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package projectilesimulationfile;

/**
 *
 * @author ilyas
 */
import java.util.function.UnaryOperator;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;

public class SimulationController {

    private ParameterMenu[] parameterMenus;
    private Background background;
    private int numberOfProjectiles;
    private Color[] projectileColors = {Color.BLUE, Color.RED, Color.GREEN};
    private GraphicsContext gc;
    private Canvas canvas;
    private VBox controlPanel;

    // Shared controls
    private Label titleLabel;
    private TextField gravityTextField;
    private ComboBox<String> gravityUnitComboBox;
    private ComboBox<Integer> numberOfProjectilesComboBox;
    private Button simulateButton;
    private Button resetButton;

    public SimulationController(BorderPane mainPane) {
        this.numberOfProjectiles = 1;
        this.parameterMenus = new ParameterMenu[3];
        createMenuBar(mainPane);
        createCanvas(mainPane);
        createControlPanel(mainPane);
        updateParameterMenus();

        // Apply default light theme
        setTheme(Color.BLACK, Color.WHITE);
    }

    public void setNumberOfProjectiles(int num) {
        this.numberOfProjectiles = num;
        updateParameterMenus();
    }

    private void createControlPanel(BorderPane mainPane) {
        controlPanel = new VBox(10);
        controlPanel.setPadding(new Insets(10));
        mainPane.setRight(controlPanel);

        // Add shared controls
        createSharedControls();

        for (int i = 0; i < parameterMenus.length; i++) {
            parameterMenus[i] = new ParameterMenu(i + 1, projectileColors[i], this); // Pass `this`
            parameterMenus[i].setVisible(false); // Initially hidden
            controlPanel.getChildren().add(parameterMenus[i]);
        }

        updateParameterMenus();
    }

    private void createSharedControls() {
        VBox sharedControls = new VBox(10);

        // Title
        titleLabel = new Label("Parameter Menu:");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        sharedControls.getChildren().add(titleLabel);

        // Gravity
        Label gravityLabel = new Label("Gravity:");
        gravityTextField = createNumericTextField(); // Use a method to restrict input to double
        gravityTextField.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());

        gravityUnitComboBox = new ComboBox<>();
        gravityUnitComboBox.getItems().addAll("m/s²", "ft/s²", "g");
        gravityUnitComboBox.setValue("m/s²");

        resetButton = new Button("Reset");
        resetButton.setOnAction(e -> resetSimulation());

        HBox gravityBox = new HBox(10);
        gravityBox.getChildren().addAll(gravityLabel, gravityTextField, gravityUnitComboBox, resetButton);
        gravityBox.setStyle("-fx-alignment: center-left;");

        // Number of Projectiles
        Label numberOfProjectilesLabel = new Label("Number of Projectiles:");
        numberOfProjectilesComboBox = new ComboBox<>();
        numberOfProjectilesComboBox.getItems().addAll(1, 2, 3);
        numberOfProjectilesComboBox.setValue(1);
        numberOfProjectilesComboBox.setOnAction(e -> {
            setNumberOfProjectiles(numberOfProjectilesComboBox.getValue());
            validateInputs();
        });

        simulateButton = new Button("Simulate");
        simulateButton.setOnAction(e -> simulateAllProjectiles());
        simulateButton.setDisable(true); // Initially disabled

        HBox projectilesBox = new HBox(10);
        projectilesBox.getChildren().addAll(numberOfProjectilesLabel, numberOfProjectilesComboBox, simulateButton);
        projectilesBox.setStyle("-fx-alignment: center-left;");

        // Add shared controls to the control panel
        sharedControls.getChildren().addAll(gravityBox, projectilesBox);
        controlPanel.getChildren().add(sharedControls);
    }

    private TextField createNumericTextField() {
        TextField textField = new TextField();
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d*\\.?\\d*") ? change : null; // Allow only numbers and a single decimal point
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
        return textField;
    }

    public void validateInputs() {
        // Disable simulate button if any required input is invalid
        boolean isValid = true;

        // Check gravity field
        if (gravityTextField.getText().isEmpty() || !isValidDouble(gravityTextField.getText())) {
            isValid = false;
        }

        // Check parameter menus
        for (int i = 0; i < numberOfProjectiles; i++) {
            ParameterMenu menu = parameterMenus[i];
            if (!menu.isValid()) {
                isValid = false;
                break;
            }
        }

        simulateButton.setDisable(!isValid); // Enable or disable simulate button based on validity
    }

    private boolean isValidDouble(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }



    private void updateParameterMenus() {
        // Update visibility of parameter menus based on the selected number of projectiles
        for (int i = 0; i < parameterMenus.length; i++) {
            parameterMenus[i].setVisible(i < numberOfProjectiles);
        }
    }
    
    private void updateBackground(Runnable backgroundSetter) {
        backgroundSetter.run();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        background.drawBackground(gc, canvas.getWidth(), canvas.getHeight());
    }



    private void createMenuBar(BorderPane mainPane) {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu optionsMenu = new Menu("Options");
        Menu themeMenu = new Menu("Theme");

        // File menu items
        MenuItem newFile = new MenuItem("New");
        MenuItem openFile = new MenuItem("Open");
        MenuItem saveFile = new MenuItem("Save");
        fileMenu.getItems().addAll(newFile, openFile, saveFile);

        // Edit menu items
        MenuItem undo = new MenuItem("Undo");
        MenuItem redo = new MenuItem("Redo");
        editMenu.getItems().addAll(undo, redo);

        // Theme menu items
        MenuItem darkTheme = new MenuItem("Dark");
        MenuItem lightTheme = new MenuItem("Light");

        darkTheme.setOnAction(e -> setTheme(Color.WHITE, Color.BLACK));
        lightTheme.setOnAction(e -> setTheme(Color.BLACK, Color.WHITE));

        // Background options under Theme
        Menu backgroundMenu = new Menu("Background");
        MenuItem skyBackground = new MenuItem("Sky");
        MenuItem mountainBackground = new MenuItem("Mountain");
        MenuItem cityBackground = new MenuItem("City");
        MenuItem oceanBackground = new MenuItem("Ocean");
        MenuItem spaceBackground = new MenuItem("Space");
        MenuItem desertBackground = new MenuItem("Desert");
        MenuItem jungleBackground = new MenuItem("Jungle");

        skyBackground.setOnAction(e -> updateBackground(() -> background.setSkyBackground()));
        mountainBackground.setOnAction(e -> updateBackground(() -> background.setMountainBackground()));
        cityBackground.setOnAction(e -> updateBackground(() -> background.setCityBackground()));
        oceanBackground.setOnAction(e -> updateBackground(() -> background.setOceanBackground()));
        spaceBackground.setOnAction(e -> updateBackground(() -> background.setSpaceBackground()));
        desertBackground.setOnAction(e -> updateBackground(() -> background.setDesertBackground()));
        jungleBackground.setOnAction(e -> updateBackground(() -> background.setJungleBackground()));

        backgroundMenu.getItems().addAll(skyBackground, mountainBackground, cityBackground, oceanBackground,
                spaceBackground, desertBackground, jungleBackground);

        themeMenu.getItems().addAll(darkTheme, lightTheme, new SeparatorMenuItem(), backgroundMenu);
        menuBar.getMenus().addAll(fileMenu, editMenu, optionsMenu, themeMenu);

        mainPane.setTop(menuBar);
    }

    
    private void setTheme(Color textColor, Color backgroundColor) {
        controlPanel.setStyle("-fx-background-color: " + toHex(backgroundColor) + ";");

        // Update text color for shared controls
        titleLabel.setTextFill(textColor);

        for (Node node : controlPanel.getChildren()) {
            if (node instanceof VBox) {
                for (Node vBoxNode : ((VBox) node).getChildren()) {
                    if (vBoxNode instanceof HBox) {
                        for (Node hBoxNode : ((HBox) vBoxNode).getChildren()) {
                            if (hBoxNode instanceof Label) {
                                ((Label) hBoxNode).setTextFill(textColor); // Change label colors
                            }
                        }
                    }
                }
            }
        }

        // Update ParameterMenu variables' text color
        for (ParameterMenu menu : parameterMenus) {
            if (menu != null) {
                menu.updateLabelColors(textColor); // Ensure parameter menu text color is updated
            }
        }

        // Redraw the background to ensure it reflects the new theme
        background.drawBackground(gc, canvas.getWidth(), canvas.getHeight());
    }           


    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private void createCanvas(BorderPane mainPane) {
        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();
        VBox.setVgrow(canvas, Priority.ALWAYS);
        mainPane.setCenter(canvas);
        background = new Background(Color.WHITE);
        background.setSkyBackground();
        background.drawBackground(gc, canvas.getWidth(), canvas.getHeight());
    }
    
    public void simulateAllProjectiles() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        background.drawBackground(gc, canvas.getWidth(), canvas.getHeight());

        for (int i = 0; i < numberOfProjectiles; i++) {
            ParameterMenu menu = parameterMenus[i];
            double initialVelocity = convertVelocityToSI(menu.getInitialVelocity(), menu.getInitialVelocityUnit());
            double launchAngle = validateLaunchAngle(menu.getLaunchAngle(), menu.getLaunchAngleUnit());
            double launchHeight = convertHeightToMeters(menu.getLaunchHeight(), menu.getLaunchHeightUnit());
            double weight = convertWeightToSI(menu.getWeight(), menu.getWeightUnit());
            double airResistance = convertAirResistanceToSI(menu.getAirResistance(), menu.getAirResistanceUnit());
            double gravity = convertGravityToSI(Double.parseDouble(gravityTextField.getText()), gravityUnitComboBox.getValue());

            Path trajectory = TrajectoryPathPlotter.pathTrajectory(initialVelocity, launchAngle, launchHeight, weight, airResistance, gravity);
            drawTrajectory(trajectory, i);
        }
    }


    
    public void resetSimulation() {
        // Reset all settings to default
        numberOfProjectilesComboBox.setValue(1);
        gravityTextField.clear();
        gravityUnitComboBox.setValue("m/s²");

        for (ParameterMenu menu : parameterMenus) {
            menu.resetFields();
            menu.setVisible(false);
        }

        parameterMenus[0].setVisible(true); // Ensure the first menu is visible by default
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawTrajectory(Path trajectory, int projectileIndex) {
        trajectory.setStroke(projectileColors[projectileIndex]);
        trajectory.setStrokeWidth(2);
        controlPanel.getChildren().add(trajectory);
    }
    
    private double validateLaunchAngle(double angle, String unit) {
        if (unit.equals("degrees")) {
            return Math.min(90, Math.max(0, angle)); // Clamp between 0 and 90
        } else if (unit.equals("radians")) {
            return Math.min(Math.PI / 2, Math.max(0, angle)); // Clamp between 0 and π/2
        }
        return angle;
    }
    
    private double convertVelocityToSI(double velocity, String unit) {
        switch (unit) {
            case "km/h":
                return velocity * 1000 / 3600;
            case "ft/s":
                return velocity * 0.3048;
            default:
                return velocity;
        }
    }

    private double convertAngleToRadians(double angle, String unit) {
        return unit.equals("degrees") ? Math.toRadians(angle) : angle;
    }

    private double convertHeightToMeters(double height, String unit) {
        switch (unit) {
            case "cm":
                return height / 100;
            case "ft":
                return height * 0.3048;
            default:
                return height;
        }
    }
    
    private double convertWeightToSI(double weight, String unit) {
        switch (unit) {
            case "g":
                return weight / 1000;
            case "lb":
                return weight * 0.453592;
            default:
                return weight;
        }
    }

    private double convertGravityToSI(double gravity, String unit) {
        switch (unit) {
            case "ft/s²":
                return gravity * 0.3048;
            case "g":
                return gravity * 9.81;
            default:
                return gravity;
        }
    }
    
    private double convertAirResistanceToSI(double airResistance, String unit) {
        switch (unit) {
            case "kg/m":
                return airResistance;
            case "g/cm":
                return airResistance / 100;
            case "lb/ft":
                return airResistance * 1.4881639;
            default:
                return airResistance;
        }
    }
}
