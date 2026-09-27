package game.hatching;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.creatures.OmenSheep;
import game.conditions.TurnBasedCondition;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Omen Sheep Egg, a specific type of {@link Egg}. This egg hatches into an
 * {@link game.actors.creatures.OmenSheep} after being on the ground for a specific duration
 * ({@value #HATCH_DURATION} turns).
 * <p>
 * When eaten by an {@link Actor} (typically the Player/Farmer), this egg increases the eater's
 * maximum health by {@value #MAX_HEALTH_BOOST} points. The egg keeps track of the number of turns
 * it has spent on the ground to determine hatching. If picked up, this timer resets.
 */
public class OmenSheepEgg extends Egg {

    /**
     * The number of turns an OmenSheepEgg must be on the ground before it hatches.
     */
    private static final int HATCH_DURATION = 3;
    /**
     * The amount by which the eater's maximum health is increased when this egg is consumed.
     */
    private static final int MAX_HEALTH_BOOST = 10;

    /**
     * Counter for the number of turns this egg has spent on the ground. This is used to trigger
     * hatching.
     */
    private int turnOnGround = 0;

    /**
     * Constructor for OmenSheepEgg. Initializes the egg with the name "Omen Sheep Egg".
     */
    public OmenSheepEgg() {
        super("Omen Sheep Egg");
    }

    /**
     * Defines the hatching rules for an OmenSheep Egg. It will hatch into an
     * {@link game.actors.creatures.OmenSheep} if a {@link TurnBasedCondition} indicates that the
     * egg has been on the ground for more than {@value #HATCH_DURATION} turns.
     *
     * @param currentLocation The current {@link Location} of the egg (not directly used by this
     *                        rule's condition).
     * @return A list containing one {@link HatchingRules} instance for hatching an OmenSheep based
     * on time spent on the ground.
     */
    @Override
    public List<HatchingRules> getHatchingRules(Location currentLocation) {
        List<HatchingRules> hatchingRules = new ArrayList<>();
        hatchingRules.add(new HatchingRules(new TurnBasedCondition(turnOnGround, HATCH_DURATION),
                OmenSheep::new)); // Supplier for creating a new OmenSheep

        return hatchingRules;
    }

    /**
     * Called once per turn when the egg is on the ground. Increments the {@link #turnOnGround}
     * counter and then delegates to the parent {@link Egg#tick(Location)} method to handle the
     * hatching process.
     *
     * @param currentLocation The current {@link Location} of this egg on the map.
     */
    @Override
    public void tick(Location currentLocation) {
        turnOnGround++;
        super.tick(currentLocation); // Handles the hatching logic
    }

    /**
     * Called once per turn when the egg is carried by an actor. If the egg is in an actor's
     * inventory, its {@link #turnOnGround} counter is reset to 0, as the hatching condition is
     * based on time spent on the ground.
     *
     * @param currentLocation The {@link Location} of the actor carrying this egg.
     * @param actor           The {@link Actor} carrying this egg.
     */
    @Override
    public void tick(Location currentLocation, Actor actor) {
        turnOnGround = 0; // Reset ground timer if picked up
    }

    /**
     * Defines the effect of this egg being eaten by an {@link Actor}. The {@code eater}'s maximum
     * health is increased by {@value #MAX_HEALTH_BOOST} points using
     * {@link Actor#modifyAttributeMaximum(Enum,
     * edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations, int)} with
     * {@link edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes#HEALTH}. The egg is
     * then removed from the eater's inventory.
     *
     * @param eater The {@link Actor} eating the egg.
     * @param map   The {@link GameMap} where the eating occurs (not directly used here).
     * @return A string describing that the eater ate the egg and their max HP increased.
     */
    @Override
    public String eatenBy(Actor eater, GameMap map) {
        String message = eater + " eats the " + this;
        // Increase eater's maximum health by MAX_HEALTH_BOOST points.
        eater.modifyAttributeMaximum(BaseActorAttributes.HEALTH, ActorAttributeOperations.INCREASE,
                MAX_HEALTH_BOOST);
        message += " and feels invigorated. Max HP increased by " + MAX_HEALTH_BOOST + "!";
        eater.removeItemFromInventory(this); // Egg is consumed from inventory
        return message;
    }

    /**
     * Provides the menu description for the action of eating this Omen Sheep Egg. Includes the
     * potential maximum health boost benefit.
     *
     * @param actor The {@link Actor} who might eat this egg.
     * @return A string for the eat action menu, e.g., "Player eats Omen Sheep Egg (Max HP +10)".
     */
    @Override
    public String getEatMenuDescription(Actor actor) {
        return actor + " eats " + this + " (Max HP +" + MAX_HEALTH_BOOST + ")";
    }
}
