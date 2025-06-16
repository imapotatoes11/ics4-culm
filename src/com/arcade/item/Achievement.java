/**
 * Achievement.java
 *
 * represents an achievement that players can earn in the arcade
 * extends Item class to provide achievement-specific functionality
 * includes factory methods for creating common achievements
 *
 * date: jun 15, 2025
 * author: wen zheng, kevin wang
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
     * 
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    // ======== GENERAL ACHIEVEMENTS ========

    /**
     * factory method for creating a first victory achievement
     * awarded for winning any game for the first time
     * 
     * @return a first victory achievement instance
     */
    public static Achievement firstVictory() {
        return new Achievement("First Victory!",
                "Congratulations! You've won your first game in the arcade!");
    }

    /**
     * factory method for creating a perfect performance achievement
     * awarded for achieving a perfect performance score (1.0) in any game
     * 
     * @return a perfect performance achievement instance
     */
    public static Achievement perfectPerformance() {
        return new Achievement("Perfection Achieved!",
                "Outstanding! You achieved a perfect performance score in a game!");
    }

    /**
     * factory method for creating a hard mode champion achievement
     * awarded for completing any game on maximum difficulty (10)
     * 
     * @return a hard mode champion achievement instance
     */
    public static Achievement hardModeChampion() {
        return new Achievement("Hard Mode Champion!",
                "Incredible! You conquered a game on maximum difficulty level!");
    }

    /**
     * factory method for creating a comeback kid achievement
     * awarded for winning a game after using an Extra Life item
     * 
     * @return a comeback kid achievement instance
     */
    public static Achievement comebackKid() {
        return new Achievement("Comeback Kid!",
                "Amazing! You turned defeat into victory using an Extra Life!");
    }

    /**
     * factory method for creating a game over achievement
     * awarded for experiencing a game over (consolation achievement)
     * 
     * @return a game over achievement instance
     */
    public static Achievement gameOver() {
        return new Achievement("Game Over... or is it?",
                "Don't give up! Every failure is a step towards success!");
    }

    // ======== BLACKJACK ACHIEVEMENTS ========

    /**
     * factory method for creating a blackjack natural achievement
     * awarded for getting exactly 21 in blackjack
     * 
     * @return a blackjack natural achievement instance
     */
    public static Achievement blackjackNatural() {
        return new Achievement("Blackjack Natural!",
                "Perfect! You hit exactly 21 in Blackjack!");
    }

    /**
     * factory method for creating a dealer buster achievement
     * awarded for winning blackjack when dealer goes over 21
     * 
     * @return a dealer buster achievement instance
     */
    public static Achievement dealerBuster() {
        return new Achievement("Dealer Buster!",
                "Lucky! The dealer went bust and you won!");
    }

    // ======== POKEMAN ACHIEVEMENTS ========

    /**
     * factory method for creating a pokeman champion achievement
     * awarded for defeating all 4 enemies in Pokeman Adventure
     * 
     * @return a pokeman champion achievement instance
     */
    public static Achievement pokemanChampion() {
        return new Achievement("Pokeman Champion!",
                "Legendary! You defeated all enemies and became the Pokeman Champion!");
    }

    /**
     * factory method for creating a flawless victory achievement
     * awarded for completing Pokeman Adventure with full health
     * 
     * @return a flawless victory achievement instance
     */
    public static Achievement flawlessVictory() {
        return new Achievement("Flawless Victory!",
                "Masterful! You completed Pokeman Adventure without taking damage!");
    }

    /**
     * factory method for creating a boss buster achievement
     * awarded for defeating the final boss in Pokeman Adventure
     * 
     * @return a boss buster achievement instance
     */
    public static Achievement bossBuster() {
        return new Achievement("Boss Buster!",
                "Epic! You defeated the final boss in Pokeman Adventure!");
    }

    // ======== TRIVIA ACHIEVEMENTS ========

    /**
     * factory method for creating a trivia master achievement
     * awarded for getting all trivia questions correct
     * 
     * @return a trivia master achievement instance
     */
    public static Achievement triviaMaster() {
        return new Achievement("Trivia Master!",
                "Brilliant! You answered all trivia questions correctly!");
    }

    /**
     * factory method for creating a scholar achievement
     * awarded for completing trivia on difficulty 8+ with high accuracy
     * 
     * @return a scholar achievement instance
     */
    public static Achievement scholar() {
        return new Achievement("Scholar!",
                "Impressive! You excelled at high-difficulty trivia questions!");
    }

    // ======== ESCAPE ROOM ACHIEVEMENTS ========

    /**
     * factory method for creating an escape artist achievement
     * awarded for completing the escape room
     * 
     * @return an escape artist achievement instance
     */
    public static Achievement escapeArtist() {
        return new Achievement("Escape Artist!",
                "Clever! You solved all the puzzles and escaped the haunted mansion!");
    }

    /**
     * factory method for creating a speed runner achievement
     * awarded for completing escape room in under 3 minutes
     * 
     * @return a speed runner achievement instance
     */
    public static Achievement speedRunner() {
        return new Achievement("Speed Runner!",
                "Lightning fast! You escaped the mansion in record time!");
    }

    // ======== DICEOPOLY ACHIEVEMENTS ========

    /**
     * factory method for creating a board master achievement
     * awarded for reaching the end of the Diceopoly board
     * 
     * @return a board master achievement instance
     */
    public static Achievement boardMaster() {
        return new Achievement("Board Master!",
                "Excellent! You reached the end of the Diceopoly board!");
    }

    // ======== MADLIBS ACHIEVEMENTS ========

    /**
     * factory method for creating a creative writer achievement
     * awarded for completing any Mad Libs story
     * 
     * @return a creative writer achievement instance
     */
    public static Achievement creativeWriter() {
        return new Achievement("Creative Writer!",
                "Wonderful! You created an amazing story with your imagination!");
    }
}