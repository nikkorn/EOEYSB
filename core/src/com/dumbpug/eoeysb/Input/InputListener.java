package com.dumbpug.eoeysb.Input;

import com.dumbpug.eoeysb.tools.ControlBase;


public interface InputListener {
	public void onDown(ControlBase control);
	public void onDrag(ControlBase control);
	public void onUp(ControlBase control);
}
