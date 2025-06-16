/**
 * Bcolors.java
 *
 * utility class for console text coloring and formatting
 * provides ansi escape codes for terminal text styling
 * currently disabled for jgrasp compatibility
 *
 * date: jun 15, 2025
 * author: kevin wang
 */
package com.arcade.util;

/**
 * utility class providing constants for console text coloring and formatting
 * contains ansi escape codes for various text colors, background colors, and
 * styles
 * currently all constants are empty strings due to jgrasp compatibility issues
 */
public class Bcolors {
    // jgrasp doesn't support ansi colors, so all codes are commented out
    // uncomment the actual ansi codes when using a terminal that supports them

    // basic text formatting
    public static final String HEADER = "\033[95m";
    public static final String OKBLUE = "\033[94m";
    public static final String OKCYAN = "\033[96m";
    public static final String OKGREEN = "\033[92m";
    public static final String WARNING = "\033[93m";
    public static final String FAIL = "\033[91m";
    public static final String ENDC = "\033[0m"; // reset to default
    public static final String BOLD = "\033[1m";
    public static final String UNDERLINE = "\033[4m";

    // standard text colors
    public static final String BLACK = "\033[30m";
    public static final String RED = "\033[31m";
    public static final String GREEN = "\033[32m";
    public static final String YELLOW = "\033[33m";
    public static final String BLUE = "\033[34m";
    public static final String MAGENTA = "\033[35m";
    public static final String CYAN = "\033[36m";
    public static final String WHITE = "\033[37m";
    public static final String PURPLE = "\033[35m"; // alias for magenta

    // bright text colors (high intensity)
    public static final String BRIGHT_BLACK = "\033[90m";
    public static final String BRIGHT_RED = "\033[91m";
    public static final String BRIGHT_GREEN = "\033[92m";
    public static final String BRIGHT_YELLOW = "\033[93m";
    public static final String BRIGHT_BLUE = "\033[94m";
    public static final String BRIGHT_MAGENTA = "\033[95m";
    public static final String BRIGHT_CYAN = "\033[96m";
    public static final String BRIGHT_WHITE = "\033[97m";

    // background colors
    public static final String BG_BLACK = "\033[40m";
    public static final String BG_RED = "\033[41m";
    public static final String BG_GREEN = "\033[42m";
    public static final String BG_YELLOW = "\033[43m";
    public static final String BG_BLUE = "\033[44m";
    public static final String BG_MAGENTA = "\033[45m";
    public static final String BG_CYAN = "\033[46m";
    public static final String BG_WHITE = "\033[47m";

    // bright background colors (high intensity)
    public static final String BG_BRIGHT_BLACK = "\033[100m";
    public static final String BG_BRIGHT_RED = "\033[101m";
    public static final String BG_BRIGHT_GREEN = "\033[102m";
    public static final String BG_BRIGHT_YELLOW = "\033[103m";
    public static final String BG_BRIGHT_BLUE = "\033[104m";
    public static final String BG_BRIGHT_MAGENTA = "\033[105m";
    public static final String BG_BRIGHT_CYAN = "\033[106m";
    public static final String BG_BRIGHT_WHITE = "\033[107m";

    // additional text styling options
    public static final String DIM = "\033[2m"; // dim/faint text
    public static final String ITALIC = "\033[3m"; // italic text
    public static final String BLINK = "\033[5m"; // blinking text
    public static final String REVERSE = "\033[7m"; // reverse video
    public static final String STRIKETHROUGH = "\033[9m"; // strikethrough text

    // convenience constants combining bold with colors
    public static final String BOLD_BLACK = BOLD + BLACK;
    public static final String BOLD_RED = BOLD + RED;
    public static final String BOLD_GREEN = BOLD + GREEN;
    public static final String BOLD_YELLOW = BOLD + YELLOW;
    public static final String BOLD_BLUE = BOLD + BLUE;
    public static final String BOLD_MAGENTA = BOLD + MAGENTA;
    public static final String BOLD_CYAN = BOLD + CYAN;
    public static final String BOLD_WHITE = BOLD + WHITE;

    // convenience constants combining bold with bright colors
    public static final String BOLD_BRIGHT_BLACK = BOLD + BRIGHT_BLACK;
    public static final String BOLD_BRIGHT_RED = BOLD + BRIGHT_RED;
    public static final String BOLD_BRIGHT_GREEN = BOLD + BRIGHT_GREEN;
    public static final String BOLD_BRIGHT_YELLOW = BOLD + BRIGHT_YELLOW;
    public static final String BOLD_BRIGHT_BLUE = BOLD + BRIGHT_BLUE;
    public static final String BOLD_BRIGHT_MAGENTA = BOLD + BRIGHT_MAGENTA;
    public static final String BOLD_BRIGHT_CYAN = BOLD + BRIGHT_CYAN;
    public static final String BOLD_BRIGHT_WHITE = BOLD + BRIGHT_WHITE;
}
