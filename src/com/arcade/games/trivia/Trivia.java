/**
 * Trivia.java
 *
 * implementation of a trivia question game for the arcade system
 * features difficulty-based questions with scoring system and item support
 * includes luck items and ticket multipliers for enhanced gameplay
 *
 * date: jun 15, 2025
 * author: kevin wang
 */
package com.arcade.games.trivia;

import com.arcade.games.Game;
import com.arcade.item.Functional;
import com.arcade.item.Luck;
import com.arcade.item.TicketMultiplier;

import java.util.*;

/**
 * trivia game implementation extending the base Game class
 * presents multiple choice questions based on difficulty level
 * supports functional items like luck and ticket multipliers
 */
public class Trivia extends Game {
   private Question[] questionList = new Question[30]; // array holding all available questions
   private int ticketMultiplier = 1; // multiplier for final ticket rewards

   // centralized question data: [question text, difficulty level, correct answer]
   // this design makes it easier to add/modify questions without changing code
   // structure
   private static final Object[][] QUESTION_DATA = new Object[][] {
         { "What color is grass? \nA) Blue \nB) Green \nC) Red \nD) Yellow\n", 1, 'b' },
         { "What vehicle can fly? \nA) Car \nB) Train \nC) Plane \nD) Submarine\n", 1, 'c' },
         { "What is the second letter of the alphabet? \nA) C \nB) A \nC) D \nD) B\n", 1, 'd' },
         { "Who is the main character of Kung Fu Panda? \nA) Viper \nB) Monkey \nC) Shifu \nD) Po\n", 2, 'd' },
         { "How many novels are in the Harry Potter Series? \nA) 7 \nB) 6 \nC) 5 \nD) 8\n", 2, 'a' },
         { "What majority color is Pikachu from Pokemon? \nA) Violet \nB) Green \nC) Yellow \nD) Black\n", 2, 'c' },
         { "What is the biggest planet in our solar system? \nA) Mercury \nB) Sun \nC) Jupiter \nD) Earth\n", 3, 'c' },
         { "What is the second largest country by land area? \nA) Brazil \nB) Canada \nC) USA \nD) China\n", 3, 'b' },
         { "Who founded the tech company Apple? \nA) Elon Musk \nB) Jeff Bezos \nC) Steve Jobs \nD) Warren Buffett\n",
               3, 'c' },
         { "Who painted the Mona Lisa? \nA) Leonardo Davinci \nB) Pablo Picasso \nC) Bob Ross \nD) Vincent Van Gogh\n",
               4, 'a' },
         { "Which actor did not play Spiderman? \nA) Andrew Garfield \nB) Tobey Maguire \nC) Tom Holland \nD) Henry Cavil\n",
               4, 'd' },
         { "What is the most common eye color in the world? \nA) Black \nB) Blue \nC) Green \nD) Brown\n", 4, 'd' },
         { "What country is the Taj Mahal located in? \nA) USA \nB) Brazil \nC) France \nD) India\n", 5, 'd' },
         { "What is the capital of Italy? \nA) Venice \nB) Rome \nC) Vatican City \nD) Milan\n", 5, 'b' },
         { "Which planet in our solar system has the most moons? \nA) Jupiter \nB) Earth \nC) Saturn \nD) Uranus\n", 5,
               'c' },
         { "Which of the following Celebrities are not bald? \nA) Tom Hardy \nB) Vin Diesel \nC) Dwayne Johnson \nD) Joe Rogan\n",
               6, 'a' },
         { "What is the only letter not used in American State? \nA) Z \nB) P \nC) X \nD) Q\n", 6, 'd' },
         { "Which novel commences with the line Call Me Ishmael? \nA) Hamlet \nB) Percy Jackson \nC) Moby Dick \nD) Hunger Games\n",
               6, 'c' },
         { "Which novel features the fictional language Newspeak? \nA) Brave New World \nB) The Road \nC) 1984 \nD) Hunger Games\n",
               7, 'c' },
         { "What year did the Berlin Wall fall? \nA) 1988 \nB) 1989 \nC) 1990 \nD) 1991\n", 7, 'b' },
         { "What is the name of a word that can be read the same forwards and backwards? \nA) Acroynm \nB) Emordnilap \nC) Palindrome \nD) Synonym\n",
               7, 'c' },
         { "What element has the highest boiling point? \nA) Tungsten \nB) Plutonium \nC) Uranium \nD) Einsteinium\n",
               8, 'a' },
         { "What is the oldest written language still used present day? \nA) English \nB) Hindi \nC) Arabic \nD) Chinese\n",
               8, 'd' },
         { "What year did the Chernobyl nuclear disaster occur? \nA) 1985 \nB) 1986 \nC) 1987 \nD) 1988\n", 8, 'b' },
         { "What is the name of the largest moon on Saturn? \nA) Titan \nB) Colossal \nC) Alpha 6 \nD) Gamma 9\n", 9,
               'a' },
         { "Rounded down to the nearest minute, what is the record breathe hold time underwater? \nA) 24 minutes \nB) 18 minutes \nC) 30 minutes \nD) 16 minutes\n",
               9, 'a' },
         { "Rounded to the nearest meter, How tall is the CN Tower? \nA) 513 m \nB) 533 m \nC) 553 m \nD) 573 m\n", 9,
               'c' },
         { "What is the only continent where coffee grows naturally? \nA) Asia \nB) South America \nC) Europe \nD) Africa\n",
               10, 'd' },
         { "What color does gold leaf paper appear if you hold it up to a light? \nA) Still Gold \nB) Green \nC) Red \nD) Faint Blue\n",
               10, 'b' },
         { "How long is a Jiffy? \nA) 1 millionth of a second \nB) 1 billionth of a second \nC) 1 trillionth of a second \nD) 1 thousandth of a second\n",
               10, 'c' },
   };

