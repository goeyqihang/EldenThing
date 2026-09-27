package game.conditions;

import edu.monash.fit2099.engine.actors.Actor;

/**
 * A {@link Condition} that checks whether a given {@link Actor}'s inventory is empty.
 * <p>
 * This condition evaluates to true if the actor's item inventory (retrieved by
 * {@link Actor#getItemInventory()}) contains no items. It can be used, for example, to trigger
 * specific NPC monologues or game events when the player or another actor is unencumbered.
 * </p>
 */
public class EmptyInventoryCondition implements Condition {

    /**
     * The actor whose inventory will be checked.
     */
    private final Actor actor;

    /**
     * Constructs an {@code EmptyInventoryCondition} for a specific actor.
     *
     * @param actor The {@link Actor} whose inventory status (empty or not) will be evaluated by
     *              this condition. Must not be null.
     */
    public EmptyInventoryCondition(Actor actor) {
        this.actor = actor;
    }

    /**
     * Checks if the associated actor's inventory is empty.
     *
     * @return {@code true} if the actor's item inventory is empty, {@code false} otherwise.
     */
    @Override
    public boolean check() {
        return actor.getItemInventory().isEmpty(); // Checks if actor's inventory is empty
    }
}
