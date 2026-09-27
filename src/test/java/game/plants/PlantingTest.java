package game.plants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.TestActor;
import game.TestWorld;
import game.actors.Player;
import game.grounds.Soil;
import game.plants.actions.PlantAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlantingTest {

    private GameMap map;
    private Player farmer;

    @BeforeEach
    void setUp() {
        map = TestWorld.map("xxx", "x.x", "xxx");
        farmer = new Player("Farmer", '@', 100);
        map.addActor(farmer, map.at(1, 1));
    }

    @Test
    void inheritreePurifiesTheBlightAroundIt() {
        Location field = map.at(1, 1);

        new PlantAction(new InheritreeSeed(), field).execute(farmer, map);

        assertInstanceOf(Inheritree.class, field.getGround());
        for (Exit exit : field.getExits()) {
            assertInstanceOf(Soil.class, exit.getDestination().getGround());
        }
        assertEquals(175, farmer.getAttribute(BaseActorAttributes.STAMINA), "costs 25 stamina");
    }

    @Test
    void inheritreeHealsAndRestsItsNeighboursEveryTurn() {
        map.at(0, 0).setGround(new Inheritree());
        farmer.hurt(20);
        farmer.modifyAttribute(BaseActorAttributes.STAMINA, ActorAttributeOperations.DECREASE, 30);

        map.at(0, 0).tick();

        assertEquals(85, farmer.getAttribute(BaseActorAttributes.HEALTH));
        assertEquals(175, farmer.getAttribute(BaseActorAttributes.STAMINA));
    }

    @Test
    void bloodroseDrainsItsPlanter() {
        new PlantAction(new BloodroseSeed(), map.at(1, 1)).execute(farmer, map);

        assertEquals(95, farmer.getAttribute(BaseActorAttributes.HEALTH));
        assertEquals(200 - 75 - 5, farmer.getAttribute(BaseActorAttributes.STAMINA));
    }

    @Test
    void bloodroseHurtsItsNeighboursEveryTurn() {
        map.at(0, 0).setGround(new Bloodrose());
        TestActor neighbour = new TestActor(100);
        map.addActor(neighbour, map.at(0, 1));

        map.at(0, 0).tick();

        assertEquals(90, neighbour.getAttribute(BaseActorAttributes.HEALTH));
    }

    @Test
    void plantingNeedsEnoughStamina() {
        InheritreeSeed seed = new InheritreeSeed();
        farmer.addItemToInventory(seed);
        farmer.modifyAttribute(BaseActorAttributes.STAMINA, ActorAttributeOperations.DECREASE, 180);

        String result = new PlantAction(seed, map.at(1, 1)).execute(farmer, map);

        assertTrue(result.contains("does not have enough stamina"), result);
        assertInstanceOf(Soil.class, map.at(1, 1).getGround());
        assertTrue(farmer.getItemInventory().contains(seed), "the seed is not used up");
    }
}
