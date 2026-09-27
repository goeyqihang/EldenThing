package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A type of ground that is on fire.
 * <p>
 * This ground damages any actor standing on it each turn. This class itself does not
 * manage the duration of the fire or how it reverts to a different ground type; that
 * logic is typically handled by a wrapper class like {@link TemporaryGround}.
 *
 * @see TemporaryGround
 */
public class BurningGround extends Ground {

    /**
     * The character used to represent burning ground on the map.
     */
    private static final char FIRE_DISPLAY_CHAR = '^';

    /**
     * The amount of damage inflicted each turn on an actor standing on this ground.
     */
    private static final int DAMAGE_PER_TURN_FROM_FIRE = 5;

    /**
     * Constructor for BurningGround.
     * Initializes the ground with a specific display character and name.
     */
    public BurningGround() {
        super(FIRE_DISPLAY_CHAR, "Burning Ground");
    }

    /**
     * Called once per turn, this method applies the burning effect to any actor at this location.
     * <p>
     * If an actor is present, this method will inflict damage on them. If the damage
     * renders the actor unconscious, it will handle that state transition.
     * This method is designed to be called by a managing class's tick method (e.g.,
     * {@link TemporaryGround#tick(Location)}).
     *
     * @param currentLocation The location where the burning ground is active.
     */
    @Override
    public void tick(Location currentLocation) {
        if (currentLocation.containsAnActor()) {
            Actor victim = currentLocation.getActor();
            if (victim != null) {
                victim.hurt(DAMAGE_PER_TURN_FROM_FIRE);
                if (!victim.isConscious()) {
                    victim.unconscious(currentLocation.map());
                }
            }
        }
    }
}
