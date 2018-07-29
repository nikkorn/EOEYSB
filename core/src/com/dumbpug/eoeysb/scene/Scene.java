package com.dumbpug.eoeysb.scene;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.eoeysb.scene.jetpack.JetPack;

/**
 * The game scene, containing sections and entities.
 */
public class Scene {
    /**
     * The jetpack.
     */
    private JetPack jetpack = new JetPack();

    /**
     * Get the jetpack.
     * @return The jetpack;
     */
    public JetPack getJetpack() {
        return this.jetpack;
    }

    /**
     * Draw the scene.
     * @param batch The batch to use in drawing the scene.
     */
    public void draw(SpriteBatch batch) {
        // Draw the jetpack.
        this.jetpack.draw(batch);
    }
}
