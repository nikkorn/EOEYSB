package com.dumbpug.eoeysb.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dumbpug.eoeysb.Constants;
import com.dumbpug.eoeysb.panel.ResultPanel;
import com.dumbpug.eoeysb.scene.hud.FuelTankMeters;
import com.dumbpug.eoeysb.scene.hud.HeightCounter;
import com.dumbpug.eoeysb.tools.Animation;

/**
 * Scene Drawer.
 */
public class SceneDrawer {
    /**
     * Our height (score) counter.
     */
    HeightCounter heightCounter = new HeightCounter();
    /**
     * Our Fuel Tank HUD drawer.
     */
    FuelTankMeters fuelTankMeters = new FuelTankMeters();

    /** Constants specific to drawing scenes. */
    private static final float grassBaseMovementVertMultiplier     = 1f;
    private static final float mountainRangeMovementVertMultiplier = 0.5f;
    private static final float blueSkyBaseMovementVertMultiplier   = 0.2f;
    private static final float fullSkyMovementVertMultiplier       = Constants.MOVEMENT_UNIT * 0.0025f;
    private static final float mountainRangeAspectRatio            = 1.40909f;

    /** World Sprites/Animations */
    Sprite fullSkyBackground;
    Sprite blueSkyBase;
    Sprite grassBase;
    Sprite launcherBase;
    Sprite launcherPromptBackground;
    Animation mountainRange;
    Animation launcherPrompt;

    /**
     * Create a new instance of the SceneDrawer class.
     */
    public SceneDrawer() {
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
    }

    /**
     * Get the sky height percentage.
     * @return The sky height percentage.
     */
    public int getSkyHeightPercentage(){
        return (int) ((fullSkyBackground.getY() / -(fullSkyBackground.getHeight() - Gdx.graphics.getHeight())) * 100);
    }

    /**
     * Draw the specified scene.
     * @param batch The batch to use in drawing.
     * @param scene The scene to draw.
     * @param resultPanel The result panel.
     */
    public void draw(SpriteBatch batch, Scene scene, ResultPanel resultPanel) {
        // Update positions of backdrops based on player height.
        blueSkyBase.setPosition(blueSkyBase.getX(), -(scene.getCurrentHeight() * blueSkyBaseMovementVertMultiplier));
        grassBase.setPosition(grassBase.getX(), -(scene.getCurrentHeight() * grassBaseMovementVertMultiplier));
        launcherBase.setPosition(launcherBase.getX(), (grassBase.getHeight()-(launcherBase.getHeight()/1.35f))-(scene.getCurrentHeight() * grassBaseMovementVertMultiplier));
        // Set the position of the blue sky/black space background based on our height.
        if(((scene.getCurrentHeight() * fullSkyMovementVertMultiplier) + Gdx.graphics.getHeight()) < fullSkyBackground.getHeight()){
            fullSkyBackground.setPosition(fullSkyBackground.getX(), -(scene.getCurrentHeight() * fullSkyMovementVertMultiplier));
        } else {
            fullSkyBackground.setPosition(fullSkyBackground.getX(), -(fullSkyBackground.getHeight() - Gdx.graphics.getHeight()));
        }
        // Get the current frame of our mountain-range animation.
        TextureRegion currentMountainFrame = mountainRange.getCurrentFrame(true);
        TextureRegion currentLaunchPromptFrame = null;
        // We will draw the launch prompt frame over the scene if we have not launched yet.
        if(!scene.getJetpack().hasLaunched()) {
            currentLaunchPromptFrame = launcherPrompt.getCurrentFrame(true);
        }
        batch.begin();
        // Draw the background panels.
        fullSkyBackground.draw(batch);
        blueSkyBase.draw(batch);
        batch.draw(currentMountainFrame, 0, 0 - (mountainRangeMovementVertMultiplier * scene.getCurrentHeight()), Gdx.graphics.getWidth(), Gdx.graphics.getWidth() * mountainRangeAspectRatio);
        grassBase.draw(batch);
        launcherBase.draw(batch);
        // Draw all of the clouds in the background so that they are behind all other entities.
        scene.getCloudGenerator().drawCloudsInBackground(batch);
        // Draw the jetpack.
        scene.getJetpack().draw(batch);
        // Draw all of the active entities in the scene.
        // scene.getEntityManager().draw(batch);
        // Draw all of the clouds in the foreground so that they are in front of all other entities.
        scene.getCloudGenerator().drawCloudsInForeground(batch);
        // Draw the height counter.
        heightCounter.drawScore(batch, scene.getScore(), false);
        // Draw the fuel levels.
        fuelTankMeters.drawFuelTankHud(batch, scene.getJetpack().getLeftEngine().getFuelLevelPercentage(), scene.getJetpack().getRightEngine().getFuelLevelPercentage());
        // Draw our launch prompt if the user hasn't launched yet.
        if(!scene.getJetpack().hasLaunched()) {
            launcherPromptBackground.draw(batch);
            batch.draw(currentLaunchPromptFrame, 0, (Gdx.graphics.getHeight()/2)-(Gdx.graphics.getWidth()/2), Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
        }
        // Draw our results panel if it is showing.
        if(resultPanel.isShowing()) {
            resultPanel.drawPane(batch);
        }
        batch.end();
    }
}
