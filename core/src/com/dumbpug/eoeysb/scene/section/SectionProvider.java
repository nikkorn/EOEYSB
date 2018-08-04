package com.dumbpug.eoeysb.scene.section;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.dumbpug.eoeysb.scene.entities.IEntityFactory;
import com.dumbpug.eoeysb.scene.entities.factories.FloatingFuelPodFactory;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Provider of scene sections.
 */
public class SectionProvider {
    /**
     * The entity factories.
     */
    private HashMap<Integer, IEntityFactory> entityFactories;
    /**
     * The sections that can be returned by this provider.
     */
    private ArrayList<Section> sections;
    /**
     * The last section returned.
     */
    private Section lastSectionReturned;

    /**
     * Create a new instance of the SectionProvider class.
     */
    public SectionProvider() {
        // Create the entity factories map.
        this.entityFactories = createEntityFactories();
        // Create a reader for the JSON file containing the section details.
        JsonValue sectionDetailsArray = new JsonReader().parse(Gdx.files.internal("section_details.json"));
        // Create the sections.
        this.sections = createSections(sectionDetailsArray);
    }

    public Section getNextSection(int height) {
        return null;
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
            // Get the section height.
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
