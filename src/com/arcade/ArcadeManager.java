/**
 * ArcadeManager.java
 *
 * central management system for the arcade gaming platform
 * handles player authentication, data persistence, game management,
 * and provides various sorting and searching algorithms
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

import com.arcade.games.Game;
import com.arcade.games.blackjack.BlackJack;
import com.arcade.games.pokeman.PokemanGame;
import com.arcade.games.diceopoly.Diceopoly;
import com.arcade.games.escaperoom.EscapeRoom;
import com.arcade.games.trivia.Trivia;
import com.arcade.games.madlibs.MadLibs;
import com.arcade.player.Player;
import com.arcade.item.Achievement;
import com.arcade.item.Functional;

/**
 * manages all arcade operations including player authentication,
 * data persistence, game instances, and provides various algorithms
 * for sorting and searching player data
 */
public class ArcadeManager {
    // persistent storage file for player data
    private static String ARCADE_FILE = "arcade.txt";
    private Player player; // currently logged in player
    private List<Player> players; // all registered players
    private List<Game> games; // available games in the arcade

    /**
     * enumeration for different login attempt outcomes
     * used to provide specific feedback to users during authentication
     */
    public static enum LoginStatus {
        INCORRECT_PASSWORD, USERNAME_NOT_FOUND, SUCCESS
    }

    /**
     * constructor that initializes the arcade with all available games
     * sets up game instances with their respective difficulties and rewards
     */
    public ArcadeManager() {
        // initialize games collection
        this.games = new ArrayList<>();

        // add all available game instances with variable rewards
        // each game has different difficulty levels and reward ranges
        BlackJack blackjack = new BlackJack(1, "Blackjack", 10, 10, 20);
        blackjack.setMinTicketReward(10);
        blackjack.setMaxTicketReward(30);
        games.add(blackjack);

        PokemanGame pokemon = new PokemanGame(2, "Pokemon Battle", 8, 15, 25);
        pokemon.setMinTicketReward(15);
        pokemon.setMaxTicketReward(40);
        games.add(pokemon);

        Diceopoly diceopoly = new Diceopoly(3, "Diceopoly", 6, 20, 35);
        diceopoly.setMinTicketReward(20);
        diceopoly.setMaxTicketReward(50);
        games.add(diceopoly);

        Trivia trivia = new Trivia(4, "Trivia Challenge", 5, 8, 16);
        trivia.setMinTicketReward(8);
        trivia.setMaxTicketReward(25);
        games.add(trivia);

        EscapeRoom escapeRoom = new EscapeRoom(5, "Escape Room", 9, 25, 42);
        escapeRoom.setMinTicketReward(25);
        escapeRoom.setMaxTicketReward(60);
        games.add(escapeRoom);

        MadLibs madLibs = new MadLibs(6, "Mad Libs", 5, 12, 18);
        madLibs.setMinTicketReward(12);
        madLibs.setMaxTicketReward(25);
        games.add(madLibs);
    }

    /**
     * attempts to authenticate a user with username and password
     * compares hashed passwords for security
     * 
     * @param username the username to authenticate
     * @param password the hashed password to verify
     * @return login status indicating success or failure reason
     */
    public LoginStatus tryLogin(String username, String password) {
        // first check if player with username exists
        Player p = searchForPlayer(username);
        if (p != null) {
            // player exists, check if password matches stored hash
            if (p.getPassword().equals(password)) {
                // set the current player for this session
                this.player = p;
                return LoginStatus.SUCCESS;
            } else {
                return LoginStatus.INCORRECT_PASSWORD;
            }
        }
        return LoginStatus.USERNAME_NOT_FOUND;
    }

    /**
     * searches for a player by username using linear search
     * loads player data from file if not already loaded
     * 
     * @param username the username to search for (case-sensitive)
     * @return the player object if found, null otherwise
     */
    public Player searchForPlayer(String username) {
        if (username == null || username.isEmpty()) {
            System.err.println("Username cannot be null or empty.");
            return null;
        }

        // ensure player data is loaded from file
        this.players = loadFromFile();

        // linear search through all players
        for (Player p : players) {
            if (p.getUsername().equals(username)) {
                return p;
            }
        }
        System.err.println("Player with username " + username + " not found.");
        return null;
    }

