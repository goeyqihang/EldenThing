package game.growingparts;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A behaviour that allows a {@link Growable} actor to decide to grow.
 * This behaviour will trigger a {@link GrowPartAction} only if there are no other actors
 * in adjacent locations, preventing the actor from growing while in direct combat.
 */
public class GrowPartBehaviour implements Behaviour {

    /**
     * The growable entity that this behaviour is attached to.
     */
    private final Growable grower;

    /**
     * Constructor for GrowPartBehaviour.
     *
     * @param grower The {@link Growable} entity that this behaviour will control.
     */
    public GrowPartBehaviour(Growable grower) {
        this.grower = grower;
    }

    /**
     * Determines the action to be taken by the actor.
     * If there are no other actors in adjacent tiles, it returns a {@link GrowPartAction}.
     * Otherwise, it returns null, allowing other behaviours to take precedence.
     *
     * @param actor The actor performing the behaviour.
     * @param map   The map the actor is on.
     * @return A {@link GrowPartAction} if conditions are met, otherwise null.
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {
        Location currentLocation = map.locationOf(actor);

        for (Exit exit : currentLocation.getExits()) {
            Location destination = exit.getDestination();

            if (destination.containsAnActor()) {
                return null;
            }
        }
        // No actors nearby, so it is safe to grow
        return new GrowPartAction(grower);
    }
}
