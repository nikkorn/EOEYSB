package com.dumbpug.eoeysb.scene.jetpack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dumbpug.eoeysb.tools.Animation;

/**
 * Drawer of jetpack effects.
 */
public class JetPackEffectDrawer {
    private float effectWidth;
    private float effectHeight;
    private float jetPackSpriteHeight;
    private Sprite jetPackSprite;
    private com.dumbpug.eoeysb.tools.Animation leftEngineFire;
    private com.dumbpug.eoeysb.tools.Animation rightEngineFire;

    /**
     * Create a new instance of the JetPackEffectDrawer class.
     * @param jetPackSprite The jetpack sprite.
     */
    public JetPackEffectDrawer(Sprite jetPackSprite) {
        this.jetPackSpriteHeight = jetPackSprite.getHeight();
        this.effectWidth         = jetPackSprite.getWidth();
        this.effectHeight        = effectWidth / 7f;
        this.jetPackSprite       = jetPackSprite;
        leftEngineFire           = new Animation(new Texture(Gdx.files.internal("JetPackEffects/jet_flame_left.png")), 2, 1, (float) 1 / 16f);
        rightEngineFire          = new Animation(new Texture(Gdx.files.internal("JetPackEffects/jet_flame_right.png")), 2, 1, (float) 1 / 16f);
    }

    /**
     * Draw the jetpack effects.
     * @param batch The sprite bath to use.
     * @param leftEngineState The state of the left engine.
     * @param rightEngineState The state of the right engine.
     */
    public void draw(SpriteBatch batch, EngineState leftEngineState, EngineState rightEngineState) {
        // Take care of left engine effects.
        switch(leftEngineState) {
            case FIRING:
                TextureRegion leftEngineFiredTexture = leftEngineFire.getCurrentFrame(true);
                batch.draw(leftEngineFiredTexture, jetPackSprite.getX(), jetPackSprite.getY() - effectHeight, effectWidth/2, effectHeight+(jetPackSpriteHeight/2),
                        effectWidth, effectHeight, 1, 1, jetPackSprite.getRotation());
                break;
            case DAMAGED:
                // TODO Draw some smoke or something.
                break;
            default:
        }
        // Take care of right engine effects.
        switch(rightEngineState) {
            case FIRING:
                TextureRegion rightEngineFiredTexture = rightEngineFire.getCurrentFrame(true);
                batch.draw(rightEngineFiredTexture, jetPackSprite.getX(), jetPackSprite.getY() - effectHeight, effectWidth/2, effectHeight+(jetPackSpriteHeight/2),
                        effectWidth, effectHeight, 1, 1, jetPackSprite.getRotation());
                break;
            case DAMAGED:
                // TODO Draw some smoke or something.
                break;
            default:
        }
    }
}
