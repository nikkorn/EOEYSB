package com.dumbpug.eoeysb.scene;

import com.badlogic.gdx.Gdx;
import com.dumbpug.eoeysb.Input.InputManager;
import com.dumbpug.eoeysb.tools.Column;
import com.dumbpug.eoeysb.tools.Pane;
import com.dumbpug.eoeysb.tools.Button;

/**
 * The pane overlaying the scene into which the thruster buttons are injected.
 */
public class ThrusterControlPane extends Pane {
    /**
     * The thruster buttons.
     */
    Button leftThrusterButton, rightThrusterButton;

    /**
     * Create a new instance of the ThrusterControlPane class.
     * @param inputManager The input manager.
     */
    public ThrusterControlPane(InputManager inputManager) {
        this.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.setPosition(0, 0);

        com.dumbpug.eoeysb.tools.Row thrusterButtonPaneRow = new com.dumbpug.eoeysb.tools.Row();
        thrusterButtonPaneRow.rowHeight = "*";
        this.addRow(thrusterButtonPaneRow, 1);

        Column leftThrusterColumn = new Column();
        leftThrusterColumn.columnWidth = "50%";

        Column rightThrusterColumn = new Column();
        rightThrusterColumn.columnWidth = "50%";

        thrusterButtonPaneRow.addColumn(leftThrusterColumn, 1);
        thrusterButtonPaneRow.addColumn(rightThrusterColumn, 2);

        leftThrusterButton = new com.dumbpug.eoeysb.tools.Button();
        leftThrusterButton.setActiveAreaOnSprite(false);
        leftThrusterButton.setStretchToColumn(true);

        rightThrusterButton = new com.dumbpug.eoeysb.tools.Button();
        rightThrusterButton.setActiveAreaOnSprite(false);
        rightThrusterButton.setStretchToColumn(true);

        leftThrusterColumn.setControl(leftThrusterButton, Column.ControlOrientation.CENTER);
        rightThrusterColumn.setControl(rightThrusterButton, Column.ControlOrientation.CENTER);

        this.organise();

        inputManager.addControl(leftThrusterButton);
        inputManager.addControl(rightThrusterButton);

        Gdx.input.setInputProcessor(inputManager);
    }

    /**
     * Get whether the left thruster button is pressed.
     * @return Whether the left thruster button is pressed.
     */
    public boolean isLeftThrusterButtonPressed() {
        return this.leftThrusterButton.isButtonPressed();
    }

    /**
     * Get whether the right thruster button is pressed.
     * @return Whether the right thruster button is pressed.
     */
    public boolean isRightThrusterButtonPressed() {
        return this.rightThrusterButton.isButtonPressed();
    }
}
