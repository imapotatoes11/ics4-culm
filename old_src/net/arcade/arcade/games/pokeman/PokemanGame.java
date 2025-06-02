/*
 * PokemanGame.java
 *
 * Date: 05 28, 2025
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
package net.arcade.arcade.games.pokeman;

import java.util.ArrayList;
import java.util.Scanner;

import net.arcade.arcade.games.Game;
import net.arcade.arcade.item.Functional;
import net.arcade.arcade.util.Bcolors;

public class PokemanGame extends Game {
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
        super(2, "Pokeman Adventure", 3, 15, 50);
        this.scanner = new Scanner(System.in);
    }

    public PokemanGame(int id, String title, int difficulty, int requiredTokens, int ticketReward) {
        super(id, title, difficulty, requiredTokens, ticketReward);
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        PokemanGame game = new PokemanGame();
        ArrayList<Functional> useItems = new ArrayList<>(); // Placeholder for items
        game.runGame(useItems);
    }

    @Override
    public int runGame(ArrayList<Functional> useItems) {
        // Clear screen
        String lines = System.getProperty("LINES");
        System.out.println("\n".repeat(lines != null ? Integer.parseInt(lines) : 20));

        displayGameIntro();
        initializeGame();

        // Battle through 4 enemies
        for (int battleNumber = 1; battleNumber <= 4; battleNumber++) {
            Pokeman enemy = Pokeman.createEnemyPokeman(battleNumber);

            displayBattleIntro(battleNumber, enemy);

            if (!startBattle(enemy)) {
                // Player lost
                displayDefeatScreen();
                return 0; // No tickets earned
            }

            displayBattleVictory(battleNumber, enemy);

            // Heal player between battles (except after final battle)
            if (battleNumber < 4) {
                healPlayerPokeman();
            }
        }

        // Player won all battles!
        displayVictoryScreen();
        return this.getTicketReward();
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

    private boolean startBattle(Pokeman enemy) {
        Battle battle = new Battle(playerPokeman, enemy);
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

        // Restore some HP and full energy
        int healAmount = playerPokeman.getMaxHp() / 3; // Heal 1/3 of max HP
        int newHp = Math.min(playerPokeman.getMaxHp(), playerPokeman.getCurrentHp() + healAmount);

        // Create a new pokeman with restored stats (simpler than adding setters)
        playerPokeman = new Pokeman(
                playerPokeman.getName(),
                playerPokeman.getMaxHp(),
                playerPokeman.getMaxEnergy(),
                playerPokeman.getMoves(),
                playerPokeman.isBoss());

        // Manually set HP to healed amount
        int damageToTake = playerPokeman.getMaxHp() - newHp;
        if (damageToTake > 0) {
            playerPokeman.takeDamage(damageToTake);
        }

        System.out.println(STYLE_INFO + "HP restored! Energy fully recharged!" + STYLE_END);
        System.out.println();
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }

    private void displayVictoryScreen() {
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
        System.out.println();
        System.out.println(STYLE_WIN_HEADER + "You have earned " + Bcolors.BOLD +
                this.getTicketReward() + " tickets!" + STYLE_END);
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
