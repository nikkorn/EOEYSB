package com.dumbpug.eoeysb.scene.entities;

import com.dumbpug.eoeysb.scene.jetpack.Engine;

/**
 * Represents an entity that can move on the x and y axis in the scene.
 */
public abstract class Entity {
    /**
     * The position of the entity.
     */
    private float x, y;
    /**
     * The velocity of the entity.
     */
    private float velocityX, velocityY;
    /**
     * The rotation of the entity.
     */
    private float rotation;

    /**
     * Create a new instance of the Entity class.
     * @param x The x position of the entity.
     * @param y The x position of the entity.
     */
    public Entity(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Update the position of this entity.
     * @param playerOffset The player offset to apply to the entity position.
     */
    public void update(float playerOffset) {
        this.x += this.velocityX;
        this.y += this.velocityY;
        this.y += playerOffset;
    }

    /**
     * Get the x position.
     * @return The x position.
     */
    public float getX() {
        return this.x;
    }

    /**
     * Get the y position.
     * @return The y position.
     */
    public float getY() {
        return this.y;
    }

    /**
     * Set the x velocity of the entity.
     * @param velocityX The x velocity of the entity.
     */
    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    /**
     * Set the y velocity of the entity.
     * @param velocityY The y velocity of the entity.
     */
    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    /**
     * Set the rotation of the entity.
     * @param rotation The rotation of the entity.
     */
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    /**
     * Get the rotation of the entity.
     * @return The rotation of the entity.
     */
    public float getRotation() {
        return this.rotation;
    }

    /**
     * Get the size of the entity.
     * @return The size of the entity.
     */
    public abstract float getSize();

    /**
     * Get the collision radius of the entity.
     * @return The collision radius of the entity.
     */
    public abstract float getCollisionRadius();

    /**
     * Handle a collision with an engine.
     * @param engine The colliding engine.
     */
    public abstract void onEngineCollision(Engine engine);
}
