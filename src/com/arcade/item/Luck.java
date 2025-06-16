/**
 * Luck.java
 *
 * functional item that temporarily reduces game difficulty
 * extends Functional class to provide luck-based game advantages
 * makes games easier by decreasing their difficulty factor
 *
 * date: jun 15, 2025
 * author: wen zheng, kevin wang
 */
package com.arcade.item;

import java.io.*;
import java.util.*;

/**
 * functional item that provides luck-based advantages in games
 * temporarily reduces game difficulty to improve player success chances
 * has configurable difficulty reduction factor and limited uses
 */
public class Luck extends Functional {
    private int difficultyDecreaseFactor; // amount to reduce game difficulty by

    /**
     * constructor for creating a luck item with custom parameters
     * 
     * @param name                     the display name of the item
     * @param numUses                  the number of times this item can be used
     * @param price                    the cost in tickets to purchase this item
     * @param difficultyDecreaseFactor the amount to reduce game difficulty by
     */
    public Luck(String name, int numUses, int price, int difficultyDecreaseFactor) {
        super(name, numUses, price);
        this.difficultyDecreaseFactor = difficultyDecreaseFactor;
    }

    /**
     * gets the difficulty decrease factor of this luck item
     * 
     * @return the amount by which this item reduces game difficulty
     */
    public int getDifficultyDecreaseFactor() {
        return difficultyDecreaseFactor;
    }

    /**
     * sets the difficulty decrease factor of this luck item
     * 
     * @param difficultyDecreaseFactor the amount by which to reduce game difficulty
     */
    public void setDifficultyDecreaseFactor(int difficultyDecreaseFactor) {
        this.difficultyDecreaseFactor = difficultyDecreaseFactor;
    }

    /**
     * activates the luck item
     * decreases the number of uses remaining
     * actual difficulty reduction logic not yet implemented
     */
    public void activate() {
        this.setNumUses(this.getNumUses() - 1); // consume one use
        // difficulty reduction logic not implemented yet
        // would need to be integrated with individual game difficulty systems
    }
}
