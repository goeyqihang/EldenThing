package game.grounds;

import edu.monash.fit2099.engine.positions.Ground;

/**
 * A class that represents the floor inside a building. It is represented by the character '_' and
 * named "Floor". This ground type uses the default behaviors inherited from {@link Ground},
 * typically meaning it is passable by actors and does not block thrown objects.
 *
 * @author Riordan D. Alfredo
 * Modified by: Goey Qi Hang
 */
public class Floor extends Ground {

    /**
     * Constructor for the Floor class. Sets the display character to '_' and the name to "Floor".
     */
    public Floor() {
        super('_', "Floor");
    }
}
