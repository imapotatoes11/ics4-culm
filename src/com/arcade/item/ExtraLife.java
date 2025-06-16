/**
 * ExtraLife.java
 *
 * functional item that provides additional chances in games
 * extends Functional class to give players extra attempts
 * useful for high-difficulty games or when facing failure
 *
 * date: jun 15, 2025
 * author: wen zheng, kevin wang
 */
package com.arcade.item;

import java.io.*;
import java.util.*;

/**
 * functional item that grants an extra life or chance in games
 * allows players to continue playing after failure conditions
 * can be purchased with tickets and has limited uses
 */
public class ExtraLife extends Functional {

    /**
     * constructor for creating an extra life item with custom parameters
     * 
     * @param name    the display name of the item
     * @param numUses the number of times this item can be used
     * @param price   the cost in tickets to purchase this item
     */
    public ExtraLife(String name, int numUses, int price) {
        super(name, numUses, price);
    }

    /**
     * default constructor for creating a standard extra life item
     * uses default values: name "Extra Life", 1 use, costs 10 tickets
     */
    public ExtraLife() {
        super("Extra Life", 1, 10); // default values for an extra life
    }

    /**
     * activates the extra life item
     * note: current implementation seems incorrect - it adds uses instead of
     * consuming them
     * should probably integrate with game logic to provide actual extra life
     * functionality
     */
    public void activate() {
        this.setNumUses(getNumUses() + 1); // increment the number of uses (seems like a bug)
        // this implementation appears incorrect - should decrease uses and provide
        // extra life
        // actual extra life logic would need to be integrated with individual games
    }
}
