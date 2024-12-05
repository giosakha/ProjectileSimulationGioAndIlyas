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
    private Color backgroundColor = Color.WHITE; // Default background color
    private Image backgroundImage;              // Holds the loaded background image
    private String backgroundImagePath;         // Path to the background image

    // Default constructor (no background image, default color)
    public Background() {
        this.backgroundColor = Color.WHITE; // Default color
    }

    // Constructor for setting background color
    public Background(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    // Constructor for setting background image
    public Background(String backgroundImagePath) {
        this.backgroundImagePath = backgroundImagePath;
        this.backgroundImage = loadImage(backgroundImagePath);
    }

    // Getter for background color
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    // Setter for background color
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.backgroundImage = null; // Reset image if color is set
    }

    // Setter for background image with path
    public void setBackgroundImage(String backgroundImagePath) {
        this.backgroundImagePath = backgroundImagePath;
        this.backgroundImage = loadImage(backgroundImagePath);
    }

    public void drawFixedBackground(GraphicsContext gc, double width, double height) {
        if (backgroundImage != null) {
            gc.drawImage(backgroundImage, 0, 0, width, height); // Fixed static image
        } else if (backgroundColor != null) {
            gc.setFill(backgroundColor);
            gc.fillRect(0, 0, width, height); // Fixed static color
        }
    }

    private Image loadImage(String relativePath) {
        try {
            System.out.println("Attempting to load image: " + relativePath); // Debugging
            Image image = new Image(getClass().getResourceAsStream("/" + relativePath));
            if (image.isError()) {
                throw new Exception("Image loading error");
            }
            return image;
        } catch (Exception e) {
            System.out.println("Error loading image: " + relativePath);
            e.printStackTrace();
            return null;
        }
    }



    // Methods to set specific backgrounds
    public void setSkyBackground() {
        setBackgroundImage("projectilesimulationfile/assets/sky.png");
    }

    public void setMountainBackground() {
        setBackgroundImage("projectilesimulationfile/assets/mountain.png");
    }

    public void setCityBackground() {
        setBackgroundImage("projectilesimulationfile/assets/city.png");
    }

    public void setOceanBackground() {
        setBackgroundImage("projectilesimulationfile/assets/ocean.png");
    }

    public void setSpaceBackground() {
        setBackgroundImage("projectilesimulationfile/assets/space.png");
    }

    public void setDesertBackground() {
        setBackgroundImage("projectilesimulationfile/assets/desert.jpg");
    }

    public void setJungleBackground() {
        setBackgroundImage("projectilesimulationfile/assets/jungle.jpg");
    }
}
