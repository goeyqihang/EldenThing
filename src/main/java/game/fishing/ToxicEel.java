package game.fishing;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.capabilities.GeneralCapability;
import game.eating.EatAction;
import game.eating.Eatable;
import game.effects.ContinuousDamageEffect;

/**
 * Represents a Toxic Eel, a dangerous item that can be caught while fishing.
 * Catching the eel applies a damage-over-time effect to the catcher.
 * It can also be eaten, which will harm the consumer.
 * It implements both {@link Eatable} and {@link Fishable}.
 */
public class ToxicEel extends Item implements Eatable, Fishable {

    /**
     * The catch rate for the Toxic Eel, which is 50%.
     */
    private static final double CATCH_RATE = 0.5;

    /**
     * How many turns the catcher is shocked for after catching the eel.
     */
    private static final int SHOCK_DURATION = 2;

    /**
     * The damage dealt to the catcher on each turn of the shock.
     */
    private static final int SHOCK_DAMAGE_PER_TURN = 5;

    /**
     * The damage dealt to an actor that eats the eel.
     */
    private static final int DAMAGE_WHEN_EATEN = 10;

    /**
     * Constructor for the ToxicEel.
     * Initializes the item with its name, display character, and makes it portable.
     */
    public ToxicEel() {
        super("Toxic Eel", 'C', true);
    }

    /**
     * Returns the list of allowable actions for this item.
     * If the owner has the {@link GeneralCapability#CONSUMER} capability, an {@link EatAction} is
     * added.
     *
     * @param owner The actor who owns this item.
     * @param map   The map the owner is on.
     * @return A list of allowable actions.
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = super.allowableActions(owner, map);
        if (owner.hasCapability(GeneralCapability.CONSUMER)) {
            actions.add(new EatAction(this));
        }
        return actions;
    }

    /**
     * Gets the probability of catching this Toxic Eel.
     *
     * @return The catch rate of 0.5.
     */
    @Override
    public double getCatchChance() {
        return CATCH_RATE;
    }

    /**
     * Defines what happens when the Toxic Eel is caught by an actor.
     * The eel is added to the actor's inventory, and a shocking status effect is applied to the
     * actor.
     *
     * @param actor The actor who caught the eel.
     * @return A string describing the event.
     */
    @Override
    public String catchBy(Actor actor) {
        actor.addItemToInventory(this);
        actor.addStatusEffect(new ContinuousDamageEffect("Shocked by Toxic Eel", SHOCK_DURATION,
                SHOCK_DAMAGE_PER_TURN));
        return actor + " catches a Toxic Eel. It feels like a shocking experience! Ouch!";
    }

    /**
     * Defines what happens when the Toxic Eel is eaten.
     * The eater is hurt for {@value #DAMAGE_WHEN_EATEN} HP, and the eel is removed from their
     * inventory. The eater is knocked out if this drops their health to zero.
     *
     * @param eater The actor eating the eel.
     * @param map   The map the actor is on.
     * @return A string describing the result of eating the eel.
     */
    @Override
    public String eatenBy(Actor eater, GameMap map) {
        eater.removeItemFromInventory(this);
        eater.hurt(DAMAGE_WHEN_EATEN);
        String result = eater + " eats the " + this + " and takes " + DAMAGE_WHEN_EATEN
                + " damage.";
        if (!eater.isConscious()) {
            result += "\n" + eater.unconscious(map);
        }
        return result;
    }

    /**
     * Returns a description for the menu when an actor can eat this eel.
     *
     * @param actor The actor who can eat the eel.
     * @return A string for the eat action menu.
     */
    @Override
    public String getEatMenuDescription(Actor actor) {
        return actor + " eats the " + this;
    }
}
