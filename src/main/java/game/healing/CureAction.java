package game.healing;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

/**
 * An {@link Action} that allows an {@link Actor} to attempt to cure a {@link Curable} target (which
 * can be another Actor or a Ground tile) at a specific location using a healing item. This action
 * checks for and deducts stamina cost from the performing actor if applicable, then delegates the
 * actual curing logic to the target's {@link Curable#cure(Actor, Location, Item)} method.
 */
public class CureAction extends Action {

    /**
     * The target entity (Actor or Ground) to be cured. Must implement the {@link Curable}
     * interface.
     */
    private final Curable target;
    /**
     * The {@link Location} of the target entity being cured.
     */
    private final Location targetLocation;
    /**
     * The {@link Item} being used by the actor to perform the cure.
     */
    private final Item healingItem;

    /**
     * Constructor for CureAction.
     *
     * @param target         The {@link Curable} entity to be cured.
     * @param targetLocation The {@link Location} where the target resides.
     * @param healingItem    The {@link Item} used for the healing action (e.g., a
     *                       {@link game.healing.items.Talisman}).
     */
    public CureAction(Curable target, Location targetLocation, Item healingItem) {
        this.target = target;
        this.targetLocation = targetLocation;
        this.healingItem = healingItem;
    }

    /**
     * Executes the cure action. First, it retrieves the stamina cost required by the target via
     * {@link Curable#getCureStaminaCost()}. It checks if the performing actor has the
     * {@link BaseActorAttributes#STAMINA} attribute. If so, it verifies if the actor has enough
     * stamina. If not, it returns a message indicating insufficient stamina. If stamina is
     * sufficient, it deducts the cost from the actor's stamina, calls the {@code cure} method on
     * the {@code target} and returns the result string from the target's cure logic. Actors
     * without a stamina attribute cannot cure.
     *
     * @param actor The actor performing the cure action.
     * @param map   The {@link GameMap} where the action takes place.
     * @return A string describing the outcome of the action (either success message from the
     * target's cure method or a failure message due to insufficient stamina).
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        int staminaCost = target.getCureStaminaCost();
        if (actor.hasAttribute(BaseActorAttributes.STAMINA)) {
            Integer currentStamina = actor.getAttribute(BaseActorAttributes.STAMINA);
            // Check if stamina is sufficient
            if (currentStamina == null || currentStamina < staminaCost) {
                return actor + " does not have enough stamina (" + currentStamina + "/"
                        + staminaCost + ") to cure " + target + ".";
            }
            actor.modifyAttribute(BaseActorAttributes.STAMINA, ActorAttributeOperations.DECREASE,
                    staminaCost);
            return target.cure(actor, targetLocation, healingItem);
        }
        return actor + " has no stamina and cannot cure " + target + ".";
    }

    /**
     * Provides a description of the cure action suitable for display in a menu.
     *
     * @param actor The actor performing the action.
     * @return A string describing the action, e.g., "Player uses Talisman on Blight at (x, y) to
     * cure Blight of any illness.".
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " uses " + healingItem + " on " + target + " at " + targetLocation
                + " to cure " + target + " of any illness.";
    }
}
