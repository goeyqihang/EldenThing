package game.grounds;

import edu.monash.fit2099.engine.positions.Ground;

/**
 * An enumeration representing capabilities specific to ground types
 * ({@link Ground}). These capabilities define specific
 * properties or interactions related to the terrain itself, such as whether it can be
 * planted on or set on fire.
 */
public enum GroundCapability {
    /**
     * Indicates that an actor can perform a planting action on this ground.
     */
    CAN_PLANT_SEED,

    /**
     * Indicates that the ground can be set on fire by spells or other effects.
     * For example, a fire spell checks for this capability before replacing
     * the ground with {@link BurningGround}.
     */
    BURNABLE
}
