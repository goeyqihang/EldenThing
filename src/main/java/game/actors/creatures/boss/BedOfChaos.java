package game.actors.creatures.boss;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.creatures.Creature;
import game.actors.creatures.boss.parts.BossPart;
import game.actors.creatures.boss.parts.Branch;
import game.actors.creatures.boss.parts.Leaf;
import game.behaviours.AttackBehaviour;
import game.behaviours.AttackConditionEvaluator;
import game.capabilities.GeneralCapability;
import game.growingparts.GrowPartBehaviour;
import game.growingparts.Growable;
import game.weapons.BedOfChaosClaw;
import game.weapons.actions.AttackAction;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Represents the Bed of Chaos, a boss creature in the game.
 * The Bed of Chaos is a complex creature that is composed of multiple parts, which it can grow over
 * time. Its power increases as it grows more parts. It implements {@link AttackConditionEvaluator}
 * to define its attack logic and {@link Growable} to handle its growth mechanics.
 */
public class BedOfChaos extends Creature implements AttackConditionEvaluator, Growable {

    /**
     * The name of the Bed of Chaos.
     */
    private static final String NAME = "Bed of Chaos";

    /**
     * The character representing the Bed of Chaos on the game map.
     */
    private static final char DISPLAY_CHAR = 'T';

    /**
     * The initial hit points of the Bed of Chaos.
     */
    private static final int HIT_POINTS = 1000;

    /**
     * The base damage dealt by the Bed of Chaos's intrinsic weapon.
     */
    private static final int BASE_DAMAGE = 25;

    /**
     * The hit rate of the Bed of Chaos's intrinsic weapon.
     */
    private static final int HIT_RATE = 75;

    /**
     * The priority of the attack behaviour.
     */
    private static final int PRIORITY_ATTACK = 1;

    /**
     * The priority of the grow behaviour.
     */
    private static final int PRIORITY_GROW = 5;

    /**
     * Random number generator for growth decisions.
     */
    private final Random random = new Random();

    /**
     * A list of the direct parts attached to the Bed of Chaos.
     */
    private final List<BossPart> directParts = new ArrayList<>();

    /**
     * The intrinsic weapon of the Bed of Chaos.
     */
    private final BedOfChaosClaw bossWeapon = new BedOfChaosClaw(BASE_DAMAGE, "strikes", HIT_RATE);

    /**
     * Constructor for the Bed of Chaos.
     * Initializes the boss with its name, display character, and health, and sets its intrinsic
     * weapon.
     */
    public BedOfChaos() {
        super(NAME, DISPLAY_CHAR, HIT_POINTS);
        this.setIntrinsicWeapon(bossWeapon);
    }

    /**
     * Initializes the behaviours for the Bed of Chaos.
     * It includes an {@link AttackBehaviour} and a {@link GrowPartBehaviour}.
     */
    @Override
    protected void initializeBehaviours() {
        this.addBehaviour(PRIORITY_ATTACK, new AttackBehaviour(this));
        this.addBehaviour(PRIORITY_GROW, new GrowPartBehaviour(this));
    }

    /**
     * Evaluates whether the Bed of Chaos should attack a potential target.
     * Before attacking, it calculates the total damage contribution from all its parts and updates
     * its weapon's damage. It always decides to attack if a target is present.
     *
     * @param attacker        The actor performing the attack (this instance).
     * @param potentialTarget The actor being considered as a target.
     * @param map             The current game map.
     * @return always true, indicating it will attack if a target is available.
     */
    @Override
    public boolean evaluate(Actor attacker, Actor potentialTarget, GameMap map) {
        int totalAdditionalDamage = this.getDamageContribution();
        bossWeapon.setDamage(BASE_DAMAGE + totalAdditionalDamage);
        return true;
    }

    /**
     * Attempts to grow a new part.
     * The Bed of Chaos will randomly grow either a Branch or a Leaf. It then triggers the growth
     * process for all its parts.
     *
     * @return a String detailing what parts were grown, with repeated lines collapsed.
     */
    @Override
    public String attemptGrow() {
        StringBuilder growMessage = new StringBuilder();
        if (random.nextBoolean()) {
            directParts.add(new Branch());
            growMessage.append("It grows a Branch...\n");
        } else {
            directParts.add(new Leaf());
            growMessage.append("It grows a Leaf...\n");
        }
        // Index-based loop on purpose: parts added during this loop are visited too, so a new
        // Branch can keep growing within the same turn.
        int index = 0;
        while (index < directParts.size()) {
            BossPart bossPart = directParts.get(index);
            growMessage.append(bossPart.grow(this, directParts));
            index++;
        }
        return summarise(growMessage.toString());
    }

    /**
     * Collapses repeated lines into a single line with a count, e.g. "A Leaf heals the boss for
     * 5 HP. (x40)". The boss keeps gaining parts, so without this the growth report would grow
     * by hundreds of lines per turn.
     *
     * @param report the growth report, one event per line
     * @return the report with each distinct line listed once, in order of first appearance
     */
    private static String summarise(String report) {
        Map<String, Integer> lineCounts = new LinkedHashMap<>();
        for (String line : report.split("\n")) {
            if (!line.isBlank()) {
                lineCounts.merge(line, 1, Integer::sum);
            }
        }
        StringBuilder summary = new StringBuilder();
        lineCounts.forEach((line, count) -> summary.append(line)
                .append(count > 1 ? " (x" + count + ")" : "")
                .append("\n"));
        return summary.toString();
    }

    /**
     * Calculates the total additional damage contributed by all of the boss's parts.
     *
     * @return the total additional damage.
     */
    public int getDamageContribution() {
        int totalAdditionalDamage = 0;
        for (BossPart bossPart : directParts) {
            totalAdditionalDamage += bossPart.getDamageContribution();
        }
        return totalAdditionalDamage;
    }

    /**
     * Returns a list of allowable actions that another actor can perform on the Bed of Chaos.
     * If the other actor is hostile, an {@link AttackAction} is added to the list.
     *
     * @param otherActor the actor that might be performing an action.
     * @param direction  String representing the direction of the other actor.
     * @param map        The current GameMap.
     * @return A list of allowable actions.
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        ActionList actions = super.allowableActions(otherActor, direction, map);
        if (otherActor.hasCapability(GeneralCapability.HOSTILE_TO_ENEMY)) {
            actions.add(new AttackAction(this, direction));
        }
        return actions;
    }
}
