/*
 * TicketMultiplier.java
 *
 * Date: 06 02, 2025
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
package com.arcade.item;

import java.io.*;
import java.util.*;

public class TicketMultiplier extends Functional {
    private int multiplier;

    public TicketMultiplier(String name, int numUses, int price, int multiplier) {
        super(name, numUses, price);
        this.multiplier = multiplier;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public void activate() {
        this.setNumUses(this.getNumUses() - 1);
        // not implemented yet
    }

}
