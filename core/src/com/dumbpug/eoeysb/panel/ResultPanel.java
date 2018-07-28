package com.dumbpug.eoeysb.panel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.eoeysb.scene.hud.HeightCounter;

/**
 * Created by Nikolas Howard.
 * The dialog that shows when the user loses the game.
 */
public class ResultPanel extends com.dumbpug.eoeysb.tools.Pane {
    // Is this dialog currently showing and active?
    private boolean isShowing = false;
    // The values that our dialog will display.
    private int score = 0;
    private int highScore = 0;
    private boolean hasNewHighScore = false;
    // Our dialog buttons
    private com.dumbpug.eoeysb.tools.Button retryButton;
    private com.dumbpug.eoeysb.tools.Button exitButton;
    // Reference to the owning states InputManager.
    private com.dumbpug.eoeysb.Input.InputManager currentInputManager;
    // Our HeightCounter that we use to draw scores.
    private HeightCounter scoreHeightCounter;
    private int scorePositionX;
    private int scorePositionY;
    private int highScorePositionY;

    public ResultPanel() {
        // Size this panel.
        int panelDimUnit = (int) (Gdx.graphics.getWidth()*0.20);
        this.setSize(panelDimUnit*4, panelDimUnit*3);

        // Position this panel.
        int panelPosY = (int) ((Gdx.graphics.getHeight()/2) - (this.getHeight()/2));
        int panelPosX = (int) (Gdx.graphics.getWidth()*0.10);
        this.setPosition(panelPosX, panelPosY);

        // Initialise our retry button.
        retryButton = new com.dumbpug.eoeysb.tools.Button();
        retryButton.setControlSprite(new Sprite(new Texture(Gdx.files.internal("Button/btn_retry_up.png"))), true);
        retryButton.setActiveSprite(new Sprite(new Texture(Gdx.files.internal("Button/btn_retry_down.png"))));
        // Stretch the control area to match whatever column it is in.
        retryButton.setStretchToColumn(true);
        retryButton.setKeepAspectRatio(true);
        // we only want to active a button press when pushing on the sprite, not the control area.
        retryButton.setActiveAreaOnSprite(true);

        // Initialise our exit button.
        exitButton = new com.dumbpug.eoeysb.tools.Button();
        exitButton.setControlSprite(new Sprite(new Texture(Gdx.files.internal("Button/btn_exit_up.png"))), true);
        exitButton.setActiveSprite(new Sprite(new Texture(Gdx.files.internal("Button/btn_exit_down.png"))));
        // Stretch the control area to match whatever column it is in.
        exitButton.setStretchToColumn(true);
        exitButton.setKeepAspectRatio(true);
        // we only want to active a button press when pushing on the sprite, not the control area.
        exitButton.setActiveAreaOnSprite(true);

        // Add our panel background.
        this.setBackgroundSprite(new Sprite(new Texture(Gdx.files.internal("Panel/pnl_result.png"))));

        // Create panel rows.
        com.dumbpug.eoeysb.tools.Row scoreRow = new com.dumbpug.eoeysb.tools.Row();
        scoreRow.rowHeight = "*";
        com.dumbpug.eoeysb.tools.Row controlRow = new com.dumbpug.eoeysb.tools.Row();
        controlRow.rowHeight = "22%";
        com.dumbpug.eoeysb.tools.Row padRow = new com.dumbpug.eoeysb.tools.Row();
        padRow.rowHeight = "4%";

        // Create control columns
        com.dumbpug.eoeysb.tools.Column retryControlColumn = new com.dumbpug.eoeysb.tools.Column();
        retryControlColumn.setControl(retryButton, com.dumbpug.eoeysb.tools.Column.ControlOrientation.CENTER);
        retryControlColumn.columnWidth = "*";
        com.dumbpug.eoeysb.tools.Column exitControlColumn = new com.dumbpug.eoeysb.tools.Column();
        exitControlColumn.setControl(exitButton, com.dumbpug.eoeysb.tools.Column.ControlOrientation.CENTER);
        exitControlColumn.columnWidth = "*";

        // Finish filling our panel.
        controlRow.addColumn(retryControlColumn, 1);
        controlRow.addColumn(exitControlColumn, 2);
        this.addRow(scoreRow, 1);
        this.addRow(controlRow, 2);
        this.addRow(padRow, 3);
        this.organise();

        // Initialise our HeightCounter for scores.
        scoreHeightCounter      = new HeightCounter((int) (this.getHeight()*0.175));
        this.scorePositionX     = (int) (panelPosX + (this.getWidth()*0.056));
        this.scorePositionY     = (int) (panelPosY + (this.getHeight()*0.625));
        this.highScorePositionY = (int) (panelPosY + (this.getHeight()*0.317));
    }

    /**
     * Show this panel and use a new InputProcessor..
     */
    public void show() {
        this.isShowing = true;
        this.currentInputManager = new com.dumbpug.eoeysb.Input.InputManager();
        // We need to make sure that our InputManager picks up input
        // From our two controls.
        this.currentInputManager.addControl(retryButton);
        this.currentInputManager.addControl(exitButton);
        Gdx.input.setInputProcessor(this.currentInputManager);
    }

    /**
     * Is this panel currently being shown?
     * @return isShowing
     */
    public boolean isShowing() {
        return this.isShowing;
    }

    /**
     * Set the details that will be displayed by the result pane.
     * @param score
     * @param highScore
     * @param newHighScore
     */
    public void setScores(int score, int highScore, boolean newHighScore) {
        this.score = score;
        this.highScore = highScore;
        this.hasNewHighScore = newHighScore;
        // Our panel background will depend on whether we got a high score.
        if(newHighScore){
            this.setBackgroundSprite(new Sprite(new Texture(Gdx.files.internal("Panel/pnl_result_high_score.png"))));
        } else {
            this.setBackgroundSprite(new Sprite(new Texture(Gdx.files.internal("Panel/pnl_result.png"))));
        }
    }

    /**
     * Draw this pane with overlapping scores.
     * @param batch
     */
    @Override
    public void drawPane(SpriteBatch batch){
        // Only draw if this panel is currently showing.
        if(isShowing) {
            super.drawPane(batch);
            // Draw score.
            scoreHeightCounter.drawScore(batch, this.score, this.hasNewHighScore, this.scorePositionX, this.scorePositionY);
            // Draw high score.
            scoreHeightCounter.drawScore(batch, this.highScore, this.hasNewHighScore, this.scorePositionX, this.highScorePositionY);
        }
    }

    /**
     * Has the user pressed the 'Retry' button?
     * @return
     */
    public boolean retryRequested() {
        return retryButton.isButtonReleased();
    }

    /**
     * Has the user pressed the 'Exit' button?
     * @return
     */
    public boolean exitRequested() {
        return exitButton.isButtonReleased();
    }
}
