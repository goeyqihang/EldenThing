package game.spells;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.effects.HealEffect;

/**
 * A SpellBook that represents a self-healing spell.
 * <p>
 * When cast, this spell heals the caster for a fixed amount of health points (HP)
 * at the cost of mana. The spell is always available to be cast on oneself.
 *
 * @see SpellBook
 * @see CastSpellAction
 * @see HealEffect
 */
public class HealSpell extends SpellBook {

    /**
     * The name of the spell.
     */
    private static final String NAME = "Heal Spell";

    /**
     * The character used to represent this item in the game map or inventory.
     */
    private static final char DISPLAY_CHAR = 'h';

    /**
     * The amount of HP this spell restores.
     */
    private static final int HEAL_AMOUNT = 25;

    /**
     * The mana cost required to cast this spell.
     */
    private static final int MANA_COST = 30;

    /**
     * A description of the spell's effects for display in the game.
     */
    private static final String DESCRIPTION = "Heals the caster for " + HEAL_AMOUNT + " HP.";

    /**
     * Constructor for the HealSpell.
     * Initializes the spell with its predefined name, display character, mana cost, and
     * description.
     */
    public HealSpell() {
        super(NAME, DISPLAY_CHAR, MANA_COST, DESCRIPTION);
    }

    /**
     * Activates the healing effect of the spell.
     * <p>
     * This method verifies that the target of the spell is the caster themselves.
     * If so, it applies a {@link HealEffect} to the caster, restoring their HP.
     * This implementation strictly enforces self-casting.
     *
     * @param caster      The actor casting the spell.
     * @param map         The GameMap where the spell is being cast.
     * @param targetActor The intended target of the spell, which must be the caster.
     * @return A string describing the result of the spell activation.
     */
    @Override
    public String activate(Actor caster, GameMap map, Actor targetActor) {
        // This spell is designed to only heal the caster.
        if (caster == targetActor) {
            new HealEffect(HEAL_AMOUNT).applyEffect(targetActor, map);
            return caster + " casts " + this + " on themself, healing for " + HEAL_AMOUNT + " HP.";
        }
        // This case should ideally not be reached due to how allowableActions is set up.
        return caster + " fails to cast " + this + ": No valid target specified for activation.";
    }

    /**
     * Provides a list of actions that can be performed with this spell.
     * <p>
     * This method always adds a {@link CastSpellAction} to the list, with the caster
     * set as the target. This ensures the Heal Spell is always available for self-use.
     *
     * @param caster The actor holding the spellbook.
     * @param map    The current GameMap.
     * @return An ActionList containing the action to cast the heal spell on oneself.
     */
    @Override
    public ActionList allowableActions(Actor caster, GameMap map) {
        ActionList actions = super.allowableActions(caster, map);
        // The target for the HealSpell is always the caster themselves.
        actions.add(new CastSpellAction(this, caster));
        return actions;
    }
}
