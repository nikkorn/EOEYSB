package com.dumbpug.eoeysb.scene.clouds;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * A cloud.
 */
public class Cloud {
    // The sprite for the cloud.
    public Sprite sprite;
    // The distance to move cloud on step.
    public float step;
    // Direction of cloud.
    public CloudDirection direction;
    // Cloud position
    public float posY, posX;
    // Is this cloud in the foreground.
    public boolean isForegroundCloud = false;

    public void move(float offsetY){
        switch (direction) {
            case LEFT:
                posX -= step;
                break;
            case RIGHT:
                posX += step;
                break;
        }
        // Foreground clouds will move faster vertically too.
        if(isForegroundCloud){
            posY += (offsetY*1.5f);
        } else {
            posY += offsetY;
        }
    }

    public Sprite getSprite(){
        sprite.setPosition(posX, posY);
        return sprite;
    }
}
