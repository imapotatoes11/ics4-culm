/*
 * PokemanGame.java
 *
 * Date: 05 30, 2025
 *
 * Copyright 2025 Kevin Wang
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the license at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the license is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * license for the specific language governing permissions and limitations under
 * the license.
 */
package com.arcade.games.pokeman;

import java.util.*;
import com.arcade.games.Game;
import com.arcade.util.Bcolors;
import com.arcade.item.*;

public class PokemanGame extends Game {
    // TODO: ADJUST DIFFICULTY: DIFFICULTY 5 IS STILL NOT POSSIBLE
    private Pokeman playerPokeman;
    private Scanner scanner;

    // Styling constants
    private static final String STYLE_TITLE = Bcolors.BOLD + Bcolors.OKGREEN;
    private static final String STYLE_INFO = Bcolors.OKCYAN;
    private static final String STYLE_WARNING = Bcolors.WARNING;
    private static final String STYLE_ERROR = Bcolors.FAIL;
    private static final String STYLE_HEADER = Bcolors.BOLD + Bcolors.BRIGHT_CYAN;
    private static final String STYLE_WIN_HEADER = Bcolors.BOLD_GREEN;
    private static final String STYLE_LOSE_HEADER = Bcolors.BOLD_RED;
    private static final String STYLE_END = Bcolors.ENDC;

    public PokemanGame() {
        // Default constructor with preset values
        // for testing purposes
        super(2, "Pokeman Adventure", 7, 15, 50);
        this.scanner = new Scanner(System.in);
    }

    public PokemanGame(int id, String title, int difficulty, int requiredTokens, int ticketReward) {
        super(id, title, difficulty, requiredTokens, ticketReward);
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        // Main method to run the game
        // This is just a placeholder for testing purposes
        PokemanGame game = new PokemanGame();
        ArrayList<Functional> useItems = new ArrayList<>(); // Placeholder for items
        useItems.add(new Luck("luck", 1, 10, 2));
        useItems.add(new ExtraLife("extra life", 2, 10));
        game.runGame(useItems);
    }

    @Override
    public int runGame(ArrayList<Functional> useItems) {
        // Clear screen
        String lines = System.getProperty("LINES");
        System.out.println("\n".repeat(lines != null ? Integer.parseInt(lines) : 20));

        displayGameIntro();

        // determine effective difficulty, apply Luck items
        int difficulty = this.getDifficulty();
        for (Functional item : useItems) {
            if (item instanceof Luck && item.getNumUses() > 0) {
                int factor = ((Luck) item).getDifficultyDecreaseFactor();
                difficulty = Math.max(1, difficulty - factor);
                item.setNumUses(item.getNumUses() - 1);
                System.out.println(STYLE_INFO + "Luck used! Difficulty reduced to " +
                        difficulty + STYLE_END);
            }
        }

        initializeGame();

        int battlesWon = 0;
        int totalBattles = 4;

        // Battle through 4 enemies
        battleLoop: for (int battleNumber = 1; battleNumber <= totalBattles; battleNumber++) {
            Pokeman enemy = Pokeman.createEnemyPokeman(battleNumber, difficulty);
            displayBattleIntro(battleNumber, enemy);

            if (!startBattle(enemy, difficulty)) {
                // try ExtraLife
                for (Functional item : useItems) {
                    if (item instanceof ExtraLife && item.getNumUses() > 0) {
                        item.setNumUses(item.getNumUses() - 1);
                        playerPokeman.heal(playerPokeman.getMaxHp());
                        System.out.println(STYLE_WARNING +
                                "Extra Life used! " + playerPokeman.getName() +
                                " is revived at full HP." + STYLE_END);
                        battleNumber--; // retry same battle
                        continue battleLoop; // jumps back to the next iteration of the outer loop
                        // TODO: DONT USE CONTINUE
                    }
                }
                displayDefeatScreen();
                return 0; // No tickets for losing
            }

            battlesWon++;
            displayBattleVictory(battleNumber, enemy);
            if (battleNumber < totalBattles)
                healPlayerPokeman();
        }

        // Calculate performance-based ticket reward
        double performanceScore = calculatePerformanceScore(battlesWon, totalBattles);
        int baseTickets = calculateTicketReward(performanceScore);

        // apply TicketMultiplier after base calculation
        int finalTickets = baseTickets;
        for (Functional item : useItems) {
            if (item instanceof TicketMultiplier && item.getNumUses() > 0) {
                int mult = TicketMultiplier.MULTIPLIER;
                finalTickets *= mult;
                item.setNumUses(item.getNumUses() - 1);
                System.out.println(STYLE_INFO + "Ticket Multiplier used! " +
                        "Your tickets x" + mult + STYLE_END);
            }
        }

        displayVictoryScreen(finalTickets);
        return finalTickets;
    }

