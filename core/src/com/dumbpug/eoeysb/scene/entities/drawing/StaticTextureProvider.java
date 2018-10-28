package com.dumbpug.eoeysb.scene.entities.drawing;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Provider of a static texture.
 */
public class StaticTextureProvider implements ITextureProvider {
    /**
     * The texture to use.
     */
    private TextureRegion texture;

    /**
     * Create a new instance of the StaticTextureProvider class.
     * @param texture The texture to use.
     */
    public StaticTextureProvider(Texture texture) {
        this.texture = new TextureRegion(texture);
    }

    @Override
    public TextureRegion getTexture() {
        return this.texture;
    }
}
