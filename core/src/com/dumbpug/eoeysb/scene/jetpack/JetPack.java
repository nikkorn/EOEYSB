package com.dumbpug.eoeysb.scene.jetpack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.eoeysb.Constants;
import com.dumbpug.eoeysb.Math.GameMath;
import com.dumbpug.eoeysb.Math.ScreenPoint;

public class JetPack {
	/**
	 * The jetpack sprite.
	 */
	private Sprite jetPackSprite;
	/**
	 * The jetpack position and rotation.
	 */
	private float posX, posY, rotation = 0f;
	/**
	 * The jetpack velocity.
	 */
	private float velX, velY, rotationalVelocity = 0f;
	/**
	 * Whether the jetpack has launched.
	 */
	private boolean hasLaunched = false;
	/**
	 * The highest point that we have reached.
	 */
	private int highestWindowedHeight = 0;
	/**
	 * The height that we started at before launch.
	 */
	private int startingHeight;
	/**
	 * The screen points of both engines.
	 */
    private ScreenPoint leftEngineScreenPoint = new ScreenPoint();
    private ScreenPoint rightEngineScreenPoint = new ScreenPoint();
	/**
	 * The fuel levels of the jetpack engines.
	 */
	private double fuelLevelLeft = Constants.FUELLING_TANK_LIMIT, fuelLevelRight = Constants.FUELLING_TANK_LIMIT;
	/**
	 *  The effect states of our engines.
	 */
	JetPackEffectDrawer.EngineState leftEngineState = JetPackEffectDrawer.EngineState.NONE;
	JetPackEffectDrawer.EngineState rightEngineState = JetPackEffectDrawer.EngineState.NONE;
	/**
	 * The collidable state of our engines.
	 */
	private JetPackCollidableState collidableState = null;
	/**
	 * Manages the drawing of certain JetPack related elements.
	 */
	private JetPackEffectDrawer effectDrawer;

	/**
	 * Create a new instance of the JetPack class.
	 */
	public JetPack() {
		jetPackSprite = new Sprite(new Texture(Gdx.files.internal("MiscImages/jet.png")));
		jetPackSprite.setSize(Constants.JETPACK_WIDTH, Constants.JETPACK_WIDTH * (jetPackSprite.getHeight() / jetPackSprite.getWidth()));
		jetPackSprite.setOrigin(jetPackSprite.getWidth() / 2, jetPackSprite.getHeight() / 2);
		// Set the initial jetpack position.
		this.posX = (Gdx.graphics.getWidth() / 2) - (jetPackSprite.getWidth() / 2);
		this.posY = Constants.JETPACK_INITIAL_Y_OFFSET - (jetPackSprite.getHeight() / 2);
		// Set the jetpack sprite position.
		jetPackSprite.setPosition(posX, posY);
		// Set the starting/height windowed height.
		// These are used in determining the highest point that the jetpack has reached.
		this.startingHeight        = (int) this.posY;
		this.highestWindowedHeight = (int) this.posY;;
		// Initialise our effect drawer.
		effectDrawer = new JetPackEffectDrawer(jetPackSprite);
	}

