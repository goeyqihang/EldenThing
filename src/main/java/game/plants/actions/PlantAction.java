package game.plants.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.GroundCapability;
import game.plants.Plant;
import game.plants.Seed;

/**
 * An {@link Action} that allows an {@link Actor} to plant a {@link Seed} at their current location,
 * provided the ground is suitable (has {@link GroundCapability#CAN_PLANT_SEED}). This
 * action handles stamina cost deduction, seed removal, creating the specific {@link Plant}
 * instance, replacing the ground with the new plant, and triggering the plant's instant effects.
 */
public class PlantAction extends Action {

    /**
     * The {@link Seed} item to be planted.
     */
    private final Seed seed;
    /**
     * The {@link Location} where the seed will be planted (actor's current location).
     */
    private final Location plantLocation;

    /**
     * Constructor for PlantAction.
     *
     * @param seed          The {@link Seed} item that the actor intends to plant.
     * @param plantLocation The {@link Location} where the planting will occur.
     */
    public PlantAction(Seed seed, Location plantLocation) {
        this.seed = seed;
        this.plantLocation = plantLocation;
    }

    /**
     * Executes the planting action.
     * Checks if the actor has enough stamina based on the seed's requirement.
     * If sufficient stamina is available, it deducts the cost, removes the seed from the actor's
     * inventory, creates the corresponding {@link Plant} using {@link Seed#createPlant()},
     * sets the ground at the target location to the new plant, and executes the plant's
     * {@link Plant#executeInstantEffects(Actor, Location)}.
     * If the actor lacks the stamina attribute or has insufficient stamina, the action fails and
     * returns an appropriate message.
     *
     * @param actor The actor performing the planting action.
     * @param map   The {@link GameMap} where the action takes place.
     * @return A string describing the outcome: success message including any instant plant effects,
     * or a failure message due to insufficient stamina or missing stamina attribute.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        int staminaCost = seed.getStaminaCost();

        // Planting costs stamina, so actors without a stamina attribute cannot plant
        if (!actor.hasAttribute(BaseActorAttributes.STAMINA)) {
            return actor + " has no stamina and cannot plant " + seed + ".";
        }
        Integer currentStamina = actor.getAttribute(BaseActorAttributes.STAMINA);
        if (currentStamina == null || currentStamina < staminaCost) {
            return actor + " does not have enough stamina (" + currentStamina + "/"
                    + staminaCost + ") to plant " + seed + ".";
        }
        actor.modifyAttribute(BaseActorAttributes.STAMINA, ActorAttributeOperations.DECREASE,
                staminaCost);

        // Replace the ground with the new plant, then trigger the plant's instant effects
        actor.removeItemFromInventory(seed);
        Plant plant = seed.createPlant();
        plantLocation.setGround(plant);
        String effectMessage = plant.executeInstantEffects(actor, plantLocation);

        return actor + " plants the " + seed + "." + effectMessage;
    }

    /**
     * Provides a description of the planting action suitable for display in a menu.
     *
     * @param actor The actor performing the action.
     * @return A string describing the action, e.g., "Player plants the Inheritree Seed".
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " plants the " + seed;
    }
}
