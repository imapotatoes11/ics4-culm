/*
 * Move.java
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
package net.imapotatoes11.arcade.games.pokeman;

import java.util.Random;

public class Move {
    private String name;
    private int energyCost;
    private int baseDamage;
    private int damageVariance;
    private String description;
    private static Random random = new Random();

    public Move(String name, int energyCost, int baseDamage, int damageVariance, String description) {
        this.name = name;
        this.energyCost = energyCost;
        this.baseDamage = baseDamage;
        this.damageVariance = damageVariance;
        this.description = description;
    }

    public int calculateDamage() {
        return baseDamage + random.nextInt(damageVariance + 1);
    }

    public boolean canAfford(int currentEnergy) {
        return currentEnergy >= energyCost;
    }

    public String getDisplayString() {
        return String.format("%s (%d EN) - %s", name, energyCost, description);
    }

    // Factory methods for pre-defined moves
    public static Move createFireball() {
        return new Move("Fireball", 3, 15, 5, "Deal 15-20 fire damage");
    }

    public static Move createSlash() {
        return new Move("Slash", 0, 8, 4, "Deal 8-12 physical damage");
    }

    public static Move createEnemyAttack() {
        return new Move("Tackle", 1, 6, 3, "Deal 6-9 damage");
    }

    public static Move createBossAttack() {
        return new Move("Mega Blast", 2, 12, 6, "Deal 12-18 powerful damage");
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public String getDescription() {
        return description;
    }
}
