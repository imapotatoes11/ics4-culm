/*
 * PokemanGame.java
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
 * distributed under the License on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 */
package net.imapotatoes11.arcade.games.pokeman;

import net.imapotatoes11.arcade.games.Game;
import net.imapotatoes11.arcade.item.Functional;
import net.imapotatoes11.arcade.util.Bcolors;
import java.util.*;

public class PokemanGame extends Game {
    private List<Pokeman> opponents;
    private Pokeman player;
    private static final String STYLE_TITLE = Bcolors.BOLD + Bcolors.OKGREEN;
    private static final String STYLE_INFO = Bcolors.OKCYAN;
    private static final String STYLE_WARNING = Bcolors.WARNING;
    private static final String STYLE_HEADER = Bcolors.BOLD + Bcolors.BRIGHT_CYAN;
    private static final String STYLE_WIN = Bcolors.BOLD_GREEN;
    private static final String STYLE_LOSE = Bcolors.BOLD_RED;
    private static final String STYLE_END = Bcolors.ENDC;

    public PokemanGame() {
        super(2, "Pokeman Battle", 5, 0, 50);
    }

    public static void main(String[] args) {
        PokemanGame game = new PokemanGame();
        ArrayList<Functional> items = new ArrayList<>();
        System.exit(game.runGame(items));
    }

    @Override
    public int runGame(ArrayList<Functional> useItems) {
        clearScreen();
        System.out.println(STYLE_TITLE + "Welcome to Pokeman Battle!" + STYLE_END);
        System.out.println(STYLE_INFO + "Defeat 3 wild Pokeman and 1 Boss to win." + STYLE_END);
        System.out.println(STYLE_WARNING + "Difficulty Level: " + getDifficulty() + STYLE_END + "\n");

        // Initialize player and opponents
        initializePlayer();
        initializeOpponents();

        // Battle through opponents
        for (int i = 0; i < opponents.size(); i++) {
            Pokeman enemy = opponents.get(i);
            System.out
                    .println(STYLE_HEADER + String.format("--- Battle %d: %s ---", i + 1, enemy.getName()) + STYLE_END);
            Battle battle = new Battle(player, enemy);
            boolean won = battle.run();
            if (!won) {
                System.out.println(STYLE_LOSE + "Game Over. You lost at " + enemy.getName() + STYLE_END);
                return 0;
            }
            System.out.println();
        }

        // Victory
        System.out.println(STYLE_WIN + "Congratulations! You defeated all Pokeman!" + STYLE_END);
        System.out.println(STYLE_INFO + "You have earned " + getTicketReward() + " tickets!" + STYLE_END);
        return getTicketReward();
    }

    private void initializePlayer() {
        List<Move> moves = Arrays.asList(
                new Move("Fireball", 5, 3),
                new Move("Slash", 2, 0));
        this.player = new Pokeman("Hero", 20, 3, moves);
    }

    private void initializeOpponents() {
        opponents = new ArrayList<>();
        List<Move> wildMoves = Arrays.asList(
                new Move("Tackle", 3, 0),
                new Move("Ember", 4, 2));
        for (int i = 1; i <= 3; i++) {
            opponents.add(new Pokeman("Wild" + i, 10, 3, wildMoves));
        }
        // Boss
        List<Move> bossMoves = Arrays.asList(
                new Move("Flame Burst", 6, 3),
                new Move("Claw", 4, 1));
        opponents.add(new Pokeman("Bossomon", 25, 4, bossMoves));
    }

    private void clearScreen() {
        String lines = System.getProperty("LINES");
        int n = lines != null ? Integer.parseInt(lines) : 50;
        System.out.print("\n".repeat(n));
    }
}
