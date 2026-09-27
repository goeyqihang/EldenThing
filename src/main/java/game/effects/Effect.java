package game.effects;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * An interface representing an effect that can be applied to an
 * {@link Actor} within the game.
 * <p>
 * Effects are typically applied as a consequence of certain actions or events, such as purchasing
 * an item, consuming an item, or being affected by a skill or environmental factor. Implementations
 * of this interface will define the specific logic of what the effect does in the
 * {@link #applyEffect(Actor, GameMap)} method.
 * </p>
 */
public interface Effect {

    /**
     * Applies the specific effect to the given actor. The implementation of this method will
     * contain the logic for how the effect modifies the actor's state or the game world.
     *
     * @param actor The {@link Actor} to whom the effect will be
     *              applied. Must not be null.
     * @param map   The {@link GameMap} where the effect is
     *              taking place. This can be used if the effect needs to interact with or modify
     *              the map, though not all effects will require it. Must not be null.
     */
    void applyEffect(Actor actor, GameMap map);
}
