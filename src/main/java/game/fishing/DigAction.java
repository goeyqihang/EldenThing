package game.fishing;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.Soil;

/**
 * An Action that allows an Actor to dig up the ground at a specific location,
 * replacing it with a new type of ground.
 */
public class DigAction extends Action {

    /**
     * The item used to perform the digging action.
     */
    private final Item digItem;

    /**
     * The location where the digging will take place.
     */
    private final Location digLocation;

    /**
     * The new ground that will replace the original ground after digging.
     */
    private final Ground newGround;

    /**
     * Constructor for DigAction.
     * By default, the dug ground will be replaced with Soil.
     *
     * @param digItem     The item used for digging (e.g., a Shovel).
     * @param digLocation The location to be dug.
     */
    public DigAction(Item digItem, Location digLocation) {
        this(digItem, digLocation, new Soil());
    }

    /**
     * Constructor for DigAction that allows specifying the new ground type.
     *
     * @param digItem     The item used for digging.
     * @param digLocation The location to be dug.
     * @param newGround   The ground type to replace the old one with.
     */
    public DigAction(Item digItem, Location digLocation, Ground newGround) {
        this.digItem = digItem;
        this.digLocation = digLocation;
        this.newGround = newGround;
    }

    /**
     * Executes the digging action.
     * The ground at the digLocation is replaced with the newGround.
     *
     * @param actor The actor performing the action.
     * @param map   The map the actor is on.
     * @return A string describing the result of the action.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        Ground originalGround = digLocation.getGround();
        digLocation.setGround(newGround);
        return actor + " digs up the " + originalGround + " with the " + digItem + ", leaving "
                + newGround + " behind.";
    }

    /**
     * Returns a description of this action suitable for displaying in a menu.
     *
     * @param actor The actor performing the action.
     * @return A string describing the action.
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " digs up the " + digLocation.getGround() + " at (" + digLocation.x() + ", "
                + digLocation.y() + ") with the " + digItem;
    }
}
