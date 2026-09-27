package game.effects;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * An {@link Effect} that applies healing to an {@link Actor}.
 * <p>
 * When this effect is applied, it calls the {@link Actor#heal(int)} method on the target actor,
 * increasing their current health by the specified amount, up to their maximum health.
 * </p>
 */
public class HealEffect implements Effect {

    /**
     * The amount of health to be restored to the actor.
     */
    private final int healAmount;

    /**
     * Constructs a {@code HealEffect}.
     *
     * @param healAmount The amount of health this effect will restore. Should be a non-negative
     *                   integer.
     */
    public HealEffect(int healAmount) {
        this.healAmount = healAmount;
    }

    /**
     * Applies the healing effect to the specified actor. This will increase the actor's current
     * health by the {@link #healAmount}, capped at the actor's maximum health.
     *
     * @param actor The {@link Actor} to whom the healing will be applied. Must not be null.
     * @param map   The {@link GameMap} where the effect occurs. Currently not used by this specific
     *              effect but is part of the {@link Effect} interface.
     */
    @Override
    public void applyEffect(Actor actor, GameMap map) {
        actor.heal(healAmount);
    }
}
