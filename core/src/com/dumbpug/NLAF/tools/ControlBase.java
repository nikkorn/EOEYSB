package com.dumbpug.NLAF.tools;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Base class of all controls that can be passed to the InputMonitor class. 
 * Can be added to a cell in a pane or as stand alone.
 * @author nikolas.howard
 *
 */
public class ControlBase {
	// Sprite for this control.
	protected Sprite controlSprite;
	
	// The Aspect Ratio of the sprite in its original form.
	private float controlSpriteOriginalAR = 0;
	
	// Control Types
	public enum ControlType {BASE, BUTTON}
	
	// Position
	private float Xpos = 0;
	private float Ypos = 0;
	
	// Dimensions
	private float width;
	private float height;
	
	// Keep aspect ratio on resize 
	private boolean keepAspectRatio = true;
	
	// Determines whether the touch area is limited to the sprite area instead of the control itself  
	private boolean isActiveAreaOnSprite = false;
	
	// Determines whether the sprite should stretch to its container (only if used in pane)
	private boolean isStretchToColumn = false;
	
	// Determines whether this control is active and should respond to input.
	private boolean isControlActive = true;
	
	/**
	 * Draw the control sprite if we have one
	 * @param batch
	 */
	public void draw(SpriteBatch batch){
		if(controlSprite != null){
			controlSprite.draw(batch);
		}
	}
	
	/**
	 * Set the size of the control
	 * @param width
	 * @param height
	 */
	public void setSize(float width, float height){
		this.width = width;
		this.height = height;
		// If we have a control sprite, then it needs to be re-laid out. 
		if(controlSprite != null){
			layoutSprite();
		}
	}
	
	public float getWidth(){
		return this.width;
	}
	
	public float getHeight(){
		return this.height;
	}
	
	public float getPosX(){
		return this.Xpos;
	}
	
	public float getPosY(){
		return this.Ypos;
	}
	
	public void setPosition(float x, float y){
		// Get the offset of the sprite from the control if one exists.
		if(controlSprite != null){
			float spriteXOffset = controlSprite.getX() - this.Xpos;
			float spriteYOffset = controlSprite.getY() - this.Ypos;
			controlSprite.setPosition(x + spriteXOffset, y + spriteYOffset);
		}
		// Set the new control position.
		this.Xpos = x;
		this.Ypos = y;
	}
	
	public void setControlSprite(Sprite sprite, boolean matchControlSizeToSprite){
		controlSprite = sprite;
		// Calculate AR of sprite.
		controlSpriteOriginalAR = controlSprite.getHeight()/controlSprite.getWidth();
		if(matchControlSizeToSprite){
			// Set the size of the control to match the Sprite size
			this.setSize(controlSprite.getWidth(), controlSprite.getHeight());
			// Set the sprite position to match the control position.
			controlSprite.setPosition(this.getPosX(), this.getPosY());
		} else{
			layoutSprite();
		}
	}

	public boolean isKeepAspectRatio() {
		return keepAspectRatio;
	}

	public void setKeepAspectRatio(boolean keepAspectRatio) {
		boolean settingChanged = (this.keepAspectRatio != keepAspectRatio);
		this.keepAspectRatio = keepAspectRatio;
		// Resize/position the sprite to reflect this change.
		if(settingChanged){
			layoutSprite();
		}
	}

	public boolean isActiveAreaOnSprite() {
		return isActiveAreaOnSprite;
	}

	public void setActiveAreaOnSprite(boolean isActiveAreaOnSprite) {
		this.isActiveAreaOnSprite = isActiveAreaOnSprite;
	}
	

	public boolean isStretchToColumn() {
		return isStretchToColumn;
	}
	

	public void setStretchToColumn(boolean isStretchToColumn) {
		this.isStretchToColumn = isStretchToColumn;
	}
	
	/**
	 * Lays out the sprite to fit the control.
	 */
	private void layoutSprite(){
		// If keepAspectRatio is true, then we will need to set the control sprite
		// to match the size of the control while keeping 
		if(keepAspectRatio){
			//  stretch sprite to match controls existing dimensions while keeping AR and reposition
			float currentControlAR = this.height/this.width;
			if(controlSpriteOriginalAR > currentControlAR){
				// Match Control/Sprite Height.
				controlSprite.setSize((this.height / controlSpriteOriginalAR), this.height);
				controlSprite.setPosition((this.getPosX() + (this.width / 2)) - (controlSprite.getWidth()/2), this.getPosY());
			} else if(controlSpriteOriginalAR < currentControlAR) {
				// Match Control/Sprite Width.
				controlSprite.setSize(this.width, (this.width * controlSpriteOriginalAR));
				controlSprite.setPosition(this.getPosX(), (this.getPosY() + (this.height / 2)) - (controlSprite.getHeight()/2));
			} else {
				// The AR of the control and sprite are the same, match dimensions/positions. 
				controlSprite.setSize(this.width, this.height);
				controlSprite.setPosition(this.getPosX(), this.getPosY());
			}
		} else {
			// stretch sprite to match controls existing dimensions and reposition 
			controlSprite.setSize(this.width, this.height);
			controlSprite.setPosition(this.getPosX(), this.getPosY());
		}
	}
	
	/**
	 * Takes a point on the screen and determines if it is in the bounds of this control.
	 * @param posX
	 * @param posY
	 * @return
	 */
	public boolean isPointInActiveArea(float posX, float posY){
		boolean returnVal = true;
		// If active area is limited to sprite, then just check sprite bounds. Otherwise
		// check bounds of whole control.
		if(isActiveAreaOnSprite && (controlSprite != null)){
			boolean inXBounds = (posX >= controlSprite.getX() && posX <= (controlSprite.getX() + controlSprite.getWidth()));
			boolean inYBounds = (posY >= controlSprite.getY() && posY <= (controlSprite.getY() + controlSprite.getHeight()));
			returnVal = (inXBounds && inYBounds);
		} else {
			boolean inXBounds = (posX >= Xpos && posX <= (Xpos + width));
			boolean inYBounds = (posY >= Ypos && posY <= (Ypos + height));
			returnVal = (inXBounds && inYBounds);
		}
		return returnVal;
	}

	public boolean isControlActive() {
		return isControlActive;
	}

	public void setControlActive(boolean isControlActive) {
		this.isControlActive = isControlActive;
	}
	
	public ControlType getControlType(){
		return ControlType.BASE;
	}
}
