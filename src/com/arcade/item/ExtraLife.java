/*
 * ExtraLife.java
 *
 * Date: 06 06, 2025
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

public class ExtraLife extends Functional {
    public ExtraLife(String name, int numUses, int price) {
        super(name, numUses, price);
    }

    public ExtraLife() {
        super("Extra Life", 1, 10); // Default values for an extra life
    }

    public void activate() {
        this.setNumUses(getNumUses() + 1); // Increment the number of uses
    }

}
