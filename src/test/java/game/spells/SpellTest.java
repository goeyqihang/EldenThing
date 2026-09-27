package game.spells;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.monash.fit2099.engine.actors.StatusEffect;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.TestActor;
import game.TestWorld;
import game.actors.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpellTest {

    private GameMap map;
    private Player farmer;

    @BeforeEach
    void setUp() {
        map = TestWorld.soilMap();
        farmer = new Player("Farmer", '@', 100);
        map.addActor(farmer, map.at(2, 2));
    }

    private int mana() {
        return farmer.getAttribute(BaseActorAttributes.MANA);
    }

    @Test
    void healSpellHealsTheCasterForMana() {
        farmer.hurt(40);

        new CastSpellAction(new HealSpell(), farmer).execute(farmer, map);

        assertEquals(85, farmer.getAttribute(BaseActorAttributes.HEALTH));
        assertEquals(200 - 30, mana());
    }

    @Test
    void spellsNeedEnoughMana() {
        farmer.hurt(40);
        farmer.modifyAttribute(BaseActorAttributes.MANA, ActorAttributeOperations.DECREASE, 190);

        String result = new CastSpellAction(new HealSpell(), farmer).execute(farmer, map);

        assertTrue(result.contains("does not have enough mana"), result);
        assertEquals(60, farmer.getAttribute(BaseActorAttributes.HEALTH));
        assertEquals(10, mana());
    }

    @Test
    void poisonDealsDamageOverThreeTurns() {
        TestActor target = new TestActor(200);
        map.addActor(target, map.at(3, 2));

        new CastSpellAction(new PoisonSpell(), target).execute(farmer, map);
        StatusEffect poison = target.getStatusEffects().get(0);
        for (int turn = 0; turn < 3; turn++) {
            poison.tick(map.locationOf(target), target);
        }

        assertEquals(200 - 3 * 25, target.getAttribute(BaseActorAttributes.HEALTH));
        assertTrue(target.getStatusEffects().isEmpty(), "the poison wears off");
        assertEquals(200 - 40, mana());
    }

    @Test
    void fireSpellBurnsEveryTileAroundTheCaster() {
        TestActor target = new TestActor(100);
        map.addActor(target, map.at(3, 2));

        new CastSpellAction(new FireSpell(), null).execute(farmer, map);

        assertEquals(90, target.getAttribute(BaseActorAttributes.HEALTH));
        for (Exit exit : map.locationOf(farmer).getExits()) {
            assertEquals('^', exit.getDestination().getGround().getDisplayChar());
        }
        assertEquals(200 - 25, mana());
    }

    @Test
    void teleportSpellMovesTheCasterSomewhereElseOnTheMap() {
        Location start = map.locationOf(farmer);

        new CastSpellAction(new TeleportSpell(), farmer).execute(farmer, map);

        Location destination = map.locationOf(farmer);
        assertNotEquals(start, destination);
        assertSame(map, destination.map());
        assertEquals(200 - 20, mana());
    }
}
