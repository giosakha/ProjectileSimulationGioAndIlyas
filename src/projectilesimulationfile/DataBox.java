/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectilesimulationfile;

/**
 *
 * @author ilyas
 */

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;

public class DataBox extends VBox {
    private Label titleLabel;
    private Label xVelocityLabel;
    private Label yVelocityLabel;
    private Label xAccelerationLabel;
    private Label yAccelerationLabel;
    private Label xPositionLabel;
    private Label yPositionLabel;
    private Label timeLabel;
    private Label angleLabel;

    private Color pathColor;

    // Constructor
    public DataBox() {
        titleLabel = new Label("Data:");
        xVelocityLabel = new Label("x velocity: ");
        yVelocityLabel = new Label("y velocity: ");
        xAccelerationLabel = new Label("x acceleration: ");
        yAccelerationLabel = new Label("y acceleration: ");
        xPositionLabel = new Label("x position: ");
        yPositionLabel = new Label("y position: ");
        timeLabel = new Label("time: ");
        angleLabel = new Label("angle: ");

        getChildren().addAll(titleLabel, xVelocityLabel, yVelocityLabel, xAccelerationLabel,
                yAccelerationLabel, xPositionLabel, yPositionLabel, timeLabel, angleLabel);

        setVisible(false);
    }

    public void attachToProjectilePath(Node projectilePath, double initialVelocity, double launchAngle,
                                       double initialHeight, double weight, double airResistance, double gravity, Color color) {
        this.pathColor = color; // Set the color of the text based on the projectile path

        projectilePath.setOnMouseMoved(event -> {
            setVisible(true);
            setLayoutX(event.getSceneX() + 10);
            setLayoutY(event.getSceneY() + 10);
            // Ensure color and data visibility work for incomplete paths
            updateColors(pathColor);
        });

        projectilePath.setOnMouseExited(event -> setVisible(false));
    }

    public void updateDataLabels(double xVelocity, double yVelocity, double xPosition, double yPosition, double time, double angle) {
        xVelocityLabel.setText("x velocity: " + String.format("%.2f", xVelocity));
        yVelocityLabel.setText("y velocity: " + String.format("%.2f", yVelocity));
        xPositionLabel.setText("x position: " + String.format("%.2f", xPosition));
        yPositionLabel.setText("y position: " + String.format("%.2f", yPosition));
        timeLabel.setText("time: " + String.format("%.2f", time));
        angleLabel.setText("angle: " + String.format("%.2f", angle));
    }

    public void updateColors(Color textColor) {
        titleLabel.setTextFill(textColor);
        for (Node node : getChildren()) {
            if (node instanceof Label) {
                ((Label) node).setTextFill(textColor);
            }
        }
    }
}
