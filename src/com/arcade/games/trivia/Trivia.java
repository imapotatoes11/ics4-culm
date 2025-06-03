public class Trivia extends Game {
   private Question[] questionList = new Question[30];
   
   public void setAllQuestions () {
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
  }
     
  public Question[] getQuestionList () {
     return questionList;   
  }
}