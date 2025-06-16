/**
 * PokemanGame.java
 *
 * implementation of a pokemon-style battle game for the arcade system
 * features turn-based combat with multiple battles and boss encounters
 * includes functional item support and difficulty-based enemy scaling
 *
 * date: jun 15, 2025
 * author: kevin wang
 */
package com.arcade.games.pokeman;

import java.util.*;
import com.arcade.games.Game;
import com.arcade.util.Bcolors;
import com.arcade.item.*;
import com.arcade.item.AchievementChecker;
import com.arcade.item.Achievement;

/**
 * pokemon-style battle game extending the base Game class
 * players fight through multiple battles including a final boss
 * features dynamic difficulty adjustment and comprehensive item support
 */
public class PokemanGame extends Game {
    // TODO: ADJUST DIFFICULTY: DIFFICULTY 5 IS STILL NOT POSSIBLE
    private Pokeman playerPokeman; // the player's pokemon character
    private Scanner scanner; // handles user input throughout the game

    // Styling constants
    private static final String STYLE_TITLE = Bcolors.BOLD + Bcolors.OKGREEN;
    private static final String STYLE_INFO = Bcolors.OKCYAN;
    private static final String STYLE_WARNING = Bcolors.WARNING;
    private static final String STYLE_ERROR = Bcolors.FAIL;
    private static final String STYLE_HEADER = Bcolors.BOLD + Bcolors.BRIGHT_CYAN;
    private static final String STYLE_WIN_HEADER = Bcolors.BOLD_GREEN;
    private static final String STYLE_LOSE_HEADER = Bcolors.BOLD_RED;
    private static final String STYLE_END = Bcolors.ENDC;

    /**
     * default constructor with preset values for testing purposes
     * creates a pokeman game with moderate difficulty settings
     */
    public PokemanGame() {
        super(2, "Pokeman Adventure", 7, 15, 50);
        this.scanner = new Scanner(System.in);
    }

    /**
     * constructor for pokeman game with custom parameters
     * 
     * @param id             unique identifier for this game instance
     * @param title          display name for the game
     * @param difficulty     difficulty level (1-10, affects enemy strength)
     * @param requiredTokens cost in tokens to play
     * @param ticketReward   base reward in tickets for winning
     */
    public PokemanGame(int id, String title, int difficulty, int requiredTokens, int ticketReward) {
        super(id, title, difficulty, requiredTokens, ticketReward);
        this.scanner = new Scanner(System.in);
    }

