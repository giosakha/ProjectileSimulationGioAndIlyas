/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProjectileSimulationfile;

import javafx.scene.effect.Light.Point;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;

/**
 *
 * @author giorg
 */
public class PhysicsEngine {
   
    private static double gravity = 9.81;
    public static Point calculatePosition(double time, double initVelocity , double launchAngle) {
       double angleRad = Math.toRadians(launchAngle);
        double x = initVelocity * time * Math.cos(angleRad);
        double y = (initVelocity * time * Math.sin(angleRad)) - (0.5 * gravity * time * time);
        
    return new Point(x,y , 5 , Color.RED);
    }
    public static double[] calculateVelocity(double time) {
      double[] Velocities ; //x,y,actual 
       
     return null;
        
    }
    public static double addAirResistance(double velocity) {
      double velocityAirResistance = 0; //actual
       
      return velocityAirResistance;
        
    }
    public Path updateTrajectory() {
        Path trajectory;    
        return null;
    }
}
