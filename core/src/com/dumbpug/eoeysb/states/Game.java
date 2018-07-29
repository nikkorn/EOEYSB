package com.dumbpug.eoeysb.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.eoeysb.Constants;
import com.dumbpug.eoeysb.Input.InputManager;
import com.dumbpug.eoeysb.panel.ResultPanel;
import com.dumbpug.eoeysb.scene.Scene;
import com.dumbpug.eoeysb.scene.SceneDrawer;
import com.dumbpug.eoeysb.scene.ThrusterControlPane;

/**
 * The main game state.
 */
public class Game extends State {
	/**
	 * The game scene.
	 */
	private Scene scene;
	/**
	 * The drawer of the game scene.
	 */
	private SceneDrawer sceneDrawer;
	/**
	 * The thruster control pane.
	 */
	private ThrusterControlPane thrusterControlPane;
	/**
	 * The results panel displayed on dropping out.
	 */
	private ResultPanel resultPanel;
	/**
	 * The sprite batch to use in drawing the game.
	 */
	private SpriteBatch batch;
	/**
	 * The main input manager for this state.
	 */
	private InputManager inputManager;
	
	@Override
	public void stateLoad() {
		// Create a new SpriteBatch.
		batch = new SpriteBatch();
		// Create a new input manager for this state.
		inputManager = new InputManager();
		// Create the thruster control pane.
		this.thrusterControlPane = new ThrusterControlPane(inputManager);
		// Create the result panel.
		this.resultPanel = new ResultPanel();
		// Create the game scene.
		this.scene = new Scene(this.resultPanel);
		// Create the scene drawer.
		this.sceneDrawer = new SceneDrawer();
	}

	@Override
	public void renderState() {
		// Update the scene.
		this.scene.update(this.thrusterControlPane.isLeftThrusterButtonPressed(), this.thrusterControlPane.isRightThrusterButtonPressed());
		// Is the result pane showing?
		if (resultPanel.isShowing()) {
			// Check to see if the user has requested to Retry/Exit.
			if(resultPanel.exitRequested()){
				// Go to title.
				markStateChange(StateManager.STATE_TITLE, true, true);
			} else if(resultPanel.retryRequested()) {
				// User wants to retry, get a fresh state.
				markStateChange(StateManager.STATE_GAME, true, true);
			}
		}
		// Draw the scene, overlaid with the results panel if it is showing.
		this.sceneDrawer.draw(this.batch, this.scene, this.resultPanel);
	}

	@Override
	public void renderStateLoading() {}

	public int getScore() {
		return (int) ((this.scene.getPlayerHeight() + this.scene.getJetpack().getHighestWindowedScore()) / Constants.METER);
	}
}
