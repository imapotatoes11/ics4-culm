/**
 * Functional.java
 *
 * abstract base class for functional items in the arcade system
 * functional items are usable items that provide in-game benefits
 * extends Item class with usage tracking and pricing
 *
 * date: jun 15, 2025
 * author: wen zheng, kevin wang
 */
package com.arcade.item;

/**
 * abstract base class for functional items that can be used in games
 * provides common functionality for items with limited uses and prices
 * must be extended by specific functional item types
 */
public abstract class Functional extends Item {
    private int numUses; // number of times this item can be used
    private int price; // cost in tickets to purchase this item

    /**
     * constructor for creating a functional item
     * 
     * @param name    the display name of the item
     * @param numUses the number of times this item can be used
     * @param price   the cost in tickets to purchase this item
     */
    public Functional(String name, int numUses, int price) {
        super(name);
        this.numUses = numUses;
        this.price = price;
    }

    /**
     * abstract method that must be implemented by subclasses
     * defines what happens when the item is activated/used
     */
    public abstract void activate();

    /**
     * gets the number of uses remaining for this item
     * 
     * @return the number of uses left
     */
    public int getNumUses() {
        return numUses;
    }

    /**
     * sets the number of uses remaining for this item
     * 
     * @param numUses the number of uses to set
     */
    public void setNumUses(int numUses) {
        this.numUses = numUses;
    }

    /**
     * gets the price of this item in tickets
     * 
     * @return the price in tickets
     */
    public int getPrice() {
        return price;
    }

    /**
     * sets the price of this item in tickets
     * 
     * @param price the price to set in tickets
     */
    public void setPrice(int price) {
        this.price = price;
    }
}
