/*
 * ArcadeManager.java
 *
 * Date: 05 30, 2025
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

import com.arcade.games.Game;
import com.arcade.games.blackjack.BlackJack;
import com.arcade.games.pokeman.PokemanGame;
import com.arcade.games.diceopoly.Diceopoly;
import com.arcade.games.escaperoom.EscapeRoom;
import com.arcade.games.trivia.Trivia;
import com.arcade.player.Player;
import com.arcade.item.Achievement;
import com.arcade.item.Functional;

public class ArcadeManager {
    // TODO: Store instances of the games
    private static String ARCADE_FILE = "arcade.txt";
    private Player player;
    private List<Player> players;
    private List<Game> games;

    public static enum LoginStatus {
        INCORRECT_PASSWORD, USERNAME_NOT_FOUND, SUCCESS
    }

    public ArcadeManager() {
        // initialize games here
        this.games = new ArrayList<>();

        // Add all available game instances with variable rewards
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
    }

    public LoginStatus tryLogin(String username, String password) {
        // First check if player with username exists
        Player p = searchForPlayer(username);
        if (p != null) {
            // Player exists, check password
            if (p.getPassword().equals(password)) {
                // set the current player
                this.player = p;
                return LoginStatus.SUCCESS;
            } else {
                return LoginStatus.INCORRECT_PASSWORD;
            }
        }
        return LoginStatus.USERNAME_NOT_FOUND;
    }

    public Player searchForPlayer(String username) {
        if (username == null || username.isEmpty()) {
            System.err.println("Username cannot be null or empty.");
            return null;
        }
        this.players = loadFromFile();
        for (Player p : players) {
            if (p.getUsername().equals(username)) {
                return p;
            }
        }
        System.err.println("Player with username " + username + " not found.");
        return null;
    }

    public List<Player> searchForPlayersByName(String name) {
        if (name == null || name.isEmpty()) {
            System.err.println("Name cannot be null or empty.");
            return Collections.emptyList();
        }
        this.players = loadFromFile();
        List<Player> foundPlayers = new ArrayList<>();
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
        for (Player p : players) {
            if (p.getUsername().equals(player.getUsername())) {
                System.err.println("Player with username " + player.getUsername() + " already exists.");
                return false;
            }
        }
        players.add(player);
        return saveToFile();
    }

    public boolean removePlayer(String username) {
        if (username == null || username.isEmpty()) {
            System.err.println("Username cannot be null or empty.");
            return false;
        }
        this.players = loadFromFile();
        Iterator<Player> iterator = players.iterator();
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

    public boolean saveToFile() {
        if (players == null || players.isEmpty()) {
            this.players = loadFromFile();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCADE_FILE))) {
            for (Player p : players) {
                writer.write(p.getUsername().toLowerCase() + "\n");
                writer.write(p.getPassword() + "\n");
                writer.write(p.getAge() + "\n");
                writer.write(p.getName() + "\n");
                writer.write(":\n"); // Separator for players
            }
            writer.write("end\n"); // End of file marker
            return true;
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
            return false;
        }
    }

    public List<Player> loadFromFile() {
        List<Player> players = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCADE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("end"))
                    break; // stop at end marker
                // TODO: DO WITHOUT BREAK AND CONTINUE
                if (line.equals(":"))
                    continue; // skip separators

                String username = line;
                String password = reader.readLine();
                String ageLine = reader.readLine();
                String name = reader.readLine();
                reader.readLine(); // consume the ":" after each record

                int age;
                try {
                    age = Integer.parseInt(ageLine);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid age for user " + username + ": " + ageLine);
                    continue;
                }

                // Player p = new Player();
                // p.setUsername(username.toLowerCase());
                // p.setPassword(password);
                // p.setAge(age);
                // p.setName(name);
                Player p = new Player(name, username.toLowerCase(), password, age);
                players.add(p);
                System.out.println(
                        "Found player: " + p.getUsername() + ", Age: " + p.getAge() + ", Name: " + p.getName());
                System.out.println("Player password: " + p.getPassword()); // Debugging line
            }
        } catch (IOException e) {
            System.err.println("Error loading from file: " + e.getMessage());
        }
        return players;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public static String getArcadeFile() {
        return ARCADE_FILE;
    }

    public static void setArcadeFile(String arcadeFile) {
        ARCADE_FILE = arcadeFile;
    }

    // Check if current player is admin (username "admin")
    public boolean isAdmin() {
        return player != null && "admin".equals(player.getUsername());
    }

    // NEW METHODS FOR ENHANCED FUNCTIONALITY

    /**
     * Quick Sort implementation for sorting players by username
     * Demonstrates: Sorting Algorithm #1, Recursion
     */
    public void sortPlayersByUsername() {
        this.players = loadFromFile();
        Player[] playerArray = players.toArray(new Player[0]);
        quickSortPlayers(playerArray, 0, playerArray.length - 1);
        this.players = Arrays.asList(playerArray);
    }

    private void quickSortPlayers(Player[] arr, int low, int high) {
        if (low < high) {
            int pi = partitionPlayers(arr, low, high);
            quickSortPlayers(arr, low, pi - 1); // Recursion
            quickSortPlayers(arr, pi + 1, high); // Recursion
        }
    }

    private int partitionPlayers(Player[] arr, int low, int high) {
        String pivot = arr[high].getUsername();
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (arr[j].getUsername().compareToIgnoreCase(pivot) <= 0) {
                i++;
                Player temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        Player temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    /**
     * Merge Sort implementation for sorting players by age
     * Demonstrates: Sorting Algorithm #2, Recursion
     */
    public void sortPlayersByAge() {
        this.players = loadFromFile();
        Player[] playerArray = players.toArray(new Player[0]);
        mergeSortPlayersByAge(playerArray, 0, playerArray.length - 1);
        this.players = Arrays.asList(playerArray);
    }

    private void mergeSortPlayersByAge(Player[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortPlayersByAge(arr, left, mid); // Recursion
            mergeSortPlayersByAge(arr, mid + 1, right); // Recursion
            mergePlayersByAge(arr, left, mid, right);
        }
    }

    private void mergePlayersByAge(Player[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        Player[] leftArr = new Player[n1];
        Player[] rightArr = new Player[n2];

        System.arraycopy(arr, left, leftArr, 0, n1);
        System.arraycopy(arr, mid + 1, rightArr, 0, n2);

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if (leftArr[i].getAge() <= rightArr[j].getAge()) {
                arr[k] = leftArr[i];
                i++;
            } else {
                arr[k] = rightArr[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr[k] = leftArr[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = rightArr[j];
            j++;
            k++;
        }
    }

    /**
     * Binary Search implementation for searching players by username
     * Demonstrates: Searching Algorithm #1, requires sorted array
     */
    public Player binarySearchPlayerByUsername(String username) {
        sortPlayersByUsername(); // Ensure sorted
        Player[] playerArray = players.toArray(new Player[0]);
        return binarySearchRecursive(playerArray, username.toLowerCase(), 0, playerArray.length - 1);
    }

    private Player binarySearchRecursive(Player[] arr, String target, int left, int right) {
        if (left <= right) {
            int mid = left + (right - left) / 2;

            int comparison = arr[mid].getUsername().compareToIgnoreCase(target);

            if (comparison == 0) {
                return arr[mid];
            } else if (comparison > 0) {
                return binarySearchRecursive(arr, target, left, mid - 1); // Recursion
            } else {
                return binarySearchRecursive(arr, target, mid + 1, right); // Recursion
            }
        }
        return null;
    }

    /**
     * Linear Search implementation for searching players by age range
     * Demonstrates: Searching Algorithm #2
     */
    public List<Player> linearSearchPlayersByAgeRange(int minAge, int maxAge) {
        this.players = loadFromFile();
        List<Player> result = new ArrayList<>();

        for (Player p : players) {
            if (p.getAge() >= minAge && p.getAge() <= maxAge) {
                result.add(p);
            }
        }

        return result;
    }

    /**
     * Recursive method to calculate factorial (demonstration of recursion)
     * Used for calculating achievement points
     */
    public int calculateFactorial(int n) {
        if (n <= 1) {
            return 1;
        }
        return n * calculateFactorial(n - 1); // Recursion
    }

    /**
     * Display all players with formatting
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

        for (Player p : players) {
            System.out.printf("%-15s\t%-15s\t%d\t%d\n",
                    p.getUsername(),
                    p.getName(),
                    p.getAge(),
                    p.getAchievements().size());
        }
    }

    /**
     * Display players sorted by username
     */
    public void displayPlayersSortedByUsername() {
        sortPlayersByUsername();
        System.out.println("\n=== PLAYERS SORTED BY USERNAME ===");
        displayAllPlayers();
    }

    /**
     * Display players sorted by age
     */
    public void displayPlayersSortedByAge() {
        sortPlayersByAge();
        System.out.println("\n=== PLAYERS SORTED BY AGE ===");
        displayAllPlayers();
    }

    /**
     * Search and display players by age range
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

        for (Player p : results) {
            System.out.printf("%-15s\t%-15s\t%d\n",
                    p.getUsername(),
                    p.getName(),
                    p.getAge());
        }
    }

    // Get games list for playing
    public List<Game> getGames() {
        return games;
    }

    /**
     * Calculate difficulty adjustment based on player age
     * Players aged 20-30 have highest difficulty (no adjustment)
     * Younger or older players get reduced difficulty
     * 
     * @param baseGameDifficulty The original difficulty of the game
     * @param playerAge          The age of the current player
     * @return The adjusted difficulty (clamped between 1-10)
     */
    public int calculateAgeBasedDifficulty(int baseGameDifficulty, int playerAge) {
        if (playerAge >= 20 && playerAge <= 30) {
            // Players in their 20s-30s get full difficulty
            return baseGameDifficulty;
        }

        // Calculate how far the player is from the optimal age range (20-30)
        int distanceFromOptimal;
        if (playerAge < 20) {
            distanceFromOptimal = 20 - playerAge;
        } else {
            distanceFromOptimal = playerAge - 30;
        }

        // Reduce difficulty based on distance from optimal age
        // Each 5 years away reduces difficulty by 1 level
        int difficultyReduction = distanceFromOptimal / 5;

        // Apply the reduction
        int adjustedDifficulty = baseGameDifficulty - difficultyReduction;

        // Ensure difficulty stays within valid range (1-10)
        return Math.max(1, Math.min(10, adjustedDifficulty));
    }

    /**
     * Adjust game difficulty based on current player's age
     * This method modifies the game's difficulty setting
     * 
     * @param game The game to adjust
     */
    public void adjustGameDifficultyForCurrentPlayer(Game game) {
        if (player == null) {
            System.err.println("No current player set, cannot adjust difficulty");
            return;
        }

        // Store the original difficulty as a backup
        int originalDifficulty = game.getDifficulty();

        // Calculate and apply age-based difficulty
        int adjustedDifficulty = calculateAgeBasedDifficulty(originalDifficulty, player.getAge());
        game.setDifficulty(adjustedDifficulty);

        // Display the adjustment to the player
        if (adjustedDifficulty != originalDifficulty) {
            System.out.println("📊 Difficulty adjusted for your age (" + player.getAge() + "): "
                    + originalDifficulty + " → " + adjustedDifficulty);
        }
    }

    // Get game by ID
    public Game getGameById(int id) {
        for (Game game : games) {
            if (game.getId() == id) {
                return game;
            }
        }
        return null;
    }

    /**
     * Check if the current player can afford to play a game
     */
    public boolean canPlayerAffordGame(Game game) {
        return player != null && player.hasEnoughTokens(game.getRequiredTokens());
    }

    /**
     * Process game payment and award tickets
     * 
     * @param game       The game being played
     * @param ticketsWon The number of tickets won
     * @return true if transaction successful, false otherwise
     */
    public boolean processGameTransaction(Game game, int ticketsWon) {
        if (player == null) {
            System.err.println("No player logged in for transaction.");
            return false;
        }

        // Deduct tokens (this should have been checked before calling this method)
        if (!player.spendTokens(game.getRequiredTokens())) {
            System.err.println("Player cannot afford this game.");
            return false;
        }

        // Award tickets
        player.addTickets(ticketsWon);

        System.out.println(
                "💰 Transaction complete: -" + game.getRequiredTokens() + " tokens, +" + ticketsWon + " tickets");
        System.out.println("💳 Current balance: " + player.getWallet().getTokens() + " tokens, " +
                player.getWallet().getTickets() + " tickets");

        return true;
    }
}
