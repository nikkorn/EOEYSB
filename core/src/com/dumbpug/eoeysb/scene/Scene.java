package com.dumbpug.eoeysb.scene;

import com.dumbpug.eoeysb.Constants;
import com.dumbpug.eoeysb.panel.ResultPanel;
import com.dumbpug.eoeysb.scene.clouds.CloudGenerator;
import com.dumbpug.eoeysb.scene.entities.Entities;
import com.dumbpug.eoeysb.scene.jetpack.JetPack;

/**
 * The game scene, containing sections and entities.
 */
public class Scene {
    /**
     * The jetpack.
     */
    private JetPack jetpack = new JetPack();
    /**
     * The collection of all entities in the scene.
     */
    private Entities entities = new Entities();
    /**
     * The cloud generator.
     */
    private CloudGenerator cloudGenerator = new CloudGenerator();
    /**
     * The result panel.
     */
    private ResultPanel resultPanel;
    /**
     * The player height.
     */
    private float playerHeight = 0f;
    /**
     * Whether the game is still in progress.
     */
    private boolean inProgress = true;

    /**
     * Create a new instance of the Scene class.
     * @param resultPanel The result panel.
     */
    public Scene(ResultPanel resultPanel) { this.resultPanel = resultPanel; }

    /**
     * Get the jetpack.
     * @return The jetpack;
     */
    public JetPack getJetpack() { return this.jetpack; }

    /**
     * Get the entities.
     * @return The entities.
     */
    public Entities getEntities() {
        return this.entities;
    }

    /**
     * Get the cloud generator.
     * @return The cloud generator.
     */
    public CloudGenerator getCloudGenerator() { return this.cloudGenerator; }

    /**
     * Get the current player height in meters.
     * @return The current player height in meters.
     */
    public float getCurrentHeight() { return (int)(this.playerHeight / Constants.METER); }

    /**
     * Get the greatest height reached by the player in meters.
     * @return The greatest height reached by the player in meters.
     */
    public float getGreatestHeight() {
        return (this.playerHeight + jetpack.getHighestWindowedHeight()) / Constants.METER;
    }

    /**
     * Get the player score.
     * @return The player score.
     */
    public int getScore() { return (int)getGreatestHeight(); }

    /**
     * Update the scene.
     * @param isLeftThrusterButtonPressed Whether the left thruster button is being pressed.
     * @param isRightThrusterButtonPressed Whether the right thruster button is being pressed.
     */
    public void update(boolean isLeftThrusterButtonPressed, boolean isRightThrusterButtonPressed) {
        // Store the amount of distance we move up vertically as part of this update.
        float verticalClimb = 0;

        // Is the round still in progress?
        if(inProgress) {
            // Move our jetpack and get the amount we need to essentially move the scene down by.
            verticalClimb = jetpack.move(isLeftThrusterButtonPressed, isRightThrusterButtonPressed);

            // Move up screen.
            playerHeight += verticalClimb;

            // TODO Only generate clouds at a certain height
            cloudGenerator.generateCloud();

            // Move the clouds both horizontally (will always move in this way) and vertically
            // depending on whether the jetpack has moved up.
            cloudGenerator.moveClouds(-verticalClimb);

            // Have we lost the game by dropping out?
            if(jetpack.hasDroppedOut()) {
                // We have lost the game!
                this.inProgress = false;
            }
        } else {
            // The user has lost! Only bother with logic that steps non interactive elements like clouds, birds, asteroids etc.
            // Display the results page if it is hidden.
            if(!resultPanel.isShowing()) {
                int highScore           = com.dumbpug.eoeysb.Game.getHighScore();
                int score               = getScore();
                boolean gotNewHighScore = score > highScore;
                if(gotNewHighScore) {
                    com.dumbpug.eoeysb.Game.setHighScore(score);
                    highScore = score;
                }
                resultPanel.setScores(score, highScore, gotNewHighScore);
                resultPanel.show();
            }

            // TODO Only generate clouds at a certain height
            cloudGenerator.generateCloud();

            // Move the clouds both horizontally (will always move in this way) and vertically
            // depending on whether the jetpack has moved up.
            cloudGenerator.moveClouds(-verticalClimb);
        }

        // Update the entities in the scene.
        // entities.update(this.getGreatestHeight(), -verticalClimb);

        // Check for any cases where entities in the scene have collided with the jetpack.
        jetpack.checkForEntityCollisions(this.entities.getAll());
    }
}
