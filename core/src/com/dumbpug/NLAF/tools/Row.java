package com.dumbpug.NLAF.tools;

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
public class Row {
	public int rowNum;
	public String rowHeight; // Can be a percentage, number or *
	public float rowWidth;
	public float positionX;
	public float positionY;
	public Float calculatedHeight; // Set when pane is organised, is actual height
	public Float maxHeight = 9999f; // default 9999
	public Float minHeight = 0f; // default 0
	public HashMap<Integer,Column> columnList = new HashMap<Integer,Column>();
	private Sprite backgroundSprite = null;
	private Animation backgroundAnimation = null; 
	    
    /**
     * Sets the position of this row.
     * @param posX
     * @param posY
     */
    public void setPosition(float posX, float posY){
    	this.positionX = posX;
    	this.positionY = posY;
    }
    
    /**
     * Get the X position of this row.
     * @return X position.
     */
    public float getPositionX(){
    	return this.positionX;
    }
    
    /**
     * Get the Y position of this row.
     * @return Y positon.
     */
    public float getPositionY(){
    	return this.positionY;
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
     * This function is called by the pane, responsible for drawing background or border if they are set.
     */
    public void draw(SpriteBatch batch){
    	// Resize/Reposition background sprite to match row.
    	if(backgroundSprite != null){
    		backgroundSprite.setSize(rowWidth, calculatedHeight);
    		backgroundSprite.setPosition(positionX, positionY);
    		backgroundSprite.draw(batch);
    	}
    	
    	// Resize/Reposition background animation to match column, then draw.
	    if(backgroundAnimation != null){
		    TextureRegion currentRunningFrame = backgroundAnimation.getCurrentFrame(true);
		    batch.draw(currentRunningFrame, positionX, positionY, rowWidth, calculatedHeight);
	    }
  
    	// Call draw on columns.
    	for(int i = 1; i <= columnList.size(); i++){
    		columnList.get(i).draw(batch);
    	}
    }
    
	/**
	 * Add a new column.
	 * @param Column object.
	 * @param Positon at which to add the Column.
	 */
	public void addColumn(Column column, int colPosition){
		// Error if we there is already a column at this position.
		if(!columnList.containsKey(column)){
			// Add column to map.
			columnList.put(colPosition, column);
		} else {
			// TODO Throw error!
		}
	}
	
	/**
	 * Remove row.
	 * @param row
	 */
	public void removeColumn(Column column){
		// TODO
	}
	
	/**
	 * Organise columns in row.
	 */
	public void organiseColumns(){
		ArrayList<Column> staticWidthColumns = new ArrayList<Column>(); 
		ArrayList<Column> percentageWidthColumns = new ArrayList<Column>();
		ArrayList<Column> stretchWidthColumns = new ArrayList<Column>();
		
		// Organise rows into lists
		for(int i = 1; i <= columnList.size(); i++){
			// Get current column
			Column currentColumn = columnList.get(i);
			
			// Get current columns width
			String currentColumnWidth = currentColumn.columnWidth;
			
			// Separate the columns into types (static, percentage or stretch)
			if(currentColumnWidth.contains("%")){
				percentageWidthColumns.add(currentColumn);
			} else if(currentColumnWidth.equals("*")){
				stretchWidthColumns.add(currentColumn);
			} else {
				staticWidthColumns.add(currentColumn);
			}
		}
		
		// Static column widths are exactly that, static, these are first.
		for(Column column : staticWidthColumns){
			float width = 0;
			
			// Attempt to cast the height to a float, if error occurs then were dealing with invalid input
			try{
				width = Float.parseFloat(column.columnWidth);
			} catch (Exception e){
				// TODO Throw illegal cast exception
				System.out.println("Error! - Illegal Cast - static column width to float");
			}
			
			// Set actual row height
			column.calculatedWidth = width;
			column.columnHeight = calculatedHeight;
		}
		
		// Percentage column widths are a percentage of the row width. Calculate these next.
		for(Column column : percentageWidthColumns){
			float width = 0;
			float percentage = 0;
			
			// Strip the percentage sign
			String columnPercentage = column.columnWidth.replace("%", "");
			
			// Attempt to cast the width to a float, if error occurs then were dealing with invalid input
			try{
				percentage = Float.parseFloat(columnPercentage);
			} catch (Exception e){
				// TODO Throw illegal cast exception
				System.out.println("Error! - Illegal Cast - static row height to float");
			}
			
			// Calculate the width of this column
			width = (rowWidth/100)*percentage;
			
			// Set actual column width
			column.calculatedWidth = width;
			column.columnHeight = calculatedHeight;
		}
		
		int stretchColumnCount = stretchWidthColumns.size();
		float totalUsedWidthCount = 0f;
		float freeColumnSpace = 0f;
		
		// Get all the used space from percentage and static columns.
		for(Column column : percentageWidthColumns){
			totalUsedWidthCount += column.calculatedWidth;
		}
		
		for(Column column : staticWidthColumns){
			totalUsedWidthCount += column.calculatedWidth;
		}
		
		// Calculate free space
		freeColumnSpace = rowWidth - totalUsedWidthCount;
		
		// Stretch column heights take whatever space is left and splits it among each column
		for(Column column : stretchWidthColumns){
			column.calculatedWidth = freeColumnSpace/stretchColumnCount;
			column.columnHeight = calculatedHeight;
		}
		
		// Set the column positions based on their new dimensions.
		setColumnPositions();
		// Layout the controls in the columns
		layoutColumnControls();
	}
	
	/**
	 * Sets the positions of the row's columns when organising.
	 */
	private void setColumnPositions(){
		float columnPointX = this.getPositionX();
		float columnPointY = this.getPositionY();
		
		for(int k = 1; k <= columnList.size(); k++){
			Column currentColumn = columnList.get(k);
			// Set column position.
			currentColumn.setPosition(columnPointX, columnPointY);
			// Get position at end of this column
			columnPointX += currentColumn.calculatedWidth;
		}
	}
	
	private void layoutColumnControls(){
		for(int k = 1; k <= columnList.size(); k++){
			Column currentColumn = columnList.get(k);
			// Layout the control in the column (if there is one linked to it)
			currentColumn.layoutControl();
		}
	}
}