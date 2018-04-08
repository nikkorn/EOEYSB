package com.dumbpug.NLAF.Input;

import com.dumbpug.NLAF.Tools.ControlBase;


public interface InputListener {
	public void onDown(ControlBase control);
	public void onDrag(ControlBase control);
	public void onUp(ControlBase control);
}
