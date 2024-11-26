/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectilesimulationfile;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * 
 * @author ilyas
 */

public class Background {
    private Color backgroundColor;

    public Background(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void drawBackground(GraphicsContext gc, double width, double height) {
        gc.setFill(backgroundColor);
        gc.fillRect(0, 0, width, height);
    }

    public void drawGrid(GraphicsContext gc, double width, double height, double spacing) {
        gc.setStroke(Color.GRAY);
        for (double x = 0; x <= width; x += spacing) {
            gc.strokeLine(x, 0, x, height);
        }
        for (double y = 0; y <= height; y += spacing) {
            gc.strokeLine(0, y, width, y);
        }
    }
}