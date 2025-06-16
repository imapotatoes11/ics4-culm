/**
 * Item.java
 *
 * base class for all items in the arcade system
 * provides common functionality for all item types
 * serves as parent class for achievements, functional items, etc.
 *
 * date: jun 15, 2025
 * author: wen zheng, kevin wang
 */
package com.arcade.item;

/**
 * base class for all items in the arcade system
 * defines common properties that all items share
 * provides foundation for inheritance hierarchy
 */
public class Item {
    private String name; // display name of the item

    /**
     * constructor for creating an item with a name
     * 
     * @param name the display name of the item
     */
    public Item(String name) {
        this.name = name;
    }

    /**
     * gets the name of the item
     * 
     * @return the item's name
     */
    public String getName() {
        return name;
    }

    /**
     * sets the name of the item
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
