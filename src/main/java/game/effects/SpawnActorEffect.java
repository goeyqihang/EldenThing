package game.effects;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Supplier;

/**
 * An {@link Effect} that spawns a new {@link Actor} on the {@link GameMap}. The actor to be spawned
 * is provided by a {@link Supplier}. The new actor is placed at a random, valid, adjacent location
 * to a target actor. The target actor can either be the actor to whom the effect is applied or a
 * specifically designated {@code spawnNearActor}.
 */
public class SpawnActorEffect implements Effect {

    /**
     * A supplier function that creates instances of the actor to be spawned. This allows for
     * flexible creation of different actor types.
     */
    private final Supplier<Actor> actorSupplier;

    /**
     * An optional specific actor near whom the new actor should be spawned. If null, the new actor
     * will be spawned near the actor to whom this effect is applied.
     */
    private final Actor spawnNearActor;

    /**
     * Random number generator used to pick a spawn location.
     */
    private final Random random = new Random();

    /**
     * Constructs a {@code SpawnActorEffect} that spawns an actor near a specific target actor.
     *
     * @param actorSupplier  A {@link Supplier} that will produce the new {@link Actor} instance.
     *                       For example, {@code GoldenBeetle::new}.
     * @param spawnNearActor The {@link Actor} near whom the new actor will be spawned. If this is
     *                       {@code null}, the actor to whom the effect is applied will be used as
     *                       the reference point for spawning.
     */
    public SpawnActorEffect(Supplier<Actor> actorSupplier, Actor spawnNearActor) {
        this.actorSupplier = actorSupplier;
        this.spawnNearActor = spawnNearActor;
    }

    /**
     * Constructs a {@code SpawnActorEffect} that spawns an actor near the actor to whom the effect
     * is applied.
     *
     * @param actorSupplier A {@link Supplier} that will produce the new {@link Actor} instance. For
     *                      example, {@code GoldenBeetle::new}.
     */
    public SpawnActorEffect(Supplier<Actor> actorSupplier) {
        this(actorSupplier, null); // Default to spawning near the actor receiving the effect
    }

    /**
     * Applies the effect, attempting to spawn a new actor on the map.
     * <p>
     * It determines the central location for spawning: either the location of
     * {@link #spawnNearActor} if specified, or the location of the {@code actor} parameter (the one
     * to whom the effect is being applied).
     * <p>
     * It then identifies all adjacent locations (exits) that are valid for the new actor to enter
     * (i.e., {@link Location#canActorEnter(Actor)} returns true for the new actor type and the
     * location is not already occupied by another actor).
     * <p>
     * If one or more such valid spawn locations are found, one is chosen randomly, and a new actor
     * (obtained from {@link #actorSupplier}) is added to the map at that location. If no valid
     * adjacent spawn locations are found, the effect does nothing.
     *
     * @param actor The {@link Actor} to whom this effect is being applied. If
     *              {@link #spawnNearActor} is {@code null}, this actor's location is used as the
     *              center for spawning.
     * @param map   The {@link GameMap} on which the new actor will be spawned.
     */
    @Override
    public void applyEffect(Actor actor, GameMap map) {
        // Determine the actor whose location will be the center for spawning
        Actor targetActor = (spawnNearActor != null) ? spawnNearActor : actor;
        Location actorLocation = map.locationOf(targetActor);

        // If the target actor is not on the map, cannot spawn.
        if (actorLocation == null) {
            return;
        }

        ArrayList<Location> validSpawnLocations = new ArrayList<>();
        // A throwaway instance, used only to check canActorEnter
        Actor newActorInstanceForCheck = actorSupplier.get();

        // Find all valid adjacent locations for spawning
        for (Exit exit : actorLocation.getExits()) {
            Location destination = exit.getDestination();
            // Check if the destination is valid: actor can enter and no other actor is there
            if (destination.canActorEnter(newActorInstanceForCheck) && !map.isAnActorAt(
                    destination)) {
                validSpawnLocations.add(destination);
            }
        }

        // If there are valid locations, pick one randomly and spawn the actor
        if (!validSpawnLocations.isEmpty()) {
            int randomIndex = random.nextInt(validSpawnLocations.size());
            Location spawnLocation = validSpawnLocations.get(randomIndex);
            // A new instance is created here to ensure a fresh actor is added
            map.addActor(actorSupplier.get(), spawnLocation);
        }
        // If no valid locations, the effect does nothing silently.
    }
}
