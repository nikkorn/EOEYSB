package com.dumbpug.NLAF.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.NLAF.Math.GameMath;
import com.dumbpug.NLAF.Math.ScreenPoint;
import com.dumbpug.NLAF.Main.JetPackEffectDrawer.EngineState;

public class JetPack {
	private Sprite jetPackFrame;
	private float maxVelocity = Constants.MOVEMENT_UNIT*1.6f;
	private float accel = Constants.MOVEMENT_UNIT/50;
	private float rotationalVelocity = 0f;
	private float maxRotationalVelocity = 5f;
	private float accelRotation = 0.1f;
	private float gravitationalDown = Constants.MOVEMENT_UNIT/2.5f;
	private float rotation = 0f;
	private float posX;
	private float posY;
	private float velX;
	private float velY;
	private int highestWindowedHeight = 0;
	private int startingHeight;
	private boolean hasLaunched = false;
	private double jetEngineOffset;
    private ScreenPoint leftEngineScreenPoint = new ScreenPoint();
    private ScreenPoint rightEngineScreenPoint = new ScreenPoint();

	// Variables relating to fuelling.
	private double fuelLevelLeft;
	private double fuelLevelRight;
	private final double fuelTankLimit = 120;
	private double stepFuelUse = 0.1;

	// Effect states of our engines.
	EngineState leftEngineState = EngineState.NONE;
	EngineState rightEngineState = EngineState.NONE;

	// Holds the state of our collidable engines.
	private JetPackCollidableState collidableState = null;

	// Manages the drawing of certain JetPack related elements.
	private JetPackEffectDrawer effectDrawer;

	/**
	 * Constructor.
	 * @param width
	 * @param positionX
	 * @param positionY
	 */
	public JetPack(float width, float positionX, float positionY) {
		jetPackFrame = new Sprite(new Texture(Gdx.files.internal("MiscImages/jet.png")));
		jetPackFrame.setSize(width, width * (jetPackFrame.getHeight() / jetPackFrame.getWidth()));
		jetPackFrame.setOrigin(jetPackFrame.getWidth() / 2, jetPackFrame.getHeight() / 2);
		this.posX = positionX - (jetPackFrame.getWidth() / 2);
		this.posY = positionY - (jetPackFrame.getHeight() / 2);
		startingHeight = (int) this.posY;
		jetPackFrame.setPosition(posX, posY);
		jetEngineOffset = width*0.357;
		// Fill our tanks.
		this.fuelLevelLeft  = this.fuelTankLimit;
		this.fuelLevelRight = this.fuelTankLimit;
		// Initialise our effect drawer.
		effectDrawer = new JetPackEffectDrawer(jetPackFrame);
	}

	/**
	 * Moves the whole frame, returns the Y movement difference since last render.
	 * @param leftEnginePress
	 * @param rightEnginePress
	 * @return
	 */
	public float move(boolean leftEnginePress, boolean rightEnginePress) {
		float originalPosY = posY;
		// If we have not launched (user presses both engines) then do nothing this time.
		if(!hasLaunched) {
			// Are we launching?
			if(leftEnginePress && rightEnginePress) {
				// LAUNCH!
				// Shoot us off by setting the Y velocity to max.
				this.velY = maxVelocity;
				// Set the flag to show we have launched.
				hasLaunched = true;
			} else {
				return 0f;
			}
		}
		// Let us first check to see if the engines have the fuel to move.
		if(fuelLevelLeft == 0) {
			// If we dont have fuel in this engine just ignore the press.
			leftEnginePress = false;
		}
		if(fuelLevelRight == 0) {
			// If we dont have fuel in this engine just ignore the press.
			rightEnginePress = false;
		}
		if (leftEnginePress && rightEnginePress) {
			velX += accel * Math.cos(Math.toRadians(rotation + 90));
			velY += accel * Math.sin(Math.toRadians(rotation + 90));
			// Don't allow the jetpack movement to exceed the max velocity.
			if(velX > maxVelocity) {
				velX = maxVelocity;
			}
			if(velY > maxVelocity) {
				velY = maxVelocity;
			}
			// We've used fuel for both engines.
			this.fuelLevelLeft  -= this.stepFuelUse;
			this.fuelLevelRight -= this.stepFuelUse;
			// Decrease rotational velocity towards 0
			reduceRotationalVelocityToZero();
		} else if (rightEnginePress) {
			// decrease main velocity
			reducePackVelocityToZero();
			// increase right rotational velocity
			if (rotationalVelocity + accelRotation <= maxRotationalVelocity) {
				rotationalVelocity += accelRotation;
			}
			// We've used fuel for right engine.
			this.fuelLevelRight -= this.stepFuelUse;
		} else if (leftEnginePress) {
			// decrease main velocity
			reducePackVelocityToZero();
			// increase left rotational velocity
			if (rotationalVelocity - accelRotation >= -maxRotationalVelocity) {
				rotationalVelocity -= accelRotation;
			}
			// We've used fuel for left engine.
			this.fuelLevelLeft -= this.stepFuelUse;
		} else {
			// decrease rotational velocity towards 0
			reduceRotationalVelocityToZero();
			// decrease main velocity
			reducePackVelocityToZero();
		}
		// Clear up any negative fuel levels
		if(this.fuelLevelLeft < 0) {
			this.fuelLevelLeft = 0;
		}
		if(this.fuelLevelRight < 0) {
			this.fuelLevelRight = 0;
		}
		// Reset the rotation of the jetpack to 0 so we can carry out some stuff.
		jetPackFrame.setRotation(0);
		// Add the velocity to the rotation
		rotation += rotationalVelocity;
		this.posX += velX;
		this.posY += velY;
		// Keep the ship on the screen.
		if (jetPackFrame.getX() > Gdx.graphics.getWidth()) {
			this.posX = 0 - jetPackFrame.getWidth();
		} else if (jetPackFrame.getX() < (0 - jetPackFrame.getWidth())) {
			this.posX = Gdx.graphics.getWidth();
		}
		// Stop the ship from moving off the top of the screen by clamping it to 66% of the height
		// of the screen, the amount that the jetpack exceeds this is the amount that the rest of the
		// scene needs to be pushed down by.
		// Store the amount that the jetpack will offset the screen in the Y direction.
		float jetpackScreenOffset = 0f;
		// Have we exceeded the limit?
		if(this.posY > (Gdx.graphics.getHeight() * 0.5f)) {
			// Store the offset.
			jetpackScreenOffset = this.posY - (Gdx.graphics.getHeight() * 0.5f);
			// The jetpack has attempted to fly further than our clamp, stop it.
			this.posY = Gdx.graphics.getHeight() * 0.5f;
		}
		// Set the new position of the jetpack origin.
		jetPackFrame.setPosition((int) this.posX,(int) this.posY);
		// Have we reached a new windowed height? If so make a note of it as the main game state needs this to calculate the score.
		if(posY > highestWindowedHeight) {
			highestWindowedHeight = (int) posY;
		}
		// Rotate the pack.
		jetPackFrame.setRotation(rotation + rotationalVelocity);
		// Have negative gravitational pull
		this.posY -= (gravitationalDown * (this.maxVelocity-this.velY));
		// Let us set our engine state variables for our effect drawer.
		// TODO Add conditions for when engine is damaged/sputtering etc.
		if(leftEnginePress) {
			this.leftEngineState = EngineState.FIRED;
		} else {
			this.leftEngineState = EngineState.NONE;
		}
		if(rightEnginePress) {
			this.rightEngineState = EngineState.FIRED;
		} else {
			this.rightEngineState = EngineState.NONE;
		}
		// Return the offset of the jetpack movement.
		return jetpackScreenOffset;
	}

