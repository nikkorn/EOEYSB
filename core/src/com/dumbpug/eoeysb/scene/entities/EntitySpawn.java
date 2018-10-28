package com.dumbpug.eoeysb.scene.entities;

import com.badlogic.gdx.utils.JsonValue;

/**
 * Defines a type and position for a spawn-able entity.
 */
public class EntitySpawn {
    /**
     * The entity type id.
     */
    private int entityTypeID;
    /**
     * The position to spawn the entity at.
     * This is relative to the section bounds.
     */
    private int x, y;

    /**
     * Create an instance of the EntitySpawn class.
     * @param entityTypeID Th entity type id.
     * @param x The x spawn position.
     * @param y The y spawn position.
     */
    private EntitySpawn(int entityTypeID, int x, int y) {
        this.entityTypeID = entityTypeID;
        this.x            = x;
        this.y            = y;
    }

    /**
     * Create a EntitySpawn instance based on a JSON representation of a spawn.
     * @param spawnJSON The JSON representation of a spawn.
     * @return The entity spawn.
     */
    public static EntitySpawn fromJSONValue(JsonValue spawnJSON) {
        int entityTypeID = spawnJSON.getInt("id");
        int x            = spawnJSON.getInt("x");
        int y            = spawnJSON.getInt("y");
        return new EntitySpawn(entityTypeID, x, y);
    }

    /**
     * Get the entity type id.
     * @return The entity type id.
     */
    public int getEntityTypeID() {
        return this.entityTypeID;
    }

    /**
     * Get the x spawn position.
     * @return The x spawn position.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Get the y spawn position.
     * @return The y spawn position.
     */
    public int getY() {
        return this.y;
    }
}
