package game.teleport;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Action to teleport an actor to a different location/map.
 */
public class TeleportAction extends Action {

    /**
     * The destination of the teleport.
     */
    private final Location location;

    /**
     * Constructor for TeleportAction.
     *
     * @param location the destination to teleport to
     */
    public TeleportAction(Location location) {
        this.location = location;
    }

    /**
     * Execute the teleportation.
     *
     * @param actor the actor performing the action
     * @param map the current game map
     * @return a description of the action result
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        GameMap targetMap = location.map();

        // Check if target map is valid
        if (targetMap == null) {
            return actor + " failed to teleport - invalid destination!";
        }

        // Check if target location is already occupied
        if (location.containsAnActor()) {
            return actor + " failed to teleport - destination is blocked!";
        }

        // Check if actor can enter the target location
        if (!location.canActorEnter(actor)) {
            return actor + " failed to teleport - cannot enter destination!";
        }

        // All checks passed - perform teleportation
        map.removeActor(actor);
        targetMap.addActor(actor, location);

        return actor + " teleports to " + location;
    }

    /**
     * Returns a description of this action suitable for displaying in a menu.
     *
     * @param actor the actor performing the action
     * @return a string describing the action
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " teleports to " + location;
    }
}
