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
import com.arcade.item.Luck;
import com.arcade.item.ExtraLife;
import com.arcade.item.TicketMultiplier;
import com.arcade.util.Bcolors;

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
            System.out.println("\n\n" + Bcolors.BOLD + Bcolors.CYAN + "=== ARCADE LOGIN ===" + Bcolors.ENDC);
            System.out.println(Bcolors.BRIGHT_YELLOW + "Welcome to Arcade!" + Bcolors.ENDC);
            System.out.println("Would you like to:");
            System.out.println(Bcolors.OKBLUE + "  1. Log in as a user" + Bcolors.ENDC);
            System.out.println(Bcolors.OKBLUE + "  2. Create an account" + Bcolors.ENDC);
            System.out.println(Bcolors.OKBLUE + "  3. Exit" + Bcolors.ENDC);
            System.out.print(Bcolors.BOLD + Bcolors.BRIGHT_CYAN + "Enter an option: " + Bcolors.ENDC);

            // parse input as integer for menu selection
            String input = sc.nextLine();
            if (input.isEmpty()) {
                System.out.println(Bcolors.WARNING + "Please enter a valid option." + Bcolors.ENDC);
                continue;
            }

            int option;
            try {
                option = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println(Bcolors.FAIL + "Please enter a valid number." + Bcolors.ENDC);
                continue;
            }
            if (option == 3)
                return;
            if (option == 2) {
                System.out.print(Bcolors.OKCYAN + "Enter username (must be unique): " + Bcolors.ENDC);
                String username = sc.nextLine();

                // check if username already exists in the system
                if (arcadeManager.searchForPlayer(username.toLowerCase()) != null) {
                    System.out
                            .println(Bcolors.FAIL
                                    + "Error: Username already exists. Try logging in or select a different username."
                                    + Bcolors.ENDC);
                } else {
                    // use console for secure password input (hides typing)
                    Console console = System.console();
                    String password;
                    if (console != null) {
                        password = new String(console.readPassword(Bcolors.OKCYAN + "Enter Password: " + Bcolors.ENDC));
                    } else {
                        // fallback for testing when console is not available
                        System.out.print(Bcolors.OKCYAN + "Enter Password: " + Bcolors.ENDC);
                        password = sc.nextLine();
                    }

                    // hash password using sha-256 for security
                    String hashedPassword = generateSHA256(password);
                    System.out.print(Bcolors.OKCYAN + "Enter your age: " + Bcolors.ENDC);
                    int age = Integer.parseInt(sc.nextLine());
                    System.out.print(Bcolors.OKCYAN + "Enter your name: " + Bcolors.ENDC);
                    String name = sc.nextLine();

                    // create new player and add to system
                    Player newPlayer = new Player(name, username.toLowerCase(), hashedPassword, age);
                    arcadeManager.addPlayer(newPlayer);
                    System.out.println(
                            Bcolors.OKGREEN + "Account created successfully! You can now log in." + Bcolors.ENDC);
                }
            }
            if (option == 1) {
                System.out.print(Bcolors.OKCYAN + "Enter username: " + Bcolors.ENDC);
                String usernameInput = sc.nextLine();

                // secure password input
                Console console = System.console();
                String passwordInput;
                if (console != null) {
                    passwordInput = new String(
                            console.readPassword(Bcolors.OKCYAN + "Enter Password: " + Bcolors.ENDC));
                } else {
                    // fallback for testing when console is not available
                    System.out.print(Bcolors.OKCYAN + "Enter Password: " + Bcolors.ENDC);
                    passwordInput = sc.nextLine();
                }

                // hash password for comparison with stored hash
                String hashedPassword = generateSHA256(passwordInput);
                ArcadeManager.LoginStatus status = arcadeManager.tryLogin(usernameInput, hashedPassword);

                // handle different login outcomes
                switch (status) {
                    case USERNAME_NOT_FOUND:
                        System.out.println(Bcolors.FAIL
                                + "Error: Username not found. Try again or sign up as a new user." + Bcolors.ENDC);
                        break;
                    case INCORRECT_PASSWORD:
                        System.out.println(Bcolors.FAIL
                                + "Error: The password you entered is incorrect. Please try again." + Bcolors.ENDC);
                        break;
                    case SUCCESS:
                        System.out.println(Bcolors.OKGREEN + "Login success!" + Bcolors.ENDC);
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
        System.out.println("\n\n" + Bcolors.BOLD + Bcolors.MAGENTA + "=== ARCADE > ADMIN PANEL ===" + Bcolors.ENDC);
        System.out.println(Bcolors.BRIGHT_MAGENTA + "Welcome Administrator, " + arcadeManager.getPlayer().getUsername()
                + "!" + Bcolors.ENDC);

        boolean running = true;
        do {
            System.out.println("\n\n" + Bcolors.BOLD + Bcolors.MAGENTA + "=== ARCADE > ADMIN MENU ===" + Bcolors.ENDC);
            System.out.println(Bcolors.CYAN + "  1. View all players" + Bcolors.ENDC);
            System.out.println(Bcolors.CYAN + "  2. View players sorted by username" + Bcolors.ENDC);
            System.out.println(Bcolors.CYAN + "  3. View players sorted by age" + Bcolors.ENDC);
            System.out.println(Bcolors.CYAN + "  4. Search player by username (Binary Search)" + Bcolors.ENDC);
            System.out.println(Bcolors.CYAN + "  5. Search players by age range (Linear Search)" + Bcolors.ENDC);
            System.out.println(Bcolors.CYAN + "  6. Search players by name" + Bcolors.ENDC);
            System.out.println(Bcolors.CYAN + "  7. Remove player" + Bcolors.ENDC);
            System.out.println(Bcolors.CYAN + "  8. View player statistics" + Bcolors.ENDC);
            System.out.println(Bcolors.YELLOW + "  9. Log out" + Bcolors.ENDC);
            System.out.print(Bcolors.BOLD + Bcolors.BRIGHT_CYAN + "Enter an option: " + Bcolors.ENDC);

            try {
                int choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1:
                        arcadeManager.displayAllPlayers();
                        break;
                    case 2:
                        // demonstrates selection sort algorithm
                        arcadeManager.displayPlayersSortedByUsername();
                        break;
                    case 3:
                        // demonstrates insertion sort algorithm
                        arcadeManager.displayPlayersSortedByAge();
                        break;
                    case 4:
                        System.out.print(Bcolors.OKCYAN + "Enter username to search: " + Bcolors.ENDC);
                        String searchUsername = sc.nextLine();

                        // demonstrates binary search algorithm (requires sorted data)
                        Player foundPlayer = arcadeManager.binarySearchPlayerByUsername(searchUsername);
                        if (foundPlayer != null) {
                            System.out.println(
                                    "\n" + Bcolors.BOLD + Bcolors.GREEN + "=== PLAYER FOUND ===" + Bcolors.ENDC);
                            System.out
                                    .println(Bcolors.OKBLUE + "Username: " + Bcolors.ENDC + foundPlayer.getUsername());
                            System.out.println(Bcolors.OKBLUE + "Name: " + Bcolors.ENDC + foundPlayer.getName());
                            System.out.println(Bcolors.OKBLUE + "Age: " + Bcolors.ENDC + foundPlayer.getAge());
                            System.out.println(Bcolors.OKBLUE + "Achievements: " + Bcolors.ENDC
                                    + foundPlayer.getAchievements().size());
                        } else {
                            System.out.println(Bcolors.WARNING + "Player not found." + Bcolors.ENDC);
                        }
                        break;
                    case 5:
                        System.out.print(Bcolors.OKCYAN + "Enter minimum age: " + Bcolors.ENDC);
                        int minAge = Integer.parseInt(sc.nextLine());
                        System.out.print(Bcolors.OKCYAN + "Enter maximum age: " + Bcolors.ENDC);
                        int maxAge = Integer.parseInt(sc.nextLine());

                        // demonstrates linear search algorithm
                        arcadeManager.searchPlayersByAgeRange(minAge, maxAge);
                        break;
                    case 6:
                        System.out.print(Bcolors.OKCYAN + "Enter player name to search: " + Bcolors.ENDC);
                        String playerName = sc.nextLine();
                        List<Player> playersFound = arcadeManager.searchForPlayersByName(playerName);
                        if (!playersFound.isEmpty()) {
                            System.out.println(
                                    "\n" + Bcolors.BOLD + Bcolors.GREEN + "=== PLAYERS FOUND ===" + Bcolors.ENDC);
                            for (Player p : playersFound) {
                                System.out.println(Bcolors.OKBLUE + "Username: " + Bcolors.ENDC + p.getUsername() +
                                        Bcolors.OKBLUE + ", Name: " + Bcolors.ENDC + p.getName() +
                                        Bcolors.OKBLUE + ", Age: " + Bcolors.ENDC + p.getAge());
                            }
                        }
                        break;
                    case 7:
                        System.out.print(Bcolors.OKCYAN + "Enter username to remove: " + Bcolors.ENDC);
                        String removeUsername = sc.nextLine();
                        if (arcadeManager.removePlayer(removeUsername)) {
                            System.out.println(Bcolors.OKGREEN + "Player removed successfully." + Bcolors.ENDC);
                        } else {
                            System.out.println(Bcolors.FAIL + "Failed to remove player." + Bcolors.ENDC);
                        }
                        break;
                    case 8:
                        displayPlayerStatistics(arcadeManager);
                        break;
                    case 9:
                        running = false;
                        System.out.println(Bcolors.BRIGHT_YELLOW + "Logging out..." + Bcolors.ENDC);
                        break;
                    default:
                        System.out.println(Bcolors.WARNING + "Invalid option, please try again." + Bcolors.ENDC);
                }
            } catch (NumberFormatException e) {
                System.out.println(Bcolors.FAIL + "Please enter a valid number." + Bcolors.ENDC);
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
        System.out.println("\n\n" + Bcolors.BOLD + Bcolors.BLUE + "=== ARCADE > USER DASHBOARD ===" + Bcolors.ENDC);
        System.out.println(Bcolors.BRIGHT_BLUE + "Welcome to the Arcade, " + arcadeManager.getPlayer().getUsername()
                + "!" + Bcolors.ENDC);
        boolean running = true;
        do {
            System.out.println("\n\n" + Bcolors.BOLD + Bcolors.BLUE + "=== ARCADE > MAIN MENU ===" + Bcolors.ENDC);
            System.out.println(Bcolors.BRIGHT_WHITE + "What would you like to do?" + Bcolors.ENDC);
            System.out.println(Bcolors.CYAN + "  1. View your profile" + Bcolors.ENDC);
            System.out.println(Bcolors.CYAN + "  2. Play a game" + Bcolors.ENDC);
            System.out.println(Bcolors.CYAN + "  3. View items/achievements" + Bcolors.ENDC);
            System.out.println(Bcolors.CYAN + "  4. Buy tokens" + Bcolors.ENDC);
            System.out.println(Bcolors.CYAN + "  5. Shop for items/powerups" + Bcolors.ENDC);
            System.out.println(Bcolors.YELLOW + "  6. Log out" + Bcolors.ENDC);
            System.out.print(Bcolors.BOLD + Bcolors.BRIGHT_CYAN + "Enter an option: " + Bcolors.ENDC);

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
                        buyTokens(arcadeManager, sc);
                        break;
                    case 5:
                        shopForItems(arcadeManager, sc);
                        break;
                    case 6:
                        running = false;
                        System.out.println(Bcolors.BRIGHT_YELLOW + "Thank you for playing! Goodbye, " +
                                arcadeManager.getPlayer().getUsername() + "!" + Bcolors.ENDC);
                        break;
                    default:
                        System.out.println(Bcolors.WARNING + "Invalid option, please try again." + Bcolors.ENDC);
                }
            } catch (NumberFormatException e) {
                System.out.println(Bcolors.FAIL + "Please enter a valid number." + Bcolors.ENDC);
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

        System.out.println(
                "\n\n" + Bcolors.BOLD + Bcolors.GREEN + "=== ARCADE > MAIN MENU > YOUR PROFILE ===" + Bcolors.ENDC);
        System.out.println(Bcolors.OKBLUE + "Username: " + Bcolors.ENDC + Bcolors.BRIGHT_WHITE + player.getUsername()
                + Bcolors.ENDC);
        System.out.println(
                Bcolors.OKBLUE + "Name: " + Bcolors.ENDC + Bcolors.BRIGHT_WHITE + player.getName() + Bcolors.ENDC);
        System.out.println(
                Bcolors.OKBLUE + "Age: " + Bcolors.ENDC + Bcolors.BRIGHT_WHITE + player.getAge() + Bcolors.ENDC);
        System.out.println(Bcolors.OKBLUE + "Achievements: " + Bcolors.ENDC + Bcolors.BRIGHT_WHITE
                + player.getAchievements().size() + Bcolors.ENDC);

        // display wallet information with emoji indicators
        System.out.println("\n" + Bcolors.BOLD + Bcolors.CYAN + "💳 WALLET:" + Bcolors.ENDC);
        System.out.println("  " + Bcolors.YELLOW + "Tokens: " + Bcolors.BRIGHT_YELLOW + player.getWallet().getTokens()
                + Bcolors.ENDC);
        System.out.println("  " + Bcolors.MAGENTA + "Tickets: " + Bcolors.BRIGHT_MAGENTA
                + player.getWallet().getTickets() + Bcolors.ENDC);

        // calculate achievement score using factorial (demonstrates recursion)
        // limit to 5 to prevent overflow with large numbers
        int achievementScore = arcadeManager.calculateFactorial(Math.min(player.getAchievements().size(), 5));
        System.out.println(Bcolors.BRIGHT_GREEN + "Achievement Score: " + achievementScore + " points" + Bcolors.ENDC);

        // display individual achievements if any exist
        if (!player.getAchievements().isEmpty()) {
            System.out.println("\n" + Bcolors.BOLD + Bcolors.GREEN + "Your Achievements:" + Bcolors.ENDC);
            for (Achievement achievement : player.getAchievements()) {
                System.out.println(Bcolors.GREEN + "- " + achievement.getName() + ": " + Bcolors.ENDC
                        + achievement.getDescription());
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

        System.out.println("\n\n" + Bcolors.BOLD + Bcolors.RED + "=== ARCADE > MAIN MENU > GAMES ===" + Bcolors.ENDC);
        System.out.println(Bcolors.CYAN + "💳 Your balance: " + Bcolors.BRIGHT_YELLOW + player.getWallet().getTokens() +
                Bcolors.CYAN + " tokens, " + Bcolors.BRIGHT_MAGENTA + player.getWallet().getTickets() +
                Bcolors.CYAN + " tickets" + Bcolors.ENDC);
        System.out.println(Bcolors.OKBLUE + "🎯 Difficulty is automatically adjusted based on your age (" +
                Bcolors.BRIGHT_WHITE + arcadeManager.getPlayer().getAge() + Bcolors.OKBLUE + ")" + Bcolors.ENDC);
        System.out.println(Bcolors.DIM + "   Players aged 20-30 get full difficulty; others get reduced difficulty."
                + Bcolors.ENDC + "\n");

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

        System.out.print(Bcolors.BOLD + Bcolors.BRIGHT_MAGENTA + "Select a game (1-" + games.size()
                + ") or 0 to go back: " + Bcolors.ENDC);

        try {
            int choice = Integer.parseInt(sc.nextLine());
            if (choice == 0) {
                return;
            }

            if (choice >= 1 && choice <= games.size()) {
                Game selectedGame = games.get(choice - 1);

                // check if player has enough tokens
                if (!arcadeManager.canPlayerAffordGame(selectedGame)) {
                    System.out.println(Bcolors.FAIL + "❌ You don't have enough tokens to play "
                            + selectedGame.getTitle() + "!" + Bcolors.ENDC);
                    System.out.println(Bcolors.WARNING + "   Required: " + selectedGame.getRequiredTokens() + " tokens"
                            + Bcolors.ENDC);
                    System.out.println(Bcolors.WARNING + "   You have: " + player.getWallet().getTokens() + " tokens"
                            + Bcolors.ENDC);
                    return;
                }

                // confirm payment before starting game
                System.out.println("\n" + Bcolors.YELLOW + "💰 This game costs " + Bcolors.BRIGHT_YELLOW
                        + selectedGame.getRequiredTokens() +
                        Bcolors.YELLOW + " tokens." + Bcolors.ENDC);
                System.out
                        .print(Bcolors.BOLD + Bcolors.BRIGHT_YELLOW + "Do you want to proceed? (y/n): " + Bcolors.ENDC);
                String confirm = sc.nextLine().toLowerCase();

                if (!confirm.startsWith("y")) {
                    System.out.println(Bcolors.WARNING + "Game cancelled." + Bcolors.ENDC);
                    return;
                }

                // apply age-based difficulty adjustment before starting the game
                arcadeManager.adjustGameDifficultyForCurrentPlayer(selectedGame);

                System.out.println(
                        "\n" + Bcolors.BRIGHT_GREEN + "Starting " + selectedGame.getTitle() + "..." + Bcolors.ENDC);

                // gather available items from player's inventory
                ArrayList<Functional> availableItems = new ArrayList<>();
                if (player.getWallet().getPowerups() != null) {
                    for (Functional item : player.getWallet().getPowerups()) {
                        if (item.getNumUses() > 0) {
                            availableItems.add(item);
                        }
                    }
                }

                // let player choose items to use (simplified for now - could add selection
                // menu)
                ArrayList<Functional> itemsToUse = new ArrayList<>();
                if (!availableItems.isEmpty()) {
                    System.out.println("\n" + Bcolors.BRIGHT_BLUE + "🎮 You have " + availableItems.size()
                            + " powerups available!" + Bcolors.ENDC);
                    System.out.println(Bcolors.OKBLUE + "Your powerups will be automatically used during the game."
                            + Bcolors.ENDC);
                    itemsToUse.addAll(availableItems);
                }

                // polymorphism: calling runGame on different game types
                int ticketsWon = selectedGame.runGame(itemsToUse);

                // process the transaction (deduct tokens, award tickets)
                arcadeManager.processGameTransaction(selectedGame, ticketsWon);

                // award achievement for playing games
                Achievement gameAchievement = new Achievement("Game Player",
                        "Played " + selectedGame.getTitle());
                arcadeManager.getPlayer().addAchievement(gameAchievement);

            } else {
                System.out.println(Bcolors.WARNING + "Invalid selection." + Bcolors.ENDC);
            }
        } catch (NumberFormatException e) {
            System.out.println(Bcolors.FAIL + "Please enter a valid number." + Bcolors.ENDC);
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
            System.out.println("\n\n" + Bcolors.BOLD + Bcolors.PURPLE
                    + "=== ARCADE > MAIN MENU > ITEMS & ACHIEVEMENTS ===" + Bcolors.ENDC);
            System.out.println(Bcolors.CYAN + "    1. View wallet & balance" + Bcolors.ENDC);
            System.out.println(Bcolors.CYAN + "    2. View all achievements" + Bcolors.ENDC);
            System.out.println(Bcolors.CYAN + "    3. Sort achievements alphabetically" + Bcolors.ENDC);
            System.out.println(Bcolors.CYAN + "    4. Search for specific achievement" + Bcolors.ENDC);
            System.out.println(Bcolors.YELLOW + "    5. Back to main menu" + Bcolors.ENDC);
            System.out.print(Bcolors.BOLD + Bcolors.BRIGHT_CYAN + "Enter an option: " + Bcolors.ENDC);

            try {
                int choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1:
                        System.out.println("\n\n" + Bcolors.BOLD + Bcolors.GREEN
                                + "=== ARCADE > MAIN MENU > ITEMS & ACHIEVEMENTS > WALLET ===" + Bcolors.ENDC);
                        System.out.println(Bcolors.BOLD + Bcolors.CYAN + "💳 WALLET BALANCE:" + Bcolors.ENDC);
                        System.out.println("   " + Bcolors.YELLOW + "Tokens: " + Bcolors.BRIGHT_YELLOW
                                + player.getWallet().getTokens() + Bcolors.ENDC);
                        System.out.println("   " + Bcolors.MAGENTA + "Tickets: " + Bcolors.BRIGHT_MAGENTA
                                + player.getWallet().getTickets() + Bcolors.ENDC);

                        // display powerups if any exist
                        if (player.getWallet().getPowerups() != null && !player.getWallet().getPowerups().isEmpty()) {
                            System.out.println("\n" + Bcolors.BOLD + Bcolors.BLUE + "🎮 POWERUPS:" + Bcolors.ENDC);
                            for (Functional powerup : player.getWallet().getPowerups()) {
                                System.out.println(Bcolors.OKBLUE + "   - " + powerup.getName() + " (Uses: "
                                        + powerup.getNumUses() +
                                        ", Price: " + powerup.getPrice() + " tickets)" + Bcolors.ENDC);
                            }
                        } else {
                            System.out.println("\n" + Bcolors.BOLD + Bcolors.BLUE + "🎮 POWERUPS: " + Bcolors.DIM
                                    + "None" + Bcolors.ENDC);
                        }

                        // display trophies if any exist
                        if (player.getWallet().getTrophies() != null && !player.getWallet().getTrophies().isEmpty()) {
                            System.out.println("\n" + Bcolors.BOLD + Bcolors.YELLOW + "🏆 TROPHIES:" + Bcolors.ENDC);
                            for (Achievement trophy : player.getWallet().getTrophies()) {
                                System.out.println(Bcolors.BRIGHT_YELLOW + "   - " + trophy.getName() + ": "
                                        + Bcolors.ENDC + trophy.getDescription());
                            }
                        } else {
                            System.out.println("\n" + Bcolors.BOLD + Bcolors.YELLOW + "🏆 TROPHIES: " + Bcolors.DIM
                                    + "None" + Bcolors.ENDC);
                        }
                        break;
                    case 2:
                        List<Achievement> achievements = player.getAchievements();
                        if (achievements.isEmpty()) {
                            System.out.println(Bcolors.WARNING
                                    + "You have no achievements yet. Play some games to earn them!" + Bcolors.ENDC);
                        } else {
                            System.out.println("\n\n" + Bcolors.BOLD + Bcolors.GREEN
                                    + "=== ARCADE > MAIN MENU > ITEMS & ACHIEVEMENTS > VIEW ALL ===" + Bcolors.ENDC);
                            for (Achievement achievement : achievements) {
                                System.out.println(Bcolors.GREEN + "      - " + achievement.getName() + ": "
                                        + Bcolors.ENDC + achievement.getDescription());
                            }
                        }
                        break;
                    case 3:
                        // demonstrates bubble sort algorithm
                        player.sortAchievements();
                        break;
                    case 4:
                        System.out.print(Bcolors.OKCYAN + "Enter achievement name to search: " + Bcolors.ENDC);
                        String searchName = sc.nextLine();

                        // demonstrates linear search algorithm
                        Achievement found = player.findAchievementByName(searchName);
                        if (found != null) {
                            System.out.println(Bcolors.OKGREEN + "Found: " + found.getName() + " - " + Bcolors.ENDC
                                    + found.getDescription());
                        } else {
                            System.out.println(Bcolors.WARNING + "Achievement not found." + Bcolors.ENDC);
                        }
                        break;
                    case 5:
                        viewing = false;
                        break;
                    default:
                        System.out.println(Bcolors.WARNING + "Invalid option, please try again." + Bcolors.ENDC);
                }
            } catch (NumberFormatException e) {
                System.out.println(Bcolors.FAIL + "Please enter a valid number." + Bcolors.ENDC);
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

        System.out.println("\n" + Bcolors.BOLD + Bcolors.CYAN + "=== PLAYER STATISTICS ===" + Bcolors.ENDC);
        System.out
                .println(Bcolors.OKBLUE + "Total Players: " + Bcolors.BRIGHT_WHITE + allPlayers.size() + Bcolors.ENDC);

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

        System.out.printf(Bcolors.OKBLUE + "Average Age: " + Bcolors.BRIGHT_WHITE + "%.1f years\n" + Bcolors.ENDC,
                averageAge);
        System.out.printf(
                Bcolors.OKBLUE + "Average Achievements: " + Bcolors.BRIGHT_WHITE + "%.1f per player\n" + Bcolors.ENDC,
                averageAchievements);
        System.out.println(
                Bcolors.OKBLUE + "Total Achievements: " + Bcolors.BRIGHT_WHITE + totalAchievements + Bcolors.ENDC);
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

    /**
     * handles token purchasing functionality
     * simulates buying tokens with real money
     * provides different token package options for players
     * 
     * @param arcadeManager the arcade manager instance
     * @param sc            scanner for user input
     */
    private static void buyTokens(ArcadeManager arcadeManager, Scanner sc) {
        Player player = arcadeManager.getPlayer();

        System.out.println(
                "\n\n" + Bcolors.BOLD + Bcolors.YELLOW + "=== ARCADE > MAIN MENU > BUY TOKENS ===" + Bcolors.ENDC);
        System.out.println(
                Bcolors.CYAN + "💳 Current balance: " + Bcolors.BRIGHT_YELLOW + player.getWallet().getTokens() +
                        Bcolors.CYAN + " tokens, " + Bcolors.BRIGHT_MAGENTA + player.getWallet().getTickets() +
                        Bcolors.CYAN + " tickets" + Bcolors.ENDC);
        System.out.println("\n" + Bcolors.BOLD + Bcolors.GREEN + "💰 TOKEN PACKAGES AVAILABLE:" + Bcolors.ENDC);
        System.out.println(Bcolors.OKGREEN + "  1. Small Pack - 25 tokens ($5.00)" + Bcolors.ENDC);
        System.out.println(Bcolors.OKGREEN + "  2. Medium Pack - 60 tokens ($10.00) " + Bcolors.BRIGHT_GREEN
                + "[BEST VALUE!]" + Bcolors.ENDC);
        System.out.println(Bcolors.OKGREEN + "  3. Large Pack - 100 tokens ($15.00)" + Bcolors.ENDC);
        System.out.println(Bcolors.OKGREEN + "  4. Mega Pack - 200 tokens ($25.00)" + Bcolors.ENDC);
        System.out.println(Bcolors.YELLOW + "  5. Cancel purchase" + Bcolors.ENDC);
        System.out.print(Bcolors.BOLD + Bcolors.BRIGHT_CYAN + "Select a package: " + Bcolors.ENDC);

        try {
            int choice = Integer.parseInt(sc.nextLine());
            int tokensToAdd = 0;
            String packageName = "";
            String price = "";

            switch (choice) {
                case 1:
                    tokensToAdd = 25;
                    packageName = "Small Pack";
                    price = "$5.00";
                    break;
                case 2:
                    tokensToAdd = 60;
                    packageName = "Medium Pack";
                    price = "$10.00";
                    break;
                case 3:
                    tokensToAdd = 100;
                    packageName = "Large Pack";
                    price = "$15.00";
                    break;
                case 4:
                    tokensToAdd = 200;
                    packageName = "Mega Pack";
                    price = "$25.00";
                    break;
                case 5:
                    System.out.println("Purchase cancelled.");
                    return;
                default:
                    System.out.println("Invalid selection.");
                    return;
            }

            // Confirm purchase
            System.out.println("\n💰 You selected: " + packageName + " (" + tokensToAdd + " tokens for " + price + ")");
            System.out.print(Bcolors.BOLD + Bcolors.BRIGHT_YELLOW + "Confirm purchase? (y/n): " + Bcolors.ENDC);
            String confirm = sc.nextLine().toLowerCase();

            if (confirm.startsWith("y")) {
                // Simulate payment processing
                System.out.println("💳 Processing payment...");
                try {
                    Thread.sleep(1000); // Simulate processing delay
                } catch (InterruptedException e) {
                    // Handle interruption
                }

                // Add tokens to player's wallet
                player.addTokens(tokensToAdd);

                System.out.println("✅ Payment successful! " + tokensToAdd + " tokens added to your wallet.");
                System.out.println("💳 New balance: " + player.getWallet().getTokens() + " tokens, " +
                        player.getWallet().getTickets() + " tickets");

                // Award achievement for first purchase
                Achievement purchaseAchievement = new Achievement("Big Spender",
                        "Purchased token package: " + packageName);
                arcadeManager.getPlayer().addAchievement(purchaseAchievement);
            } else {
                System.out.println("Purchase cancelled.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    /**
     * handles item and powerup shopping functionality
     * allows players to purchase functional items using tickets
     * provides different types of powerups with various effects
     * 
     * @param arcadeManager the arcade manager instance
     * @param sc            scanner for user input
     */
    private static void shopForItems(ArcadeManager arcadeManager, Scanner sc) {
        Player player = arcadeManager.getPlayer();

        boolean shopping = true;
        while (shopping) {
            System.out.println("\n\n=== ARCADE > MAIN MENU > ITEM SHOP ===");
            System.out.println("💳 Current balance: " + player.getWallet().getTokens() + " tokens, " +
                    player.getWallet().getTickets() + " tickets");
            System.out.println("\n🛍️ ITEMS AVAILABLE FOR PURCHASE:");
            System.out.println(
                    Bcolors.GREEN + "  1. 🍀 Luck Charm - Reduces game difficulty (3 uses, 20 tickets)" + Bcolors.ENDC);
            System.out.println(Bcolors.RED + "  2. ❤️  Extra Life - Gives second chance in games (1 use, 15 tickets)"
                    + Bcolors.ENDC);
            System.out.println(Bcolors.MAGENTA
                    + "  3. 🎫 Ticket Multiplier - Doubles ticket rewards (1 use, 25 tickets)" + Bcolors.ENDC);
            System.out.println(Bcolors.BRIGHT_GREEN
                    + "  4. 🍀 Super Luck Charm - Greatly reduces difficulty (2 uses, 35 tickets)" + Bcolors.ENDC);
            System.out.println(Bcolors.BRIGHT_RED + "  5. ❤️  Life Bundle - Multiple extra lives (3 uses, 40 tickets)"
                    + Bcolors.ENDC);
            System.out.println(Bcolors.BRIGHT_MAGENTA
                    + "  6. 🎫 Mega Multiplier - Triples ticket rewards (1 use, 50 tickets)" + Bcolors.ENDC);
            System.out.println(Bcolors.CYAN + "  7. View your current items" + Bcolors.ENDC);
            System.out.println(Bcolors.YELLOW + "  8. Back to main menu" + Bcolors.ENDC);
            System.out.print(Bcolors.BOLD + Bcolors.BRIGHT_CYAN + "Select an option: " + Bcolors.ENDC);

            try {
                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1:
                        purchaseItem(player, new Luck("Luck Charm", 3, 20, 2), sc);
                        break;
                    case 2:
                        purchaseItem(player, new ExtraLife("Extra Life", 1, 15), sc);
                        break;
                    case 3:
                        purchaseItem(player, new TicketMultiplier("Ticket Multiplier", 1, 25), sc);
                        break;
                    case 4:
                        purchaseItem(player, new Luck("Super Luck Charm", 2, 35, 3), sc);
                        break;
                    case 5:
                        purchaseItem(player, new ExtraLife("Life Bundle", 3, 40), sc);
                        break;
                    case 6:
                        purchaseItem(player, new TicketMultiplier("Mega Multiplier", 1, 50), sc);
                        break;
                    case 7:
                        viewCurrentItems(player);
                        break;
                    case 8:
                        shopping = false;
                        break;
                    default:
                        System.out.println("Invalid selection.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * handles the purchase of a specific functional item
     * validates player has enough tickets and processes the transaction
     * 
     * @param player the player making the purchase
     * @param item   the functional item to purchase
     * @param sc     scanner for user input
     */
    private static void purchaseItem(Player player, Functional item, Scanner sc) {
        System.out.println("\n💰 Item: " + item.getName());
        System.out.println("💰 Price: " + item.getPrice() + " tickets");
        System.out.println("💰 Uses: " + item.getNumUses());

        if (player.getWallet().getTickets() < item.getPrice()) {
            System.out.println("❌ You don't have enough tickets to buy this item!");
            System.out.println("   Required: " + item.getPrice() + " tickets");
            System.out.println("   You have: " + player.getWallet().getTickets() + " tickets");
            return;
        }

        System.out.print(Bcolors.BOLD + Bcolors.BRIGHT_YELLOW + "Confirm purchase? (y/n): " + Bcolors.ENDC);
        String confirm = sc.nextLine().toLowerCase();

        if (confirm.startsWith("y")) {
            // Deduct tickets
            player.getWallet().setTickets(player.getWallet().getTickets() - item.getPrice());

            // Add item to player's powerups (initialize list if needed)
            if (player.getWallet().getPowerups() == null) {
                player.getWallet().setPowerups(new ArrayList<>());
            }
            player.getWallet().getPowerups().add(item);

            System.out.println("✅ Purchase successful! " + item.getName() + " added to your inventory.");
            System.out.println("💳 New balance: " + player.getWallet().getTokens() + " tokens, " +
                    player.getWallet().getTickets() + " tickets");

            // Award achievement for item purchase
            Achievement shopAchievement = new Achievement("Savvy Shopper",
                    "Purchased item: " + item.getName());
            player.addAchievement(shopAchievement);
        } else {
            System.out.println("Purchase cancelled.");
        }
    }

    /**
     * displays all items currently owned by the player
     * shows powerups and trophies with their details
     * 
     * @param player the player whose items to display
     */
    private static void viewCurrentItems(Player player) {
        System.out.println("\n\n=== ARCADE > MAIN MENU > ITEM SHOP > YOUR ITEMS ===");
        System.out.println("💳 Current balance: " + player.getWallet().getTokens() + " tokens, " +
                player.getWallet().getTickets() + " tickets");

        // Display powerups
        if (player.getWallet().getPowerups() != null && !player.getWallet().getPowerups().isEmpty()) {
            System.out.println("\n🎮 YOUR POWERUPS:");
            for (Functional powerup : player.getWallet().getPowerups()) {
                System.out.println("   - " + powerup.getName() + " (Uses: " + powerup.getNumUses() +
                        ", Original Price: " + powerup.getPrice() + " tickets)");
            }
        } else {
            System.out.println("\n🎮 YOUR POWERUPS: None");
            System.out.println("   Purchase some powerups to enhance your gaming experience!");
        }

        // Display trophies
        if (player.getWallet().getTrophies() != null && !player.getWallet().getTrophies().isEmpty()) {
            System.out.println("\n🏆 YOUR TROPHIES:");
            for (Achievement trophy : player.getWallet().getTrophies()) {
                System.out.println("   - " + trophy.getName() + ": " + trophy.getDescription());
            }
        } else {
            System.out.println("\n🏆 YOUR TROPHIES: None");
            System.out.println("   Complete special achievements to earn trophies!");
        }
    }
}
