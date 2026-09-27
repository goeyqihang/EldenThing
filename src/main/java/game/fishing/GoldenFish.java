package game.fishing;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.capabilities.GeneralCapability;
import game.eating.EatAction;
import game.eating.Eatable;

/**
 * A rare and valuable item that can be caught while fishing.
 * The Golden Fish can be eaten for a significant healing effect or kept for its monetary value,
 * which is awarded immediately upon being caught.
 * It implements both {@link Eatable} and {@link Fishable}.
 */
public class GoldenFish extends Item implements Eatable, Fishable {

    /**
     * The catch rate for the Golden Fish, which is 5%.
     */
    private static final double CATCH_RATE = 0.05;

    /**
     * The runes awarded immediately when the Golden Fish is caught.
     */
    private static final int RUNES_WHEN_CAUGHT = 1000;

    /**
     * The hit points restored when the Golden Fish is eaten.
     */
    private static final int HEAL_AMOUNT = 50;

    /**
     * Constructor for the GoldenFish.
     * Initializes the item with its name, display character, and makes it portable.
     */
    public GoldenFish() {
        super("Golden Fish", 'G', true);
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
     * Gets the probability of catching this Golden Fish.
     *
     * @return The catch rate of 0.05.
     */
    @Override
    public double getCatchChance() {
        return CATCH_RATE;
    }

    /**
     * Defines what happens when the Golden Fish is caught by an actor.
     * The fish is added to the actor's inventory, and the actor gains {@value #RUNES_WHEN_CAUGHT}
     * runes.
     *
     * @param actor The actor who caught the fish.
     * @return A string describing the event.
     */
    @Override
    public String catchBy(Actor actor) {
        actor.addItemToInventory(this);
        actor.addBalance(RUNES_WHEN_CAUGHT);
        return actor + " catches a shimmering Golden Fish! What a stroke of luck: "
                + RUNES_WHEN_CAUGHT + " runes gained!";
    }

    /**
     * Defines what happens when the Golden Fish is eaten.
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
        return eater + " eats the shimmering Golden Fish. It feels invigorating!";
    }

    /**
     * Returns a description for the menu when an actor can eat this fish.
     *
     * @param actor The actor who can eat the fish.
     * @return A string for the eat action menu.
     */
    @Override
    public String getEatMenuDescription(Actor actor) {
        return actor + " eats the Golden Fish";
    }
}
