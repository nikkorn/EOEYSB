package com.dumbpug.NLAF.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dumbpug.NLAF.Input.InputManager;
import com.dumbpug.NLAF.Tools.Animation;
import com.dumbpug.NLAF.Tools.Button;
import com.dumbpug.NLAF.Tools.Column;
import com.dumbpug.NLAF.Tools.Pane;
import com.dumbpug.NLAF.Tools.Row;

/**
 * Created by nik on 17/05/16.
 */
public class eysbInfoFuelPods extends State{
    Pane pane;
    SpriteBatch batch;
    Sprite background;
    Sprite fuelPodSprite;

    Button leftButton;
    Button exitButton;

    // Aspect Ratio of controls animation
    float fuelPodSpriteAspectRatio = 1.0847f;

    @Override
    public void stateLoad() {
        // Make a new SpriteBatch.
        batch = new SpriteBatch();

        // Set background sprite.
        background = new Sprite(new Texture(Gdx.files.internal("Panel/pnl_part_c.png")));
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.setPosition(0, 0);

        // Set fuel pod info sprite.
        fuelPodSprite = new Sprite(new Texture(Gdx.files.internal("MiscImages/catch_fuel_info.png")));
        fuelPodSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth() * fuelPodSpriteAspectRatio);
        int infoSpriteYPos = (int) ((Gdx.graphics.getHeight()/2) - ((Gdx.graphics.getWidth() * fuelPodSpriteAspectRatio)/2));
        fuelPodSprite.setPosition(0, infoSpriteYPos);

        // Set up our left button control
        leftButton = new Button();
        leftButton.setControlSprite(new Sprite(new Texture(Gdx.files.internal("Button/btn_left.png"))), true);
        leftButton.setActiveSprite(new Sprite(new Texture(Gdx.files.internal("Button/btn_left_down.png"))));
        // Stretch the control area to match whatever column it is in.
        leftButton.setStretchToColumn(true);
        leftButton.setKeepAspectRatio(true);
        // we only want to active a button press when pushing on the sprite, not the control area.
        leftButton.setActiveAreaOnSprite(true);

        // Set up our exit button control
        exitButton = new Button();
        exitButton.setControlSprite(new Sprite(new Texture(Gdx.files.internal("Button/btn_exit_up.png"))), true);
        exitButton.setActiveSprite(new Sprite(new Texture(Gdx.files.internal("Button/btn_exit_down.png"))));
        // Stretch the control area to match whatever column it is in.
        exitButton.setStretchToColumn(true);
        exitButton.setKeepAspectRatio(true);
        // we only want to active a button press when pushing on the sprite, not the control area.
        exitButton.setActiveAreaOnSprite(true);

        // Make a new InputManager for title screen!
        InputManager inputManager = new InputManager();

        // Set the new InputManager as the Input Processor
        Gdx.input.setInputProcessor(inputManager);

        // Add our button controls to the input manager.
        inputManager.addControl(leftButton);
        inputManager.addControl(exitButton);

        // Make button pane
        pane = new Pane();

        // Make pane as wide as display but only as high as our button row.
        pane.setSize((int) Gdx.graphics.getWidth(), (int) (Gdx.graphics.getHeight()*0.08));
        pane.setPosition(0, (int) (Gdx.graphics.getHeight()*0.1));

        // Define Rows/Columns
        Row row1 = new Row();
        row1.rowHeight = "*";

        Column column1 = new Column();
        column1.setControl(leftButton, Column.ControlOrientation.CENTER);
        column1.columnWidth = "33%";

        Column column2 = new Column();
        column2.setControl(exitButton, Column.ControlOrientation.CENTER);
        column2.columnWidth = "33%";

        Column column3 = new Column();
        column3.columnWidth = "33%";

        row1.addColumn(column1, 1);
        row1.addColumn(column2, 2);
        row1.addColumn(column3, 3);

        pane.addRow(row1, 1);

        // Organise and layout the pane to reflect everything we've configured for it.
        pane.organise();
    }

    @Override
    public void renderState() {
        // Check if the exit button has been pressed.
        if(exitButton.isButtonReleased()){
            markStateChange(StateManager.STATE_EYSB_TITLE, true, true);
        }
        if(leftButton.isButtonReleased()){
            markStateChange(StateManager.STATE_EYSB_INFO_FUEL_LEVEL, true, true);
        }
        // Draw all elements.
        batch.begin();
        background.draw(batch);
        fuelPodSprite.draw(batch);
        pane.drawPane(batch);
        batch.end();
    }

    @Override
    public void renderStateLoading() {}
}
