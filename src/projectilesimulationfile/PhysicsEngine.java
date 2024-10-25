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
   
    private static final double gravity = 9.81;

    public static Point calculatePosition(double time, double initVelocity, double launchAngle, double initialY) {
        double angleRad = Math.toRadians(launchAngle);
        double x = initVelocity * time * Math.cos(angleRad);
        double y = initialY + (initVelocity * time * Math.sin(angleRad)) - (0.5 * gravity * time * time);

        return new Point(x, y, 5, Color.RED);
    }

    public static double[] calculateVelocity(double time, double initialVelocity, double launchAngle) {
        double[] velocities = new double[3];
        double angleRad = Math.toRadians(launchAngle);
        double vx = initialVelocity * Math.cos(angleRad);
        double vy = (initialVelocity * Math.sin(angleRad)) - (gravity * time);

        double speed = Math.sqrt(vx * vx + vy * vy);
        velocities[0] = vx;
        velocities[1] = vy;
        velocities[2] = speed;

        return velocities;
    }

    public static double calculateAcceleration() {
        return gravity;
    }
}

    
