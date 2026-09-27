package game.spells;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * An Action that allows an Actor to cast a spell from a SpellBook.
 * This action handles the mana cost deduction and triggers the spell's effect.
 */
public class CastSpellAction extends Action {

    /**
     * The SpellBook item that contains the spell to be cast.
     */
    private final SpellBook spellbook;

    /**
     * The target Actor for the spell. Can be null for non-targeted spells.
     */
    private final Actor target;

    /**
     * Constructor for creating a CastSpellAction.
     *
     * @param spellbook The SpellBook item containing the spell to be cast.
     * @param target    The Actor who is the target of the spell. May be null.
     */
    public CastSpellAction(SpellBook spellbook, Actor target) {
        this.spellbook = spellbook;
        this.target = target;
    }

    /**
     * Executes the spell casting action.
     * <p>
     * This method first checks if the caster has sufficient mana. If they do,
     * the mana cost is deducted, and the spell's effect is activated via the
     * SpellBook's activate method. If not, it returns a message indicating
     * insufficient mana.
     *
     * @param caster The actor performing the spell cast.
     * @param map    The map the actor is on.
     * @return A string describing the result of the action (e.g., spell effect or failure message).
     */
    @Override
    public String execute(Actor caster, GameMap map) {
        int manaCost = spellbook.getManaCost();

        if (!caster.hasAttribute(BaseActorAttributes.MANA)
                || caster.getAttribute(BaseActorAttributes.MANA) < manaCost) {
            return caster + " does not have enough mana to cast " + spellbook
                    + ". (Required: " + manaCost + ")";
        }

        // Deduct mana
        caster.modifyAttribute(BaseActorAttributes.MANA, ActorAttributeOperations.DECREASE,
                manaCost);

        // Activate the spell by calling the spellbook's activate method
        return spellbook.activate(caster, map, target);
    }

    /**
     * Returns a descriptive string for the game menu.
     * <p>
     * This description informs the player what spell will be cast, who the target is (if any),
     * and how much mana it will cost.
     *
     * @param actor The actor performing the action.
     * @return A string suitable for display in the menu.
     */
    @Override
    public String menuDescription(Actor actor) {
        String description = actor + " casts " + spellbook;

        // If the target is not null, it's a targeted spell, so add the target's name.
        if (this.target != null) {
            description += " on " + target;
        }

        description += " (Cost: " + spellbook.getManaCost() + " Mana)";
        return description;
    }
}
