package game.fishing;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * An Action that allows an Actor to fish in a Pond.
 * When executed, this action determines what, if anything, is caught based on the
 * catch chances of the items available in the pond.
 */
public class FishAction extends Action {

    /**
     * The pond where the fishing is taking place.
     */
    private final Pond pond;
    /**
     * The item used for fishing, e.g., a FishingRod.
     */
    private final Item fishingItem;
    /**
     * The location of the pond.
     */
    private final Location pondLocation;
    /**
     * A random number generator to determine the outcome of the fishing attempt.
     */
    private final Random random = new Random();

    /**
     * Constructor for FishAction.
     *
     * @param fishingRod   The item being used to fish.
     * @param pond         The pond to fish in.
     * @param pondLocation The location of the pond.
     */
    public FishAction(Item fishingRod, Pond pond, Location pondLocation) {
        this.fishingItem = fishingRod;
        this.pond = pond;
        this.pondLocation = pondLocation;
    }

    /**
     * Executes the fishing action.
     * It iterates through all fishable items in the pond and rolls a chance to catch each one.
     * If one or more items are potentially caught, one is chosen at random and given to the actor.
     *
     * @param actor The actor performing the action.
     * @param map   The map the actor is on.
     * @return A string describing the result of the fishing attempt.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        List<Fishable> fishableItems = pond.getFishableItems();
        List<Fishable> potentialCatches = new ArrayList<>();
        // Roll for each fishable item independently
        for (Fishable fishableItem : fishableItems) {
            if (random.nextDouble() < fishableItem.getCatchChance()) {
                potentialCatches.add(fishableItem);
            }
        }

        if (potentialCatches.isEmpty()) {
            return "You are unlucky! Nothing was caught...";
        } else {
            // If there are potential catches, pick one at random from the list.
            Fishable caughtItem = potentialCatches.get(random.nextInt(potentialCatches.size()));

            // Add the single, randomly selected caught item to the actor's inventory.
            return caughtItem.catchBy(actor);
        }
    }

    /**
     * Returns a description of this action suitable for displaying in a menu.
     *
     * @param actor The actor performing the action.
     * @return A string describing the action.
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " fishes at the Pond at (" + pondLocation.x() + ", " + pondLocation.y()
                + ") with the " + fishingItem;
    }
}
