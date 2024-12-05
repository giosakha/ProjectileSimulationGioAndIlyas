/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package projectilesimulationfile;

/**
 *
 * @author ilyas
 */
import java.io.File;
import java.util.Stack;
import java.util.function.UnaryOperator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class SimulationController {

    private BorderPane mainPane;
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
    
    private Stack<Runnable> undoStack = new Stack<>();
    private Stack<Runnable> redoStack = new Stack<>();

    public SimulationController(BorderPane mainPane) {
        this.mainPane = mainPane;
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
        boolean isValid = true;

        // Validate gravity input
        if (gravityTextField.getText().isEmpty() || !isValidDouble(gravityTextField.getText())) {
            isValid = false;
        }

        // Validate all parameter menus
        for (int i = 0; i < numberOfProjectiles; i++) {
            ParameterMenu menu = parameterMenus[i];
            if (!menu.areFieldsValid()) {
                isValid = false;
                break;
            }

            double angle = menu.getLaunchAngle();
            String unit = menu.getLaunchAngleUnit();

            if ((unit.equals("degrees") && (angle > 90 || angle < 0)) ||
                (unit.equals("radians") && (angle > Math.PI / 2 || angle < 0))) {
                isValid = false;
                break;
            }

            // Validate velocity and air resistance ranges
            double velocity = menu.getInitialVelocity();
            double airResistance = menu.getAirResistance();

            if (velocity < 0.1 || velocity > 1000 || airResistance < 0 || airResistance > 1000) {
                isValid = false;
                break;
            }
        }

        simulateButton.setDisable(!isValid); // Disable simulate button if inputs are invalid
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
        System.out.println("Updating background..."); // Debug message
        backgroundSetter.run(); // Set the background
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Clear the canvas
        background.drawFixedBackground(gc, canvas.getWidth(), canvas.getHeight()); // Redraw the background
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
        newFile.setOnAction(e -> resetSimulation());
        openFile.setOnAction(e -> openSimulation());
        saveFile.setOnAction(e -> saveSimulation());
        fileMenu.getItems().addAll(newFile, openFile, saveFile);

        // Edit menu items
        MenuItem undo = new MenuItem("Undo");
        MenuItem redo = new MenuItem("Redo");
        undo.setOnAction(e -> undoAction());
        redo.setOnAction(e -> redoAction());
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

    private void saveSimulation() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Simulation");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Simulation Files", "*.sim"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            // Write simulation state to file
        }
    }

    private void openSimulation() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Simulation");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Simulation Files", "*.sim"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            // Load simulation state from file
        }
    }

    private void undoAction() {
        if (!undoStack.isEmpty()) {
            Runnable lastAction = undoStack.pop();
            lastAction.run(); // Perform the undo action
            redoStack.push(lastAction);
            redrawCanvas();
        }
    }

    private void redoAction() {
        if (!redoStack.isEmpty()) {
            Runnable redoAction = redoStack.pop();
            redoAction.run(); // Perform the redo action
            undoStack.push(redoAction);
            redrawCanvas();
        }
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
        background.drawFixedBackground(gc, canvas.getWidth(), canvas.getHeight());
    }           


    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private void createCanvas(BorderPane mainPane) {
        // Create a static background canvas
        Canvas backgroundCanvas = new Canvas(800, 600);
        GraphicsContext backgroundGc = backgroundCanvas.getGraphicsContext2D();

        // Draw the fixed background once
        background = new Background(Color.LIGHTBLUE); // Example color
        background.setSkyBackground(); // Example image
        background.drawFixedBackground(backgroundGc, 800, 600);

        // Create a dynamic canvas for trajectories
        canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();

        // Overlay the canvases
        StackPane stackPane = new StackPane(backgroundCanvas, canvas);
        mainPane.setCenter(stackPane);

        // Add listeners for canvas resizing
        canvas.widthProperty().addListener((observable, oldWidth, newWidth) -> redrawCanvas());
        canvas.heightProperty().addListener((observable, oldHeight, newHeight) -> redrawCanvas());
    }

    private void redrawCanvas() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int i = 0; i < numberOfProjectiles; i++) {
            ParameterMenu menu = parameterMenus[i];
            double initialVelocity = convertVelocityToSI(menu.getInitialVelocity(), menu.getInitialVelocityUnit());
            double launchAngle = convertAngleToRadians(menu.getLaunchAngle(), menu.getLaunchAngleUnit());
            double launchHeight = convertHeightToMeters(menu.getLaunchHeight(), menu.getLaunchHeightUnit());
            double weight = convertWeightToSI(menu.getWeight(), menu.getWeightUnit());
            double airResistance = convertAirResistanceToSI(menu.getAirResistance(), menu.getAirResistanceUnit());
            double gravity = convertGravityToSI(Double.parseDouble(gravityTextField.getText()), gravityUnitComboBox.getValue());

            drawTrajectoryPath(initialVelocity, launchAngle, launchHeight, weight, airResistance, gravity, i);
        }
    }


    private void adjustCanvasSize(double maxX, double maxY) {
        double margin = 50; // Add a margin for better visibility
        canvas.setWidth(maxX + margin);
        canvas.setHeight(maxY + margin);
        redrawCanvas(); // Ensure trajectories are redrawn
    }

    private void simulateAllProjectiles() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double maxX = 0, maxY = 0;

        for (int i = 0; i < numberOfProjectiles; i++) {
            ParameterMenu menu = parameterMenus[i];
            double initialVelocity = convertVelocityToSI(menu.getInitialVelocity(), menu.getInitialVelocityUnit());
            double launchAngle = convertAngleToRadians(menu.getLaunchAngle(), menu.getLaunchAngleUnit());
            double launchHeight = convertHeightToMeters(menu.getLaunchHeight(), menu.getLaunchHeightUnit());
            double weight = convertWeightToSI(menu.getWeight(), menu.getWeightUnit());
            double airResistance = convertAirResistanceToSI(menu.getAirResistance(), menu.getAirResistanceUnit());
            double gravity = convertGravityToSI(Double.parseDouble(gravityTextField.getText()), gravityUnitComboBox.getValue());

            Point2D maxPoint = TrajectoryPathPlotter.findMaxExtents(
                initialVelocity, launchAngle, launchHeight, weight, airResistance, gravity
            );
            maxX = Math.max(maxX, maxPoint.getX());
            maxY = Math.max(maxY, maxPoint.getY() + launchHeight);

            drawTrajectoryPath(initialVelocity, launchAngle, launchHeight, weight, airResistance, gravity, i);
            animateTrajectory(initialVelocity, launchAngle, launchHeight, weight, airResistance, gravity, i);
        }

        adjustCanvasSize(maxX, maxY);
    }


    private void drawTrajectoryPath(double initialVelocity, double launchAngle, double launchHeight,
                                double weight, double airResistance, double gravity, int projectileIndex) {
        Path trajectory = TrajectoryPathPlotter.pathTrajectory(
            initialVelocity, launchAngle, launchHeight, weight, airResistance, gravity
        );
        trajectory.setStroke(projectileColors[projectileIndex]);
        trajectory.setStrokeWidth(2);

        // Draw trajectory directly on the canvas
        gc.setStroke(projectileColors[projectileIndex]);
        gc.setLineWidth(2);

        Point2D prevPoint = null;
        for (PathElement element : trajectory.getElements()) {
            if (element instanceof MoveTo moveTo) {
                prevPoint = new Point2D(moveTo.getX(), canvas.getHeight() - moveTo.getY());
            } else if (element instanceof LineTo lineTo) {
                if (prevPoint != null) {
                    gc.strokeLine(prevPoint.getX(), canvas.getHeight() - prevPoint.getY(),
                                  lineTo.getX(), canvas.getHeight() - lineTo.getY());
                }
                prevPoint = new Point2D(lineTo.getX(), lineTo.getY());
            }
        }
    }


    private void animateTrajectory(double initialVelocity, double launchAngle, double launchHeight,
                               double weight, double airResistance, double gravity, int projectileIndex) {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        double[] time = {0.0};

        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.05), event -> {
            Point2D position = PhysicsEngine.calculatePosition(time[0], initialVelocity, launchAngle, launchHeight, weight, airResistance);
            if (position.getY() <= 0) {
                timeline.stop();
            } else {
                gc.setFill(projectileColors[projectileIndex]);
                gc.fillOval(position.getX(), canvas.getHeight() - position.getY(), 4, 4);
            }
            time[0] += 0.05;
        }));

        timeline.play();
    }

    public void resetSimulation() {
        numberOfProjectilesComboBox.setValue(1);
        gravityTextField.clear();
        gravityUnitComboBox.setValue("m/s²");

        for (ParameterMenu menu : parameterMenus) {
            menu.resetFields();
            menu.setVisible(false);
        }

        parameterMenus[0].setVisible(true); // Ensure the first menu is visible

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        background.drawFixedBackground(gc, canvas.getWidth(), canvas.getHeight()); // Retain the current background
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

