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
import com.arcade.player.Player;

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
    }

    public LoginStatus tryLogin(String username, String password) {
        // First check if player with username exists
        Player p = searchForPlayer(username);
        if (p != null) {
            // Player exists, check password
            if (p.getPassword().equals(password)) {
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
        if (player == null) {
            System.err.println("Player is null, cannot save to file.");
            return false;
        }
        if (player.getUsername() == null || player.getPassword() == null || player.getName() == null) {
            System.err.println("Player data is incomplete, cannot save to file.");
            return false;
        }
        this.players = loadFromFile();
        // ~~TODONE: load file first then append new player data~~
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCADE_FILE))) {
            // writer.write(player.getUsername() + "\n");
            // writer.write(player.getPassword() + "\n");
            // writer.write(player.getAge() + "\n");
            // writer.write(player.getName() + "\n");
            for (Player p : players) {
                writer.write(p.getUsername() + "\n");
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

                Player p = new Player();
                p.setUsername(username);
                p.setPassword(password);
                p.setAge(age);
                p.setName(name);
                players.add(p);
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
}
