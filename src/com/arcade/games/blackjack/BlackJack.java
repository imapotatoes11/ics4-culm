/**
 * BlackJack.java
 *
 * implementation of the blackjack card game for the arcade system
 * provides a full blackjack experience with dealer ai and difficulty adjustment
 * includes dynamic dealer behavior based on game difficulty
 *
 * date: jun 15, 2025
 * author: kevin wang
 */
package com.arcade.games.blackjack;

import java.util.*;
import com.arcade.item.Functional;
import com.arcade.util.Bcolors;
import com.arcade.games.Game;

/**
 * blackjack card game implementation extending the base Game class
 * features dynamic dealer ai that adjusts behavior based on difficulty level
 * includes comprehensive scoring system and visual formatting
 */
public class BlackJack extends Game {
    public static final int NUM_CARDS = 52; // total number of cards in deck

    /**
     * enumeration representing all possible card types in blackjack
     * includes ace through king with face cards having value 10
     */
    public static enum Cards {
        ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN,
        JACK, QUEEN, KING
    }

    // game state variables
    private List<Cards> playerHand; // cards currently held by player
    private List<Cards> dealerHand; // cards currently held by dealer
    private List<Cards> deck; // remaining cards in deck
    private Random random; // random number generator for shuffling and ai

    // styling constants for console output formatting
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

    /**
     * default constructor for blackjack game
     * creates a blackjack game with default settings
     */
    public BlackJack() {
        super(1, "Blackjack", 10, 10, 20);
        this.random = new Random();
    }

    /**
     * constructor for blackjack game with custom parameters
     * 
     * @param id             unique identifier for this game instance
     * @param title          display name for the game
     * @param difficulty     difficulty level (1-10, affects dealer ai)
     * @param requiredTokens cost in tokens to play
     * @param ticketReward   base reward in tickets for winning
     */
    public BlackJack(int id, String title, int difficulty, int requiredTokens, int ticketReward) {
        super(id, title, difficulty, requiredTokens, ticketReward);
        this.random = new Random();
    }

    /**
     * main method for testing the blackjack game independently
     * creates a game instance and runs it with empty items list
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        BlackJack game = new BlackJack(10, "Blackjack", 10, 10, 20);
        ArrayList<Functional> useItems = new ArrayList<>(); // placeholder for items
        game.runGame(useItems);
    }

    /**
     * main game loop for blackjack
     * handles game initialization, player turn, dealer turn, and scoring
     * 
     * @param useItems list of functional items player can use (not implemented)
     * @return number of tickets won based on game outcome
     */
    public int runGame(ArrayList<Functional> useItems) {
        // initialize game state
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
        deck = new ArrayList<>();

        // clear screen for better presentation (if supported)
        String lines = System.getProperty("LINES");
        System.out.println("\n".repeat(lines != null ? Integer.parseInt(lines) : 20));

        // display game introduction and rules
        System.out.println(STYLE_TITLE + "Welcome to Blackjack!" + STYLE_END);
        System.out.println(STYLE_INFO
                + "You will play against the dealer. Try to get as close to 21 without going over." + STYLE_END);
        System.out.println(STYLE_INFO
                + "Aces can count as 1 or 11, face cards are worth 10, and all other cards are worth their number."
                + STYLE_END);
        System.out.println(STYLE_WARNING + "Difficulty Level: " + this.getDifficulty() + STYLE_END);
        System.out.println(STYLE_TITLE + "Let's begin!" + STYLE_END + "\n");

        // initialize deck with one of each card type
        for (Cards card : Cards.values()) {
            deck.add(card);
        }

        // shuffle the deck for randomness
        Collections.shuffle(deck);

        // main game loop - continues until player stands or busts
        while (true) {
            if (playerTurn()) {
                break; // player stands or busts, end their turn
            }
        }

        // dealer plays according to ai rules
        dealerTurn();

        // determine winner and calculate rewards
        return declareWinner();
    }

