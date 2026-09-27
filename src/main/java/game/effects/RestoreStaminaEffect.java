package game.effects;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * An {@link Effect} that restores an {@link edu.monash.fit2099.engine.actors.Actor}'s current
 * stamina points.
 * <p>
 * When this effect is applied, it calls the
 * {@link edu.monash.fit2099.engine.actors.Actor#modifyAttribute(Enum,
 * edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations, int)} method on the target
 * actor, using {@link edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes#STAMINA} as
 * the attribute and
 * {@link edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations#INCREASE} as the
 * operation. This increases the actor's current stamina by the specified amount, up to their
 * maximum stamina.
 * </p>
 */
public class RestoreStaminaEffect implements Effect {

    /**
     * The amount of stamina to be restored to the actor.
     */
    private final int amount;

    /**
     * Constructs a {@code RestoreStaminaEffect}.
     *
     * @param amount The amount of stamina this effect will restore. Should be a non-negative
     *               integer.
     */
    public RestoreStaminaEffect(int amount) {
        this.amount = amount;
    }

    /**
     * Applies the effect to the specified actor, restoring their current stamina.
     *
     * @param actor The {@link edu.monash.fit2099.engine.actors.Actor} whose current stamina will be
     *              restored. Must not be null and should have the
     *              {@link edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes#STAMINA}
     *              attribute.
     * @param map   The {@link edu.monash.fit2099.engine.positions.GameMap} where the effect occurs.
     *              Currently not used by this specific effect but is part of the {@link Effect}
     *              interface.
     */
    @Override
    public void applyEffect(Actor actor, GameMap map) {
        // Check if actor has stamina attribute before attempting to modify it
        if (actor.hasAttribute(BaseActorAttributes.STAMINA)) {
            actor.modifyAttribute(BaseActorAttributes.STAMINA, ActorAttributeOperations.INCREASE,
                    amount);
        }
        // If the actor doesn't have stamina, the effect does nothing silently.
    }
}
