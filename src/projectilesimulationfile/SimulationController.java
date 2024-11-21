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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.Priority;

public class SimulationController {

    private int numberOfProjectiles;

    private double[] x, y, vx, vy;
    private Color[] projectileColors = {Color.BLUE, Color.RED, Color.GREEN};
    private GraphicsContext gc;
    private Canvas canvas;
    private VBox controlPanel;
    private Label dataLabel;

    private ParameterMenu ball1Menu;
    private ParameterMenu ball2Menu;
    private ParameterMenu ball3Menu;

    public SimulationController(BorderPane mainPane) {
        createMenuBar(mainPane);
        createDataLabel(mainPane);
        createCanvas(mainPane);
        createControlPanel(mainPane);
        setTheme(Color.BLACK, Color.WHITE); // Default to light theme
    }

    private void createMenuBar(BorderPane mainPane) {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu optionsMenu = new Menu("Options");
        Menu themeMenu = new Menu("Theme");

        MenuItem newFile = new MenuItem("New");
        MenuItem openFile = new MenuItem("Open");
        MenuItem saveFile = new MenuItem("Save");
        fileMenu.getItems().addAll(newFile, openFile, saveFile);

        MenuItem undo = new MenuItem("Undo");
        MenuItem redo = new MenuItem("Redo");
        editMenu.getItems().addAll(undo, redo);

        MenuItem settings = new MenuItem("Settings");
        optionsMenu.getItems().addAll(settings);

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

        gc.setFill(backgroundColor);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
       
        dataLabel.setTextFill(Color.BLACK);
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
    }

    private void createControlPanel(BorderPane mainPane) {
        controlPanel = new VBox(10);
        controlPanel.setPadding(new Insets(10));
        mainPane.setRight(controlPanel);

        HBox numberOfProjectilesBox = new HBox(5);
        Label numberOfProjectilesLabel = new Label("Number of Projectiles:");
        ComboBox<Integer> numberOfProjectilesComboBox = new ComboBox<>();
        numberOfProjectilesComboBox.getItems().addAll(1, 2, 3);
        numberOfProjectilesComboBox.setValue(1);
        numberOfProjectilesComboBox.setOnAction(e -> {
            numberOfProjectiles = numberOfProjectilesComboBox.getValue();
            updateParameterMenus();
        });

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(e -> resetTrajectory());
        numberOfProjectilesBox.getChildren().addAll(numberOfProjectilesLabel, numberOfProjectilesComboBox, resetButton);

        HBox gravityBox = new HBox(5);
        Label gravityLabel = new Label("Gravity:");
        TextField gravityTextField = new TextField("");
        ComboBox<String> gravityUnitComboBox = new ComboBox<>();
        gravityUnitComboBox.getItems().addAll("m/s²", "ft/s²", "g");
        gravityUnitComboBox.setValue("m/s²");

        gravityBox.getChildren().addAll(gravityLabel, gravityTextField, gravityUnitComboBox);

        controlPanel.getChildren().addAll(numberOfProjectilesBox, gravityBox);

        ball1Menu = new ParameterMenu(1, projectileColors[0]);
        ball2Menu = new ParameterMenu(2, projectileColors[1]);
        ball3Menu = new ParameterMenu(3, projectileColors[2]);

        controlPanel.getChildren().addAll(ball1Menu, ball2Menu, ball3Menu);
        updateParameterMenus();
    }
    
    private void updateParameterMenus() {
        ball2Menu.setVisible(numberOfProjectiles >= 2);
        ball3Menu.setVisible(numberOfProjectiles == 3);
    }
    
    private void resetTrajectory() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    
    private void createDataLabel(BorderPane mainPane) {
        HBox dataBox = new HBox();
        dataBox.setPadding(new Insets(5));
    
        Label dataLabelTitle = new Label("Data: ");
        dataLabelTitle.setFont(new Font("Arial", 16));
        dataLabelTitle.setTextFill(Color.BLACK);

        dataLabel = new Label("Data: ");
        dataLabel.setFont(new Font("Arial", 16));
        dataLabel.setTextFill(Color.BLACK);
        dataBox.getChildren().addAll(dataLabelTitle, dataLabel);
    
        mainPane.setBottom(dataBox);
    }
}