    /**
     * Calculate performance score based on battles won and remaining health
     */
    private double calculatePerformanceScore(int battlesWon, int totalBattles) {
        // Base score from battles won (0.0 to 0.7)
        double battleScore = (double) battlesWon / totalBattles * 0.7;

        // Bonus for completing all battles (0.0 to 0.2)
        double completionBonus = (battlesWon == totalBattles) ? 0.2 : 0.0;

        // Health bonus: remaining health percentage (0.0 to 0.1)
        double healthBonus = 0.0;
        if (battlesWon == totalBattles && playerPokeman != null) {
            double healthPercentage = (double) playerPokeman.getCurrentHp() / playerPokeman.getMaxHp();
            healthBonus = healthPercentage * 0.1;
        }

        return battleScore + completionBonus + healthBonus;
    }

    private void displayGameIntro() {
        System.out.println(STYLE_TITLE + "╔══════════════════════════════════════════════╗");
        System.out.println("║              POKEMAN ADVENTURE              ║");
        System.out.println("╚══════════════════════════════════════════════╝" + STYLE_END);
        System.out.println(Bcolors.ITALIC + "*Gotta poke 'em all!*" + STYLE_END);
        System.out.println();
        System.out.println(STYLE_INFO + "Welcome, Pokeman Trainer!" + STYLE_END);
        System.out.println(
                STYLE_INFO + "Your mission: Defeat 3 normal Pokemans and 1 BOSS to become champion!" + STYLE_END);
        System.out.println();
        System.out.println(STYLE_WARNING + "Combat Rules:" + STYLE_END);
        System.out.println("• You and enemies take turns attacking");
        System.out.println("• Each Pokeman has HP (health) and Energy");
        System.out.println("• Moves cost energy - you regenerate 1 energy per turn");
        System.out.println("• Reduce enemy HP to 0 to win the battle");
        System.out.println("• If your HP reaches 0, you lose the game!");
        System.out.println();
        System.out.println(STYLE_WARNING + "Difficulty Level: " + this.getDifficulty() + STYLE_END);
        System.out.println();
        System.out.print(STYLE_TITLE + "Press Enter to begin your adventure..." + STYLE_END);
        scanner.nextLine();
    }

    private void initializeGame() {
        playerPokeman = Pokeman.createPlayerPokeman();
        System.out.println(
                STYLE_INFO + "\nYour Pokeman " + playerPokeman.getName() + " is ready for battle!" + STYLE_END);
    }

    private void displayBattleIntro(int battleNumber, Pokeman enemy) {
        System.out.println(STYLE_HEADER + "\n══════════════════════════════════════════════");
        if (battleNumber == 4) {
            System.out.println("                FINAL BOSS BATTLE!");
            System.out.println("══════════════════════════════════════════════" + STYLE_END);
            System.out.println(STYLE_ERROR + "A wild " + enemy.getName() + " appears!" + STYLE_END);
            System.out.println(STYLE_ERROR + "This is the ultimate challenge!" + STYLE_END);
        } else {
            System.out.printf("                  BATTLE #%d\n", battleNumber);
            System.out.println("══════════════════════════════════════════════" + STYLE_END);
            System.out.println(STYLE_WARNING + "A wild " + enemy.getName() + " appears!" + STYLE_END);
        }
        System.out.println();
        System.out.print("Press Enter to start the battle...");
        scanner.nextLine();
    }

    private boolean startBattle(Pokeman enemy, int difficulty) {
        Battle battle = new Battle(playerPokeman, enemy, difficulty);
        return battle.startBattle();
    }

