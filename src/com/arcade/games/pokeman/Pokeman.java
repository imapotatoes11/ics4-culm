/**
 * Pokeman.java
 *
 * represents a pokeman character in the battle game system
 * manages health, energy, moves, and battle mechanics
 * provides factory methods for creating player and enemy pokemans
 *
 * date: jun 15, 2025
 * author: kevin wang
 */
package com.arcade.games.pokeman;

import java.util.*;
import com.arcade.util.Bcolors;

/**
 * represents a pokeman character with stats and battle capabilities
 * manages health points, energy for moves, and available attacks
 * supports both player and enemy pokemans with difficulty scaling
 */
public class Pokeman {
    private String name; // display name of the pokeman
    private int maxHp; // maximum health points
    private int currentHp; // current health points
    private int maxEnergy; // maximum energy points
    private int currentEnergy; // current energy points
    private ArrayList<Move> moves; // available moves/attacks
    private boolean isBoss; // whether this is a boss enemy

    /**
     * constructor for creating a pokeman with specified stats and moves
     * 
     * @param name      the display name of the pokeman
     * @param maxHp     maximum health points
     * @param maxEnergy maximum energy points
     * @param moves     list of available moves
     * @param isBoss    whether this pokeman is a boss
     */
    public Pokeman(String name, int maxHp, int maxEnergy, ArrayList<Move> moves, boolean isBoss) {
        this.name = name;
        this.maxHp = maxHp;
        this.currentHp = maxHp; // start at full health
        this.maxEnergy = maxEnergy;
        this.currentEnergy = maxEnergy; // start at full energy
        this.moves = new ArrayList<>(moves); // create copy to avoid reference issues
        this.isBoss = isBoss;
    }

    /**
     * reduces the pokeman's current health by the specified damage amount
     * ensures health cannot go below zero
     * 
     * @param damage the amount of damage to take
     */
    public void takeDamage(int damage) {
        currentHp = Math.max(0, currentHp - damage);
    }

    /**
     * attempts to use a move against a target pokeman
     * checks energy requirements and applies damage if successful
     * 
     * @param move   the move to use
     * @param target the pokeman to attack
     * @return true if move was used successfully, false if insufficient energy
     */
    public boolean useMove(Move move, Pokeman target) {
        if (!move.canAfford(currentEnergy)) {
            System.out.println(Bcolors.FAIL + name + " doesn't have enough energy!" + Bcolors.ENDC);
            return false;
        }

        // consume energy for the move
        consumeEnergy(move.getEnergyCost());

        // calculate damage using base damage calculation
        int damage = move.calculateDamage();

        // apply damage to target
        target.takeDamage(damage);

        // display battle result with colorful formatting
        System.out.println(Bcolors.BOLD + name + Bcolors.ENDC + " used " +
                Bcolors.BRIGHT_YELLOW + move.getName() + Bcolors.ENDC + "!");
        System.out.println(Bcolors.FAIL + target.getName() + " takes " + damage + " damage!" + Bcolors.ENDC);

        return true;
    }

    /**
     * attempts to use a move against a target with difficulty scaling
     * checks energy requirements and applies scaled damage if successful
     * 
     * @param move       the move to use
     * @param target     the pokeman to attack
     * @param difficulty the current difficulty level for damage scaling
     * @return true if move was used successfully, false if insufficient energy
     */
    public boolean useMove(Move move, Pokeman target, int difficulty) {
        if (!move.canAfford(currentEnergy)) {
            System.out.println(Bcolors.FAIL + name + " doesn't have enough energy!" + Bcolors.ENDC);
            return false;
        }

        // consume energy for the move
        consumeEnergy(move.getEnergyCost());

        // calculate damage with difficulty scaling applied
        int damage = move.calculateDamage(difficulty);

        // apply damage to target
        target.takeDamage(damage);

        // display battle result with colorful formatting
        System.out.println(Bcolors.BOLD + name + Bcolors.ENDC + " used " +
                Bcolors.BRIGHT_YELLOW + move.getName() + Bcolors.ENDC + "!");
        System.out.println(Bcolors.FAIL + target.getName() + " takes " + damage + " damage!" + Bcolors.ENDC);

        return true;
    }

