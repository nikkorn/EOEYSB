package com.dumbpug.NLAF.tools;

public class PaneObjectProperties {	
	public int row;
	public int column;
	public boolean stretchToCell;
	
	public static int MARGIN_LEFT = 0;
	public static int MARGIN_TOP = 1;
	public static int MARGIN_RIGHT = 2;
	public static int MARGIN_BOTTOM = 3;
	private Float[] margin = new Float[4];

	public static int CLIP_LEFT = 0;
	public static int CLIP_TOP = 1;
	public static int CLIP_RIGHT = 2;
	public static int CLIP_BOTTOM = 3;
	private Float[] clipping = new Float[4];
	
	public void setStretchToCell(boolean stretchToCell){
		this.stretchToCell = stretchToCell;
	}
	
	public void setClipping(int pos, Float value){
		clipping[pos] = value;
	}
	
	public Float getClipping(int pos){
		return clipping[pos];
	}
	
	public void setMargin(int pos, Float value){
		margin[pos] = value;
	}
	
	public Float getMargin(int pos){
		return margin[pos];
	}
}
