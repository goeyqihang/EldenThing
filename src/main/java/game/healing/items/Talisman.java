package game.healing.items;

import edu.monash.fit2099.engine.items.Item;
import game.capabilities.GeneralCapability;
import game.healing.Curable;
import game.healing.CureAction;

/**
 * A class representing a Talisman item. This item is portable (can be picked up and dropped) and
 * has the {@link GeneralCapability#CAN_CURE} capability, so actors carrying it are offered a
 * {@link CureAction} on nearby {@link Curable} actors and ground tiles. Represented by 'o' on the
 * map.
 *
 * @author Adrian Kristanto
 * Modified by: Goey Qi Hang
 */
public class Talisman extends Item {

    /**
     * Constructor for the Talisman. Initializes the item with the name "Talisman", display
     * character 'o', and makes it portable.
     */
    public Talisman() {
        super("Talisman", 'o', true);
        this.addCapability(GeneralCapability.CAN_CURE);
    }
}
