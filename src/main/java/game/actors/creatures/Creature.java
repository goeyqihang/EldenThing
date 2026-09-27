package game.actors.creatures;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import game.behaviours.behaviourselectors.BehaviourSelector;
import game.behaviours.behaviourselectors.PriorityBehaviourSelector;
import java.util.Map;
import java.util.TreeMap;

/**
 * An abstract base class for creatures within the game world. This class extends the base
 * {@link Actor} class and provides a common framework for managing creature behaviors using a
 * priority-based system. Subclasses are expected to define their specific characteristics and add
 * relevant behaviours.
 */
public abstract class Creature extends Actor {

    /**
     * The behaviours assigned to this creature, keyed by priority. Lower integer values indicate
     * higher priority; a {@link TreeMap} keeps them sorted in that order.
     */
    private final Map<Integer, Behaviour> behaviours;

    /**
     * The strategy used to select which behaviour to execute from the available behaviours.
     */
    private final BehaviourSelector behaviourSelector;

    /**
     * Whether {@link #initializeBehaviours()} has run yet.
     */
    private boolean behavioursInitialized = false;

    /**
     * Constructor for the Creature class. Behaviours are selected in priority order, using a
     * {@link PriorityBehaviourSelector}.
     *
     * @param name        the name of the Creature
     * @param displayChar the character that will represent the Creature in the display
     * @param hitPoints   the Creature's starting hit points
     */
    public Creature(String name, char displayChar, int hitPoints) {
        this(name, displayChar, hitPoints, new PriorityBehaviourSelector());
    }

    /**
     * Constructor with custom behaviour selector.
     *
     * @param name              the name of the creature
     * @param displayChar       the character used to represent the creature
     * @param hitPoints         the creature's hit points
     * @param behaviourSelector the strategy for selecting behaviours
     */
    public Creature(String name, char displayChar, int hitPoints,
            BehaviourSelector behaviourSelector) {
        super(name, displayChar, hitPoints);
        this.behaviours = new TreeMap<>();
        this.behaviourSelector = behaviourSelector;
    }

    /**
     * Template method for subclasses to register their behaviours using
     * {@link #addBehaviour(int, Behaviour)}. It is called once, just before the creature's first
     * turn, rather than from the constructor, so that it never runs on a partially constructed
     * subclass.
     */
    protected abstract void initializeBehaviours();

    /**
     * Adds a behaviour to this creature. A behaviour with the same priority is replaced.
     *
     * @param priority  the priority of the behaviour; lower values are considered first
     * @param behaviour the behaviour to add
     */
    protected void addBehaviour(int priority, Behaviour behaviour) {
        behaviours.put(priority, behaviour);
    }

    /**
     * Selects and returns an action to perform on the current turn based on assigned behaviours and
     * the configured behaviour selection strategy. The BehaviourSelector determines which behaviour
     * to execute from the available behaviours.
     *
     * @param actions    collection of possible Actions for this Actor (typically not used directly
     *                   here, but provided by the engine)
     * @param lastAction The Action this Actor took the last turn. Can do interesting things in
     *                   conjunction with Action.getNextAction()
     * @param map        the {@link GameMap} containing the Actor
     * @param display    the {@link Display} I/O object to which messages may be written
     * @return the Action to be performed, determined by the behaviour selector, or DoNothingAction
     * if no valid action is available.
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        if (!behavioursInitialized) {
            initializeBehaviours();
            behavioursInitialized = true;
        }

        // Use the behaviour selector to choose an action from available behaviours
        Action selectedAction = behaviourSelector.selectBehaviour(behaviours, this, map);

        if (selectedAction != null) {
            return selectedAction;
        }

        // If no behaviour produces a valid action, do nothing
        return new DoNothingAction();
    }
}
