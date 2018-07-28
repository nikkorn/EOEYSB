package com.dumbpug.NLAF.tools;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * 
 * @author nikolas.howard
 * 
 * This Object is attached to a pane element (pane, row, column).
 * 
 * Keeps a reference of border image resources, and on a call to draw() will draw
 * the border resources around the element it is attached to with defined thickness.
 * 
 * All Custom Resources should have the same thickness unless a thickness is defined.
 * 
 * Border resources:
 *  - BORDER_CORNER_TOPLEFT
 *  - BORDER_CORNER_TOPRIGHT
 *  - BORDER_CORNER_BOTTOMLEFT
 *  - BORDER_CORNER_BOTTOMRIGHT
 *  - BORDER_TOP
 *  - BORDER_BOTTOM
 *  - BORDER_LEFT
 *  - BORDER_RIGHT
 *
 */
public class PaneElementBorder {
	public enum BorderElementType {VERTICAL,HORIZONTAL,CORNER}
	
	// Points to a folder in assets that contains border image files
	private String borderResFolder = null;
	// The thickness of the border, if NULL then keep original dimensions
	private Float borderThickness = null;
	
	private ArrayList<Sprite> positionedBorderResource = new ArrayList<Sprite>();
	
	public void setBorderType(String borderRes){
		this.borderResFolder = borderRes;
	}
	
	public String getBorderType(){
		return borderResFolder;
	}

	public float getBorderThickness() {
		return borderThickness;
	}

	public void setBorderThickness(float borderThickness) {
		this.borderThickness = borderThickness;
	}
	
	/**
	 * Called by a pane element, sets the border
	 * @param posX
	 * @param posY
	 * @param width
	 * @param height
	 */
	public void prepareBorder(float posX, float posY, float width, float height){
		// Clear existing border resources
		positionedBorderResource.clear();
		// If the border thickness hasn't been defined, then we need to determine this from 
		// the resources dimensions.
		float thickness = 0;
		if(borderThickness == null){
			// Determine border thickness from a corner resource as all border resources must have same thickness
			Sprite cornerSprite = getBorderResourceAsSprite("BORDER_CORNER_TOPLEFT");
			thickness = cornerSprite.getWidth();
		} else {
			thickness = borderThickness;
		}
		
		// Draw the horizontal elements
		// Determine the border width, we take the width taken up by the corners
		float borderHorzontalWidth = width - thickness;
		float borderHorzontalResourceWidth;
		float borderHorzontalResourceAR;
		
		// Determine border resource width from a resource 
		Sprite sprite_TOP = getBorderResourceAsSprite("BORDER_TOP");
		// Now we calculate the width if we were to resize the resource with the correct thickness.
		borderHorzontalResourceAR = sprite_TOP.getWidth() / sprite_TOP.getHeight();
		sprite_TOP.setSize(thickness * borderHorzontalResourceAR, thickness);
		borderHorzontalResourceWidth = sprite_TOP.getWidth();
		
		int requiredSegments = (int) (borderHorzontalWidth / borderHorzontalResourceWidth);
		float remainingWidth = borderHorzontalWidth % borderHorzontalResourceWidth;
		requiredSegments = (remainingWidth > (borderHorzontalResourceWidth / 2)) ? requiredSegments + 1 : requiredSegments;

		// Calculate the width for each section.
		borderHorzontalResourceWidth = borderHorzontalWidth / requiredSegments;
		
		float widthCount = 0;
		for(int i = 0; i < requiredSegments; i++){
			// Get border sprite.
			Sprite segmentSprite_TOP = getBorderResourceAsSprite("BORDER_TOP");
			Sprite segmentSprite_BOTTOM = getBorderResourceAsSprite("BORDER_BOTTOM");
			// Resize our border sprite.
			segmentSprite_TOP.setSize(borderHorzontalResourceWidth, thickness);
			segmentSprite_BOTTOM.setSize(borderHorzontalResourceWidth, thickness);
			setBorderResource(segmentSprite_TOP, BorderElementType.HORIZONTAL, posX + widthCount, posY + height, thickness);
			setBorderResource(segmentSprite_BOTTOM, BorderElementType.HORIZONTAL, posX + widthCount, posY, thickness);
			widthCount += borderHorzontalResourceWidth;
		}
		
		// Draw the vertical elements
		// Determine the border width, we take the width taken up by the corners
		float borderVerticalHeight = height - thickness;
		float borderVerticalResourceWidth;
		float borderVerticalResourceAR;
		
		// Determine border resource width from a resource 
		Sprite sprite_LEFT = getBorderResourceAsSprite("BORDER_LEFT");
		// Now we calculate the width if we were to resize the resource with the correct thickness.
		borderVerticalResourceAR = sprite_TOP.getWidth() / sprite_TOP.getHeight();
		sprite_LEFT.setSize(thickness, thickness* borderVerticalResourceAR);
		borderVerticalResourceWidth = sprite_TOP.getWidth();
		
		requiredSegments = (int) (borderVerticalHeight / borderVerticalResourceWidth);
		remainingWidth = borderVerticalHeight % borderVerticalResourceWidth;
		requiredSegments = (remainingWidth > (borderVerticalResourceWidth / 2)) ? requiredSegments + 1 : requiredSegments;

		// Calculate the width for each section.
		borderVerticalResourceWidth = borderVerticalHeight / requiredSegments;
		
		float heightCount = 0;
		for(int i = 0; i < requiredSegments; i++){
			// Get border sprite.
			Sprite segmentSprite_LEFT = getBorderResourceAsSprite("BORDER_LEFT");
			Sprite segmentSprite_RIGHT = getBorderResourceAsSprite("BORDER_RIGHT");
			// Resize our border sprite.
			segmentSprite_LEFT.setSize(thickness, borderVerticalResourceWidth);
			segmentSprite_RIGHT.setSize(thickness, borderVerticalResourceWidth);
			setBorderResource(segmentSprite_LEFT, BorderElementType.VERTICAL, posX, (posY ) - heightCount, thickness);
			setBorderResource(segmentSprite_RIGHT, BorderElementType.VERTICAL, posX + width, (posY ) - heightCount, thickness);
			heightCount -= borderVerticalResourceWidth;
		}
		
		// Draw corners
		Sprite spriteCORNER_TOPLEFT = getBorderResourceAsSprite("BORDER_CORNER_TOPLEFT");
		setBorderResource(spriteCORNER_TOPLEFT, BorderElementType.CORNER, posX, posY+height, thickness);
		
		Sprite spriteCORNER_TOPRIGHT = getBorderResourceAsSprite("BORDER_CORNER_TOPRIGHT");
		setBorderResource(spriteCORNER_TOPRIGHT, BorderElementType.CORNER, posX+width, posY+height, thickness);
		
		Sprite spriteCORNER_BOTTOMLEFT = getBorderResourceAsSprite("BORDER_CORNER_BOTTOMLEFT");
		setBorderResource(spriteCORNER_BOTTOMLEFT, BorderElementType.CORNER, posX, posY, thickness);
		
		Sprite spriteCORNER_BOTTOMRIGHT = getBorderResourceAsSprite("BORDER_CORNER_BOTTOMRIGHT");
		setBorderResource(spriteCORNER_BOTTOMRIGHT, BorderElementType.CORNER, posX+width, posY, thickness);
	}
	
