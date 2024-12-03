/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectilesimulationfile;
/**
 *
 * @author giorg
 */
import ProjectileSimulationfile.PhysicsEngine;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.geometry.Point2D;

public class TrajectoryPathPlotter {

    public static Path pathTrajectory(double initialVelocity, double launchAngle, double initialHeight, double weight, double airResistance, double gravity) {
        Path path = new Path();

        // Dynamically calculate time interval based on initial velocity and gravity
        double dynamicTimeInterval = calculateTimeInterval(initialVelocity, gravity);
        double time = 0.0;

        // Calculate initial position
        Point2D position = PhysicsEngine.calculatePosition(time, initialVelocity, launchAngle, initialHeight, weight, airResistance);
        path.getElements().add(new MoveTo(position.getX(), position.getY()));

        // Add trajectory points
        while (position.getY() >= 0) { // Continue until projectile hits the ground
            time += dynamicTimeInterval;

            position = PhysicsEngine.calculatePosition(time, initialVelocity, launchAngle, initialHeight, weight, airResistance);

            if (position.getY() < 0) {
                // Adjust the final point to ground level
                double tGround = calculateTimeToGround(time, initialVelocity, launchAngle, initialHeight, gravity);
                position = PhysicsEngine.calculatePosition(tGround, initialVelocity, launchAngle, initialHeight, weight, airResistance);
            }

            path.getElements().add(new LineTo(position.getX(), position.getY()));

            // Optional: Limit the number of points to prevent memory issues
            if (path.getElements().size() > 500) {
                break;
            }
        }

        return path;
    }

    private static double calculateTimeInterval(double initialVelocity, double gravity) {
        // Adjust time step dynamically based on velocity and gravity
        return Math.min(0.05, Math.sqrt(2 * 0.01 / gravity)); // Example dynamic calculation
    }

    private static double calculateTimeToGround(double time, double initialVelocity, double launchAngle, double initialHeight, double gravity) {
        // Estimate time when the projectile will hit the ground
        double vy = initialVelocity * Math.sin(launchAngle);
        double totalTime = (-vy - Math.sqrt(vy * vy - 2 * gravity * initialHeight)) / (-gravity);
        return Math.min(time, totalTime); // Ensure the calculated time is not past the ground
    }
}