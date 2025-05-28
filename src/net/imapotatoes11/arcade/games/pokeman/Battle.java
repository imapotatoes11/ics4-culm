/*
 * Battle.java
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

import java.util.*;
import net.imapotatoes11.arcade.util.Bcolors;

public class Battle {
    private Pokeman player;
    private Pokeman enemy;
    private Scanner scanner;

    private static final String STYLE_INFO = Bcolors.OKCYAN;
    private static final String STYLE_WARNING = Bcolors.WARNING;
    private static final String STYLE_ERROR = Bcolors.FAIL;
    private static final String STYLE_HEADER = Bcolors.BOLD + Bcolors.BRIGHT_CYAN;
    private static final String STYLE_END = Bcolors.ENDC;

    public Battle(Pokeman player, Pokeman enemy) {
        this.player = player;
        this.enemy = enemy;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Runs the battle loop. Returns true if player wins.
     */
    public boolean run() {
        System.out.println(STYLE_HEADER + "\n=== BATTLE START ===" + STYLE_END);
        while (true) {
            // Player's turn
            if (playerTurn()) {
                if (enemy.isDefeated()) {
                    System.out.println(STYLE_INFO + enemy.getName() + " has been defeated!" + STYLE_END);
                    return true;
                }
                // Enemy's turn
                if (!enemyTurn()) {
                    System.out.println(STYLE_ERROR + "You have been defeated..." + STYLE_END);
                    return false;
                }
            }
        }
    }

    private boolean playerTurn() {
        player.regenEnergy();
        printBattleScene();
        System.out.println(STYLE_HEADER + "-- Your Turn --" + STYLE_END);
        List<Move> moves = player.getMovePool();
        for (int i = 0; i < moves.size(); i++) {
            System.out.printf("[%d] %s%n", i + 1, moves.get(i));
        }
        System.out.print(STYLE_WARNING + "Choose move > " + STYLE_END);
        String input = scanner.nextLine().trim();
        int idx;
        try {
            idx = Integer.parseInt(input) - 1;
        } catch (NumberFormatException e) {
            System.out.println(STYLE_ERROR + "Invalid choice." + STYLE_END);
            return playerTurn();
        }
        if (idx < 0 || idx >= moves.size()) {
            System.out.println(STYLE_ERROR + "Invalid choice." + STYLE_END);
            return playerTurn();
        }
        Move move = moves.get(idx);
        if (move.getEnergyCost() > player.getCurrentEnergy()) {
            System.out.println(STYLE_ERROR + "Not enough energy!" + STYLE_END);
            return playerTurn();
        }
        int dmg = move.execute(player, enemy);
        printRoundResult(move, player, dmg);
        return true;
    }

    private boolean enemyTurn() {
        enemy.regenEnergy();
        printBattleScene();
        System.out.println(STYLE_HEADER + "-- Enemy Turn --" + STYLE_END);
        Move move = enemy.chooseMove();
        int dmg = move.execute(enemy, player);
        printRoundResult(move, enemy, dmg);
        return !player.isDefeated();
    }

    private void printBattleScene() {
        System.out.println();
        System.out.println(player.displayStatus() + "    VS    " + enemy.displayStatus());
    }

    private void printRoundResult(Move move, Pokeman user, int damage) {
        System.out.printf(
                STYLE_INFO + "%s used %s! It dealt %d damage.%s%n",
                user.getName(), move.getName(), damage, STYLE_END);
    }
}