    /**
     * calculates the total value of a hand of cards
     * handles ace value optimization (1 or 11) to prevent busting when possible
     * 
     * @param hand the list of cards to evaluate
     * @return the optimal total value of the hand
     */
    private int calculateHandValue(List<Cards> hand) {
        int value = 0;
        int aces = 0; // count aces separately for special handling

        // first pass: count all cards, treating aces as 11
        for (Cards card : hand) {
            switch (card) {
                case ACE:
                    value += 11; // initially count ace as 11
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

        // second pass: convert aces from 11 to 1 if hand would bust
        while (value > 21 && aces > 0) {
            value -= 10; // convert ace from 11 to 1 (difference of 10)
            aces--;
        }

        return value;
    }

    /**
     * allows player to draw another card from the deck
     * removes card from deck and adds to player's hand
     */
    private void hit() {
        if (deck.size() > 0) {
            Cards drawnCard = deck.remove(0); // draw the top card
            System.out.println(Bcolors.OKGREEN + "You drew: " + Bcolors.BOLD + drawnCard + Bcolors.ENDC);
            playerHand.add(drawnCard);
        } else {
            System.out.println(Bcolors.FAIL + "No more cards to draw!" + Bcolors.ENDC);
        }
    }

    /**
     * handles the player's turn in blackjack
     * displays hand information and prompts for hit or stand decision
     * 
     * @return true if player's turn is over (stand or bust), false to continue
     */
    private boolean playerTurn() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            // display game state header
            System.out.println(STYLE_SUBHEADER + "\n==============================");
            System.out.println("        BLACKJACK TURN        ");
            System.out.println("==============================" + STYLE_END);

            // show player's current hand
            System.out.print(STYLE_INFO + "Your hand: " + STYLE_END);
            for (Cards card : playerHand) {
                System.out.print(Bcolors.BOLD + card + " " + Bcolors.ENDC);
            }
            System.out.println();

            // calculate and display hand value with appropriate coloring
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
                hit(); // draw another card
                int value = calculateHandValue(playerHand);
                System.out.println(Bcolors.OKGREEN + "You drew a card!" + Bcolors.ENDC);

                // check if player busted
                if (value > 21) {
                    System.out.println(Bcolors.FAIL + Bcolors.BOLD + "BUST! Your hand value is " + value
                            + ". You lose this round." + Bcolors.ENDC);
                    return true; // player busted, end turn
                }
            } else if (input.equals("s") || input.equals("stand")) {
                System.out.println(Bcolors.OKCYAN + "You chose to stand." + Bcolors.ENDC);
                return true; // player stands, end turn
            } else {
                // invalid input, prompt again
                System.out.println(
                        Bcolors.FAIL + "Invalid input. Please enter 'H' to hit or 'S' to stand." + Bcolors.ENDC);
            }
        }
    }

    /**
     * allows dealer to draw another card from the deck
     * removes card from deck and adds to dealer's hand
     */
    private void hitDealer() {
        if (deck.size() > 0) {
            Cards drawnCard = deck.remove(0); // draw the top card
            System.out.println(Bcolors.OKCYAN + "Dealer drew: " + Bcolors.BOLD + drawnCard + Bcolors.ENDC);
            dealerHand.add(drawnCard);
        } else {
            System.out.println(Bcolors.FAIL + "No more cards to draw for dealer!" + Bcolors.ENDC);
        }
    }

    /**
     * handles the dealer's turn with dynamic ai based on difficulty
     * dealer behavior becomes more unpredictable at lower difficulties
     * at higher difficulties, dealer plays more optimally
     */
    private void dealerTurn() {
        System.out.println(
                STYLE_SUBHEADER + "\nDealer's turn (Difficulty Level: " + this.getDifficulty() + ")" + STYLE_END);

        // calculate dynamic threshold based on difficulty
        // base threshold is 17 (standard blackjack dealer rule)
        int baseThreshold = 17;

        // exponential variance: high when difficulty is low, low when difficulty is
        // high
        int maxVariance = 10;
        double exponent = (11.0 - this.getDifficulty()) / 10.0; // maps difficulty 1→exponent 1.0, 10→0.1
        int variance = (int) Math.max(1, Math.round(Math.pow(maxVariance, exponent)));

        // create biased random adjustment
        double u = random.nextDouble(); // flat distribution in [0,1)
        double bias = Math.pow(u, 0.5); // sqrt(u) biases toward 1
        int sign = random.nextBoolean() ? +1 : -1; // choose positive or negative
        int randomAdjustment = (int) Math.round(bias * variance) * sign;

        // calculate final threshold (17 ± randomAdjustment)
        int threshold = baseThreshold + randomAdjustment;

        System.out.println(STYLE_WARNING + "Dealer will hit until hand value reaches: " + threshold + STYLE_END);

        // dealer hits until reaching threshold
        while (calculateHandValue(dealerHand) < threshold) {
            hitDealer();
            int currentValue = calculateHandValue(dealerHand);

            // check if dealer busted
            if (currentValue > 21) {
                System.out.println(
                        Bcolors.FAIL + Bcolors.BOLD + "Dealer busted with " + currentValue + "!" + Bcolors.ENDC);
                break;
            }
        }

        System.out.println(STYLE_INFO + "Dealer stands with hand value: " + calculateHandValue(dealerHand) + STYLE_END);
    }

    /**
     * determines the winner of the blackjack game and calculates ticket rewards
     * implements comprehensive scoring system based on performance
     * 
     * @return number of tickets won based on game outcome and performance
     */
    private int declareWinner() {
        int playerValue = calculateHandValue(playerHand);
        int dealerValue = calculateHandValue(dealerHand);

        // determine header color based on outcome: green=win, red=lose, yellow=tie
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

        // display game results header
        System.out.println(headerStyle + "\n==============================");
        System.out.println("          GAME OVER           ");
        System.out.println("==============================" + STYLE_END);

        // display player's final hand
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

        // display dealer's final hand
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

        // calculate performance score and tickets won
        int ticketsWon = 0;
        if (playerValue > 21) {
            // player busted - no reward
            System.out.println(STYLE_ERROR + Bcolors.BOLD + "You busted! Dealer wins." + STYLE_END);
            ticketsWon = 0;
        } else if (dealerValue > 21 || playerValue > dealerValue) {
            // player won - calculate performance-based reward
            System.out.println(STYLE_TITLE + Bcolors.BOLD + "You win!" + STYLE_END);

            double performanceScore = 0.5; // base winning score

            // bonus for getting exactly 21 (blackjack)
            if (playerValue == 21) {
                performanceScore += 0.3;
            }

            // bonus for beating dealer by a larger margin
            if (dealerValue <= 21) {
                int margin = playerValue - dealerValue;
                performanceScore += Math.min(0.2, margin * 0.05); // up to 0.2 bonus for large margins
            } else {
                // extra bonus for dealer busting
                performanceScore += 0.2;
            }

            ticketsWon = calculateTicketReward(performanceScore);

        } else if (playerValue < dealerValue) {
            // dealer won - no reward
            System.out.println(STYLE_ERROR + Bcolors.BOLD + "Dealer wins!" + STYLE_END);
            ticketsWon = 0;
        } else {
            // tie game - small consolation reward
            System.out.println(STYLE_WARNING + Bcolors.BOLD + "It's a tie!" + STYLE_END);
            ticketsWon = calculateTicketReward(0.25);
        }

        System.out.println(headerStyle + "==============================" + STYLE_END);

        // display final ticket reward
        if (ticketsWon > 0) {
            System.out.println("\nYou have earned " + Bcolors.BOLD + Bcolors.GREEN +
                    ticketsWon + Bcolors.ENDC + " tickets!");
        } else {
            System.out.println("\nYou earned " + Bcolors.BOLD + Bcolors.RED +
                    "0" + Bcolors.ENDC + " tickets this round.");
        }

        return ticketsWon;
    }
}
