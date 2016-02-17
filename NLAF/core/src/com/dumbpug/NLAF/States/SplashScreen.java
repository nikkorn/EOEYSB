package com.dumbpug.NLAF.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.NLAF.Main.Constants;
import com.dumbpug.NLAF.Transitions.FadeOut;

public class SplashScreen extends State {
	private Sprite dumbPugSprite;
	private SpriteBatch batch;
	
	FadeOut fadeOutTransition;

	private long stateStartTime;
	private long SPLASH_DELAY_TIME = 4000;

	@Override
	public void stateLoad() {
		stateStartTime = System.currentTimeMillis();
		Texture tx_dumbPugSprite = new Texture(Gdx.files.internal("MiscImages/dumbpug.png"));
		dumbPugSprite = new Sprite(tx_dumbPugSprite);
		dumbPugSprite.setOrigin(dumbPugSprite.getWidth() / 2,
				dumbPugSprite.getHeight() / 2);
		dumbPugSprite.setSize(
				(Gdx.graphics.getHeight() * Constants.LOGO_SCALE) * 1.5f,
				(Gdx.graphics.getHeight() * Constants.LOGO_SCALE));
		dumbPugSprite.setPosition((Gdx.graphics.getWidth() / 2)
				- (dumbPugSprite.getWidth() / 2),
				(Gdx.graphics.getHeight() / 2)
						- (dumbPugSprite.getHeight() / 2));
		batch = new SpriteBatch();
		
		fadeOutTransition = new FadeOut(); 
		fadeOutTransition.onLoad();
	}

	@Override
	public void renderState() {
		// Draw our logo
		batch.begin();
		dumbPugSprite.draw(batch);
		batch.end();
		// We have been rendering our splash screen for long enough, move to the title screen
		if ((System.currentTimeMillis() - stateStartTime) > SPLASH_DELAY_TIME) {
			// Fade out
			fadeOutTransition.Render();
			// Check if transition has finished, if so then move to other state
			if(fadeOutTransition.isCompleted()){
				markStateChange(StateManager.STATE_EYSB_TITLE, true, true);
			}
		}
	}

	@Override
	public void renderStateLoading() {
		// Render nothing until Logo is loaded
	}
}
