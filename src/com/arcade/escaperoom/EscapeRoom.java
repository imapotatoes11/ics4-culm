//public class EscapeRoom extends Game {
//   public static final int STAGES = 4;
//   private int currentStage;
//   private char chosenOption;
//   private static final int QUESTION_INDEX = 0;
//   private static final int CORRECT_ANS_INDEX = 1;
//   private String[][] scenario = {{"question", "ans1", "ans2", "ans3"}};
//
//   private static String[] shuffleAnswers (String[] array) {
//      String[] copy = System.arraycopy(array);
//      Random random = new Random();
//      int r1, r2, r3, r4;
//      r1 = rand.nextInt(1, array.length);
//      r2 = rand.nextInt(1, array.length);
//      r3 = rand.nextInt(1, array.length);
//      r4 = rand.nextInt(1, array.length);
//
//      String tmp = copy[r1];
//      copy[r1] = copy[r2];
//      copy[r2] = tmp;
//
//      tmp = copy[r3];
//      copy[r3] = copy[r4];
//      copy[r4] = tmp;
//   }
//
//}

package com.arcade.escaperoom;

import java.util.Scanner;
import com.arcade.player.Player;
import com.arcade.games.Game;
import com.arcade.util.Bcolors;

/**
 * A text-based escape room game where the player has to solve a series of
 * puzzles to "escape".
 * This class contains the logic and narrative for a specific escape room
 * scenario.
 *
 * @author Gemini
 */
public class EscapeRoom extends Game {

    private Player player;
    private Scanner scanner = new Scanner(System.in);

    // Game state variables
    private boolean hasKey = false;
    private boolean isDoorUnlocked = false;
    private boolean isSafeOpened = false;
    private int attemptsLeft = 3; // For the riddle

    /**
     * Constructor for the EscapeRoom game.
     * 
     * @param player The player who will be playing the game.
     */
    public EscapeRoom(Player player) {
        this.player = player;
    }

    /**
     * Starts and manages the main game loop for the escape room.
     * It presents the scenario to the player and processes their input
     * until the game is won or the player decides to quit.
     *
     * @param player The current player.
     * @return The number of tickets won (a fixed amount for winning, 0 otherwise).
     */
    @Override
    public double play(Player player) {
        printIntroduction();

        while (true) {
            printRoomDescription();
            System.out.println(Bcolors.CYAN + "What do you want to do?" + Bcolors.ENDC);
            System.out.println("1. Examine the " + Bcolors.YELLOW + "Old Bookshelf" + Bcolors.ENDC);
            System.out.println("2. Look at the " + Bcolors.YELLOW + "Fireplace" + Bcolors.ENDC);
            System.out.println("3. Check the " + Bcolors.YELLOW + "Locked Door" + Bcolors.ENDC);
            System.out.println("4. Inspect the " + Bcolors.YELLOW + "Painting" + Bcolors.ENDC);
            if (isSafeOpened) {
                System.out.println("5. Look inside the " + Bcolors.GREEN + "Open Safe" + Bcolors.ENDC);
            }
            System.out.println("0. " + Bcolors.RED + "Give Up and Leave" + Bcolors.ENDC);
            System.out.print(Bcolors.CYAN + "Enter your choice: " + Bcolors.ENDC);

            String choice = scanner.nextLine();
            System.out.println(); // Add a newline for better readability

            switch (choice) {
                case "1":
                    examineBookshelf();
                    break;
                case "2":
                    examineFireplace();
                    break;
                case "3":
                    checkDoor();
                    if (isDoorUnlocked) {
                        // If the door is unlocked, the game is won.
                        return winGame();
                    }
                    break;
                case "4":
                    inspectPainting();
                    break;
                case "5":
                    if (isSafeOpened) {
                        lookInSafe();
                    } else {
                        System.out.println(Bcolors.RED + "Invalid choice." + Bcolors.ENDC);
                    }
                    break;
                case "0":
                    System.out.println(Bcolors.BLUE
                            + "You decide that puzzles are not for you today. You leave the room." + Bcolors.ENDC);
                    return 0; // No tickets for giving up
                default:
                    System.out.println(Bcolors.RED + "Invalid choice. Please try again." + Bcolors.ENDC);
                    break;
            }
        }
    }

    /**
     * Prints the initial story and introduction for the escape room.
     */
    private void printIntroduction() {
        System.out.println(Bcolors.PURPLE + "========================================");
        System.out.println("   Welcome to the Haunted Mansion!");
        System.out.println("========================================" + Bcolors.ENDC);
        System.out.println("You find yourself in a dusty, dimly lit study.");
        System.out.println("The heavy oak door behind you clicks shut, and the lock turns.");
        System.out.println("A chilling whisper echoes through the room... 'Solve the puzzle, or stay forever.'");
        System.out.println("Your goal is to find the key and unlock the door to escape.\n");
    }

