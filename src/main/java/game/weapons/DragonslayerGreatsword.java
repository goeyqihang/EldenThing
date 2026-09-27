package game.weapons;

import edu.monash.fit2099.engine.actors.Actor;
import game.buying.Purchasable;
import game.effects.Effect;
import game.effects.IncreaseMaxHealthEffect;
import java.util.List;

/**
 * Represents a Dragonslayer Greatsword, a powerful type of {@link WeaponItem} in the game. This
 * greatsword has high damage, a specific verb ("strikes"), and a good hit rate. It also implements
 * the {@link Purchasable} interface, allowing it to be bought from merchants. Purchasing a
 * Dragonslayer Greatsword provides a base {@link IncreaseMaxHealthEffect} to the buyer, increasing
 * their maximum health by 15.
 */
public class DragonslayerGreatsword extends WeaponItem implements Purchasable {

    /**
     * Constructor for the DragonslayerGreatsword. Initializes the Dragonslayer Greatsword with the
     * name "Dragonslayer Greatsword", display character 'D', 70 damage, "strikes" as its verb, and
     * a 75% hit rate.
     */
    public DragonslayerGreatsword() {
        super("Dragonslayer Greatsword", 'D', 70, "strikes", 75);
    }

    /**
     * Gets the base effects applied to an {@link Actor} upon purchasing this Dragonslayer
     * Greatsword. For this weapon, purchasing it grants an {@link IncreaseMaxHealthEffect} that
     * increases the buyer's maximum health by 15 points.
     *
     * @return A list containing an {@link IncreaseMaxHealthEffect} of 15.
     */
    @Override
    public List<Effect> getBasePurchaseEffects() {
        // Purchasing this increases max health
        return List.of(new IncreaseMaxHealthEffect(15));
    }

    /**
     * Defines how the Dragonslayer Greatsword is transferred to the purchasing {@link Actor}. This
     * method adds the Dragonslayer Greatsword instance to the actor's inventory.
     *
     * @param actor The {@link Actor} who is purchasing this Dragonslayer Greatsword.
     */
    @Override
    public void sellTo(Actor actor) {
        actor.addItemToInventory(this); // Add this greatsword to the actor's inventory
    }
}
