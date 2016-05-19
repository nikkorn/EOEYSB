package com.dumbpug.NLAF.Math;

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
    public static ScreenPoint getTargetPosition(double x, double y, double angle, double distance, ScreenPoint result) {
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
     * @param circleBsRadius
     * @return intersect
     */
    public static boolean circlesIntersect(double circleAposX, double circleAposY, double circleARadius,
                                           double circleBposX, double circleBposY, double circleBsRadius) {

        double dist = Math.sqrt((circleAposX - circleBposX) * (circleAposX - circleBposX)
             + (circleAposY - circleBposY) * (circleAposY - circleBposY));

        if (Math.abs((circleARadius - circleBsRadius)) <= dist && dist <= (circleARadius + circleBsRadius)) {
            return true;
        } else {
            return false;
        }
    }
}