    /**
     * searches for players by name using case-insensitive matching
     * can return multiple players with the same name
     * 
     * @param name the name to search for
     * @return list of players with matching names (empty if none found)
     */
    public List<Player> searchForPlayersByName(String name) {
        if (name == null || name.isEmpty()) {
            System.err.println("Name cannot be null or empty.");
            return Collections.emptyList();
        }

        this.players = loadFromFile();
        List<Player> foundPlayers = new ArrayList<>();

        // search through all players for matching names
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(name)) {
                foundPlayers.add(p);
            }
        }

        if (foundPlayers.isEmpty()) {
            System.err.println("No players found with name " + name + ".");
        }
        return foundPlayers;
    }

    /**
     * adds a new player to the system and saves to file
     * performs validation to ensure no duplicate usernames
     * 
     * @param player the player object to add
     * @return true if successfully added, false otherwise
     */
    public boolean addPlayer(Player player) {
        if (player == null) {
            System.err.println("Player is null, cannot add to file.");
            return false;
        }
        if (player.getUsername() == null || player.getPassword() == null || player.getName() == null) {
            System.err.println("Player data is incomplete, cannot add to file.");
            return false;
        }

        this.players = loadFromFile();

        // check for duplicate usernames
        for (Player p : players) {
            if (p.getUsername().equals(player.getUsername())) {
                System.err.println("Player with username " + player.getUsername() + " already exists.");
                return false;
            }
        }

        players.add(player);
        return saveToFile();
    }

    /**
     * removes a player from the system by username
     * updates the persistent storage after removal
     * 
     * @param username the username of the player to remove
     * @return true if successfully removed, false otherwise
     */
    public boolean removePlayer(String username) {
        if (username == null || username.isEmpty()) {
            System.err.println("Username cannot be null or empty.");
            return false;
        }

        this.players = loadFromFile();
        Iterator<Player> iterator = players.iterator();

        // iterate safely while potentially removing elements
        while (iterator.hasNext()) {
            Player p = iterator.next();
            if (p.getUsername().equals(username)) {
                iterator.remove();
                return saveToFile();
            }
        }

        System.err.println("Player with username " + username + " not found.");
        return false;
    }

    /**
     * saves all player data to the persistent storage file
     * uses a specific format with separators for easy parsing
     * 
     * @return true if save successful, false otherwise
     */
    public boolean saveToFile() {
        if (players == null || players.isEmpty()) {
            this.players = loadFromFile();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCADE_FILE))) {
            // write each player's data in a structured format
            for (Player p : players) {
                writer.write(p.getUsername().toLowerCase() + "\n");
                writer.write(p.getPassword() + "\n");
                writer.write(p.getAge() + "\n");
                writer.write(p.getName() + "\n");
                writer.write(p.getWallet().getTokens() + "\n");
                writer.write(p.getWallet().getTickets() + "\n");
                writer.write(":\n"); // separator between players
            }
            writer.write("end\n"); // end of file marker
            return true;
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
            return false;
        }
    }

    /**
     * loads all player data from the persistent storage file
     * parses the structured format and creates player objects
     * supports both old format (4 fields) and new format (6 fields) for backward
     * compatibility
     * 
     * @return list of all players loaded from file
     */
    public List<Player> loadFromFile() {
        List<Player> players = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCADE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("end"))
                    break; // stop at end marker

                // skip separator lines
                if (line.equals(":"))
                    continue;

                // read basic player data: username, password, age, name
                String username = line;
                String password = reader.readLine();
                String ageLine = reader.readLine();
                String name = reader.readLine();

                // peek at the next line to determine format
                String nextLine = reader.readLine();

                int age;
                int tokens = 50; // default starting tokens
                int tickets = 0; // default starting tickets

                try {
                    age = Integer.parseInt(ageLine);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid age for user " + username + ": " + ageLine);
                    continue;
                }

                // check if we have the new format (tokens and tickets) or old format
                if (nextLine != null && !nextLine.equals(":")) {
                    // new format: nextLine should be tokens
                    try {
                        tokens = Integer.parseInt(nextLine);
                        String ticketsLine = reader.readLine();
                        if (ticketsLine != null && !ticketsLine.equals(":")) {
                            tickets = Integer.parseInt(ticketsLine);
                            reader.readLine(); // consume the ":" separator
                        }
                    } catch (NumberFormatException e) {
                        // if parsing fails, treat as old format and reset to defaults
                        System.out.println("Note: Using default wallet values for user " + username +
                                " (old format detected)");
                        tokens = 50;
                        tickets = 0;
                        // nextLine was ":", so we don't need to read another separator
                    }
                }
                // if nextLine was ":", we already have the separator and use default values

                // create player object with loaded data
                Player p = new Player(name, username.toLowerCase(), password, age);
                // set wallet balances
                p.getWallet().setTokens(tokens);
                p.getWallet().setTickets(tickets);
                players.add(p);
                // System.out.println(
                // "Found player: " + p.getUsername() + ", Age: " + p.getAge() + ", Name: " +
                // p.getName() + ", Tokens: " + tokens + ", Tickets: " + tickets);
            }
        } catch (IOException e) {
            System.err.println("Error loading from file: " + e.getMessage());
        }

        // if we loaded players and detected old format, automatically save in new
        // format
        if (!players.isEmpty()) {
            this.players = players;
            saveToFile(); // this will save in the new format with tokens and tickets
        }

        return players;
    }

    /**
     * gets the currently logged in player
     * 
     * @return the current player object
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * sets the currently logged in player
     * 
     * @param player the player to set as current
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * gets the list of all registered players
     * 
     * @return list of all players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * sets the list of registered players
     * 
     * @param players the list of players to set
     */
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    /**
     * gets the arcade data file name
     * 
     * @return the file name used for persistent storage
     */
    public static String getArcadeFile() {
        return ARCADE_FILE;
    }

    /**
     * sets the arcade data file name
     * 
     * @param arcadeFile the file name to use for persistent storage
     */
    public static void setArcadeFile(String arcadeFile) {
        ARCADE_FILE = arcadeFile;
    }

    /**
     * checks if the current player has admin privileges
     * admin is identified by the username "admin"
     * 
     * @return true if current player is admin, false otherwise
     */
    public boolean isAdmin() {
        return player != null && "admin".equals(player.getUsername());
    }

    /**
     * sorts players by username using selection sort algorithm
     * demonstrates simple sorting with o(n²) complexity
     * modifies the internal players list
     */
    public void sortPlayersByUsername() {
        this.players = loadFromFile();
        Player[] playerArray = players.toArray(new Player[0]);
        selectionSortPlayersByUsername(playerArray);
        this.players = Arrays.asList(playerArray);
    }

    /**
     * selection sort implementation for player objects by username
     * finds the minimum element and places it at the beginning
     * 
     * @param arr the array of players to sort
     */
    private void selectionSortPlayersByUsername(Player[] arr) {
        int n = arr.length;

        // traverse through all array elements
        for (int i = 0; i < n - 1; i++) {
            // find the minimum element in the remaining unsorted array
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j].getUsername().compareToIgnoreCase(arr[minIndex].getUsername()) < 0) {
                    minIndex = j;
                }
            }

            // swap the found minimum element with the first element
            if (minIndex != i) {
                Player temp = arr[minIndex];
                arr[minIndex] = arr[i];
                arr[i] = temp;
            }
        }
    }

    /**
     * sorts players by age using insertion sort algorithm
     * demonstrates simple sorting by building the sorted array one element at a
     * time
     * modifies the internal players list
     */
    public void sortPlayersByAge() {
        this.players = loadFromFile();
        Player[] playerArray = players.toArray(new Player[0]);
        insertionSortPlayersByAge(playerArray);
        this.players = Arrays.asList(playerArray);
    }

    /**
     * insertion sort implementation for player objects by age
     * builds the final sorted array one element at a time
     * 
     * @param arr the array of players to sort
     */
    private void insertionSortPlayersByAge(Player[] arr) {
        int n = arr.length;

        // traverse from the second element to the end
        for (int i = 1; i < n; i++) {
            Player key = arr[i];
            int j = i - 1;

            // move elements of arr[0..i-1] that are greater than key
            // one position ahead of their current position
            while (j >= 0 && arr[j].getAge() > key.getAge()) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }

            // place key at its correct position
            arr[j + 1] = key;
        }
    }

    /**
     * searches for a player by username using binary search algorithm
     * requires the player list to be sorted by username first
     * demonstrates efficient searching with o(log n) complexity
     * 
     * @param username the username to search for
     * @return the player object if found, null otherwise
     */
    public Player binarySearchPlayerByUsername(String username) {
        sortPlayersByUsername(); // ensure the array is sorted first
        Player[] playerArray = players.toArray(new Player[0]);
        return binarySearchRecursive(playerArray, username.toLowerCase(), 0, playerArray.length - 1);
    }

    /**
     * recursive binary search implementation for player objects
     * divides search space in half with each iteration
     * 
     * @param arr    the sorted array of players
     * @param target the username to find
     * @param left   the left boundary of current search space
     * @param right  the right boundary of current search space
     * @return the player if found, null otherwise
     */
    private Player binarySearchRecursive(Player[] arr, String target, int left, int right) {
        if (left <= right) {
            // calculate middle index
            int mid = left + (right - left) / 2;

            // compare middle element with target
            int comparison = arr[mid].getUsername().compareToIgnoreCase(target);

            if (comparison == 0) {
                return arr[mid]; // found the target
            } else if (comparison > 0) {
                // target is in the left half
                return binarySearchRecursive(arr, target, left, mid - 1); // recursion
            } else {
                // target is in the right half
                return binarySearchRecursive(arr, target, mid + 1, right); // recursion
            }
        }
        return null; // target not found
    }

    /**
     * searches for players within a specific age range using linear search
     * demonstrates simple searching algorithm with o(n) complexity
     * 
     * @param minAge the minimum age (inclusive)
     * @param maxAge the maximum age (inclusive)
     * @return list of players within the age range
     */
    public List<Player> linearSearchPlayersByAgeRange(int minAge, int maxAge) {
        this.players = loadFromFile();
        List<Player> result = new ArrayList<>();

        // check each player's age against the range
        for (Player p : players) {
            if (p.getAge() >= minAge && p.getAge() <= maxAge) {
                result.add(p);
            }
        }

        return result;
    }

    /**
     * calculates factorial using recursion
     * used for calculating achievement points based on number of achievements
     * demonstrates recursive mathematical computation
     * 
     * @param n the number to calculate factorial for
     * @return the factorial of n
     */
    public int calculateFactorial(int n) {
        if (n <= 1) {
            return 1; // base case: 0! = 1! = 1
        }
        return n * calculateFactorial(n - 1); // recursive case
    }

    /**
     * displays all players in a formatted table
     * loads player data if not already available
     */
    public void displayAllPlayers() {
        this.players = loadFromFile();
        if (players.isEmpty()) {
            System.out.println("No players found.");
            return;
        }

        System.out.println("\n=== ALL PLAYERS ===");
        System.out.println("Username\t\tName\t\tAge\tAchievements");
        System.out.println("--------------------------------------------------------");

        // display each player's information in tabular format
        for (Player p : players) {
            System.out.printf("%-15s\t%-15s\t%d\t%d\n",
                    p.getUsername(),
                    p.getName(),
                    p.getAge(),
                    p.getAchievements().size());
        }
    }

    /**
     * displays all players sorted alphabetically by username
     * uses selection sort algorithm for sorting
     */
    public void displayPlayersSortedByUsername() {
        sortPlayersByUsername();
        System.out.println("\n=== PLAYERS SORTED BY USERNAME ===");
        displayAllPlayers();
    }

    /**
     * displays all players sorted by age
     * uses insertion sort algorithm for sorting
     */
    public void displayPlayersSortedByAge() {
        sortPlayersByAge();
        System.out.println("\n=== PLAYERS SORTED BY AGE ===");
        displayAllPlayers();
    }

    /**
     * searches and displays players within a specified age range
     * uses linear search algorithm
     * 
     * @param minAge the minimum age to search for
     * @param maxAge the maximum age to search for
     */
    public void searchPlayersByAgeRange(int minAge, int maxAge) {
        List<Player> results = linearSearchPlayersByAgeRange(minAge, maxAge);

        if (results.isEmpty()) {
            System.out.println("No players found in age range " + minAge + "-" + maxAge);
            return;
        }

        System.out.println("\n=== PLAYERS IN AGE RANGE " + minAge + "-" + maxAge + " ===");
        System.out.println("Username\t\tName\t\tAge");
        System.out.println("----------------------------------------");

        // display matching players
        for (Player p : results) {
            System.out.printf("%-15s\t%-15s\t%d\n",
                    p.getUsername(),
                    p.getName(),
                    p.getAge());
        }
    }

    /**
     * gets the list of available games in the arcade
     * 
     * @return list of all game instances
     */
    public List<Game> getGames() {
        return games;
    }

    /**
     * calculates difficulty adjustment based on player age
     * players aged 20-30 have highest difficulty (no adjustment)
     * younger or older players get reduced difficulty for better experience
     * 
     * @param baseGameDifficulty the original difficulty of the game
     * @param playerAge          the age of the current player
     * @return the adjusted difficulty (clamped between 1-10)
     */
    public int calculateAgeBasedDifficulty(int baseGameDifficulty, int playerAge) {
        if (playerAge >= 20 && playerAge <= 30) {
            // players in their 20s-30s get full difficulty
            return baseGameDifficulty;
        }

        // calculate how far the player is from the optimal age range (20-30)
        int distanceFromOptimal;
        if (playerAge < 20) {
            distanceFromOptimal = 20 - playerAge;
        } else {
            distanceFromOptimal = playerAge - 30;
        }

        // reduce difficulty based on distance from optimal age
        // each 5 years away reduces difficulty by 1 level
        int difficultyReduction = distanceFromOptimal / 5;

        // apply the reduction
        int adjustedDifficulty = baseGameDifficulty - difficultyReduction;

        // ensure difficulty stays within valid range (1-10)
        return Math.max(1, Math.min(10, adjustedDifficulty));
    }

    /**
     * adjusts game difficulty based on current player's age
     * modifies the game's difficulty setting directly
     * provides feedback to the player about the adjustment
     * 
     * @param game the game instance to adjust
     */
    public void adjustGameDifficultyForCurrentPlayer(Game game) {
        if (player == null) {
            System.err.println("No current player set, cannot adjust difficulty");
            return;
        }

        // store the original difficulty as a backup reference
        int originalDifficulty = game.getDifficulty();

        // calculate and apply age-based difficulty adjustment
        int adjustedDifficulty = calculateAgeBasedDifficulty(originalDifficulty, player.getAge());
        game.setDifficulty(adjustedDifficulty);

        // inform the player about the adjustment if it occurred
        if (adjustedDifficulty != originalDifficulty) {
            System.out.println("📊 Difficulty adjusted for your age (" + player.getAge() + "): "
                    + originalDifficulty + " → " + adjustedDifficulty);
        }
    }

    /**
     * finds a game by its unique identifier
     * 
     * @param id the game id to search for
     * @return the game object if found, null otherwise
     */
    public Game getGameById(int id) {
        for (Game game : games) {
            if (game.getId() == id) {
                return game;
            }
        }
        return null;
    }

    /**
     * checks if the current player has enough tokens to play a game
     * 
     * @param game the game to check affordability for
     * @return true if player can afford the game, false otherwise
     */
    public boolean canPlayerAffordGame(Game game) {
        return player != null && player.hasEnoughTokens(game.getRequiredTokens());
    }

    /**
     * processes a game transaction by deducting tokens and awarding tickets
     * handles the financial aspect of playing games
     * 
     * @param game       the game that was played
     * @param ticketsWon the number of tickets earned from the game
     * @return true if transaction successful, false otherwise
     */
    public boolean processGameTransaction(Game game, int ticketsWon) {
        if (player == null) {
            System.err.println("No player logged in for transaction.");
            return false;
        }

        // deduct tokens (this should have been checked before calling this method)
        if (!player.spendTokens(game.getRequiredTokens())) {
            System.err.println("Player cannot afford this game.");
            return false;
        }

        // award tickets based on game performance
        player.addTickets(ticketsWon);

        // provide transaction feedback to the player
        System.out.println(
                "💰 Transaction complete: -" + game.getRequiredTokens() + " tokens, +" + ticketsWon + " tickets");
        System.out.println("💳 Current balance: " + player.getWallet().getTokens() + " tokens, " +
                player.getWallet().getTickets() + " tickets");

        return true;
    }
}
