/**
 * Achievement.java
 *
 * represents an achievement that players can earn in the arcade
 * extends Item class to provide achievement-specific functionality
 * includes factory methods for creating common achievements
 *
 * date: jun 15, 2025
 * author: kevin wang
 */
package com.arcade.item;

import com.arcade.item.Item;

/**
 * represents an achievement that can be earned by players
 * achievements have names (inherited from Item) and descriptions
 * provides factory methods for creating predefined achievements
 */
public class Achievement extends Item {
    private String description; // detailed description of the achievement

    /**
     * constructor for creating an achievement with name and description
     * 
     * @param name        the name of the achievement
     * @param description detailed description of what the achievement represents
     */
    public Achievement(String name, String description) {
        super(name);
        this.description = description;
    }

    /**
     * gets the description of the achievement
     * 
     * @return the achievement description
     */
    public String getDescription() {
        return description;
    }

    /**
     * sets the description of the achievement
     * note: this method seems to have a bug - it sets description instead of name
     * 
     * @param description the description to set (though parameter name suggests
     *                    name)
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * factory method for creating a champion achievement
     * awarded for playing over 10 games in the arcade
     * 
     * @return a champion achievement instance
     */
    public static Achievement champion() {
        return new Achievement("Champion Achievement Unlocked!",
                "Congratulation! This achievement is awarded for playing over 10 games inside the simulation arcade!");
    }

    /**
     * factory method for creating a power-up pro achievement
     * awarded for using over 3 items in the arcade
     * 
     * @return a power-up pro achievement instance
     */
    public static Achievement powerUpPro() {
        return new Achievement("Power-Up Pro Achievement Unlocked!",
                "Congratulation! This achievement is awarded for using over 3 items inside the simulation arcade!");
    }

    /**
     * factory method for creating a level legend achievement
     * awarded for completing a game over difficulty level 7
     * 
     * @return a level legend achievement instance
     */
    public static Achievement levelLegend() {
        return new Achievement("Level Legend Achievement Unlocked!",
                "Congratulation! This achievement is awarded for completing a game over difficulty level 7 inside the simulation arcade!");
    }

    /**
     * factory method for creating a boss buster achievement
     * awarded for completing a game at difficulty level 10 (maximum)
     * 
     * @return a boss buster achievement instance
     */
    public static Achievement bossBuster() {
        return new Achievement("Boss Buster Achievement Unlocked!",
                "Congratulation! This achievement is awarded for completing a game at difficulty level 10 inside the simulation arcade!");
    }

    /**
     * factory method for creating a death pixel achievement
     * awarded for losing or dying once in the arcade (consolation achievement)
     * 
     * @return a death pixel achievement instance
     */
    public static Achievement deathPixel() {
        return new Achievement("Death Pixel Achievement Unlocked!",
                "Congratulation! This achievement is awarded for losing/dying once inside the simulation arcade!");
    }
}