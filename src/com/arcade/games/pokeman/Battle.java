/**
 * Battle.java
 *
 * manages turn-based combat between two pokemans
 * handles player input, enemy ai, and battle flow
 * provides difficulty-based ai behavior and visual battle interface
 *
 * date: jun 15, 2025
 * author: kevin wang
 */
package com.arcade.games.pokeman;

import java.util.*;

import com.arcade.util.Bcolors;

/**
 * manages turn-based battle mechanics between player and enemy pokemans
 * handles user interface, ai decision making, and battle flow
 * provides dynamic difficulty scaling for enemy behavior
 */
public class Battle {
    private Pokeman player; // the player's pokeman
    private Pokeman enemy; // the enemy pokeman
    private int turnNumber; // current turn counter for tracking battle progress
    private int difficulty; // difficulty level affecting enemy ai behavior
    private Random random; // random number generator for ai decisions
    private Scanner scanner; // handles user input during battle

    // styling constants for consistent console output formatting
    private static final String STYLE_HEADER = Bcolors.BOLD + Bcolors.BRIGHT_CYAN;
    private static final String STYLE_INFO = Bcolors.OKCYAN;
    private static final String STYLE_WARNING = Bcolors.WARNING;
    private static final String STYLE_SUCCESS = Bcolors.OKGREEN;
    private static final String STYLE_ERROR = Bcolors.FAIL;
    private static final String STYLE_END = Bcolors.ENDC;

    /**
     * constructor for creating a battle between two pokemans
     * 
     * @param player     the player's pokeman
     * @param enemy      the enemy pokeman
     * @param difficulty the difficulty level (affects enemy ai behavior)
     */
    public Battle(Pokeman player, Pokeman enemy, int difficulty) {
        this.player = player;
        this.enemy = enemy;
        this.difficulty = difficulty;
        this.turnNumber = 1; // battles start at turn 1
        this.random = new Random();
        this.scanner = new Scanner(System.in);
    }

    /**
     * initiates and manages the main battle loop
     * alternates between player and enemy turns until one is defeated
     * 
     * @return true if player wins, false if player loses
     */
    public boolean startBattle() {
        System.out.println(STYLE_HEADER + "\n========================================");
        System.out.println("           BATTLE BEGINS!");
        System.out.println("========================================" + STYLE_END);
        System.out.println(STYLE_INFO + player.getName() + " VS " + enemy.getName() + STYLE_END);

        if (enemy.isBoss()) {
            System.out.println(STYLE_ERROR + "*** BOSS BATTLE ***" + STYLE_END);
        }

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();

        // main battle loop - continues until one pokeman is defeated
        while (!player.isDefeated() && !enemy.isDefeated()) {
            displayBattleScreen();

            // player's turn
            System.out.println(STYLE_SUCCESS + "\n--- YOUR TURN ---" + STYLE_END);
            if (playerTurn()) {
                break; // battle ended
            }

            if (enemy.isDefeated()) {
                break; // enemy defeated
            }

            // enemy's turn
            System.out.println(STYLE_ERROR + "\n--- ENEMY TURN ---" + STYLE_END);
            if (enemyTurn()) {
                break; // battle ended
            }

            turnNumber++;
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }

        return displayBattleResult();
    }

    /**
     * handles the player's turn including energy regeneration and move selection
     * allows player to choose from available moves and execute them
     * 
     * @return true if battle should end, false to continue
     */
    private boolean playerTurn() {
        // regenerate energy at start of turn (standard pokeman battle mechanic)
        player.regenerateEnergy();

        ArrayList<Move> availableMoves = player.getAvailableMoves();

        if (availableMoves.isEmpty()) {
            System.out.println(STYLE_ERROR + "No moves available! You must pass this turn." + STYLE_END);
            return false;
        }

        displayMoveMenu();
        Move selectedMove = getUserMoveChoice();

        if (selectedMove != null) {
            player.useMove(selectedMove, enemy);
        }

        return false; // battle continues
    }

    /**
     * handles the enemy's turn with difficulty-based ai behavior
     * enemy ai becomes smarter and more strategic at higher difficulties
     * 
     * @return true if battle should end, false to continue
     */
    private boolean enemyTurn() {
        // regenerate energy at start of turn
        enemy.regenerateEnergy();

        // simple ai: select a move based on difficulty level
        ArrayList<Move> availableMoves = enemy.getAvailableMoves();

        if (availableMoves.isEmpty()) {
            System.out
                    .println(STYLE_INFO + enemy.getName() + " has no available moves and passes the turn." + STYLE_END);
            return false;
        }

        Move selectedMove;

        // difficulty-based ai: higher difficulty = smarter move selection
        if (difficulty >= 7) {
            // high difficulty: always choose the highest damage move available
            selectedMove = availableMoves.get(0);
            for (Move move : availableMoves) {
                if (move.getBaseDamage() > selectedMove.getBaseDamage()) {
                    selectedMove = move;
                }
            }
        } else if (difficulty >= 4) {
            // medium difficulty: 70% chance for highest damage move, 30% random
            if (random.nextDouble() < 0.7) {
                selectedMove = availableMoves.get(0);
                for (Move move : availableMoves) {
                    if (move.getBaseDamage() > selectedMove.getBaseDamage()) {
                        selectedMove = move;
                    }
                }
            } else {
                selectedMove = availableMoves.get(random.nextInt(availableMoves.size()));
            }
        } else {
            // low difficulty: completely random move selection
            selectedMove = availableMoves.get(random.nextInt(availableMoves.size()));
        }

        enemy.useMove(selectedMove, player, difficulty);

        return false; // battle continues
    }

