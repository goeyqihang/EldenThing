package game.buying;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.effects.Effect;
import java.util.List;

/**
 * An {@link Action} that allows an {@link Actor} (buyer) to purchase a {@link Purchasable} item
 * from another {@link Actor} (merchant). This action handles rune deduction from the buyer, adding
 * the item to the buyer's inventory, and applying any associated {@link Effect}s from both the
 * item's base properties and the specific {@link MerchantOffer}.
 */
public class PurchaseAction extends Action {

    /**
     * The item that the actor intends to purchase. Must implement {@link Purchasable}.
     */
    private final Purchasable itemToPurchase;
    /**
     * The cost of the item in runes.
     */
    private final int runePrice;
    /**
     * The merchant {@link Actor} selling the item.
     */
    private final Actor merchant;
    /**
     * A list of {@link Effect}s specific to this purchase offer, which will be applied to the buyer
     * in addition to the item's base effects.
     */
    private final List<Effect> purchaseEffects;

    /**
     * Constructor for PurchaseAction.
     *
     * @param itemToPurchase  The {@link Purchasable} item to be bought.
     * @param runePrice       The price of the item in runes.
     * @param merchant        The {@link Actor} selling the item.
     * @param purchaseEffects A list of {@link Effect}s that are specific to this
     *                        {@link MerchantOffer} and will be applied to the buyer upon successful
     *                        purchase. These are in addition to any effects defined by
     *                        {@link Purchasable#getBasePurchaseEffects()}.
     */
    public PurchaseAction(Purchasable itemToPurchase, int runePrice, Actor merchant,
            List<Effect> purchaseEffects) {
        this.itemToPurchase = itemToPurchase;
        this.runePrice = runePrice;
        this.merchant = merchant;
        this.purchaseEffects = purchaseEffects;
    }

    /**
     * Executes the purchase action:
     * <ol>
     * <li>Checks if the {@code buyer} has enough runes ({@link Actor#getBalance()}). If not,
     * returns a message indicating insufficient funds.</li>
     * <li>Deducts the {@link #runePrice} from the buyer's balance.</li>
     * <li>Transfers the {@link #itemToPurchase} to the buyer using
     * {@link Purchasable#sellTo(Actor)}.</li>
     * <li>Applies the item's base purchase effects
     * ({@link Purchasable#getBasePurchaseEffects()}).</li>
     * <li>Applies the additional {@link #purchaseEffects} of the specific
     * {@link MerchantOffer}.</li>
     * </ol>
     *
     * @param buyer The {@link Actor} performing the purchase (the buyer).
     * @param map   The {@link GameMap} where the action takes place.
     * @return A string describing the outcome of the purchase attempt (success or failure due to
     * insufficient runes).
     */
    @Override
    public String execute(Actor buyer, GameMap map) {
        // Check if a buyer has enough runes
        if (buyer.getBalance() < runePrice) {
            return buyer + " does not have enough runes to buy the " + itemToPurchase + ".";
        }

        // Deduct runes from buyer
        buyer.deductBalance(runePrice);

        // Give item to buyer
        itemToPurchase.sellTo(buyer);

        // Apply base purchase effects from the item itself
        for (Effect effect : itemToPurchase.getBasePurchaseEffects()) {
            effect.applyEffect(buyer, map);
        }

        // Apply all additional stored purchase effects from the merchant's offer
        for (Effect effect : purchaseEffects) {
            effect.applyEffect(buyer, map);
        }

        return buyer + " bought a " + itemToPurchase + " from " + merchant + " for "
                + runePrice + " runes.";
    }

    /**
     * Returns a description of this action suitable for display in a menu.
     *
     * @param actor The {@link Actor} considering the purchase.
     * @return A string describing the purchase option, including the item, its price, and the
     * merchant. e.g., "Player buys Broadsword (150 runes) from Kale".
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " buys " + itemToPurchase + " (" + runePrice + " runes) from " + merchant;
    }
}
