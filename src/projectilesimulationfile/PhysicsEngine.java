/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectilesimulationfile;


/**
*
* @author giorg
*/
   
import javafx.geometry.Point2D;

public class PhysicsEngine {
   
    private static double gravity;
    private static double airResistance;

    public static void setGravity(double g) {
        gravity = g;
    }

    public static void setAirResistance(double ar) {
        airResistance = ar;
    }

    public static Point2D calculatePosition(double time, double initVelocity, double launchAngle, double initialY, double weight, double airResistance) {
        double angleRad = Math.toRadians(launchAngle);
    
        // Initial horizontal and vertical velocities
        double v0x = initVelocity * Math.cos(angleRad);
        double v0y = initVelocity * Math.sin(angleRad);
    
        // Calculate the horizontal position considering air resistance
        double x = v0x * time - (0.5 * airResistance * Math.pow(v0x, 2) * time * time / weight);
    
        // Calculate the vertical position
        double y = initialY + (v0y * time) - (0.5 * gravity * time * time);
    
        // Adjust vertical position for air resistance
        double airResistanceEffect = airResistance * Math.pow(v0y, 2) * time / weight;
        y -= airResistanceEffect;

        return new Point2D(x, y);
    }   

    public static double[] calculateVelocity(double time, double initialVelocity, double launchAngle) {
        double[] velocities = new double[3];
        double angleRad = Math.toRadians(launchAngle);
        double vx = initialVelocity * Math.cos(angleRad);
        double vy = (initialVelocity * Math.sin(angleRad)) - (gravity * time);

        double speed = Math.sqrt(vx * vx + vy * vy);
        velocities[0] = vx; // Horizontal velocity
        velocities[1] = vy; // Vertical velocity
        velocities[2] = speed; // Total speed

        return velocities;
    }

    public static double calculateAccelerationY() {
        return gravity;
    }

    public static double calculateAccelerationX(double velocity) {
        return -airResistance * velocity;
    }

    public static double calculateFinalAngle(double time, double initialVelocity, double launchAngle) {
        double[] velocities = calculateVelocity(time, initialVelocity, launchAngle);
        double vx = velocities[0];
        double vy = velocities[1];

        double finalAngleRad = Math.atan2(vy, vx);
        return Math.toDegrees(finalAngleRad);
    }
}