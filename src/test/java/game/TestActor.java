package game;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * A plain actor for tests: no items, no runes, no capabilities, and it never does anything.
 */
public class TestActor extends Actor {

    /**
     * Creates a test actor.
     *
     * @param hitPoints the actor's starting hit points
     */
    public TestActor(int hitPoints) {
        super("Test Actor", 'a', hitPoints);
    }

    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        return new DoNothingAction();
    }
}
