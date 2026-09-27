package game.hatching;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.TestWorld;
import game.actors.Player;
import game.actors.creatures.GoldenBeetle;
import game.actors.creatures.OmenSheep;
import game.grounds.Blight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EggTest {

    private GameMap map;
    private Location nest;

    @BeforeEach
    void setUp() {
        map = TestWorld.soilMap();
        nest = map.at(2, 2);
    }

    private void passTurns(int turns) {
        for (int turn = 0; turn < turns; turn++) {
            nest.tick();
        }
    }

    @Test
    void omenSheepEggHatchesAfterThreeTurnsOnTheGround() {
        OmenSheepEgg egg = new OmenSheepEgg();
        nest.addItem(egg);

        passTurns(3);
        assertTrue(nest.getItems().contains(egg), "not hatched after 3 turns");

        passTurns(1);
        assertFalse(nest.getItems().contains(egg));
        assertEquals(1, TestWorld.count(map, OmenSheep.class));
    }

    @Test
    void carryingAnOmenSheepEggResetsItsTimer() {
        OmenSheepEgg egg = new OmenSheepEgg();
        nest.addItem(egg);
        passTurns(3);

        egg.tick(nest, new Player("Farmer", '@', 100));
        passTurns(3);

        assertTrue(nest.getItems().contains(egg), "the timer started again");
    }

    @Test
    void goldenBeetleEggOnlyHatchesNextToCursedGround() {
        GoldenBeetleEgg egg = new GoldenBeetleEgg();
        nest.addItem(egg);

        passTurns(5);
        assertEquals(0, TestWorld.count(map, GoldenBeetle.class), "no curse nearby");

        map.at(2, 3).setGround(new Blight());
        passTurns(1);
        assertEquals(1, TestWorld.count(map, GoldenBeetle.class));
        assertFalse(nest.getItems().contains(egg));
    }

    @Test
    void eatingEggsGivesTheirRewards() {
        Player farmer = new Player("Farmer", '@', 100);
        OmenSheepEgg sheepEgg = new OmenSheepEgg();
        GoldenBeetleEgg beetleEgg = new GoldenBeetleEgg();
        farmer.addItemToInventory(sheepEgg);
        farmer.addItemToInventory(beetleEgg);
        farmer.modifyAttribute(BaseActorAttributes.STAMINA, ActorAttributeOperations.DECREASE, 50);

        sheepEgg.eatenBy(farmer, map);
        beetleEgg.eatenBy(farmer, map);

        assertEquals(110, farmer.getAttributeMaximum(BaseActorAttributes.HEALTH));
        assertEquals(170, farmer.getAttribute(BaseActorAttributes.STAMINA));
        assertFalse(farmer.getItemInventory().contains(sheepEgg));
        assertFalse(farmer.getItemInventory().contains(beetleEgg));
    }
}
