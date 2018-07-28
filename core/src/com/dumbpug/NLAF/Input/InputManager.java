package com.dumbpug.NLAF.Input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.dumbpug.NLAF.tools.Button;
import com.dumbpug.NLAF.tools.ControlBase;
import com.dumbpug.NLAF.tools.ControlBase.ControlType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

/**
 * 
 * @author nikolas.howard
 *
 */
public class InputManager implements InputProcessor {
	
	public class ListenerControlLink {
		public InputListener listener;
		public ControlBase control;
		
		public ListenerControlLink(InputListener listener, ControlBase control){
			this.listener = listener;
			this.control = control;
		}
	}
	
	// List of listener/control links
	ArrayList<ListenerControlLink> activeLinks = new ArrayList<ListenerControlLink>();
	
	// map of controls that have been touched down on (touch up hasn't happened yet) key is the reference pointer generated on touch down.
	HashMap<Integer,ListenerControlLink> touchedDownControls = new HashMap<Integer,ListenerControlLink>();
	
	// Should we register input?
	private boolean acknowledgeInput = true;
	
	/**
	 * We are adding a listener and a control, meaning if the control is altered we update 
	 * the associated listener too, passing the control reference 
	 * @param listener
	 * @param control
	 */
	public void addControl(InputListener listener, ControlBase control){
		// Lock the active links list.
		synchronized(activeLinks){
			boolean controlInstanceExists = false;
			for(ListenerControlLink link : activeLinks){
				if(link.control == control){
					controlInstanceExists = true;
					break;
				}
			}
			if(!controlInstanceExists){
				ListenerControlLink tempLink = new ListenerControlLink(listener, control);
				activeLinks.add(tempLink);
			}
		}
	}
	
	/**
	 * Adding a control with no listener, we are just altering the state of the control.
	 * @param control
	 */
	public void addControl(ControlBase control){
		// Lock the active links list.
		synchronized(activeLinks){
			boolean controlInstanceExists = false;
			for(ListenerControlLink link : activeLinks){
				if(link.control == control){
					controlInstanceExists = true;
					break;
				}
			}
			if(!controlInstanceExists){
				ListenerControlLink tempLink = new ListenerControlLink(null, control);
				activeLinks.add(tempLink);
			}
		}
	}
	
	/**
	 * Remove the reference of this control in activeLinks if one exists. And from touchedDownControls
	 * @param control
	 */
	public void removeControl(ControlBase control){
		// Lock the active links list.
		synchronized(activeLinks){
			Iterator<ListenerControlLink> controlLinkIterator = activeLinks.iterator();
			while(controlLinkIterator.hasNext()) {
                ListenerControlLink link = controlLinkIterator.next();
                if(link.control == control){
                    controlLinkIterator.remove();
                }
            }
		}
	}
	
	/**
	 * Remove all controls linked to this listener.
	 * @param listener
	 */
	public void removeListenerControls(InputListener listener){
		// Lock the active links list.
		synchronized(activeLinks){
			// TODO
		}
	}
	
	public boolean isAcknowledgeInput() {
		return acknowledgeInput;
	}

	public void setAcknowledgeInput(boolean acknowledgeInput) {
		this.acknowledgeInput = acknowledgeInput;
	}

	@Override
	public boolean keyDown(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		// Only handle input if this object is set to do so
		if(acknowledgeInput){
			int posx = arg0;
			// Calculate Y value from bottom left corner as this is how NLAF copes with positioning.
			int posy = Gdx.graphics.getHeight() - arg1;
			// Lock the listener/control links list.
			synchronized(activeLinks){
				for(ListenerControlLink link : activeLinks){
					ControlBase linkControl = link.control;
					// check if this press is within the active area of the control.
					if(linkControl.isPointInActiveArea(posx, posy)){
						// React according to control type.
						if(linkControl.getControlType() == ControlType.BUTTON){
							Button buttonControl = (Button) linkControl;
							buttonControl.setButtonPressed(true);
							// Set the pointer reference and add to active map.
							synchronized(touchedDownControls){
								touchedDownControls.put(arg2, link);
							}
							// Let the attached listener of the event.
							if(link.listener != null){
								link.listener.onDown(linkControl);
							}
						}  
						// TODO React to other control types
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		int posx = arg0;
		// Calculate Y value from bottom left corner as this is how NLAF copes with positioning.
		int posy = Gdx.graphics.getHeight() - arg1;
		int pointer = arg2;
		ControlBase control;
		InputListener listener;
		ListenerControlLink tempLink = null;
		synchronized(touchedDownControls){
			tempLink = touchedDownControls.get(pointer);
		}
		if(tempLink != null){
			control = tempLink.control;
			listener = tempLink.listener;
			
			// React according to control type.
			if(control.getControlType() == ControlType.BUTTON){
				Button buttonControl = (Button) control;
				// If we've dragged off of the button, it is no longer pressed, but also does not qualify
				// as being released (properly pushed).
				if(!buttonControl.isPointInActiveArea(posx, posy)){
					if(buttonControl.isButtonPressed()){
						buttonControl.setButtonPressed(false);
					}
				}
			}
			// TODO handle other control types
			// Update the listener of this control.
			if(listener != null){
				listener.onDrag(control);
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		int posx = arg0;
		// Calculate Y value from bottom left corner as this is how NLAF copes with positioning.
		int posy = Gdx.graphics.getHeight() - arg1;
		int pointer = arg2;
		ControlBase control = null;
		InputListener listener = null;
		synchronized(touchedDownControls){
			ListenerControlLink tempLink = touchedDownControls.get(pointer);
			if(tempLink != null){
				control = tempLink.control;
				listener = tempLink.listener;
				// React according to control type.
				if(control.getControlType() == ControlType.BUTTON){
					Button buttonControl = (Button) control;
					// If we've dragged off of the button, it is no longer pressed, but also does not qualify
					// as being released (properly pushed).
					if(buttonControl.isPointInActiveArea(posx, posy) && buttonControl.isButtonPressed()){
						// Mark button as being pressed and released.
						buttonControl.setButtonReleased(true);
					} 
					buttonControl.setButtonPressed(false);	
				}
				// TODO handle other control types
				// Update the listener of this control.
				if(listener != null){
					listener.onUp(control);
				}
				// remove the link from the active controls
				touchedDownControls.remove(pointer);
			}
		}
		return false;
	}
}