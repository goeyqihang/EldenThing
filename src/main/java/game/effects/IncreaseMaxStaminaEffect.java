package game.effects;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * An {@link Effect} that increases an {@link edu.monash.fit2099.engine.actors.Actor}'s maximum
 * stamina points.
 * <p>
 * When this effect is applied, it calls the
 * {@link edu.monash.fit2099.engine.actors.Actor#modifyAttributeMaximum(Enum,
 * edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations, int)} method on the target
 * actor, using {@link edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes#STAMINA} as
 * the attribute and
 * {@link edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations#INCREASE} as the
 * operation. This permanently raises the actor's maximum stamina by the specified amount. The
 * engine's {@link edu.monash.fit2099.engine.actors.attributes.BaseActorAttribute} also refills the
 * actor's current stamina to the new maximum.
 * </p>
 */
public class IncreaseMaxStaminaEffect implements Effect {

    /**
     * The amount by which the actor's maximum stamina will be increased.
     */
    private final int increaseAmount;

    /**
     * Constructs an {@code IncreaseMaxStaminaEffect}.
     *
     * @param increaseAmount The amount to add to the actor's maximum stamina. Should be a
     *                       non-negative integer.
     */
    public IncreaseMaxStaminaEffect(int increaseAmount) {
        this.increaseAmount = increaseAmount;
    }

    /**
     * Applies the effect to the specified actor, increasing their maximum stamina.
     *
     * @param actor The {@link edu.monash.fit2099.engine.actors.Actor} whose maximum stamina will be
     *              increased. Must not be null and should have the
     *              {@link edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes#STAMINA}
     *              attribute.
     * @param map   The {@link edu.monash.fit2099.engine.positions.GameMap} where the effect occurs.
     *              Currently not used by this specific effect but is part of the {@link Effect}
     *              interface.
     */
    @Override
    public void applyEffect(Actor actor, GameMap map) {
        actor.modifyAttributeMaximum(BaseActorAttributes.STAMINA, ActorAttributeOperations.INCREASE,
                increaseAmount);
    }
}