	/**
	 * Sets a resource.
	 * @param sprite The Sprite to draw
	 * @param elementType The type, needed to determine Sprite offset
	 * @param posX 
	 * @param posY
	 * @param thickness The thickness of the border
	 */
	private void setBorderResource(Sprite sprite, BorderElementType elementType, float posX, float posY, float thickness){
		// The positions we will draw the sprite at, will be offset based on resource type and size.
		float drawPosX = posX;
		float drawPosY = posY;
		float offset = thickness/2;
	
		// Apply the offsets
		switch(elementType){
			case CORNER:
				drawPosX -= offset;
				drawPosY -= offset;
				// Corners are square
				sprite.setSize(thickness, thickness);
				break;
				
			case VERTICAL:
				drawPosX -= offset;
				drawPosY += offset;
				break;
				
			case HORIZONTAL:
				drawPosY -= offset;
				drawPosX += offset;
				break;
		}
		
		// Reposition sprite with offset.
		sprite.setPosition(drawPosX, drawPosY);
		positionedBorderResource.add(sprite);
	}
	
	/**
	 * Draw the border resources
	 * @param batch
	 */
	public void drawBorder(SpriteBatch batch){
		for(Sprite sprite : positionedBorderResource){
			sprite.draw(batch);
		}
	}
	
	private Sprite getBorderResourceAsSprite(String resourceName){
		Sprite resource = null;
		// Ensure we have a border set.
		if(borderResFolder != null){
			try{
				// Get resource
				resource = new Sprite(new Texture(Gdx.files.internal("Border/" + this.getBorderType() + "/" + resourceName + ".png")));
			} catch (Exception e) {
				// Do nothing, return null Sprite
			}
		}
		return resource;
	}
}
