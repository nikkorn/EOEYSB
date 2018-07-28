package com.dumbpug.eoeysb.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dumbpug.eoeysb.Constants;
import com.dumbpug.eoeysb.Input.InputManager;
import com.dumbpug.eoeysb.scene.Scene;
import com.dumbpug.eoeysb.scene.clouds.CloudGenerator;
import com.dumbpug.eoeysb.scene.entities.EntityManager;
import com.dumbpug.eoeysb.scene.hud.FuelTankMeters;
import com.dumbpug.eoeysb.scene.hud.HeightCounter;
import com.dumbpug.eoeysb.panel.ResultPanel;
import com.dumbpug.eoeysb.tools.Column;
import com.dumbpug.eoeysb.tools.Column.ControlOrientation;

/**
 * The main game state.
 */
public class Game extends State {
	/**
	 * The game scene.
	 */
	Scene scene;

	com.dumbpug.eoeysb.tools.Pane thrusterButtonPane;
	com.dumbpug.eoeysb.tools.Button rightThruster;
	com.dumbpug.eoeysb.tools.Button leftThruster;
	SpriteBatch batch;
	// World Sprites/Animations
	Sprite fullSkyBackground;
	Sprite blueSkyBase;
	Sprite grassBase;
	Sprite launcherBase;
    Sprite launcherPromptBackground;
	com.dumbpug.eoeysb.tools.Animation mountainRange;
	com.dumbpug.eoeysb.tools.Animation launcherPrompt;
	// Main input manager for this state
	com.dumbpug.eoeysb.Input.InputManager inputManager;
	// Our cloud generator
	CloudGenerator cloudGenerator;
	// Our height (score) counter.
	HeightCounter heightCounter;
	// Our Fuel Tank HUD drawer.
	FuelTankMeters fuelTankHUD;
	// Our results pane.
    ResultPanel resultPanel;
	// Our EntityManager.
    EntityManager entityManager;
    // The loosest interpretation of our height in game.
	private float testVertViewHeight = 0f;
    // Is the game in progress, this is false when the user loses.
    private boolean inProgress = true;
	// Multipliers for asset vertical movement.
	float grassBaseMovementVertMultiplier = 1f;
	float mountainRangeMovementVertMultiplier = 0.5f;
	float blueSkyBaseMovementVertMultiplier = 0.2f;
	float fullSkyMovementVertMultiplier = Constants.MOVEMENT_UNIT*0.0025f;
	// Aspect Ratio of mountain range animation
	float mountainRangeAspectRatio = 1.40909f;
	
	@Override
	public void stateLoad() {
		// Make a new SpriteBatch.
		batch = new SpriteBatch();
		
		// Make a new inputmanager for this state.
		inputManager = new InputManager();
		
		// Set background sprite.
		fullSkyBackground = new Sprite(new Texture(Gdx.files.internal("BackDrops/sky_full.png")));
		float skyAR = fullSkyBackground.getHeight()/fullSkyBackground.getWidth();
		fullSkyBackground.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth() * skyAR);
		fullSkyBackground.setPosition(0, 0);
		
		// Set sky base sprite.
		blueSkyBase = new Sprite(new Texture(Gdx.files.internal("BackDrops/sky_base.png")));
		blueSkyBase.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		blueSkyBase.setPosition(0, 0);
		
		// Set mountain range animation.
		mountainRange = new com.dumbpug.eoeysb.tools.Animation(new Texture(Gdx.files.internal("BackDrops/Mountain_Sea_AnimationSheet2.png")), 1, 2, (float) 1/1f);

        // Set launcher prompt animation.
        launcherPrompt = new com.dumbpug.eoeysb.tools.Animation(new Texture(Gdx.files.internal("MiscImages/launch_prompt.png")), 1, 2, (float) 1/1f);

        // Set launcher prompt background.
        launcherPromptBackground = new Sprite(new Texture(Gdx.files.internal("Panel/pnl_part_c.png")));
        launcherPromptBackground.setAlpha(0.7f);
        launcherPromptBackground.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        launcherPromptBackground.setPosition(0, 0);
		
