package com.dumbpug.eoeysb.scene;

import com.dumbpug.eoeysb.Constants;
import com.dumbpug.eoeysb.panel.ResultPanel;
import com.dumbpug.eoeysb.scene.clouds.CloudGenerator;
import com.dumbpug.eoeysb.scene.entities.EntityManager;
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
     * The entity manager.
     */
    private EntityManager entityManager = new EntityManager();
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
    public Scene(ResultPanel resultPanel) {
        this.resultPanel = resultPanel;
    }

    /**
     * Get the jetpack.
     * @return The jetpack;
     */
    public JetPack getJetpack() {
        return this.jetpack;
    }

    /**
     * Get the entity manager.
     * @return The entity manager.
     */
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Get the cloud generator.
     * @return The cloud generator.
     */
    public CloudGenerator getCloudGenerator() {
        return this.cloudGenerator;
    }

    /**
     * Get the player height.
     * @return The player height.
     */
    public float getPlayerHeight() {
        return this.playerHeight;
    }

    /**
     * Get the current score.
     * @return The current score.
     */
    public int getScore() {
        return (int) ((getPlayerHeight() + jetpack.getHighestWindowedScore()) / Constants.METER);
    }

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

            // TODO Only generate clouds at a certain height
            cloudGenerator.generateCloud();

            // Move the clouds both horizontally (will always move in this way) and vertically
            // depending on whether the jetpack has moved up.
            cloudGenerator.moveClouds(-verticalClimb);
        }

        // Attempt to generate some new entities.
        // TODO Get sensible value for sky height percentage.
        entityManager.generate(40f);

        // Move all active entities.
        entityManager.moveEntities(-verticalClimb);

        // Check for any cases where entities in the scene have collided with the jetpack.
        jetpack.checkForCollision(this.entityManager.getActiveEntities());
    }
}
