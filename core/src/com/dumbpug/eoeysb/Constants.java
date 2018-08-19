package com.dumbpug.eoeysb;

import com.badlogic.gdx.Gdx;

public class Constants {
	public static final float LOGO_SCALE 	                    = 0.17f;
	public static final float MOVEMENT_UNIT                     = Gdx.graphics.getWidth() * 0.0075f;
	public static final float METER 		                    = Gdx.graphics.getWidth() * 0.1f;

	/** Scene */
	public static final int SCENE_HEIGHT_METERS                 = 2500;
	public static final int SCENE_FIRST_SECTION_HEIGHT_METERS   = 25;
	public static final float SCENE_DROPOUT                     = METER * 4;

	/** Jetpack */
	public static final float JETPACK_WIDTH                     = Gdx.graphics.getWidth() / 2.5f;
	public static final float JETPACK_HEIGHT                    = JETPACK_WIDTH * 0.357f;
	public static final float JETPACK_INITIAL_Y_OFFSET          = Gdx.graphics.getWidth() / 2.5f;
	public static final float JETPACK_GRAVITY                   = MOVEMENT_UNIT / 2.5f;
	public static final float JETPACK_MAX_VELOCITY              = MOVEMENT_UNIT * 1.6f;
	public static final float JETPACK_MAX_ROTATIONAL_VELOCITY   = 7.5f;
	public static final float JETPACK_ACCELERATION              = MOVEMENT_UNIT / 35f;
	public static final float JETPACK_ROTATIONAL_ACCELERATION   = 1.5f;
	public static final float JETPACK_ENGINE_OFFSET             = JETPACK_WIDTH * 0.357f;
	public static final float JETPACK_ENGINE_COLLISION_RADIUS   = (JETPACK_HEIGHT / 2f) * 0.85f;

	/** Fuelling */
	public static final double FUELLING_TANK_LIMIT              = 120;
	public static final double FUELLING_TICK_USE                = 0.1;
}
