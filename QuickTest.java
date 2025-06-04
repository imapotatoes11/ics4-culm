///*
// * QuickTest.java
// *
// * Quick test to demonstrate Pokeman game functionality
// */
//
//import net.imapotatoes11.arcade.games.pokeman.*;
//
//public class QuickTest {
//    public static void main(String[] args) {
//        System.out.println("=== POKEMAN GAME DEMO ===\n");
//
//        // Create pokemans
//        Pokeman player = Pokeman.createPlayerPokeman();
//        Pokeman enemy1 = Pokeman.createEnemyPokeman(1);
//        Pokeman boss = Pokeman.createEnemyPokeman(4);
//
//        System.out.println("✅ Pokeman Creation Test:");
//        System.out.println("Player: " + player.getName() + " (HP: " + player.getMaxHp() + ", Energy: "
//                + player.getMaxEnergy() + ")");
//        System.out.println("Enemy: " + enemy1.getName() + " (HP: " + enemy1.getMaxHp() + ", Energy: "
//                + enemy1.getMaxEnergy() + ")");
//        System.out.println(
//                "Boss: " + boss.getName() + " (HP: " + boss.getMaxHp() + ", Energy: " + boss.getMaxEnergy() + ")");
//        System.out.println();
//
//        System.out.println("✅ ASCII Art Test:");
//        System.out.println("Normal Pokeman:");
//        System.out.println(player.getAsciiArt());
//        System.out.println("\nBoss Pokeman:");
//        System.out.println(boss.getAsciiArt());
//        System.out.println();
//
//        System.out.println("✅ Move System Test:");
//        System.out.println("Player moves:");
//        for (int i = 0; i < player.getMoves().size(); i++) {
//            Move move = player.getMoves().get(i);
//            System.out.println("  [" + (i + 1) + "] " + move.getDisplayString());
//        }
//        System.out.println();
//
//        System.out.println("✅ Battle Mechanics Test:");
//        System.out.println("Before battle:");
//        System.out.print("Player: ");
//        player.displayStats();
//        System.out.print("Enemy: ");
//        enemy1.displayStats();
//        System.out.println();
//
//        // Simulate a turn
//        Move fireball = player.getMoves().get(0); // Fireball
//        System.out.println("Player attacks with " + fireball.getName() + ":");
//        player.useMove(fireball, enemy1);
//        System.out.println();
//
//        System.out.println("After attack:");
//        System.out.print("Player: ");
//        player.displayStats();
//        System.out.print("Enemy: ");
//        enemy1.displayStats();
//        System.out.println();
//
//        // Test energy regeneration
//        System.out.println("Energy regeneration:");
//        player.regenerateEnergy();
//        System.out.print("Player after regen: ");
//        player.displayStats();
//        System.out.println();
//
//        System.out.println("✅ All core mechanics working!");
//        System.out.println("✅ Game is ready to play!");
//        System.out.println("\nTo play the full game, run:");
//        System.out.println("java -cp . net.imapotatoes11.arcade.games.pokeman.PokemanGame");
//    }
//}
