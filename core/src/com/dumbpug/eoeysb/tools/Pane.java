package com.dumbpug.eoeysb.tools;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * 
 * @author nikolas.howard
 *
 */
public class Pane extends ControlBase {
	private Sprite backgroundSprite = null;
	private Animation backgroundAnimation = null; 
	private com.dumbpug.eoeysb.tools.PaneElementBorder paneElementBorder = null;
	private HashMap<Integer, com.dumbpug.eoeysb.tools.Row> rowList = new HashMap<Integer, com.dumbpug.eoeysb.tools.Row>();
	
	/**
	 * Set the size of the Pane
	 * @param width
	 * @param height
	 */
	public void setSize(float width, float height){
		// Do actual resize in base control class
		super.setSize(width, height);
		// We've resized the pane,  we will have to layout the pane again
		organise();
	}
	
	 /**
     * Set background sprite (clear any animation)
     * @param sprite
     */
    public void setBackgroundSprite(Sprite sprite){
    	this.backgroundSprite = sprite;
    	backgroundAnimation = null;
    }
    
    /**
     * Get background sprite
     * @param sprite
     */
    public Sprite getBackgroundSprite(){
    	return this.backgroundSprite;
    }
    
    /**
     * Set background animation (clear any static sprite)
     * @param sprite
     */
    public void setBackgroundAnimation(Animation animation){
    	backgroundSprite = null;
    	this.backgroundAnimation = animation;
    }
    
    /**
     * Get background animation
     * @param sprite
     */
    public Animation getBackgroundAnimation(){
    	return this.backgroundAnimation;
    }
    
	/**
	 * Add a new row.
	 * @param Row object.
	 * @param Positon at which to add the row.
	 */
	public void addRow(com.dumbpug.eoeysb.tools.Row row, int rowPosition){
		// Error if we there is already a row at this position.
		if(!rowList.containsKey(rowPosition)){
			// Add row to map.
			rowList.put(rowPosition, row);
			// Re-Organise Pane.
			organise();
		} else {
			// TODO Throw error!
		}
	}
	
	/**
	 * Remove row.
	 * @param row
	 */
	public void removeRow(com.dumbpug.eoeysb.tools.Row row){
		// TODO Remove row.
	}
	
	/**
	 * Remove row.
	 * @param rowPos
	 */
	public void removeRow(int rowPos){
		// TODO Remove row.
	}
	
	/**
	 * When called all child objects are reformatted if necessary
	 */
	public void organise(){
		// Organise the rows in the pane
		organiseRows();
		// Organise border if we have one.
    	if(paneElementBorder != null){
    		paneElementBorder.prepareBorder(getPosX(), getPosY(), getWidth(), getHeight());
    	}
	}
	
