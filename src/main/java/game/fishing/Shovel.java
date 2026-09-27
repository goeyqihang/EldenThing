package game.fishing;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import game.capabilities.GeneralCapability;

/**
 * An item representing a Shovel.
 * A shovel can be used for digging and possesses the {@link GeneralCapability#CAN_DIG} capability.
 * Interestingly, it can also be found while fishing, thus it implements {@link Fishable}.
 */
public class Shovel extends Item implements Fishable {

    /**
     * The probability of finding a Shovel while fishing, which is 60%.
     */
    private static final double CATCH_RATE = 0.6;

    /**
     * Constructor for the Shovel.
     * Initializes the item with its name, display character, and makes it portable.
     * It also grants the CAN_DIG capability.
     */
    public Shovel() {
        super("Shovel", 'S', true);
        this.addCapability(GeneralCapability.CAN_DIG);
    }

    /**
     * Gets the probability of catching this Shovel while fishing.
     *
     * @return The catch rate of 0.6.
     */
    @Override
    public double getCatchChance() {
        return CATCH_RATE;
    }

    /**
     * Defines what happens when the Shovel is "caught" by an actor.
     * The shovel is added to the actor's inventory.
     *
     * @param actor The actor who caught the shovel.
     * @return A string describing the event.
     */
    @Override
    public String catchBy(Actor actor) {
        actor.addItemToInventory(this);
        return actor + " catches a Shovel. It seems like a lucky day... or not. What can it do?";
    }
}
