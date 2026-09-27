package game.teleport;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.capabilities.GeneralCapability;
import java.util.ArrayList;
import java.util.List;

/**
 * A teleportation gate that allows actors to travel between different maps.
 * The gate is represented by the character 'A' and can have multiple destinations.
 */
public class TeleportationGate extends Ground {

    /**
     * The destinations this gate can teleport an actor to.
     */
    private final List<Location> locations;

    /**
     * Constructor for TeleportationGate.
     * Initializes the gate with the character 'A'.
     */
    public TeleportationGate() {
        super('A', "Teleportation Gate");
        this.locations = new ArrayList<>();
    }

    /**
     * Add a destination to this teleportation gate.
     *
     * @param targetLocation the destination location
     */
    public void addDestination(Location targetLocation) {
        locations.add(targetLocation);
    }

    /**
     * Returns the actions that can be performed on this teleportation gate.
     * Provides teleportation options to all configured destinations.
     *
     * @param actor     the actor performing the action
     * @param location  the current location
     * @param direction the direction of the action (not used for teleportation)
     * @return a list of possible teleport actions
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList actions = super.allowableActions(actor, location, direction);
        if (actor.hasCapability(GeneralCapability.CAN_TELEPORT)) {
            // Only allow teleportation if the actor is standing ON this teleportation gate
            if (location.getGround() == this && location.containsAnActor()
                    && location.getActor() == actor) {
                // Add teleport actions for each destination
                for (Location targetLocation : locations) {
                    // Skip destinations that are the gate's own location
                    if (!isSameLocation(location, targetLocation)) {
                        actions.add(new TeleportAction(targetLocation));
                    }
                }
            }
        }
        return actions;
    }

    /**
     * Check if the current location is the same as the target location.
     *
     * @param currentLocation the current location
     * @param targetLocation  the target location to check
     * @return true if locations are the same, false otherwise
     */
    private boolean isSameLocation(Location currentLocation, Location targetLocation) {
        return currentLocation.map() == targetLocation.map() &&
                currentLocation.x() == targetLocation.x() &&
                currentLocation.y() == targetLocation.y();
    }

    /**
     * Check if actors can enter this teleportation gate.
     * Teleportation gates allow actors to step on them.
     *
     * @param actor the actor attempting to enter
     * @return true, as actors can step on teleportation gates
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return true;
    }
}
