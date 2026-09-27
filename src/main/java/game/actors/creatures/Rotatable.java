package game.actors.creatures;

import edu.monash.fit2099.engine.positions.GameMap;

/**
 * Interface for actors that are susceptible to rot and have a countdown timer.
 */
public interface Rotatable {

    /**
     * Decrements the internal countdown timer for the rot effect. Should be called each turn.
     * Checks if the countdown has reached zero and handles expiry if necessary.
     *
     * @param map The map the actor is on.
     */
    void tickRotCountdown(GameMap map);

    /**
     * Resets the rot countdown timer back to its initial value.
     */
    void resetRotCountdown();

    /**
     * Checks if the rot countdown timer has expired (reached zero or less).
     *
     * @return true if the timer has expired, false otherwise.
     */
    boolean isRotExpired();

    /**
     * Gets the initial duration (in turns) for the rot countdown for this entity.
     *
     * @return The initial countdown value.
     */
    int getInitialRotCountdown();

    /**
     * Gets the current remaining turns on the rot countdown.
     *
     * @return The current countdown value.
     */
    int getCurrentRotCountdown();
}
