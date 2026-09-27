package game.effects;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * An {@link Effect} that applies a specified amount of damage to an
 * {@link Actor}.
 * <p>
 * When this effect is applied, it calls the
 * {@link Actor#hurt(int)} method on the target actor, reducing
 * their health by the given amount.
 * </p>
 */
public class DamageEffect implements Effect {

    /**
     * The amount of damage to be applied to the actor.
     */
    private final int amount;

    /**
     * Constructs a {@code DamageEffect}.
     *
     * @param amount The amount of damage this effect will inflict. Should be a non-negative
     *               integer.
     */
    public DamageEffect(int amount) {
        this.amount = amount;
    }

    /**
     * Applies the damage effect to the specified actor. This will reduce the actor's current health
     * by the {@link #amount}, and knock the actor out if their health drops to zero.
     *
     * @param actor The {@link Actor} to whom the damage will be
     *              applied. Must not be null.
     * @param map   The {@link GameMap} where the effect occurs.
     */
    @Override
    public void applyEffect(Actor actor, GameMap map) {
        actor.hurt(amount);
        if (!actor.isConscious()) {
            actor.unconscious(map);
        }
    }
}