    /**
     * main method for testing the pokeman game independently
     * creates a game instance and runs it with sample items
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        PokemanGame game = new PokemanGame();
        ArrayList<Functional> useItems = new ArrayList<>(); // placeholder for testing
        useItems.add(new Luck("luck", 1, 10, 2));
        useItems.add(new ExtraLife("extra life", 2, 10));
        game.runGame(useItems);
    }

    /**
     * main game loop for pokeman battles
     * processes items, runs through multiple battles, and calculates rewards
     * 
     * @param useItems list of functional items player can use during battles
     * @return number of tickets won based on performance
     */
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
        boolean usedExtraLife = false;

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
                        usedExtraLife = true;
                        System.out.println(STYLE_WARNING +
                                "Extra Life used! " + playerPokeman.getName() +
                                " is revived at full HP." + STYLE_END);
                        battleNumber--; // retry same battle
                        continue battleLoop; // jumps back to the next iteration of the outer loop
                        // TODO: DONT USE CONTINUE
                    }
                }
                displayDefeatScreen();

                // Check and display achievements for game over
                List<Achievement> achievements = new ArrayList<>();
                achievements.addAll(AchievementChecker.checkGeneralAchievements(false, 0.0, difficulty, usedExtraLife));
                achievements.addAll(AchievementChecker.checkPokemanAchievements(false, battlesWon, totalBattles, 0.0));
                AchievementChecker.displayAchievements(achievements);

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

        // Check and display achievements for victory
        double finalHealthPercentage = playerPokeman != null
                ? (double) playerPokeman.getCurrentHp() / playerPokeman.getMaxHp()
                : 0.0;

        List<Achievement> achievements = new ArrayList<>();
        achievements
                .addAll(AchievementChecker.checkGeneralAchievements(true, performanceScore, difficulty, usedExtraLife));
        achievements.addAll(
                AchievementChecker.checkPokemanAchievements(true, battlesWon, totalBattles, finalHealthPercentage));
        AchievementChecker.displayAchievements(achievements);

        return finalTickets;
    }

    /**
     * calculates performance score based on battles won and remaining health
     * considers completion rate, health preservation, and battle efficiency
     * 
     * @param battlesWon   number of battles successfully completed
     * @param totalBattles total number of battles in the game
     * @return performance score from 0.0 (worst) to 1.0 (best)
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

    /**
     * displays the game introduction and rules to the player
     * includes stylized header, combat rules, and difficulty information
     */
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

    /**
     * initializes the game by creating the player's pokeman
     * displays confirmation message when ready
     */
    private void initializeGame() {
        playerPokeman = Pokeman.createPlayerPokeman();
        System.out.println(
                STYLE_INFO + "\nYour Pokeman " + playerPokeman.getName() + " is ready for battle!" + STYLE_END);
    }

    /**
     * displays introduction for each battle with appropriate styling
     * differentiates between normal battles and the final boss battle
     * 
     * @param battleNumber the current battle number (1-4)
     * @param enemy        the enemy pokeman for this battle
     */
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

    /**
     * initiates and manages a single battle between player and enemy
     * delegates to the Battle class for actual combat mechanics
     * 
     * @param enemy      the enemy pokeman to fight
     * @param difficulty the current difficulty level
     * @return true if player wins, false if player loses
     */
    private boolean startBattle(Pokeman enemy, int difficulty) {
        Battle battle = new Battle(playerPokeman, enemy, difficulty);
        return battle.startBattle();
    }

    /**
     * displays victory message after winning a battle
     * different styling for normal battles vs final boss
     * 
     * @param battleNumber the battle that was just won
     * @param enemy        the enemy that was defeated
     */
    private void displayBattleVictory(int battleNumber, Pokeman enemy) {
        if (battleNumber == 4) {
            System.out.println(STYLE_WIN_HEADER + "\n🎉 FINAL BOSS DEFEATED! 🎉" + STYLE_END);
            System.out.println(STYLE_WIN_HEADER + "You have conquered " + enemy.getName() + "!" + STYLE_END);
        } else {
            System.out.println(STYLE_WIN_HEADER + "\n✅ Victory!" + STYLE_END);
            System.out.println(STYLE_WIN_HEADER + "You defeated " + enemy.getName() + "!" + STYLE_END);
        }
    }

    /**
     * heals the player's pokeman between battles
     * provides strategic healing to maintain challenge while allowing progression
     */
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

    /**
     * displays the victory screen when player completes all battles
     * shows celebration message and ticket reward information
     */
    private void displayVictoryScreen() {
        displayVictoryScreen(this.getTicketReward());
    }

    /**
     * displays the victory screen with ticket reward information
     * 
     * @param ticketsEarned the number of tickets won
     */
    private void displayVictoryScreen(int ticketsEarned) {
        System.out.println(STYLE_WIN_HEADER + "\n🏆 CONGRATULATIONS! 🏆" + STYLE_END);
        System.out.println(STYLE_WIN_HEADER + "You are now the POKEMAN CHAMPION!" + STYLE_END);
        System.out.println(STYLE_WIN_HEADER + "You have defeated all enemies and proven your worth!" + STYLE_END);
        System.out.println();
        System.out.println(STYLE_WIN_HEADER + "🎫 Tickets Earned: " + ticketsEarned + STYLE_END);
        System.out.println();
        System.out.println(STYLE_INFO + "Final Stats:" + STYLE_END);
        System.out.println(STYLE_INFO + "- " + playerPokeman.getName() + " HP: " +
                playerPokeman.getCurrentHp() + "/" + playerPokeman.getMaxHp() + STYLE_END);
        System.out.println(STYLE_INFO + "- Battles Won: 4/4" + STYLE_END);
        System.out.println(STYLE_INFO + "- Difficulty: " + this.getDifficulty() + STYLE_END);
        System.out.println();
        System.out.println(STYLE_WIN_HEADER + "Thank you for playing Pokeman Adventure!" + STYLE_END);
    }

    /**
     * displays the defeat screen when player loses
     * shows encouragement message and suggests trying again
     */
    private void displayDefeatScreen() {
        System.out.println(STYLE_LOSE_HEADER + "\n💔 GAME OVER 💔" + STYLE_END);
        System.out.println(STYLE_LOSE_HEADER + "Your Pokeman has been defeated..." + STYLE_END);
        System.out.println();
        System.out.println(STYLE_INFO + "Don't give up! Try again and become stronger!" + STYLE_END);
        System.out.println(STYLE_INFO + "Consider using items to help you on your journey." + STYLE_END);
        System.out.println();
        System.out.println(STYLE_LOSE_HEADER + "Better luck next time, trainer!" + STYLE_END);
    }
}
