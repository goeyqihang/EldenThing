package game.conditions;

/**
 * An interface representing a condition that can be checked within the game.
 * This is a functional interface whose instances are used to determine if certain
 * criteria are met, often to gate actions, trigger events, or select dialogue.
 * <p>
 * Implementations of this interface will define specific criteria in their
 * {@link #check()} method. For example, a condition could check an actor's health,
 * inventory, location, or the state of the game world.
 * </p>
 */
public interface Condition {

    /**
     * Checks if the specific condition defined by the implementing class is currently met.
     *
     * @return {@code true} if the condition is satisfied, {@code false} otherwise.
     */
    boolean check();
}
