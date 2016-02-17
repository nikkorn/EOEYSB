package com.dumbpug.NLAF.States;

import java.util.HashMap;

/**
 * 
 * @author nikolas.howard
 *
 */
public class StateManager {
	// The current state that will be rendered
	private int currentState;
	
	// Map of active States.
	private HashMap<Integer,State> activeStates = new HashMap<Integer,State>();
	
	//----------------------------------------------------------
	// State ID's (When creating a new state, add an entry here)
	//----------------------------------------------------------
	public static int STATE_SPLASH = 0;
	public static int STATE_TITLE = 1;
	public static int STATE_LEVEL_ONE = 2;
	public static int STATE_EYSB_TITLE = 3;
	public static int STATE_EYSB_MAINGAME = 4;
	//----------------------------------------------------------
	// State ID's (When creating a new state, add an entry here)
	//----------------------------------------------------------
	
	/**
	 * Set the current State.
	 * @param State ID
	 */
	public void setCurrentState(int state){
		if(activeStates.containsKey(state)){
			currentState = state;
		}
	}
	
	/**
	 * Remove an active state, take care not to delete the current state.
	 * @param State ID
	 */
	public void endState(int state){
		if(activeStates.containsKey(state)){
			activeStates.remove(state);
		}
	}
	
	/**
	 * Add a State if it is not already an active state.
	 * @param State ID
	 */
	public void addState(int state){
		// Don't instantiate a state that is already up and running
		if(!activeStates.containsKey(state)){
			switch(state){
			case 0:
				activeStates.put(state, new SplashScreen());
				break;
			case 1:
				activeStates.put(state, new TitleScreen());
				break;
			case 2:
				activeStates.put(state, new LevelOne());
				break;
			case 3:
				activeStates.put(state, new eysbTitleScreen());
				break;
			case 4:
				activeStates.put(state, new eysbMainGame());
				break;
				
			//----------------------------------------------------------
			// Add cases here when adding new states!	
			//----------------------------------------------------------
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
		State currentStateObject = activeStates.get(currentState);
		// Check for a state change request
		if((stateRequestId = currentStateObject.getStateChangeRequestID()) == -1){
			currentStateObject.render();
		} else {
			// A state change request has been made by the current state
			if(currentStateObject.isStateRequestForFreshState()){
				// Get a NEW state
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
