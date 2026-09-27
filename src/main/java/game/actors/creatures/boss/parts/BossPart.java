package game.actors.creatures.boss.parts;

import edu.monash.fit2099.engine.actors.Actor;
import java.util.List;

/**
 * An interface for components that make up a larger boss entity.
 * Each BossPart can contribute to the boss's overall power and has its own growth logic.
 *
 * @see game.actors.creatures.boss.BedOfChaos
 */
public interface BossPart {

    /**
     * Gets the amount of additional damage this part contributes to the boss's attacks.
     *
     * @return the damage contribution as an integer.
     */
    int getDamageContribution();

    /**
     * Defines the growth behaviour for this part.
     * This method can trigger further growth, add new parts to the boss, or have other effects.
     *
     * @param actor       The main body of the boss to which this part belongs.
     * @param directParts The list of parts directly attached to the boss, which can be
     *                    modified by this method.
     * @return A string describing the growth that occurred.
     */
    String grow(Actor actor, List<BossPart> directParts);
}
