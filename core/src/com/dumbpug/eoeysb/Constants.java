package com.dumbpug.eoeysb;

import com.badlogic.gdx.Gdx;

/**
 * Game constants.
 */
public class Constants {
	public static final float LOGO_SCALE 	                    = 0.17f;
	public static final float MOVEMENT_UNIT                     = Gdx.graphics.getWidth() * 0.0075f;
	public static final float METER 		                    = Gdx.graphics.getWidth() * 0.1f;

	/** Scene */
	public static final int SCENE_HEIGHT_METERS                 = 2500;
	public static final int SCENE_FIRST_SECTION_HEIGHT_METERS   = 25;
	public static final float SCENE_DROPOUT                     = METER * 4;
	public static final float SCENE_ACTIVE_BOUNDS               = Gdx.graphics.getWidth() / 2f;

	/** Jetpack */
	public static final float JETPACK_WIDTH                     = Gdx.graphics.getWidth() / 2.5f;
	public static final float JETPACK_HEIGHT                    = JETPACK_WIDTH * 0.357f;
	public static final float JETPACK_INITIAL_Y_OFFSET          = Gdx.graphics.getWidth() / 2.5f;
	public static final float JETPACK_GRAVITY                   = 0.9f; // MOVEMENT_UNIT / 2.5f;
	public static final float JETPACK_MAX_VELOCITY              = MOVEMENT_UNIT * 1.6f;
	public static final float JETPACK_MAX_ROTATIONAL_VELOCITY   = 3.5f;
	public static final float JETPACK_ACCELERATION              = MOVEMENT_UNIT / 25f;
	public static final float JETPACK_ROTATIONAL_ACCELERATION   = 0.2f;
	public static final float JETPACK_ENGINE_OFFSET             = JETPACK_WIDTH * 0.357f;
	public static final float JETPACK_ENGINE_COLLISION_RADIUS   = (JETPACK_HEIGHT / 2f) * 0.85f;

	/** Fuelling */
	public static final double FUELLING_TANK_LIMIT              = 120;
	public static final double FUELLING_TICK_USE                = 0.1;

	/** Entities */
	public static final float FUEL_POD_SIZE                     = METER;
}
