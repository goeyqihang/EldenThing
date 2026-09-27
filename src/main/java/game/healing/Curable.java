package game.healing;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;

/**
 * An interface for entities (Actors or Ground) that can be cured or cleansed of certain effects
 * (like rot or blight) by a {@link CureAction}. Implementing classes must define the specific
 * curing logic and the associated stamina cost for the actor performing the cure.
 */
public interface Curable {

    /**
     * Defines the logic for curing the implementing entity. This method is called when an actor
     * performs a cure action on this entity. The implementation should detail the effects of the
     * cure (e.g., removing a status effect, changing ground type).
     *
     * @param healer       The actor performing the cure action.
     * @param cureLocation The location of the entity being cured.
     * @param healingItem  The item used by the healer to perform the cure.
     * @return A descriptive string detailing the outcome of the cure attempt (e.g., "Blight was
     * cleansed.").
     */
    String cure(Actor healer, Location cureLocation, Item healingItem);

    /**
     * Returns the amount of stamina required for an actor to perform the cure action on this
     * entity.
     *
     * @return The integer value representing the stamina cost. Can be 0 if no stamina is required.
     */
    int getCureStaminaCost();
}
