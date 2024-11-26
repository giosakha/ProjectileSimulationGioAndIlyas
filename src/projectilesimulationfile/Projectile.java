/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectilesimulationfile;

/**
 *
 * @author giorg
 */
public class Projectile {
    
    private double initialVelocity;    
    private double launchAngle;        
    private double mass;               
    private double initialHeight;      
    private double airResistance;      

    public Projectile(double initialVelocity, double launchAngle, double mass, double initialHeight, double airResistance) {
        this.initialVelocity = initialVelocity;
        this.launchAngle = launchAngle;
        this.mass = mass;
        this.initialHeight = initialHeight;
        this.airResistance = airResistance;
    }

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

