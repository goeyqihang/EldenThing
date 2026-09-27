package game.hatching;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.creatures.GoldenBeetle;
import game.capabilities.GeneralCapability;
import game.conditions.NearbyCapabilityCondition;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Golden Beetle Egg, a specific type of {@link Egg}. This egg hatches into a
 * {@link game.actors.creatures.GoldenBeetle} if it is on or adjacent to a location with the
 * {@link GeneralCapability#CURSED} capability.
 * <p>
 * When eaten, this egg restores {@value #STAMINA_RESTORE_ON_EAT} stamina to the eater if they
 * possess a stamina attribute.
 */
public class GoldenBeetleEgg extends Egg {

    /**
     * The amount of stamina restored to an actor when this egg is eaten.
     */
    private static final int STAMINA_RESTORE_ON_EAT = 20;

    /**
     * Constructor for GoldenBeetleEgg. Initializes the egg with the name "Golden Beetle Egg".
     */
    public GoldenBeetleEgg() {
        super("Golden Beetle Egg");
    }

    /**
     * Defines the hatching rules for a Golden Beetle Egg. It will hatch into a
     * {@link game.actors.creatures.GoldenBeetle} if a {@link NearbyCapabilityCondition} detects the
     * {@link GeneralCapability#CURSED} capability at or adjacent to the egg's
     * {@code currentLocation}.
     *
     * @param currentLocation The current {@link Location} of the egg.
     * @return A list containing one {@link HatchingRules} instance for hatching a GoldenBeetle
     * under cursed conditions.
     */
    @Override
    public List<HatchingRules> getHatchingRules(Location currentLocation) {
        List<HatchingRules> hatchingRules = new ArrayList<>();
        hatchingRules.add(
                new HatchingRules(
                        new NearbyCapabilityCondition(currentLocation, GeneralCapability.CURSED),
                        GoldenBeetle::new)); // Supplier for creating a new GoldenBeetle

        return hatchingRules;
    }

    /**
     * Defines the effect of this egg being eaten by an {@link Actor}. The {@code eater} restores
     * {@value #STAMINA_RESTORE_ON_EAT} stamina if they have a stamina attribute
     * ({@link edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes#STAMINA}). The egg is
     * then removed from the eater's inventory.
     *
     * @param eater The {@link Actor} eating the egg.
     * @param map   The {@link GameMap} where the eating occurs (not directly used here).
     * @return A string describing that the eater ate the egg and the stamina restored, or a message
     * indicating no change in stamina if the eater lacks the attribute.
     */
    @Override
    public String eatenBy(Actor eater, GameMap map) {
        String message = eater + " eats the " + this;
        // Check if the eater has a stamina attribute
        if (eater.hasAttribute(BaseActorAttributes.STAMINA)) {
            eater.modifyAttribute(BaseActorAttributes.STAMINA, ActorAttributeOperations.INCREASE,
                    STAMINA_RESTORE_ON_EAT);
            message += " and restores " + STAMINA_RESTORE_ON_EAT + " stamina.";
        } else {
            message += ", but feels no change in stamina."; // Eater might not have stamina
        }
        eater.removeItemFromInventory(this); // Egg is consumed from inventory
        return message;
    }

    /**
     * Provides the menu description for the action of eating this Golden Beetle Egg. Includes the
     * potential stamina restoration benefit.
     *
     * @param actor The {@link Actor} who might eat this egg.
     * @return A string for the eat action menu, e.g., "Player eats Golden Beetle Egg (Stamina
     * +20)".
     */
    @Override
    public String getEatMenuDescription(Actor actor) {
        return actor + " eats " + this + " (Stamina +" + STAMINA_RESTORE_ON_EAT + ")";
    }
}
