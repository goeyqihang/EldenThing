package game.fishing;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.capabilities.GeneralCapability;
import game.eating.EatAction;
import game.eating.Eatable;

/**
 * Represents a Salmon Fish item that can be caught from a pond.
 * This fish can be eaten to restore a small amount of health.
 * It implements both {@link Eatable} and {@link Fishable}.
 */
public class SalmonFish extends Item implements Eatable, Fishable {

    /**
     * The catch rate for the Salmon Fish, which is 30%.
     */
    private static final double CATCH_RATE = 0.3;

    /**
     * The hit points restored when the Salmon Fish is eaten.
     */
    private static final int HEAL_AMOUNT = 10;

    /**
     * Constructor for the SalmonFish.
     * Initializes the item with its name, display character, and makes it portable.
     */
    public SalmonFish() {
        super("Salmon Fish", 'S', true);
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
     * Gets the probability of catching this Salmon Fish.
     *
     * @return The catch rate of 0.3.
     */
    @Override
    public double getCatchChance() {
        return CATCH_RATE;
    }

    /**
     * Defines what happens when the Salmon Fish is caught by an actor.
     * The fish is added to the actor's inventory.
     *
     * @param actor The actor who caught the fish.
     * @return A string describing the event.
     */
    @Override
    public String catchBy(Actor actor) {
        actor.addItemToInventory(this);
        return actor + " catches a Salmon Fish. It looks delicious!";
    }

    /**
     * Defines what happens when the Salmon Fish is eaten.
     * The eater is healed for {@value #HEAL_AMOUNT} HP, and the fish is removed from their
     * inventory.
     *
     * @param eater The actor eating the fish.
     * @param map   The map the actor is on.
     * @return A string describing the result of eating the fish.
     */
    @Override
    public String eatenBy(Actor eater, GameMap map) {
        eater.removeItemFromInventory(this);
        eater.heal(HEAL_AMOUNT);
        return eater + " eats the " + this + " and heals " + HEAL_AMOUNT + " HP.";
    }

    /**
     * Returns a description for the menu when an actor can eat this fish.
     *
     * @param actor The actor who can eat the fish.
     * @return A string for the eat action menu.
     */
    @Override
    public String getEatMenuDescription(Actor actor) {
        return actor + " eats the " + this;
    }
}
