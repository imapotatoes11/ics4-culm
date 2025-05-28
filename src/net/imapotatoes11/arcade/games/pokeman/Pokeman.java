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
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package net.imapotatoes11.arcade.games.pokeman;

import java.util.*;
import net.imapotatoes11.arcade.util.Bcolors;

public class Pokeman {
    private String name;
    private int maxLife;
    private int currentLife;
    private int maxEnergy;
    private int currentEnergy;
    private List<Move> movePool;
    private Random random;

    public Pokeman(String name, int maxLife, int maxEnergy, List<Move> movePool) {
        this.name = name;
        this.maxLife = maxLife;
        this.currentLife = maxLife;
        this.maxEnergy = maxEnergy;
        this.currentEnergy = maxEnergy;
        this.movePool = new ArrayList<>(movePool);
        this.random = new Random();
    }

    public void regenEnergy() {
        if (currentEnergy < maxEnergy) {
            currentEnergy++;
        }
    }

    public int takeDamage(int amount) {
        int actual = Math.min(amount, currentLife);
        currentLife -= actual;
        return actual;
    }

    public boolean isDefeated() {
        return currentLife <= 0;
    }

    public Move chooseMove() {
        List<Move> affordable = new ArrayList<>();
        for (Move m : movePool) {
            if (m.getEnergyCost() <= currentEnergy) {
                affordable.add(m);
            }
        }
        if (affordable.isEmpty()) {
            // pick lowest cost
            Move lowest = movePool.get(0);
            for (Move m : movePool) {
                if (m.getEnergyCost() < lowest.getEnergyCost()) {
                    lowest = m;
                }
            }
            return lowest;
        }
        return affordable.get(random.nextInt(affordable.size()));
    }

    public String displayStatus() {
        String lifeBar = String.format("HP:%d/%d", currentLife, maxLife);
        String energyBar = String.format("EN:%d/%d", currentEnergy, maxEnergy);
        return String.format("%s%s%s | %s%s",
                Bcolors.BOLD, name, Bcolors.ENDC,
                lifeBar, energyBar);
    }

    // getters and setters
    public String getName() {
        return name;
    }

    public int getCurrentLife() {
        return currentLife;
    }

    public int getCurrentEnergy() {
        return currentEnergy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public void setCurrentEnergy(int currentEnergy) {
        this.currentEnergy = Math.max(0, Math.min(currentEnergy, maxEnergy));
    }

    public List<Move> getMovePool() {
        return movePool;
    }
}
