package com.dumbpug.eoeysb;

import com.badlogic.gdx.Gdx;

public class Constants {
	public static final float LOGO_SCALE 	                    = 0.17f;
	public static final float MOVEMENT_UNIT                     = Gdx.graphics.getHeight()*0.005f;
	public static final float METER 		                    = Gdx.graphics.getHeight()*0.03f;

	/** jetpack */
	public static final float JETPACK_WIDTH                     = Gdx.graphics.getWidth() / 2.5f;
	public static final float JETPACK_INITIAL_Y_OFFSET          = Gdx.graphics.getWidth() / 2.5f;
	public static final float JETPACK_GRAVITY                   = MOVEMENT_UNIT / 2.5f;
	public static final float JETPACK_MAX_VELOCITY              = MOVEMENT_UNIT * 1.6f;
	public static final float JETPACK_MAX_ROTATIONAL_VELOCITY   = 5f;
	public static final float JETPACK_ACCELERATION              = MOVEMENT_UNIT / 50f;
	public static final float JETPACK_ROTATIONAL_ACCELERATION   = 0.1f;
	public static final float JETPACK_ENGINE_OFFSET             = JETPACK_WIDTH * 0.357f;

	/** fuelling */
	public static final double FUELLING_TANK_LIMIT       = 120;
	public static final double FUELLING_TICK_USE         = 0.1;
}
