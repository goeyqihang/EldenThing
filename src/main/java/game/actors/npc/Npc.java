package game.actors.npc;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import game.behaviours.WanderBehaviour;
import game.buying.MerchantOffer;
import game.buying.PurchaseAction;
import game.capabilities.GeneralCapability;
import game.weapons.actions.AttackAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * An abstract class representing a Non-Player Character (NPC) in the game. NPCs extend
 * {@link Actor} and include functionalities for behaviors, monologues, and potentially offering
 * items for purchase. All NPCs are initialized with a {@link WanderBehaviour}. Concrete NPC classes
 * should define specific monologues and may add other behaviors or merchant capabilities.
 */
public abstract class Npc extends Actor implements Speakable {

    /**
     * A map of behaviors for this NPC, keyed by their priority. Lower integer values indicate
     * higher priority. Behaviors determine the NPC's actions during their turn.
     */
    private final Map<Integer, Behaviour> behaviours = new TreeMap<>();

    /**
     * The priority value used for the default wandering behavior.
     */
    private static final int PRIORITY_WANDER = 999;

    /**
     * A list of {@link MerchantOffer}s that this NPC can provide if they have the
     * {@link GeneralCapability#CAN_SELL} capability.
     */
    protected final List<MerchantOffer> offers = new ArrayList<>();

    /**
     * Constructor for an NPC. Initializes the NPC with a name, display character, and hit points.
     * Adds a default {@link WanderBehaviour} with a low priority.
     *
     * @param name        the name of the NPC
     * @param displayChar the character to represent the NPC on the map
     * @param hitPoints   initial and maximum health of the NPC
     */
    public Npc(String name, char displayChar, int hitPoints) {
        super(name, displayChar, hitPoints);
        this.addBehaviour(PRIORITY_WANDER, new WanderBehaviour());
    }

    /**
     * Returns the actions that other actors can perform on this NPC. By default, this includes a
     * {@link ListenAction} allowing other actors to hear monologues. If this NPC has the
     * {@link GeneralCapability#CAN_SELL} capability and the {@code otherActor} has the
     * {@link GeneralCapability#CAN_BUY} capability, {@link PurchaseAction}s for each of the NPC's
     * {@link #offers} are also added. It also allows {@link AttackAction} if the other actor has
     * {@link GeneralCapability#HOSTILE_TO_ENEMY}.
     *
     * @param otherActor the actor interacting with this NPC
     * @param direction  the direction of the other actor relative to this NPC
     * @param map        the current {@link GameMap}
     * @return an {@link ActionList} containing the actions that can be performed on this NPC
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        ActionList actions = super.allowableActions(otherActor, direction, map);
        if (otherActor.hasCapability(GeneralCapability.CAN_LISTEN)) {
            actions.add(new ListenAction(this));
        }

        // Check if this NPC can sell and the other actor can buy
        if (this.hasCapability(GeneralCapability.CAN_SELL) && otherActor.hasCapability(
                GeneralCapability.CAN_BUY)) {
            for (MerchantOffer offer : offers) {
                actions.add(new PurchaseAction(offer.createItem(), offer.getPrice(), this,
                        offer.getEffects()));
            }
        }

        if (otherActor.hasCapability(GeneralCapability.HOSTILE_TO_ENEMY)) {
            actions.add(new AttackAction(this, direction));
        }
        return actions;
    }

    /**
     * Defines the behavior of the NPC on its turn. The NPC iterates through its {@link #behaviours}
     * in order of priority (lowest number first). The first behavior that returns a non-null
     * {@link Action} will have that action executed. If no behavior provides an action, the NPC
     * performs a {@link DoNothingAction}.
     *
     * @param actions    the list of possible actions (typically not used directly by
     *                   behaviour-driven NPCs)
     * @param lastAction the action the actor did last turn
     * @param map        the map the actor is on
     * @param display    the I/O object to which messages may be written
     * @return the action to perform this turn, or {@link DoNothingAction} if no behavior dictates
     * an action.
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        // TreeMap iterates in ascending key order, so higher-priority behaviours come first
        for (Behaviour behaviour : behaviours.values()) {
            Action action = behaviour.getAction(this, map);
            if (action != null) {
                return action;
            }
        }
        // If no behaviour returned an action
        return new DoNothingAction();
    }

    /**
     * Adds a new behavior to the NPC with a specified priority. If a behavior with the same
     * priority already exists, it will be replaced. Lower priority numbers indicate higher priority
     * (executed first).
     *
     * @param priority  the priority of the behavior
     * @param behaviour the {@link Behaviour} to add
     */
    public void addBehaviour(int priority, Behaviour behaviour) {
        if (behaviour != null) {
            behaviours.put(priority, behaviour);
        }
    }
}
