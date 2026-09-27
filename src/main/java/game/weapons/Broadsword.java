package game.weapons;

import edu.monash.fit2099.engine.actors.Actor;
import game.buying.Purchasable;
import game.effects.Effect;
import game.effects.HealEffect;
import java.util.List;

/**
 * Represents a Broadsword, a type of {@link WeaponItem} in the game. The Broadsword has specific
 * damage, verb, and hit rate characteristics. It also implements the {@link Purchasable} interface,
 * meaning it can be bought from merchants. Purchasing a Broadsword provides a base
 * {@link HealEffect} to the buyer.
 */
public class Broadsword extends WeaponItem implements Purchasable {

    /**
     * Constructor for the Broadsword. Initializes the Broadsword with the name "Broadsword",
     * display character 'b', 30 damage, "slashes" as its verb, and a 50% hit rate.
     */
    public Broadsword() {
        super("Broadsword", 'b', 30, "slashes", 50);
    }

    /**
     * Gets the base effects applied to an {@link Actor} upon purchasing this Broadsword. For the
     * Broadsword, this includes a {@link HealEffect} that restores 10 health points.
     *
     * @return A list containing a {@link HealEffect} of 10 HP.
     */
    @Override
    public List<Effect> getBasePurchaseEffects() {
        // Purchasing a Broadsword heals the buyer a bit
        return List.of(new HealEffect(10));
    }

    /**
     * Defines how the Broadsword is transferred to the purchasing {@link Actor}. This method adds
     * the Broadsword instance to the actor's inventory.
     *
     * @param actor The {@link Actor} who is purchasing this Broadsword.
     */
    @Override
    public void sellTo(Actor actor) {
        actor.addItemToInventory(this); // Add this broadsword to the actor's inventory
    }
}
