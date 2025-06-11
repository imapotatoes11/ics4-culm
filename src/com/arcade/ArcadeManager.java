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
import java.util.*;

import com.arcade.games.Game;
import com.arcade.player.Player;

public class ArcadeManager {
    // TODO: Store instances of the games
    private static String ARCADE_FILE = "arcade.txt";
    private Player player;
    private List<Player> players;
    private List<Game> games;

    public ArcadeManager(Player player) {
        this.player = player;
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
            String username;
            while ((username = reader.readLine()) != null) {
                String password = reader.readLine();
                int age = Integer.parseInt(reader.readLine());
                String name = reader.readLine();
                Player player = new Player();
                player.setUsername(username);
                player.setPassword(password);
                player.setAge(age);
                player.setName(name);
                players.add(player);
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
