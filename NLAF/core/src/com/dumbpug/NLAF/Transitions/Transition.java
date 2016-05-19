package com.dumbpug.NLAF.Transitions;

// Simple frame for a transition for states

public abstract class Transition {
	private boolean isCompleted = false;
	
	public boolean isCompleted(){
		return isCompleted;
	}
	
	protected void markAsComplete(){
		isCompleted = true;
	}
	
	public abstract void Render();
	public abstract void onLoad();
}
