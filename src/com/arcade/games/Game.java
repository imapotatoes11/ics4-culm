/**
 * Game.java
 *
 * abstract base class for all arcade games
 * defines common game properties and methods
 * implements ticket reward calculation system
 *
 * date: jun 15, 2025
 * author: wen zheng, kevin wang
 */
package com.arcade.games;

import java.util.*;
import com.arcade.item.Functional;

/**
 * abstract base class for all games in the arcade system
 * defines common properties like difficulty, rewards, and costs
 * provides template methods for game implementation
 */
public abstract class Game {
    private int id; // unique identifier for the game
    private String title; // display name of the game
    private int difficulty; // difficulty level from 1-10
    private int requiredTokens; // cost in tokens to play
    private int ticketReward; // base/average ticket reward
    private int minTicketReward; // minimum ticket reward possible
    private int maxTicketReward; // maximum ticket reward possible

    /**
     * constructor for creating a game with basic reward structure
     * automatically sets min/max rewards as 50% and 150% of base reward
     * 
     * @param id             unique identifier for the game
     * @param title          display name of the game
     * @param difficulty     difficulty level (1-10 scale)
     * @param requiredTokens cost in tokens to play
     * @param ticketReward   base ticket reward amount
     */
    public Game(int id, String title, int difficulty, int requiredTokens, int ticketReward) {
        this.id = id;
        this.title = title;
        this.difficulty = difficulty;
        this.requiredTokens = requiredTokens;
        this.ticketReward = ticketReward;
        // set default min/max as 50% and 150% of base reward
        this.minTicketReward = Math.max(1, ticketReward / 2);
        this.maxTicketReward = ticketReward + (ticketReward / 2);
    }

    /**
     * constructor for creating a game with explicit reward range
     * allows precise control over minimum and maximum rewards
     * 
     * @param id              unique identifier for the game
     * @param title           display name of the game
     * @param difficulty      difficulty level (1-10 scale)
     * @param requiredTokens  cost in tokens to play
     * @param minTicketReward minimum tickets that can be won
     * @param maxTicketReward maximum tickets that can be won
     */
    public Game(int id, String title, int difficulty, int requiredTokens, int minTicketReward, int maxTicketReward) {
        this.id = id;
        this.title = title;
        this.difficulty = difficulty;
        this.requiredTokens = requiredTokens;
        this.minTicketReward = minTicketReward;
        this.maxTicketReward = maxTicketReward;
        this.ticketReward = (minTicketReward + maxTicketReward) / 2; // calculate average
    }

    /**
     * abstract method that must be implemented by all game subclasses
     * defines the main game logic and user interaction
     * 
     * @param useItems list of functional items the player can use during the game
     * @return number of tickets won from playing the game
     */
    public abstract int runGame(ArrayList<Functional> useItems);

    /**
     * calculates ticket reward based on player performance
     * uses linear interpolation between min and max rewards
     * 
     * @param performanceScore a score from 0.0 (worst) to 1.0 (best performance)
     * @return number of tickets to award based on performance
     */
    public int calculateTicketReward(double performanceScore) {
        // clamp performance score between 0 and 1 to prevent errors
        performanceScore = Math.max(0.0, Math.min(1.0, performanceScore));

        // linear interpolation between min and max rewards
        int rewardRange = maxTicketReward - minTicketReward;
        int bonusTickets = (int) Math.round(rewardRange * performanceScore);

        return minTicketReward + bonusTickets;
    }

    /**
     * gets the game's unique identifier
     * 
     * @return the game id
     */
    public int getId() {
        return id;
    }

    /**
     * sets the game's unique identifier
     * 
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * gets the game's display title
     * 
     * @return the game title
     */
    public String getTitle() {
        return title;
    }

    /**
     * sets the game's display title
     * 
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * gets the game's difficulty level
     * 
     * @return the difficulty (1-10 scale)
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * sets the game's difficulty level
     * validates that difficulty is within acceptable range
     * 
     * @param difficulty the difficulty to set (must be 1-10)
     * @throws IllegalArgumentException if difficulty is not between 1 and 10
     */
    public void setDifficulty(int difficulty) {
        if (difficulty < 1 || difficulty > 10) {
            throw new IllegalArgumentException("Difficulty must be between 1 and 10.");
        }
        this.difficulty = difficulty;
    }

    /**
     * gets the number of tokens required to play this game
     * 
     * @return the required tokens
     */
    public int getRequiredTokens() {
        return requiredTokens;
    }

    /**
     * sets the number of tokens required to play this game
     * validates that tokens cannot be negative
     * 
     * @param requiredTokens the tokens to set
     * @throws IllegalArgumentException if required tokens is negative
     */
    public void setRequiredTokens(int requiredTokens) {
        if (requiredTokens < 0) {
            throw new IllegalArgumentException("Required tokens cannot be negative.");
        }
        this.requiredTokens = requiredTokens;
    }

    /**
     * gets the base/average ticket reward for this game
     * 
     * @return the base ticket reward
     */
    public int getTicketReward() {
        return ticketReward;
    }

    /**
     * sets the base/average ticket reward for this game
     * validates that reward cannot be negative
     * 
     * @param ticketReward the ticket reward to set
     * @throws IllegalArgumentException if ticket reward is negative
     */
    public void setTicketReward(int ticketReward) {
        if (ticketReward < 0) {
            throw new IllegalArgumentException("Ticket reward cannot be negative.");
        }
        this.ticketReward = ticketReward;
    }

    /**
     * gets the minimum ticket reward possible for this game
     * 
     * @return the minimum ticket reward
     */
    public int getMinTicketReward() {
        return minTicketReward;
    }

    /**
     * sets the minimum ticket reward possible for this game
     * validates that reward cannot be negative
     * 
     * @param minTicketReward the minimum ticket reward to set
     * @throws IllegalArgumentException if minimum ticket reward is negative
     */
    public void setMinTicketReward(int minTicketReward) {
        if (minTicketReward < 0) {
            throw new IllegalArgumentException("Minimum ticket reward cannot be negative.");
        }
        this.minTicketReward = minTicketReward;
    }

    /**
     * gets the maximum ticket reward possible for this game
     * 
     * @return the maximum ticket reward
     */
    public int getMaxTicketReward() {
        return maxTicketReward;
    }

    /**
     * sets the maximum ticket reward possible for this game
     * validates that reward cannot be negative
     * 
     * @param maxTicketReward the maximum ticket reward to set
     * @throws IllegalArgumentException if maximum ticket reward is negative
     */
    public void setMaxTicketReward(int maxTicketReward) {
        if (maxTicketReward < 0) {
            throw new IllegalArgumentException("Maximum ticket reward cannot be negative.");
        }
        this.maxTicketReward = maxTicketReward;
    }

    /**
     * gets a string representation of the ticket reward range
     * shows single value if min equals max, otherwise shows range
     * 
     * @return string representation of reward range (e.g., "10-30" or "15")
     */
    public String getTicketRewardRange() {
        if (minTicketReward == maxTicketReward) {
            return String.valueOf(minTicketReward);
        }
        return minTicketReward + "-" + maxTicketReward;
    }
}
