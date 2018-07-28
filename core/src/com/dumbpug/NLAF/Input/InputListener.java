package com.dumbpug.NLAF.Input;

import com.dumbpug.NLAF.tools.ControlBase;


public interface InputListener {
	public void onDown(ControlBase control);
	public void onDrag(ControlBase control);
	public void onUp(ControlBase control);
}
