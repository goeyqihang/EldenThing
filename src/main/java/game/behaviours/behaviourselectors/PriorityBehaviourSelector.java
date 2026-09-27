package game.behaviours.behaviourselectors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.positions.GameMap;
import java.util.Map;

/**
 * A behaviour selector that tries behaviours in priority order. This is the traditional approach -
 * try each behaviour in sequence until one returns a valid action.
 */
public class PriorityBehaviourSelector implements BehaviourSelector {

    /**
     * Select behaviour by priority order. Goes through the behaviour list in order and returns the
     * first valid action.
     *
     * @param behaviours the available behaviours, keyed by priority (lower values first)
     * @param actor      the actor performing the behaviour
     * @param map        the current game map
     * @return the first valid Action found, or null if none are valid
     */
    @Override
    public Action selectBehaviour(Map<Integer, Behaviour> behaviours, Actor actor, GameMap map) {
        for (Behaviour behaviour : behaviours.values()) {
            Action action = behaviour.getAction(actor, map);
            if (action != null) {
                return action; // Return first valid action found
            }
        }
        return null; // No valid behaviour found
    }
}
