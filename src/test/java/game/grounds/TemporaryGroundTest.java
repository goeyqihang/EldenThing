package game.grounds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.TestActor;
import game.TestWorld;
import game.capabilities.GeneralCapability;
import org.junit.jupiter.api.Test;

class TemporaryGroundTest {

    @Test
    void restoresTheOriginalGroundWhenTimeIsUp() {
        GameMap map = TestWorld.soilMap();
        Location location = map.at(2, 2);
        Soil original = new Soil();
        location.setGround(new TemporaryGround(original, new BurningGround(), 3));

        location.tick();
        location.tick();
        assertInstanceOf(TemporaryGround.class, location.getGround(), "still burning after 2 turns");

        location.tick();
        assertSame(original, location.getGround());
    }

    @Test
    void burningGroundHurtsWhoeverStandsOnItEveryTurn() {
        GameMap map = TestWorld.soilMap();
        Location location = map.at(2, 2);
        TestActor actor = new TestActor(100);
        map.addActor(actor, location);
        location.setGround(new TemporaryGround(new Soil(), new BurningGround(), 3));

        for (int turn = 0; turn < 3; turn++) {
            location.tick();
        }

        assertEquals(85, actor.getAttribute(BaseActorAttributes.HEALTH));
    }

    @Test
    void looksAndBehavesLikeTheGroundItWraps() {
        TemporaryGround wall = new TemporaryGround(new Soil(), new Wall(), 3);
        assertEquals('#', wall.getDisplayChar());
        assertFalse(wall.canActorEnter(new TestActor(100)));

        TemporaryGround burningBlight = new TemporaryGround(new Blight(), new BurningGround(), 3);
        assertEquals('^', burningBlight.getDisplayChar());
        assertTrue(burningBlight.canActorEnter(new TestActor(100)));
        assertFalse(burningBlight.hasCapability(GeneralCapability.CURSED));
        assertFalse(burningBlight.hasCapability(GroundCapability.BURNABLE),
                "burning ground cannot be set on fire again");
    }
}
