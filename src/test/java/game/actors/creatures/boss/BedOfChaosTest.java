package game.actors.creatures.boss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import game.TestActor;
import game.TestWorld;
import game.actors.creatures.boss.parts.BossPart;
import game.actors.creatures.boss.parts.Branch;
import game.actors.creatures.boss.parts.Leaf;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BedOfChaosTest {

    private final BedOfChaos boss = new BedOfChaos();

    @Test
    void leafHealsTheBossAndAddsOneDamage() {
        boss.hurt(20);

        new Leaf().grow(boss, new ArrayList<>());

        assertEquals(985, boss.getAttribute(BaseActorAttributes.HEALTH));
        assertEquals(1, new Leaf().getDamageContribution());
    }

    @Test
    void branchAddsThreeDamage() {
        assertEquals(3, new Branch().getDamageContribution());
    }

    @Test
    void branchGrowsOnePartPerTurnUntilItGrowsALeaf() {
        Branch branch = new Branch();
        List<BossPart> parts = new ArrayList<>();

        for (int turn = 0; turn < 200; turn++) {
            int before = parts.size();
            branch.grow(boss, parts);
            assertEquals(before + 1, parts.size(), "a productive branch grows exactly one part");

            if (parts.get(parts.size() - 1) instanceof Leaf) {
                int partsWhenDone = parts.size();
                assertEquals("", branch.grow(boss, parts));
                assertEquals(partsWhenDone, parts.size(), "no more growth after a leaf");
                return;
            }
            assertInstanceOf(Branch.class, parts.get(parts.size() - 1));
        }
        fail("the branch never grew a leaf");
    }

    @Test
    void attacksHitForBaseDamagePlusEveryPart() {
        for (int turn = 0; turn < 10; turn++) {
            boss.attemptGrow();
        }
        int expectedDamage = 25 + boss.getDamageContribution();
        TestActor target = new TestActor(1_000_000);

        boss.evaluate(boss, target, TestWorld.soilMap());
        String result;
        do {
            result = boss.getIntrinsicWeapon().attack(boss, target, TestWorld.soilMap());
        } while (result.contains("misses"));

        assertTrue(result.endsWith("for " + expectedDamage + " damage"), result);
    }

    @Test
    void growthReportStaysShortAsTheBossGrows() {
        String report = "";
        for (int turn = 0; turn < 40; turn++) {
            report = boss.attemptGrow();
        }

        Set<String> events = new HashSet<>();
        for (String line : report.strip().split("\n")) {
            String event = line.replaceAll(" \\(x\\d+\\)$", "");
            assertTrue(event.matches("It grows a (Branch|Leaf)\\.\\.\\.|Branch is growing\\.\\.\\."
                    + "|A Leaf heals the boss for 5 HP\\."), "unexpected line: " + line);
            assertTrue(events.add(event), "repeated line: " + line);
        }
    }
}
