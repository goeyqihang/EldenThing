package game.weapons;

import edu.monash.fit2099.engine.actors.Actor;
import game.buying.Purchasable;
import game.effects.DamageEffect;
import game.effects.Effect;
import java.util.List;

/**
 * Represents a Katana, a type of {@link WeaponItem} in the game. The Katana is characterized by its
 * damage, verb ("slashes"), and hit rate. It implements the {@link Purchasable} interface,
 * indicating it can be bought from merchants. Purchasing a Katana applies a base
 * {@link DamageEffect} to the buyer, dealing 25 damage to them as a side effect of acquiring it.
 */
public class Katana extends WeaponItem implements Purchasable {

    /**
     * Constructor for the Katana. Initializes the Katana with the name "Katana", display character
     * 'j', 50 damage, "slashes" as its verb, and a 60% hit rate.
     */
    public Katana() {
        super("Katana", 'j', 50, "slashes", 60);
    }

    /**
     * Gets the base effects applied to an {@link Actor} upon purchasing this Katana. For the
     * Katana, this includes a {@link DamageEffect} that inflicts 25 points of damage upon the
     * buyer.
     *
     * @return A list containing a {@link DamageEffect} of 25.
     */
    @Override
    public List<Effect> getBasePurchaseEffects() {
        // Purchasing a Katana damages the buyer
        return List.of(new DamageEffect(25));
    }

    /**
     * Defines how the Katana is transferred to the purchasing {@link Actor}. This method adds the
     * Katana instance to the actor's inventory.
     *
     * @param actor The {@link Actor} who is purchasing this Katana.
     */
    @Override
    public void sellTo(Actor actor) {
        actor.addItemToInventory(this); // Add this katana to the actor's inventory
    }
}
