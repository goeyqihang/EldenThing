package game.plants;

/**
 * Represents a specific type of {@link Seed} item used to grow an {@link Inheritree} plant.
 * This seed has a defined name and stamina cost associated with planting it.
 */
public class InheritreeSeed extends Seed {

    /**
     * The constant name for the Inheritree Seed, used for identification and display.
     */
    public static final String NAME = "Inheritree Seed";

    /**
     * The stamina an actor spends to plant an Inheritree Seed.
     */
    private static final int STAMINA_COST = 25;

    /**
     * Constructor for the InheritreeSeed.
     * Initializes the seed with its specific name using the superclass constructor.
     */
    public InheritreeSeed() {
        super(InheritreeSeed.NAME);
    }

    /**
     * Creates and returns a new instance of the {@link Inheritree} plant.
     * This method is called when the seed is successfully planted.
     *
     * @return A new {@link Inheritree} object.
     */
    @Override
    public Plant createPlant() {
        return new Inheritree();
    }

    /**
     * Gets the stamina cost required for an actor to plant this specific seed.
     *
     * @return The stamina cost ({@value #STAMINA_COST}) for planting an Inheritree Seed.
     */
    @Override
    public int getStaminaCost() {
        return STAMINA_COST;
    }
}
