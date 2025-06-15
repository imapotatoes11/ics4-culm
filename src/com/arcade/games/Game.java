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
package com.arcade.games;

import java.util.*;
import com.arcade.item.Functional;;

public abstract class Game {
    private int id;
    private String title;
    private int difficulty; // 1-10 scale
    private int requiredTokens;
    private int ticketReward; // base/average ticket reward
    private int minTicketReward; // minimum ticket reward
    private int maxTicketReward; // maximum ticket reward

    public Game(int id, String title, int difficulty, int requiredTokens, int ticketReward) {
        this.id = id;
        this.title = title;
        this.difficulty = difficulty;
        this.requiredTokens = requiredTokens;
        this.ticketReward = ticketReward;
        // Set default min/max as 50% and 150% of base reward
        this.minTicketReward = Math.max(1, ticketReward / 2);
        this.maxTicketReward = ticketReward + (ticketReward / 2);
    }

    public Game(int id, String title, int difficulty, int requiredTokens, int minTicketReward, int maxTicketReward) {
        this.id = id;
        this.title = title;
        this.difficulty = difficulty;
        this.requiredTokens = requiredTokens;
        this.minTicketReward = minTicketReward;
        this.maxTicketReward = maxTicketReward;
        this.ticketReward = (minTicketReward + maxTicketReward) / 2; // average
    }

    public abstract int runGame(ArrayList<Functional> useItems);

    /**
     * Calculate ticket reward based on performance
     * 
     * @param performanceScore A score from 0.0 (worst) to 1.0 (best)
     * @return Number of tickets to award
     */
    public int calculateTicketReward(double performanceScore) {
        // Clamp performance score between 0 and 1
        performanceScore = Math.max(0.0, Math.min(1.0, performanceScore));

        // Linear interpolation between min and max rewards
        int rewardRange = maxTicketReward - minTicketReward;
        int bonusTickets = (int) Math.round(rewardRange * performanceScore);

        return minTicketReward + bonusTickets;
    }

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

    public int getMinTicketReward() {
        return minTicketReward;
    }

    public void setMinTicketReward(int minTicketReward) {
        if (minTicketReward < 0) {
            throw new IllegalArgumentException("Minimum ticket reward cannot be negative.");
        }
        this.minTicketReward = minTicketReward;
    }

    public int getMaxTicketReward() {
        return maxTicketReward;
    }

    public void setMaxTicketReward(int maxTicketReward) {
        if (maxTicketReward < 0) {
            throw new IllegalArgumentException("Maximum ticket reward cannot be negative.");
        }
        this.maxTicketReward = maxTicketReward;
    }

    public String getTicketRewardRange() {
        if (minTicketReward == maxTicketReward) {
            return String.valueOf(minTicketReward);
        }
        return minTicketReward + "-" + maxTicketReward;
    }
}
