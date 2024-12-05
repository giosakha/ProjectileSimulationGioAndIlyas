/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectilesimulationfile;

/**
 *
 * @author giorg
 */

import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.geometry.Point2D;
import javafx.scene.shape.PathElement;

public class TrajectoryPathPlotter {

    private static final double MAX_SIMULATION_TIME = 30.0; // Maximum simulation time in seconds
    private static final int MAX_PATH_POINTS = 10000; // Maximum number of points in the trajectory path

    public static Path pathTrajectory(double initialVelocity, double launchAngle, double initialHeight, 
                                      double weight, double airResistance, double gravity) {
        Path path = new Path();

        double canvasHeight = 600; // Adjust canvas height based on the simulation settings
        Point2D startPosition = new Point2D(0, canvasHeight - initialHeight);

        path.getElements().add(new MoveTo(startPosition.getX(), startPosition.getY()));

        double time = 0.0;
        double timeInterval = 0.05; // Fixed interval for smoother path
        int pointCount = 0;

        while (time < MAX_SIMULATION_TIME && pointCount < MAX_PATH_POINTS) {
            Point2D position = PhysicsEngine.calculatePosition(time, initialVelocity, launchAngle, initialHeight, weight, airResistance);
            if (position.getY() <= 0) break; // Stop when the projectile hits the ground

            Point2D adjustedPosition = new Point2D(position.getX(), canvasHeight - position.getY());
            path.getElements().add(new LineTo(adjustedPosition.getX(), adjustedPosition.getY()));

            time += timeInterval;
            pointCount++;
        }

        if (pointCount >= MAX_PATH_POINTS) {
            System.out.println("Warning: Maximum path points reached, trajectory truncated.");
        }

        return path;
    }

    public static Point2D findMaxExtents(double initialVelocity, double launchAngle, double initialHeight, 
                                         double weight, double airResistance, double gravity) {
        Path path = pathTrajectory(initialVelocity, launchAngle, initialHeight, weight, airResistance, gravity);

        double maxX = 0, maxY = 0;
        for (PathElement element : path.getElements()) {
            if (element instanceof MoveTo moveTo) {
                maxX = Math.max(maxX, moveTo.getX());
                maxY = Math.max(maxY, moveTo.getY());
            } else if (element instanceof LineTo lineTo) {
                maxX = Math.max(maxX, lineTo.getX());
                maxY = Math.max(maxY, lineTo.getY());
            }
        }
        return new Point2D(maxX, maxY);
    }
}
