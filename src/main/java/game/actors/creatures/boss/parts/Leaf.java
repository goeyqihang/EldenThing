package game.actors.creatures.boss.parts;

import edu.monash.fit2099.engine.actors.Actor;
import java.util.List;

/**
 * Represents a leaf part of a boss.
 * A Leaf contributes a small amount to the boss's damage and provides a healing effect to the boss
 * during its growth phase. A Leaf is a terminal part and does not grow further.
 *
 * @see BossPart
 * @see game.actors.creatures.boss.BedOfChaos
 */
public class Leaf implements BossPart {

    /**
     * The additional damage this leaf adds to the boss's attacks.
     */
    private static final int DAMAGE_CONTRIBUTION = 1;

    /**
     * The hit points this leaf restores to the boss each time it grows.
     */
    private static final int HEAL_AMOUNT = 5;

    /**
     * Gets the damage contribution of this leaf.
     *
     * @return The fixed damage value of {@value #DAMAGE_CONTRIBUTION}.
     */
    @Override
    public int getDamageContribution() {
        return DAMAGE_CONTRIBUTION;
    }

    /**
     * Executes the "growth" logic for this leaf.
     * Instead of growing new parts, a Leaf heals the main body of the boss.
     *
     * @param actor       The main body of the boss to be healed.
     * @param directParts The list of parts attached to the boss (not modified by this method).
     * @return A string describing the healing effect.
     */
    @Override
    public String grow(Actor actor, List<BossPart> directParts) {
        actor.heal(HEAL_AMOUNT);
        return "A Leaf heals the boss for " + HEAL_AMOUNT + " HP.\n";
    }
}
