package game.spells;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * An abstract base class for all spell-casting items in the game.
 * <p>
 * A SpellBook is a portable Item that has an associated mana cost and a description.
 * Concrete implementations of this class must provide the specific logic for what happens
 * when the spell is activated.
 *
 * @see Item
 * @see CastSpellAction
 */
public abstract class SpellBook extends Item {

    /**
     * The amount of mana required to cast the spell.
     */
    private final int manaCost;

    /**
     * The description of the spell's effect.
     */
    private final String description;

    /**
     * Constructor for the SpellBook.
     *
     * @param name        The name of the spell, to be displayed in-game.
     * @param displayChar The character that represents this item on the game map.
     * @param manaCost    The integer amount of mana required to activate the spell.
     * @param description A short string describing what the spell does.
     */
    public SpellBook(String name, char displayChar, int manaCost, String description) {
        super(name, displayChar, true); // All spellbooks are portable
        this.manaCost = manaCost;
        this.description = description;
    }

    /**
     * Gets the mana cost of the spell.
     *
     * @return The integer mana cost.
     */
    public int getManaCost() {
        return manaCost;
    }

    /**
     * Gets the description of the spell.
     *
     * @return The string description of the spell's effects.
     */
    public String getDescription() {
        return description;
    }

    /**
     * An abstract method that defines the spell's effect when activated.
     * <p>
     * This method must be implemented by all concrete subclasses. It contains the
     * core logic of the spell, such as dealing damage, applying effects, or healing.
     * This method is typically called by a {@link CastSpellAction} after the mana cost
     * has been successfully paid.
     *
     * @param caster The actor casting the spell.
     * @param map    The GameMap where the spell is being cast.
     * @param target The actor being targeted by the spell. This can be null for
     * area-of-effect or self-cast spells.
     * @return A string describing the result of the spell's activation for display
     * in the game console.
     */
    public abstract String activate(Actor caster, GameMap map, Actor target);
}
