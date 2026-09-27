package game.plants;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;

/**
 * An abstract base class for all plant-like entities in the game. Since plants occupy a location on
 * the map similarly to terrain, this class extends {@link Ground}. Concrete subclasses must
 * implement the specific behaviors for instant effects upon planting and periodic effects that
 * occur each turn (tick).
 */
public abstract class Plant extends Ground {

    /**
     * Constructor for the Plant class. Passes the display character and name to the parent
     * {@link Ground} constructor.
     *
     * @param displayChar The character used to represent this Plant on the game map.
     * @param name        The name of this Plant.
     */
    public Plant(char displayChar, String name) {
        super(displayChar, name);
    }

    /**
     * Abstract method to define and execute any effects that occur *immediately* when this plant is
     * placed on the map (typically when planted by an actor). Subclasses must implement this to
     * define their specific planting effects (e.g., damaging the planter, purifying surroundings).
     *
     * @param planter  The {@link Actor} who performed the action that resulted in this plant being
     *                 placed.
     * @param location The {@link Location} where the plant is placed.
     * @return A string describing the instant effects that occurred. Can be empty if there are no
     * instant effects.
     */
    public abstract String executeInstantEffects(Actor planter, Location location);

    /**
     * Abstract method called once per turn, allowing the plant to perform periodic actions or
     * effects. Subclasses must implement this to define their ongoing behavior (e.g., healing
     * nearby actors, damaging nearby actors, growing, producing items).
     *
     * @param location The current {@link Location} of this Plant on the map.
     */
    @Override
    public abstract void tick(Location location);
}
