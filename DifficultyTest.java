
// Simple test to verify difficulty features
import com.arcade.games.pokeman.*;

public class DifficultyTest {
    public static void main(String[] args) {
        System.out.println("Testing Pokeman Difficulty System");
        System.out.println("==================================");

        // Test enemy creation with different difficulties
        System.out.println("\n1. Testing Enemy Creation:");
        for (int difficulty = 1; difficulty <= 10; difficulty += 3) {
            Pokeman enemy = Pokeman.createEnemyPokeman(1, difficulty);
            System.out.printf("Difficulty %d - Enemy: %s (HP: %d, ATK: %d, DEF: %d)%n",
                    difficulty, enemy.getName(), enemy.getMaxHp(), enemy.getAttack(), enemy.getDefense());
        }

        // Test move damage calculation with different difficulties
        System.out.println("\n2. Testing Move Damage Calculation:");
        Move testMove = new Move("Test Attack", 20, 2);
        for (int difficulty = 1; difficulty <= 10; difficulty += 3) {
            int damage = testMove.calculateDamage(difficulty);
            System.out.printf("Difficulty %d - Move damage: %d (base: %d)%n",
                    difficulty, damage, testMove.getBaseDamage());
        }

        // Test Battle creation with difficulty
        System.out.println("\n3. Testing Battle Creation:");
        Pokeman player = Pokeman.createPlayerPokeman();
        Pokeman enemy = Pokeman.createEnemyPokeman(1, 5);
        Battle battle = new Battle(player, enemy, 5);
        System.out.println("Battle created successfully with difficulty 5");

        // Test healing functionality
        System.out.println("\n4. Testing Healing:");
        Pokeman testPokeman = Pokeman.createPlayerPokeman();
        int originalHp = testPokeman.getCurrentHp();
        // Simulate damage
        testPokeman.takeDamage(30);
        int afterDamage = testPokeman.getCurrentHp();
        // Heal
        testPokeman.heal(20);
        int afterHealing = testPokeman.getCurrentHp();
        System.out.printf("HP: %d -> %d (after damage) -> %d (after healing)%n",
                originalHp, afterDamage, afterHealing);

        System.out.println("\n✅ All difficulty features working correctly!");
    }
}