	/**
	 * Draw all elements of the jet pack.
	 * @param batch
	 */
	public void draw(SpriteBatch batch) {
		// Draw our jetpack effects.
		effectDrawer.draw(batch, this.leftEngineState, this.rightEngineState);
		// Draw our jetpack.
		jetPackFrame.draw(batch);
	}

	private void reduceRotationalVelocityToZero() {
		if (rotationalVelocity < 0) {
			if (rotationalVelocity + (accelRotation / 2) >= 0) {
				rotationalVelocity = 0;
			} else {
				rotationalVelocity += (accelRotation / 2);
			}
		} else if (rotationalVelocity > 0) {
			if (rotationalVelocity - (accelRotation / 2) <= 0) {
				rotationalVelocity = 0;
			} else {
				rotationalVelocity -= (accelRotation / 2);
			}
		}
	}

	private void reducePackVelocityToZero() {
		if (velX < 0) {
			if (velX + (accelRotation / 2) >= 0) {
				velX = 0;
			} else {
				velX += (accelRotation / 2);
			}
		} else if (velX > 0) {
			if (velX - (accelRotation / 2) <= 0) {
				velX = 0;
			} else {
				velX -= (accelRotation / 2);
			}
		}
		if (velY < 0) {
			if (velY + (accelRotation / 2) >= 0) {
				velY = 0;
			} else {
				velY += (accelRotation / 2);
			}
		} else if (velY > 0) {
			if (velY - (accelRotation / 2) <= 0) {
				velY = 0;
			} else {
				velY -= (accelRotation / 2);
			}
		}
	}

	/**
	 * Return the highest score that this jetpack has achieved within the bounds of the window.
	 * @return
	 */
	public int getHighestWindowedScore() { return (int) (highestWindowedHeight-startingHeight) / 10; }

	public double getFuelLevelPercentageRight() {
		return (fuelLevelRight/fuelTankLimit)*100;
	}

	public double getFuelLevelPercentageLeft() {
		return (fuelLevelLeft/fuelTankLimit)*100;
	}

	public void addFuelRight() {
		this.fuelLevelRight = this.fuelTankLimit;
	}

	public void addFuelLeft() {
		this.fuelLevelLeft = this.fuelTankLimit;
	}

	/**
	 * Return true if our sprite has dropped off the bottom of our screen.
	 * Meaning that the user has lost this round.
	 * @return
	 */
	public boolean hasDroppedOut() {
		// Is our sprite off the bottom of the screen?
	 	return this.posY < -jetPackFrame.getWidth();
	}

	/**
	 * Get the collidable state of the JetPack.
	 * @return collidable state.
	 */
	public JetPackCollidableState getCollidableState() {
		GameMath.getTargetPosition(jetPackFrame.getX() + (jetPackFrame.getWidth()/2), jetPackFrame.getY() + (jetPackFrame.getHeight()/2), rotation - 180, this.jetEngineOffset, leftEngineScreenPoint);
		GameMath.getTargetPosition(jetPackFrame.getX() + (jetPackFrame.getWidth()/2), jetPackFrame.getY() + (jetPackFrame.getHeight()/2), rotation, this.jetEngineOffset, rightEngineScreenPoint);
		if(this.collidableState == null) {
			collidableState = new JetPackCollidableState(leftEngineScreenPoint, rightEngineScreenPoint, jetPackFrame.getHeight());
		} else {
			collidableState.set(leftEngineScreenPoint, rightEngineScreenPoint);
		}
		return collidableState;
	}

	/**
	 * Returns whether the jetpack has launched.
	 * @return
	 */
	public boolean hasLaunched() {
		return this.hasLaunched;
	}
}