   /**
    * constructor for creating a trivia game with custom parameters
    * initializes all questions from the centralized data array
    * 
    * @param id             unique identifier for this game instance
    * @param title          display name for the game
    * @param difficulty     difficulty level (1-10, determines which questions are
    *                       asked)
    * @param requiredTokens cost in tokens to play
    * @param ticketReward   base reward in tickets for winning
    */
   public Trivia(int id, String title, int difficulty, int requiredTokens, int ticketReward) {
      super(id, title, difficulty, requiredTokens, ticketReward);
      // initialize questions using data from the static array
      // this loop converts the generic object array into proper Question objects
      for (int i = 0; i < questionList.length; i++) {
         questionList[i] = new Question();
         Object[] d = QUESTION_DATA[i]; // get data for this question
         questionList[i].setQuestion((String) d[0]); // question text
         questionList[i].setDifficultyLevel((Integer) d[1]); // difficulty level
         questionList[i].setAnswer((Character) d[2]); // correct answer
      }
   }

   /**
    * default constructor for trivia game
    * creates a trivia game with default settings for testing
    */
   public Trivia() {
      super(1, "Trivia", 10, 10, 20);
   }

   /**
    * gets the array of all available questions
    * 
    * @return array of Question objects
    */
   public Question[] getQuestionList() {
      return questionList;
   }

   /**
    * main game loop for trivia
    * processes functional items, runs questions, and calculates rewards
    * 
    * @param items list of functional items player can use during the game
    * @return number of tickets won based on performance
    */
   public int runGame(ArrayList<Functional> items) {
      // process functional items before starting the game
      for (Functional f : items) {
         // luck item reduces difficulty by 2 levels (minimum 1)
         if (f instanceof Luck) {
            f.activate();
            if (getDifficulty() > 3) {
               setDifficulty(getDifficulty() - 2);
            } else {
               setDifficulty(1); // ensure difficulty never goes below 1
            }
            // ticket multiplier increases final reward
         } else if (f instanceof TicketMultiplier) {
            f.activate();
            ticketMultiplier = TicketMultiplier.MULTIPLIER;
            // reject unusable items with feedback
         } else {
            System.out.println("Sorry, you can't use this power up for Trivia.");
         }
      }

      System.out.println("Welcome to Trivia!");
      System.out.println("Your calculated difficulty is " + getDifficulty() + ". Here are your questions:");

      // run the actual trivia questions and get results
      TriviaResult result = runTriviaQuestions();

      // calculate performance-based ticket reward
      double performanceScore = calculatePerformanceScore(result);
      int baseTickets = calculateTicketReward(performanceScore);
      int finalTickets = baseTickets * ticketMultiplier; // apply multiplier

      System.out.printf("Game Over! You answered %d/%d questions correctly with a score of %d!\n",
            result.correctAnswers, result.totalQuestions, result.score);
      System.out.printf("You've earned %d tickets!\n", finalTickets);

      return finalTickets;
   }

