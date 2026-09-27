package game.growingparts;

/**
 * An interface for entities that have a growth mechanic.
 * Classes implementing this interface must define the logic for what happens
 * when they attempt to grow, which is typically triggered by a behavior.
 *
 * @see GrowPartBehaviour
 * @see GrowPartAction
 */
public interface Growable {

    /**
     * Defines the growth behavior for the implementing entity.
     * This method contains the logic for how the entity grows, potentially adding new parts
     * or having other effects on itself or the game world.
     *
     * @return A string describing the outcome of the growth attempt.
     */
    String attemptGrow();
}
