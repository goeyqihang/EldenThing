package game.actors.npc;

import game.conditions.Condition;

/**
 * Represents a single line of dialogue (monologue) that an NPC can say when a certain
 * {@link Condition} is met.
 *
 * <p>Each monologue consists of a {@code Condition} that determines whether it
 * should be spoken, and a corresponding {@code message} string. The availability of the monologue
 * is checked via the {@link #availability()} method which in turn calls the
 * {@link Condition#check()} method.</p>
 */
public class Monologue {

    /**
     * The condition under which this monologue is available.
     */
    private final Condition condition;

    /**
     * The message to be displayed when the condition is satisfied.
     */
    private final String message;

    /**
     * Constructs a {@code Monologue} with the given condition and message.
     *
     * @param condition the {@link Condition} that determines if this monologue can be triggered
     * @param message   the monologue message to display
     */
    public Monologue(Condition condition, String message) {
        this.condition = condition;
        this.message = message;
    }

    /**
     * Checks if this monologue is currently available to be spoken. This is determined by
     * evaluating the associated {@link Condition}.
     *
     * @return true if the condition for this monologue is met, false otherwise.
     */
    public boolean availability() {
        return condition.check();
    }

    /**
     * Gets the message content of this monologue.
     *
     * @return The string message of the monologue.
     */
    public String getMessage() {
        return message;
    }
}
