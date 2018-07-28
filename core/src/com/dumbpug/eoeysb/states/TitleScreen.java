package com.dumbpug.eoeysb.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.eoeysb.Input.InputManager;
import com.dumbpug.eoeysb.scene.clouds.CloudGenerator;
import com.dumbpug.eoeysb.tools.Button;
import com.dumbpug.eoeysb.tools.Column;
import com.dumbpug.eoeysb.tools.Column.ControlOrientation;
import com.dumbpug.eoeysb.tools.Pane;
import com.dumbpug.eoeysb.tools.Row;

public class TitleScreen extends State {
	Pane pane;
	SpriteBatch batch;
	Sprite titleBackground;
    Sprite title;
    Sprite dumbpugTitle;

	Button playButton;
    Button infoButton;
	
	CloudGenerator cloudGenerator;
	
	@Override
	public void stateLoad() {
		// Make a new SpriteBatch.
		batch = new SpriteBatch();
		
		// Set title sprite.
        title = new Sprite(new Texture(Gdx.files.internal("MiscImages/eoeysb_title.png")));
        title.setSize((int) Gdx.graphics.getWidth(), (int) (Gdx.graphics.getWidth() / 3));
        title.setPosition(0, (int) (Gdx.graphics.getHeight()*0.7));

        // Set owner sprite.
        dumbpugTitle = new Sprite(new Texture(Gdx.files.internal("MiscImages/dumbpug_title.png")));
        dumbpugTitle.setSize((int) Gdx.graphics.getWidth(), (int) (Gdx.graphics.getWidth() / 11));
        dumbpugTitle.setPosition(0, dumbpugTitle.getHeight());

        // Set background sprite.
        titleBackground = new Sprite(new Texture(Gdx.files.internal("MiscImages/sky_blue.png")));
        titleBackground.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        titleBackground.setPosition(0, 0);

		// Set up our play button control
		playButton = new Button();
		playButton.setControlSprite(new Sprite(new Texture(Gdx.files.internal("Button/btn_play_up.png"))), true);
		playButton.setActiveSprite(new Sprite(new Texture(Gdx.files.internal("Button/btn_play_down.png"))));
		// Stretch the control area to match whatever column it is in.
		playButton.setStretchToColumn(true);
		playButton.setKeepAspectRatio(true);
		// we only want to active a button press when pushing on the sprite, not the control area.
		playButton.setActiveAreaOnSprite(true);

        // Set up our info button control
        infoButton = new Button();
        infoButton.setControlSprite(new Sprite(new Texture(Gdx.files.internal("Button/btn_info_up.png"))), true);
        infoButton.setActiveSprite(new Sprite(new Texture(Gdx.files.internal("Button/btn_info_down.png"))));
        // Stretch the control area to match whatever column it is in.
        infoButton.setStretchToColumn(true);
        infoButton.setKeepAspectRatio(true);
        // we only want to active a button press when pushing on the sprite, not the control area.
        infoButton.setActiveAreaOnSprite(true);
		
		// Make a new InputManager for title screen!
		InputManager inputManager = new InputManager();
		
		// Set the new InputManager as the Input Processor
		Gdx.input.setInputProcessor(inputManager);
		
		// Add our test button control to the input manager.
		inputManager.addControl(playButton);
        inputManager.addControl(infoButton);
		
		cloudGenerator = new CloudGenerator();
		
		// When we load the title screen we want clouds already generated. 
		for(int i = 0; i < 1000; i++){
			cloudGenerator.generateCloud();
			cloudGenerator.moveClouds(0);
		}
		
		// Make button pane
		pane = new Pane();
		// Make pane as big as display
		pane.setSize((int) (Gdx.graphics.getWidth()*0.7), (int) (Gdx.graphics.getHeight()*0.2));
		pane.setPosition((int) ((Gdx.graphics.getWidth()*0.3)/2), (int) (Gdx.graphics.getHeight()*0.12));

		// Define Rows/Columns
		Row row1 = new Row();
		row1.rowHeight = "*";

        Column column1 = new Column();
        column1.setControl(playButton, ControlOrientation.CENTER);
        column1.columnWidth = "*";

		Row row2 = new Row();
		row2.rowHeight = "5%";
		
		Row row3 = new Row();
		row3.rowHeight = "*";

        Column column2 = new Column();
        column2.setControl(infoButton, ControlOrientation.CENTER);
        column2.columnWidth = "*";

		row1.addColumn(column1, 1);
		row3.addColumn(column2, 1);

		pane.addRow(row1, 1);
		pane.addRow(row2, 2);
		pane.addRow(row3, 3);

		// Organise and layout the pane to reflect everything we've configured for it.
		pane.organise();
	}

	@Override
	public void renderState() {
		// Generate some pretty clouds.
		cloudGenerator.generateCloud();
        // Move the clouds we have
        cloudGenerator.moveClouds(-1);
		// Check button presses.
		if(playButton.isButtonReleased()){
			markStateChange(com.dumbpug.eoeysb.states.StateManager.STATE_GAME, true, true);
		}
		if(infoButton.isButtonReleased()){
			markStateChange(com.dumbpug.eoeysb.states.StateManager.STATE_INFO_CONTROLS, true, true);
		}
		// Draw all elements.
		batch.begin();
		titleBackground.draw(batch);
		cloudGenerator.drawCloudsInBackground(batch);
        title.draw(batch);
		pane.drawPane(batch);
        dumbpugTitle.draw(batch);
		cloudGenerator.drawCloudsInForeground(batch);
		batch.end();
	}

	@Override
	public void renderStateLoading() {
		// Render nothing until Logo is loaded	
	}
}