/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectilesimulationfile;

/**
 *
 * @author giorg
 */


public class ProjectileModels {
    private double initialVelocity;    // m/s
    private double launchAngle;        // degrees
    private double mass;               // kg
    private double initialHeight;      // meters
    private double airResistance;      // drag coefficient

    // Constructor to initialize the projectile with basic properties
    public ProjectileModels(double initialVelocity, double launchAngle, double mass, double initialHeight, double airResistance) {
        this.initialVelocity = initialVelocity;
        this.launchAngle = launchAngle;
        this.mass = mass;
        this.initialHeight = initialHeight;
        this.airResistance = airResistance;
    }

    // Getters and Setters
    public double getInitialVelocity() {
        return initialVelocity;
    }

    public void setInitialVelocity(double initialVelocity) {
        this.initialVelocity = initialVelocity;
    }

    public double getLaunchAngle() {
        return launchAngle;
    }

    public void setLaunchAngle(double launchAngle) {
        this.launchAngle = launchAngle;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getInitialHeight() {
        return initialHeight;
    }

    public void setInitialHeight(double initialHeight) {
        this.initialHeight = initialHeight;
    }

    public double getAirResistance() {
        return airResistance;
    }

    public void setAirResistance(double airResistance) {
        this.airResistance = airResistance;
    }

   
}

