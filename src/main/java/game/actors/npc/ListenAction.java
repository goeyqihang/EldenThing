package game.actors.npc;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * An {@link Action} that allows an {@link Actor} to listen to a {@link Speakable} (such as an
 * {@link Npc}) and possibly hear a monologue if any condition is satisfied.
 *
 * <p>The {@code ListenAction} retrieves the target's {@link Monologue}s by calling
 * {@link Speakable#getMonologues(Actor, GameMap)}, which allows the NPC to dynamically determine
 * which monologues are relevant based on the listener and the current game state. From the
 * available monologues (those whose conditions are met), one is chosen at random to be
 * displayed.</p>
 */
public class ListenAction extends Action {

    /**
     * Random number generator used to pick one of the available monologues.
     */
    private static final Random random = new Random();

    /**
     * The speaker (typically an NPC) this action listens to.
     */
    private final Speakable target;

    /**
     * Constructs a new {@code ListenAction} targeting the given NPC.
     *
     * @param target the NPC to listen to
     */
    public ListenAction(Speakable target) {
        this.target = target;
    }

    /**
     * Executes the listen action. It retrieves the relevant {@link Monologue}s from the target by
     * calling {@link Speakable#getMonologues(Actor, GameMap)}. It then filters these to
     * find monologues whose {@link Monologue#availability()} (condition) is true. If any such
     * monologues are available, one is chosen at random from the list of available monologues, and
     * its message is returned. If no monologues are available (either none were returned by the
     * NPC, or none of their conditions were met), a default message "It says nothing..." is
     * returned.
     *
     * @param actor the actor performing the action (the listener)
     * @param map   the game map the actor is on
     * @return the monologue message if any are available, otherwise a fallback message
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        List<Monologue> monologues = target.getMonologues(actor, map);
        List<Monologue> availableMonologues = new ArrayList<>();
        for (Monologue monologue : monologues) {
            if (monologue.availability()) { // Check condition
                availableMonologues.add(monologue);
            }
        }
        if (!availableMonologues.isEmpty()) {
            int randomIndex = random.nextInt(availableMonologues.size());
            return availableMonologues.get(randomIndex).getMessage();
        }
        return "It says nothing..."; // Default if no suitable monologue
    }

    /**
     * Returns a string description of this action, suitable for display in a menu.
     *
     * @param actor the actor performing the action
     * @return a string describing the listen action, e.g., "Player listens to Kale"
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " listens to " + target;
    }
}
