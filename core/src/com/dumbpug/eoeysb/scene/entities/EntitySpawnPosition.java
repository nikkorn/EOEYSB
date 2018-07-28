package com.dumbpug.eoeysb.scene.entities;

import com.badlogic.gdx.Gdx;

import java.util.Random;

/**
 * Created by Nikolas Howard
 */
public class EntitySpawnPosition {
    private Random ran = new Random();

    // Fuel pod spawn.
    private float[][] fuelPodSpawnPosition =  {
            {Gdx.graphics.getHeight(), 0, 190, 230},
            {Gdx.graphics.getHeight(), Gdx.graphics.getWidth()*0.33f, 160, 200},
            {Gdx.graphics.getHeight(), Gdx.graphics.getWidth()*0.66f, 160, 200},
            {Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), 130, 170}
    };

    /**
     * Randomly picks a spawn position for a new fule pod from the array of potential spawns.
     * @param screenSpriteOffset
     * @return
     */
    public float[] getFuelPodSpawnPosition(int screenSpriteOffset) {
        float[] spawnPosition = fuelPodSpawnPosition[ran.nextInt(fuelPodSpawnPosition.length)];
        int rotationalAngleDifference = (int) (spawnPosition[3] - spawnPosition[2]);
        // Return a randomly picked spawn point with a random directional angle.
        return new float[] {spawnPosition[0] + screenSpriteOffset, spawnPosition[1], ran.nextInt(rotationalAngleDifference) + spawnPosition[2]};
    }
}
