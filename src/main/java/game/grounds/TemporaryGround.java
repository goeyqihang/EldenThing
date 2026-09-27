package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A wrapper class that allows a type of Ground to exist temporarily at a location.
 * <p>
 * This class holds a reference to an 'original' ground type and a 'temporary' ground type.
 * For a set duration, it will present itself and behave as the temporary ground: its display
 * character, capabilities, allowable actions and whether actors can enter all come from the
 * temporary ground.
 * Each turn, it calls the tick method of the temporary ground and counts down its duration.
 * Once the duration expires, it replaces itself at its location with the original ground.
 * This is useful for effects like fires that burn out or magical terrain that fades.
 *
 * @see Ground
 */
public class TemporaryGround extends Ground {
    /**
     * The original ground that the location will revert to after the effect ends.
     */
    private final Ground originalGround;

    /**
     * The temporary ground whose appearance and behavior are used during the effect's duration.
     */
    private final Ground temporaryGround;

    /**
     * The remaining number of turns for this temporary effect.
     */
    private int duration;

    /**
     * Constructor for TemporaryGround.
     *
     * @param originalGround  The Ground type that the location will revert to when the
     *                        duration expires.
     * @param temporaryGround The Ground type that will be active temporarily. This object's
     *                        display character and tick behavior will be used.
     * @param duration        The number of game turns this temporary ground should last.
     */
    public TemporaryGround(Ground originalGround, Ground temporaryGround, int duration) {
        // Show the temporary ground's display character and name on the map
        super(temporaryGround.getDisplayChar(), temporaryGround.toString());
        this.originalGround = originalGround;
        this.temporaryGround = temporaryGround;
        this.duration = duration;
    }

    /**
     * Called once per turn, this method handles the logic for the temporary ground.
     * <p>
     * It performs two main functions:
     * 1. It calls the {@code tick} method of the underlying temporary ground, allowing it to
     * perform its own per-turn actions (e.g., BurningGround dealing damage).
     * 2. It decrements its own duration timer. If the timer reaches zero, it replaces
     * itself at the given location with the original ground type.
     *
     * @param location The location of the TemporaryGround instance.
     */
    @Override
    public void tick(Location location) {
        // Delegate the tick behaviour to the active temporary ground.
        temporaryGround.tick(location);

        if (this.duration > 0) {
            this.duration--;
            if (this.duration <= 0) {
                // When duration expires, replace this ground with the original one.
                location.setGround(this.originalGround);
            }
        }
    }

    /**
     * Delegates to the temporary ground.
     *
     * @param actor the Actor to check
     * @return whether the temporary ground lets the actor enter
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return temporaryGround.canActorEnter(actor);
    }

    /**
     * Delegates to the temporary ground.
     *
     * @return whether the temporary ground blocks thrown objects
     */
    @Override
    public boolean blocksThrownObjects() {
        return temporaryGround.blocksThrownObjects();
    }

    /**
     * Delegates to the temporary ground.
     *
     * @param actor     the Actor acting
     * @param location  the current Location
     * @param direction the direction of the Ground from the Actor
     * @return the actions the temporary ground allows
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        return temporaryGround.allowableActions(actor, location, direction);
    }

    /**
     * Delegates to the temporary ground, so that for example a burning tile does not count as
     * burnable or cursed while it burns.
     *
     * @param capability the capability to check
     * @return whether the temporary ground has the capability
     */
    @Override
    public boolean hasCapability(Enum<?> capability) {
        return temporaryGround.hasCapability(capability);
    }
}
