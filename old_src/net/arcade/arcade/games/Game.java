/*
 * Game.java
 *
 * Date: 05 27, 2025
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
package net.arcade.arcade.games;

import java.io.*;
import java.util.*;

import net.arcade.arcade.item.Functional;

public abstract class Game {
    private int id;
    private String title;
    private int difficulty; // 1-10 scale
    private int requiredTokens;
    private int ticketReward;

    public Game(int id, String title, int difficulty, int requiredTokens, int ticketReward) {
        this.id = id;
        this.title = title;
        this.difficulty = difficulty;
        this.requiredTokens = requiredTokens;
        this.ticketReward = ticketReward;
    }

    public abstract int runGame(ArrayList<Functional> useItems);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        if (difficulty < 1 || difficulty > 10) {
            throw new IllegalArgumentException("Difficulty must be between 1 and 10.");
        }
        this.difficulty = difficulty;
    }

    public int getRequiredTokens() {
        return requiredTokens;
    }

    public void setRequiredTokens(int requiredTokens) {
        if (requiredTokens < 0) {
            throw new IllegalArgumentException("Required tokens cannot be negative.");
        }
        this.requiredTokens = requiredTokens;
    }

    public int getTicketReward() {
        return ticketReward;
    }

    public void setTicketReward(int ticketReward) {
        if (ticketReward < 0) {
            throw new IllegalArgumentException("Ticket reward cannot be negative.");
        }
        this.ticketReward = ticketReward;
    }
}
