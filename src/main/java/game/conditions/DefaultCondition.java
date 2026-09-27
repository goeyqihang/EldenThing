package game.conditions;

/**
 * A default implementation of the {@link Condition} interface that always evaluates to true.
 * <p>
 * This condition can be used as a placeholder or a fallback when an action or event should always
 * be available or triggered, irrespective of other game states. For example, it can be used for
 * default monologues that an NPC can always say.
 * </p>
 */
public class DefaultCondition implements Condition {

    /**
     * Checks the condition. For {@code DefaultCondition}, this always returns true.
     *
     * @return {@code true} always.
     */
    @Override
    public boolean check() {
        return true;
    }
}
