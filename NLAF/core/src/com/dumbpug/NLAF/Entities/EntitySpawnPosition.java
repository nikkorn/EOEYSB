package com.dumbpug.NLAF.Entities;

import com.badlogic.gdx.Gdx;

import java.util.Random;

/**
 * Created by Nikolas Howard
 */
public class EntitySpawnPosition {
    private Random ran = new Random();

    // Propellor fuel pod spawn.
    private float[][] propellorFuelPodSpawnPosition =  {
            {Gdx.graphics.getHeight(), 0, 190, 230},
            {Gdx.graphics.getHeight(), Gdx.graphics.getWidth()*0.33f, 160, 200},
            {Gdx.graphics.getHeight(), Gdx.graphics.getWidth()*0.66f, 160, 200},
            {Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), 130, 170}
    };

    public float[] getPropellorFuelPodSpawnPosition(int screenSpriteOffset) {
        float[] spawnPosition = propellorFuelPodSpawnPosition[ran.nextInt(propellorFuelPodSpawnPosition.length)];
        int rotationalAngleDifference = (int) (spawnPosition[3] - spawnPosition[2]);
        // Return a randomly picked spawn point with a random directional angle.
        return new float[] {spawnPosition[0] + screenSpriteOffset, spawnPosition[1], ran.nextInt(rotationalAngleDifference) + spawnPosition[2]};
    }
}
