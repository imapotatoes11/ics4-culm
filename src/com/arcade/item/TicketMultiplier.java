/**
 * TicketMultiplier.java
 *
 * functional item that multiplies ticket rewards from games
 * extends Functional class to provide ticket enhancement capability
 * allows players to boost their earnings temporarily
 *
 * date: jun 15, 2025
 * author: wen zheng, kevin wang
 */
package com.arcade.item;

import java.io.*;
import java.util.*;

/**
 * functional item that multiplies the number of tickets earned from games
 * provides a temporary boost to ticket earnings when activated
 * has limited uses and costs tickets to purchase
 */
public class TicketMultiplier extends Functional {
    public static final int MULTIPLIER = 2; // multiplier factor for ticket rewards

    /**
     * constructor for creating a ticket multiplier item
     * 
     * @param name    the display name of the item
     * @param numUses the number of times this item can be used
     * @param price   the cost in tickets to purchase this item
     */
    public TicketMultiplier(String name, int numUses, int price) {
        super(name, numUses, price);
    }

    /**
     * activates the ticket multiplier item
     * decreases the number of uses remaining
     * actual ticket multiplication logic not yet implemented
     */
    @Override
    public void activate() {
        this.setNumUses(this.getNumUses() - 1); // consume one use
        // ticket multiplication logic not implemented yet
        // would need to be integrated with game reward system
    }
}
