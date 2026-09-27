package game.eating;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * An {@link Action} that allows an {@link Actor} to eat an {@link Eatable} target.
 * <p>
 * When this action is executed, it delegates the eating logic to the
 * {@link Eatable#eatenBy(Actor, GameMap)} method of the target. The menu description for this
 * action is also provided by the target through {@link Eatable#getEatMenuDescription(Actor)}.
 * </p>
 */
public class EatAction extends Action {

    /**
     * The target that will be eaten. Must implement the {@link Eatable} interface.
     */
    private final Eatable target;

    /**
     * Constructor for EatAction.
     *
     * @param target The {@link Eatable} entity (which could be an
     *               {@link edu.monash.fit2099.engine.items.Item} or an {@link Actor}) to be eaten.
     *               Must not be null.
     */
    public EatAction(Eatable target) {
        this.target = target;
    }

    /**
     * Executes the eating action.
     * <p>
     * This method calls the {@link Eatable#eatenBy(Actor, GameMap)} method on the {@link #target}
     * {@link Eatable} instance, passing the {@code actor} (the eater) and the {@code map}. The
     * specific effects of being eaten (e.g., healing, item removal) are handled within the target's
     * implementation of {@code eatenBy}.
     * </p>
     *
     * @param actor The {@link Actor} performing the action (the eater).
     * @param map   The {@link GameMap} where the action takes place.
     * @return A string describing the result of the eating action, as returned by the target's
     * {@code eatenBy} method.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        return this.target.eatenBy(actor, map);
    }

    /**
     * Returns a description of this action suitable for display in a menu.
     * <p>
     * This method retrieves the menu description by calling the
     * {@link Eatable#getEatMenuDescription(Actor)} method on the {@link #target} {@link Eatable}
     * instance.
     * </p>
     *
     * @param actor The {@link Actor} who might perform the action.
     * @return A string describing the eat action, specific to the target being eaten. For example,
     * "Player eats Omen Sheep Egg".
     */
    @Override
    public String menuDescription(Actor actor) {
        // Get the specific menu description from the Eatable item/actor
        return this.target.getEatMenuDescription(actor);
    }
}
