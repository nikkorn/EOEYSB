package com.dumbpug.NLAF.scene.entities;

import com.dumbpug.NLAF.Main.JetPack;

/**
 * Represents an entity in the scene
 */
public interface Entity {
    public void setActive(boolean isActive);
    public boolean isActive();
    public boolean isInScreenBounds();
    public void move(double playerOffset);
    public float getOriginX();
    public float getOriginY();
    public void setPosX(float posX);
    public void setPosY(float posY);
    public void setDirectionalRotation(float directionalRotation);
    public float getCollisionRadius();
    public void leftEngineCollision(JetPack jetPack);
    public void rightEngineCollision(JetPack jetPack);
}
