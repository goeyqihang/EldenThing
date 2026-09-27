package game.spells;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.BurningGround;
import game.grounds.GroundCapability;
import game.grounds.TemporaryGround;

/**
 * A SpellBook that represents an area-of-effect (AoE) fire spell.
 * <p>
 * This spell, when cast, ignites the ground in all adjacent tiles around the caster.
 * It instantly damages any actors on those tiles and leaves the ground burning for a
 * set duration, dealing damage over time to any actor that stands on it.
 * The spell can only be cast if there is at least one other actor nearby.
 *
 * @see SpellBook
 * @see CastSpellAction
 * @see TemporaryGround
 * @see BurningGround
 */
public class FireSpell extends SpellBook {

    /**
     * The mana cost required to cast this spell.
     */
    private static final int MANA_COST = 25;

    /**
     * The name of the spell.
     */
    private static final String NAME = "Fire Spell";

    /**
     * The character used to represent this item in the game map or inventory.
     */
    private static final char DISPLAY_CHAR = 'f';

    /**
     * The initial damage dealt instantly to actors caught in the spell's area of effect.
     */
    private static final int INSTANT_AREA_DAMAGE = 10;

    /**
     * The number of game turns the ground will remain burning after the spell is cast.
     */
    private static final int BURN_DURATION = 3;

    /**
     * A description of the spell's effects for display in the game.
     */
    private static final String DESCRIPTION = "If an enemy is nearby, ignites surrounding tiles. "
            + "Instantly burns occupants, and tiles remain burning for " + BURN_DURATION
            + " turns, damaging those on them.";

    /**
     * Constructor to create a new FireSpell item.
     * Initializes the spell with its predefined name, display character, mana cost, and
     * description.
     */
    public FireSpell() {
        super(NAME, DISPLAY_CHAR, MANA_COST, DESCRIPTION);
    }

    /**
     * Activates the Fire Spell's area-of-effect.
     * <p>
     * This method iterates through all exits from the caster's location. For each adjacent tile,
     * it deals instant damage to any actor present (other than the caster) and sets the ground
     * to BurningGround for a specified duration if the ground is burnable.
     *
     * @param caster The actor casting the spell.
     * @param map    The GameMap where the spell is being cast.
     * @param target The target actor (not used for this AoE spell, can be null).
     * @return A string describing the outcome of the spell activation.
     */
    @Override
    public String activate(Actor caster, GameMap map, Actor target) {
        Location casterLocation = map.locationOf(caster);

        for (Exit effectExit : casterLocation.getExits()) {
            Location tileToBurn = effectExit.getDestination();
            Ground originalGround = tileToBurn.getGround();

            if (tileToBurn.containsAnActor()) {
                Actor victim = tileToBurn.getActor();
                if (victim != caster) {
                    victim.hurt(INSTANT_AREA_DAMAGE);
                    if (!victim.isConscious()) {
                        victim.unconscious(map);
                    }
                }
            }
            if (originalGround.hasCapability(GroundCapability.BURNABLE)) {
                tileToBurn.setGround(
                        new TemporaryGround(originalGround, new BurningGround(), BURN_DURATION));
            }
        }

        return "The surroundings of " + caster + " erupt in flames!";
    }

    /**
     * Provides a list of actions that can be performed with this spell.
     * <p>
     * This method checks if there is an enemy (any actor other than the caster) in an
     * adjacent tile. If an enemy is present, it adds a {@link CastSpellAction} to the
     * list of allowable actions, making the spell available for use.
     *
     * @param caster The actor holding the spellbook.
     * @param map    The current GameMap.
     * @return An ActionList containing the available actions, including casting the spell if
     * conditions are met.
     */
    @Override
    public ActionList allowableActions(Actor caster, GameMap map) {
        ActionList actions = super.allowableActions(caster, map);
        boolean enemyNearby = false;
        Location ownerLocation = map.locationOf(caster);

        // Check surrounding tiles for any actor that isn't the caster
        for (Exit exit : ownerLocation.getExits()) {
            Location destination = exit.getDestination();
            if (destination.containsAnActor() && destination.getActor() != caster) {
                enemyNearby = true;
                break; // Found an enemy, no need to check further.
            }
        }

        // Only add the spell action if an enemy is present.
        if (enemyNearby) {
            // Pass null for the target, as this AoE spell doesn't have a specific target.
            actions.add(new CastSpellAction(this, null));
        }
        return actions;
    }
}
