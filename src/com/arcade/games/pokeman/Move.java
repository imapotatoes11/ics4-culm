/**
 * Move.java
 *
 * represents a move/attack that pokemans can use in battle
 * manages damage calculation, energy costs, and move descriptions
 * provides factory methods for creating common move types
 *
 * date: jun 15, 2025
 * author: kevin wang
 */
package com.arcade.games.pokeman;

import java.util.*;

/**
 * represents a move that can be used by pokemans in battle
 * manages damage output, energy requirements, and difficulty scaling
 * provides both base and scaled damage calculation methods
 */
public class Move {
    private String name; // display name of the move
    private int energyCost; // energy required to use this move
    private int baseDamage; // minimum damage this move can deal
    private int damageVariance; // additional random damage possible
    private String description; // description of the move's effects
    private static Random random = new Random(); // shared random generator for damage calculation

    /**
     * constructor for creating a move with specified parameters
     * 
     * @param name           the display name of the move
     * @param energyCost     the energy required to use this move
     * @param baseDamage     the minimum damage this move deals
     * @param damageVariance the additional random damage possible
     * @param description    description of the move's effects
     */
    public Move(String name, int energyCost, int baseDamage, int damageVariance, String description) {
        this.name = name;
        this.energyCost = energyCost;
        this.baseDamage = baseDamage;
        this.damageVariance = damageVariance;
        this.description = description;
    }

    /**
     * calculates damage using base damage plus random variance
     * provides unpredictable damage output for dynamic battles
     * 
     * @return damage amount between baseDamage and baseDamage + damageVariance
     */
    public int calculateDamage() {
        return baseDamage + random.nextInt(damageVariance + 1);
    }

    /**
     * calculates damage with difficulty scaling applied
     * higher difficulty levels result in more consistent (higher minimum) damage
     * used primarily for enemy moves to balance gameplay
     * 
     * @param difficulty the current difficulty level (1-10)
     * @return damage amount scaled based on difficulty
     */
    public int calculateDamage(int difficulty) {
        // difficulty affects damage variance for enemies:
        // higher difficulty = more consistent (higher minimum) damage
        if (difficulty >= 7) {
            // high difficulty: 75-100% of max damage for more consistent threats
            int minDamage = (int) (baseDamage + damageVariance * 0.75);
            int maxDamage = baseDamage + damageVariance;
            return minDamage + random.nextInt(maxDamage - minDamage + 1);
        } else if (difficulty >= 4) {
            // medium difficulty: 50-100% of max damage for moderate consistency
            int minDamage = (int) (baseDamage + damageVariance * 0.5);
            int maxDamage = baseDamage + damageVariance;
            return minDamage + random.nextInt(maxDamage - minDamage + 1);
        } else {
            // low difficulty: normal variance (0-100%) for maximum unpredictability
            return calculateDamage();
        }
    }

    /**
     * checks if a pokeman has enough energy to use this move
     * 
     * @param currentEnergy the pokeman's current energy level
     * @return true if the pokeman can afford to use this move
     */
    public boolean canAfford(int currentEnergy) {
        return currentEnergy >= energyCost;
    }

    /**
     * gets a formatted display string for this move
     * includes name, energy cost, and description for menu display
     * 
     * @return formatted string showing move details
     */
    public String getDisplayString() {
        return String.format("%s (%d EN) - %s", name, energyCost, description);
    }

    /**
     * factory method for creating a fireball move
     * high damage ranged attack with significant energy cost
     * 
     * @return a fireball move suitable for player use
     */
    public static Move createFireball() {
        return new Move("Fireball", 3, 15, 5, "Deal 15-20 fire damage");
    }

    /**
     * factory method for creating a slash move
     * moderate damage melee attack with no energy cost
     * 
     * @return a slash move suitable for player use
     */
    public static Move createSlash() {
        return new Move("Slash", 0, 8, 4, "Deal 8-12 physical damage");
    }

    /**
     * factory method for creating a basic enemy attack
     * balanced damage and energy cost for normal enemies
     * 
     * @return a tackle move suitable for enemy use
     */
    public static Move createEnemyAttack() {
        return new Move("Tackle", 1, 6, 3, "Deal 6-9 damage");
    }

    /**
     * factory method for creating a powerful boss attack
     * high damage move with moderate energy cost for boss enemies
     * 
     * @return a mega blast move suitable for boss use
     */
    public static Move createBossAttack() {
        return new Move("Mega Blast", 2, 12, 6, "Deal 12-18 powerful damage");
    }

    /**
     * gets the name of this move
     * 
     * @return the move's name
     */
    public String getName() {
        return name;
    }

    /**
     * gets the energy cost of this move
     * 
     * @return the energy required to use this move
     */
    public int getEnergyCost() {
        return energyCost;
    }

    /**
     * gets the base damage of this move
     * 
     * @return the minimum damage this move deals
     */
    public int getBaseDamage() {
        return baseDamage;
    }

    /**
     * gets the description of this move
     * 
     * @return the move's description
     */
    public String getDescription() {
        return description;
    }
}
