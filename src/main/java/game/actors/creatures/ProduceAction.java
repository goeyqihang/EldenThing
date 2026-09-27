package game.actors.creatures;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * An Action that allows a specific {@link ActorProducible} creature to produce offspring. This
 * action is typically initiated by a {@link game.behaviours.ProduceBehaviour} when the producing
 * actor meets the conditions to produce.
 */
public class ProduceAction extends Action {

    /**
     * The actor that is capable of producing offspring. It must implement the
     * {@link ActorProducible} interface.
     */
    private final ActorProducible producerActor;

    /**
     * Constructor.
     *
     * @param producer The creature that will produce offspring. It must implement
     *                 {@link ActorProducible}. The Actor performing this action should be this
     *                 producer.
     */
    public ProduceAction(ActorProducible producer) {
        this.producerActor = producer;
    }

    /**
     * Executes the produce action. It verifies that the actor performing the action is the same as
     * the {@link #producerActor}. If so, it calls the
     * {@link ActorProducible#produceOffspring(Actor, GameMap)} method on the producer.
     *
     * @param actor The actor performing the action (should be the {@link #producerActor}).
     * @param map   The game map where the action takes place.
     * @return A string describing the outcome of the production. If the producer's
     * {@code produceOffspring} method returns null or an empty string, a default message "actor
     * attempts to produce offspring." is returned. If the actor is not the producer, a message
     * indicating this is returned.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        // Ensure the actor performing the action is the one meant to produce.
        if (actor != this.producerActor) {
            return actor + " cannot force another to produce offspring.";
        }

        // Delegate to the producer's specific logic.
        String result = this.producerActor.produceOffspring(actor, map);

        // Provide a default message if the specific production logic doesn't.
        if (result == null || result.isEmpty()) {
            return actor + " attempts to produce offspring."; // Default message
        }
        return result;
    }

    /**
     * Returns a description of this action suitable for display in a menu. This action is typically
     * not chosen from a menu by the creature itself, but this provides a standard description.
     *
     * @param actor The actor performing the action.
     * @return A string describing the action, e.g., "OmenSheep produces offspring".
     */
    @Override
    public String menuDescription(Actor actor) {
        // This action is typically not chosen from a menu by the creature itself.
        return actor + " produces offspring";
    }
}
