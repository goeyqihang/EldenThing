package game.actors.npc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.positions.GameMap;
import game.TestActor;
import game.TestWorld;
import game.actors.Player;
import game.behaviours.AttackBehaviour;
import game.buying.PurchaseAction;
import game.weapons.actions.AttackAction;
import java.util.List;
import org.junit.jupiter.api.Test;

class NpcTest {

    private static long available(List<Monologue> monologues) {
        return monologues.stream().filter(Monologue::availability).count();
    }

    @Test
    void gutsOnlyAttacksTargetsWithMoreThanFiftyHealth() {
        GameMap map = TestWorld.soilMap();
        NpcGuts guts = new NpcGuts();
        map.addActor(guts, map.at(2, 2));
        TestActor target = new TestActor(100);
        map.addActor(target, map.at(3, 2));
        AttackBehaviour attackBehaviour = new AttackBehaviour(guts);

        assertInstanceOf(AttackAction.class, attackBehaviour.getAction(guts, map));

        target.hurt(50);
        assertNull(attackBehaviour.getAction(guts, map), "leaves a target on 50 HP alone");
    }

    @Test
    void kaleOnlySaysHisDefaultLineToAWealthyListener() {
        GameMap map = TestWorld.soilMap();
        NpcKale kale = new NpcKale();
        map.addActor(kale, map.at(2, 2));

        List<Monologue> monologues = kale.getMonologues(new Player("Farmer", '@', 100), map);

        assertEquals(1, available(monologues));
    }

    @Test
    void kaleReactsToLowRunesAnEmptyInventoryAndNearbyBlight() {
        GameMap map = TestWorld.map(".....", "..x..", ".....");
        NpcKale kale = new NpcKale();
        map.addActor(kale, map.at(2, 2));

        List<Monologue> monologues = kale.getMonologues(new TestActor(100), map);

        assertEquals(4, available(monologues));
    }

    @Test
    void listeningReturnsOneOfTheAvailableLines() {
        GameMap map = TestWorld.soilMap();
        NpcSellen sellen = new NpcSellen();
        map.addActor(sellen, map.at(2, 2));
        TestActor listener = new TestActor(100);

        String heard = new ListenAction(sellen).execute(listener, map);

        assertTrue(sellen.getMonologues(listener, map).stream()
                .anyMatch(monologue -> monologue.getMessage().equals(heard)));
    }

    @Test
    void merchantsOfferTheirWeaponsAndCanBeListenedTo() {
        GameMap map = TestWorld.soilMap();
        NpcKale kale = new NpcKale();
        map.addActor(kale, map.at(2, 2));
        Player farmer = new Player("Farmer", '@', 100);
        map.addActor(farmer, map.at(3, 2));

        ActionList actions = kale.allowableActions(farmer, "West", map);

        int purchases = 0;
        boolean canListen = false;
        for (Action action : actions) {
            purchases += action instanceof PurchaseAction ? 1 : 0;
            canListen |= action instanceof ListenAction;
        }
        assertEquals(2, purchases);
        assertTrue(canListen);
    }

    @Test
    void creaturesCannotBuyOrListen() {
        GameMap map = TestWorld.soilMap();
        NpcKale kale = new NpcKale();
        map.addActor(kale, map.at(2, 2));

        ActionList actions = kale.allowableActions(new TestActor(100), "West", map);

        assertEquals(0, actions.size());
    }
}
