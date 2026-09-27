package game.conditions;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.monash.fit2099.engine.positions.GameMap;
import game.TestActor;
import game.TestWorld;
import game.actors.Player;
import game.capabilities.GeneralCapability;
import game.grounds.Blight;
import game.healing.items.Talisman;
import org.junit.jupiter.api.Test;

class ConditionTest {

    @Test
    void defaultConditionAlwaysHolds() {
        assertTrue(new DefaultCondition().check());
    }

    @Test
    void lowHealthMeansBelowFifty() {
        TestActor actor = new TestActor(100);
        actor.hurt(50);
        assertFalse(new LowHealthCondition(actor).check(), "50 HP is not low");

        actor.hurt(1);
        assertTrue(new LowHealthCondition(actor).check(), "49 HP is low");
    }

    @Test
    void targetHealthMustBeStrictlyAboveTheThreshold() {
        TestActor actor = new TestActor(51);
        assertTrue(new DynamicTargetHealthCondition(actor, 50).check());

        actor.hurt(1);
        assertFalse(new DynamicTargetHealthCondition(actor, 50).check());
    }

    @Test
    void lowRunesMeansBelowFiveHundred() {
        TestActor actor = new TestActor(100);
        actor.addBalance(500);
        assertFalse(new LowRunesCondition(actor).check());

        actor.deductBalance(1);
        assertTrue(new LowRunesCondition(actor).check());
    }

    @Test
    void emptyInventoryCondition() {
        TestActor actor = new TestActor(100);
        assertTrue(new EmptyInventoryCondition(actor).check());

        actor.addItemToInventory(new Talisman());
        assertFalse(new EmptyInventoryCondition(actor).check());
    }

    @Test
    void turnBasedConditionHoldsOnceTheTurnHasPassed() {
        assertFalse(new TurnBasedCondition(3, 3).check());
        assertTrue(new TurnBasedCondition(4, 3).check());
    }

    @Test
    void nearbyCapabilityChecksGroundActorsAndItemsAround() {
        GameMap map = TestWorld.map(".....", ".....", ".....");
        NearbyCapabilityCondition nearCursed =
                new NearbyCapabilityCondition(map.at(2, 1), GeneralCapability.CURSED);
        assertFalse(nearCursed.check());

        map.at(3, 2).setGround(new Blight());
        assertTrue(nearCursed.check(), "adjacent cursed ground");

        map.addActor(new Player("Farmer", '@', 100), map.at(1, 0));
        assertTrue(new NearbyCapabilityCondition(map.at(2, 1), GeneralCapability.FOLLOWABLE)
                .check(), "adjacent actor");

        map.at(4, 2).addItem(new Talisman());
        assertTrue(new NearbyCapabilityCondition(map.at(3, 1), GeneralCapability.CAN_CURE)
                .check(), "item on an adjacent tile");
        assertFalse(new NearbyCapabilityCondition(map.at(1, 1), GeneralCapability.CAN_CURE)
                .check(), "item two tiles away");
    }
}
