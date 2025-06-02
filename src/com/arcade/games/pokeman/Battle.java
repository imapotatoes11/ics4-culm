/*
 * Battle.java
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

import com.arcade.util.Bcolors;

public class Battle {
    private Pokeman player;
    private Pokeman enemy;
    private int turnNumber;
    private Random random;
    private Scanner scanner;

    // Styling constants
    private static final String STYLE_HEADER = Bcolors.BOLD + Bcolors.BRIGHT_CYAN;
    private static final String STYLE_INFO = Bcolors.OKCYAN;
    private static final String STYLE_WARNING = Bcolors.WARNING;
    private static final String STYLE_SUCCESS = Bcolors.OKGREEN;
    private static final String STYLE_ERROR = Bcolors.FAIL;
    private static final String STYLE_END = Bcolors.ENDC;

    public Battle(Pokeman player, Pokeman enemy) {
        this.player = player;
        this.enemy = enemy;
        this.turnNumber = 1;
        this.random = new Random();
        this.scanner = new Scanner(System.in);
    }

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

        // Main battle loop
        while (!player.isDefeated() && !enemy.isDefeated()) {
            displayBattleScreen();

            // Player's turn
            System.out.println(STYLE_SUCCESS + "\n--- YOUR TURN ---" + STYLE_END);
            if (playerTurn()) {
                break; // Battle ended
            }

            if (enemy.isDefeated()) {
                break; // Enemy defeated
            }

            // Enemy's turn
            System.out.println(STYLE_ERROR + "\n--- ENEMY TURN ---" + STYLE_END);
            if (enemyTurn()) {
                break; // Battle ended
            }

            turnNumber++;
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }

        return displayBattleResult();
    }

    private boolean playerTurn() {
        // Regenerate energy at start of turn
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

        return false; // Battle continues
    }

    private boolean enemyTurn() {
        // Regenerate energy at start of turn
        enemy.regenerateEnergy();

        // Simple AI: select a random available move
        ArrayList<Move> availableMoves = enemy.getAvailableMoves();

        if (availableMoves.isEmpty()) {
            System.out
                    .println(STYLE_INFO + enemy.getName() + " has no available moves and passes the turn." + STYLE_END);
            return false;
        }

        Move selectedMove = availableMoves.get(random.nextInt(availableMoves.size()));
        enemy.useMove(selectedMove, player);

        return false; // Battle continues
    }

    private void displayBattleScreen() {
        // Clear screen effect
        System.out.println("\n".repeat(3));

        System.out.println(STYLE_HEADER + "===========================================");
        System.out.printf("           POKEMAN BATTLE - TURN %d\n", turnNumber);
        System.out.println("===========================================" + STYLE_END);
        System.out.println();

        // Display pokemans side by side
        String[] playerArt = player.getAsciiArt().split("\n");
        String[] enemyArt = enemy.getAsciiArt().split("\n");

        // Pokeman names
        System.out.printf("    %-20s          VS          %s\n",
                player.getName().toUpperCase(), enemy.getName().toUpperCase());

        // ASCII art side by side
        int maxLines = Math.max(playerArt.length, enemyArt.length);
        for (int i = 0; i < maxLines; i++) {
            String leftSide = i < playerArt.length ? playerArt[i] : "                ";
            String rightSide = i < enemyArt.length ? enemyArt[i] : "";
            System.out.printf("%-20s                      %s\n", leftSide, rightSide);
        }

        System.out.println();

        // Stats display
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

    private void displayHpBar(Pokeman pokeman) {
        int barLength = 12;
        int filled = (int) ((double) pokeman.getCurrentHp() / pokeman.getMaxHp() * barLength);

        String color;
        if (pokeman.getCurrentHp() > pokeman.getMaxHp() * 0.6) {
            color = Bcolors.BRIGHT_GREEN;
        } else if (pokeman.getCurrentHp() > pokeman.getMaxHp() * 0.3) {
            color = Bcolors.BRIGHT_YELLOW;
        } else {
            color = Bcolors.BRIGHT_RED;
        }

        System.out.printf("%s%s%s%s %d/%d",
                color, "█".repeat(filled),
                Bcolors.BRIGHT_BLACK, "░".repeat(barLength - filled),
                pokeman.getCurrentHp(), pokeman.getMaxHp());
        System.out.print(Bcolors.ENDC);
    }

    private void displayEnergyBar(Pokeman pokeman) {
        int barLength = 4;
        int filled = (int) ((double) pokeman.getCurrentEnergy() / pokeman.getMaxEnergy() * barLength);

        System.out.printf("%s%s%s%s %d/%d",
                Bcolors.BRIGHT_BLUE, "█".repeat(filled),
                Bcolors.BRIGHT_BLACK, "░".repeat(barLength - filled),
                pokeman.getCurrentEnergy(), pokeman.getMaxEnergy());
        System.out.print(Bcolors.ENDC);
    }

    private void displayMoveMenu() {
        System.out.println(STYLE_INFO + "YOUR MOVES:" + STYLE_END);
        ArrayList<Move> moves = player.getMoves();

        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            String available = move.canAfford(player.getCurrentEnergy()) ? STYLE_SUCCESS : STYLE_ERROR;
            System.out.printf("%s[%d] %s%s\n", available, i + 1, move.getDisplayString(), STYLE_END);
        }
    }

    private Move getUserMoveChoice() {
        ArrayList<Move> moves = player.getMoves();

        while (true) {
            System.out.print(STYLE_WARNING + "\nChoose your move [1-" + moves.size() + "]: " + STYLE_END);
            String input = scanner.nextLine().trim();

            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= moves.size()) {
                    Move selectedMove = moves.get(choice - 1);
                    if (selectedMove.canAfford(player.getCurrentEnergy())) {
                        return selectedMove;
                    } else {
                        System.out.println(STYLE_ERROR + "Not enough energy for that move!" + STYLE_END);
                    }
                } else {
                    System.out.println(STYLE_ERROR + "Invalid choice. Please enter a number between 1 and "
                            + moves.size() + STYLE_END);
                }
            } catch (NumberFormatException e) {
                System.out.println(STYLE_ERROR + "Please enter a valid number." + STYLE_END);
            }
        }
    }

    private boolean displayBattleResult() {
        System.out.println(STYLE_HEADER + "\n===========================================");
        System.out.println("              BATTLE OVER!");
        System.out.println("===========================================" + STYLE_END);

        if (player.isDefeated()) {
            System.out.println(STYLE_ERROR + "💀 " + player.getName() + " was defeated!" + STYLE_END);
            System.out.println(STYLE_ERROR + "You lose this battle!" + STYLE_END);
            return false;
        } else {
            System.out.println(STYLE_SUCCESS + "🎉 " + enemy.getName() + " was defeated!" + STYLE_END);
            System.out.println(STYLE_SUCCESS + "You win this battle!" + STYLE_END);
            return true;
        }
    }
}
