package com.dumbpug.eoeysb.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FadeOut extends Transition {
	private Sprite BlankSprite;
	private SpriteBatch batch;
	
	float fadeCount = 0;
	float fadeIncrement = 0.01f;

	@Override
	public void Render() {
		// Increment the alpha value
		fadeCount += fadeIncrement;
		// If our screen is completely black then the transition has finished
		if(fadeCount > 1f){
			fadeCount = 1f;
			markAsComplete();
		}
		// Set the transparency
		BlankSprite.setAlpha(fadeCount);
		// Draw the overlay
		batch.begin();
		BlankSprite.draw(batch);
		batch.end();
	}

	@Override
	public void onLoad() {
		batch = new SpriteBatch();
		BlankSprite = new Sprite(new Texture(Gdx.files.internal("TransitionResources/blank.png")));
		BlankSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		BlankSprite.setPosition(0,0);
	}

}
