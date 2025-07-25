/**
 * EscapeRoom.java
 *
 * implementation of a text-based escape room game for the arcade system
 * players solve puzzles and riddles to escape a haunted mansion
 * features multiple rooms, items, and branching paths
 * includes performance tracking and dynamic difficulty adjustment
 * supports functional items like extra lives and luck modifiers
 *
 * key game elements:
 * - puzzle solving with limited attempts
 * - inventory management with key items
 * - time-based scoring system
 * - multiple endings based on performance
 * - ticket rewards scaled by completion and efficiency
 *
 * date: jun 15, 2025
 * author: kevin wang (eddie wrote a small part of the code)
 */

package com.arcade.games.escaperoom;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.arcade.games.Game;
import com.arcade.item.ExtraLife;
import com.arcade.item.Functional;
import com.arcade.item.Luck;
import com.arcade.item.TicketMultiplier;
import com.arcade.item.AchievementChecker;
import com.arcade.item.Achievement;
import com.arcade.util.Bcolors;

/**
 * A text-based escape room game where the player has to solve a series of
 * puzzles to "escape".
 * This class contains the logic and narrative for a specific escape room
 * scenario.
 * It now correctly extends the Game class and implements item functionality.
 */
public class EscapeRoom extends Game {

    private Scanner scanner = new Scanner(System.in);

    // Game state variables
    private boolean hasKey = false;
    private boolean isDoorUnlocked = false;
    private boolean isSafeOpened = false;
    private int attemptsLeft = 3; // For the riddle
    private int ticketMultiplier = 1;

    // Performance tracking variables
    private int totalActions = 0;
    private int riddleAttemptsUsed = 0;
    private boolean usedExtraLife = false;
    private long startTime;
    private long endTime;

    /**
     * Default constructor for the EscapeRoom game.
     */
    public EscapeRoom() {
        // id, title, difficulty, requiredTokens, ticketReward
        super(5, "Haunted Mansion Escape", 5, 25, 250);
    }

    /**
     * Constructor for the EscapeRoom game with custom parameters.
     *
     * @param id             The game's ID.
     * @param title          The game's title.
     * @param difficulty     The game's difficulty level.
     * @param requiredTokens The tokens required to play.
     * @param ticketReward   The base ticket reward for winning.
     */
    public EscapeRoom(int id, String title, int difficulty, int requiredTokens, int ticketReward) {
        super(id, title, difficulty, requiredTokens, ticketReward);
    }

