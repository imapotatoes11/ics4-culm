/**
 * ArcadeRunner.java
 *
 * main entry point for the arcade gaming system
 * handles user authentication, menu navigation, and game selection
 * provides both admin and regular user interfaces
 *
 * date: jun 15, 2025
 * author: kevin wang
 */
package com.arcade;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import com.arcade.player.Player;
import com.arcade.games.Game;
import com.arcade.item.Functional;
import com.arcade.item.Achievement;

/**
 * main class that runs the arcade gaming system
 * manages user authentication, game selection, and provides
 * separate interfaces for admin and regular users
 */
public class ArcadeRunner {

    /**
     * main method that starts the arcade application
     * handles login process and directs users to appropriate menus
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        ArcadeManager arcadeManager = new ArcadeManager();
        Scanner sc = new Scanner(System.in);

        // load existing player data from file
        arcadeManager.loadFromFile();

        boolean loggedIn = false;

        // authentication loop - continues until user logs in or exits
        while (!loggedIn) {
            System.out.println("\n\n=== ARCADE LOGIN ===");
            System.out.println("Welcome to Arcade!");
            System.out.println("Would you like to:");
            System.out.println("  1. Log in as a user");
            System.out.println("  2. Create an account");
            System.out.println("  3. Exit");
            System.out.print("Enter an option: ");

            // parse first character of input as integer for menu selection
            int option = Integer.parseInt(String.valueOf(sc.nextLine().charAt(0)));
            if (option == 3)
                return;
            if (option == 2) {
                System.out.print("Enter username (must be unique): ");
                String username = sc.nextLine();

                // check if username already exists in the system
                if (arcadeManager.searchForPlayer(username.toLowerCase()) != null) {
                    System.out
                            .println("Error: Username already exists. Try logging in or select a different username.");
                } else {
                    // use console for secure password input (hides typing)
                    Console console = System.console();
                    String password = new String(console.readPassword("Enter Password: "));

                    // hash password using sha-256 for security
                    String hashedPassword = generateSHA256(password);
                    System.out.print("Enter your age: ");
                    int age = Integer.parseInt(sc.nextLine());
                    System.out.print("Enter your name: ");
                    String name = sc.nextLine();

                    // create new player and add to system
                    Player newPlayer = new Player(name, username.toLowerCase(), hashedPassword, age);
                    arcadeManager.addPlayer(newPlayer);
                    System.out.println("Account created successfully! You can now log in.");
                }
            }
            if (option == 1) {
                System.out.print("Enter username: ");
                String usernameInput = sc.nextLine();

                // secure password input
                Console console = System.console();
                String passwordInput = new String(console.readPassword("Enter Password: "));

                // hash password for comparison with stored hash
                String hashedPassword = generateSHA256(passwordInput);
                ArcadeManager.LoginStatus status = arcadeManager.tryLogin(usernameInput, hashedPassword);

                // handle different login outcomes
                switch (status) {
                    case USERNAME_NOT_FOUND:
                        System.out.println("Error: Username not found. Try again or sign up as a new user.");
                        break;
                    case INCORRECT_PASSWORD:
                        System.out.println("Error: The password you entered is incorrect. Please try again.");
                        break;
                    case SUCCESS:
                        System.out.println("Login success!");
                        loggedIn = true;
                        break;
                }
            }
        }

        // direct user to appropriate menu based on admin status
        if (arcadeManager.isAdmin()) {
            runAdminMenu(arcadeManager, sc);
        } else {
            runUserMenu(arcadeManager, sc);
        }
    }

    /**
     * runs the admin menu interface with administrative functions
     * provides access to player management, statistics, and sorting features
     * demonstrates admin interface design and player management operations
     * 
     * @param arcadeManager the arcade manager instance
     * @param sc            scanner for user input
     */
    private static void runAdminMenu(ArcadeManager arcadeManager, Scanner sc) {
        System.out.println("\n\n=== ARCADE > ADMIN PANEL ===");
        System.out.println("Welcome Administrator, " + arcadeManager.getPlayer().getUsername() + "!");

        boolean running = true;
        do {
            System.out.println("\n\n=== ARCADE > ADMIN MENU ===");
            System.out.println("  1. View all players");
            System.out.println("  2. View players sorted by username");
            System.out.println("  3. View players sorted by age");
            System.out.println("  4. Search player by username (Binary Search)");
            System.out.println("  5. Search players by age range (Linear Search)");
            System.out.println("  6. Search players by name");
            System.out.println("  7. Remove player");
            System.out.println("  8. View player statistics");
            System.out.println("  9. Log out");
            System.out.print("Enter an option: ");

            try {
                int choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1:
                        arcadeManager.displayAllPlayers();
                        break;
                    case 2:
                        // demonstrates quick sort algorithm
                        arcadeManager.displayPlayersSortedByUsername();
                        break;
                    case 3:
                        // demonstrates merge sort algorithm
                        arcadeManager.displayPlayersSortedByAge();
                        break;
                    case 4:
                        System.out.print("Enter username to search: ");
                        String searchUsername = sc.nextLine();

                        // demonstrates binary search algorithm (requires sorted data)
                        Player foundPlayer = arcadeManager.binarySearchPlayerByUsername(searchUsername);
                        if (foundPlayer != null) {
                            System.out.println("\n=== PLAYER FOUND ===");
                            System.out.println("Username: " + foundPlayer.getUsername());
                            System.out.println("Name: " + foundPlayer.getName());
                            System.out.println("Age: " + foundPlayer.getAge());
                            System.out.println("Achievements: " + foundPlayer.getAchievements().size());
                        } else {
                            System.out.println("Player not found.");
                        }
                        break;
                    case 5:
                        System.out.print("Enter minimum age: ");
                        int minAge = Integer.parseInt(sc.nextLine());
                        System.out.print("Enter maximum age: ");
                        int maxAge = Integer.parseInt(sc.nextLine());

                        // demonstrates linear search algorithm
                        arcadeManager.searchPlayersByAgeRange(minAge, maxAge);
                        break;
                    case 6:
                        System.out.print("Enter player name to search: ");
                        String playerName = sc.nextLine();
                        List<Player> playersFound = arcadeManager.searchForPlayersByName(playerName);
                        if (!playersFound.isEmpty()) {
                            System.out.println("\n=== PLAYERS FOUND ===");
                            for (Player p : playersFound) {
                                System.out.println("Username: " + p.getUsername() +
                                        ", Name: " + p.getName() +
                                        ", Age: " + p.getAge());
                            }
                        }
                        break;
                    case 7:
                        System.out.print("Enter username to remove: ");
                        String removeUsername = sc.nextLine();
                        if (arcadeManager.removePlayer(removeUsername)) {
                            System.out.println("Player removed successfully.");
                        } else {
                            System.out.println("Failed to remove player.");
                        }
                        break;
                    case 8:
                        displayPlayerStatistics(arcadeManager);
                        break;
                    case 9:
                        running = false;
                        System.out.println("Logging out...");
                        break;
                    default:
                        System.out.println("Invalid option, please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        } while (running);
    }

    /**
     * runs the regular user menu interface with game and profile functions
     * provides access to games, profile viewing, and item management
     * 
     * @param arcadeManager the arcade manager instance
     * @param sc            scanner for user input
     */
    private static void runUserMenu(ArcadeManager arcadeManager, Scanner sc) {
        System.out.println("\n\n=== ARCADE > USER DASHBOARD ===");
        System.out.println("Welcome to the Arcade, " + arcadeManager.getPlayer().getUsername() + "!");
        boolean running = true;
        do {
            System.out.println("\n\n=== ARCADE > MAIN MENU ===");
            System.out.println("What would you like to do?");
            System.out.println("  1. View your profile");
            System.out.println("  2. Play a game");
            System.out.println("  3. View items/achievements");
            System.out.println("  4. Log out");
            System.out.print("Enter an option: ");

            try {
                int choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1:
                        viewProfile(arcadeManager);
                        break;
                    case 2:
                        playGame(arcadeManager, sc);
                        break;
                    case 3:
                        viewItemsAndAchievements(arcadeManager, sc);
                        break;
                    case 4:
                        running = false;
                        System.out.println("Thank you for playing! Goodbye, " +
                                arcadeManager.getPlayer().getUsername() + "!");
                        break;
                    default:
                        System.out.println("Invalid option, please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        } while (running);
    }

    /**
     * displays comprehensive user profile information
     * shows personal details, wallet balance, and achievements
     * demonstrates recursive factorial calculation for achievement scoring
     * 
     * @param arcadeManager the arcade manager instance
     */
    private static void viewProfile(ArcadeManager arcadeManager) {
        Player player = arcadeManager.getPlayer();

        System.out.println("\n\n=== ARCADE > MAIN MENU > YOUR PROFILE ===");
        System.out.println("Username: " + player.getUsername());
        System.out.println("Name: " + player.getName());
        System.out.println("Age: " + player.getAge());
        System.out.println("Achievements: " + player.getAchievements().size());

        // display wallet information with emoji indicators
        System.out.println("\n💳 WALLET:");
        System.out.println("  Tokens: " + player.getWallet().getTokens());
        System.out.println("  Tickets: " + player.getWallet().getTickets());

        // calculate achievement score using factorial (demonstrates recursion)
        // limit to 5 to prevent overflow with large numbers
        int achievementScore = arcadeManager.calculateFactorial(Math.min(player.getAchievements().size(), 5));
        System.out.println("Achievement Score: " + achievementScore + " points");

        // display individual achievements if any exist
        if (!player.getAchievements().isEmpty()) {
            System.out.println("\nYour Achievements:");
            for (Achievement achievement : player.getAchievements()) {
                System.out.println("- " + achievement.getName() + ": " + achievement.getDescription());
            }
        }
    }

    /**
     * handles game selection and playing functionality
     * demonstrates polymorphism with different game types
     * includes age-based difficulty adjustment system
     * 
     * @param arcadeManager the arcade manager instance
     * @param sc            scanner for user input
     */
    private static void playGame(ArcadeManager arcadeManager, Scanner sc) {
        List<Game> games = arcadeManager.getGames();
        Player player = arcadeManager.getPlayer();

        System.out.println("\n\n=== ARCADE > MAIN MENU > GAMES ===");
        System.out.println("💳 Your balance: " + player.getWallet().getTokens() + " tokens, " +
                player.getWallet().getTickets() + " tickets");
        System.out.println("🎯 Difficulty is automatically adjusted based on your age (" +
                arcadeManager.getPlayer().getAge() + ")");
        System.out.println("   Players aged 20-30 get full difficulty; others get reduced difficulty.\n");

        // display available games with affordability indicators
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);

            // calculate what the adjusted difficulty would be for display
            int originalDifficulty = game.getDifficulty();
            int adjustedDifficulty = arcadeManager.calculateAgeBasedDifficulty(originalDifficulty,
                    arcadeManager.getPlayer().getAge());

            // show both original and adjusted difficulty if different
            String difficultyDisplay = (adjustedDifficulty != originalDifficulty)
                    ? originalDifficulty + "→" + adjustedDifficulty
                    : String.valueOf(originalDifficulty);

            // check if player can afford the game
            boolean canAfford = arcadeManager.canPlayerAffordGame(game);
            String affordabilityIndicator = canAfford ? "✅" : "❌";

            System.out.println("    " + (i + 1) + ". " + affordabilityIndicator + " " + game.getTitle() +
                    " (Difficulty: " + difficultyDisplay +
                    ", Cost: " + game.getRequiredTokens() + " tokens" +
                    ", Reward: " + game.getTicketRewardRange() + " tickets)");
        }

        System.out.print("Select a game (1-" + games.size() + ") or 0 to go back: ");

        try {
            int choice = Integer.parseInt(sc.nextLine());
            if (choice == 0) {
                return;
            }

            if (choice >= 1 && choice <= games.size()) {
                Game selectedGame = games.get(choice - 1);

                // check if player has enough tokens
                if (!arcadeManager.canPlayerAffordGame(selectedGame)) {
                    System.out.println("❌ You don't have enough tokens to play " + selectedGame.getTitle() + "!");
                    System.out.println("   Required: " + selectedGame.getRequiredTokens() + " tokens");
                    System.out.println("   You have: " + player.getWallet().getTokens() + " tokens");
                    return;
                }

                // confirm payment before starting game
                System.out.println("\n💰 This game costs " + selectedGame.getRequiredTokens() + " tokens.");
                System.out.print("Do you want to proceed? (y/n): ");
                String confirm = sc.nextLine().toLowerCase();

                if (!confirm.startsWith("y")) {
                    System.out.println("Game cancelled.");
                    return;
                }

                // apply age-based difficulty adjustment before starting the game
                arcadeManager.adjustGameDifficultyForCurrentPlayer(selectedGame);

                System.out.println("\nStarting " + selectedGame.getTitle() + "...");

                // polymorphism: calling runGame on different game types
                ArrayList<Functional> items = new ArrayList<>(); // empty items list for now
                int ticketsWon = selectedGame.runGame(items);

                // process the transaction (deduct tokens, award tickets)
                arcadeManager.processGameTransaction(selectedGame, ticketsWon);

                // award achievement for playing games
                Achievement gameAchievement = new Achievement("Game Player",
                        "Played " + selectedGame.getTitle());
                arcadeManager.getPlayer().addAchievement(gameAchievement);

            } else {
                System.out.println("Invalid selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    /**
     * manages viewing and searching of items and achievements
     * provides sorting and searching functionality for user items
     * demonstrates bubble sort and linear search algorithms
     * 
     * @param arcadeManager the arcade manager instance
     * @param sc            scanner for user input
     */
    private static void viewItemsAndAchievements(ArcadeManager arcadeManager, Scanner sc) {
        Player player = arcadeManager.getPlayer();

        boolean viewing = true;
        while (viewing) {
            System.out.println("\n\n=== ARCADE > MAIN MENU > ITEMS & ACHIEVEMENTS ===");
            System.out.println("    1. View wallet & balance");
            System.out.println("    2. View all achievements");
            System.out.println("    3. Sort achievements alphabetically");
            System.out.println("    4. Search for specific achievement");
            System.out.println("    5. Back to main menu");
            System.out.print("Enter an option: ");

            try {
                int choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1:
                        System.out.println("\n\n=== ARCADE > MAIN MENU > ITEMS & ACHIEVEMENTS > WALLET ===");
                        System.out.println("💳 WALLET BALANCE:");
                        System.out.println("   Tokens: " + player.getWallet().getTokens());
                        System.out.println("   Tickets: " + player.getWallet().getTickets());

                        // display powerups if any exist
                        if (player.getWallet().getPowerups() != null && !player.getWallet().getPowerups().isEmpty()) {
                            System.out.println("\n🎮 POWERUPS:");
                            for (Functional powerup : player.getWallet().getPowerups()) {
                                System.out.println("   - " + powerup.getName() + " (Uses: " + powerup.getNumUses() +
                                        ", Price: " + powerup.getPrice() + " tickets)");
                            }
                        } else {
                            System.out.println("\n🎮 POWERUPS: None");
                        }

                        // display trophies if any exist
                        if (player.getWallet().getTrophies() != null && !player.getWallet().getTrophies().isEmpty()) {
                            System.out.println("\n🏆 TROPHIES:");
                            for (Achievement trophy : player.getWallet().getTrophies()) {
                                System.out.println("   - " + trophy.getName() + ": " + trophy.getDescription());
                            }
                        } else {
                            System.out.println("\n🏆 TROPHIES: None");
                        }
                        break;
                    case 2:
                        List<Achievement> achievements = player.getAchievements();
                        if (achievements.isEmpty()) {
                            System.out.println("You have no achievements yet. Play some games to earn them!");
                        } else {
                            System.out.println("\n\n=== ARCADE > MAIN MENU > ITEMS & ACHIEVEMENTS > VIEW ALL ===");
                            for (Achievement achievement : achievements) {
                                System.out.println(
                                        "      - " + achievement.getName() + ": " + achievement.getDescription());
                            }
                        }
                        break;
                    case 3:
                        // demonstrates bubble sort algorithm
                        player.sortAchievements();
                        break;
                    case 4:
                        System.out.print("Enter achievement name to search: ");
                        String searchName = sc.nextLine();

                        // demonstrates linear search algorithm
                        Achievement found = player.findAchievementByName(searchName);
                        if (found != null) {
                            System.out.println("Found: " + found.getName() + " - " + found.getDescription());
                        } else {
                            System.out.println("Achievement not found.");
                        }
                        break;
                    case 5:
                        viewing = false;
                        break;
                    default:
                        System.out.println("Invalid option, please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * displays comprehensive player statistics for admin use
     * calculates and shows aggregate data about all players
     * 
     * @param arcadeManager the arcade manager instance
     */
    private static void displayPlayerStatistics(ArcadeManager arcadeManager) {
        List<Player> allPlayers = arcadeManager.getPlayers();
        if (allPlayers == null) {
            allPlayers = arcadeManager.loadFromFile();
        }

        if (allPlayers.isEmpty()) {
            System.out.println("No players found.");
            return;
        }

        System.out.println("\n=== PLAYER STATISTICS ===");
        System.out.println("Total Players: " + allPlayers.size());

        // calculate aggregate statistics
        int totalAge = 0;
        int totalAchievements = 0;

        for (Player p : allPlayers) {
            totalAge += p.getAge();
            totalAchievements += p.getAchievements().size();
        }

        // calculate averages with proper decimal formatting
        double averageAge = (double) totalAge / allPlayers.size();
        double averageAchievements = (double) totalAchievements / allPlayers.size();

        System.out.printf("Average Age: %.1f years\n", averageAge);
        System.out.printf("Average Achievements: %.1f per player\n", averageAchievements);
        System.out.println("Total Achievements: " + totalAchievements);
    }

    /**
     * generates a sha-256 hash for a given string
     * used for secure password storage and verification
     * 
     * @param input the string to be hashed
     * @return the sha-256 hash as a hexadecimal string, or null if algorithm not
     *         found
     */
    public static String generateSHA256(String input) {
        try {
            // get an instance of the MessageDigest for the sha-256 algorithm
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // perform the hash computation
            // get bytes using utf-8 encoding which is the standard
            byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // convert the byte array into a hexadecimal string
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            // this exception is thrown if the algorithm is not available
            // highly unlikely for standard algorithms like sha-256
            System.err.println("SHA-256 algorithm not found!");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * helper method to convert a byte array into a hexadecimal string
     * representation
     * used by the sha-256 hashing function
     * 
     * @param hash the byte array to convert
     * @return the hexadecimal string representation
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            // convert each byte to a hex value
            String hex = Integer.toHexString(0xff & hash[i]);
            // prepend '0' if the hex value is only one character
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
