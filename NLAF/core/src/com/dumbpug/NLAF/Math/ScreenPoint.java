package com.dumbpug.NLAF.Math;

/**
 * Created by Nikolas Howard.
 */
public class ScreenPoint {
    private double posX = 0;
    private double posY = 0;

    public void setY(double posY) { this.posY = posY; }

    public double getY() {
        return this.posY;
    }

    public void setX(double posX) {
        this.posX = posX;
    }

    public double getX() { return this.posX; }
}
