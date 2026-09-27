package game.spells;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.effects.ContinuousDamageEffect;

/**
 * A SpellBook that represents a targeted poison spell.
 * <p>
 * This spell, when cast on a target, applies a damage-over-time effect (poison) that
 * hurts the target for a set number of turns. The spell can be cast on any conscious actor.
 *
 * @see SpellBook
 * @see CastSpellAction
 * @see ContinuousDamageEffect
 */
public class PoisonSpell extends SpellBook {

    /**
     * The mana cost required to cast this spell.
     */
    private static final int MANA_COST = 40;

    /**
     * The name of the spell.
     */
    private static final String NAME = "Poison Spell";

    /**
     * The character used to represent this item in the game map or inventory.
     */
    private static final char DISPLAY_CHAR = 'p';

    /**
     * The number of game turns the poison effect will last.
     */
    private static final int POISON_DURATION = 3;

    /**
     * The damage dealt by the poison each turn.
     */
    private static final int POISON_DAMAGE_PER_TURN = 25;

    /**
     * A description of the spell's effects for display in the game.
     */
    private static final String DESCRIPTION = "Poisons a nearby target for " + POISON_DURATION
            + " turns, dealing " + POISON_DAMAGE_PER_TURN + " damage per turn.";

    /**
     * Constructor for the PoisonSpell.
     * Initializes the spell with its predefined name, display character, mana cost, and
     * description.
     */
    public PoisonSpell() {
        super(NAME, DISPLAY_CHAR, MANA_COST, DESCRIPTION);
    }

    /**
     * Activates the poison spell on a target actor.
     * <p>
     * This method creates a {@link ContinuousDamageEffect} and applies it to the target,
     * causing them to take damage over several turns.
     *
     * @param caster The actor casting the spell.
     * @param map    The GameMap where the spell is being cast.
     * @param target The actor being targeted by the spell.
     * @return A string describing that the target has been poisoned.
     */
    @Override
    public String activate(Actor caster, GameMap map, Actor target) {
        ContinuousDamageEffect poisonEffect = new ContinuousDamageEffect("Poisoned by " + NAME,
                POISON_DURATION, POISON_DAMAGE_PER_TURN);
        // Add the poison status effect to the target actor.
        target.addStatusEffect(poisonEffect);
        return target + " is engulfed in a " + NAME + " and becomes poisoned!";
    }

    /**
     * Returns a list of actions that the owner of this item can perform on another actor.
     * <p>
     * This method is called by the game engine to determine if the spell can be cast on a
     * potential target ({@code otherActor}). It will add a {@link CastSpellAction} if the target
     * is valid (i.e., not null and is conscious), making the spell usable against them.
     *
     * @param otherActor The potential target actor.
     * @param location   The location of the other actor (unused in this implementation).
     * @return An ActionList that includes the {@link CastSpellAction} if the target is valid.
     */
    @Override
    public ActionList allowableActions(Actor otherActor, Location location) {
        ActionList actions = super.allowableActions(otherActor, location);
        // Check if the potential target is a valid, conscious actor.
        if (otherActor != null && otherActor.isConscious()) {
            actions.add(new CastSpellAction(this, otherActor));
        }
        return actions;
    }
}
