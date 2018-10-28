package com.dumbpug.eoeysb.scene.jetpack;

import com.dumbpug.eoeysb.Constants;
import com.dumbpug.eoeysb.math.Position;
import com.dumbpug.eoeysb.scene.entities.Entity;

/**
 * A jetpack engine.
 */
public class Engine {
    /**
     * The position of the engine.
     */
    private Position position = new Position();
    /**
     * The engine state.
     */
    private EngineState state = EngineState.NORMAL;
    /**
     * The fuel level.
     */
    private double fuelLevel = Constants.FUELLING_TANK_LIMIT;

    /**
     * Get the position of the engine.
     * @return The position of the engine.
     */
    public Position getPosition() { return this.position; }

    /**
     * Get the engine state.
     * @return The engine state.
     */
    public EngineState getState() { return this.state; }

    /**
     * Get the remaining fuel level as a percentage.
     * @return The remaining fuel level as a percentage.
     */
    public double getFuelLevelPercentage() { return (this.fuelLevel / Constants.FUELLING_TANK_LIMIT) * 100; }

    /**
     * Update the engine, passing a flag defining whether to attempt to fire it..
     * @return Whether the engine fired.
     */
    public boolean update(boolean fire) {
        // If the engine is damaged or we are not being asked to fire then we cannot do anything.
        if (this.state == EngineState.DAMAGED || !fire) {
            return false;
        }
        // Do we have the fuel to fire?
        boolean canFire = this.fuelLevel >= Constants.FUELLING_TICK_USE;
        // If we can fire then use some fuel to do so.
        if (canFire) {
            // Use the fuel.
            this.fuelLevel -= Constants.FUELLING_TICK_USE;
            // We do not want negative fuel levels.
            this.fuelLevel = (this.fuelLevel < 0) ? 0 : this.fuelLevel;
            // We managed to fire!
            return true;
        }
        // We did not have the fuel needed to fire.
        return false;
    }

    /**
     * Refuel the engine.
     */
    public void refuel() { this.fuelLevel = Constants.FUELLING_TANK_LIMIT; }

    /**
     * Checks whether the engine is colliding with the specified entity and acts accordingly.
     * @param entity The entity to check for a collision with.
     */
    public void checkForEntityCollision(Entity entity) {
        if (entity.isCollidingWithEngine(this)) {
            entity.onEngineCollision(this);
        }
    }
}
