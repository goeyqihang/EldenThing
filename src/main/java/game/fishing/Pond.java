package game.fishing;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.capabilities.GeneralCapability;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * A type of ground that represents a pond where an actor can fish.
 * It contains a variety of {@link Fishable} items that can be caught.
 * Actors cannot enter the pond.
 */
public class Pond extends Ground {

    /**
     * Creates the items that can be caught by fishing in this pond. Every fishing attempt gets
     * fresh instances, so each catch is a separate item.
     */
    private final List<Supplier<Fishable>> fishableFactories;

    /**
     * Constructor for Pond.
     * Initializes the ground with its display character and name, and populates the list of
     * fishable items.
     */
    public Pond() {
        super('~', "Pond");
        this.fishableFactories = new ArrayList<>();
        fishableFactories.add(SalmonFish::new);
        fishableFactories.add(ToxicEel::new);
        fishableFactories.add(Shovel::new);
        fishableFactories.add(GoldenFish::new);
        fishableFactories.add(OldBoot::new);
    }

    /**
     * Returns a list of allowable actions an actor can perform at this location.
     * If the actor has an item with the {@link GeneralCapability#CAN_FISH} capability,
     * a {@link FishAction} is made available.
     *
     * @param actor     The actor at the location.
     * @param location  The current location.
     * @param direction The direction of the ground from the actor.
     * @return A list of allowable actions.
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList actions = super.allowableActions(actor, location, direction);
        for (Item item : actor.getItemInventory()) {
            if (item.hasCapability(GeneralCapability.CAN_FISH)) {
                actions.add(new FishAction(item, this, location));
            }
        }
        return actions;
    }

    /**
     * Creates a new instance of every item that can be caught in this pond.
     *
     * @return A new list containing one new instance of each fishable item.
     */
    public List<Fishable> getFishableItems() {
        List<Fishable> fishableItems = new ArrayList<>();
        for (Supplier<Fishable> factory : fishableFactories) {
            fishableItems.add(factory.get());
        }
        return fishableItems;
    }

    /**
     * Determines whether an actor can enter this ground.
     *
     * @param actor The actor to check.
     * @return false, as actors cannot enter a Pond.
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return false;
    }
}
