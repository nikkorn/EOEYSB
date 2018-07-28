package com.dumbpug.eoeysb.scene;

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
}