    /**
     * displays the main battle interface with pokeman art and stats
     * shows both pokemans side by side with health and energy bars
     */
    private void displayBattleScreen() {
        // clear screen effect for better presentation
        System.out.println("\n".repeat(3));

        System.out.println(STYLE_HEADER + "===========================================");
        System.out.printf("           POKEMAN BATTLE - TURN %d\n", turnNumber);
        System.out.println("===========================================" + STYLE_END);
        System.out.println();

        // display pokemans side by side with ascii art
        String[] playerArt = player.getAsciiArt().split("\n");
        String[] enemyArt = enemy.getAsciiArt().split("\n");

        // pokeman names at the top
        System.out.printf("    %-20s          VS          %s\n",
                player.getName().toUpperCase(), enemy.getName().toUpperCase());

        // ascii art side by side (handles different art heights)
        int maxLines = Math.max(playerArt.length, enemyArt.length);
        for (int i = 0; i < maxLines; i++) {
            String leftSide = i < playerArt.length ? playerArt[i] : "                ";
            String rightSide = i < enemyArt.length ? enemyArt[i] : "";
            System.out.printf("%-20s                      %s\n", leftSide, rightSide);
        }

        System.out.println();

        // stats display with visual bars
        System.out.print("HP: ");
        displayHpBar(player);
        System.out.printf("        HP: ");
        displayHpBar(enemy);
        System.out.println();

        System.out.print("EN: ");
        displayEnergyBar(player);
        System.out.printf("                  EN: ");
        displayEnergyBar(enemy);
        System.out.println();

        System.out.println(STYLE_HEADER + "===========================================" + STYLE_END);
    }

    /**
     * displays a visual health bar for a pokeman
     * uses colored block characters to show current vs maximum health
     * 
     * @param pokeman the pokeman whose health bar to display
     */
    private void displayHpBar(Pokeman pokeman) {
        int maxBarLength = 12; // length of the health bar in characters
        int currentHp = pokeman.getCurrentHp();
        int maxHp = pokeman.getMaxHp();

        // calculate how much of the bar should be filled
        int filledLength = (int) ((double) currentHp / maxHp * maxBarLength);
        int emptyLength = maxBarLength - filledLength;

        // create visual bar with color coding
        String colorCode = currentHp > maxHp * 0.6 ? Bcolors.BRIGHT_GREEN
                : currentHp > maxHp * 0.3 ? Bcolors.BRIGHT_YELLOW : Bcolors.BRIGHT_RED;

        System.out.print(colorCode + "█".repeat(filledLength) +
                Bcolors.BRIGHT_BLACK + "░".repeat(emptyLength) + Bcolors.ENDC +
                String.format(" %d/%d", currentHp, maxHp));
    }

    /**
     * displays a visual energy bar for a pokeman
     * uses colored block characters to show current vs maximum energy
     * 
     * @param pokeman the pokeman whose energy bar to display
     */
    private void displayEnergyBar(Pokeman pokeman) {
        int maxBarLength = 6; // length of the energy bar in characters
        int currentEnergy = pokeman.getCurrentEnergy();
        int maxEnergy = pokeman.getMaxEnergy();

        // calculate how much of the bar should be filled
        int filledLength = (int) ((double) currentEnergy / maxEnergy * maxBarLength);
        int emptyLength = maxBarLength - filledLength;

        // create visual bar with blue color scheme
        System.out.print(Bcolors.BRIGHT_BLUE + "█".repeat(filledLength) +
                Bcolors.BRIGHT_BLACK + "░".repeat(emptyLength) + Bcolors.ENDC +
                String.format(" %d/%d", currentEnergy, maxEnergy));
    }

    /**
     * displays the menu of available moves for the player
     * shows move names, energy costs, and descriptions
     */
    private void displayMoveMenu() {
        ArrayList<Move> availableMoves = player.getAvailableMoves();

        System.out.println(STYLE_INFO + "\nChoose your move:" + STYLE_END);
        for (int i = 0; i < availableMoves.size(); i++) {
            Move move = availableMoves.get(i);
            System.out.printf("  %d. %s\n", i + 1, move.getDisplayString());
        }
        System.out.print("Enter move number (1-" + availableMoves.size() + "): ");
    }

    /**
     * gets the player's move choice from user input
     * validates input and returns the selected move
     * 
     * @return the selected move, or null if invalid input
     */
    private Move getUserMoveChoice() {
        ArrayList<Move> availableMoves = player.getAvailableMoves();

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice >= 1 && choice <= availableMoves.size()) {
                return availableMoves.get(choice - 1);
            } else {
                System.out.println(STYLE_ERROR + "Invalid choice! Using first available move." + STYLE_END);
                return availableMoves.get(0); // fallback to first move
            }
        } catch (NumberFormatException e) {
            System.out.println(STYLE_ERROR + "Invalid input! Using first available move." + STYLE_END);
            return availableMoves.get(0); // fallback to first move
        }
    }

    /**
     * displays the battle result and determines the winner
     * shows final stats and returns battle outcome
     * 
     * @return true if player wins, false if player loses
     */
    private boolean displayBattleResult() {
        System.out.println(STYLE_HEADER + "\n========================================");
        System.out.println("           BATTLE COMPLETE!");
        System.out.println("========================================" + STYLE_END);

        if (player.isDefeated()) {
            System.out.println(STYLE_ERROR + "💀 " + player.getName() + " has been defeated!" + STYLE_END);
            System.out.println(STYLE_ERROR + enemy.getName() + " wins the battle!" + STYLE_END);
            return false; // player lost
        } else {
            System.out.println(STYLE_SUCCESS + "🎉 " + enemy.getName() + " has been defeated!" + STYLE_END);
            System.out.println(STYLE_SUCCESS + player.getName() + " wins the battle!" + STYLE_END);
            return true; // player won
        }
    }
}
