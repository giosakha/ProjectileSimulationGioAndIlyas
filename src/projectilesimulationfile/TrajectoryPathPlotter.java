/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectilesimulationfile;
import ProjectileSimulationfile.PhysicsEngine;

import javafx.scene.effect.Light.Point;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 *
 * @author giorg
 */
public class TrajectoryPathPlotter { //draws path
    private static final double timeInterval = 0.1;
    public static Path pathTrajectory(double initVelocity, double launchAngle, double initialY) {
        Path path = new Path();
        double time = 0.0;
        Point position = PhysicsEngine.calculatePosition(time, initVelocity, launchAngle, initialY);
        double x = position.getX(), y = position.getY();
        path.getElements().add(new MoveTo(x, y));
        while (y >= 0) {
            time += timeInterval;
            position = PhysicsEngine.calculatePosition(time, initVelocity, launchAngle, initialY);
            x = position.getX();
            y = position.getY();
            path.getElements().add(new LineTo(x, y));
        }
        return path;
    }
}   
    