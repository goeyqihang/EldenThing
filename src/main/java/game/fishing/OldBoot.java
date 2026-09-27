package game.fishing;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;

/**
 * A piece of junk that can be caught while fishing.
 * It serves no purpose other than cluttering inventory.
 * It implements the {@link Fishable} interface.
 */
public class OldBoot extends Item implements Fishable {

    /**
     * The catch rate for the Old Boot, which is 40%.
     */
    private static final double CATCH_RATE = 0.4;

    /**
     * Constructor for the OldBoot.
     * Initializes the item with its name, display character, and makes it portable.
     */
    public OldBoot() {
        super("Old Boot", '&', true);
    }

    /**
     * Gets the probability of catching this Old Boot.
     *
     * @return The catch rate of 0.4.
     */
    @Override
    public double getCatchChance() {
        return CATCH_RATE;
    }

    /**
     * Defines what happens when the Old Boot is caught by an actor.
     * The boot is added to the actor's inventory.
     *
     * @param actor The actor who caught the boot.
     * @return A string describing the event.
     */
    @Override
    public String catchBy(Actor actor) {
        actor.addItemToInventory(this);
        return actor + " catches an old boot. It seems useless...";
    }
}
