package game.conditions;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;

/**
 * A {@link Condition} that checks if an {@link Actor}'s current health is below a predefined
 * threshold.
 * <p>
 * This condition is used to determine if an actor is considered to have "low health". The health is
 * retrieved using the {@link BaseActorAttributes#HEALTH} attribute, and it's compared against a
 * static threshold {@code LOW_HEALTH} (currently 50).
 * </p>
 */
public class LowHealthCondition implements Condition {

    /**
     * The health threshold below which an actor is considered to have low health. If the actor's
     * current health is less than this value, the condition is met.
     */
    private static final int LOW_HEALTH = 50;

    /**
     * The actor whose health will be checked.
     */
    private final Actor actor;

    /**
     * Constructs a {@code LowHealthCondition} for a specific actor.
     *
     * @param actor The {@link Actor} whose health status will be evaluated by this condition. Must
     *              not be null.
     */
    public LowHealthCondition(Actor actor) {
        this.actor = actor;
    }

    /**
     * Checks if the associated actor's current health is less than the {@code LOW_HEALTH}
     * threshold.
     *
     * @return {@code true} if the actor's current health (obtained via
     * {@link Actor#getAttribute(Enum)} with {@link BaseActorAttributes#HEALTH}) is less than
     * {@value #LOW_HEALTH}, {@code false} otherwise. Returns {@code false} if the actor does not
     * have a health attribute or if its value is null.
     */
    @Override
    public boolean check() {
        // It's safer to check if the attribute exists and is not null
        if (actor.hasAttribute(BaseActorAttributes.HEALTH)) {
            Integer currentHealth = actor.getAttribute(BaseActorAttributes.HEALTH);
            if (currentHealth != null) {
                return currentHealth < LOW_HEALTH;
            }
        }
        return false; // If no health attribute or it's null, condition is not met.
    }
}
