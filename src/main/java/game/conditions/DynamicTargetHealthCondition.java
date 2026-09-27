package game.conditions;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;

/**
 * A condition that checks if a specific target Actor's health
 * is above a given threshold.
 * This class implements the general {@link Condition} interface.
 */
public class DynamicTargetHealthCondition implements Condition {

    /**
     * The actor whose health is checked.
     */
    private final Actor target;

    /**
     * The condition holds when the target's health is strictly greater than this value.
     */
    private final int healthThreshold;

    /**
     * Constructs a DynamicTargetHealthCondition.
     *
     * @param target          The Actor whose health needs to be checked.
     * @param healthThreshold The health threshold. The condition is true if the target's health
     *                        is strictly greater than this value.
     */
    public DynamicTargetHealthCondition(Actor target, int healthThreshold) {
        this.target = target;
        this.healthThreshold = healthThreshold;
    }

    /**
     * Checks if the configured target's health is above the configured threshold.
     *
     * @return true if the target exists, has health, and health is greater than the threshold;
     * false otherwise.
     */
    @Override
    public boolean check() {
        if (this.target != null && this.target.hasAttribute(BaseActorAttributes.HEALTH)) {
            Integer targetHp = this.target.getAttribute(BaseActorAttributes.HEALTH);
            return targetHp != null && targetHp > this.healthThreshold;
        }
        return false;
    }
}
