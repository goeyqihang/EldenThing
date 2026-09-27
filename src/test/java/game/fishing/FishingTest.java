package game.fishing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.TestWorld;
import game.actors.Player;
import game.effects.ContinuousDamageEffect;
import game.grounds.Blight;
import game.grounds.Soil;
import game.plants.Bloodrose;
import game.plants.Inheritree;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FishingTest {

    private GameMap map;
    private Player farmer;

    @BeforeEach
    void setUp() {
        map = TestWorld.soilMap();
        farmer = new Player("Farmer", '@', 100);
        map.addActor(farmer, map.at(2, 2));
    }

    @Test
    void pondHoldsFiveKindsOfCatchWithTheirOwnChances() {
        List<Fishable> catches = new Pond().getFishableItems();

        assertEquals(5, catches.size());
        assertEquals(0.3, chanceOf(catches, SalmonFish.class));
        assertEquals(0.5, chanceOf(catches, ToxicEel.class));
        assertEquals(0.6, chanceOf(catches, Shovel.class));
        assertEquals(0.05, chanceOf(catches, GoldenFish.class));
        assertEquals(0.4, chanceOf(catches, OldBoot.class));
    }

    private static double chanceOf(List<Fishable> catches, Class<?> type) {
        return catches.stream().filter(type::isInstance).findFirst().orElseThrow().getCatchChance();
    }

    @Test
    void everyFishingAttemptGetsFreshItems() {
        Pond pond = new Pond();
        assertNotSame(pond.getFishableItems().get(0), pond.getFishableItems().get(0));
    }

    @Test
    void actorsCannotWalkIntoThePond() {
        assertFalse(new Pond().canActorEnter(farmer));
    }

    @Test
    void fishingCatchesAtMostOneItemPerAttempt() {
        Pond pond = new Pond();
        Location pondLocation = map.at(2, 1);
        pondLocation.setGround(pond);
        Item rod = new FishingRod();
        int caughtInTotal = 0;

        for (int attempt = 0; attempt < 50; attempt++) {
            int before = farmer.getItemInventory().size();
            new FishAction(rod, pond, pondLocation).execute(farmer, map);
            int caught = farmer.getItemInventory().size() - before;
            assertTrue(caught == 0 || caught == 1, "caught " + caught + " items at once");
            caughtInTotal += caught;
        }
        assertTrue(caughtInTotal > 0, "50 attempts without a single catch");
    }

    @Test
    void goldenFishIsWorthRunesAndHealsWhenEaten() {
        GoldenFish fish = new GoldenFish();
        fish.catchBy(farmer);
        assertEquals(10000 + 1000, farmer.getBalance());

        farmer.hurt(60);
        fish.eatenBy(farmer, map);
        assertEquals(90, farmer.getAttribute(BaseActorAttributes.HEALTH));
        assertFalse(farmer.getItemInventory().contains(fish));
    }

    @Test
    void toxicEelShocksWhoeverCatchesIt() {
        new ToxicEel().catchBy(farmer);

        assertTrue(farmer.getStatusEffects().stream()
                .anyMatch(ContinuousDamageEffect.class::isInstance));
    }

    @Test
    void eatingAToxicEelCanKnockYouOut() {
        ToxicEel eel = new ToxicEel();
        farmer.addItemToInventory(eel);
        farmer.hurt(90);

        eel.eatenBy(farmer, map);

        assertFalse(map.contains(farmer));
    }

    @Test
    void shovelDigsUpPlants() {
        Location bloodrose = map.at(1, 1);
        bloodrose.setGround(new Bloodrose());
        Location inheritree = map.at(3, 3);
        inheritree.setGround(new Inheritree());

        digUp(bloodrose);
        digUp(inheritree);

        assertInstanceOf(Blight.class, bloodrose.getGround(), "a Bloodrose leaves Blight behind");
        assertInstanceOf(Soil.class, inheritree.getGround(), "an Inheritree leaves Soil behind");
    }

    private void digUp(Location location) {
        for (Action action : location.getGround().allowableActions(farmer, location, "")) {
            if (action instanceof DigAction) {
                action.execute(farmer, map);
                return;
            }
        }
        throw new AssertionError("no dig action offered for " + location.getGround());
    }
}
