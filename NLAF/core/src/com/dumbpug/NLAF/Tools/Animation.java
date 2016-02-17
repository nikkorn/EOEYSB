package com.dumbpug.NLAF.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {        
	com.badlogic.gdx.graphics.g2d.Animation     animation;                       
	TextureRegion[]        frames;                     
	float stateTime = 0f;                                       
	    
	public Animation(Texture texture, int rows, int columns, float step){
		TextureRegion[][] tempRegion = TextureRegion.split(texture, texture.getWidth()/columns, texture.getHeight()/rows);
		frames = new TextureRegion[rows*columns];
		int index = 0;
		for (int i = 0; i < rows; i++) {
	        for (int j = 0; j < columns; j++) {
	            frames[index++] = tempRegion[i][j];
	        }
		}
		animation = new com.badlogic.gdx.graphics.g2d.Animation(step, frames);   
	}
	
	public TextureRegion getCurrentFrame(boolean loop){
		stateTime += Gdx.graphics.getDeltaTime();           
		return animation.getKeyFrame(stateTime, loop);
	}
	
	public void step(){
		stateTime += Gdx.graphics.getDeltaTime();           
	}
	
	public boolean isFinished(){
		return animation.isAnimationFinished(stateTime);
	}
}
