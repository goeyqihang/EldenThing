package game.actors.creatures.boss.parts;

import edu.monash.fit2099.engine.actors.Actor;
import java.util.List;
import java.util.Random;

/**
 * Represents a branch part of a boss.
 * A Branch contributes to the boss's damage and can grow further, adding new parts to the boss.
 * A branch can only be productive once, after which it may grow a non-productive part like a leaf.
 *
 * @see BossPart
 * @see game.actors.creatures.boss.BedOfChaos
 */
public class Branch implements BossPart {

    /**
     * The additional damage this branch adds to the boss's attacks.
     */
    private static final int DAMAGE_CONTRIBUTION = 3;

    /**
     * A random number generator to decide the outcome of growth.
     */
    private final Random random = new Random();

    /**
     * A flag indicating whether this branch can continue to grow new parts.
     */
    private boolean isProductive = true;

    /**
     * Gets the damage contribution of this branch.
     *
     * @return The fixed damage value of {@value #DAMAGE_CONTRIBUTION}.
     */
    @Override
    public int getDamageContribution() {
        return DAMAGE_CONTRIBUTION;
    }

    /**
     * Executes the growth logic for this branch.
     * If the branch is productive, it will randomly grow another Branch or a Leaf.
     * If it grows a Leaf, this branch becomes non-productive and cannot grow further.
     *
     * @param actor       The main body of the boss.
     * @param directParts The list of parts attached to the boss, to which new parts will be added.
     * @return A string describing the growth that occurred, or an empty string if the branch is no
     * longer productive.
     */
    @Override
    public String grow(Actor actor, List<BossPart> directParts) {
        if (isProductive) {
            StringBuilder returnMessage = new StringBuilder();
            returnMessage.append("Branch is growing...\n");

            if (random.nextBoolean()) {
                directParts.add(new Branch());
                returnMessage.append("It grows a Branch...\n");
            } else {
                directParts.add(new Leaf());
                this.isProductive = false;
                returnMessage.append("It grows a Leaf...\n");
            }

            return returnMessage.toString();
        }
        return "";
    }
}
