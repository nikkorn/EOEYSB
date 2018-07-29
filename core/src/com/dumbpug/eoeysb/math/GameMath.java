package com.dumbpug.eoeysb.math;

/**
 * Created by Nikolas Howard.
 */
public class GameMath {

    /**
     * Get the position at angle and distance from old position.
     * @param x
     * @param y
     * @param angle
     * @param distance
     * @return
     */
    public static Position getTargetPosition(double x, double y, double angle, double distance, Position result) {
        result.setX(Math.cos(angle * Math.PI / 180) * distance + x);
        result.setY(Math.sin(angle * Math.PI / 180) * distance + y);
        return result;
    }

    /**
     * Calculates whether two circle intersect.
     * @param circleAposX
     * @param circleAposY
     * @param circleARadius
     * @param circleBposX
     * @param circleBposY
     * @param circleBRadius
     * @return intersect
     */
    public static boolean circlesIntersect(double circleAposX, double circleAposY, double circleARadius,
                                           double circleBposX, double circleBposY, double circleBRadius) {

        double distance = Math.sqrt((circleAposX - circleBposX) * (circleAposX - circleBposX)
             + (circleAposY - circleBposY) * (circleAposY - circleBposY));

        return (Math.abs((circleARadius - circleBRadius)) <= distance && distance <= (circleARadius + circleBRadius));
    }
}
