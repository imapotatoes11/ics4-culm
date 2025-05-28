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
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package net.imapotatoes11.arcade.games.pokeman;

public class Move {
    private String name;
    private int damage;
    private int energyCost;

    public Move(String name, int damage, int energyCost) {
        this.name = name;
        this.damage = damage;
        this.energyCost = energyCost;
    }

    public int execute(Pokeman user, Pokeman target) {
        user.setCurrentEnergy(user.getCurrentEnergy() - energyCost);
        int dealt = target.takeDamage(damage);
        return dealt;
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    @Override
    public String toString() {
        return String.format("%s (Cost: %d, Dmg: %d)", name, energyCost, damage);
    }
}
