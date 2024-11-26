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

public class DataBox extends VBox {
    private Label titleLabel;
    private Label xVelocityLabel;
    private Label yVelocityLabel;
    private Label xAccelerationLabel;
    private Label yAccelerationLabel;
    private Label xPositionLabel;
    private Label yPositionLabel;
    private Label timeLabel;

    public DataBox() {
        titleLabel = new Label("Data:");
        xVelocityLabel = new Label("x velocity: ");
        yVelocityLabel = new Label("y velocity: ");
        xAccelerationLabel = new Label("x acceleration: ");
        yAccelerationLabel = new Label("y acceleration: ");
        xPositionLabel = new Label("x position: ");
        yPositionLabel = new Label("y position: ");
        timeLabel = new Label("time: ");

        getChildren().addAll(titleLabel, xVelocityLabel, yVelocityLabel, xAccelerationLabel, 
                              yAccelerationLabel, xPositionLabel, yPositionLabel, timeLabel);
    }

    public void updateDataLabels(double xVelocity, double yVelocity, double xAcceleration, 
                                  double yAcceleration, double xPosition, double yPosition, double time) {
        xVelocityLabel.setText("x velocity: " + xVelocity);
        yVelocityLabel.setText("y velocity: " + yVelocity);
        xAccelerationLabel.setText("x acceleration: " + xAcceleration);
        yAccelerationLabel.setText("y acceleration: " + yAcceleration);
        xPositionLabel.setText("x position: " + xPosition);
        yPositionLabel.setText("y position: " + yPosition);
        timeLabel.setText("time: " + time);
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
