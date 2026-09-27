package game.plants;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.capabilities.GeneralCapability;
import game.fishing.DigAction;
import game.grounds.Soil;

/**
 * Represents an Inheritree, a beneficial {@link Plant} that purifies cursed ground upon planting
 * and provides periodic healing and stamina restoration to nearby actors.
 * It is represented by 't' on the map and has the {@link GeneralCapability#BLESSED} capability.
 * It can be dug up, turning the ground back into Soil.
 */
public class Inheritree extends Plant {

    /**
     * The amount of health points restored to adjacent actors each turn.
     */
    private static final int HEAL_AMOUNT = 5;
    /**
     * The amount of stamina points restored to adjacent actors each turn.
     */
    private static final int STAMINA_RESTORE_AMOUNT = 5;

    /**
     * Constructor for the Inheritree.
     * Initializes the plant with display character 't', name "Inheritree", and adds the
     * BLESSED capability.
     */
    public Inheritree() {
        super('t', "Inheritree");
        this.addCapability(GeneralCapability.BLESSED);
    }

    /**
     * Executes the instant effects that occur when the Inheritree is planted.
     * It checks all adjacent ground tiles and replaces any with the
     * {@link GeneralCapability#CURSED} capability (such as Blight) with {@link Soil}.
     *
     * @param planter  The actor who planted the Inheritree.
     * @param location The location where the Inheritree was planted.
     * @return A string describing the purification effect if any cursed ground was converted,
     * otherwise an empty string.
     */
    @Override
    public String executeInstantEffects(Actor planter, Location location) {
        int curedCount = 0; // Counter for purified tiles

        // Check adjacent locations
        for (Exit exit : location.getExits()) {
            Location adjacentLocation = exit.getDestination();
            Ground adjacentGround = adjacentLocation.getGround();
            // If the adjacent ground is cursed...
            if (adjacentGround.hasCapability(GeneralCapability.CURSED)) {
                adjacentLocation.setGround(new Soil()); // ...replace it with Soil
                curedCount++; // Increment the counter
            }
        }

        // Return a message only if purification occurred
        if (curedCount > 0) {
            return " The Inheritree purifies the surrounding cursed ground.";
        }
        return ""; // Return empty string if no purification happened
    }

    /**
     * Called once per turn, providing periodic healing and stamina restoration.
     * The Inheritree heals adjacent actors by {@value #HEAL_AMOUNT} health and restores
     * their stamina by {@value #STAMINA_RESTORE_AMOUNT} points.
     *
     * @param location The current location of this Inheritree on the map.
     */
    @Override
    public void tick(Location location) {
        // Iterate through adjacent locations
        for (Exit exit : location.getExits()) {
            Location destination = exit.getDestination();
            // Check if an actor is present at the adjacent location
            if (destination.containsAnActor()) {
                Actor target = destination.getActor();
                // Heal the actor
                target.heal(HEAL_AMOUNT);
                // Restore stamina if the actor has the stamina attribute
                if (target.hasAttribute(BaseActorAttributes.STAMINA)) {
                    target.modifyAttribute(BaseActorAttributes.STAMINA,
                            ActorAttributeOperations.INCREASE, STAMINA_RESTORE_AMOUNT);
                }
            }
        }
    }

    /**
     * Returns a list of allowable actions an actor can perform on this Inheritree ground.
     * If the actor has an item with the {@link GeneralCapability#CAN_DIG} capability,
     * a {@link DigAction} is made available to dig up the Inheritree and replace it with Soil.
     *
     * @param actor     The actor interacting with the ground.
     * @param location  The location of the ground.
     * @param direction The direction of the ground from the actor.
     * @return A list of allowable actions.
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList actions = super.allowableActions(actor, location, direction);
        for (Item item : actor.getItemInventory()) {
            if (item.hasCapability(GeneralCapability.CAN_DIG)) {
                actions.add(new DigAction(item, location, new Soil()));
            }
        }
        return actions;
    }
}
