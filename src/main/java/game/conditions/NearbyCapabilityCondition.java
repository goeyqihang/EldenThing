package game.conditions;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import java.util.ArrayList;

/**
 * A {@link Condition} that checks if any {@link edu.monash.fit2099.engine.GameEntity} (i.e.,
 * {@link edu.monash.fit2099.engine.positions.Ground}, {@link Actor}, or {@link Item}) at a
 * specified central {@link Location} or any of its adjacent (directly connected by an {@link Exit})
 * locations possesses a specific {@link Enum} capability.
 * <p>
 * The check includes:
 * <ul>
 * <li>The ground at the central location and its exits.</li>
 * <li>Any actor at the central location and its exits.</li>
 * <li>Any items on the ground at the central location and its exits.</li>
 * </ul>
 * If any of these entities have the specified capability, the condition is met.
 */
public class NearbyCapabilityCondition implements Condition {

    /**
     * The central location around which to check for the capability. The check includes this
     * location and all locations accessible via its direct exits.
     */
    private final Location centerLocation;
    /**
     * The capability to search for in nearby game entities.
     */
    private final Enum<?> capability;

    /**
     * Constructs a {@code NearbyCapabilityCondition}.
     *
     * @param centerLocation The {@link Location} that serves as the center point for the capability
     *                       check. Must not be null.
     * @param capability     The specific {@link Enum} capability to look for in entities at and
     *                       around the {@code centerLocation}. Must not be null.
     */
    public NearbyCapabilityCondition(Location centerLocation, Enum<?> capability) {
        this.centerLocation = centerLocation;
        this.capability = capability;
    }

    /**
     * Checks if the specified {@link #capability} is present in any game entity at the
     * {@link #centerLocation} or any of its adjacent locations. It iterates through the
     * {@link #centerLocation} and all locations reachable through its {@link Exit}s. For each of
     * these locations, it checks:
     * <ol>
     * <li>If the {@link edu.monash.fit2099.engine.positions.Ground} at the location has the
     * capability.</li>
     * <li>If there is an {@link Actor} at the location, whether that actor has the capability.</li>
     * <li>For every {@link Item} on the ground at the location, whether that item has the
     * capability.</li>
     * </ol>
     * The method returns {@code true} as soon as the first entity with the capability is found.
     * If no such entity is found after checking all relevant locations and entities,
     * it returns {@code false}.
     *
     * @return {@code true} if an entity with the specified capability is found at or adjacent to
     * the center location, {@code false} otherwise.
     */
    @Override
    public boolean check() {
        // Create a list of locations to check: the center and all its direct exits
        ArrayList<Location> locationsToCheck = new ArrayList<>();
        locationsToCheck.add(centerLocation);
        for (Exit exit : centerLocation.getExits()) {
            locationsToCheck.add(exit.getDestination());
        }

        // Iterate through each location to check for the capability
        for (Location location : locationsToCheck) {
            // Check ground
            if (location.getGround().hasCapability(capability)) {
                return true;
            }
            // Check an actor, if present
            if (location.containsAnActor()) {
                Actor actorAtLocation = location.getActor();
                if (actorAtLocation != null && actorAtLocation.hasCapability(capability)) {
                    return true;
                }
            }
            // Check items on the ground
            for (Item item : location.getItems()) {
                if (item.hasCapability(capability)) {
                    return true;
                }
            }
        }
        // If the capability was not found in any entity at any of the checked locations
        return false;
    }
}