    /**
     * Starts and manages the main game loop for the escape room.
     * It presents the scenario to the player and processes their input
     * until the game is won or the player decides to quit.
     *
     * @param useItems A list of functional items the player wants to use.
     * @return The number of tickets won based on performance.
     */
    @Override
    public int runGame(ArrayList<Functional> useItems) {
        startTime = System.currentTimeMillis();

        // Process items at the start of the game
        for (Functional item : useItems) {
            if (item instanceof Luck && item.getNumUses() > 0) {
                int factor = ((Luck) item).getDifficultyDecreaseFactor();
                this.attemptsLeft += factor;
                item.setNumUses(item.getNumUses() - 1);
                System.out.println(Bcolors.GREEN
                        + "A sense of clarity washes over you. You feel luckier! (Riddle attempts increased by "
                        + factor + ")" + Bcolors.ENDC);
            } else if (item instanceof TicketMultiplier && item.getNumUses() > 0) {
                this.ticketMultiplier = TicketMultiplier.MULTIPLIER;
                System.out.println(Bcolors.GREEN + "Your potential rewards feel greater! (Ticket Multiplier is active)"
                        + Bcolors.ENDC);
                // We'll consume this item's use at the end if the player wins
            }
            // ExtraLife is handled when the player fails a critical task
        }

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

            totalActions++; // Track player actions for performance

            switch (choice) {
                case "1":
                    examineBookshelf(useItems);
                    break;
                case "2":
                    examineFireplace();
                    break;
                case "3":
                    checkDoor();
                    if (isDoorUnlocked) {
                        return winGame(useItems);
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
     * 
     * @param useItems List of items to check for Extra Life.
     */
    private void examineBookshelf(ArrayList<Functional> useItems) {
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
                riddleAttemptsUsed++;

                if (answer.contains("painting")) {
                    System.out.println(Bcolors.GREEN
                            + "Correct! As you speak the word, you hear a faint 'click' from the wall where the painting hangs."
                            + Bcolors.ENDC);
                    isSafeOpened = true;
                    return;
                } else {
                    attemptsLeft--;
                    if (attemptsLeft > 0) {
                        System.out
                                .println(Bcolors.RED + "Nothing happens. You have " + attemptsLeft + " attempt(s) left."
                                        + Bcolors.ENDC);
                    } else {
                        // Check for Extra Life
                        boolean extraLifeUsed = false;
                        for (Functional item : useItems) {
                            if (item instanceof ExtraLife && item.getNumUses() > 0) {
                                item.setNumUses(item.getNumUses() - 1);
                                this.attemptsLeft = 3; // Reset attempts
                                this.usedExtraLife = true;
                                System.out.println(Bcolors.YELLOW
                                        + "Just as you're about to give up, a surge of determination fills you. An Extra Life is consumed!"
                                        + Bcolors.ENDC);
                                System.out.println(
                                        Bcolors.GREEN + "Your riddle attempts have been restored!" + Bcolors.ENDC);
                                extraLifeUsed = true;
                                break;
                            }
                        }
                        if (!extraLifeUsed) {
                            System.out.println(
                                    Bcolors.RED + "The book snaps shut! The riddle's magic has faded." + Bcolors.ENDC);
                        }
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
     * Calculate performance score based on various factors
     * 
     * @return Performance score from 0.0 to 1.0
     */
    private double calculatePerformanceScore() {
        double score = 0.0;

        // Base completion score (0.4 for completing the game)
        score += 0.4;

        // Efficiency score based on total actions (0.0 to 0.3)
        // Fewer actions = better performance
        int maxActions = 15; // Reasonable number of actions to complete
        double efficiencyScore = Math.max(0.0, 1.0 - (double) totalActions / maxActions) * 0.3;
        score += efficiencyScore;

        // Riddle solving efficiency (0.0 to 0.2)
        if (isSafeOpened) {
            // Better score for solving riddle with fewer attempts
            double riddleEfficiency = Math.max(0.0, 1.0 - (double) riddleAttemptsUsed / 3.0) * 0.2;
            score += riddleEfficiency;
        }

        // Time bonus (0.0 to 0.1)
        // Complete faster = better score
        long gameTime = endTime - startTime;
        long maxReasonableTime = 300000; // 5 minutes in milliseconds
        double timeBonus = Math.max(0.0, 1.0 - (double) gameTime / maxReasonableTime) * 0.1;
        score += timeBonus;

        // Penalty for using Extra Life (-0.1)
        if (usedExtraLife) {
            score -= 0.1;
        }

        // Ensure score is between 0.0 and 1.0
        return Math.max(0.0, Math.min(1.0, score));
    }

    /**
     * Awards the player tickets for winning and returns the amount.
     *
     * @param useItems The list of items to find and consume the TicketMultiplier.
     * @return The number of tickets won.
     */
    private int winGame(ArrayList<Functional> useItems) {
        endTime = System.currentTimeMillis();

        System.out.println(Bcolors.YELLOW
                + "\nCongratulations! You've solved the puzzle and escaped the Haunted Mansion!" + Bcolors.ENDC);

        // Calculate performance-based ticket reward
        double performanceScore = calculatePerformanceScore();
        int baseTickets = calculateTicketReward(performanceScore);

        // Apply ticket multiplier if used
        int finalTickets = baseTickets;
        if (this.ticketMultiplier > 1) {
            for (Functional item : useItems) {
                if (item instanceof TicketMultiplier && item.getNumUses() > 0) {
                    item.setNumUses(item.getNumUses() - 1);
                    finalTickets *= this.ticketMultiplier;
                    System.out.println(Bcolors.GREEN + "Your Ticket Multiplier doubles your reward!" + Bcolors.ENDC);
                    break;
                }
            }
        }

        // Display performance stats
        System.out.println(Bcolors.CYAN + "\n=== PERFORMANCE SUMMARY ===" + Bcolors.ENDC);
        System.out.println("Actions taken: " + totalActions);
        System.out.println("Riddle attempts: " + riddleAttemptsUsed + "/" + (riddleAttemptsUsed + attemptsLeft));
        System.out.println("Time taken: " + ((endTime - startTime) / 1000) + " seconds");
        if (usedExtraLife) {
            System.out.println(Bcolors.YELLOW + "Extra Life used" + Bcolors.ENDC);
        }
        System.out.println("Performance score: " + String.format("%.1f%%", performanceScore * 100));

        System.out.println("You've been awarded " + Bcolors.GREEN + finalTickets + " tickets!" + Bcolors.ENDC);

        // Check and display achievements
        long completionTimeSeconds = (endTime - startTime) / 1000;
        List<Achievement> achievements = new ArrayList<>();
        achievements.addAll(
                AchievementChecker.checkGeneralAchievements(true, performanceScore, getDifficulty(), usedExtraLife));
        achievements.addAll(AchievementChecker.checkEscapeRoomAchievements(true, completionTimeSeconds));
        AchievementChecker.displayAchievements(achievements);

        return finalTickets;
    }
}