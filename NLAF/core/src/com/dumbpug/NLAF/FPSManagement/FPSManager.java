package com.dumbpug.NLAF.FPSManagement;

public class FPSManager {
	private float time = 0;
	private float DELAY;
	
	long lastFPS = System.nanoTime();
    int frameCount = 0;
    boolean showFPS = false;
    
	private Updateable renderableObject;
	
	public FPSManager(Updateable ref){
		renderableObject = ref;
		DELAY = 1/30f;
		lastFPS = System.nanoTime();
	}
	
    public FPSManager(Updateable ref, float delay){
    	renderableObject = ref;
		DELAY = delay;
		lastFPS = System.nanoTime();
	}
    
    public void update(float dt){
    	time += dt;
    	if(time >= DELAY){
    		renderableObject.controlledRender();
    		time -= DELAY;
    	}
    	if(showFPS){
    		frameCount++;
    		if (System.nanoTime() > (lastFPS + 1000000000)) {
                System.out.println("FPS : " + frameCount);
                lastFPS = System.nanoTime();
                frameCount = 0;
            }
    	}
    }
    
    public void showFPS(boolean show){
    	showFPS = show;
    }
}
