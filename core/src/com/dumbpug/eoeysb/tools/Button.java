package com.dumbpug.eoeysb.tools;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A button.
 *
 *On touchdown, any controls that are in that region are added to the list toucheddowncontrols and the controls setPressed is called and set to true.
 *(In a normal situation you wouldnt be able to press multiple buttons in one press, so usually would only ever contain one control)
 *
 *on touchup, any controls that are in the list toucheddowncontrols have setPressed called and set to false before being removed from the list. setReleased(true) is only called on an 
 *object within toucheddowncontrols IF the point in which the touchup event occurs in teh region of that control
 *
 */
public class Button extends ControlBase {
	private volatile boolean isButtonPressed;
	private volatile boolean isButtonReleased;
	
	Sprite activeSprite;  // The sprite that is displayed if the button is active (pressed)
	
	public Button(){
		setButtonPressed(false);
		setButtonReleased(false);
		activeSprite = null;
	}
	
	public void setActiveSprite(Sprite sprite){
		activeSprite = sprite;
	}
	
	public void draw(SpriteBatch batch){
		if(isButtonPressed) {
			if(activeSprite != null){
				// Set the active control sprite to be the same dimensions and position as the default control sprite.
				// TODO Active sprite resizing/repositioning will eventually take place in overriden controlbase resize/repositon methods.
				activeSprite.setPosition(controlSprite.getX(), controlSprite.getY());
				activeSprite.setSize(controlSprite.getWidth(), controlSprite.getHeight());
				// Draw the active sprite instead of the default control sprite.
				activeSprite.draw(batch);		
			}
		} else {
			super.draw(batch);
		}
	}

	public boolean isButtonPressed() {
		return isButtonPressed;
	}

	public void setButtonPressed(boolean isButtonPressed) {
		this.isButtonPressed = isButtonPressed;
	}

	public boolean isButtonReleased() {
		// Reset the button released state when called. (so it can be reused)
		boolean result = isButtonReleased;
		isButtonReleased = false;
		return result;
	}

	public void setButtonReleased(boolean isButtonReleased) {
		this.isButtonReleased = isButtonReleased;
	}
	
	public ControlType getControlType(){
		return ControlType.BUTTON;
	}
}