    /**
     * Prints the current state of the room to the player.
     */
    private void printRoomDescription() {
        System.out.println(Bcolors.BLUE + "----------------------------------------" + Bcolors.ENDC);
        System.out.println("You are in a study. You see an " + Bcolors.YELLOW + "Old Bookshelf" + Bcolors.ENDC
                + ", a cold " + Bcolors.YELLOW + "Fireplace" + Bcolors.ENDC + ",");
        System.out.println("a large " + Bcolors.YELLOW + "Painting" + Bcolors.ENDC + " of a stern-looking man, and the "
                + Bcolors.YELLOW + "Locked Door" + Bcolors.ENDC + ".");
        if (hasKey) {
            System.out.println(Bcolors.GREEN + "You are holding a small, ornate brass key." + Bcolors.ENDC);
        }
        System.out.println(Bcolors.BLUE + "----------------------------------------" + Bcolors.ENDC);
    }

    /**
     * Handles the logic for when the player examines the bookshelf.
     * Contains a riddle that reveals the location of a hidden safe.
     */
    private void examineBookshelf() {
        System.out.println("The bookshelf is filled with ancient, leather-bound books.");
        System.out.println("One book, titled 'Riddles of the Dark', seems to call to you.");
        System.out.println("Do you want to read it? (yes/no)");
        String read = scanner.nextLine().trim().toLowerCase();

        if (read.equals("yes")) {
            System.out.println("The page opens to a single riddle:");
            System.out.println(Bcolors.PURPLE + "I have a face but no eyes, hands but no arms.");
            System.out.println("I watch over the room's heart, where warmth departs." + Bcolors.ENDC);
            System.out.println("What am I?");

            while (attemptsLeft > 0) {
                System.out.print("Your answer: ");
                String answer = scanner.nextLine().trim().toLowerCase();
                if (answer.contains("painting")) {
                    System.out.println(Bcolors.GREEN
                            + "Correct! As you speak the word, you hear a faint 'click' from the wall where the painting hangs."
                            + Bcolors.ENDC);
                    isSafeOpened = true;
                    return;
                } else {
                    attemptsLeft--;
                    if (attemptsLeft > 0) {
                        System.out.println(Bcolors.RED + "Nothing happens. You have " + attemptsLeft + " attempts left."
                                + Bcolors.ENDC);
                    } else {
                        System.out.println(
                                Bcolors.RED + "The book snaps shut! The riddle's magic has faded." + Bcolors.ENDC);
                    }
                }
            }
        } else {
            System.out.println("You decide to leave the books alone.");
        }
    }

    /**
     * Handles the logic for when the player examines the fireplace.
     */
    private void examineFireplace() {
        System.out.println("The fireplace is cold and filled with soot. There's nothing of interest here.");
        System.out.println("You just get your hands dirty.");
    }

    /**
     * Handles the logic for when the player inspects the painting.
     * If the riddle has been solved, this reveals the safe.
     */
    private void inspectPainting() {
        if (isSafeOpened) {
            System.out.println("Behind the painting, a small safe is revealed, its door slightly ajar.");
        } else {
            System.out.println(
                    "The painting is of a stern-faced man with piercing eyes. It feels like he's watching you.");
            System.out.println("The frame is bolted to the wall. You can't move it.");
        }
    }

    /**
     * Handles the logic for looking inside the safe. This is where the key is
     * found.
     */
    private void lookInSafe() {
        if (!hasKey) {
            System.out.println("You reach into the safe and your fingers close around a cold, metal object.");
            System.out.println(Bcolors.GREEN + "You found a small, ornate brass key!" + Bcolors.ENDC);
            hasKey = true;
        } else {
            System.out.println("The safe is now empty.");
        }
    }

    /**
     * Handles the logic for checking the locked door.
     * If the player has the key, they can unlock it.
     */
    private void checkDoor() {
        if (hasKey) {
            System.out.println("You have the key! Do you want to try unlocking the door? (yes/no)");
            String unlock = scanner.nextLine().trim().toLowerCase();
            if (unlock.equals("yes")) {
                System.out.println(Bcolors.GREEN
                        + "You slide the brass key into the lock. It turns with a satisfying *CLICK*." + Bcolors.ENDC);
                System.out.println(Bcolors.GREEN
                        + "The door creaks open, revealing the hallway of the arcade. You've escaped!" + Bcolors.ENDC);
                isDoorUnlocked = true;
            } else {
                System.out.println("You decide not to use the key just yet.");
            }
        } else {
            System.out.println("The door is locked tight. You need to find a key.");
        }
    }

    /**
     * Awards the player tickets for winning and returns the amount.
     * 
     * @return The number of tickets won.
     */
    private double winGame() {
        double ticketsWon = 250; // A handsome prize for your intellect!
        System.out.println(Bcolors.YELLOW + "\nCongratulations, " + player.getName()
                + "! You've solved the puzzle and escaped the Haunted Mansion!" + Bcolors.ENDC);
        System.out.println("You've been awarded " + Bcolors.GREEN + ticketsWon + " tickets!" + Bcolors.ENDC);
        player.getWallet().addTickets(ticketsWon);
        return ticketsWon;
    }
}
