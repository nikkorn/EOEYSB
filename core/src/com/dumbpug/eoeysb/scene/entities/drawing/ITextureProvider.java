package com.dumbpug.eoeysb.scene.entities.drawing;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Provider of entity textures.
 */
public interface ITextureProvider {

    /**
     * Get the entity texture.
     * @return The entity texture.
     */
    public TextureRegion getTexture();
}
