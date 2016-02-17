package com.dumbpug.NLAF.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Nikolas Howard.
 */
public class HeightCounter {
    // An array of our digit textures.
    private TextureRegion[][] digitTextureRegions;
    // An array of our gold digit textures (for use when counter exceeds high score).
    private TextureRegion[][] digitGoldTextureRegions;
    // The height of our digits.
    private float digitHeight;
    // The width of our digits.
    private float digitWidth;

    public HeightCounter() {
        // Get our digit texture regions.
        digitTextureRegions = TextureRegion.split(new Texture(Gdx.files.internal("FontTextures/digits.png")), 11, 15);
        digitGoldTextureRegions = TextureRegion.split(new Texture(Gdx.files.internal("FontTextures/digits_gold.png")), 11, 15);
        // Calculate digit size based on screen resolution.
        digitHeight = Gdx.graphics.getHeight()*0.05f;
        digitWidth  = digitHeight*(11f/15f);
    }

    public HeightCounter(int heightOfDigit) {
        // Get our digit texture regions.
        digitTextureRegions = TextureRegion.split(new Texture(Gdx.files.internal("FontTextures/digits.png")), 11, 15);
        digitGoldTextureRegions = TextureRegion.split(new Texture(Gdx.files.internal("FontTextures/digits_gold.png")), 11, 15);
        // Calculate digit size based on screen resolution.
        digitHeight = heightOfDigit;
        digitWidth  = digitHeight*(11f/15f);
    }

    /**
     * Draw the height counter to the screen.
     * @param batch
     * @param score
     * @param isHighScore
     */
    public void drawScore(SpriteBatch batch, int score, boolean isHighScore) {
        // Make sure we don't have a negative score.
        if(score < 0) {
            score = 0;
        }
        // The digit textures we use will depend on whether this is a high score. If it is we user the pretty gold digits.
        TextureRegion[][] appropriateTextureRegion = isHighScore ? digitGoldTextureRegions : digitTextureRegions;
        // Get the score as a string to make it easier to parse the digits.
        String scoreString = Integer.toString(score);
        // Let us calculate where we will need to start drawing from on the X axis. This will depend on score length.
        int xPos = (int) ((Gdx.graphics.getWidth()/2) - (((scoreString.length()+1) * digitWidth)/2));
        // Let us calculate where we will need to start drawing from on the Y axis.
        int yPos = (int) (Gdx.graphics.getHeight() - digitHeight);
        // Let us draw each digit in turn.
        int digitIndex = 0;
        for(; digitIndex < scoreString.length(); digitIndex++) {
            batch.draw(appropriateTextureRegion[0][Integer.parseInt(scoreString.charAt(digitIndex)+"")], (int) (xPos + (digitIndex*digitWidth)), yPos, digitWidth, digitHeight);
        }
        // We still need to draw the 'm' after the score.
        batch.draw(appropriateTextureRegion[0][10], (int)(xPos + ((digitIndex)*digitWidth)), yPos, digitWidth, digitHeight);
    }

    /**
     * Draw the height counter at a user defined position.
     * @param batch
     * @param score
     * @param isHighScore
     * @param posX
     * @param posY
     */
    public void drawScore(SpriteBatch batch, int score, boolean isHighScore, int posX, int posY) {
        // Make sure we don't have a negative score.
        if(score < 0) {
            score = 0;
        }
        // The digit textures we use will depend on whether this is a high score. If it is we user the pretty gold digits.
        TextureRegion[][] appropriateTextureRegion = isHighScore ? digitGoldTextureRegions : digitTextureRegions;
        // Get the score as a string to make it easier to parse the digits.
        String scoreString = Integer.toString(score);
        // Let us draw each digit in turn.
        int digitIndex = 0;
        for(; digitIndex < scoreString.length(); digitIndex++) {
            batch.draw(appropriateTextureRegion[0][Integer.parseInt(scoreString.charAt(digitIndex)+"")], (int) (posX + (digitIndex*digitWidth)), posY, digitWidth, digitHeight);
        }
        // We still need to draw the 'm' after the score.
        batch.draw(appropriateTextureRegion[0][10], (int)(posX + ((digitIndex)*digitWidth)), posY, digitWidth, digitHeight);
    }
}