	/**
	 * Organise the rows within the pane, and call organise for each.
	 */
	public void organiseRows(){
		float paneHeight = this.getHeight();
		float paneWidth = this.getWidth();
		
		ArrayList<com.dumbpug.eoeysb.tools.Row> staticHeightRows = new ArrayList<com.dumbpug.eoeysb.tools.Row>();
		ArrayList<com.dumbpug.eoeysb.tools.Row> percentageHeightRows = new ArrayList<com.dumbpug.eoeysb.tools.Row>();
		ArrayList<com.dumbpug.eoeysb.tools.Row> stretchHeightRows = new ArrayList<com.dumbpug.eoeysb.tools.Row>();
		
		// Organise rows into lists
		for(int i = 1; i <= rowList.size(); i++){
			// Get current Row
			com.dumbpug.eoeysb.tools.Row currentRow = rowList.get(i);
			
			// Get current rows height
			String currentRowHeight = currentRow.rowHeight;
			
			// Separate the rows into types (static, percentage or stretch)
			if(currentRowHeight.contains("%")){
				percentageHeightRows.add(currentRow);
			} else if(currentRowHeight.equals("*")){
				stretchHeightRows.add(currentRow);
			} else {
				staticHeightRows.add(currentRow);
			}
		}
		
		// Static row heights are exactly that, static, these are first.
		for(com.dumbpug.eoeysb.tools.Row row : staticHeightRows){
			float height = 0;
			
			// Attempt to cast the height to a float, if error occurs then were dealing with invalid input
			try{
				height = Float.parseFloat(row.rowHeight);
			} catch (Exception e){
				// TODO Throw illegal cast exception
				System.out.println("Error! - Illegal Cast - static row height to float");
			}
			
			// Set actual row height
			row.calculatedHeight = height;
			row.rowWidth = paneWidth;
		}
		
		// Percentage row heights are a percentage of the pane height. Calculate these next.
		for(com.dumbpug.eoeysb.tools.Row row : percentageHeightRows){
			float height = 0;
			float percentage = 0;
			
			// Strip the percentage sign
			String rowPercentage = row.rowHeight.replace("%", "");
			
			// Attempt to cast the height to a float, if error occurs then were dealing with invalid input
			try{
				percentage = Float.parseFloat(rowPercentage);
			} catch (Exception e){
				// TODO Throw illegal cast exception
				System.out.println("Error! - Illegal Cast - static row height to float");
			}
			
			// Calculate the height of this row 
			height = (paneHeight/100)*percentage;
			
			// Set actual row height
			row.calculatedHeight = height;
			row.rowWidth = paneWidth;
		}
		
		int stretchRowCount = stretchHeightRows.size();
		float totalUsedHeightCount = 0f;
		float freeRowSpace = 0f;
		
		// Get all the used space from percentage and static rows.
		for(com.dumbpug.eoeysb.tools.Row row : percentageHeightRows){
			totalUsedHeightCount += row.calculatedHeight;
		}
		
		for(com.dumbpug.eoeysb.tools.Row row : staticHeightRows){
			totalUsedHeightCount += row.calculatedHeight;
		}
		
		// Calculate free space
		freeRowSpace = paneHeight - totalUsedHeightCount;
		
		// Stretch row heights take whatever space is left and splits it among each row
		for(com.dumbpug.eoeysb.tools.Row row : stretchHeightRows){
			row.calculatedHeight = freeRowSpace/stretchRowCount;
			row.rowWidth = paneWidth;
		}
		
		// Set the row positions based on their new dimensions.
		setRowPositions();
		
		// Each row should now organise its columns in the same manner.
		for(int i = 1; i <= rowList.size(); i++){
			com.dumbpug.eoeysb.tools.Row currentRow = rowList.get(i);
			currentRow.organiseColumns();
		}
	}
	
	/**
	 * Sets the positions for each row.
	 */
	private void setRowPositions(){
		float rowPointX = this.getPosX();
		float rowPointY = this.getPosY() + this.getHeight();
		
		// Iterate through rows.
		for(int i = 1; i <= rowList.size(); i++){
			// Get current Row
			com.dumbpug.eoeysb.tools.Row currentRow = rowList.get(i);
			// Move the Y position of the row point down
			rowPointY -= currentRow.calculatedHeight;
			// Set row position
			currentRow.setPosition(rowPointX, rowPointY);
		}
	}
	
	/**
	 * Draw the pane and all of its contents (Pane->Row->Column->Control)
	 */
	public void drawPane(SpriteBatch batch){
		// Resize/Reposition background sprite to match pane, then draw.
    	if(backgroundSprite != null){
    		backgroundSprite.setSize(this.getWidth(), this.getHeight());
    		backgroundSprite.setPosition(this.getPosX(), this.getPosY());
    		backgroundSprite.draw(batch);
    	}
    	
    	// Resize/Reposition background animation to match pane, then draw.
    	if(backgroundAnimation != null){
    		TextureRegion currentRunningFrame = backgroundAnimation.getCurrentFrame(true);
    		batch.draw(currentRunningFrame, this.getPosX(), this.getPosY(), this.getWidth(), this.getHeight());
    	}
    
		// Call draw for each row.
    	for(int i = 1; i <= rowList.size(); i++){
    		rowList.get(i).draw(batch);
    	}
    	
    	// Draw Border if one is attached.
    	if(paneElementBorder != null){
    		paneElementBorder.drawBorder(batch);
    	}
	}
	
	/**
	 * Takes a value and a Min and Max range. Returns appropriate value.
	 * @param value
	 * @param max
	 * @param min
	 * @return
	 */
	public float calculateValueAgainstMaxMinRange(float value, float max, float min){
		if(value > max){
			return max;
		}
		if(value < min){
			return min;
		}
		return value;
	}

	public com.dumbpug.eoeysb.tools.PaneElementBorder getBorder() {
		return paneElementBorder;
	}

	public void setBorder(com.dumbpug.eoeysb.tools.PaneElementBorder paneElementBorder) {
		this.paneElementBorder = paneElementBorder;
	}
}