	/**
	 * Moves the whole jetpack, returns the Y movement difference since last render.
	 * @param leftEnginePress
	 * @param rightEnginePress
	 * @return The Y movement difference since last render.
	 */
	public float move(boolean leftEnginePress, boolean rightEnginePress) {
		float originalPosY = posY;
		// If we have not launched (user presses both engines) then do nothing this time.
		if(!hasLaunched) {
			// Are we launching?
			if(leftEnginePress && rightEnginePress) {
				// LAUNCH! Shoot us off by setting the Y velocity to max.
				this.velY = Constants.JETPACK_MAX_VELOCITY;
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
			velX += Constants.JETPACK_ACCELERATION * Math.cos(Math.toRadians(rotation + 90));
			velY += Constants.JETPACK_ACCELERATION * Math.sin(Math.toRadians(rotation + 90));
			// Don't allow the jetpack movement to exceed the max velocity.
			if(velX > Constants.JETPACK_MAX_VELOCITY) {
				velX = Constants.JETPACK_MAX_VELOCITY;
			}
			if(velY > Constants.JETPACK_MAX_VELOCITY) {
				velY = Constants.JETPACK_MAX_VELOCITY;
			}
			// We've used fuel for both engines.
			this.fuelLevelLeft  -= Constants.FUELLING_TICK_USE;
			this.fuelLevelRight -= Constants.FUELLING_TICK_USE;
			// Decrease rotational velocity towards 0.
			reduceRotationalVelocityToZero();
		} else if (rightEnginePress) {
			// decrease main velocity
			reducePackVelocityToZero();
			// increase right rotational velocity
			if (rotationalVelocity + Constants.JETPACK_ROTATIONAL_ACCELERATION <= Constants.JETPACK_MAX_ROTATIONAL_VELOCITY) {
				rotationalVelocity += Constants.JETPACK_ROTATIONAL_ACCELERATION;
			}
			// We've used fuel for right engine.
			this.fuelLevelRight -= Constants.FUELLING_TICK_USE;
		} else if (leftEnginePress) {
			// decrease main velocity
			reducePackVelocityToZero();
			// increase left rotational velocity
			if (rotationalVelocity - Constants.JETPACK_ROTATIONAL_ACCELERATION >= -Constants.JETPACK_MAX_ROTATIONAL_VELOCITY) {
				rotationalVelocity -= Constants.JETPACK_ROTATIONAL_ACCELERATION;
			}
			// We've used fuel for left engine.
			this.fuelLevelLeft -= Constants.FUELLING_TICK_USE;
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
		jetPackSprite.setRotation(0);
		// Add the velocity to the rotation
		rotation += rotationalVelocity;
		this.posX += velX;
		this.posY += velY;
		// Keep the ship on the screen.
		if (jetPackSprite.getX() > Gdx.graphics.getWidth()) {
			this.posX = 0 - jetPackSprite.getWidth();
		} else if (jetPackSprite.getX() < (0 - jetPackSprite.getWidth())) {
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
		jetPackSprite.setPosition((int) this.posX,(int) this.posY);
		// Have we reached a new windowed height? If so make a note of it as the main game state needs this to calculate the score.
		if(posY > highestWindowedHeight) {
			highestWindowedHeight = (int) posY;
		}
		// Rotate the pack.
		jetPackSprite.setRotation(rotation + rotationalVelocity);
		// Apply gravity to the pack.
		this.posY -= (Constants.JETPACK_GRAVITY * (Constants.JETPACK_MAX_VELOCITY - this.velY));
		// Let us set our engine state variables for our effect drawer.
		// TODO Add conditions for when engine is damaged/sputtering etc.
		if(leftEnginePress) {
			this.leftEngineState = JetPackEffectDrawer.EngineState.FIRED;
		} else {
			this.leftEngineState = JetPackEffectDrawer.EngineState.NONE;
		}
		if(rightEnginePress) {
			this.rightEngineState = JetPackEffectDrawer.EngineState.FIRED;
		} else {
			this.rightEngineState = JetPackEffectDrawer.EngineState.NONE;
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
		jetPackSprite.draw(batch);
	}

	/**
	 * Reduce the jetpack rotational velocity.
	 */
	private void reduceRotationalVelocityToZero() {
		if (rotationalVelocity < 0) {
			if (rotationalVelocity + (Constants.JETPACK_ROTATIONAL_ACCELERATION / 2) >= 0) {
				rotationalVelocity = 0;
			} else {
				rotationalVelocity += (Constants.JETPACK_ROTATIONAL_ACCELERATION  / 2);
			}
		} else if (rotationalVelocity > 0) {
			if (rotationalVelocity - (Constants.JETPACK_ROTATIONAL_ACCELERATION  / 2) <= 0) {
				rotationalVelocity = 0;
			} else {
				rotationalVelocity -= (Constants.JETPACK_ROTATIONAL_ACCELERATION  / 2);
			}
		}
	}

	/**
	 * Reduce the jetpack velocity.
	 */
	private void reducePackVelocityToZero() {
		if (velX < 0) {
			if (velX + (Constants.JETPACK_ROTATIONAL_ACCELERATION  / 2) >= 0) {
				velX = 0;
			} else {
				velX += (Constants.JETPACK_ROTATIONAL_ACCELERATION  / 2);
			}
		} else if (velX > 0) {
			if (velX - (Constants.JETPACK_ROTATIONAL_ACCELERATION  / 2) <= 0) {
				velX = 0;
			} else {
				velX -= (Constants.JETPACK_ROTATIONAL_ACCELERATION  / 2);
			}
		}
		if (velY < 0) {
			if (velY + (Constants.JETPACK_ROTATIONAL_ACCELERATION  / 2) >= 0) {
				velY = 0;
			} else {
				velY += (Constants.JETPACK_ROTATIONAL_ACCELERATION  / 2);
			}
		} else if (velY > 0) {
			if (velY - (Constants.JETPACK_ROTATIONAL_ACCELERATION  / 2) <= 0) {
				velY = 0;
			} else {
				velY -= (Constants.JETPACK_ROTATIONAL_ACCELERATION  / 2);
			}
		}
	}

	/**
	 * Return the highest score that this jetpack has achieved within the bounds of the window.
	 * @return
	 */
	public int getHighestWindowedScore() { return (int) (highestWindowedHeight-startingHeight); }

	public double getFuelLevelPercentageRight() {
		return (fuelLevelRight / Constants.FUELLING_TANK_LIMIT) * 100;
	}

	public double getFuelLevelPercentageLeft() {
		return (fuelLevelLeft / Constants.FUELLING_TANK_LIMIT) * 100;
	}

	/**
	 * Refill the fuel for the right engine.
	 */
	public void addFuelRight() {
		this.fuelLevelRight = Constants.FUELLING_TANK_LIMIT;
	}

	/**
	 * Refill the fuel for the left engine.
	 */
	public void addFuelLeft() {
		this.fuelLevelLeft = Constants.FUELLING_TANK_LIMIT;
	}

	/**
	 * Return whether our jetpack has dropped off the bottom of our screen.
	 * Meaning that the user has lost this round.
	 * @return Whether our jetpack has dropped off the bottom of our screen.
	 */
	public boolean hasDroppedOut() {
	 	return this.posY < -Constants.JETPACK_WIDTH;
	}

	/**
	 * Get the collidable state of the JetPack.
	 * @return collidable state.
	 */
	public JetPackCollidableState getCollidableState() {
		GameMath.getTargetPosition(jetPackSprite.getX() + (jetPackSprite.getWidth()/2),
				jetPackSprite.getY() + (jetPackSprite.getHeight()/2), rotation - 180, Constants.JETPACK_ENGINE_OFFSET, leftEngineScreenPoint);
		GameMath.getTargetPosition(jetPackSprite.getX() + (jetPackSprite.getWidth()/2),
				jetPackSprite.getY() + (jetPackSprite.getHeight()/2), rotation, Constants.JETPACK_ENGINE_OFFSET, rightEngineScreenPoint);
		if(this.collidableState == null) {
			collidableState = new JetPackCollidableState(leftEngineScreenPoint, rightEngineScreenPoint, jetPackSprite.getHeight());
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