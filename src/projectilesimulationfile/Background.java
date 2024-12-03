/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectilesimulationfile;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * 
 * @author ilyas
 */
public class Background {
    private Color backgroundColor = Color.WHITE;
    private Image backgroundImage;
    private String backgroundImagePath;

    public Background(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Background(String backgroundImagePath) {
        this.backgroundImagePath = backgroundImagePath;
        this.backgroundImage = new Image("file:" + backgroundImagePath);
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setBackgroundImage(String backgroundImagePath) {
        this.backgroundImagePath = backgroundImagePath;
        this.backgroundImage = new Image("file:" + backgroundImagePath);
    }

    public void drawBackground(GraphicsContext gc, double width, double height) {
        if (backgroundImage != null) {
            gc.drawImage(backgroundImage, 0, 0, width, height);
        } else {
            gc.setFill(backgroundColor != null ? backgroundColor : Color.WHITE);
            gc.fillRect(0, 0, width, height);
        }
    }

    
    public void setSkyBackground() {
        setBackgroundImage("assets/sky.png");
    }

    public void setMountainBackground() {
        setBackgroundImage("assets/mountain.png");
    }

    public void setCityBackground() {
        setBackgroundImage("assets/city.png");
    }

    public void setOceanBackground() {
        setBackgroundImage("assets/ocean.png");
    }

    public void setSpaceBackground() {
        setBackgroundImage("assets/space.png");
    }

    public void setDesertBackground() {
        setBackgroundImage("assets/desert.jpg");
    }

    public void setJungleBackground() {
        setBackgroundImage("assets/jungle.jpg");
    }
}