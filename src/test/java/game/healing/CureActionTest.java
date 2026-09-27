package game.healing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.TestWorld;
import game.actors.Player;
import game.grounds.Blight;
import game.grounds.Soil;
import game.healing.items.Talisman;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CureActionTest {

    private GameMap map;
    private Player farmer;
    private Location blight;

    @BeforeEach
    void setUp() {
        map = TestWorld.map("x..", "...");
        farmer = new Player("Farmer", '@', 100);
        map.addActor(farmer, map.at(1, 1));
        farmer.addItemToInventory(new Talisman());
        blight = map.at(0, 0);
    }

    private Action cureAction() {
        for (Action action : blight.getGround().allowableActions(farmer, blight, "North-West")) {
            if (action instanceof CureAction) {
                return action;
            }
        }
        throw new AssertionError("a Talisman should offer a cure for Blight");
    }

    @Test
    void curingBlightTurnsItIntoSoilForFiftyStamina() {
        cureAction().execute(farmer, map);

        assertInstanceOf(Soil.class, blight.getGround());
        assertEquals(150, farmer.getAttribute(BaseActorAttributes.STAMINA));
    }

    @Test
    void curingNeedsEnoughStamina() {
        farmer.modifyAttribute(BaseActorAttributes.STAMINA, ActorAttributeOperations.DECREASE, 160);

        String result = cureAction().execute(farmer, map);

        assertTrue(result.contains("does not have enough stamina"), result);
        assertInstanceOf(Blight.class, blight.getGround());
        assertEquals(40, farmer.getAttribute(BaseActorAttributes.STAMINA));
    }
}
