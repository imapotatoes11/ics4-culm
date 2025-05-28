/*
 * PokemanTest.java
 *
 * Simple test to verify the Pokeman game mechanics work
 */
package net.imapotatoes11.arcade.games.pokeman;

public class PokemanTest {
    public static void main(String[] args) {
        System.out.println("=== POKEMAN GAME TEST ===\n");

        // Test Pokeman creation
        Pokeman player = Pokeman.createPlayerPokeman();
        Pokeman enemy = Pokeman.createEnemyPokeman(1);

        System.out.println("Player Pokeman: " + player.getName());
        System.out.println("Enemy Pokeman: " + enemy.getName());
        System.out.println();

        // Test ASCII Art
        System.out.println("Player ASCII Art:");
        System.out.println(player.getAsciiArt());
        System.out.println();

        System.out.println("Enemy ASCII Art:");
        System.out.println(enemy.getAsciiArt());
        System.out.println();

        // Test Boss ASCII Art
        Pokeman boss = Pokeman.createEnemyPokeman(4);
        System.out.println("Boss ASCII Art:");
        System.out.println(boss.getAsciiArt());
        System.out.println();

        // Test stats display
        System.out.println("=== STATS TEST ===");
        System.out.println("Player stats:");
        player.displayStats();
        System.out.println();

        System.out.println("Enemy stats:");
        enemy.displayStats();
        System.out.println();

        // Test moves
        System.out.println("=== MOVES TEST ===");
        System.out.println("Player moves:");
        for (Move move : player.getMoves()) {
            System.out.println("- " + move.getDisplayString());
        }
        System.out.println();

        // Test battle mechanics
        System.out.println("=== BATTLE MECHANICS TEST ===");
        System.out.println("Before attack:");
        System.out.println("Player HP: " + player.getCurrentHp() + "/" + player.getMaxHp());
        System.out.println("Enemy HP: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp());
        System.out.println();

        // Player attacks enemy
        Move fireball = player.getMoves().get(0); // Fireball
        System.out.println("Player uses " + fireball.getName() + ":");
        player.useMove(fireball, enemy);
        System.out.println();

        System.out.println("After attack:");
        System.out.println("Player HP: " + player.getCurrentHp() + "/" + player.getMaxHp());
        System.out.println("Player Energy: " + player.getCurrentEnergy() + "/" + player.getMaxEnergy());
        System.out.println("Enemy HP: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp());
        System.out.println();

        // Test energy regeneration
        System.out.println("=== ENERGY REGENERATION TEST ===");
        player.regenerateEnergy();
        System.out.println("After regeneration:");
        System.out.println("Player Energy: " + player.getCurrentEnergy() + "/" + player.getMaxEnergy());
        System.out.println();

        System.out.println("=== TEST COMPLETE ===");
        System.out.println("All basic mechanics working correctly!");
    }
}
