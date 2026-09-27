package game;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.FancyGroundFactory;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.positions.World;
import game.grounds.Blight;
import game.grounds.Floor;
import game.grounds.Soil;
import game.grounds.Wall;
import java.util.Arrays;

/**
 * Builds small maps for tests, using the same ground characters as the game: {@code .} Soil,
 * {@code x} Blight, {@code #} Wall and {@code _} Floor.
 */
public final class TestWorld {

    private TestWorld() {
    }

    /**
     * Creates a map from the given rows and registers it with a new {@link World}, so that actors
     * can be added to it.
     *
     * @param rows the rows of the map
     * @return the new map
     */
    public static GameMap map(String... rows) {
        World world = new World(new Display());
        FancyGroundFactory groundFactory = new FancyGroundFactory(new Blight(), new Wall(),
                new Floor(), new Soil());
        GameMap map = new GameMap("Test", groundFactory, Arrays.asList(rows));
        world.addGameMap(map);
        return map;
    }

    /**
     * Creates a 5x5 map of Soil.
     *
     * @return the new map
     */
    public static GameMap soilMap() {
        return map(".....", ".....", ".....", ".....", ".....");
    }

    /**
     * Counts the actors of a given type on a map.
     *
     * @param map  the map to search
     * @param type the type of actor to count
     * @return the number of matching actors
     */
    public static int count(GameMap map, Class<? extends Actor> type) {
        int count = 0;
        for (int y : map.getYRange()) {
            for (int x : map.getXRange()) {
                Location location = map.at(x, y);
                if (location.containsAnActor() && type.isInstance(location.getActor())) {
                    count++;
                }
            }
        }
        return count;
    }
}
