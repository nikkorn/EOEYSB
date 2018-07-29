package com.dumbpug.eoeysb.scene.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.ArrayList;
import java.util.Random;

/**
 * Manager of entities in a scene.
 */
public class EntityManager {
    public static final int MAX_PROPELLOR_FUEL_PODS = 3;
    public static final int MAX_FLOATING_FUEL_PODS = 3;
    public static final int MAX_BIRDS = 2;
    public static final int MAX_ALIEN_SHIPS = 1;
    public static final int MAX_PLANES = 1;
    public static final int MAX_ASTEROIDS = 3;

    // Generates random numbers used in deciding whether an entity wins a spawn.
    private Random ran = new Random();

    // Provides spawn details for new entities.
    private EntitySpawnPosition entitySpawnPosition = new EntitySpawnPosition();

    // Entity Pools
    private com.dumbpug.eoeysb.scene.entities.PropellorFuelPod[] propellorFuelPods;
    private com.dumbpug.eoeysb.scene.entities.FloatingFuelPod[] floatingFuelPods;
    // private Bird[] birds;
    // private Plane[] planes;
    // private AlienShip[] alienShips;
    // private Asteroid[] Asteroids;

    // Shared Animations/Sprites
    private com.dumbpug.eoeysb.tools.Animation propellorFuelPodAnimation;
    private Sprite floatingFuelPodSprite;

    public EntityManager() {
        // Initialise shared resources.
        propellorFuelPodAnimation = new com.dumbpug.eoeysb.tools.Animation(new Texture(Gdx.files.internal("MiscImages/fuel_props.png")), 1, 6, (float) 1/12f);
        floatingFuelPodSprite = new Sprite(new Texture(Gdx.files.internal("MiscImages/fuel_floating.png")));
        // ...

        // Initialise pools.
        propellorFuelPods = new com.dumbpug.eoeysb.scene.entities.PropellorFuelPod[MAX_PROPELLOR_FUEL_PODS];
        for(int i = 0; i < MAX_PROPELLOR_FUEL_PODS; i++) {
            propellorFuelPods[i] = new com.dumbpug.eoeysb.scene.entities.PropellorFuelPod(0,0,0);
        }

        floatingFuelPods = new com.dumbpug.eoeysb.scene.entities.FloatingFuelPod[MAX_FLOATING_FUEL_PODS];
        for(int i = 0; i < MAX_FLOATING_FUEL_PODS; i++) {
            floatingFuelPods[i] = new com.dumbpug.eoeysb.scene.entities.FloatingFuelPod(0,0,0);
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
        // Need to spawn certain entities at different heights. Nothing below 2% as this is the launch area
        if(skyHeightPercentage > 2) {
            //Are we in the atmosphere?
            if(skyHeightPercentage < 50) {
                // ----------------------- PropellorFuelPod -----------------------
                // Has there been enough time since we last spawned this?
                if(currentTime > (com.dumbpug.eoeysb.scene.entities.PropellorFuelPod.lastSpawned + com.dumbpug.eoeysb.scene.entities.PropellorFuelPod.spawnCooldown)) {
                    // Do we win a spawn?
                    if(winsSpawn(com.dumbpug.eoeysb.scene.entities.PropellorFuelPod.spawnChance)){
                        // We won! generate new entity IF there is an inactive one.
                        for(com.dumbpug.eoeysb.scene.entities.PropellorFuelPod propellorFuelPod : propellorFuelPods) {
                            if(!propellorFuelPod.isActive()){
                                // Reuse this entity
                                // Get random spawn point
                                float[] spawnPoint = entitySpawnPosition.getFuelPodSpawnPosition(propellorFuelPod.getSpriteSize());
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
            } else {
                // ----------------------- FloatingFuelPod -----------------------
                // Has there been enough time since we last spawned this?
                if(currentTime > (com.dumbpug.eoeysb.scene.entities.FloatingFuelPod.lastSpawned + com.dumbpug.eoeysb.scene.entities.FloatingFuelPod.spawnCooldown)) {
                    // Do we win a spawn?
                    if(winsSpawn(com.dumbpug.eoeysb.scene.entities.FloatingFuelPod.spawnChance)){
                        // We won! generate new entity IF there is an inactive one.
                        for(com.dumbpug.eoeysb.scene.entities.FloatingFuelPod floatingFuelPod : floatingFuelPods) {
                            if(!floatingFuelPod.isActive()){
                                // Reuse this entity
                                // Get random spawn point
                                float[] spawnPoint = entitySpawnPosition.getFuelPodSpawnPosition(floatingFuelPod.getSpriteSize());
                                floatingFuelPod.setPosY(spawnPoint[0]);
                                floatingFuelPod.setPosX(spawnPoint[1]);
                                floatingFuelPod.setDirectionalRotation(spawnPoint[2]);
                                floatingFuelPod.setActive(true);
                                break;
                            }
                        }
                    }
                }
                // ----------------------- FloatingFuelPod -----------------------
            }
        }
    }

    /**
     * Get all active entities.
     * @return All active entities.
     */
    public ArrayList<Entity> getActiveEntities() {
        // Create a list to hold all of the entities.
        ArrayList<Entity> entities = new ArrayList<Entity>();
        // Are we colliding with any propellor fuel pods?
        for(PropellorFuelPod propellorFuelPod : propellorFuelPods) {
            if(propellorFuelPod.isActive()){
                entities.add(propellorFuelPod);
            }
        }
        // Are we colliding with any floating fuel pods?
        for(FloatingFuelPod floatingFuelPod : floatingFuelPods) {
            if(floatingFuelPod.isActive()){
                entities.add(floatingFuelPod);
            }
        }
        // Return all of our active entities.
        return entities;
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
        for(com.dumbpug.eoeysb.scene.entities.PropellorFuelPod propellorFuelPod : propellorFuelPods) {
            if(propellorFuelPod.isActive()){
                propellorFuelPod.draw(batch, propellorFuelPodAnimationTexture);
            }
        }
        // Draw active FloatingFuelPod objects.
        for(com.dumbpug.eoeysb.scene.entities.FloatingFuelPod floatingFuelPod : floatingFuelPods) {
            if(floatingFuelPod.isActive()){
                floatingFuelPod.draw(batch, floatingFuelPodSprite);
            }
        }
    }

    public void moveEntities(double playerOffset) {
        // Move active PropellorFuelPod objects
        for(com.dumbpug.eoeysb.scene.entities.PropellorFuelPod propellorFuelPod : propellorFuelPods) {
            if(propellorFuelPod.isActive()){
                propellorFuelPod.move(playerOffset);
            }
        }
        // Move active FloatingFuelPod objects
        for(com.dumbpug.eoeysb.scene.entities.FloatingFuelPod floatingFuelPod : floatingFuelPods) {
            if(floatingFuelPod.isActive()){
                floatingFuelPod.move(playerOffset);
            }
        }
    }
}
