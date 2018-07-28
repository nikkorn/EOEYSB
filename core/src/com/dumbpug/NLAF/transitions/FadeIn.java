package com.dumbpug.NLAF.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FadeIn extends Transition {
	private Sprite BlankSprite;
	private SpriteBatch batch;
	
	float fadeCount = 1f;
	float fadeIncrement = 0.01f;

	@Override
	public void Render() {
		// Increment the alpha value
		fadeCount -= fadeIncrement;
		// If our screen is completely black then the transition has finished
		if(fadeCount <= 0f){
			fadeCount = 0f;
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
