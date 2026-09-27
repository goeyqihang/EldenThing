package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import java.util.ArrayList;
import java.util.Random;

/**
 * A behaviour that allows an Actor to wander randomly around the map. It selects a random valid
 * exit from the actor's current location and returns a MoveActorAction to move there.
 * <p>
 * Created by the FIT2099 Teaching Team.
 *
 * @author Riordan D. Alfredo
 * Modified by: Goey Qi Hang
 */
public class WanderBehaviour implements Behaviour {

    /**
     * Random number generator for selecting exits.
     */
    private final Random random = new Random();

    /**
     * Returns a MoveAction to wander to a random adjacent location, if possible. It checks all
     * exits from the actor's current location. If an exit leads to a location the actor can enter,
     * a corresponding MoveActorAction is added to a list. If the list of possible move actions is
     * not empty, one is chosen randomly and returned. If no movement is possible (e.g., actor is
     * surrounded by walls or other actors), returns null.
     *
     * @param actor the Actor enacting the behaviour
     * @param map   the map that actor is currently on
     * @return a MoveActorAction to a random valid destination, or null if no valid move is
     * possible.
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {
        ArrayList<Action> actions = new ArrayList<>();

        // Check all exits from the current location
        for (Exit exit : map.locationOf(actor).getExits()) {
            Location destination = exit.getDestination();
            // Check if the actor can enter the destination
            if (destination.canActorEnter(actor)) {
                // Add a move action for this exit
                actions.add(destination.getMoveAction(actor, "around", exit.getHotKey()));
            }
        }

        // If there are possible move actions, choose one randomly
        if (!actions.isEmpty()) {
            return actions.get(random.nextInt(actions.size()));
        }
        // Otherwise, no action can be taken
        return null;
    }
}