    private void displayBattleVictory(int battleNumber, Pokeman enemy) {
        if (battleNumber == 4) {
            return; // Final victory handled separately
        }

        System.out.println(STYLE_WIN_HEADER + "\n🎉 VICTORY! 🎉" + STYLE_END);
        System.out.println(STYLE_INFO + enemy.getName() + " has been defeated!" + STYLE_END);
        System.out.printf(STYLE_INFO + "Battles completed: %d/4\n" + STYLE_END, battleNumber);
        System.out.println();
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }

    private void healPlayerPokeman() {
        System.out.println(STYLE_INFO + "\n✨ Your Pokeman rests and recovers some health..." + STYLE_END);

        // Difficulty-based healing: higher difficulty = less healing
        // Difficulty 1-3: 25-30 HP, Difficulty 4-6: 15-20 HP, Difficulty 7-10: 5-10 HP
        int baseHealing;
        int healingVariance;

        if (this.getDifficulty() <= 3) {
            baseHealing = 25;
            healingVariance = 5;
        } else if (this.getDifficulty() <= 6) {
            baseHealing = 15;
            healingVariance = 5;
        } else {
            baseHealing = 5;
            healingVariance = 5;
        }

        java.util.Random random = new java.util.Random();
        int healAmount = baseHealing + random.nextInt(healingVariance + 1);

        int oldHp = playerPokeman.getCurrentHp();
        int newHp = Math.min(playerPokeman.getMaxHp(), oldHp + healAmount);

        // Manually set HP using reflection or add a heal method to Pokeman
        // For now, we'll add a heal method to Pokeman class
        playerPokeman.heal(healAmount);

        System.out.println(STYLE_INFO + playerPokeman.getName() + " recovered " +
                (newHp - oldHp) + " HP! (" + oldHp + " → " + newHp + ")" + STYLE_END);
    }

    private void displayVictoryScreen() {
        displayVictoryScreen(this.getTicketReward());
    }

    private void displayVictoryScreen(int ticketsEarned) {
        System.out.println(STYLE_WIN_HEADER + "\n╔══════════════════════════════════════════════╗");
        System.out.println("║                  🏆 CHAMPION! 🏆               ║");
        System.out.println("╚══════════════════════════════════════════════╝" + STYLE_END);
        System.out.println();
        System.out.println(STYLE_TITLE + "🎊 INCREDIBLE! You've defeated all challengers! 🎊" + STYLE_END);
        System.out
                .println(STYLE_INFO + "You and " + playerPokeman.getName() + " have proven yourselves as" + STYLE_END);
        System.out.println(STYLE_INFO + "the ultimate Pokeman team!" + STYLE_END);
        System.out.println();
        System.out.println(STYLE_INFO + "Battles won: 4/4" + STYLE_END);
        System.out.println(STYLE_INFO + "Status: POKEMAN CHAMPION!" + STYLE_END);

        // Display health status for performance context
        if (playerPokeman != null) {
            System.out.println(STYLE_INFO + "Final HP: " + playerPokeman.getCurrentHp() + "/" +
                    playerPokeman.getMaxHp() + STYLE_END);
        }

        System.out.println();
        System.out.println(STYLE_WIN_HEADER + "You have earned " + Bcolors.BOLD +
                ticketsEarned + " tickets!" + STYLE_END);
        System.out.println(STYLE_WIN_HEADER + "══════════════════════════════════════════════" + STYLE_END);
    }

    private void displayDefeatScreen() {
        System.out.println(STYLE_LOSE_HEADER + "\n╔══════════════════════════════════════════════╗");
        System.out.println("║                 💀 DEFEATED 💀                ║");
        System.out.println("╚══════════════════════════════════════════════╝" + STYLE_END);
        System.out.println();
        System.out.println(STYLE_ERROR + "Your Pokeman adventure has come to an end..." + STYLE_END);
        System.out.println(STYLE_INFO + "But don't give up! Train harder and try again!" + STYLE_END);
        System.out.println();
        System.out.println(STYLE_ERROR + "You earned " + Bcolors.BOLD + "0 tickets." + STYLE_END);
        System.out.println(STYLE_LOSE_HEADER + "══════════════════════════════════════════════" + STYLE_END);
    }
}
