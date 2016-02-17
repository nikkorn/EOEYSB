package com.dumbpug.NLAF.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dumbpug.NLAF.Main.JetPack;
import com.dumbpug.NLAF.Main.JetPackCollidableState;
import com.dumbpug.NLAF.Tools.Animation;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Nikolas Howard.
 */
public class EntityManager {
    public static final int MAX_PROPELLOR_FUEL_PODS = 3;
    public static final int MAX_ROCKET_FUEL_PODS = 3;
    public static final int MAX_BIRDS = 2;
    public static final int MAX_ALIEN_SHIPS = 1;
    public static final int MAX_PLANES = 1;
    public static final int MAX_ASTEROIDS = 3;

    // All potential spawn positions (y, x, rotation)
    private float[][] spawnPosition =  {
        {Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth()/2, 0},
        {Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth()/2, 0}
    };

    // RNG
    private Random ran = new Random();

    // Provides spawn details for new entities.
    private EntitySpawnPosition entitySpawnPosition = new EntitySpawnPosition();

    // Entity Pools
    private PropellorFuelPod[] propellorFuelPods;
    // private RocketFuelPod[] rocketFuelPods;
    // private Bird[] birds;
    // private Plane[] planes;
    // private AlienShip[] alienShips;
    // private Asteroid[] Asteroids;

    // Shared Animations/Sprites
    private Animation propellorFuelPodAnimation;

    public EntityManager() {
        // Initialise shared resources.
        propellorFuelPodAnimation = new Animation(new Texture(Gdx.files.internal("MiscImages/fuel_props.png")), 1, 6, (float) 1/12f);
        // ...

        // Initialise pools.
        propellorFuelPods = new PropellorFuelPod[MAX_PROPELLOR_FUEL_PODS];
        for(int i = 0; i < MAX_PROPELLOR_FUEL_PODS; i++) {
            propellorFuelPods[i] = new PropellorFuelPod(0,0,0);
        }

        // ...
    }

    /**
     * Generate all types of enities, based on player height.
     * @param skyHeightPercentage
     */
    public void generate(float skyHeightPercentage) {
        // Get current time.
        long currentTime = System.currentTimeMillis();

        // TODO eventually need to spawn certain entities at different heights.

        // ----------------------- PropellorFuelPod -----------------------
        // Has there been enough time since we last spawned this?
        if(currentTime > (PropellorFuelPod.lastSpawned + PropellorFuelPod.spawnCooldown)) {
            // Do we win a spawn?
            if(winsSpawn(PropellorFuelPod.spawnChance)){
                // We won! generate new entity IF there is an inactive one.
                for(PropellorFuelPod propellorFuelPod : propellorFuelPods) {
                    if(!propellorFuelPod.isActive()){
                        // Reuse this entity
                        // Get random spawn point
                        float[] spawnPoint = entitySpawnPosition.getPropellorFuelPodSpawnPosition(propellorFuelPod.getSpriteSize());
                        propellorFuelPod.setPosY(spawnPoint[0]);
                        propellorFuelPod.setPosX(spawnPoint[1]);
                        propellorFuelPod.setDirectionalRotation(spawnPoint[2]);
                        propellorFuelPod.setActive(true);

                        break;
                    }
                }
            }
        }
        // ----------------------- PropellerFuelPod -----------------------

        // ...
    }

    public void checkCollisions(JetPack jetPack) {
        // Get our jetpack collidable state.
        JetPackCollidableState collidableState = jetPack.getCollidableState();
        // Are we colliding with any fuel pods?
        for(PropellorFuelPod propellorFuelPod : propellorFuelPods) {
            if(propellorFuelPod.isActive()){
                // Has this entity collided with our engines?
                if(collidableState.collidesWithLeftEngine(propellorFuelPod.getOriginX(), propellorFuelPod.getOriginY(), propellorFuelPod.getCollisionRadius())) {
                    propellorFuelPod.leftEngineCollision(jetPack);
                } else if (collidableState.collidesWithRightEngine(propellorFuelPod.getOriginX(), propellorFuelPod.getOriginY(), propellorFuelPod.getCollisionRadius())) {
                    propellorFuelPod.rightEngineCollision(jetPack);
                }
            }
        }

        // ...
    }

    public boolean winsSpawn(int spawnChance) {
        return ran.nextInt(1000) <= spawnChance;
    }

    /**
     * Draw all active entities.
     * @param batch
     */
    public void draw(SpriteBatch batch) {
        // Draw active PropellorFuelPod objects
        TextureRegion propellorFuelPodAnimationTexture = propellorFuelPodAnimation.getCurrentFrame(true);
        for(PropellorFuelPod propellorFuelPod : propellorFuelPods) {
            if(propellorFuelPod.isActive()){
                propellorFuelPod.draw(batch, propellorFuelPodAnimationTexture);
            }
        }

        // ...
    }

    public void moveEntities(double playerOffset) {
        // Move active PropellorFuelPod objects
        for(PropellorFuelPod propellorFuelPod : propellorFuelPods) {
            if(propellorFuelPod.isActive()){
                propellorFuelPod.move(playerOffset);
            }
        }

        // ...
    }

    public PropellorFuelPod[] getPropellorFuelPods() { return this.propellorFuelPods; }
}
