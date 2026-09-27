package game.capabilities;

/**
 * General capabilities or states that game entities
 * ({@link edu.monash.fit2099.engine.GameEntity}) can possess. They are used throughout the game to
 * decide which interactions are available (for example, who can buy, cure, fish or be followed)
 * without checking concrete types.
 *
 * @author Riordan D. Alfredo
 * Modified by: Goey Qi Hang
 */
public enum GeneralCapability {
    /**
     * Capability indicating an actor will attack actors designated as enemies.
     */
    HOSTILE_TO_ENEMY,

    /**
     * Capability indicating an item can be used to cure {@link game.healing.Curable} targets, such
     * as Blight or creatures suffering from Crimson Rot, via {@link game.healing.CureAction}.
     */
    CAN_CURE,

    /**
     * Capability indicating an entity is blessed, which may influence certain game mechanics like
     * creature production.
     */
    BLESSED,

    /**
     * Capability indicating an actor can be followed by other actors (e.g., by an actor with
     * {@link game.behaviours.FollowBehaviour}).
     */
    FOLLOWABLE,

    /**
     * Capability indicating an actor can be consumed (eaten) directly from the game map by an actor
     * with the {@link #CONSUMER} capability.
     */
    CONSUMABLE_ON_MAP,

    /**
     * Capability indicating an actor can consume/eat other entities or items (e.g., those marked
     * {@link #CONSUMABLE_ON_MAP} or implementing {@link game.eating.Eatable}).
     */
    CONSUMER,

    /**
     * Capability indicating an entity (actor or ground) is cursed, which may trigger specific
     * interactions or environmental effects.
     */
    CURSED,

    /**
     * Capability indicating an NPC can sell items to other actors. Works in conjunction with
     * {@link #CAN_BUY}.
     */
    CAN_SELL,

    /**
     * Capability indicating an actor (usually the player) can buy items from NPCs who have the
     * {@link #CAN_SELL} capability.
     */
    CAN_BUY,

    /**
     * Capability indicating an actor (usually the player) can listen to monologues from NPCs.
     */
    CAN_LISTEN,

    /**
     * Capability indicating an actor can travel through a teleportation gate.
     */
    CAN_TELEPORT,

    /**
     * Capability indicating an item enables fishing actions.
     */
    CAN_FISH,

    /**
     * Capability indicating an item enables digging actions.
     */
    CAN_DIG
}
