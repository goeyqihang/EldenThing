package game.hatching;

import edu.monash.fit2099.engine.actors.Actor;
import game.conditions.Condition;
import java.util.function.Supplier;

/**
 * Represents a set of rules that govern the hatching of an {@link Egg}. Each {@code HatchingRules}
 * instance pairs a {@link Condition} with a {@link java.util.function.Supplier} for creating an
 * {@link Actor} (the hatchling).
 * <p>
 * When an {@link Egg} attempts to hatch, it will iterate through its list of {@code HatchingRules}.
 * For each rule, if its {@link #condition} is met (evaluates to true via
 * {@link Condition#check()}), the {@link #creatureSupplier} is used to generate a new {@link Actor}
 * instance, which is then returned as the result of the hatching attempt.
 * </p>
 */
public class HatchingRules {

    /**
     * The condition that must be met for this hatching rule to apply. If {@link Condition#check()}
     * returns true, the egg may hatch according to this rule.
     */
    private final Condition condition;
    /**
     * A supplier function that, when called, returns a new instance of the {@link Actor} that
     * should hatch if the {@link #condition} is met. Using a {@link java.util.function.Supplier}
     * allows for deferred creation of the actor.
     */
    private final Supplier<Actor> creatureSupplier;

    /**
     * Constructs a {@code HatchingRules} instance.
     *
     * @param condition        The {@link Condition} that must be satisfied for this rule to trigger
     *                         a hatch. Must not be null.
     * @param creatureSupplier A {@link java.util.function.Supplier} that provides a new instance of
     *                         the {@link Actor} to be hatched when the condition is met. For
     *                         example, {@code OmenSheep::new}. Must not be null.
     */
    public HatchingRules(Condition condition, Supplier<Actor> creatureSupplier) {
        this.condition = condition;
        this.creatureSupplier = creatureSupplier;
    }

    /**
     * Attempts to hatch a creature based on this rule. First, it checks if the associated
     * {@link #condition} is met by calling {@link Condition#check()}. If the condition is true, it
     * then uses the {@link #creatureSupplier} to get a new instance of the hatchling
     * {@link Actor}.
     *
     * @return A new {@link Actor} instance (the hatchling) if the condition is met and the supplier
     * provides an actor; otherwise, returns {@code null}.
     */
    public Actor tryHatch() {
        if (condition.check()) {
            return creatureSupplier.get(); // Create and return the new actor
        }
        return null; // Condition not met, or supplier returned null (though typically shouldn't)
    }
}
