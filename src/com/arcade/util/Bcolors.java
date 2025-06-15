/*
 * Bcolors.java
 *
 * Date: 05 30, 2025
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
package com.arcade.util;

public class Bcolors {
    // JGRASP DOESN'T SUPPORT ANSI COLORS, UNCOMMENT TO USE

    public static final String HEADER = "";// "\033[95m";
    public static final String OKBLUE = "";// "\033[94m";
    public static final String OKCYAN = "";// "\033[96m";
    public static final String OKGREEN = "";// "\033[92m";
    public static final String WARNING = "";// "\033[93m";
    public static final String FAIL = "";// "\033[91m";
    public static final String ENDC = "";// "\033[0m";
    public static final String BOLD = "";// "\033[1m";
    public static final String UNDERLINE = "";// "\033[4m";

    // Text colors
    public static final String BLACK = "";// "\033[30m";
    public static final String RED = "";// "\033[31m";
    public static final String GREEN = "";// "\033[32m";
    public static final String YELLOW = "";// "\033[33m";
    public static final String BLUE = "";// "\033[34m";
    public static final String MAGENTA = "";// "\033[35m";
    public static final String CYAN = "";// "\033[36m";
    public static final String WHITE = "";// "\033[37m";
    public static final String PURPLE = "";// "\033[35m"; // Alias for MAGENTA

    // Bright text colors
    public static final String BRIGHT_BLACK = "";// "\033[90m";
    public static final String BRIGHT_RED = "";// "\033[91m";
    public static final String BRIGHT_GREEN = "";// "\033[92m";
    public static final String BRIGHT_YELLOW = "";// "\033[93m";
    public static final String BRIGHT_BLUE = "";// "\033[94m";
    public static final String BRIGHT_MAGENTA = "";// "\033[95m";
    public static final String BRIGHT_CYAN = "";// "\033[96m";
    public static final String BRIGHT_WHITE = "";// "\033[97m";

    // Background colors
    public static final String BG_BLACK = "";// "\033[40m";
    public static final String BG_RED = "";// "\033[41m";
    public static final String BG_GREEN = "";// "\033[42m";
    public static final String BG_YELLOW = "";// "\033[43m";
    public static final String BG_BLUE = "";// "\033[44m";
    public static final String BG_MAGENTA = "";// "\033[45m";
    public static final String BG_CYAN = "";// "\033[46m";
    public static final String BG_WHITE = "";// "\033[47m";

    // Bright background colors
    public static final String BG_BRIGHT_BLACK = "";// "\033[100m";
    public static final String BG_BRIGHT_RED = "";// "\033[101m";
    public static final String BG_BRIGHT_GREEN = "";// "\033[102m";
    public static final String BG_BRIGHT_YELLOW = "";// "\033[103m";
    public static final String BG_BRIGHT_BLUE = "";// "\033[104m";
    public static final String BG_BRIGHT_MAGENTA = "";// "\033[105m";
    public static final String BG_BRIGHT_CYAN = "";// "\033[106m";
    public static final String BG_BRIGHT_WHITE = "";// "\033[107m";

    // Text styles
    public static final String DIM = "";// "\033[2m";
    public static final String ITALIC = "";// "\033[3m";
    public static final String BLINK = "";// "\033[5m";
    public static final String REVERSE = "";// "\033[7m";
    public static final String STRIKETHROUGH = "";// "\033[9m";

    // Bold combinations
    public static final String BOLD_BLACK = BOLD + BLACK;
    public static final String BOLD_RED = BOLD + RED;
    public static final String BOLD_GREEN = BOLD + GREEN;
    public static final String BOLD_YELLOW = BOLD + YELLOW;
    public static final String BOLD_BLUE = BOLD + BLUE;
    public static final String BOLD_MAGENTA = BOLD + MAGENTA;
    public static final String BOLD_CYAN = BOLD + CYAN;
    public static final String BOLD_WHITE = BOLD + WHITE;

    // Bold bright combinations
    public static final String BOLD_BRIGHT_BLACK = BOLD + BRIGHT_BLACK;
    public static final String BOLD_BRIGHT_RED = BOLD + BRIGHT_RED;
    public static final String BOLD_BRIGHT_GREEN = BOLD + BRIGHT_GREEN;
    public static final String BOLD_BRIGHT_YELLOW = BOLD + BRIGHT_YELLOW;
    public static final String BOLD_BRIGHT_BLUE = BOLD + BRIGHT_BLUE;
    public static final String BOLD_BRIGHT_MAGENTA = BOLD + BRIGHT_MAGENTA;
    public static final String BOLD_BRIGHT_CYAN = BOLD + BRIGHT_CYAN;
    public static final String BOLD_BRIGHT_WHITE = BOLD + BRIGHT_WHITE;
}
