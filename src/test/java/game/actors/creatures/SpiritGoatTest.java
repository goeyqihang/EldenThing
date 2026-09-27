package game.actors.creatures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actions.MoveActorAction;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import game.TestWorld;
import game.actors.Player;
import game.healing.items.Talisman;
import game.plants.Inheritree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpiritGoatTest {

    private GameMap map;
    private SpiritGoat goat;

    @BeforeEach
    void setUp() {
        map = TestWorld.soilMap();
        goat = new SpiritGoat();
        map.addActor(goat, map.at(2, 2));
    }

    private Action playTurn() {
        return goat.playTurn(new ActionList(), new DoNothingAction(), map, new Display());
    }

    @Test
    void wandersOnItsFirstTurn() {
        assertInstanceOf(MoveActorAction.class, playTurn());
    }

    @Test
    void collapsesFromCrimsonRotAfterTenTurns() {
        for (int turn = 1; turn < SpiritGoat.INITIAL_ROT_COUNTDOWN; turn++) {
            playTurn();
        }
        assertTrue(map.contains(goat), "still alive after 9 turns");

        playTurn();
        assertFalse(map.contains(goat), "collapsed after 10 turns");
    }

    @Test
    void curingResetsTheRotTimer() {
        for (int turn = 0; turn < 6; turn++) {
            playTurn();
        }
        assertEquals(4, goat.getCurrentRotCountdown());

        goat.cure(new Player("Farmer", '@', 100), map.locationOf(goat), new Talisman());

        assertEquals(SpiritGoat.INITIAL_ROT_COUNTDOWN, goat.getCurrentRotCountdown());
    }

    @Test
    void reproducesNextToABlessedInheritree() {
        map.at(3, 2).setGround(new Inheritree());

        Action action = playTurn();
        assertInstanceOf(ProduceAction.class, action);
        action.execute(goat, map);

        assertEquals(2, TestWorld.count(map, SpiritGoat.class));
    }
}
