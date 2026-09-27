package game.conditions;

import edu.monash.fit2099.engine.actors.Actor;

/**
 * A {@link Condition} that checks if an {@link Actor}'s current balance of runes is below a
 * predefined threshold.
 * <p>
 * This condition is used to determine if an actor is considered to have "low runes". The rune
 * balance is retrieved using {@link Actor#getBalance()}, and it's compared against a static
 * threshold {@code LOW_RUNES} (currently 500).
 * </p>
 */
public class LowRunesCondition implements Condition {

    /**
     * The rune balance threshold below which an actor is considered to have low runes. If the
     * actor's current rune balance is less than this value, the condition is met.
     */
    private static final int LOW_RUNES = 500;

    /**
     * The actor whose rune balance will be checked.
     */
    private final Actor actor;

    /**
     * Constructs a {@code LowRunesCondition} for a specific actor.
     *
     * @param actor The {@link Actor} whose rune balance will be evaluated by this condition. Must
     *              not be null.
     */
    public LowRunesCondition(Actor actor) {
        this.actor = actor;
    }

    /**
     * Checks if the associated actor's current rune balance is less than the {@code LOW_RUNES}
     * threshold.
     *
     * @return {@code true} if the actor's current rune balance (obtained via
     * {@link Actor#getBalance()}) is less than {@value #LOW_RUNES}, {@code false} otherwise.
     */
    @Override
    public boolean check() {
        return actor.getBalance() < LOW_RUNES;
    }
}
