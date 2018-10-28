package com.dumbpug.eoeysb.scene.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.eoeysb.Constants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * A collection of entities.
 */
public class Entities {
    /**
     * The list of active entities.
     */
    private ArrayList<Entity> entities = new ArrayList<Entity>();
    /**
     * The entity generator.
     */
    private EntityGenerator entityGenerator = new EntityGenerator();

    /**
     * Get an immutable list of all entities.
     * @return An immutable list of all entities.
     */
    public Collection<Entity> getAll() { return Collections.unmodifiableList(entities); }

    /**
     * Update the entities in the collection.
     * @param greatestHeight The greatest height reached by the player.
     * @param playerOffset The player offset to apply to each entity position.
     */
    public void update(float greatestHeight, float playerOffset) {
        // Attempt to generate some new entities for a current height.
        this.entities.addAll(this.entityGenerator.getEntitiesForHeight((int)greatestHeight));
        // Iterate over and update all entities, removing any that are inactive or outside the screen bounds.
        Iterator<Entity> entitiesIterator = entities.iterator();
        while (entitiesIterator.hasNext()) {
            // Get the next entity.
            Entity entity = entitiesIterator.next();
            // If the entity is out of the screen bounds or is inactive then we need to get rid of it.
            if (Entities.isEntityOutOfScreenBounds(entity) || !entity.isActive()) {
                // Remove the entity.
                entitiesIterator.remove();
                // Move on to the next entity.
                continue;
            }
            // Update the current entity.
            entity.update(playerOffset);
        }
    }

    /**
     * Draw all active entities.
     * @param batch The sprite batch to use in drawing the entities.
     */
    public void draw(SpriteBatch batch) {
        for (Entity entity : this.entities) {
            if (entity.isActive()) {
                entity.draw(batch);
            }
        }
    }

    /**
     * Gets whether an entity is out of screen bounds.
     * @return Whether an entity is out of screen bounds.
     */
    private static boolean isEntityOutOfScreenBounds(Entity entity) {
        return entity.getY() < -Constants.SCENE_ACTIVE_BOUNDS;
    }
}
