package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.creatures.ActorProducible;
import game.actors.creatures.ProduceAction;

/**
 * A {@link Behaviour} that allows an {@link Actor} which implements {@link ActorProducible} to
 * attempt to produce offspring or an egg. This behaviour checks if the
 * {@link ActorProducible#canProduceOffspring(Actor, GameMap)} condition is met and, if so, returns
 * a {@link ProduceAction}.
 */
public class ProduceBehaviour implements Behaviour {

    /**
     * The actor that is capable of producing. This actor must implement the {@link ActorProducible}
     * interface.
     */
    private final ActorProducible producer;

    /**
     * Constructor for ProduceBehaviour.
     *
     * @param producerActor The actor that will be checked for its ability to produce and for whom a
     *                      {@link ProduceAction} will be created. This actor must implement
     *                      {@link ActorProducible}.
     */
    public ProduceBehaviour(ActorProducible producerActor) {
        this.producer = producerActor;
    }

    /**
     * Determines if the associated {@link #producer} actor can produce offspring/egg and returns a
     * {@link ProduceAction} if conditions are met.
     *
     * <p>This method calls {@link ActorProducible#canProduceOffspring(Actor, GameMap)}
     * on the {@link #producer}. If it returns true, a new {@link ProduceAction} targeting the
     * {@link #producer} is returned. Otherwise, null is returned, indicating that no production
     * action should be taken this turn.</p>
     *
     * @param actor The {@link Actor} performing the behaviour (this should be the same instance as
     *              {@link #producer}).
     * @param map   The {@link GameMap} where the actor is located.
     * @return A {@link ProduceAction} if the actor can produce, otherwise {@code null}.
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {
        // Check if the producer (which should be the actor this behaviour is attached to)
        // can produce offspring in the current state of the game.
        if (producer.canProduceOffspring(actor, map)) {
            // If conditions are met, return an action to produce.
            return new ProduceAction(producer);
        }
        // If conditions are not met, no action is taken.
        return null;
    }
}
