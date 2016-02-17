package com.dumbpug.NLAF.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dumbpug.NLAF.Main.Constants;
import com.dumbpug.NLAF.Main.JetPack;
import com.dumbpug.NLAF.Tools.Animation;

/**
 * Created by Nikolas Howard.
 */
public class PropellorFuelPod implements Entity {
    private float textureRotation;
    private float directionRotation;
    private int posX;
    private int posY;
    private int spriteSize;
    private boolean isActive = false;
    private int activeScreenBounds;
    private float step = Constants.MOVEMENT_UNIT/3f;

    // Spawn related
    public static long spawnCooldown = 4000;
    public static long lastSpawned = System.currentTimeMillis();
    public static int spawnChance = 5;

    public PropellorFuelPod(float posX, float posY, float directionalRotation) {
        this.posX = (int) posX;
        this.posY = (int) posY;
        this.directionRotation = directionalRotation;
        this.spriteSize = (int) (Gdx.graphics.getWidth()*0.14);
        this.activeScreenBounds = spriteSize*3;
    }

    /**
     * Is this entity within screen bounds?
     * @return within screen bounds.
     */
    public boolean isInScreenBounds() {
        // Is this entity within the bounds of the screen, if not then it is dead to us.
        if(posY < -activeScreenBounds ||
                posY > (Gdx.graphics.getHeight()+activeScreenBounds) ||
                posX < -activeScreenBounds ||
                posX > (Gdx.graphics.getWidth()+activeScreenBounds)) {
            // This entity is no longer active.
            setActive(false);
            return false;
        }
        return true;
    }

    /**
     * Called when this entity collides with the left engine of our jetpack.
     * @param jetPack
     */
    public void leftEngineCollision(JetPack jetPack) {
        // Top up the fuel for our left engine.
        jetPack.addFuelLeft();
        // Set this entity as inactive.
        this.setActive(false);
    }

    /**
     * Called when this entity collides with the left engine of our jetpack.
     * @param jetPack
     */
    public void rightEngineCollision(JetPack jetPack) {
        // Top up the fuel for our left engine.
        jetPack.addFuelRight();
        // Set this entity as inactive.
        this.setActive(false);
    }

    /**
     * Get whether this entity is active.
     * @return
     */
    public boolean isActive() {
        return this.isActive;
    }

    /**
     * Set whether this entity is active.
     * @param isActive
     */
    public void setActive(boolean isActive) {
        // Keep track of the last time this type of entity was spawned.
        if(isActive) {
            lastSpawned = System.currentTimeMillis();
        }
        this.isActive = isActive;
    }

    /**
     * Set the X position.
     * @param posX
     */
    public void setPosX(float posX) {
        this.posX = (int) posX;
    }

    /**
     * Set the Y position.
     * @param posY
     */
    public void setPosY(float posY) { this.posY = (int) posY; }

    /**
     * Set the directioanl rotation.
     * @param directionalRotation
     */
    public void setDirectionalRotation(float directionalRotation) { this.directionRotation = directionalRotation; }

    /**
     * Set the visual rotation.
     * @param rotation
     */
    public void setRotation(float rotation) { this.textureRotation = rotation; }

    /**
     * Get the X origin (center in this case) of the entity.
     * @return
     */
    public float getOriginX() {
        return  posX + (spriteSize/2);
    }

    /**
     * Get the Y origin (center in this case) of the entity.
     * @return
     */
    public float getOriginY() {
        return  posY + (spriteSize/2);
    }

    /**
     * Get the collision radius of this object.
     * @return
     */
    public float getCollisionRadius() {
        return (float) (spriteSize*0.28);
    }

    /**
     * Get the sprite size.
     * @return
     */
    public int getSpriteSize() { return this.spriteSize; }

    /**
     * Move this entity.
     */
    public void move(double playerOffset) {
        posX += step * Math.cos(Math.toRadians(this.directionRotation + 90));
        posY += step * Math.sin(Math.toRadians(this.directionRotation + 90));
        posY += playerOffset;
        this.isInScreenBounds();
    }

    /**
     * Draw this entity.
     * @param batch
     */
    public void draw(SpriteBatch batch, TextureRegion currentTexture) {
        batch.draw(currentTexture, (float) posX, (float) posY, spriteSize/2f, spriteSize/2f, spriteSize, spriteSize, 1, 1, textureRotation);
    }
}
