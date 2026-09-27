package game.behaviours.behaviourselectors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.positions.GameMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A behaviour selector that randomly picks one behaviour to try. If the selected behaviour is not
 * valid, the actor does nothing.
 */
public class RandomBehaviourSelector implements BehaviourSelector {

    /**
     * Random number generator used to pick a behaviour.
     */
    private final Random random;

    /**
     * Constructor for RandomBehaviourSelector.
     */
    public RandomBehaviourSelector() {
        this.random = new Random();
    }

    /**
     * Select behaviour randomly. Picks one behaviour at random and tries it. If it's not valid,
     * returns null (creature does nothing this turn).
     *
     * @param behaviours the available behaviours, keyed by priority
     * @param actor      the actor performing the behaviour
     * @param map        the current game map
     * @return the Action from the randomly selected behaviour, or null
     */
    @Override
    public Action selectBehaviour(Map<Integer, Behaviour> behaviours, Actor actor, GameMap map) {
        if (behaviours.isEmpty()) {
            return null;
        }

        // Convert behaviours to a list to enable random access by index
        List<Behaviour> behaviourList = new ArrayList<>(behaviours.values());

        // Randomly select one behaviour
        int randomIndex = random.nextInt(behaviourList.size());
        Behaviour selectedBehaviour = behaviourList.get(randomIndex);

        // Try the selected behaviour - if invalid, return null (do nothing)
        return selectedBehaviour.getAction(actor, map);
    }
}
