/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProjectileSimulationfile;

import java.awt.Color;
import java.awt.Point;
import javafx.scene.shape.Path;

/**
 *
 * @author giorg
 */
public class PhysicsEngine {
   
    private static double gravity = 9.81;
    
    public static class ColoredPoint {
        public double x;
        public double y;
        public Color color;

        public ColoredPoint(double x, double y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }
    
    public static ColoredPoint calculatePosition(double time, double initVelocity , double launchAngle) {
        double angleRad = Math.toRadians(launchAngle);
        double x = initVelocity * time * Math.cos(angleRad);
        double y = (initVelocity * time * Math.sin(angleRad)) - (0.5 * gravity * time * time);
        
        return new ColoredPoint(x, y, Color.RED);
    }
    
    public static double[] calculateVelocity(double time, double initialVelocity, double launchAngle, double airResistance) {
        double[] velocities = new double[3]; // Array to store vx, vy, and speed

        double angleRad = Math.toRadians(launchAngle);
        double vx = initialVelocity * Math.cos(angleRad);
        double vy = (initialVelocity * Math.sin(angleRad)) - (gravity * time);

        if (airResistance != 0) { //if there is air resistance , change vx and vy accordingly
            double airResistanceFactor = Math.exp(-airResistance * time); 
            vx *= airResistanceFactor;  
            vy *= airResistanceFactor;  
        }
        double speed = Math.sqrt(vx * vx + vy * vy); //pyhtag theorem
        velocities[0] = vx;     
        velocities[1] = vy;     
        velocities[2] = speed;  

        return velocities;    
    }
    public static double calculateAcceleration() {
        double acceleration = 0;
        // have to get it done
        return acceleration;
    }
}