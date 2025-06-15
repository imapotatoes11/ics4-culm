/*
 * ArcadeRunner.java
 *
 * Date: 06 12, 2025
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

public class ArcadeRunner {
    public static void main(String[] args) {
        ArcadeManager arcadeManager = new ArcadeManager();
        Scanner sc = new Scanner(System.in);

        arcadeManager.loadFromFile();

        boolean loggedIn = false;

        while (!loggedIn) {
            System.out.println("\n\n=== ARCADE LOGIN ===");
            System.out.println("Welcome to Arcade!");
            System.out.println("Would you like to:");
            System.out.println("  1. Log in as a user");
            System.out.println("  2. Create an account");
            System.out.println("  3. Exit");
            System.out.print("Enter an option: ");
            int option = Integer.parseInt(String.valueOf(sc.nextLine().charAt(0)));
            if (option == 3)
                return;
            if (option == 2) {
                System.out.print("Enter username (must be unique): ");
                String username = sc.nextLine();
                // Check if username already exists
                if (arcadeManager.searchForPlayer(username.toLowerCase()) != null) {
                    System.out
                            .println("Error: Username already exists. Try logging in or select a different username.");
                } else {
                    // System.out.print("Enter password: ");
                    Console console = System.console();
                    String password = new String(console.readPassword("Enter Password: "));
                    // hash password
                    String hashedPassword = generateSHA256(password);
                    System.out.print("Enter your age: ");
                    int age = Integer.parseInt(sc.nextLine());
                    System.out.print("Enter your name: ");
                    String name = sc.nextLine();
                    // Create a new player and add to the arcade manager
                    Player newPlayer = new Player(name, username.toLowerCase(), hashedPassword, age);
                    arcadeManager.addPlayer(newPlayer);
                    System.out.println("Account created successfully! You can now log in.");
                }
            }
            if (option == 1) {
                System.out.print("Enter username: ");
                String usernameInput = sc.nextLine();
                // System.out.print("Enter password: ");
                Console console = System.console();
                String passwordInput = new String(console.readPassword("Enter Password: "));
                // hash password
                String hashedPassword = generateSHA256(passwordInput);
                ArcadeManager.LoginStatus status = arcadeManager.tryLogin(usernameInput, hashedPassword);
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

        // Check if admin or regular user
        if (arcadeManager.isAdmin()) {
            runAdminMenu(arcadeManager, sc);
        } else {
            runUserMenu(arcadeManager, sc);
        }
    }

    /**
     * Admin menu with administrative functions
     * Demonstrates: Admin interface, player management
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
                        arcadeManager.displayPlayersSortedByUsername();
                        break;
                    case 3:
                        arcadeManager.displayPlayersSortedByAge();
                        break;
                    case 4:
                        System.out.print("Enter username to search: ");
                        String searchUsername = sc.nextLine();
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
     * Regular user menu with game and profile functions
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
     * Display user profile information
     */
    private static void viewProfile(ArcadeManager arcadeManager) {
        Player player = arcadeManager.getPlayer();

        System.out.println("\n\n=== ARCADE > MAIN MENU > YOUR PROFILE ===");
        System.out.println("Username: " + player.getUsername());
        System.out.println("Name: " + player.getName());
        System.out.println("Age: " + player.getAge());
        System.out.println("Achievements: " + player.getAchievements().size());

        // Display wallet information
        System.out.println("\n💳 WALLET:");
        System.out.println("  Tokens: " + player.getWallet().getTokens());
        System.out.println("  Tickets: " + player.getWallet().getTickets());

        // Calculate achievement score using factorial (demonstrates recursion)
        int achievementScore = arcadeManager.calculateFactorial(Math.min(player.getAchievements().size(), 5));
        System.out.println("Achievement Score: " + achievementScore + " points");

        if (!player.getAchievements().isEmpty()) {
            System.out.println("\nYour Achievements:");
            for (Achievement achievement : player.getAchievements()) {
                System.out.println("- " + achievement.getName() + ": " + achievement.getDescription());
            }
        }
    }

    /**
     * Game selection and playing menu
     * Demonstrates: Polymorphism with Game objects
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

        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            // Calculate what the adjusted difficulty would be for display
            int originalDifficulty = game.getDifficulty();
            int adjustedDifficulty = arcadeManager.calculateAgeBasedDifficulty(originalDifficulty,
                    arcadeManager.getPlayer().getAge());

            String difficultyDisplay = (adjustedDifficulty != originalDifficulty)
                    ? originalDifficulty + "→" + adjustedDifficulty
                    : String.valueOf(originalDifficulty);

            // Check if player can afford the game
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

                // Check if player can afford the game
                if (!arcadeManager.canPlayerAffordGame(selectedGame)) {
                    System.out.println("❌ You don't have enough tokens to play " + selectedGame.getTitle() + "!");
                    System.out.println("   Required: " + selectedGame.getRequiredTokens() + " tokens");
                    System.out.println("   You have: " + player.getWallet().getTokens() + " tokens");
                    return;
                }

                System.out.println("\n💰 This game costs " + selectedGame.getRequiredTokens() + " tokens.");
                System.out.print("Do you want to proceed? (y/n): ");
                String confirm = sc.nextLine().toLowerCase();

                if (!confirm.startsWith("y")) {
                    System.out.println("Game cancelled.");
                    return;
                }

                // Apply age-based difficulty adjustment before starting the game
                arcadeManager.adjustGameDifficultyForCurrentPlayer(selectedGame);

                System.out.println("\nStarting " + selectedGame.getTitle() + "...");

                // Polymorphism: calling runGame on different game types
                ArrayList<Functional> items = new ArrayList<>(); // Empty items list for now
                int ticketsWon = selectedGame.runGame(items);

                // Process the transaction (deduct tokens, award tickets)
                arcadeManager.processGameTransaction(selectedGame, ticketsWon);

                // Award achievement for playing games
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
     * View and search items/achievements
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

                        if (player.getWallet().getPowerups() != null && !player.getWallet().getPowerups().isEmpty()) {
                            System.out.println("\n🎮 POWERUPS:");
                            for (Functional powerup : player.getWallet().getPowerups()) {
                                System.out.println("   - " + powerup.getName() + " (Uses: " + powerup.getNumUses() +
                                        ", Price: " + powerup.getPrice() + " tickets)");
                            }
                        } else {
                            System.out.println("\n🎮 POWERUPS: None");
                        }

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
                        player.sortAchievements(); // Uses bubble sort from Player class
                        break;
                    case 4:
                        System.out.print("Enter achievement name to search: ");
                        String searchName = sc.nextLine();
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
     * Display player statistics for admin
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

        // Calculate average age
        int totalAge = 0;
        int totalAchievements = 0;

        for (Player p : allPlayers) {
            totalAge += p.getAge();
            totalAchievements += p.getAchievements().size();
        }

        double averageAge = (double) totalAge / allPlayers.size();
        double averageAchievements = (double) totalAchievements / allPlayers.size();

        System.out.printf("Average Age: %.1f years\n", averageAge);
        System.out.printf("Average Achievements: %.1f per player\n", averageAchievements);
        System.out.println("Total Achievements: " + totalAchievements);
    }

    /**
     * Generates a SHA-256 hash for a given string.
     *
     * @param input The string to be hashed.
     * @return The SHA-256 hash as a hexadecimal string. Returns null if the
     *         algorithm is not found.
     */
    public static String generateSHA256(String input) {
        try {
            // Get an instance of the MessageDigest for the SHA-256 algorithm.
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Perform the hash computation.
            // We get the bytes of the string using UTF-8 encoding, which is standard.
            byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array into a hexadecimal string.
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            // This exception is thrown if the algorithm (e.g., "SHA-256") is not available.
            // It's highly unlikely for standard algorithms like SHA-256.
            System.err.println("SHA-256 algorithm not found!");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Helper method to convert a byte array into a hexadecimal string
     * representation.
     *
     * @param hash The byte array to convert.
     * @return The hexadecimal string.
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            // Convert each byte to a hex value.
            String hex = Integer.toHexString(0xff & hash[i]);
            // Prepend '0' if the hex value is only one character.
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
