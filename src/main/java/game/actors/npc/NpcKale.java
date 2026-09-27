package game.actors.npc;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.buying.MerchantOffer;
import game.capabilities.GeneralCapability;
import game.conditions.Condition;
import game.conditions.DefaultCondition;
import game.conditions.EmptyInventoryCondition;
import game.conditions.LowRunesCondition;
import game.conditions.NearbyCapabilityCondition;
import game.effects.IncreaseMaxStaminaEffect;
import game.effects.RestoreStaminaEffect;
import game.weapons.Broadsword;
import game.weapons.DragonslayerGreatsword;
import java.util.ArrayList;
import java.util.List;

/**
 * A representation of the "Kale" character in the game. This NPC is a merchant who offers
 * {@link Broadsword} and {@link DragonslayerGreatsword} for sale. Kale has unique monologues
 * triggered by specific conditions related to the listening actor or the environment, such as the
 * listener having low runes ({@link LowRunesCondition}), an empty inventory
 * ({@link EmptyInventoryCondition}), or if Kale is near a cursed entity
 * ({@link NearbyCapabilityCondition} with {@link GeneralCapability#CURSED}). He also has a default
 * monologue.
 */
public class NpcKale extends Npc {

    /**
     * Character representing Kale on the game map.
     */
    private static final char DISPLAY_CHAR = 'k';

    /**
     * Initial hit points (health) of Kale.
     */
    private static final int HIT_POINTS = 200;

    /**
     * The name of this NPC.
     */
    private static final String NAME = "Kale";

    /**
     * Constructor for the NpcKale class.
     * <p>
     * This constructor sets up the Kale NPC with its name, display character, and initial health.
     * It also assigns him the {@link GeneralCapability#CAN_SELL} capability and defines his
     * merchant offers:
     * <ul>
     * <li>A {@link Broadsword} for 150 runes, which also applies an
     * {@link IncreaseMaxStaminaEffect} of 30.</li>
     * <li>A {@link DragonslayerGreatsword} for 1700 runes, which also applies a
     * {@link RestoreStaminaEffect} of 20.</li>
     * </ul>
     * His monologues are set up to respond to various game conditions.
     */
    public NpcKale() {
        super(NpcKale.NAME, NpcKale.DISPLAY_CHAR, NpcKale.HIT_POINTS);
        this.addCapability(GeneralCapability.CAN_SELL); // Kale can sell items

        // Buying a Broadsword from Kale also increases max stamina
        offers.add(new MerchantOffer(Broadsword::new, 150,
                List.of(new IncreaseMaxStaminaEffect(30))));

        // Buying a Dragonslayer Greatsword from Kale also restores some stamina
        offers.add(new MerchantOffer(DragonslayerGreatsword::new, 1700,
                List.of(new RestoreStaminaEffect(20))));
    }

    /**
     * Returns a list of {@link Monologue}s that Kale can say, based on various conditions.
     * <ul>
     * <li>If the {@code listener} has low runes (checked by {@link LowRunesCondition}),
     * Kale says: "Ah, hard times, I see. Keep your head low and your blade sharp."</li>
     * <li>If the {@code listener} has an empty inventory (checked by
     * {@link EmptyInventoryCondition}), Kale says: "Not a scrap to your name? Even a farmer should
     * carry a trinket or two."</li>
     * <li>If Kale is near a cursed entity (checked by {@link NearbyCapabilityCondition} for
     * {@link GeneralCapability#CURSED}), Kale says: "Rest by the flame when you can, friend.
     * These lands will wear you thin."</li>
     * <li>At any time ({@link DefaultCondition}), Kale may say: "A merchant's life is a lonely
     * one. But the roads... they whisper secrets to those who listen."</li>
     * </ul>
     *
     * @param listener The {@link Actor} who is listening to Kale.
     * @param map      The current {@link GameMap}.
     * @return A list of {@link Monologue} objects appropriate for the current situation.
     */
    @Override
    public List<Monologue> getMonologues(Actor listener, GameMap map) {
        // Define conditions for triggering specific monologues
        Condition defaultCondition = new DefaultCondition();
        Condition lowRunesCondition = new LowRunesCondition(listener);
        Condition emptyInventoryCondition = new EmptyInventoryCondition(listener);
        Condition nearbyCursedCondition = new NearbyCapabilityCondition(map.locationOf(this),
                GeneralCapability.CURSED);

        List<Monologue> monologues = new ArrayList<>();
        monologues.add(new Monologue(lowRunesCondition,
                "Ah, hard times, I see. Keep your head low and your blade sharp."));
        monologues.add(new Monologue(emptyInventoryCondition,
                "Not a scrap to your name? Even a farmer should carry a trinket or two."));
        monologues.add(new Monologue(nearbyCursedCondition,
                "Rest by the flame when you can, friend. These lands will wear you thin."));
        monologues.add(new Monologue(defaultCondition,
                "A merchant's life is a lonely one. But the roads... they whisper secrets to "
                        + "those who listen."));

        return monologues;
    }
}
