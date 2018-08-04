package com.dumbpug.eoeysb.scene.entities;

/**
 * Factory for creating a game entity.
 */
public interface IEntityFactory {

    /**
     * Create the Entity.
     * @param x The x position of the entity.
     * @param y The y position of the entity.
     * @return The Entity.
     */
    Entity create(int x, int y);

    /**
     * Get the id of the entity that this factory creates;
     * @return The id of the entity that this factory creates;
     */
    int getEntityId();
}
