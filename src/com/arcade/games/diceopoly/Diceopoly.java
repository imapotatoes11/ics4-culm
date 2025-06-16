/**
 * Diceopoly.java
 *
 * implementation of a dice-based board game for the arcade system
 * players roll dice to move around a board collecting tickets
 * features variable board length and dice count based on difficulty
 * includes support for luck and ticket multiplier items
 *
 * date: jun 15, 2025
 * author: wen zheng, refactored by kevin wang
 */
package com.arcade.games.diceopoly;

import com.arcade.games.Game;
import com.arcade.item.Functional;
import com.arcade.item.TicketMultiplier;
import com.arcade.item.Luck;
import com.arcade.item.AchievementChecker;
import com.arcade.item.Achievement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Diceopoly extends Game {
    Scanner sc = new Scanner(System.in);
    Random rand = new Random();

    private int pos = 0; // The users position on the board
    private int diceCount = 7; // The number of dice the user can roll
    private int boardLength = 20; // The length of the board array
    private String[] board; // The board the user is on
    private int dice; // Your dice roll
    private int ticketMultiplier = 1; // The multipler for extra tickets
    private final int JACKPOT = 20; // Ticket reward for landing on final space

    public Diceopoly() {
        // id = 3, title = Diceopoly, difficulty = 3, requiredTokens = 15, ticketReward=
        // 20
        super(3, "Diceopoly", 10, 15, 0);
    }

    public Diceopoly(int id, String title, int difficulty, int requiredTokens, int ticketReward) {
        super(id, title, difficulty, requiredTokens, ticketReward);
    }

    public static void main(String[] args) {
        Diceopoly game = new Diceopoly();
        ArrayList<Functional> items = new ArrayList<>();
        int earned = game.runGame(items);
        System.out.printf("Game Over! You finished at position %d and earned %d tickets!%n", game.pos, earned);
    }

    public int runGame(ArrayList<Functional> items) {
        // Activates items
        for (Functional f : items) {
            // If user uses luck item, decreases difficulty.
            if (f instanceof Luck) {
                f.activate();
                if (getDifficulty() > 3) {
                    setDifficulty(getDifficulty() - 2);
                } else {
                    setDifficulty(1);
                }
                // If user uses ticketmultiplier item, increases the ticket multiplier
                // And by extensuon the num of tickets they win
            } else if (f instanceof TicketMultiplier) {
                f.activate();
                ticketMultiplier = TicketMultiplier.MULTIPLIER;
                // If any other item is used, states that the item is unusable
            } else {
                System.out.println("Sorry, you can't use this power up for Diceopoly.");
            }
        }

        // Factoring in difficulty into the game
        // The more difficult it is, the longer the board is
        boardLength += 2 * getDifficulty();
        // Initialize the board array now that boardLength is finalized
        board = new String[boardLength];

        generateBoard();
        printBoard();
        moving();

        // Calculate performance-based ticket reward
        double performanceScore = calculatePerformanceScore();
        int baseTickets = calculateTicketReward(performanceScore);
        int finalTickets = ticketMultiplier * baseTickets;

        boolean gameWon = pos >= boardLength - 1;
        if (gameWon) {
            System.out.println("Congratulations! You reached the end.");
            System.out.println("🎉 VICTORY! You earned " + finalTickets + " tickets!");
        } else {
            System.out.println("You ran out of dice before finishing.");
            System.out.println("You earned " + finalTickets + " tickets for reaching position " + pos + ".");
        }

        // Check and display achievements
        List<Achievement> achievements = new ArrayList<>();
        achievements
                .addAll(AchievementChecker.checkGeneralAchievements(gameWon, performanceScore, getDifficulty(), false));
        achievements.addAll(AchievementChecker.checkDiceopolyAchievements(pos, boardLength));
        AchievementChecker.displayAchievements(achievements);

        return finalTickets;
    }

    /**
     * Calculate performance score based on distance traveled and completion
     */
    private double calculatePerformanceScore() {
        // Base score from distance traveled (0.0 to 0.6)
        double distanceScore = (double) pos / (boardLength - 1) * 0.6;

        // Completion bonus for reaching the end (0.0 to 0.3)
        double completionBonus = (pos >= boardLength - 1) ? 0.3 : 0.0;

        // Efficiency bonus: remaining dice when completed (0.0 to 0.1)
        double efficiencyBonus = 0.0;
        if (pos >= boardLength - 1 && diceCount > 0) {
            // More remaining dice = better efficiency
            efficiencyBonus = Math.min(0.1, diceCount * 0.02);
        }

        return distanceScore + completionBonus + efficiencyBonus;
    }

    public void moving() {
        while (diceCount > 0 && pos < boardLength - 1) {
            System.out.print("Enter anything to roll a dice!: ");
            String input = sc.nextLine();

            if (input != null) {
                dice = rollDice();
                System.out.println("You rolled a " + dice);

                // Move one tile at a time
                for (int step = 0; step < dice && pos < boardLength - 1; step++) {
                    pos++;
                    printBoard();

                    // End early if somehow moved to last tile
                    if (pos >= boardLength - 1) {
                        System.out.println("You reached the end of the board. You Win!");
                        return;
                    }
                }

                String tile = board[pos];

                if (tile.contains("Dice")) {
                    moveDice(tile);
                } else if (tile.contains("Move")) {
                    moveSpace(tile);
                } else {
                    emptySpace();
                }
                System.out.println("Current Position: " + pos);
                System.out.println("Remaining Dice: " + diceCount);

                diceCount--;
            }
        }

        System.out.println("Game Over! You finished at position " + pos);
    }

    public void printBoard() {
        StringBuilder view = new StringBuilder("|  Player  |");

        // never go past boardLength
        int end = Math.min(boardLength, pos + 5);
        for (int i = pos + 1; i < end; i++) {
            view.append(board[i]).append(" ");
        }

        System.out.println("Board View: " + view);
    }

    public int rollDice() {
        return (rand.nextInt(1, 7));
    }

    public void generateBoard() {
        for (int i = 0; i < boardLength; i++) {
            board[i] = generateTile();
        }
        // Edits the final tile to be a normal tile, in case a soft lock is caused
        board[boardLength - 1] = "|          |";
    }

    // Used by generateBoard to generate a singular tile
    public String generateTile() {
        int randomNum;
        String tile;
        randomNum = rand.nextInt(1, 9);
        switch (randomNum) {
            case 5:
                // extra dice tile
                tile = "| +" + rand.nextInt(1, 3) + " Dice  |";
                break;

            case 6:
                // minus dice tile
                tile = "| -" + rand.nextInt(1, 3) + " Dice  |";
                break;

            case 7:
                // move forward tile
                tile = "|Move -> " + rand.nextInt(1, 4) + " |";
                break;

            case 8:
                // move back tile
                tile = "|Move <- " + rand.nextInt(1, 4) + " |";
                break;
            default:
                // Empty tile
                tile = "|          |";
        }
        return (tile);
    }

    public void emptySpace() {
        System.out.println("Landed on an empty tile. Nothing happens.");
    }

    // Triggers when player lands on move space. Moves the user forwards or back
    public void moveSpace(String tile) {
        int move = Integer.parseInt(tile.replaceAll("[^0-9]", ""));

        if (tile.contains("->")) {
            System.out.println("Moving forward by " + move + " tiles!");
            pos = Math.min(pos + move, boardLength - 1);
        } else if (tile.contains("<-")) {
            System.out.println("Moving backward by " + move + " tiles!");
            pos = Math.max(pos - move, 0);
        }
    }

    public void moveDice(String tile) {
        int amount = Integer.parseInt(tile.replaceAll("[^0-9]", ""));
        if (tile.contains("+")) {
            System.out.println("Gained " + amount + " extra dice!");
            diceCount += amount;
        } else if (tile.contains("-")) {
            System.out.println("Lost " + amount + " dice!");
            if (diceCount < amount) {
                diceCount = 0;
            } else {
                diceCount -= amount;
            }
        }
    }

}
