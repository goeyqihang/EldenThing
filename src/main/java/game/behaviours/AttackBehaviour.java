package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.weapons.actions.AttackAction;

/**
 * A behaviour that allows an NPC {@link Actor} to attack another {@link Actor}. The decision to
 * attack is delegated to the NPC itself via the {@link AttackConditionEvaluator} interface.
 */
public class AttackBehaviour implements Behaviour {

    /**
     * Decides whether the actor should attack a given target.
     */
    private final AttackConditionEvaluator conditionEvaluator;

    /**
     * Constructs an AttackBehaviour.
     *
     * @param conditionEvaluator The object (typically an NPC implementing
     *                           {@link AttackConditionEvaluator}) that will provide the decision
     *                           logic for attacking.
     */
    public AttackBehaviour(AttackConditionEvaluator conditionEvaluator) {
        this.conditionEvaluator = conditionEvaluator;
    }

    /**
     * Determines and returns an {@link AttackAction} if a suitable target is found and the
     * {@link AttackConditionEvaluator} permits the attack.
     *
     * @param actor The {@link Actor} performing this behaviour (this should be the NPC that
     *              implements {@link AttackConditionEvaluator} and was passed to the constructor).
     * @param map   The {@link GameMap} where the actor is located.
     * @return an {@link AttackAction} directed at a valid target if the NPC decides to attack;
     * otherwise, {@code null}.
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {
        Location currentLocation = map.locationOf(actor);

        for (Exit exit : currentLocation.getExits()) {
            Location destination = exit.getDestination();

            if (destination.containsAnActor()) {
                Actor potentialTarget = destination.getActor();

                // Delegate the decision to attack to the injected evaluator
                if (potentialTarget != actor
                        && this.conditionEvaluator.evaluate(actor, potentialTarget, map)) {
                    // Without a weapon, AttackAction uses the actor's intrinsic weapon
                    return new AttackAction(potentialTarget, exit.getName());
                }
            }
        }
        // No target found or evaluator decided not to attack
        return null;
    }
}
