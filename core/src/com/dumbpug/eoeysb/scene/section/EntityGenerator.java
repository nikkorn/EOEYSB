package com.dumbpug.eoeysb.scene.section;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.dumbpug.eoeysb.Constants;
import com.dumbpug.eoeysb.scene.entities.Entity;
import com.dumbpug.eoeysb.scene.entities.IEntityFactory;
import com.dumbpug.eoeysb.scene.entities.factories.FloatingFuelPodFactory;
import com.dumbpug.lotto.Lotto;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Generator of scene entities.
 */
public class EntityGenerator {
    /**
     * The entity factories.
     */
    private HashMap<Integer, IEntityFactory> entityFactories;
    /**
     * The sections that can be used to generate entities.
     */
    private ArrayList<Section> sections;
    /**
     * The last section used to generate entities.
     */
    private Section lastSection;
    /**
     * Get the height in meters (from the ground) of the next section to generate.
     */
    private int nextSectionHeight = Constants.SCENE_FIRST_SECTION_HEIGHT_METERS;

    /**
     * Create a new instance of the EntityGenerator class.
     */
    public EntityGenerator() {
        // Create the entity factories map.
        this.entityFactories = createEntityFactories();
        // Create a reader for the JSON file containing the section details.
        JsonValue sectionDetailsArray = new JsonReader().parse(Gdx.files.internal("section_details.json"));
        // Create the sections.
        this.sections = createSections(sectionDetailsArray);
    }

    /**
     * Get any entities generated for the current height in meters.
     * @param height The current height in meters.
     * @return Any entities generated for the current height in meters.
     */
    public ArrayList<Entity> getEntitiesForHeight(int height) {
        // Do not do anything if we have not reached the height (from the ground) of the next section.
        if (height < this.nextSectionHeight) {
            return new ArrayList<Entity>();
        }
        // Get all of the sections that can be generated at the current height.
        ArrayList<Section> sectionsForHeight = new ArrayList<Section>();
        for (Section section : this.sections) {
            // We should never use the section that was last used ot generate entities.
            if (section == this.lastSection) {
                continue;
            }
            // Can this section be generated at the current height?
            if (section.getHeightRange().isHeightInRange(height)) {
                sectionsForHeight.add(section);
            }
        }
        // Create a lotto with which to pick the next section.
        Lotto<Section> sectionLotto = new Lotto<Section>();
        for (Section section : sectionsForHeight) {
            sectionLotto.add(section, section.getTokens());
        }
        // Pick a winning section.
        Section nextSection = sectionLotto.draw();
        this.lastSection    = nextSection;
        // Set the next height at which to generate section entities.
        this.nextSectionHeight += nextSection.getHeight();
        // Generate and return the entities for the winning section.
        return nextSection.getSectionEntities();
    }

    /**
     * Create the entity factories.
     * @return The entity factories.
     */
    private HashMap<Integer, IEntityFactory> createEntityFactories() {
        HashMap<Integer, IEntityFactory> factories = new HashMap<Integer, IEntityFactory>();
        factories.put(0, new FloatingFuelPodFactory());
        return factories;
    }

    /**
     * Create the sections based on a JSON array of section definitions
     * @param sectionDetailsArray The JSON array of section definitions.
     * @return The sections.
     */
    private ArrayList<Section> createSections(JsonValue sectionDetailsArray) {
        ArrayList<Section> sections = new ArrayList<Section>();
        // Create a section for each value in our section details array.
        for (JsonValue sectionDetails : sectionDetailsArray.iterator()) {
            // Get the section height (in meters).
            int sectionHeight = sectionDetails.getInt("height");
            // Get the section tokens.
            int sectionTokens = sectionDetails.getInt("tokens");
            // Get the section height range.
            HeightRange heightRange = HeightRange.fromJSONValue(sectionDetails.get("height_range"));
            // Get the section entity spawns.
            ArrayList<EntitySpawn> entitySpawns = new ArrayList<EntitySpawn>();
            for (JsonValue entitySpawnJson : sectionDetails.get("entity_spawns").iterator()) {
                entitySpawns.add(EntitySpawn.fromJSONValue(entitySpawnJson));
            }
            // Add the section.
            sections.add(new Section(sectionHeight, sectionTokens, heightRange, entitySpawns, this.entityFactories));
        }
        // Return the created sections.
        return sections;
    }
}
