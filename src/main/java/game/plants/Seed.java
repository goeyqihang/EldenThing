package game.plants;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.GroundCapability;
import game.plants.actions.PlantAction;

/**
 * An abstract base class for seed items in the game.
 * Seeds are portable {@link Item}s that can be planted by an {@link Actor}
 * on suitable ground to grow into a specific type of {@link Plant}.
 * Subclasses must define the type of plant created and the stamina cost for planting.
 */
public abstract class Seed extends Item {

    /**
     * Constructor for the Seed class.
     * Initializes the seed item with a given name, a default display character '*',
     * and marks it as portable.
     *
     * @param seedName The name of the seed (e.g., "Bloodrose Seed").
     */
    public Seed(String seedName) {
        // Use '*' as the default display character for all seeds, mark as portable
        super(seedName, '*', true);
    }

    /**
     * Abstract method that must be implemented by subclasses to define
     * which specific {@link Plant} instance is created when this seed is planted.
     *
     * @return A new instance of the {@link Plant} corresponding to this seed type.
     */
    public abstract Plant createPlant();

    /**
     * Abstract method that must be implemented by subclasses to define
     * the stamina cost required for an {@link Actor} to plant this seed.
     *
     * @return The integer value representing the stamina cost.
     */
    public abstract int getStaminaCost();

    /**
     * Returns a list of allowable actions that the owner can perform with this Seed item.
     * If the owner is standing on ground that has the {@link GroundCapability#CAN_PLANT_SEED}
     * capability, this method adds a {@link PlantAction} to the list, allowing the owner to plant
     * this seed.
     *
     * @param owner The {@link Actor} carrying the Seed.
     * @param map   The {@link GameMap} the owner is currently on.
     * @return An {@link ActionList} containing the {@link PlantAction} if planting is possible at
     * the owner's location.
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = super.allowableActions(owner, map);
        Location here = map.locationOf(owner);

        // Seeds can only be planted on ground that allows it
        if (here.getGround().hasCapability(GroundCapability.CAN_PLANT_SEED)) {
            actions.add(new PlantAction(this, here));
        }
        return actions;
    }
}
