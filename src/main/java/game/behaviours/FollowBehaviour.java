package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.MoveActorAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.capabilities.GeneralCapability;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A behaviour that allows an {@link Actor} to follow a target {@link Actor}. The target is
 * dynamically found if not already set or if the current target becomes invalid (e.g., moves off
 * map, becomes unconscious, or loses the {@link GeneralCapability#FOLLOWABLE} capability). This
 * behaviour will attempt to move the actor one step closer to the target if a path that reduces the
 * Manhattan distance is available and the actor is not already adjacent to the target.
 */
public class FollowBehaviour implements Behaviour {

    /**
     * The current target Actor being followed. This can be null if no target is set or found.
     */
    private Actor currentTarget;

    /**
     * Determines an action for the actor to follow its target:
     * <ol>
     * <li>Validate the {@link #currentTarget}: if it is no longer on the map, is unconscious, or
     * has lost the {@link GeneralCapability#FOLLOWABLE} capability, it is dropped.</li>
     * <li>If there is no target, scan the adjacent locations and follow the first conscious actor
     * with the {@link GeneralCapability#FOLLOWABLE} capability.</li>
     * <li>If the target is not already next to the actor (Manhattan distance of 1), return a
     * {@link MoveActorAction} to an enterable adjacent location that strictly reduces the
     * Manhattan distance. Exits are shuffled so that ties are broken randomly.</li>
     * </ol>
     * If no target is found, or no move can reduce the distance to the current target, or the actor
     * is already adjacent, this method returns null.
     *
     * @param actor the {@link Actor} enacting the behaviour.
     * @param map   the {@link GameMap} containing the actor.
     * @return a {@link MoveActorAction} to move closer to the target, or null if no suitable action
     * is found.
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {
        // 1. Validate current target
        if (currentTarget != null && (!map.contains(currentTarget) || !currentTarget.isConscious()
                || !currentTarget.hasCapability(GeneralCapability.FOLLOWABLE))) {
            currentTarget = null; // Invalidate target
        }

        // 2. If no current target, try to find a new one in the surroundings
        if (currentTarget == null) {
            Location here = map.locationOf(actor);
            // Check adjacent tiles for a followable actor
            for (Exit exit : here.getExits()) {
                Location destination = exit.getDestination();
                if (map.isAnActorAt(destination)) {
                    Actor potentialTarget = map.getActorAt(destination);
                    if (potentialTarget.hasCapability(GeneralCapability.FOLLOWABLE)
                            && potentialTarget.isConscious()) {
                        currentTarget = potentialTarget; // Found a new target
                        break; // Follow the first one found
                    }
                }
            }
        }

        // 3. If a valid target exists, attempt to move closer
        if (currentTarget != null) {
            Location here = map.locationOf(actor);
            Location there = map.locationOf(currentTarget);

            int currentDistance = distance(here, there);
            // Already next to the target, no need to move closer
            if (currentDistance <= 1 && !here.equals(there)) {
                return null;
            }

            Action preferredAction = null;
            int bestDistance = currentDistance;

            // Shuffle exits so that ties between equally good moves are broken randomly
            List<Exit> exits = new ArrayList<>(here.getExits());
            Collections.shuffle(exits); // Randomize order of checking exits

            for (Exit exit : exits) {
                Location destination = exit.getDestination();
                if (destination.canActorEnter(actor)) {
                    int newDistance = distance(destination, there);
                    if (newDistance < bestDistance) { // Strictly move closer
                        bestDistance = newDistance;
                        preferredAction = new MoveActorAction(destination, exit.getName());
                    }
                }
            }
            return preferredAction; // This will be null if no path strictly reduces distance
        }

        return null; // No target or no way to move closer
    }

    /**
     * Compute the Manhattan distance between two locations. The Manhattan distance is the sum of
     * the absolute differences of their Cartesian coordinates.
     *
     * @param a the first {@link Location}.
     * @param b the second {@link Location}.
     * @return the number of steps between a and b if movement is restricted to the four cardinal
     * directions (horizontal and vertical).
     */
    private int distance(Location a, Location b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
    }
}
