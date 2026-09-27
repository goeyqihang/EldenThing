package game.behaviours.behaviourselectors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.positions.GameMap;
import java.util.Map;

/**
 * Strategy interface for selecting behaviours for creatures. Different implementations can provide
 * different selection strategies.
 */
public interface BehaviourSelector {

    /**
     * Selects a behaviour from the given behaviours and returns the action it produces.
     *
     * @param behaviours the available behaviours, keyed by priority (lower values first)
     * @param actor      the actor performing the behaviour
     * @param map        the current game map
     * @return the Action to be performed, or null if no valid action is found
     */
    Action selectBehaviour(Map<Integer, Behaviour> behaviours, Actor actor, GameMap map);
}
