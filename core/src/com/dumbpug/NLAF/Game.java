package com.dumbpug.NLAF;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.dumbpug.NLAF.FPSManagement.FPSManager;
import com.dumbpug.NLAF.FPSManagement.Updateable;
import com.dumbpug.NLAF.states.StateManager;

public class Game extends ApplicationAdapter implements Updateable {
	private FPSManager FPSM;
	private StateManager stateManager;
    private static Preferences prefs;
	public static int highScore = 0;

	@Override
	public void create() {
		Gdx.graphics.setVSync(true);
		FPSM = new FPSManager(this, 1 / 60f);
		FPSM.showFPS(false);
		stateManager = new StateManager();
        // Get our old high score (if we have one)
        prefs = Gdx.app.getPreferences("eoeysb_score");
        highScore = prefs.getInteger("score", 0);
		// set the first state to be our splash screen
		stateManager.addState(StateManager.STATE_SPLASH);
		stateManager.setCurrentState(StateManager.STATE_SPLASH);
	}

	// SUMMARY
	// No rendering here, we update the FPSManager which in turn decides
	// whether to call controlledRender
	@Override
	public void render() {
		FPSM.update(Gdx.graphics.getDeltaTime());
	}

	// SUMMARY
	// This is where our game will actually be rendered, it is called via the
	// FPSManager
	@Override
	public void controlledRender() {
		// Render the current state;
		stateManager.renderCurrentState();
	}

    /**
     * Get the high score.
     * @return
     */
    public static int getHighScore() {
        return highScore;
    }

    /**
     * Set the high score.
     * @param score
     */
    public static void setHighScore(int score) {
        highScore = score;
        prefs.putInteger("score", score);
        prefs.flush();
    }
}
