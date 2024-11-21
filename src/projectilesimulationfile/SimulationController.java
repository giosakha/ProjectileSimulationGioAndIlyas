/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectilesimulationfile;

/**
 *
 * @author ilyas
 */
import projectilesimulationfile.ParameterMenu;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SimulationController {
    private double initialVelocity = 50;
    private double launchAngle = 45;
    private double gravity = 9.8; // Default gravity in m/s²
    private double launchHeight = 0;
    private int numberOfProjectiles = 1;

    private double[] x, y, vx, vy;
    private Color[] projectileColors = {Color.BLUE, Color.RED, Color.GREEN};
    private GraphicsContext gc;
    private Canvas canvas;
    private VBox controlPanel;
    private Label dataLabel;

    public SimulationController(BorderPane mainPane) {
        createMenuBar(mainPane);
        createCanvas(mainPane);
        createControlPanel(mainPane);
        createDataLabel(mainPane);
    }

    private void createMenuBar(BorderPane mainPane) {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu optionsMenu = new Menu("Options");
        Menu themeMenu = new Menu("Theme");

        MenuItem darkTheme = new MenuItem("Dark");
        MenuItem lightTheme = new MenuItem("Light");

        darkTheme.setOnAction(e -> setTheme(Color.WHITE, Color.BLACK));
        lightTheme.setOnAction(e -> setTheme(Color.BLACK, Color.WHITE));

        themeMenu.getItems().addAll(darkTheme, lightTheme);
        menuBar.getMenus().addAll(fileMenu, editMenu, optionsMenu, themeMenu);
        mainPane.setTop(menuBar);
    }

    private void setTheme(Color textColor, Color backgroundColor) {
        controlPanel.setStyle("-fx-background-color: " + toHex(backgroundColor) + ";");
        for (Node node : controlPanel.getChildren()) {
            if (node instanceof Label) {
                ((Label) node).setTextFill(textColor);
            }
        }
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private void createCanvas(BorderPane mainPane) {
        canvas = new Canvas(600, 400);
        gc = canvas.getGraphicsContext2D();
        mainPane.setCenter(canvas);
    }

    private void createControlPanel(BorderPane mainPane) {
        controlPanel = new VBox(10);
        controlPanel.setPadding(new Insets(10));
        mainPane.setRight(controlPanel);

        // Number of projectiles selection
        Label numberOfProjectilesLabel = new Label("Number of Projectiles:");
        ComboBox<Integer> numberOfProjectilesComboBox = new ComboBox<>();
        numberOfProjectilesComboBox.getItems().addAll(1, 2, 3);
        numberOfProjectilesComboBox.setValue(1);
        numberOfProjectilesComboBox.setOnAction(e -> {
            numberOfProjectiles = numberOfProjectilesComboBox.getValue();
            updateParameterMenus();
        });

        // Gravity input field
        HBox gravityBox = new HBox( 5);
        Label gravityLabel = new Label("Gravity (m/s²):");
        TextField gravityTextField = new TextField("9.8");
        gravityBox.getChildren().addAll(gravityLabel, gravityTextField, new ComboBox<String>());

        // Reset button
        Button resetButton = new Button("Reset");

        controlPanel.getChildren().addAll(numberOfProjectilesLabel, numberOfProjectilesComboBox, gravityBox, resetButton);
        updateParameterMenus();
    }

    private void updateParameterMenus() {
        controlPanel.getChildren().removeIf(node -> node instanceof ParameterMenu);
        for (int i = 0; i < numberOfProjectiles; i++) {
            controlPanel.getChildren().add(new ParameterMenu(i + 1, projectileColors[i]));
        }
    }

    private void createDataLabel(BorderPane mainPane) {
        dataLabel = new Label("Data: ");
        dataLabel.setFont(new Font("Arial", 16));
        mainPane.setTop(dataLabel);
    }
}