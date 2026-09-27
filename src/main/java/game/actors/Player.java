package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttribute;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.displays.Menu;
import edu.monash.fit2099.engine.positions.GameMap;
import game.capabilities.GeneralCapability;
import game.fishing.FishingRod;
import game.fishing.Shovel;
import game.spells.FireSpell;
import game.spells.HealSpell;
import game.spells.PoisonSpell;
import game.spells.TeleportSpell;
import game.weapons.BareFist;

/**
 * Class representing the Player character in the game. The player can interact with the game world,
 * has health, stamina and mana attributes, and is controlled by the user via a menu.
 *
 * @author Adrian Kristanto
 */
public class Player extends Actor {

    /**
     * Maximum stamina point the player can have.
     */
    private static final int MAXIMUM_STAMINA_POINT = 200;

    /**
     * Maximum mana point the player can have. Mana is spent on casting spells.
     */
    private static final int MAXIMUM_MANA_POINT = 200;

    /**
     * Runes the player starts with, enough to try out the merchants' offers.
     */
    private static final int STARTING_RUNES = 10000;

    /**
     * Constructor. Initializes the player with a name, display character and hit points, gives
     * them their capabilities, a BareFist intrinsic weapon, Stamina and Mana attributes, some
     * starting runes, and a starting kit of spell books and tools.
     *
     * @param name        Name to call the player in the UI
     * @param displayChar Character to represent the player in the UI
     * @param hitPoints   Player's starting number of hitpoints
     */
    public Player(String name, char displayChar, int hitPoints) {
        super(name, displayChar, hitPoints);
        this.addCapability(GeneralCapability.HOSTILE_TO_ENEMY);
        this.addCapability(GeneralCapability.FOLLOWABLE);
        this.addCapability(GeneralCapability.CONSUMER);
        this.addCapability(GeneralCapability.CAN_LISTEN);
        this.addCapability(GeneralCapability.CAN_BUY);
        this.addCapability(GeneralCapability.CAN_TELEPORT);
        this.setIntrinsicWeapon(new BareFist());
        this.addAttribute(BaseActorAttributes.STAMINA,
                new BaseActorAttribute(Player.MAXIMUM_STAMINA_POINT));
        this.addAttribute(BaseActorAttributes.MANA,
                new BaseActorAttribute(Player.MAXIMUM_MANA_POINT));
        this.addBalance(Player.STARTING_RUNES);

        this.addItemToInventory(new PoisonSpell());
        this.addItemToInventory(new HealSpell());
        this.addItemToInventory(new TeleportSpell());
        this.addItemToInventory(new FireSpell());
        this.addItemToInventory(new FishingRod());
        this.addItemToInventory(new Shovel());
    }

    /**
     * Selects and returns an action to perform on the current turn. Prints the player's current
     * health, stamina, mana and runes. Handles multi-turn actions if any are in progress.
     * Otherwise, displays a menu of available actions to the user and returns their choice.
     *
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Used to handle multi-turn actions.
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written and the menu displayed
     * @return the Action selected by the player from the menu
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        display.println(this + " " + formatAttribute("Health", BaseActorAttributes.HEALTH)
                + " " + formatAttribute("Stamina", BaseActorAttributes.STAMINA)
                + " " + formatAttribute("Mana", BaseActorAttributes.MANA)
                + " Runes: " + this.getBalance());

        // Handle multi-turn Actions first
        if (lastAction.getNextAction() != null) {
            return lastAction.getNextAction();
        }

        // Show the menu and get the player's choice
        Menu menu = new Menu(actions);
        return menu.showMenu(this, display);
    }

    /**
     * Formats one of the player's attributes for the status line, e.g. "Health: (80/100)".
     *
     * @param label     the label to show in front of the values
     * @param attribute the attribute to format
     * @return the formatted attribute
     */
    private String formatAttribute(String label, BaseActorAttributes attribute) {
        return label + ": (" + this.getAttribute(attribute) + "/"
                + this.getAttributeMaximum(attribute) + ")";
    }

    /**
     * Returns the name of the player. Overrides the default Actor toString to provide a simpler
     * representation.
     *
     * @return The player's name.
     */
    @Override
    public String toString() {
        return this.name;
    }
}
