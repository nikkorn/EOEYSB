package com.dumbpug.NLAF.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dumbpug.NLAF.Tools.Animation;

/**
 * Created by Nikolas Howard.
 */
public class JetPackEffectDrawer {
    private float effectWidth;
    private float effectHeight;
    private float jetPackSpriteHeight;
    private Sprite jetPackSprite;
    private Animation leftEngineFire;
    private Animation rightEngineFire;

    public enum EngineState {
        NONE,
        FIRED,
        DAMAGED
    }

    public JetPackEffectDrawer(Sprite jetPackSprite) {
        this.jetPackSpriteHeight = jetPackSprite.getHeight();
        this.effectWidth = jetPackSprite.getWidth();
        this.effectHeight = effectWidth/7;
        this.jetPackSprite = jetPackSprite;
        leftEngineFire = new Animation(new Texture(Gdx.files.internal("JetPackEffects/jet_flame_left.png")), 2, 1, (float) 1/16f);
        rightEngineFire = new Animation(new Texture(Gdx.files.internal("JetPackEffects/jet_flame_right.png")), 2, 1, (float) 1/16f);
    }

    public void draw(SpriteBatch batch, EngineState leftEngineState, EngineState rightEngineState) {
        // Take care of left engine effects.
        switch(leftEngineState) {
            case NONE: // Do nothing.
                break;
            case FIRED:
                TextureRegion leftEngineFiredTexture = leftEngineFire.getCurrentFrame(true);
                batch.draw(leftEngineFiredTexture, jetPackSprite.getX(), jetPackSprite.getY() - effectHeight, effectWidth/2, effectHeight+(jetPackSpriteHeight/2),
                        effectWidth, effectHeight, 1, 1, jetPackSprite.getRotation());
                break;
            case DAMAGED:
                break;
        }

        // Take care of right engine effects.
        switch(rightEngineState) {
            case NONE: // Do nothing.
                break;
            case FIRED:
                TextureRegion rightEngineFiredTexture = rightEngineFire.getCurrentFrame(true);
                batch.draw(rightEngineFiredTexture, jetPackSprite.getX(), jetPackSprite.getY() - effectHeight, effectWidth/2, effectHeight+(jetPackSpriteHeight/2),
                        effectWidth, effectHeight, 1, 1, jetPackSprite.getRotation());
                break;
            case DAMAGED:
                break;
        }
    }
}
