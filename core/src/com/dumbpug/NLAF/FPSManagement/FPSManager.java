package com.dumbpug.NLAF.FPSManagement;

/**
 * Manages the FPS of our game, compliments LibGDX approach.
 */
public class FPSManager {
	private float time = 0;
	private float DELAY;
	long lastFPS = System.nanoTime();
    int frameCount = 0;
    boolean showFPS = false;
    
	private Updateable renderableObject;
	
	public FPSManager(Updateable ref){
		renderableObject = ref;
        // Set a default FPS of 30.
		DELAY = 1/30f;
		lastFPS = System.nanoTime();
	}
	
    public FPSManager(Updateable ref, float delay){
    	renderableObject = ref;
        // Set delay
		DELAY = delay;
        // Set last FPS
		lastFPS = System.nanoTime();
	}

    /**
	 * Do update
	 * @param deltaTimeVal
     */
    public void update(float deltaTimeVal){
    	time += deltaTimeVal;
    	if(time >= DELAY){
    		renderableObject.controlledRender();
    		time -= DELAY;
    	}
        // Are we actually displaying the FPS?
    	if(showFPS){
    		frameCount++;
    		if (System.nanoTime() > (lastFPS + 1000000000)) {
				// Print our FPS
                System.out.println("FPS : " + frameCount);
                lastFPS = System.nanoTime();
				// Reset frame count
                frameCount = 0;
            }
    	}
    }
    
    public void showFPS(boolean show){
    	showFPS = show;
    }
}
