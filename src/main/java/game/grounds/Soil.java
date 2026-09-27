package game.grounds;

import edu.monash.fit2099.engine.positions.Ground;

/**
 * A class representing the fertile soil found in the valley, suitable for planting. It is
 * represented by the character '.' and named "Soil". This ground type has the
 * {@link GroundCapability#CAN_PLANT_SEED} capability, allowing actors to plant
 * {@link game.plants.Seed} items on it. It inherits other default behaviors from the {@link Ground}
 * class.
 *
 * @author Adrian Kristanto
 * Modified by: Goey Qi Hang
 */
public class Soil extends Ground {

    /**
     * Constructor for the Soil class.
     * <p>
     * Sets the display character to '.' and adds the capabilities indicating that seeds can
     * be planted here and that the ground can be burned.
     */
    public Soil() {
        super('.', "Soil");
        this.addCapability(GroundCapability.CAN_PLANT_SEED);
        this.addCapability(GroundCapability.BURNABLE);
    }
}
