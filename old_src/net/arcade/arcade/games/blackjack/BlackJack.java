/*
 * BlackJack.java
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
package net.arcade.arcade.games.blackjack;

import java.util.*;

import net.arcade.arcade.games.Game;
import net.arcade.arcade.item.Functional;
import net.arcade.arcade.util.Bcolors;

public class BlackJack extends Game {
    public static final int NUM_CARDS = 52;

    public static enum Cards {
        ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN,
        JACK, QUEEN, KING
    }

    private List<Cards> playerHand;
    private List<Cards> dealerHand;
    private List<Cards> deck;
    private Random random;

    // styling constants
    private static final String STYLE_TITLE = Bcolors.BOLD + Bcolors.OKGREEN;
    private static final String STYLE_INFO = Bcolors.OKCYAN;
    private static final String STYLE_WARNING = Bcolors.WARNING;
    private static final String STYLE_ERROR = Bcolors.FAIL;
    private static final String STYLE_SUBHEADER = Bcolors.BOLD;
    private static final String STYLE_HEADER = Bcolors.BOLD + Bcolors.BRIGHT_CYAN;
    private static final String STYLE_WIN_HEADER = Bcolors.BOLD_GREEN;
    private static final String STYLE_LOSE_HEADER = Bcolors.BOLD_RED;
    private static final String STYLE_TIE_HEADER = Bcolors.BOLD_YELLOW;
    private static final String STYLE_END = Bcolors.ENDC;

    public BlackJack() {
        super(1, "Blackjack", 1, 10, 20);
        this.random = new Random();
    }

    public BlackJack(int id, String title, int difficulty, int requiredTokens, int ticketReward) {
        super(id, title, difficulty, requiredTokens, ticketReward);
        this.random = new Random();
    }

    public static void main(String[] args) {
        BlackJack game = new BlackJack(10, "Blackjack", 10, 10, 20);
        ArrayList<Functional> useItems = new ArrayList<>(); // Placeholder for items
        game.runGame(useItems);
    }

    public int runGame(ArrayList<Functional> useItems) {
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
        deck = new ArrayList<>();

        String lines = System.getProperty("LINES");
        // if (lines != null) {
        // int height = Integer.parseInt(lines);
        // }
        System.out.println("\n".repeat(lines != null ? Integer.parseInt(lines) : 20));
        System.out.println(STYLE_TITLE + "Welcome to Blackjack!" + STYLE_END);
        System.out.println(STYLE_INFO
                + "You will play against the dealer. Try to get as close to 21 without going over." + STYLE_END);
        System.out.println(STYLE_INFO
                + "Aces can count as 1 or 11, face cards are worth 10, and all other cards are worth their number."
                + STYLE_END);
        System.out.println(STYLE_WARNING + "Difficulty Level: " + this.getDifficulty() + STYLE_END);
        System.out.println(STYLE_TITLE + "Let's begin!" + STYLE_END + "\n");

        // Initialize decks with cards
        for (Cards card : Cards.values()) {
            deck.add(card);
        }

        // Shuffle decks
        Collections.shuffle(deck);

        // Game loop
        while (true) {
            // Player's turn
            if (playerTurn()) {
                break; // Player stands or busts
            }
        }

        // Dealer's turn
        dealerTurn();

        // Determine winner
        return declareWinner();
    }

    private int calculateHandValue(List<Cards> deck) {
        int value = 0;
        int aces = 0;

        for (Cards card : deck) {
            switch (card) {
                case ACE:
                    value += 11; // Initially count Ace as 11
                    aces++;
                    break;
                case TWO:
                    value += 2;
                    break;
                case THREE:
                    value += 3;
                    break;
                case FOUR:
                    value += 4;
                    break;
                case FIVE:
                    value += 5;
                    break;
                case SIX:
                    value += 6;
                    break;
                case SEVEN:
                    value += 7;
                    break;
                case EIGHT:
                    value += 8;
                    break;
                case NINE:
                    value += 9;
                    break;
                case TEN:
                case JACK:
                case QUEEN:
                case KING:
                    value += 10;
                    break;
            }
        }

        // Adjust for Aces if over 21
        while (value > 21 && aces > 0) {
            value -= 10; // Count Ace as 1 instead of 11
            aces--;
        }

        return value;
    }

    private void hit() {
        if (deck.size() > 0) {
            Cards drawnCard = deck.remove(0); // Draw the top card
            System.out.println(Bcolors.OKGREEN + "You drew: " + Bcolors.BOLD + drawnCard + Bcolors.ENDC);
            playerHand.add(drawnCard);
        } else {
            System.out.println(Bcolors.FAIL + "No more cards to draw!" + Bcolors.ENDC);
        }
    }

    private boolean playerTurn() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(STYLE_SUBHEADER + "\n==============================");
            System.out.println("        BLACKJACK TURN        ");
            System.out.println("==============================" + STYLE_END);
            System.out.print(STYLE_INFO + "Your hand: " + STYLE_END);
            for (Cards card : playerHand) {
                System.out.print(Bcolors.BOLD + card + " " + Bcolors.ENDC);
            }
            System.out.println();
            int handValue = calculateHandValue(playerHand);
            if (handValue > 21) {
                System.out.println(Bcolors.FAIL + "Hand value: " + handValue + Bcolors.ENDC);
            } else if (handValue == 21) {
                System.out.println(Bcolors.OKGREEN + "Hand value: " + handValue + Bcolors.ENDC);
            } else {
                System.out.println(Bcolors.OKBLUE + "Hand value: " + handValue + Bcolors.ENDC);
            }
            System.out.println(STYLE_SUBHEADER + "------------------------------" + STYLE_END);
            System.out.print(STYLE_WARNING + "Would you like to [H]it or [S]tand? > " + STYLE_END);
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("h") || input.equals("hit")) {
                hit();
                int value = calculateHandValue(playerHand);
                System.out.println(Bcolors.OKGREEN + "You drew a card!" + Bcolors.ENDC);
                if (value > 21) {
                    System.out.println(Bcolors.FAIL + Bcolors.BOLD + "BUST! Your hand value is " + value
                            + ". You lose this round." + Bcolors.ENDC);
                    return true; // Player busted
                }
            } else if (input.equals("s") || input.equals("stand")) {
                System.out.println(Bcolors.OKCYAN + "You chose to stand." + Bcolors.ENDC);
                return true; // Player stands
            } else {
                System.out.println(
                        Bcolors.FAIL + "Invalid input. Please enter 'H' to hit or 'S' to stand." + Bcolors.ENDC);
            }
        }
    }

    private void hitDealer() {
        if (deck.size() > 0) {
            Cards drawnCard = deck.remove(0); // Draw the top card
            System.out.println(Bcolors.OKCYAN + "Dealer drew: " + Bcolors.BOLD + drawnCard + Bcolors.ENDC);
            dealerHand.add(drawnCard);
        } else {
            System.out.println(Bcolors.FAIL + "No more cards to draw for dealer!" + Bcolors.ENDC);
        }
    }

    private void dealerTurn() {
        System.out.println(
                STYLE_SUBHEADER + "\nDealer's turn (Difficulty Level: " + this.getDifficulty() + ")" + STYLE_END);

        // Calculate dynamic threshold: 17 +- random(10 - difficulty)
        int baseThreshold = 17;
        // exponential variance: high when difficulty is low, low when difficulty is
        // high
        int maxVariance = 10;
        double exponent = (11.0 - this.getDifficulty()) / 10.0; // maps difficulty 1→exponent 1.0, 10→0.1
        int variance = (int) Math.max(1, Math.round(Math.pow(maxVariance, exponent)));

        // int randomAdjustment = random.nextInt(2 * variance + 1) - variance; //
        // -variance … +variance
        double u = random.nextDouble(); // flat in [0,1)
        double bias = Math.pow(u, 0.5); // sqrt(u) biases toward 1
        int sign = random.nextBoolean() ? +1 : -1; // choose + or –
        int randomAdjustment = (int) Math.round(bias * variance) * sign;

        // calculate threshold (17 ± randomAdjustment)
        int threshold = baseThreshold + randomAdjustment;

        // Ensure threshold stays within reasonable bounds (5-27)
        // threshold = Math.max(15, Math.min(27, threshold));

        System.out.println(STYLE_WARNING + "Dealer will hit until hand value reaches: " + threshold + STYLE_END);

        while (calculateHandValue(dealerHand) < threshold) {
            hitDealer();
            int currentValue = calculateHandValue(dealerHand);
            if (currentValue > 21) {
                System.out.println(
                        Bcolors.FAIL + Bcolors.BOLD + "Dealer busted with " + currentValue + "!" + Bcolors.ENDC);
                break;
            }
        }

        System.out.println(STYLE_INFO + "Dealer stands with hand value: " + calculateHandValue(dealerHand) + STYLE_END);
    }

    private int declareWinner() {
        // Logic for declaring the winner
        int playerValue = calculateHandValue(playerHand);
        int dealerValue = calculateHandValue(dealerHand);

        // choose header color: green=win, red=lose, yellow=tie
        boolean playerWon = playerValue <= 21 &&
                (dealerValue > 21 || playerValue > dealerValue);
        boolean isTie = playerValue <= 21 && dealerValue <= 21 && playerValue == dealerValue;
        String headerStyle;
        if (isTie) {
            headerStyle = STYLE_TIE_HEADER;
        } else if (playerWon) {
            headerStyle = STYLE_WIN_HEADER;
        } else {
            headerStyle = STYLE_LOSE_HEADER;
        }

        System.out.println(headerStyle + "\n==============================");
        System.out.println("          GAME OVER           ");
        System.out.println("==============================" + STYLE_END);
        System.out.print(STYLE_INFO + "Your hand: " + STYLE_END);
        for (Cards card : playerHand) {
            System.out.print(Bcolors.BOLD + card + " " + Bcolors.ENDC);
        }
        if (playerValue > 21) {
            System.out.println(Bcolors.FAIL + "\nHand value: " + playerValue + Bcolors.ENDC);
        } else if (playerValue == 21) {
            System.out.println(Bcolors.OKGREEN + "\nHand value: " + playerValue + Bcolors.ENDC);
        } else {
            System.out.println(Bcolors.OKBLUE + "\nHand value: " + playerValue + Bcolors.ENDC);
        }
        System.out.print(STYLE_INFO + "Dealer's hand: " + STYLE_END);
        for (Cards card : dealerHand) {
            System.out.print(Bcolors.BOLD + card + " " + Bcolors.ENDC);
        }
        if (dealerValue > 21) {
            System.out.println(Bcolors.FAIL + "\nDealer's hand value: " + dealerValue + Bcolors.ENDC);
        } else if (dealerValue == 21) {
            System.out.println(Bcolors.OKGREEN + "\nDealer's hand value: " + dealerValue + Bcolors.ENDC);
        } else {
            System.out.println(Bcolors.OKBLUE + "\nDealer's hand value: " + dealerValue + Bcolors.ENDC);
        }

        if (playerValue > 21) {
            System.out.println(STYLE_ERROR + Bcolors.BOLD + "You busted! Dealer wins." + STYLE_END);
        } else if (dealerValue > 21 || playerValue > dealerValue) {
            System.out.println(STYLE_TITLE + Bcolors.BOLD + "You win!" + STYLE_END);
        } else if (playerValue < dealerValue) {
            System.out.println(STYLE_ERROR + Bcolors.BOLD + "Dealer wins!" + STYLE_END);
        } else {
            System.out.println(STYLE_WARNING + Bcolors.BOLD + "It's a tie!" + STYLE_END);
        }
        System.out.println(headerStyle + "==============================" + STYLE_END);

        System.out.println("\nYou have earned " + Bcolors.BOLD
                + (playerWon ? (Bcolors.GREEN + this.getTicketReward()) : (Bcolors.RED + "0")) + Bcolors.ENDC
                + " tickets!");
        return playerWon ? this.getTicketReward() : 0; // Return ticket reward if player won
    }

}
