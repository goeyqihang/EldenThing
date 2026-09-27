package game.buying;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.TestActor;
import game.TestWorld;
import game.actors.Player;
import game.actors.creatures.OmenSheep;
import game.actors.npc.Npc;
import game.actors.npc.NpcKale;
import game.actors.npc.NpcSellen;
import game.weapons.Broadsword;
import game.weapons.Katana;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PurchaseActionTest {

    private GameMap map;
    private Player farmer;

    @BeforeEach
    void setUp() {
        map = TestWorld.soilMap();
        farmer = new Player("Farmer", '@', 100);
        map.addActor(farmer, map.at(3, 2));
    }

    private Action offerFor(Npc merchant, String itemName) {
        for (Action action : merchant.allowableActions(farmer, "West", map)) {
            if (action instanceof PurchaseAction && action.menuDescription(farmer).contains(itemName)) {
                return action;
            }
        }
        throw new AssertionError(merchant + " does not sell " + itemName);
    }

    private List<Item> itemsOfType(Actor actor, Class<? extends Item> type) {
        return actor.getItemInventory().stream().filter(type::isInstance).toList();
    }

    @Test
    void buyingFromKaleCostsRunesAndRaisesMaxStamina() {
        NpcKale kale = new NpcKale();
        map.addActor(kale, map.at(2, 2));

        offerFor(kale, "Broadsword").execute(farmer, map);

        assertEquals(10000 - 150, farmer.getBalance());
        assertEquals(230, farmer.getAttributeMaximum(BaseActorAttributes.STAMINA));
        assertEquals(1, itemsOfType(farmer, Broadsword.class).size());
    }

    @Test
    void theSameOfferFromAnotherMerchantHasDifferentEffects() {
        NpcSellen sellen = new NpcSellen();
        map.addActor(sellen, map.at(2, 2));

        offerFor(sellen, "Broadsword").execute(farmer, map);

        assertEquals(10000 - 100, farmer.getBalance());
        assertEquals(120, farmer.getAttributeMaximum(BaseActorAttributes.HEALTH));
        assertEquals(200, farmer.getAttributeMaximum(BaseActorAttributes.STAMINA));
    }

    @Test
    void katanaEffectsStackInOrder() {
        NpcSellen sellen = new NpcSellen();
        map.addActor(sellen, map.at(2, 2));

        offerFor(sellen, "Katana").execute(farmer, map);

        // Katana hurts the buyer for 25, then Sellen's offer heals 10 and adds 20 max stamina
        assertEquals(85, farmer.getAttribute(BaseActorAttributes.HEALTH));
        assertEquals(220, farmer.getAttributeMaximum(BaseActorAttributes.STAMINA));
        assertEquals(1, itemsOfType(farmer, Katana.class).size());
        assertEquals(1, TestWorld.count(map, OmenSheep.class), "an Omen Sheep appears by Sellen");
    }

    @Test
    void everyPurchaseHandsOverANewItem() {
        NpcKale kale = new NpcKale();
        map.addActor(kale, map.at(2, 2));

        offerFor(kale, "Broadsword").execute(farmer, map);
        offerFor(kale, "Broadsword").execute(farmer, map);

        List<Item> swords = itemsOfType(farmer, Broadsword.class);
        assertEquals(2, swords.size());
        assertNotSame(swords.get(0), swords.get(1));
    }

    @Test
    void cannotBuyWithoutEnoughRunes() {
        TestActor broke = new TestActor(100);
        MerchantOffer offer = new MerchantOffer(Broadsword::new, 150, List.of());

        String result = new PurchaseAction(offer.createItem(), offer.getPrice(), new NpcKale(),
                offer.getEffects()).execute(broke, map);

        assertTrue(result.contains("does not have enough runes"), result);
        assertTrue(broke.getItemInventory().isEmpty());
        assertEquals(0, broke.getBalance());
    }
}
