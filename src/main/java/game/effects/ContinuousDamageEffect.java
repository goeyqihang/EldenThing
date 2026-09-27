package game.effects;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.StatusEffect;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A reusable {@link StatusEffect} that inflicts a set amount of damage to an actor each turn for a
 * specified duration.
 * <p>
 * This effect can be used to represent various damage-over-time conditions like poison, burning,
 * curses, etc. Each turn, it damages the afflicted actor and decrements its duration until it
 * expires.
 */
public class ContinuousDamageEffect extends StatusEffect {
    /**
     * The remaining number of turns for this effect.
     */
    private int duration;

    /**
     * The amount of damage inflicted each turn.
     */
    private final int damagePerTurn;

    /**
     * Constructor for ContinuousDamageEffect.
     *
     * @param name          The name of the status effect to be displayed (e.g.,
     *                      "Poisoned", "Burning").
     * @param duration      The total number of turns the effect should last.
     * @param damagePerTurn The amount of damage to be inflicted at the start of each turn.
     */
    public ContinuousDamageEffect(String name, int duration, int damagePerTurn) {
        super(name);
        this.duration = duration;
        this.damagePerTurn = damagePerTurn;
    }

    /**
     * Executes the logic for this effect each game turn.
     * <p>
     * On each tick, this method applies damage to the actor and reduces the effect's remaining
     * duration. If the actor's health drops to zero or below as a result of this damage, their
     * unconscious state is processed, and the effect is immediately removed. The effect also
     * removes itself once its duration expires naturally.
     *
     * @param location The current location of the actor.
     * @param actor    The actor afflicted with this status effect.
     */
    @Override
    public void tick(Location location, Actor actor) {
        if (duration > 0) {
            actor.hurt(damagePerTurn);

            // Check if the actor has been knocked out by the damage.
            if (!actor.isConscious()) {
                GameMap map = location.map();
                actor.unconscious(map);
                // Remove the effect immediately since the actor is unconscious.
                actor.removeStatusEffect(this);
                return; // Exit early to avoid double-removing the effect.
            }
            duration--;
        }

        // If the duration has expired, remove the effect from the actor.
        if (duration <= 0) {
            actor.removeStatusEffect(this);
        }
    }
}
