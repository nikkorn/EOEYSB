package com.dumbpug.eoeysb.scene.entities.drawing;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dumbpug.eoeysb.tools.Animation;

/**
 * Provider of textures from animations.
 */
public class AnimationTextureProvider implements ITextureProvider {
    /**
     * The animation form which to get textures.
     */
    private Animation animation;

    /**
     * Create a new instance of the AnimationTextureProvider class.
     * @param animation The animation form which to get textures.
     */
    public AnimationTextureProvider(Animation animation) {
        this.animation = animation;
    }

    @Override
    public TextureRegion getTexture() {
        return animation.getCurrentFrame(true);
    }
}
