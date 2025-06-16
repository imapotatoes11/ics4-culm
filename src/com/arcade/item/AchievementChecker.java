/**
 * AchievementChecker.java
 *
 * utility class for checking and awarding achievements based on game events
 * contains logic for determining when achievements should be awarded
 * works with all games in the arcade system
 *
 * date: jun 15, 2025
 * author: kevin wang
 */
package com.arcade.item;

import java.util.ArrayList;
import java.util.List;

/**
 * utility class for checking achievement conditions and awarding achievements
 * provides static methods for different types of achievement checks
 */
public class AchievementChecker {

    /**
     * checks for general achievements that apply to any game
     * 
     * @param gameWon          true if the player won the game
     * @param performanceScore the performance score (0.0 to 1.0)
     * @param difficulty       the difficulty level the game was played on
     * @param usedExtraLife    true if the player used an extra life item
     * @return list of achievements earned
     */
    public static List<Achievement> checkGeneralAchievements(boolean gameWon, double performanceScore,
            int difficulty, boolean usedExtraLife) {
        List<Achievement> achievements = new ArrayList<>();

        // First Victory - any game win
        if (gameWon) {
            achievements.add(Achievement.firstVictory());
        }

        // Perfect Performance - score of 1.0
        if (gameWon && performanceScore >= 1.0) {
            achievements.add(Achievement.perfectPerformance());
        }

        // Hard Mode Champion - win on max difficulty (10)
        if (gameWon && difficulty >= 10) {
            achievements.add(Achievement.hardModeChampion());
        }

        // Comeback Kid - win after using extra life
        if (gameWon && usedExtraLife) {
            achievements.add(Achievement.comebackKid());
        }

        // Game Over - consolation for losing
        if (!gameWon) {
            achievements.add(Achievement.gameOver());
        }

        return achievements;
    }

    /**
     * checks for blackjack-specific achievements
     * 
     * @param gameWon         true if the player won
     * @param playerHandValue the final value of the player's hand
     * @param dealerHandValue the final value of the dealer's hand
     * @return list of achievements earned
     */
    public static List<Achievement> checkBlackjackAchievements(boolean gameWon, int playerHandValue,
            int dealerHandValue) {
        List<Achievement> achievements = new ArrayList<>();

        // Blackjack Natural - exactly 21
        if (gameWon && playerHandValue == 21) {
            achievements.add(Achievement.blackjackNatural());
        }

        // Dealer Buster - dealer went over 21
        if (gameWon && dealerHandValue > 21) {
            achievements.add(Achievement.dealerBuster());
        }

        return achievements;
    }

    /**
     * checks for pokeman-specific achievements
     * 
     * @param gameWon               true if the player won all battles
     * @param battlesWon            number of battles won
     * @param totalBattles          total battles in the game
     * @param finalHealthPercentage remaining health as percentage (0.0 to 1.0)
     * @return list of achievements earned
     */
    public static List<Achievement> checkPokemanAchievements(boolean gameWon, int battlesWon,
            int totalBattles, double finalHealthPercentage) {
        List<Achievement> achievements = new ArrayList<>();

        // Pokeman Champion - win all battles
        if (gameWon && battlesWon == totalBattles) {
            achievements.add(Achievement.pokemanChampion());
        }

        // Flawless Victory - complete with full health
        if (gameWon && finalHealthPercentage >= 1.0) {
            achievements.add(Achievement.flawlessVictory());
        }

        // Boss Buster - defeat the final boss (battle 4)
        if (battlesWon >= 4) {
            achievements.add(Achievement.bossBuster());
        }

        return achievements;
    }

    /**
     * checks for trivia-specific achievements
     * 
     * @param correctAnswers number of correct answers
     * @param totalQuestions total questions asked
     * @param difficulty     the difficulty level played
     * @param score          the final score achieved
     * @return list of achievements earned
     */
    public static List<Achievement> checkTriviaAchievements(int correctAnswers, int totalQuestions,
            int difficulty, int score) {
        List<Achievement> achievements = new ArrayList<>();

        // Trivia Master - all questions correct
        if (correctAnswers == totalQuestions && totalQuestions > 0) {
            achievements.add(Achievement.triviaMaster());
        }

        // Scholar - high accuracy on hard difficulty
        if (difficulty >= 8 && totalQuestions > 0) {
            double accuracy = (double) correctAnswers / totalQuestions;
            if (accuracy >= 0.8) { // 80% or better
                achievements.add(Achievement.scholar());
            }
        }

        return achievements;
    }

    /**
     * checks for escape room specific achievements
     * 
     * @param gameWon               true if the player escaped
     * @param completionTimeSeconds time taken to complete in seconds
     * @return list of achievements earned
     */
    public static List<Achievement> checkEscapeRoomAchievements(boolean gameWon, long completionTimeSeconds) {
        List<Achievement> achievements = new ArrayList<>();

        // Escape Artist - complete the escape room
        if (gameWon) {
            achievements.add(Achievement.escapeArtist());
        }

        // Speed Runner - complete in under 3 minutes (180 seconds)
        if (gameWon && completionTimeSeconds < 180) {
            achievements.add(Achievement.speedRunner());
        }

        return achievements;
    }

    /**
     * checks for diceopoly specific achievements
     * 
     * @param finalPosition the final position on the board
     * @param boardLength   the total length of the board
     * @return list of achievements earned
     */
    public static List<Achievement> checkDiceopolyAchievements(int finalPosition, int boardLength) {
        List<Achievement> achievements = new ArrayList<>();

        // Board Master - reach the end of the board
        if (finalPosition >= boardLength - 1) {
            achievements.add(Achievement.boardMaster());
        }

        return achievements;
    }

    /**
     * checks for madlibs specific achievements
     * 
     * @param gameCompleted true if the story was completed
     * @return list of achievements earned
     */
    public static List<Achievement> checkMadLibsAchievements(boolean gameCompleted) {
        List<Achievement> achievements = new ArrayList<>();

        // Creative Writer - complete any Mad Libs story
        if (gameCompleted) {
            achievements.add(Achievement.creativeWriter());
        }

        return achievements;
    }

    /**
     * displays achievements to the player
     * 
     * @param achievements list of achievements to display
     */
    public static void displayAchievements(List<Achievement> achievements) {
        if (achievements.isEmpty()) {
            return;
        }

        System.out.println("\n🏆 =============== ACHIEVEMENTS UNLOCKED! =============== 🏆");
        for (Achievement achievement : achievements) {
            System.out.println("🎉 " + achievement.getName());
            System.out.println("   " + achievement.getDescription());
            System.out.println();
        }
        System.out.println("🏆 ===================================================== 🏆\n");
    }
}