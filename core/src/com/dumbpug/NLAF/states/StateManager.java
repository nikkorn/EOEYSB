package com.dumbpug.NLAF.states;

import com.dumbpug.NLAF.states.info.Controls;
import com.dumbpug.NLAF.states.info.FuelLevel;
import com.dumbpug.NLAF.states.info.FuelPods;
import java.util.HashMap;

/**
 * Manages the rendering and switching of game states.
 */
public class StateManager {
	/**
	 * The key of the current state.
	 */
	private int currentState;
	/**
	 * The active states.
	 */
	private HashMap<Integer, State> activeStates = new HashMap<Integer, State>();
	/**
	 * The state ids.
	 */
	public static final int STATE_SPLASH          = 0;
	public static final int STATE_TITLE           = 1;
	public static final int STATE_GAME            = 2;
	public static final int STATE_INFO_CONTROLS   = 3;
    public static final int STATE_INFO_FUEL_LEVEL = 4;
    public static final int STATE_INFO_FUEL_PODS  = 5;
	
	/**
	 * Set the current State.
	 * @param state
	 */
	public void setCurrentState(int state){
		if(activeStates.containsKey(state)){
			currentState = state;
		}
	}
	
	/**
	 * Remove an active state, take care not to delete the current state.
	 * @param state
	 */
	public void endState(int state){
		if(activeStates.containsKey(state)){
			activeStates.remove(state);
		}
	}
	
	/**
	 * Add a State if it is not already an active state.
	 * @param state
	 */
	public void addState(int state){
		// Don't instantiate a state that is already up and running
		if(!activeStates.containsKey(state)){
			switch(state){
			case STATE_SPLASH:
				activeStates.put(state, new SplashScreen());
				break;
			case STATE_TITLE:
				activeStates.put(state, new TitleScreen());
				break;
			case STATE_GAME:
				activeStates.put(state, new Game());
				break;
			case STATE_INFO_CONTROLS:
				activeStates.put(state, new Controls());
				break;
            case STATE_INFO_FUEL_LEVEL:
                activeStates.put(state, new FuelLevel());
                break;
            case STATE_INFO_FUEL_PODS:
                activeStates.put(state, new FuelPods());
                break;
			}
		}
	}
	
	/**
	 * Renders the current state. 
	 */
	public void renderCurrentState(){
		int currentStateRequestId = currentState;
		int stateRequestId;
		// Get the current state from the map
		com.dumbpug.NLAF.states.State currentStateObject = activeStates.get(currentState);
		// Check for a state change request
		if((stateRequestId = currentStateObject.getStateChangeRequestID()) == -1){
			currentStateObject.render();
		} else {
			// A state change request has been made by the current state
			if(currentStateObject.isStateRequestForFreshState()){
				// Get a new state.
				endState(stateRequestId);
				addState(stateRequestId);
				setCurrentState(stateRequestId);
				currentStateObject = activeStates.get(currentState);
				currentStateObject.render();
			} else {
				// Use existing state if it exists, otherwise make a new one
				if(!activeStates.containsKey(stateRequestId)){
					addState(stateRequestId);
				}
				setCurrentState(stateRequestId);
				currentStateObject = activeStates.get(currentState);
				currentStateObject.render();
			}
			if(currentStateObject.isDisposeableOnStateChange()){
				// Remove the old state
				activeStates.remove(currentStateRequestId);
			} else {
				// We're not deleting the old state so just reset the request
				activeStates.get(currentStateRequestId).resetStateChangeRequest();
			}
		}
	}
}
