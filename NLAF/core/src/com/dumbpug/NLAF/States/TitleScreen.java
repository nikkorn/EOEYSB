package com.dumbpug.NLAF.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dumbpug.NLAF.Input.InputManager;
import com.dumbpug.NLAF.Tools.Button;
import com.dumbpug.NLAF.Tools.Column;
import com.dumbpug.NLAF.Tools.Column.ControlOrientation;
import com.dumbpug.NLAF.Tools.Pane;
import com.dumbpug.NLAF.Tools.PaneElementBorder;
import com.dumbpug.NLAF.Tools.Row;
import com.dumbpug.NLAF.Tools.Animation;

public class TitleScreen extends State {
	Pane pane;
	SpriteBatch batch;
	
	Animation runningTestAnimation;
	TextureRegion currentRunningFrame = null;
	
	Button testButton;
	
	@Override
	public void stateLoad() {
		// Make a new SpriteBatch.
		batch = new SpriteBatch();
		
		// Set up our test button
		testButton = new Button();
		testButton.setControlSprite(new Sprite(new Texture(Gdx.files.internal("Button/inactivebtn.png"))), true);
		testButton.setActiveSprite(new Sprite(new Texture(Gdx.files.internal("Button/activebtn.png"))));
		// Stretch the control area to match whatever column it is in.
		testButton.setStretchToColumn(true);
		// we only want to active a button press when pushing on the sprite, not the control area.
		testButton.setActiveAreaOnSprite(true);
		
		// Test making a new InputManager!
		InputManager inputManager = new InputManager();
		
		// Set the new InputManager as the Input Processor
		Gdx.input.setInputProcessor(inputManager);
		
		// Add our test button control to the input manager.
		inputManager.addControl(testButton);
		
		// Instantiate a test animation
		runningTestAnimation = new Animation(new Texture(Gdx.files.internal("MiscImages/animation_sheet.png")), 5, 6, (float) 1/30);
		
		// Test making a pane
		pane = new Pane();
		pane.setSize(200, 400);
		pane.setPosition((Gdx.graphics.getWidth()/2) - (pane.getWidth()/2), (Gdx.graphics.getHeight()/2) - (pane.getHeight()/2));	
		pane.setBackgroundAnimation(runningTestAnimation);
		
		// Create a border
		PaneElementBorder paneBorder = new PaneElementBorder();
		paneBorder.setBorderType("WarningBorder");
		paneBorder.setBorderThickness(6f);
		// Add the border to the pane
		pane.setBorder(paneBorder);
		
		// Define Rows/Columns
		Row row1 = new Row();
		row1.rowHeight = "*";
		
		Column column1 = new Column();
		column1.setControl(testButton, ControlOrientation.CENTER);
		column1.columnWidth = "*";
		
		row1.addColumn(column1, 1);
	
		pane.addRow(row1, 1);
		
		// Organise and layout the pane to reflect everything we've configured for it.
		pane.organise();
	}

	@Override
	public void renderState() {
		// Close on test button press
		if(testButton.isButtonReleased()){
			Gdx.app.exit();
		} 
		
		batch.begin();
		pane.drawPane(batch);
		batch.end();
	}

	@Override
	public void renderStateLoading() {
		// Render nothing until Logo is loaded	
	}
}
