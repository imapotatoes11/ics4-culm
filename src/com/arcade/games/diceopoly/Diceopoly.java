package com.arcade.games.diceopoly;

import java.util.*;
import com.arcade.games.Game;
import com.arcade.item.Functional;

public class Diceopoly extends Game {
    private int pos = 0; // The users position on the board
    private int diceCount = 7; // The number of dice the user can roll
    private int boardLength = 30; // The length of the board array
    private String[] board = new String[boardLength]; // The board the user is on
    private int dice; // Your dice roll
    Scanner sc = new Scanner(System.in);
    Random rand = new Random();

    // ANSI color codes for UI
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";

    public Diceopoly(int id, String title, int difficulty, int requiredTokens, int ticketReward) {
        super(id, title, difficulty, requiredTokens, ticketReward);
    }

    public Diceopoly() {
        super(1, "Diceopoly", 3, 5, 10); // Example values for the constructor
    }

    public int runGame(ArrayList<Functional> useItems) {
        System.out.println("Welcome to Diceopoly!");
        System.out.println("You have " + diceCount + " dice to roll.");
        System.out.println("Reach the end of the board to win!");

        generateBoard();
        printBoard();
        moving();

        return 0;
        // TODO: RETURN TICKET REWARDS
    }

    public static void main(String[] args) {
        Diceopoly game = new Diceopoly();
        game.generateBoard();
        game.printBoard();
        game.moving();
        System.out.println("Game Over! You finished at position " + game.pos);
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

    // Updated printBoard to clear screen, show status, highlight player, and color
    // tiles
    public void printBoard() {
        // Clear console
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println(String.format("Position: %d    Dice Left: %d", pos, diceCount));

        StringBuilder view = new StringBuilder();
        int start = Math.max(0, pos - 2);
        int end = Math.min(boardLength - 1, pos + 2);
        for (int i = start; i <= end; i++) {
            if (i == pos) {
                view.append(YELLOW).append("[P]").append(RESET);
            }
            view.append(colorTile(board[i])).append(" ");
        }
        System.out.println(view.toString());
    }

    // Helper that applies a color based on tile content
    public String colorTile(String tile) {
        if (tile.contains("+") && tile.contains("Dice"))
            return GREEN + tile + RESET;
        if (tile.contains("-") && tile.contains("Dice"))
            return RED + tile + RESET;
        if (tile.contains("Move ->"))
            return BLUE + tile + RESET;
        if (tile.contains("Move <-"))
            return PURPLE + tile + RESET;
        if (tile.contains("[") && tile.contains("]"))
            return CYAN + tile + RESET;
        return tile;
    }

    public int rollDice() {
        return (rand.nextInt(1, 7));
    }

    public void generateBoard() {
        for (int i = 0; i < boardLength; i++) {
            board[i] = generateTile();
        }
    }

    // Used by generateBoard to generate a singular tile
    public String generateTile() {
        int randomNum;
        String tile;
        randomNum = rand.nextInt(1, 10);
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
            case 9:
                // Extra ticket tile
                tile = "|   [ " + rand.nextInt(1, 4) + "]   |";
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
