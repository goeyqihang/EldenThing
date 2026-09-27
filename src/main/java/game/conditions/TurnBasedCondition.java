package game.conditions;

/**
 * A {@link Condition} that checks if the current game turn has exceeded a specific turn number.
 * <p>
 * This condition is met (returns {@code true}) if the {@code currentTurn} provided during
 * construction is strictly greater than the {@code specificTurn} also provided during construction.
 * It can be used to trigger events or actions that should only occur after a certain amount of game
 * time has passed.
 * </p>
 */
public class TurnBasedCondition implements Condition {

    /**
     * The current turn number in the game at the time of checking the condition.
     */
    private final int currentTurn;
    /**
     * The specific turn number that the {@link #currentTurn} must exceed for this condition to be
     * true.
     */
    private final int specificTurn;

    /**
     * Constructs a {@code TurnBasedCondition}.
     *
     * @param currentTurn  The current turn number of the game when this condition is being
     *                     evaluated.
     * @param specificTurn The turn number that {@code currentTurn} must surpass for the condition
     *                     to be met.
     */
    public TurnBasedCondition(int currentTurn, int specificTurn) {
        this.currentTurn = currentTurn;
        this.specificTurn = specificTurn;
    }

    /**
     * Checks if the current turn number is greater than the specific turn number.
     *
     * @return {@code true} if {@link #currentTurn} is strictly greater than {@link #specificTurn},
     * {@code false} otherwise.
     */
    @Override
    public boolean check() {
        return currentTurn > specificTurn;
    }
}
