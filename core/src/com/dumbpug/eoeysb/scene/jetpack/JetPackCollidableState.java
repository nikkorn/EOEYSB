package com.dumbpug.eoeysb.scene.jetpack;

import com.dumbpug.eoeysb.Math.GameMath;

/**
 * Created by Nikolas Howard.
 */
public class JetPackCollidableState {
    private com.dumbpug.eoeysb.Math.ScreenPoint leftEngineOriginScreenPoint;
    private com.dumbpug.eoeysb.Math.ScreenPoint rightEngineOriginScreenPoint;
    private double radius;

    public JetPackCollidableState(com.dumbpug.eoeysb.Math.ScreenPoint leftEngineOrigin, com.dumbpug.eoeysb.Math.ScreenPoint rightEngineOrigin, double diameter) {
        this.leftEngineOriginScreenPoint = leftEngineOrigin;
        this.rightEngineOriginScreenPoint = rightEngineOrigin;
        this.radius = (diameter/2)*0.85;
    }

    public void set(com.dumbpug.eoeysb.Math.ScreenPoint leftEngineOrigin, com.dumbpug.eoeysb.Math.ScreenPoint rightEngineOrigin) {
        this.leftEngineOriginScreenPoint = leftEngineOrigin;
        this.rightEngineOriginScreenPoint = rightEngineOrigin;
    }

    public boolean collidesWithLeftEngine(double collidablePositionX, double collidablePositionY, double collidableRadius) {
        return GameMath.circlesIntersect(leftEngineOriginScreenPoint.getX(), leftEngineOriginScreenPoint.getY(), radius,
                collidablePositionX, collidablePositionY, collidableRadius);
    }

    public boolean collidesWithRightEngine(double collidablePositionX, double collidablePositionY, double collidableRadius) {
        return GameMath.circlesIntersect(rightEngineOriginScreenPoint.getX(), rightEngineOriginScreenPoint.getY(), radius,
                collidablePositionX, collidablePositionY, collidableRadius);
    }
}
