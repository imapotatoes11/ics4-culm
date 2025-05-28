/*
 * Functional.java
 *
 * Date: 05 27, 2025
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
package net.imapotatoes11.arcade.item;

import java.io.*;
import java.util.*;

public abstract class Functional extends Item {
    private int numUses;
    private int price;

    public abstract void activate();

    public Functional(String name, int numUses, int price) {
        super(name);
        this.numUses = numUses;
        this.price = price;
    }

    public int getNumUses() {
        return numUses;
    }

    public void setNumUses(int numUses) {
        this.numUses = numUses;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
