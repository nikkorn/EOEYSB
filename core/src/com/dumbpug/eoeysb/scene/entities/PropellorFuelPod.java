package com.dumbpug.eoeysb.scene.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.dumbpug.eoeysb.Constants;
import com.dumbpug.eoeysb.scene.entities.drawing.ITextureProvider;
import com.dumbpug.eoeysb.scene.entities.drawing.StaticTextureProvider;
import com.dumbpug.eoeysb.scene.jetpack.Engine;

/**
 * A fuel pod with a propellor.
 * Should spawn in the atmosphere.
 */
public class PropellorFuelPod extends Entity {

    /**
     * Create a new instance of the PropellorFuelPod class.
     * @param x The x position of the entity.
     * @param y The y position of the entity.
     */
    public PropellorFuelPod(float x, float y) {
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

    @Override
    public ITextureProvider getTextureProvider() {
        return new StaticTextureProvider(new Texture(Gdx.files.internal("MiscImages/fuel_floating.png")));
    }

    @Override
    public float getWidth() { return Constants.FUEL_POD_SIZE; }

    @Override
    public float getHeight() { return Constants.FUEL_POD_SIZE; }
}
