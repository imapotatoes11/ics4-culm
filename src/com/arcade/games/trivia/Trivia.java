public class Trivia extends Game {
   private Question[] questionList = new Question[30];
   
   public Trivia () {
     questionList[0].setQuestion("What color is grass? \nA) Blue \nB) Green \nC) Red \nD) Yellow");
     questionList[0].setDifficultyLevel(1);
     questionList[0].setAnswer('b');
     
     questionList[1].setQuestion("What vehicle can fly? \nA) Car \nB) Train \nC) Plane \nD) Submarine");
     questionList[1].setDifficultyLevel(1);
     questionList[1].setAnswer('c');
     
     questionList[2].setQuestion("What is the second letter of the alphabet? \nA) C \nB) A \nC) D \nD) B");
     questionList[2].setDifficultyLevel(1);
     questionList[2].setAnswer('d');
     
     questionList[3].setQuestion("Who is the main character of Kung Fu Panda? \nA) Viper \nB) Monkey \nC) Shifu \nD) Po");
     questionList[3].setDifficultyLevel(2);
     questionList[3].setAnswer('d');
     
     questionList[4].setQuestion("How many novels are in the Harry Potter Series? \nA) 7 \nB) 6 \nC) 5 \nD) 8");
     questionList[4].setDifficultyLevel(2);
     questionList[4].setAnswer('a');
     
     questionList[5].setQuestion("What majority color is Pikachu from Pokemon? \nA) Violet \nB) Green \nC) Yellow \nD) Black");
     questionList[5].setDifficultyLevel(2);
     questionList[5].setAnswer('c');
     
     questionList[6].setQuestion("What is the biggest planet in our solar system? \nA) Mercury \nB) Sun \nC) Jupiter \nD) Earth");
     questionList[6].setDifficultyLevel(3);
     questionList[6].setAnswer('c');
     
     questionList[7].setQuestion("What is the second largest country by land area? \nA) Brazil \nB) Canada \nC) USA \nD) China");
     questionList[7].setDifficultyLevel(3);
     questionList[7].setAnswer('b');
     
     questionList[8].setQuestion("Who founded the tech company Apple? \nA) Elon Musk \nB) Jeff Bezos \nC) Steve Jobs \nD) Warren Buffett");
     questionList[8].setDifficultyLevel(3);
     questionList[8].setAnswer('c');
     
     questionList[9].setQuestion("Who painted the Mona Lisa? \nA) Leonardo Davinci \nB) Pablo Picasso \nC) Bob Ross \nD) Vincent Van Gogh");
     questionList[9].setDifficultyLevel(4);
     questionList[9].setAnswer('a');
     
     questionList[10].setQuestion("Which actor did not play Spiderman? \nA) Andrew Garfield \nB) Tobey Maguire \nC) Tom Holland \nD) Henry Cavil");
     questionList[10].setDifficultyLevel(4);
     questionList[10].setAnswer('d');
     
     questionList[11].setQuestion("What is the most common eye color in the world? \nA) Black \nB) Blue \nC) Green \nD) Brown");
     questionList[11].setDifficultyLevel(4);
     questionList[11].setAnswer('d');
     
     questionList[12].setQuestion("What country is the Taj Mahal located in? \nA) USA \nB) Brazil \nC) France \nD) India");
     questionList[12].setDifficultyLevel(5);
     questionList[12].setAnswer('d');
     
     questionList[13].setQuestion("What is the capital of Italy? \nA) Venice \nB) Rome \nC) Vatican City \nD) Milan");
     questionList[13].setDifficultyLevel(5);
     questionList[13].setAnswer('b');
     
     questionList[14].setQuestion("Which planet in our solar system has the most moons? \nA) Jupiter \nB) Earth \nC) Saturn \nD) Uranus");
     questionList[14].setDifficultyLevel(5);
     questionList[14].setAnswer('c');
     
     questionList[15].setQuestion("Which of the following Celebrities are not bald? \nA) Tom Hardy \nB) Vin Diesel \nC) Dwayne Johnson \nD) Joe Rogan");
     questionList[15].setDifficultyLevel(6);
     questionList[15].setAnswer('a');
     
     questionList[16].setQuestion("What is the only letter not used in American State? \nA) Z \nB) P \nC) X \nD) Q");
     questionList[16].setDifficultyLevel(6);
     questionList[16].setAnswer('d');
     
     questionList[17].setQuestion("Which novel commences with the line Call Me Ishmael? \nA) Hamlet \nB) Percy Jackson \nC) Moby Dick \nD) Hunger Games");
     questionList[17].setDifficultyLevel(6);
     questionList[17].setAnswer('c');
     
     questionList[18].setQuestion("Which novel features the fictional language Newspeak? \nA) Brave New World \nB) The Road \nC) 1984 \nD) Hunger Games");
     questionList[18].setDifficultyLevel(7);
     questionList[18].setAnswer('c');
     
     questionList[19].setQuestion("What year did the Belin Wall fall? \nA) 1988 \nB) 1989 \nC) 1990 \nD) 1991");
     questionList[19].setDifficultyLevel(7);
     questionList[19].setAnswer('b');
     
     questionList[20].setQuestion("What is the name of a word that can be read the same forwards and backwards? \nA) Acroynm \nB) Emordnilap \nC) Palindrome \nD) Synonym");
     questionList[20].setDifficultyLevel(7);
     questionList[20].setAnswer('c');
     
     questionList[21].setQuestion("What element has the highest boiling point? \nA) Tungsten \nB) Plutonium \nC) Uranium \nD) Einsteinium");
     questionList[21].setDifficultyLevel(8);
     questionList[21].setAnswer('a');
     
     questionList[22].setQuestion("What is the oldest written language still used present day? \nA) English \nB) Hindi \nC) Arabic \nD) Chinese");
     questionList[22].setDifficultyLevel(8);
     questionList[22].setAnswer('d');
     
     questionList[23].setQuestion("What year did the Chernobyl nuclear disaster occur? \nA) 1985 \nB) 1986 \nC) 1987 \nD) 1988");
     questionList[23].setDifficultyLevel(8);
     questionList[23].setAnswer('b');
     
     questionList[24].setQuestion("What is the name of the largest moon on saturn? \nA) Titan \nB) Colossal \nC) Alpha 6 \nD) Gamma 9");
     questionList[24].setDifficultyLevel(9);
     questionList[24].setAnswer('a');
     
     questionList[25].setQuestion("What branch of mathematics is not included in Calculus AB? \nA) Projection \nB) Differentiation \nC) Integration \nD) Infinite Series");
     questionList[25].setDifficultyLevel(9);
     questionList[25].setAnswer('a');
  }
     
  public Question[] getQuestionList () {
     return questionList;   
  }
}