package game.buying;

import game.effects.Effect;
import java.util.List;
import java.util.function.Supplier;

/**
 * Represents an offer made by a merchant (an {@link edu.monash.fit2099.engine.actors.Actor} with
 * {@link game.capabilities.GeneralCapability#CAN_SELL} capability). This class encapsulates how to
 * create the {@link Purchasable} item, its price in runes, and a list of additional
 * {@link Effect}s that are applied to the buyer upon purchasing this specific offer.
 * <p>
 * The {@link Purchasable#getBasePurchaseEffects()} from the item itself are always applied, and
 * these {@code additionalEffects} are applied on top of those.
 */
public class MerchantOffer {

    /**
     * Creates the item that is being offered for sale, so that every purchase hands over a new
     * item instance.
     */
    private final Supplier<? extends Purchasable> itemFactory;
    /**
     * The price of the item in runes.
     */
    private final int price;
    /**
     * A list of additional {@link Effect}s that are applied to the purchasing actor specifically
     * for this offer, beyond any base effects of the item itself.
     */
    private final List<Effect> additionalEffects;

    /**
     * Constructs a new MerchantOffer.
     *
     * @param itemFactory       Creates the item to be offered for sale, e.g.
     *                          {@code Broadsword::new}.
     * @param price             The price of the item in runes.
     * @param additionalEffects A list of effects that are specific to this offer and will be
     *                          applied to the buyer upon purchase, in addition to the item's base
     *                          purchase effects.
     */
    public MerchantOffer(Supplier<? extends Purchasable> itemFactory, int price,
            List<Effect> additionalEffects) {
        this.itemFactory = itemFactory;
        this.price = price;
        this.additionalEffects = additionalEffects;
    }

    /**
     * Creates a new instance of the {@link Purchasable} item associated with this offer.
     *
     * @return A new purchasable item.
     */
    public Purchasable createItem() {
        return itemFactory.get();
    }

    /**
     * Gets the price of the item in runes.
     *
     * @return The rune price.
     */
    public int getPrice() {
        return price;
    }

    /**
     * Gets the list of additional {@link Effect}s specific to this merchant offer. These effects
     * are applied to the buyer on top of any base effects defined by the {@link Purchasable} item
     * itself.
     *
     * @return A list of additional effects.
     */
    public List<Effect> getEffects() {
        return additionalEffects;
    }
}
