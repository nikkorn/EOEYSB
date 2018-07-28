package com.dumbpug.NLAF.tools;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * 
 * @author nikolas.howard
 *
 */
public class Column {
	public enum ControlOrientation {CENTER, NORTH, SOUTH, EAST, WEST, NORTH_WEST, NORTH_EAST, SOUTH_WEST, SOUTH_EAST}
	public int columnNum;
	public String columnWidth; // Can be a percentage, number or *
	public Float calculatedWidth; // Set when pane is organised, is actual width
	public float columnHeight;
	public float positionX;
	public float positionY;
	public Float maxWidth;
	public Float minWidth;
	public PaneObjectProperties controlProperties;
	private Sprite backgroundSprite = null;
	private com.dumbpug.NLAF.tools.Animation backgroundAnimation = null;
	
	public com.dumbpug.NLAF.tools.ControlBase control;
	public ControlOrientation controlOrientation;
	
	 /** Sets the position of this column.
     * @param posX
     * @param posY
     */
    public void setPosition(float posX, float posY){
    	this.positionX = posX;
    	this.positionY = posY;
    }
    
    /**
     * Get the X position of this column.
     * @return X position.
     */
    public float getPositionX(){
    	return this.positionX;
    }
    
    /**
     * Get the Y position of this column.
     * @return Y position.
     */
    public float getPositionY(){
    	return this.positionY;
    }
    
    /**
     * Set background sprite (clear any animation)
     * @param sprite
     */
    public void setBackgroundSprite(Sprite sprite){
    	this.backgroundSprite = sprite;
    	backgroundAnimation = null;
    }
    
    /**
     * Get background sprite
     * @param sprite
     */
    public Sprite getBackgroundSprite(){
    	return this.backgroundSprite;
    }
    
    /**
     * Set background animation (clear any static sprite)
     * @param sprite
     */
    public void setBackgroundAnimation(com.dumbpug.NLAF.tools.Animation animation){
    	backgroundSprite = null;
    	this.backgroundAnimation = animation;
    }
    
    /**
     * Get background animation
     * @param sprite
     */
    public com.dumbpug.NLAF.tools.Animation getBackgroundAnimation(){
    	return this.backgroundAnimation;
    }
    
    /**
    * This function is called by the pane, responsible for drawing background or border if they are set.
    */
    public void draw(SpriteBatch batch){
	   // Resize/Reposition background sprite to match column.
	   if(backgroundSprite != null){
		   backgroundSprite.setSize(calculatedWidth,columnHeight);
		   backgroundSprite.setPosition(positionX, positionY);
		   backgroundSprite.draw(batch);
	   }
	   
	   // Resize/Reposition background animation to match column, then draw.
	   if(backgroundAnimation != null){
		   TextureRegion currentRunningFrame = backgroundAnimation.getCurrentFrame(true);
		   batch.draw(currentRunningFrame, positionX, positionY, calculatedWidth, columnHeight);
	   }
	   
	   // Draw control if we have one.
	   if(control != null){
		   control.draw(batch);
	   }
	   // TODO draw other things like border
    }

    public void setControl(com.dumbpug.NLAF.tools.ControlBase control, ControlOrientation orient){
    	this.control = control;
    	this.controlOrientation = orient;
    }

    public com.dumbpug.NLAF.tools.ControlBase getControl(com.dumbpug.NLAF.tools.ControlBase control){
    	return control;
    }
    
    public void setControlOrientation(ControlOrientation orient){
    	this.controlOrientation = orient;
    }
    
    public ControlOrientation getControlOrientation(){
    	return controlOrientation;
    }
    
    public void layoutControl(){
    	if(control != null){
    		// If we're stretching to column then do it and ignore orientation.
    		if(control.isStretchToColumn()){
    			// TODO Potentially add padding
    			control.setSize(calculatedWidth, columnHeight);
    			control.setPosition(positionX, positionY);
    		} else {
    			// Keep original size (or if original is larger than column restrict it to column size)
    		}
    	}
    }
}
