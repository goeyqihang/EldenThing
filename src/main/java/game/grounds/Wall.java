package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;

/**
 * A class representing a wall, an impassable type of terrain. Walls are represented by '#' and
 * named "Wall". Actors cannot enter locations containing a Wall.
 *
 * @author Riordan D. Alfredo
 * Modified by: Goey Qi Hang
 */
public class Wall extends Ground {

    /**
     * Constructor for the Wall class. Sets the display character to '#' and the name to "Wall".
     */
    public Wall() {
        super('#', "Wall");
    }

    /**
     * Determines if an actor can enter this Wall. Walls are impassable.
     *
     * @param actor the Actor attempting to enter.
     * @return false always, as actors cannot enter Walls.
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return false;
    }
}
