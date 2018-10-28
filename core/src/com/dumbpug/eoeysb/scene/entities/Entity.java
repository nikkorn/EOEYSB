package com.dumbpug.eoeysb.scene.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.eoeysb.scene.entities.drawing.ITextureProvider;
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
     * Whether the entity is active.
     */
    private boolean isActive = false;

    /**
     * Create a new instance of the Entity class.
     * @param x The x position of the entity.
     * @param y The y position of the entity.
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
     * Draw this entity.
     * @param batch The sprite batch to use in drawing this entity.
     */
    public void draw(SpriteBatch batch) {
        batch.draw(this.getTextureProvider().getTexture(), x, y, getWidth() / 2f, getHeight() / 2f, getWidth(), getHeight(), 1, 1, rotation);
    }

    /**
     * Gets whether this entity is colliding with the specified engine.
     * @param engine Whether this entity is colliding with the specified engine.
     */
    public boolean isCollidingWithEngine(Engine engine) {
        // TODO Do the magic stuff!!
        return false;
    }

    /**
     * Get the x position.
     * @return The x position.
     */
    public float getX() { return this.x; }

    /**
     * Get the y position.
     * @return The y position.
     */
    public float getY() { return this.y; }

    /**
     * Set the X position.
     * @param x The x position.
     */
    public void setX(float x) { this.x = x; }

    /**
     * Set the Y position.
     * @param y The y position.
     */
    public void setY(float y) { this.y = y; }

    /**
     * Get the X origin of the entity.
     * @return The X origin of the entity.
     */
    public float getOriginX() { return x + (getWidth() / 2f); }

    /**
     * Get the Y origin of the entity.
     * @return The Y origin of the entity.
     */
    public float getOriginY() { return  y + (getHeight() / 2f); }

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
     * Get whether this entity is active.
     * @return Whether this entity is active.
     */
    public boolean isActive() {
        return this.isActive;
    }

    /**
     * Set whether this entity is active.
     * @param isActive Whether this entity is active.
     */
    public void setActive(boolean isActive) { this.isActive = isActive; }

    /**
     * Set the rotation of the entity.
     * @param rotation The rotation of the entity.
     */
    public void setRotation(float rotation) { this.rotation = rotation; }

    /**
     * Get the rotation of the entity.
     * @return The rotation of the entity.
     */
    public float getRotation() { return this.rotation; }

    /**
     * Get the provider of textures to use in drawing the entity.
     * @return The provider of textures to use in drawing the entity.
     */
    public abstract ITextureProvider getTextureProvider();

    /**
     * Get the width.
     * @return The width.
     */
    public abstract float getWidth();

    /**
     * Get the height.
     * @return The height.
     */
    public abstract float getHeight();

    /**
     * Handle a collision with an engine.
     * @param engine The colliding engine.
     */
    public abstract void onEngineCollision(Engine engine);
}