    /**
     * regenerates one energy point if not at maximum
     * called each turn to slowly restore energy
     */
    public void regenerateEnergy() {
        if (currentEnergy < maxEnergy) {
            currentEnergy++;
        }
    }

    /**
     * reduces current energy by the specified amount
     * ensures energy cannot go below zero
     * 
     * @param amount the amount of energy to consume
     */
    public void consumeEnergy(int amount) {
        currentEnergy = Math.max(0, currentEnergy - amount);
    }

    /**
     * checks if this pokeman has been defeated (health at zero)
     * 
     * @return true if current health is zero or less
     */
    public boolean isDefeated() {
        return currentHp <= 0;
    }

    /**
     * gets a list of moves that can be used with current energy
     * filters out moves that cost more energy than available
     * 
     * @return list of moves that can currently be afforded
     */
    public ArrayList<Move> getAvailableMoves() {
        ArrayList<Move> available = new ArrayList<>();
        for (Move move : moves) {
            if (move.canAfford(currentEnergy)) {
                available.add(move);
            }
        }
        return available;
    }

    /**
     * displays the pokeman's current stats with visual bars
     * shows health and energy using colored bar graphics
     */
    public void displayStats() {
        // create visual health bar using block characters
        int hpBarLength = 12;
        int hpFilled = (int) ((double) currentHp / maxHp * hpBarLength);
        String hpBar = Bcolors.BRIGHT_GREEN + "█".repeat(hpFilled) +
                Bcolors.BRIGHT_BLACK + "░".repeat(hpBarLength - hpFilled) + Bcolors.ENDC;

        // create visual energy bar using block characters
        int energyBarLength = 4;
        int energyFilled = (int) ((double) currentEnergy / maxEnergy * energyBarLength);
        String energyBar = Bcolors.BRIGHT_BLUE + "█".repeat(energyFilled) +
                Bcolors.BRIGHT_BLACK + "░".repeat(energyBarLength - energyFilled) + Bcolors.ENDC;

        System.out.printf("HP: %s %d/%d\n", hpBar, currentHp, maxHp);
        System.out.printf("EN: %s %d/%d\n", energyBar, currentEnergy, maxEnergy);
    }

    /**
     * returns ascii art representation of the pokeman
     * different art styles for regular pokemans vs boss pokemans
     * 
     * @return string containing ascii art for this pokeman
     */
    public String getAsciiArt() {
        if (isBoss) {
            return "    ▲ ▲ ▲ ▲ ▲\n" +
                    "   ( ◉ ◉ ◉ ◉ )\n" +
                    "    \\   ∩   /\n" +
                    "     -------\n" +
                    "    /|  |  |\\\n" +
                    "   ( |  |  | )";
        } else {
            return "    ▲ ▲ ▲ ▲\n" +
                    "   ( ◉   ◉ )\n" +
                    "    \\  ∩  /\n" +
                    "     ----";
        }
    }

    /**
     * factory method for creating the player's pokeman character
     * creates a balanced pokeman with moderate stats and two moves
     * 
     * @return a pokeman suitable for player use
     */
    public static Pokeman createPlayerPokeman() {
        ArrayList<Move> playerMoves = new ArrayList<>();
        playerMoves.add(Move.createFireball()); // ranged attack move
        playerMoves.add(Move.createSlash()); // melee attack move
        return new Pokeman("Sparkles", 80, 3, playerMoves, false);
    }

    /**
     * factory method for creating enemy pokemans for each battle
     * creates enemies with increasing difficulty for battle progression
     * 
     * @param battleNumber the battle number (1-4) to determine enemy type
     * @return an enemy pokeman appropriate for the specified battle
     */
    public static Pokeman createEnemyPokeman(int battleNumber) {
        ArrayList<Move> enemyMoves = new ArrayList<>();

        // create different enemies for each battle with increasing difficulty
        switch (battleNumber) {
            case 1:
                enemyMoves.add(Move.createEnemyAttack());
                return new Pokeman("Leafy", 40, 2, enemyMoves, false);
            case 2:
                enemyMoves.add(Move.createEnemyAttack());
                return new Pokeman("Rocky", 50, 3, enemyMoves, false);
            case 3:
                enemyMoves.add(Move.createEnemyAttack());
                return new Pokeman("Watery", 60, 3, enemyMoves, false);
            case 4: // final boss battle
                enemyMoves.add(Move.createBossAttack()); // powerful boss move
                enemyMoves.add(Move.createEnemyAttack()); // regular attack option
                return new Pokeman("MEGA-DESTROYER", 100, 4, enemyMoves, true);
            default:
                // fallback for invalid battle numbers
                enemyMoves.add(Move.createEnemyAttack());
                return new Pokeman("Unknown", 30, 2, enemyMoves, false);
        }
    }