		// Set grass sprite.
		grassBase = new Sprite(new Texture(Gdx.files.internal("MiscImages/grass.png")));
		float grassBaseAR = grassBase.getHeight()/grassBase.getWidth();
		grassBase.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth() * grassBaseAR);
		grassBase.setPosition(0, 0);

		// Set the launcher sprite.
		launcherBase = new Sprite(new Texture(Gdx.files.internal("MiscImages/launcher.png")));
		int launcherBaseWidth = (int) (Gdx.graphics.getWidth()/1.06f);
		launcherBase.setSize(launcherBaseWidth, launcherBaseWidth / 2);
		launcherBase.setPosition((Gdx.graphics.getWidth()/2)-(launcherBase.getWidth()/2), (grassBase.getHeight()-(launcherBase.getHeight()/2)));

		// Create various game entities.
		cloudGenerator = new CloudGenerator();
		heightCounter = new HeightCounter();
		fuelTankHUD = new FuelTankMeters();
        resultPanel = new ResultPanel();
        entityManager = new com.dumbpug.eoeysb.scene.entities.EntityManager();
		setupThrusterControlPane();
		// Create the game scene.
		this.scene = new Scene();
	}

	@Override
	public void renderState() {
        float vertMovement = 0;
        // Is the round still in progress?
        if(inProgress) {
            // Move our jetpack and get the amount we need to essentially move the scene down by.
            vertMovement = this.scene.getJetpack().move(leftThruster.isButtonPressed(), rightThruster.isButtonPressed());
            // Move up screen.
            testVertViewHeight += vertMovement;
            // Only generate clouds at a certain height
            if(testVertViewHeight > (Gdx.graphics.getHeight()*1.5) && getSkyHeightPercentage() < 50){
                cloudGenerator.generateCloud();
            }
            // Move the clouds both horizontally (will always move in this way) and vertically
            // depending on whether the jetpack has moved up.
            cloudGenerator.moveClouds(-vertMovement);
            // Set the position of our world elements.
            blueSkyBase.setPosition(blueSkyBase.getX(), -(testVertViewHeight * blueSkyBaseMovementVertMultiplier));
            grassBase.setPosition(grassBase.getX(), -(testVertViewHeight * grassBaseMovementVertMultiplier));
            launcherBase.setPosition(launcherBase.getX(), (grassBase.getHeight()-(launcherBase.getHeight()/1.35f))-(testVertViewHeight * grassBaseMovementVertMultiplier));
            // Set the position of the blue sky/black space background based on our height.
            if(((testVertViewHeight * fullSkyMovementVertMultiplier) + Gdx.graphics.getHeight()) < fullSkyBackground.getHeight()){
                fullSkyBackground.setPosition(fullSkyBackground.getX(), -(testVertViewHeight * fullSkyMovementVertMultiplier));
            } else {
                fullSkyBackground.setPosition(fullSkyBackground.getX(), -(fullSkyBackground.getHeight() - Gdx.graphics.getHeight()));
            }
            // Have we dropped of the bottom of the screen?!?
            if(this.scene.getJetpack().hasDroppedOut()) {
                // We have lost the game!
                this.inProgress = false;
            }
        } else {
            // The user has lost! Only bother with logic that steps non interactive elements like clouds, birds, asteroids etc.
            // Display the results page if it is hidden.
            if(!resultPanel.isShowing()) {
                int highScore = com.dumbpug.eoeysb.Game.getHighScore();
                int score = getScore();
                boolean gotNewHighScore = score > highScore;
                if(gotNewHighScore) {
                    com.dumbpug.eoeysb.Game.setHighScore(score);
                    highScore = score;
                }
                resultPanel.setScores(score, highScore, gotNewHighScore);
                resultPanel.show();
            }
            // Check to see if the user has requested to Retry/Exit.
            if(resultPanel.exitRequested()){
                // Go to title.
                markStateChange(StateManager.STATE_TITLE, true, true);
            } else if(resultPanel.retryRequested()) {
                // User wants to retry, get a fresh state.
                markStateChange(StateManager.STATE_GAME, true, true);
            }
            // Only generate clouds at a certain height
            if(testVertViewHeight > (Gdx.graphics.getHeight()*1.5) && getSkyHeightPercentage() < 50){
                cloudGenerator.generateCloud();
            }
            // Move the clouds both horizontally (will always move in this way) and vertically
            // depending on whether the jetpack has moved up.
            cloudGenerator.moveClouds(-vertMovement);
        }

        // Attempt to generate some new entities.
        entityManager.generate(this.getSkyHeightPercentage());

        // Move all active entities.
        entityManager.moveEntities(-vertMovement);

		// Do our collision detection.
		entityManager.checkCollisions(this.scene.getJetpack());

		// Draw the current game!
        drawState();

        //System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());
		System.out.println("HEIGHT%: " + this.getSkyHeightPercentage());
	}

    /**
     * Draw the state of the game.
     */
	public void drawState() {
		// Get the current frame of our mountain-range animation.
		TextureRegion currentMountainFrame = mountainRange.getCurrentFrame(true);
        TextureRegion currentLaunchPromptFrame = null;
        if(!this.scene.getJetpack().hasLaunched()) {
            currentLaunchPromptFrame = launcherPrompt.getCurrentFrame(true);
        }
		batch.begin();
		fullSkyBackground.draw(batch);
		blueSkyBase.draw(batch);
		batch.draw(currentMountainFrame, 0, 0 - (mountainRangeMovementVertMultiplier * testVertViewHeight), Gdx.graphics.getWidth(), Gdx.graphics.getWidth() * mountainRangeAspectRatio);
		grassBase.draw(batch);
		launcherBase.draw(batch);
		cloudGenerator.drawCloudsInBackground(batch);
        entityManager.draw(batch);
		this.scene.getJetpack().draw(batch);
		cloudGenerator.drawCloudsInForeground(batch);
        heightCounter.drawScore(batch, getScore(), false);
		fuelTankHUD.drawFuelTankHud(batch, this.scene.getJetpack().getFuelLevelPercentageLeft(), this.scene.getJetpack().getFuelLevelPercentageRight());
        // Draw our launch prompt if the user hasnt launched yet.
        if(!this.scene.getJetpack().hasLaunched()) {
            launcherPromptBackground.draw(batch);
            batch.draw(currentLaunchPromptFrame, 0, (Gdx.graphics.getHeight()/2)-(Gdx.graphics.getWidth()/2), Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
        }
        // Draw our results panel if it is showing.
        if(resultPanel.isShowing()) {
            resultPanel.drawPane(batch);
        }
        batch.end();
	}

	@Override
	public void renderStateLoading() {}
	
	/**
	 * Sets up the thruster control pane.
	 */
	public void setupThrusterControlPane(){
		thrusterButtonPane = new com.dumbpug.eoeysb.tools.Pane();
		thrusterButtonPane.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		thrusterButtonPane.setPosition(0, 0);
		
		com.dumbpug.eoeysb.tools.Row thrusterButtonPaneRow = new com.dumbpug.eoeysb.tools.Row();
		thrusterButtonPaneRow.rowHeight = "*";
		thrusterButtonPane.addRow(thrusterButtonPaneRow, 1);
		
		Column leftThrusterColumn = new Column(); 
		leftThrusterColumn.columnWidth = "50%";
		
		Column rightThrusterColumn = new Column(); 
		rightThrusterColumn.columnWidth = "50%";
		
		thrusterButtonPaneRow.addColumn(leftThrusterColumn, 1);
		thrusterButtonPaneRow.addColumn(rightThrusterColumn, 2);
		
		leftThruster = new com.dumbpug.eoeysb.tools.Button();
		leftThruster.setActiveAreaOnSprite(false);
		leftThruster.setStretchToColumn(true);
		
		rightThruster = new com.dumbpug.eoeysb.tools.Button();
		rightThruster.setActiveAreaOnSprite(false);
		rightThruster.setStretchToColumn(true);
		
		leftThrusterColumn.setControl(leftThruster, ControlOrientation.CENTER);
		rightThrusterColumn.setControl(rightThruster, ControlOrientation.CENTER);
		
		thrusterButtonPane.organise();
		
		com.dumbpug.eoeysb.Input.InputManager inputManager = new com.dumbpug.eoeysb.Input.InputManager();
		
		inputManager.addControl(leftThruster);
		inputManager.addControl(rightThruster);
		
		Gdx.input.setInputProcessor(inputManager);
	}
	
	public int getSkyHeightPercentage(){
		return (int) ((fullSkyBackground.getY() / -(fullSkyBackground.getHeight() - Gdx.graphics.getHeight())) * 100);
	}

	public int getScore() {
		return (int) ((testVertViewHeight + this.scene.getJetpack().getHighestWindowedScore())/Constants.METER);
	}
}
