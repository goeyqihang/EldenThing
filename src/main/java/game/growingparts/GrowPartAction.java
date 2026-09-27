package game.growingparts;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * An action that triggers the growth process for a {@link Growable} entity.
 * This action is typically initiated by a {@link GrowPartBehaviour}.
 */
public class GrowPartAction extends Action {

    /**
     * The entity that will perform the growth.
     */
    private final Growable grower;

    /**
     * Constructor for GrowPartAction.
     *
     * @param grower The {@link Growable} entity that will execute its growth logic.
     */
    public GrowPartAction(Growable grower) {
        this.grower = grower;
    }

    /**
     * Executes the growth action.
     * It ensures the actor performing the action is the intended grower, then calls the
     * {@link Growable#attemptGrow()} method to perform the growth.
     *
     * @param actor The actor performing the action.
     * @param map   The map the actor is on.
     * @return A string describing the result of the growth attempt.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        if (actor != grower) {
            return actor + " cannot make another entity grow.";
        }
        return actor + " is growing...\n" + grower.attemptGrow().stripTrailing();
    }

    /**
     * Returns a description of this action suitable for displaying in a menu.
     *
     * @param actor The actor performing the action.
     * @return A string describing the action.
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " grows";
    }
}
