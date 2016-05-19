package com.dumbpug.NLAF.Main;

import com.dumbpug.NLAF.Math.GameMath;
import com.dumbpug.NLAF.Math.ScreenPoint;

/**
 * Created by Nikolas Howard.
 */
public class JetPackCollidableState {
    private ScreenPoint leftEngineOriginScreenPoint;
    private ScreenPoint rightEngineOriginScreenPoint;
    private double radius;

    public JetPackCollidableState(ScreenPoint leftEngineOrigin, ScreenPoint rightEngineOrigin, double diameter) {
        this.leftEngineOriginScreenPoint = leftEngineOrigin;
        this.rightEngineOriginScreenPoint = rightEngineOrigin;
        this.radius = (diameter/2)*0.85;
    }

    public void set(ScreenPoint leftEngineOrigin, ScreenPoint rightEngineOrigin) {
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
