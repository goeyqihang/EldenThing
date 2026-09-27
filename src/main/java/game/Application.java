package game;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.FancyGroundFactory;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.World;
import game.actors.Player;
import game.actors.creatures.GoldenBeetle;
import game.actors.creatures.OmenSheep;
import game.actors.creatures.SpiritGoat;
import game.actors.creatures.boss.BedOfChaos;
import game.actors.npc.NpcGuts;
import game.actors.npc.NpcKale;
import game.actors.npc.NpcSellen;
import game.behaviours.behaviourselectors.BehaviourSelector;
import game.behaviours.behaviourselectors.PriorityBehaviourSelector;
import game.behaviours.behaviourselectors.RandomBehaviourSelector;
import game.fishing.Pond;
import game.grounds.Blight;
import game.grounds.Floor;
import game.grounds.Soil;
import game.grounds.Wall;
import game.healing.items.Talisman;
import game.plants.BloodroseSeed;
import game.plants.InheritreeSeed;
import game.teleport.TeleportationGate;
import game.ui.FancyMessage;
import java.util.Arrays;
import java.util.List;

/**
 * The main class to set up and run the game. It builds two maps, the Valley of the Inheritree and
 * Limveld, links them with a pair of teleportation gates, and places the player, creatures, NPCs
 * and the Bed of Chaos boss.
 *
 * @author Adrian Kristanto
 */
public class Application {

    /**
     * Delay between the lines of the title banner, in milliseconds.
     */
    private static final int TITLE_LINE_DELAY_MS = 200;

    /**
     * Main method to run the game application. Sets up the game world, maps, player, creatures,
     * NPCs and items.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        Display display = new Display();
        World world = new World(display);

        FancyGroundFactory groundFactory = new FancyGroundFactory(new Blight(), new Wall(),
                new Floor(), new Soil());

        List<String> valleyMap = Arrays.asList(
                "xxxx...xxxxxxxxxxxxxxxxxxxxxxx........xx",
                "xxx.....xxxxxxx..xxxxxxxxxxxxx.........x",
                "..........xxxx....xxxxxxxxxxxxxx.......x",
                "....xxx...........xxxxxxxxxxxxxxx.....xx",
                "...xxxxx...........xxxxxxxxxxxxxx.....xx",
                "...xxxxxxxxxx.......xxxxxxxx...xx......x",
                "....xxxxxxxxxx........xxxxxx...xxx......",
                "....xxxxxxxxxxx.........xxx....xxxx.....",
                "....xxxxxxxxxxx................xxxx.....",
                "...xxxx...xxxxxx.....#####.....xxx......",
                "...xxx....xxxxxxx....#___#.....xx.......",
                "..xxxx...xxxxxxxxx...#___#....xx........",
                "xxxxx...xxxxxxxxxx...##_##...xxx.......x",
                "xxxxx..xxxxxxxxxxx.........xxxxx......xx",
                "xxxxx..xxxxxxxxxxxx.......xxxxxx......xx");
        GameMap valley = new GameMap("Valley of the Inheritree", groundFactory, valleyMap);
        world.addGameMap(valley);

        List<String> limveldMap = Arrays.asList(
                ".............xxxx",
                "..............xxx",
                "................x",
                ".................",
                "................x",
                "...............xx",
                "..............xxx",
                "..............xxx",
                "..............xxx",
                ".............xxxx",
                ".............xxxx",
                "....xxx.....xxxxx",
                "....xxxx...xxxxxx");
        GameMap limveld = new GameMap("Limveld", groundFactory, limveldMap);
        world.addGameMap(limveld);

        // BEHOLD, ELDEN THING!
        printTitle(display);

        // Link the two maps with a pair of teleportation gates
        TeleportationGate valleyGate = new TeleportationGate();
        TeleportationGate limveldGate = new TeleportationGate();
        valleyGate.addDestination(limveld.at(8, 6));
        limveldGate.addDestination(valley.at(23, 14));
        valley.at(23, 14).setGround(valleyGate);
        limveld.at(8, 6).setGround(limveldGate);

        valley.at(22, 13).setGround(new Pond());

        Player player = new Player("Farmer", '@', 100);
        world.addPlayer(player, valley.at(23, 11));
        player.addItemToInventory(new BloodroseSeed());
        player.addItemToInventory(new InheritreeSeed());
        valley.at(24, 11).addItem(new Talisman());

        // Creatures in the valley pick behaviours by priority, those in Limveld pick at random
        BehaviourSelector prioritySelector = new PriorityBehaviourSelector();
        BehaviourSelector randomSelector = new RandomBehaviourSelector();

        valley.addActor(new SpiritGoat(prioritySelector), valley.at(20, 10));
        valley.addActor(new OmenSheep(prioritySelector), valley.at(20, 6));
        valley.addActor(new GoldenBeetle(prioritySelector), valley.at(18, 10));

        limveld.addActor(new SpiritGoat(randomSelector), limveld.at(5, 5));
        limveld.addActor(new OmenSheep(randomSelector), limveld.at(6, 5));
        limveld.addActor(new GoldenBeetle(randomSelector), limveld.at(7, 5));

        valley.addActor(new NpcSellen(), valley.at(10, 5));
        valley.addActor(new NpcKale(), valley.at(35, 12));
        valley.addActor(new NpcGuts(), valley.at(5, 13));

        limveld.addActor(new BedOfChaos(), limveld.at(9, 6));

        world.run();
    }

    /**
     * Prints the title banner line by line, with a short delay between lines.
     *
     * @param display the display to print the banner to
     */
    private static void printTitle(Display display) {
        for (String line : FancyMessage.TITLE.split("\n")) {
            display.println(line);
            try {
                Thread.sleep(TITLE_LINE_DELAY_MS);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
