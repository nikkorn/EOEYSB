package com.dumbpug.eoeysb.scene.section;

import com.badlogic.gdx.utils.JsonValue;

/**
 * A height range.
 */
public class HeightRange {
    /**
     * The lowest height of the range.
     */
    private Integer from;
    /**
     * The greatest height of the range.
     */
    private Integer to;

    /**
     * Create a new instance of the HeightRange class.
     * @param from The lowest height of the range.
     * @param to The greatest height of the range.
     */
    private HeightRange(Integer from, Integer to) {
        this.from = from;
        this.to   = to;
    }

    /**
     * Create a HeightRange instance based on a JSON representation of a range.
     * @param rangeJSON The JSON representation of a range.
     * @return The height range.
     */
    public static HeightRange fromJSONValue(JsonValue rangeJSON) {
        int from = rangeJSON.getInt("from", -1);
        int to   = rangeJSON.getInt("to", -1);
        return new HeightRange(from == -1 ? null : from, to == -1 ? null : to);
    }

    /**
     * Gets whether the specified height is within this range.
     * @param height The height that we are comparing against this range.
     * @return Whether the specified height is within this range.
     */
    public boolean isHeightInRange(int height) {
        // Do we even have a from/to range?
        if (this.from == null && this.to == null) {
            // No range is defined, so this height is super ok.
            return true;
        } else if (this.from == null) {
            // If the lowest point of the range was not defined then
            // all we care about is if the height is lower than or
            // equal to the highest point of the range.
            return height <= this.to;
        } else if (this.to == null) {
            // If the highest point of the range was not defined then
            // all we care about is if the height is greater than or
            // equal to the lowest point of the range.
            return height >= this.from;
        } else {
            // We have properly defined range so get whether our height is within it.
            return (height >= this.from) && (height <= this.to);
        }
    }
}
