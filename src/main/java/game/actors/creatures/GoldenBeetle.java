package game.actors.creatures;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.behaviours.FollowBehaviour;
import game.behaviours.ProduceBehaviour;
import game.behaviours.WanderBehaviour;
import game.behaviours.behaviourselectors.BehaviourSelector;
import game.capabilities.GeneralCapability;
import game.eating.EatAction;
import game.eating.Eatable;
import game.hatching.GoldenBeetleEgg;
import game.weapons.actions.AttackAction;

/**
 * Represents a Golden Beetle creature in the game. Golden Beetles can be eaten by actors with the
 * {@link GeneralCapability#CONSUMER} capability, providing healing and runes. They follow actors
 * with the {@link GeneralCapability#FOLLOWABLE} capability using {@link FollowBehaviour}, wander
 * using {@link WanderBehaviour} if no target is found, and produce {@link GoldenBeetleEgg}
 * periodically using {@link ProduceBehaviour}. They can also be attacked by actors with
 * {@link GeneralCapability#HOSTILE_TO_ENEMY}.
 */
public class GoldenBeetle extends Creature implements ActorProducible, Eatable {

    /**
     * The name of the Golden Beetle.
     */
    private static final String NAME = "Golden Beetle";
    /**
     * The display character of the Golden Beetle on the map.
     */
    private static final char DISPLAY_CHAR = 'b';
    /**
     * The initial hit points of the Golden Beetle.
     */
    private static final int HIT_POINTS = 25;
    /**
     * The number of turns that must pass before the Golden Beetle can produce another egg.
     */
    private static final int EGG_PRODUCTION_INTERVAL = 5;
    /**
     * Hit points restored to an actor that eats the Golden Beetle.
     */
    private static final int HEAL_WHEN_EATEN = 15;
    /**
     * Runes gained by an actor that eats the Golden Beetle.
     */
    private static final int RUNES_WHEN_EATEN = 1000;

    /**
     * Counter for turns since the last egg was produced.
     */
    private int turnsSinceEggProduced = 0;

    /**
     * Priority for the {@link ProduceBehaviour}.
     */
    private static final int PRIORITY_PRODUCE = 0;
    /**
     * Priority for the {@link FollowBehaviour}.
     */
    private static final int PRIORITY_FOLLOW = 5;
    /**
     * Priority for the {@link WanderBehaviour}.
     */
    private static final int PRIORITY_WANDER = 999;

    /**
     * Constructor for GoldenBeetle with default priority behaviour selector. Initializes the Golden
     * Beetle with its name, display character, hit points, and adds its capabilities.
     */
    public GoldenBeetle() {
        super(NAME, DISPLAY_CHAR, HIT_POINTS);
        this.addCapability(GeneralCapability.CONSUMABLE_ON_MAP);
    }

    /**
     * Constructor with custom behaviour selector.
     *
     * @param behaviourSelector the strategy for selecting behaviours
     */
    public GoldenBeetle(BehaviourSelector behaviourSelector) {
        super(NAME, DISPLAY_CHAR, HIT_POINTS, behaviourSelector);
        this.addCapability(GeneralCapability.CONSUMABLE_ON_MAP);
    }

    /**
     * Initialize the behaviours for Golden Beetle. Priority order: Reproduce -> Follow -> Wander
     */
    @Override
    protected void initializeBehaviours() {
        this.addBehaviour(PRIORITY_PRODUCE, new ProduceBehaviour(this));
        this.addBehaviour(PRIORITY_FOLLOW, new FollowBehaviour());
        this.addBehaviour(PRIORITY_WANDER, new WanderBehaviour());
    }

    /**
     * Called each turn. Increments the egg production counter and then defers to the parent
     * Creature's playTurn logic to select an action based on behaviours.
     *
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn.
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return the Action to be performed
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        this.turnsSinceEggProduced++;
        return super.playTurn(actions, lastAction, map, display);
    }

    /**
     * Checks if the Golden Beetle can produce an egg. It can produce if the number of turns since
     * the last egg production meets or exceeds {@value #EGG_PRODUCTION_INTERVAL}.
     *
     * @param producer The Golden Beetle (unused, context is this instance).
     * @param map      The game map (unused).
     * @return true if the beetle can produce an egg, false otherwise.
     */
    @Override
    public boolean canProduceOffspring(Actor producer, GameMap map) {
        return this.turnsSinceEggProduced >= EGG_PRODUCTION_INTERVAL;
    }

    /**
     * Produces a {@link GoldenBeetleEgg} at the Golden Beetle's current location and resets the egg
     * production timer.
     *
     * @param producer The Golden Beetle (this instance).
     * @param map      The game map where the egg will be placed.
     * @return A string describing that the beetle laid an egg.
     */
    @Override
    public String produceOffspring(Actor producer, GameMap map) {
        Location producerLocation = map.locationOf(producer);
        GoldenBeetleEgg egg = new GoldenBeetleEgg();
        producerLocation.addItem(egg);
        this.turnsSinceEggProduced = 0; // Reset counter
        return producer + " lays a " + egg + " at (" + producerLocation.x() + ","
                + producerLocation.y() + ")!";
    }

    /**
     * Returns a list of allowable actions that other actors can perform on this GoldenBeetle. If
     * the other actor has the {@link GeneralCapability#CONSUMER} capability, an {@link EatAction}
     * is added. If the other actor has the {@link GeneralCapability#HOSTILE_TO_ENEMY} capability,
     * an {@link AttackAction} is added.
     *
     * @param otherActor The actor that might perform an action.
     * @param direction  String representing the direction of the other Actor.
     * @param map        The current GameMap.
     * @return An {@link ActionList} containing the allowable actions.
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        ActionList actionsList = super.allowableActions(otherActor, direction, map);

        if (otherActor.hasCapability(GeneralCapability.CONSUMER)) {
            actionsList.add(new EatAction(this));
        }

        if (otherActor.hasCapability(GeneralCapability.HOSTILE_TO_ENEMY)) {
            actionsList.add(new AttackAction(this, direction));
        }

        return actionsList;
    }

    /**
     * Defines what happens when this Golden Beetle is eaten. The eater is healed for
     * {@value #HEAL_WHEN_EATEN} HP, gains {@value #RUNES_WHEN_EATEN} runes, and the beetle is
     * removed from the map.
     *
     * @param eater The actor eating this beetle.
     * @param map   The game map.
     * @return A string describing the eating event and its effects.
     */
    @Override
    public String eatenBy(Actor eater, GameMap map) {
        eater.heal(HEAL_WHEN_EATEN);
        eater.addBalance(RUNES_WHEN_EATEN);
        map.removeActor(this);

        return eater + " eats " + this + ", healing for " + HEAL_WHEN_EATEN + " HP and gaining "
                + RUNES_WHEN_EATEN + " runes.";
    }

    /**
     * Returns the menu description for the {@link EatAction} when targeting this Golden Beetle.
     *
     * @param actor The actor that might eat this beetle.
     * @return A string for the eat action menu, e.g., "Player eats Golden Beetle".
     */
    @Override
    public String getEatMenuDescription(Actor actor) {
        return actor + " eats " + this;
    }
}
