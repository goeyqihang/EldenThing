package game.behaviours;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * Defines a contract for entities (typically NPCs) that can evaluate whether an attack should be
 * executed against a potential target. An NPC implementing this can provide its specific attack
 * decision logic.
 */
@FunctionalInterface
public interface AttackConditionEvaluator {

    /**
     * Evaluates if the attacker should proceed with an attack against the potentialTarget. This
     * method encapsulates the NPC's specific attack conditions.
     *
     * @param attacker        The actor considering the attack (typically the NPC itself).
     * @param potentialTarget The actor being considered as a target.
     * @param map             The current game map.
     * @return true if the conditions for attacking the potentialTarget are met, false otherwise.
     */
    boolean evaluate(Actor attacker, Actor potentialTarget, GameMap map);
}
