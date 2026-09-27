package game.weapons.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.Weapon;

/**
 * An {@link Action} that allows an {@link Actor} to attack another Actor.
 * This action can be initiated using either a specific {@link Weapon} item
 * or the attacker's intrinsic weapon (if no specific weapon is provided).
 * It handles the execution of the attack via the weapon's logic and processes
 * the target becoming unconscious if their health drops to zero or below.
 * <p>
 * Created by the FIT2099 Teaching Team.
 *
 * @author Adrian Kristanto
 * Modified by: Goey Qi Hang
 */
public class AttackAction extends Action {

    /**
     * The {@link Actor} that is the target of this attack action.
     */
    private final Actor target;

    /**
     * A {@link String} describing the direction of the attack (e.g., "North", "West").
     * Primarily used for display purposes in the menu description.
     */
    private final String direction;

    /**
     * The {@link Weapon} used for this specific attack instance.
     * If null, the attacker's intrinsic weapon will be used during execution.
     */
    private Weapon weapon;

    /**
     * Constructor for an AttackAction using a specific weapon.
     *
     * @param target    The {@link Actor} to attack.
     * @param direction The direction from which the attack originates (for display).
     * @param weapon    The specific {@link Weapon} (e.g., a {@link game.weapons.WeaponItem}) to
     *                  use for the attack.
     */
    public AttackAction(Actor target, String direction, Weapon weapon) {
        this.target = target;
        this.direction = direction;
        this.weapon = weapon;
    }

    /**
     * Constructor for an AttackAction using the attacker's intrinsic weapon by default.
     * The {@code weapon} field will be null initially and resolved during execution.
     *
     * @param target    The {@link Actor} to attack.
     * @param direction The direction from which the attack originates (for display).
     */
    public AttackAction(Actor target, String direction) {
        this.target = target;
        this.direction = direction;
        // weapon is implicitly null, will use intrinsic weapon in execute()
    }

    /**
     * Executes the attack action:
     * <ol>
     * <li>Determines the weapon to use: the provided {@code weapon} field, or the attacker's
     * intrinsic weapon if the field is null.</li>
     * <li>Calls the {@link Weapon#attack(Actor, Actor, GameMap)} method on the chosen weapon,
     * delegating the core attack logic (hit chance, damage calculation) to the weapon itself.</li>
     * <li>If the {@code target} became unconscious (health &lt;= 0) as a result of the attack,
     * appends the result of the target's {@link Actor#unconscious(Actor, GameMap)} method to the
     * output string.</li>
     * </ol>
     *
     * @param actor The {@link Actor} performing the attack.
     * @param map   The {@link GameMap} where the attack occurs.
     * @return A string detailing the attack's result (e.g., "Player hits Goblin for 10 damage." or
     * "Player misses Goblin."). If the target becomes unconscious, includes the message from the
     * target's unconscious method.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        // Use intrinsic weapon if no specific weapon was provided in the constructor
        if (weapon == null) {
            weapon = actor.getIntrinsicWeapon();
        }

        // Delegate attack logic to the weapon
        String result = weapon.attack(actor, target, map);

        // Check if the target is unconscious after the attack
        if (!target.isConscious()) {
            // Append the unconscious message
            result += "\n" + target.unconscious(actor, map);
        }

        return result;
    }

    /**
     * Provides a description of the attack action suitable for display in a menu.
     * Specifies the attacker, target, and the weapon being used
     * (either the specific weapon's name or "Intrinsic Weapon").
     * Includes the direction only if it's non-empty.
     *
     * @param actor The actor performing the action.
     * @return A string describing the action, e.g., "Player attacks Goblin with Sword".
     */
    @Override
    public String menuDescription(Actor actor) {
        // Determine weapon name for description
        String weaponName = (weapon != null ? weapon.toString() : "Intrinsic Weapon");
        String directionText = direction.isEmpty() ? "" : " at " + direction;
        return actor + " attacks " + target + directionText + " with " + weaponName;
    }
}
