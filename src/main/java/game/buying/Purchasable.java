package game.buying;

import edu.monash.fit2099.engine.actors.Actor;
import game.effects.Effect;
import java.util.List;

/**
 * An interface for items or entities that can be purchased by an {@link Actor}. Implementing
 * classes must define how the item is transferred to the buyer and what base {@link Effect}s are
 * applied upon purchase.
 * <p>
 * This interface is used in conjunction with {@link PurchaseAction} and {@link MerchantOffer} to
 * facilitate a buying and selling mechanic in the game.
 */
public interface Purchasable {

    /**
     * Gets a list of base {@link Effect}s that are applied to the purchasing {@link Actor} when
     * this item is bought. These effects are inherent to the item itself, regardless of the
     * specific merchant offer.
     *
     * @return A list of {@link Effect} objects. This list can be empty if the item has no base
     * purchase effects.
     */
    List<Effect> getBasePurchaseEffects();

    /**
     * Defines how this item is transferred to the purchasing {@link Actor}. Typically, this
     * involves adding the item to the actor's inventory.
     *
     * @param actor The {@link Actor} who is purchasing this item.
     */
    void sellTo(Actor actor);
}