   /**
    * container class for trivia game results
    * holds all the statistics from a completed trivia session
    */
   private static class TriviaResult {
      int score; // total points earned (can be negative)
      int correctAnswers; // number of questions answered correctly
      int totalQuestions; // total number of questions asked

      /**
       * constructor for trivia result data
       * 
       * @param score          total points earned
       * @param correctAnswers number of correct answers
       * @param totalQuestions total questions asked
       */
      TriviaResult(int score, int correctAnswers, int totalQuestions) {
         this.score = score;
         this.correctAnswers = correctAnswers;
         this.totalQuestions = totalQuestions;
      }
   }

   /**
    * runs through all questions matching the current difficulty level
    * handles user input, scoring, and provides feedback for each answer
    * 
    * @return TriviaResult object containing game statistics
    */
   public TriviaResult runTriviaQuestions() {
      char chosenAnswer;
      int score = 0; // running total of points (can go negative)
      int correctAnswers = 0;
      int totalQuestions = 0;
      Scanner sc = new Scanner(System.in);

      // iterate through all questions to find ones matching current difficulty
      for (int i = 0; i < questionList.length; i++) {
         if (questionList[i].getDifficultyLevel() == getDifficulty()) {
            totalQuestions++;
            System.out.print(questionList[i].getQuestion());
            chosenAnswer = sc.nextLine().charAt(0);
            chosenAnswer = Character.toLowerCase(chosenAnswer); // normalize to lowercase

            if (chosenAnswer == questionList[i].getAnswer()) {
               correctAnswers++;
               System.out.println("✅ Correct!");
               // scoring system: easier questions worth fewer points
               if (questionList[i].getDifficultyLevel() <= 3) {
                  score += 1; // easy questions worth 1 point
               } else if (questionList[i].getDifficultyLevel() <= 6) {
                  score += 3; // medium questions worth 3 points
               } else {
                  score += 5; // hard questions worth 5 points
               }
            } else {
               System.out.println("❌ Wrong! The correct answer was " +
                     questionList[i].getAnswer());
               // wrong answers subtract points (creates risk/reward)
               if (questionList[i].getDifficultyLevel() <= 3) {
                  score -= 1;
               } else if (questionList[i].getDifficultyLevel() <= 6) {
                  score -= 3;
               } else {
                  score -= 5;
               }
            }
         }
      }

      return new TriviaResult(score, correctAnswers, totalQuestions);
   }

   /**
    * calculates performance score based on trivia results
    * combines accuracy percentage with score bonus for comprehensive evaluation
    * 
    * @param result the TriviaResult containing game statistics
    * @return performance score from 0.0 (worst) to 1.0 (best)
    */
   private double calculatePerformanceScore(TriviaResult result) {
      if (result.totalQuestions == 0) {
         return 0.0; // no questions answered
      }

      // base score from correct answers percentage (0.0 to 0.7)
      double accuracyScore = (double) result.correctAnswers / result.totalQuestions * 0.7;

      // bonus for positive total score (0.0 to 0.2)
      double scoreBonus = 0.0;
      if (result.score > 0) {
         // normalize score bonus based on maximum possible score for this difficulty
         int maxPossibleScore = result.totalQuestions *
               (getDifficulty() <= 3 ? 1 : getDifficulty() <= 6 ? 3 : 5);
         scoreBonus = Math.min(0.2, (double) result.score / maxPossibleScore * 0.2);
      }

      // difficulty bonus: harder difficulties get slight bonus (0.0 to 0.1)
      double difficultyBonus = (getDifficulty() - 1) / 90.0; // scales from 0 to 0.1

      return Math.max(0.0, Math.min(1.0, accuracyScore + scoreBonus + difficultyBonus));
   }

   /**
    * main method for testing the trivia game independently
    * creates a game instance and runs it with empty items list
    * 
    * @param args command line arguments (not used)
    */
   public static void main(String[] args) {
      Trivia t = new Trivia();
      t.runGame(new ArrayList<>());
   }
}
