package game.fishing;

import edu.monash.fit2099.engine.items.Item;
import game.capabilities.GeneralCapability;

/**
 * An item that represents a fishing rod.
 * This item provides the {@link GeneralCapability#CAN_FISH} capability, allowing its owner
 * to perform fishing actions.
 */
public class FishingRod extends Item {

    /**
     * Constructor.
     * Initializes the FishingRod with a name, display character, and makes it portable.
     * It also grants the CAN_FISH capability.
     */
    public FishingRod() {
        super("Fishing Rod", 'R', true);
        this.addCapability(GeneralCapability.CAN_FISH);
    }
}
