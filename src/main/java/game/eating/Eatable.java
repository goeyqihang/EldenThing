package game.eating;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * Interface for entities (items or actors) that can be eaten by an Actor.
 */
public interface Eatable {

    /**
     * Performs the action of being eaten by an actor.
     *
     * @param eater The actor eating this entity.
     * @param map   The map the actor is on.
     * @return A string describing the result of being eaten.
     */
    String eatenBy(Actor eater, GameMap map);

    /**
     * A short description of the eat action for display in a menu.
     *
     * @param actor The actor who might eat this entity.
     * @return A menu description, e.g., "Farmer eats Omen Sheep Egg (Max HP +10)".
     */
    String getEatMenuDescription(Actor actor);
}
