package com.dumbpug.eoeysb.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

/**
 * 
 * @author nikolas.howard
 *
 */
public abstract class State {
	// A flag to indicate whether to render a loading screen if resources are not loaded
	private volatile boolean resourcesLoaded = false;
	// This will be set to a state id if this state wants the state manager to render a different state
	private int newStateRequestid = -1;
	// when requesting rendering a new state, this indicates whether this state should be deleted
	private boolean disposeOnStateChange = false;
	// when requesting rendering a new state, if this is true then we will instantiate a new 
	// state regardless of whether the is already an active state of this type. If false we
	// will use an active state of this type IF it already exists, or create a new one if not
    private boolean stateRequestIsFreshState = false;
	
    /**
     * Constructor: Load state resources.
     */
	public State(){
		// When instantiating this state, we have to load any resources first
		startResourceLoader();
	}
	
	/**
	 * Starts the resource loader.
	 */
	private void startResourceLoader(){
		// Start resource loader on new thread to free main thread to render an animated loading screen
		Gdx.app.postRunnable(new Runnable(){

			@Override
			public void run() {
				// Call the custom stateLoad() method
				stateLoad();
				// Set flag to true after resources are loaded to indicate that we can now render our loaded resources
				resourcesLoaded = true;
			}
			
		});
	};
	
	/**
	 * Renders our state, does a sub-render if resources have not been loaded yet.
	 */
	public void render(){
		// Clear our screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(resourcesLoaded){
			// Render the state. Will only be called when stateLoad() has finished
			renderState();
		} else {
			// Render some type of loading screen
			renderStateLoading();
		}
	}
	
	/**
	 * This method can be called to set flags to tell the state manager that a new state should be loaded.
	 * @param newStateId
	 * @param deleteThisStateOnChange
	 * @param ensureNewStateIsFresh
	 */
	public void markStateChange(int newStateId, boolean deleteThisStateOnChange, boolean ensureNewStateIsFresh){
		newStateRequestid = newStateId;
		disposeOnStateChange = deleteThisStateOnChange;
		stateRequestIsFreshState = ensureNewStateIsFresh;
	}
	
	/**
	 * Returns whether this state is disposable after a state change.
	 * @return disposeOnStateChange
	 */
	public boolean isDisposeableOnStateChange() {
		return disposeOnStateChange;
	}
	
	/**
	 * When a state change request is picked up by the State Manager this is used to 
	 * determine whether it should be a brand new state.
	 * @return
	 */
	public boolean isStateRequestForFreshState() {
		return stateRequestIsFreshState;
	}
	
	/**
	 * Called by the state manager before every render. -1 = no request, any other 
	 * value will indicate a request and will be the ID of the new state.
	 * @return
	 */
	public int getStateChangeRequestID(){
		return  newStateRequestid;
	}
	
	/**
	 * Called by the state manager when we move to another state and we are not 
	 * deleting this state.
	 */
	public void resetStateChangeRequest(){
		newStateRequestid = -1;
	}
	
	public abstract void stateLoad();
	public abstract void renderState();
	public abstract void renderStateLoading();
	
}
