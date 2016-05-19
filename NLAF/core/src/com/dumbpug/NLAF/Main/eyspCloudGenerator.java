package com.dumbpug.NLAF.Main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Nikolas Howard
 */
public class eyspCloudGenerator {
	public enum CloudDirection {LEFT, RIGHT}
	private float cloudScale = Constants.MOVEMENT_UNIT*0.1f;
	private float cloudStep = Constants.MOVEMENT_UNIT*0.4f; //4
	private int cloudPoolMax = 100;
	// The border above and below the view in which clouds can generate. (as a percentage
	// of total screen height)
	float cloudYBorderPercentage = 400;
	private int cloudGenChancePercentage = 55;
	private ArrayList<Cloud> cloudPool = new ArrayList<Cloud>();

	// Used for random number generation to determine when to generate clouds.
	Random ran = new Random();
	
	public class Cloud{
		// The sprite for the cloud.
		public Sprite sprite;
		// The distance to move cloud on step.
		public float step;
		// Direction of cloud.
		public CloudDirection direction;
		// Cloud position
		public float posY, posX;
		public boolean isForegroundCloud = false;

		public void move(float offsetY){
			switch (direction) {
			case LEFT:
				posX -= step;
				break;
			case RIGHT:
				posX += step;
				break;
			}
			// Foreground clouds will move faster vertically too.
			if(isForegroundCloud){
				posY += (offsetY*1.5f);
			} else {
				posY += offsetY;
			}
		}

		public Sprite getSprite(){
			sprite.setPosition(posX, posY);
			return sprite;
		}
	}
	
	// This will ALWAYS move the clouds horizontally, the offset determines whether
	// they are moving vertically. (will take 0 as offset if not moving vertically)
	public void moveClouds(float offsetY){
		for (Iterator<Cloud> iterator = cloudPool.iterator(); iterator.hasNext();) {
			Cloud cloud = iterator.next();
			// If cloud is too far from viewport then delete it.
			if(cloud.direction == CloudDirection.RIGHT && cloud.posX > (Gdx.graphics.getWidth()*1.5)){
				iterator.remove();
			} else if (cloud.direction == CloudDirection.LEFT && cloud.posX < (0 - (Gdx.graphics.getWidth()/2))){
				iterator.remove();
			} else {
				cloud.move(offsetY);
			}
		}
	}
	
	public void generateCloud(){
		// Is there space in the pool for a new cloud?
		if(cloudPool.size() <= cloudPoolMax){
			// Should we generate a new cloud?
			if(ran.nextInt(1000) < cloudGenChancePercentage){
				Cloud cloud = new Cloud();
				// Calculate border in which clouds can be generated outside viewport
				float cloudYBorderVal = (Gdx.graphics.getHeight() / 100) * cloudYBorderPercentage;
				// Calculate the y position of the new cloud
				cloud.posY = ran.nextInt((int)(Gdx.graphics.getHeight() + cloudYBorderVal)) - (int)(Gdx.graphics.getHeight()/5);
				int cloudType = ran.nextInt(4);
				Sprite cloudSprite = new Sprite(new Texture(Gdx.files.internal("MiscImages/cloud_" + String.valueOf(cloudType) + ".png")));
				cloud.sprite = cloudSprite;
				// Back or foreground cloud?
				if(ran.nextInt(2) == 1){
					// Foreground clouds will be larger, transparent, and move faster
					cloud.step = cloudStep*2;
					cloud.isForegroundCloud = true;
					cloudSprite.scale(cloudScale*6);
					cloudSprite.setAlpha(0.5f);
				} else {
					// Background
					cloud.step = cloudStep;
					cloudSprite.scale(cloudScale);
				}
				// TEMP all clouds start on left and move right
				cloud.posX = 0 - (float)(cloudSprite.getWidth() * cloudSprite.getScaleX());
				cloud.direction = CloudDirection.RIGHT;
				// Add the new cloud to the pool 
				cloudPool.add(cloud);
			}
		}
	}
	
	public void drawCloudsInForeground(SpriteBatch batch){
		for(Cloud cloud : cloudPool) {
			if(cloud.isForegroundCloud){
				cloud.getSprite().draw(batch);
			}
		}
	}
	
	public void drawCloudsInBackground(SpriteBatch batch){
		for(Cloud cloud : cloudPool) {
			if(!cloud.isForegroundCloud){
				cloud.getSprite().draw(batch);
			}
		}
	}
}
