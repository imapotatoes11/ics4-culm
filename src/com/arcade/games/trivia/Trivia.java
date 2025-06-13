package com.arcade.games.trivia;

import com.arcade.games.Game;
import com.arcade.item.Functional;
import com.arcade.item.Luck;
import com.arcade.item.TicketMultiplier;

import java.util.*;

public class Trivia extends Game {
   private Question[] questionList = new Question[30];
   private int ticketMultiplier = 1;

   // move all question text, difficulty, and answer into one place
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

   public Trivia(int id, String title, int difficulty, int requiredTokens, int ticketReward) {
      super(id, title, difficulty, requiredTokens, ticketReward);
      // replace manual setup with one loop
      for (int i = 0; i < questionList.length; i++) {
         questionList[i] = new Question();
         Object[] d = QUESTION_DATA[i];
         questionList[i].setQuestion((String) d[0]);
         questionList[i].setDifficultyLevel((Integer) d[1]);
         questionList[i].setAnswer((Character) d[2]);
      }
   }

   public Question[] getQuestionList() {
      return questionList;
   }

   public int promptQuestion() {
      char chosenAnswer;
      int count = 0;
      Scanner sc = new Scanner(System.in);
      for (int i = 0; i < questionList.length; i++) {
         if (questionList[i].getDifficultyLevel() == getDifficulty()) {
            System.out.print(questionList[i].getQuestion());
            chosenAnswer = sc.nextLine().charAt(0);
            chosenAnswer = Character.toLowerCase(chosenAnswer);
            if (chosenAnswer == questionList[i].getAnswer()) {
               if (questionList[i].getDifficultyLevel() <= 3) {
                  count += 1;
               } else if (questionList[i].getDifficultyLevel() <= 6) {
                  count += 3;
               } else {
                  count += 5;
               }
            } else {
               if (questionList[i].getDifficultyLevel() <= 3) {
                  count -= 1;
               } else if (questionList[i].getDifficultyLevel() <= 6) {
                  count -= 3;
               } else {
                  count -= 5;
               }
            }
         }
      }

      return count;
   }

   public int runGame(ArrayList<Functional> items) {
      for (Functional f: items) {
         //If user uses luck item, decreases difficulty.
         if (f instanceof Luck){
            f.activate();
            if (getDifficulty() > 3){
               setDifficulty(getDifficulty() -2);
            }else{
               setDifficulty(1);
            }
            //If user uses ticketmultiplier item, increases the ticket multiplier
            //And by extension the num of tickets they win
         } else if (f instanceof TicketMultiplier){
            f.activate();
            ticketMultiplier = TicketMultiplier.MULTIPLIER;
            //If any other item is used, states that the item is unusable
         } else{
            System.out.println("Sorry, you can't use this power up for Diceopoly.");
         }
      }









      int originalReward = super.getTicketReward();

      System.out.println("Welcome to Trivia!");
      System.out.println("Your calculated difficulty is " + getDifficulty() + ". Here are your 3 questions:");

      int gameOutcome = promptQuestion();
      //Runs the game, then sets ticket reward to be the base ticket reward, plus the tickets earned
      //by the game, all multiplied by the ticket multiplier
      setTicketReward((getTicketReward() + promptQuestion())*ticketMultiplier);

      System.out.printf("Game Over! You've earned %d tickets!", getTicketReward());

      return getTicketReward();
   }

   public static void main(String[] args) {
      Trivia game = new Trivia(3, "Trivia", 5, 2, 10);
      ArrayList<Functional> items = new ArrayList<>(); // no items used here
      int earned = game.runGame(items);
   }
}
