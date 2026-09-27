package game.fishing;

import edu.monash.fit2099.engine.actors.Actor;

/**
 * An interface for entities that can be caught while fishing.
 * Classes implementing this interface must define their catch probability and the specific
 * action that occurs when they are caught by an actor.
 */
public interface Fishable {

    /**
     * Gets the probability of this item being caught.
     * This should be a value between 0.0 (never caught) and 1.0 (always caught).
     *
     * @return a double representing the chance of catching this item.
     */
    double getCatchChance();

    /**
     * Defines what happens when this item is successfully caught by an actor.
     * This typically involves adding the item to the actor's inventory and may include other
     * effects.
     *
     * @param actor The actor who caught the item.
     * @return A string describing the event of the item being caught.
     */
    String catchBy(Actor actor);
}
