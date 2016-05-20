import com.dumbpug.NLAF.Math.GameMath;
import com.dumbpug.NLAF.Math.ScreenPoint;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by nik on 11/05/16.
 */
public class GameMathUT {

    @Test
    public void checkCirclesIntersect() {
        // Circles directly overlap
        assertTrue(GameMath.circlesIntersect(1,1,1,1,1,1));
        // Circles meet at edge
        assertTrue(GameMath.circlesIntersect(1,1,1,3,1,1));
        // Narrow gap between circles
        assertFalse(GameMath.circlesIntersect(1,1,1,3.1,1,1));
    }

    @Test
    public void checkScreenPoint() {
        ScreenPoint sp = new ScreenPoint();
        // Move right
        GameMath.getTargetPosition(1, 1, 0, 1, sp);
        assertTrue(sp.getX() == 2);
        // Move left
        GameMath.getTargetPosition(1, 1, 180, 1, sp);
        assertTrue(sp.getX() == 0);
        // Move up
        GameMath.getTargetPosition(1, 1, 90, 1, sp);
        assertTrue(sp.getY() == 2);
        // Move down
        GameMath.getTargetPosition(1, 1, 270, 1, sp);
        assertTrue(sp.getY() == 0);
    }
}
