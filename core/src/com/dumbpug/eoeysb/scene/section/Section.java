package com.dumbpug.eoeysb.scene.section;

import com.dumbpug.eoeysb.scene.entities.Entity;
import com.dumbpug.eoeysb.scene.entities.IEntityFactory;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A section of a scene.
 * Sections are stacked vertically to build a navigable scene.
 */
public class Section {
    /**
     * The height of the section.
     */
    private int height;
    /**
     * The tokens held by this section when included as part of a lotto draw for generation.
     */
    private int tokens;
    /**
     * The height range in which this section can be generated.
     */
    private HeightRange heightRange;
    /**
     * The entity spawns for this section.
     */
    private ArrayList<EntitySpawn> entitySpawns;
    /**
     * The entity factories.
     */
    private HashMap<Integer, IEntityFactory> entityFactories;

    /**
     * Create a new instance of the Section class.
     * @param height The height of the section.
     * @param tokens The tokens held by this section when included as part of a lotto draw for generation.
     * @param heightRange The height range in which this section can be generated.
     * @param entitySpawns The entity spawns for this section.
     * @param entityFactories The entity factories.
     */
    public Section(int height, int tokens, HeightRange heightRange, ArrayList<EntitySpawn> entitySpawns, HashMap<Integer, IEntityFactory> entityFactories) {
        this.height          = height;
        this.tokens          = tokens;
        this.heightRange     = heightRange;
        this.entitySpawns    = entitySpawns;
        this.entityFactories = entityFactories;
    }

    /**
     * Gets the newly generated entities for this section.
     * @return The newly generated entities for this section.
     */
    public ArrayList<Entity> getSectionEntities() {
        // Create a list to hold our generated entities.
        ArrayList<Entity> generated = new ArrayList<Entity>();
        // We will generate an entity for each entity spawn defined for this section.
        for (EntitySpawn spawn : this.entitySpawns) {
            // We expect that there is a entity factory for this spawn.
            if (!this.entityFactories.containsKey(spawn.getEntityTypeID())) {
                throw new RuntimeException("There was no entity factory for entity type: " + spawn.getEntityTypeID());
            }
            // Create the entity.
            generated.add(this.entityFactories.get(spawn.getEntityTypeID()).create(spawn.getX(), spawn.getY()));
        }
        // Return the newly generated entities.
        return generated;
    }
}
