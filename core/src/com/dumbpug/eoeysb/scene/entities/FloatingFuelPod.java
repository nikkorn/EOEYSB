package com.dumbpug.eoeysb.scene.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.dumbpug.eoeysb.Constants;
import com.dumbpug.eoeysb.scene.entities.drawing.AnimationTextureProvider;
import com.dumbpug.eoeysb.scene.entities.drawing.ITextureProvider;
import com.dumbpug.eoeysb.scene.jetpack.Engine;
import com.dumbpug.eoeysb.tools.Animation;

/**
 * A fuel pod that floats through space.
 */
public class FloatingFuelPod extends Entity {

    /**
     * Create a new instance of the FloatingFuelPod class.
     * @param x The x position of the entity.
     * @param y The y position of the entity.
     */
    public FloatingFuelPod(float x, float y) {
        super(x, y);
    }

    /**
     * Called when this entity collides with an engine of our jetpack.
     * @param engine The engine we are colliding with.
     */
    @Override
    public void onEngineCollision(Engine engine) {
        // Top up the fuel for our engine.
        engine.refuel();
        // Set this entity as inactive.
        this.setActive(false);
    }

    /**
     * Update the position of this entity.
     * @param playerOffset The player offset to apply to the entity position.
     */
    @Override
    public void update(float playerOffset) {
        // Our floating fuel pod must rotate as it flies through space.
        this.setRotation(this.getRotation() + 2f);
        // Do the default entity update.
        super.update(playerOffset);
    }

    @Override
    public ITextureProvider getTextureProvider() {
        Animation animation = new com.dumbpug.eoeysb.tools.Animation(new Texture(Gdx.files.internal("MiscImages/fuel_props.png")), 1, 6, (float) 1/12f);
        return new AnimationTextureProvider(animation);
    }

    @Override
    public float getWidth() { return Constants.FUEL_POD_SIZE; }

    @Override
    public float getHeight() { return Constants.FUEL_POD_SIZE; }
}
