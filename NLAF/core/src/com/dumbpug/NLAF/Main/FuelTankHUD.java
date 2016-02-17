package com.dumbpug.NLAF.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Niklas Howard.
 */
public class FuelTankHUD {
    // An array of our fuel tank textures.
    private TextureRegion[][] fuelTankTextureRegions;
    private int fuelTextureWidth = Gdx.graphics.getWidth()/15;
    private int fuelTextureHeight = fuelTextureWidth*2;
    private int fuelTexturePadding = fuelTextureWidth/5;
    private double stateThreshold = 100/13;

    public FuelTankHUD() {
        fuelTankTextureRegions = TextureRegion.split(new Texture(Gdx.files.internal("MiscImages/fuel_indicators.png")), 17, 34);
    }

    public void drawFuelTankHud(SpriteBatch batch, double leftTankPercentage, double rightTankPercentage) {
        // Draw left tank icon. Is it empty?
        int leftIconIndex = 0;
        if(leftTankPercentage > 0) {
            leftIconIndex = ((int) (leftTankPercentage/stateThreshold)) + 1;
        }
        if(leftIconIndex > 12) {
            leftIconIndex = 12;
        }
        // Draw right tank icon. Is it empty?
        int rightIconIndex = 0;
        if(rightTankPercentage > 0) {
            rightIconIndex = ((int) (rightTankPercentage/stateThreshold)) + 1;
        }
        if(rightIconIndex > 12) {
            rightIconIndex = 12;
        }
        // Physically draw the tank HUD.
        batch.draw(fuelTankTextureRegions[0][leftIconIndex], fuelTexturePadding, (int) Gdx.graphics.getHeight()-(fuelTextureHeight + fuelTexturePadding),
                this.fuelTextureWidth, this.fuelTextureHeight);
        batch.draw(fuelTankTextureRegions[0][rightIconIndex], (int) Gdx.graphics.getWidth()-(this.fuelTextureWidth + fuelTexturePadding),
                (int) Gdx.graphics.getHeight()-(fuelTextureHeight + fuelTexturePadding), this.fuelTextureWidth, this.fuelTextureHeight);
    }
}
