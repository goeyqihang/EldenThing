package game.actors.creatures;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * Interface for Actors that can produce offspring or lay eggs.
 */
public interface ActorProducible {

    /**
     * Checks if the Actor can produce offspring/egg in the current turn.
     *
     * @param producer The producing actor.
     * @param map      The game map.
     * @return true if it can produce, false otherwise.
     */
    boolean canProduceOffspring(Actor producer, GameMap map);

    /**
     * Produces offspring or an egg.
     *
     * @param producer The producing actor.
     * @param map      The game map.
     * @return A string describing the production event, or null if no message.
     */
    String produceOffspring(Actor producer, GameMap map);
}
