package game.actors.creatures;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.TestWorld;
import game.actors.Player;
import game.hatching.OmenSheepEgg;
import game.healing.items.Talisman;
import game.plants.Inheritree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OmenSheepTest {

    private GameMap map;
    private OmenSheep sheep;

    @BeforeEach
    void setUp() {
        map = TestWorld.soilMap();
        sheep = new OmenSheep();
        map.addActor(sheep, map.at(2, 2));
    }

    private Action playTurn() {
        return sheep.playTurn(new ActionList(), new DoNothingAction(), map, new Display());
    }

    @Test
    void laysAnEggEverySevenTurns() {
        for (int turn = 1; turn < 7; turn++) {
            assertFalse(playTurn() instanceof ProduceAction, "no egg on turn " + turn);
        }

        Action seventhTurn = playTurn();
        assertInstanceOf(ProduceAction.class, seventhTurn);
        seventhTurn.execute(sheep, map);

        assertTrue(map.at(2, 2).getItems().stream().anyMatch(OmenSheepEgg.class::isInstance));
    }

    @Test
    void collapsesFromCrimsonRotAfterFifteenTurns() {
        for (int turn = 1; turn < OmenSheep.INITIAL_ROT_COUNTDOWN; turn++) {
            playTurn();
        }
        assertTrue(map.contains(sheep));

        playTurn();
        assertFalse(map.contains(sheep));
    }

    @Test
    void curingItMakesInheritreesSproutAllAround() {
        Location here = map.locationOf(sheep);

        sheep.cure(new Player("Farmer", '@', 100), here, new Talisman());

        for (Exit exit : here.getExits()) {
            assertInstanceOf(Inheritree.class, exit.getDestination().getGround());
        }
    }
}
