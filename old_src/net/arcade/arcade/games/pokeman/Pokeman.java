/*
 * Pokeman.java
 *
 * Date: 05 28, 2025
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
package net.arcade.arcade.games.pokeman;

import java.util.ArrayList;

import net.arcade.arcade.util.Bcolors;

public class Pokeman {
    private String name;
    private int maxHp;
    private int currentHp;
    private int maxEnergy;
    private int currentEnergy;
    private ArrayList<Move> moves;
    private boolean isBoss;

    public Pokeman(String name, int maxHp, int maxEnergy, ArrayList<Move> moves, boolean isBoss) {
        this.name = name;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.maxEnergy = maxEnergy;
        this.currentEnergy = maxEnergy;
        this.moves = new ArrayList<>(moves);
        this.isBoss = isBoss;
    }

    public void takeDamage(int damage) {
        currentHp = Math.max(0, currentHp - damage);
    }

    public boolean useMove(Move move, Pokeman target) {
        if (!move.canAfford(currentEnergy)) {
            System.out.println(Bcolors.FAIL + name + " doesn't have enough energy!" + Bcolors.ENDC);
            return false;
        }

        // Consume energy
        consumeEnergy(move.getEnergyCost());

        // Calculate damage
        int damage = move.calculateDamage();

        // Apply damage
        target.takeDamage(damage);

        // Display result
        System.out.println(Bcolors.BOLD + name + Bcolors.ENDC + " used " +
                Bcolors.BRIGHT_YELLOW + move.getName() + Bcolors.ENDC + "!");
        System.out.println(Bcolors.FAIL + target.getName() + " takes " + damage + " damage!" + Bcolors.ENDC);

        return true;
    }

    public void regenerateEnergy() {
        if (currentEnergy < maxEnergy) {
            currentEnergy++;
        }
    }

    public void consumeEnergy(int amount) {
        currentEnergy = Math.max(0, currentEnergy - amount);
    }

    public boolean isDefeated() {
        return currentHp <= 0;
    }

    public ArrayList<Move> getAvailableMoves() {
        ArrayList<Move> available = new ArrayList<>();
        for (Move move : moves) {
            if (move.canAfford(currentEnergy)) {
                available.add(move);
            }
        }
        return available;
    }

    public void displayStats() {
        // HP Bar
        int hpBarLength = 12;
        int hpFilled = (int) ((double) currentHp / maxHp * hpBarLength);
        String hpBar = Bcolors.BRIGHT_GREEN + "█".repeat(hpFilled) +
                Bcolors.BRIGHT_BLACK + "░".repeat(hpBarLength - hpFilled) + Bcolors.ENDC;

        // Energy Bar
        int energyBarLength = 4;
        int energyFilled = (int) ((double) currentEnergy / maxEnergy * energyBarLength);
        String energyBar = Bcolors.BRIGHT_BLUE + "█".repeat(energyFilled) +
                Bcolors.BRIGHT_BLACK + "░".repeat(energyBarLength - energyFilled) + Bcolors.ENDC;

        System.out.printf("HP: %s %d/%d\n", hpBar, currentHp, maxHp);
        System.out.printf("EN: %s %d/%d\n", energyBar, currentEnergy, maxEnergy);
    }

    public String getAsciiArt() {
        if (isBoss) {
            return "    ▲ ▲ ▲ ▲ ▲\n" +
                    "   ( ◉ ◉ ◉ ◉ )\n" +
                    "    \\   ∩   /\n" +
                    "     -------\n" +
                    "    /|  |  |\\\n" +
                    "   ( |  |  | )";
        } else {
            return "    ▲ ▲ ▲ ▲\n" +
                    "   ( ◉   ◉ )\n" +
                    "    \\  ∩  /\n" +
                    "     ----";
        }
    }

    // Factory methods
    public static Pokeman createPlayerPokeman() {
        ArrayList<Move> playerMoves = new ArrayList<>();
        playerMoves.add(Move.createFireball());
        playerMoves.add(Move.createSlash());
        return new Pokeman("Sparkles", 80, 3, playerMoves, false);
    }

    public static Pokeman createEnemyPokeman(int battleNumber) {
        ArrayList<Move> enemyMoves = new ArrayList<>();

        switch (battleNumber) {
            case 1:
                enemyMoves.add(Move.createEnemyAttack());
                return new Pokeman("Leafy", 40, 2, enemyMoves, false);
            case 2:
                enemyMoves.add(Move.createEnemyAttack());
                return new Pokeman("Rocky", 50, 3, enemyMoves, false);
            case 3:
                enemyMoves.add(Move.createEnemyAttack());
                return new Pokeman("Watery", 60, 3, enemyMoves, false);
            case 4: // Boss
                enemyMoves.add(Move.createBossAttack());
                enemyMoves.add(Move.createEnemyAttack());
                return new Pokeman("MEGA-DESTROYER", 100, 4, enemyMoves, true);
            default:
                enemyMoves.add(Move.createEnemyAttack());
                return new Pokeman("Unknown", 30, 2, enemyMoves, false);
        }
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public int getCurrentEnergy() {
        return currentEnergy;
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }

    public boolean isBoss() {
        return isBoss;
    }
}
