package game.hatching;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.capabilities.GeneralCapability;
import game.eating.EatAction;
import game.eating.Eatable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * An abstract base class for egg items in the game. Eggs are {@link Item}s that are portable and
 * can be eaten (implementing {@link Eatable}). More importantly, they can hatch into a specific
 * {@link Actor} (creature) based on a set of {@link HatchingRules} defined by concrete subclasses.
 * <p>
 * The hatching process is typically managed by the {@link #tick(Location)} method, which checks the
 * hatching rules each turn when the egg is on the ground. If an egg hatches, it is removed from its
 * location, and the new creature is placed at a suitable nearby spot.
 * </p>
 */
public abstract class Egg extends Item implements Eatable {

    /**
     * The default display character for all egg items on the map.
     */
    private static final char DISPLAY_CHAR = '0';
    /**
     * Indicates that eggs are portable by default (can be picked up and dropped).
     */
    private static final boolean IS_PORTABLE = true;

    /**
     * Random number generator used to pick where the hatchling appears.
     */
    private final Random random = new Random();

    /**
     * Constructor for the Egg class. Initializes the egg with a given name, the default display
     * character '0', and marks it as portable.
     *
     * @param name The name of the egg (e.g., "Omen Sheep Egg", "Golden Beetle Egg"). This name is
     *             used in descriptions and menu options.
     */
    public Egg(String name) {
        super(name, Egg.DISPLAY_CHAR, Egg.IS_PORTABLE);
    }

    /**
     * Abstract method that must be implemented by concrete egg subclasses. This method should
     * return a list of {@link HatchingRules} specific to this type of egg. Each rule defines a
     * {@link game.conditions.Condition} for hatching and a {@link java.util.function.Supplier} to
     * create the creature if the condition is met.
     *
     * @param currentLocation The current {@link Location} of the egg. This can be used by
     *                        conditions within the hatching rules (e.g., to check nearby
     *                        environment).
     * @return A list of {@link HatchingRules} for this egg.
     */
    public abstract List<HatchingRules> getHatchingRules(Location currentLocation);

    /**
     * Attempts to hatch the egg based on its defined {@link #getHatchingRules(Location)}. It
     * iterates through the rules, and if a rule's condition is met
     * ({@link HatchingRules#tryHatch()}), the corresponding creature is created and returned.
     *
     * @param currentLocation The current {@link Location} of the egg.
     * @return The hatched {@link Actor} if any rule's condition was met, otherwise {@code null}.
     */
    public Actor tryHatch(Location currentLocation) {
        for (HatchingRules hatchingRule : this.getHatchingRules(currentLocation)) {
            Actor hatchlingActor = hatchingRule.tryHatch();
            if (hatchlingActor != null) {
                return hatchlingActor; // Return the first successful hatch
            }
        }
        return null; // No rule was met, or no creature was supplied by the met rule
    }

    /**
     * Tries to find a suitable location to place a newly hatched actor. It checks the
     * {@code currentLocation} itself and then all its direct exits. A location is considered
     * suitable if the provided {@code actor} can enter it (as determined by
     * {@link Location#canActorEnter(Actor)}). If multiple suitable locations are found, one is
     * chosen randomly.
     *
     * @param currentLocation The initial {@link Location} (usually where the egg was).
     * @param actor           The {@link Actor} (hatchling) that needs to be placed.
     * @return A suitable {@link Location} for the hatchling if one is found, otherwise
     * {@code null}.
     */
    public Location findHatchLocation(Location currentLocation, Actor actor) {
        List<Location> locations = new ArrayList<>();
        // Check the egg's current location first
        if (currentLocation.canActorEnter(actor)) {
            locations.add(currentLocation);
        }
        // Check adjacent locations
        for (Exit exit : currentLocation.getExits()) {
            Location location = exit.getDestination();
            if (location.canActorEnter(actor)) {
                locations.add(location);
            }
        }

        if (!locations.isEmpty()) {
            int randomIndex = random.nextInt(locations.size());
            return locations.get(randomIndex);
        }
        return null; // No suitable location found
    }

    /**
     * Called once per turn when the egg is on the ground. This method attempts to hatch the egg
     * using {@link #tryHatch(Location)}. If a hatchling is produced, it then tries to place the
     * hatchling on the map at or near the egg's current location using
     * {@link #findHatchLocation(Location, Actor)}. If successfully placed, the egg item is removed
     * from its current location.
     *
     * @param currentLocation The current {@link Location} of this egg on the map.
     */
    @Override
    public void tick(Location currentLocation) {
        Actor hatchling = tryHatch(currentLocation);
        if (hatchling != null) {
            Location hatchLocation = findHatchLocation(currentLocation, hatchling);
            if (hatchLocation != null) {
                hatchLocation.addActor(hatchling); // Add the hatchling to the map
                currentLocation.removeItem(this); // Remove the egg from the map
            }
        }
    }

    /**
     * Returns a list of allowable actions that the owner can perform with this Egg. If the owner
     * has the {@link GeneralCapability#CONSUMER} capability, an {@link EatAction} is added to allow
     * the owner to eat this egg. This also includes default item actions like
     * {@link edu.monash.fit2099.engine.items.DropAction}.
     *
     * @param owner The {@link Actor} carrying or interacting with the Egg.
     * @param map   The {@link GameMap} the owner is currently on.
     * @return An {@link ActionList} containing allowable actions.
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = super.allowableActions(owner, map);
        if (owner.hasCapability(GeneralCapability.CONSUMER)) {
            actions.add(new EatAction(this));
        }
        return actions;
    }

    /**
     * Abstract method to define what happens when this specific type of egg is eaten.
     * Implementations should handle effects on the {@code eater} and remove the egg. This method is
     * part of the {@link Eatable} interface.
     *
     * @param eater The {@link Actor} eating the egg.
     * @param map   The {@link GameMap} where the eating occurs.
     * @return A string describing the outcome of being eaten.
     */
    @Override
    public abstract String eatenBy(Actor eater, GameMap map);

    /**
     * Abstract method to provide a menu description for the action of eating this egg. This method
     * is part of the {@link Eatable} interface.
     *
     * @param actor The {@link Actor} who might eat this egg.
     * @return A string for the eat action menu, e.g., "Player eats Omen Sheep Egg".
     */
    @Override
    public abstract String getEatMenuDescription(Actor actor);
}
