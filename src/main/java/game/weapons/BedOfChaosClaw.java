package game.weapons;

import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;

/**
 * Represents the intrinsic weapon of the Bed of Chaos boss.
 * This class extends {@link IntrinsicWeapon} and adds the functionality
 * to dynamically change its damage.
 *
 * @see game.actors.creatures.boss.BedOfChaos
 */
public class BedOfChaosClaw extends IntrinsicWeapon {

    /**
     * Constructor for BedOfChaosClaw.
     *
     * @param initialDamage The initial base damage of the claw.
     * @param verb          The verb to use for the attack description (e.g., "strikes").
     * @param hitRate       The chance (percentage) to hit the target.
     */
    public BedOfChaosClaw(int initialDamage, String verb, int hitRate) {
        super(initialDamage, verb, hitRate);
    }

    /**
     * Sets a new damage value for the weapon.
     * This allows the Bed of Chaos's damage to scale as it grows more parts.
     *
     * @param newDamage The new damage value.
     */
    public void setDamage(int newDamage) {
        this.damage = newDamage;
    }
}