    /**
     * factory method for creating difficulty-scaled enemy pokemans
     * adjusts enemy stats based on difficulty level for balanced gameplay
     * 
     * @param battleNumber the battle number (1-4) to determine enemy type
     * @param difficulty   the difficulty level (1-10) for stat scaling
     * @return an enemy pokeman with stats adjusted for the difficulty
     */
    public static Pokeman createEnemyPokeman(int battleNumber, int difficulty) {
        ArrayList<Move> enemyMoves = new ArrayList<>();

        // base stats for each enemy type
        String[] names = { "Leafy", "Rocky", "Watery", "MEGA-DESTROYER" };
        int[] baseHp = { 40, 50, 60, 100 };
        int[] baseEnergy = { 2, 3, 3, 4 };
        boolean[] isBoss = { false, false, false, true };

        if (battleNumber < 1 || battleNumber > 4) {
            // fallback for invalid battle numbers
            enemyMoves.add(Move.createEnemyAttack());
            return new Pokeman("Unknown", 30, 2, enemyMoves, false);
        }

        int index = battleNumber - 1;

        // difficulty scaling: higher difficulty = stronger enemies
        // difficulty 1-3: 90-100% base stats, 4-6: 100-120%, 7-10: 120-150%
        double statMultiplier;
        if (difficulty <= 3) {
            statMultiplier = 0.9 + (difficulty - 1) * 0.05; // 0.9 to 1.0
        } else if (difficulty <= 6) {
            statMultiplier = 1.0 + (difficulty - 4) * 0.07; // 1.0 to 1.2
        } else {
            statMultiplier = 1.2 + (difficulty - 7) * 0.1; // 1.2 to 1.5
        }

        // apply scaling to base stats
        int scaledHp = (int) Math.round(baseHp[index] * statMultiplier);
        int scaledEnergy = Math.max(1, (int) Math.round(baseEnergy[index] * (statMultiplier * 0.8))); // energy scales
                                                                                                      // less

        // add appropriate moves based on enemy type
        if (isBoss[index]) {
            enemyMoves.add(Move.createBossAttack());
            enemyMoves.add(Move.createEnemyAttack());
        } else {
            enemyMoves.add(Move.createEnemyAttack());
        }

        return new Pokeman(names[index], scaledHp, scaledEnergy, enemyMoves, isBoss[index]);
    }

    /**
     * heals the pokeman by the specified amount
     * ensures health cannot exceed maximum health
     * 
     * @param amount the amount of health to restore
     */
    public void heal(int amount) {
        currentHp = Math.min(maxHp, currentHp + amount);
    }

    /**
     * gets the pokeman's name
     * 
     * @return the pokeman's name
     */
    public String getName() {
        return name;
    }

    /**
     * gets the pokeman's maximum health points
     * 
     * @return the maximum health
     */
    public int getMaxHp() {
        return maxHp;
    }

    /**
     * gets the pokeman's current health points
     * 
     * @return the current health
     */
    public int getCurrentHp() {
        return currentHp;
    }

    /**
     * gets the pokeman's maximum energy points
     * 
     * @return the maximum energy
     */
    public int getMaxEnergy() {
        return maxEnergy;
    }

    /**
     * gets the pokeman's current energy points
     * 
     * @return the current energy
     */
    public int getCurrentEnergy() {
        return currentEnergy;
    }

    /**
     * gets the list of all moves this pokeman knows
     * 
     * @return the list of moves
     */
    public ArrayList<Move> getMoves() {
        return moves;
    }

    /**
     * checks if this pokeman is a boss
     * 
     * @return true if this is a boss pokeman
     */
    public boolean isBoss() {
        return isBoss;
    }
}
