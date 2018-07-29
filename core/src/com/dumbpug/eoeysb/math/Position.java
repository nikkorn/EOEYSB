package com.dumbpug.eoeysb.math;

/**
 * Represents a position.
 */
public class Position {
    /**
     * The x/y position.
     */
    private double posX = 0, posY = 0;

    public void setY(double posY) { this.posY = posY; }

    public double getY() {
        return this.posY;
    }

    public void setX(double posX) {
        this.posX = posX;
    }

    public double getX() { return this.posX; }
}
