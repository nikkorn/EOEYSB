package com.dumbpug.eoeysb.scene.jetpack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.eoeysb.Constants;
import com.dumbpug.eoeysb.math.GameMath;
import com.dumbpug.eoeysb.scene.entities.Entity;

import java.util.Collection;

public class JetPack {
	/**
	 * The engines.
	 */
	private Engine leftEngine = new Engine(), rightEngine = new Engine();
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
	 * Manages the drawing of certain JetPack related elements.
	 */
	private JetPackEffectDrawer effectDrawer;

	/**
	 * Create a new instance of the JetPack class.
	 */
	public JetPack() {
		jetPackSprite = new Sprite(new Texture(Gdx.files.internal("MiscImages/jet.png")));
		jetPackSprite.setSize(Constants.JETPACK_WIDTH, Constants.JETPACK_HEIGHT);
		jetPackSprite.setOrigin(Constants.JETPACK_WIDTH / 2, Constants.JETPACK_HEIGHT / 2);
		// Set the initial jetpack position.
		this.posX = (Gdx.graphics.getWidth() / 2) - (Constants.JETPACK_WIDTH / 2);
		this.posY = Constants.JETPACK_INITIAL_Y_OFFSET - (Constants.JETPACK_HEIGHT / 2);
		// Give the jetpack engines their initial positions.
		updateEnginePositions();
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
	 * Get the left engine.
	 * @return The left engine.
     */
	public Engine getLeftEngine() {
		return this.leftEngine;
	}

	/**
	 * Get the right engine.
	 * @return The right engine.
	 */
	public Engine getRightEngine() {
		return this.rightEngine;
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
		// Update our engines and get whether they were firing as part of this move.
		boolean leftEngineFired  = this.leftEngine.update(leftEnginePress);
		boolean rightEngineFired = this.rightEngine.update(rightEnginePress);
		// Determine how to move the pack, based on which engines have fired.
		if (leftEngineFired && rightEngineFired) {
			velX += Constants.JETPACK_ACCELERATION * Math.cos(Math.toRadians(rotation + 90));
			velY += Constants.JETPACK_ACCELERATION * Math.sin(Math.toRadians(rotation + 90));
			// Don't allow the jetpack movement to exceed the max velocity.
			if(velX > Constants.JETPACK_MAX_VELOCITY) {
				velX = Constants.JETPACK_MAX_VELOCITY;
			}
			if(velY > Constants.JETPACK_MAX_VELOCITY) {
				velY = Constants.JETPACK_MAX_VELOCITY;
			}
			// Decrease rotational velocity towards 0.
			reduceRotationalVelocityToZero();
		} else if (rightEngineFired) {
			// decrease main velocity.
			reducePackVelocityToZero();
			// increase right rotational velocity.
			if (rotationalVelocity + Constants.JETPACK_ROTATIONAL_ACCELERATION <= Constants.JETPACK_MAX_ROTATIONAL_VELOCITY) {
				rotationalVelocity += Constants.JETPACK_ROTATIONAL_ACCELERATION;
			}
		} else if (leftEngineFired) {
			// decrease main velocity.
			reducePackVelocityToZero();
			// increase left rotational velocity.
			if (rotationalVelocity - Constants.JETPACK_ROTATIONAL_ACCELERATION >= -Constants.JETPACK_MAX_ROTATIONAL_VELOCITY) {
				rotationalVelocity -= Constants.JETPACK_ROTATIONAL_ACCELERATION;
			}
		} else {
			// decrease rotational velocity towards 0.
			reduceRotationalVelocityToZero();
			// decrease main velocity.
			reducePackVelocityToZero();
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
		// Update the engine positions to reflect the new pack position.
		updateEnginePositions();
		// Return the offset of the jetpack movement.
		return jetpackScreenOffset;
	}

	/**
	 * Check for any active entities that are colliding with either engine.
	 * @param activeEntities The active entities.
     */
	public void checkForEntityCollisions(Collection<Entity> activeEntities) {
		// For every active entity...
		for (Entity entity : activeEntities) {
			// ... check for collisions with the left engine and ...
			this.leftEngine.checkForEntityCollision(entity);
			// ... check for collisions with the right engine.
			this.rightEngine.checkForEntityCollision(entity);
		}
	};

	/**
	 * Update te positions of both engines.
	 */
	public void updateEnginePositions() {
		GameMath.getTargetPosition(jetPackSprite.getX() + (jetPackSprite.getWidth() / 2),
				jetPackSprite.getY() + (jetPackSprite.getHeight() / 2), rotation - 180, Constants.JETPACK_ENGINE_OFFSET, this.leftEngine.getPosition());
		GameMath.getTargetPosition(jetPackSprite.getX() + (jetPackSprite.getWidth() / 2),
				jetPackSprite.getY() + (jetPackSprite.getHeight() / 2), rotation, Constants.JETPACK_ENGINE_OFFSET, this.rightEngine.getPosition());
	}

	/**
	 * Draw the jetpack, including any engine effects.
	 * @param batch The sprite batch to use.
	 */
	public void draw(SpriteBatch batch) {
		// Draw our jetpack effects.
		effectDrawer.draw(batch, this.leftEngine.getState(), this.rightEngine.getState());
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
	 * Return the highest height that this jetpack has reached within the bounds of the window.
	 * @return The highest height that this jetpack has reached within the bounds of the window.
	 */
	public float getHighestWindowedHeight() { return highestWindowedHeight - startingHeight; }

	/**
	 * Return whether our jetpack has dropped off the bottom of our screen.
	 * Meaning that the user has lost this round.
	 * @return Whether our jetpack has dropped off the bottom of our screen.
	 */
	public boolean hasDroppedOut() {
	 	return this.posY < -Constants.JETPACK_WIDTH;
	}

	/**
	 * Returns whether the jetpack has launched.
	 * @return
	 */
	public boolean hasLaunched() {
		return this.hasLaunched;
	}
}