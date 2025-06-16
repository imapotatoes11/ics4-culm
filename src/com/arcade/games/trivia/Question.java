/**
 * Question.java
 * 
 * represents a single trivia question in the arcade game
 * contains the question text, difficulty level, and correct answer
 *
 * date: jun 15, 2025
 * author: eddie qu, refactored bykevin wang
 */
package com.arcade.games.trivia;

/**
 * represents a single trivia question in the arcade game
 * contains the question text, difficulty level, and correct answer
 */
public class Question {
   // the text of the trivia question
   private String question;

   // difficulty level of the question (1-10 scale)
   private int difficultyLevel;

   // the correct answer to the question (single character)
   private char answer;

   /**
    * gets the question text
    * 
    * @return the question string
    */
   public String getQuestion() {
      return question;
   }

   /**
    * sets the question text
    * 
    * @param question the question string to set
    */
   public void setQuestion(String question) {
      this.question = question;
   }

   /**
    * gets the difficulty level of the question
    * 
    * @return the difficulty level as an integer
    */
   public int getDifficultyLevel() {
      return difficultyLevel;
   }

   /**
    * sets the difficulty level of the question
    * 
    * @param difficultyLevel the difficulty level to set (1-10)
    */
   public void setDifficultyLevel(int difficultyLevel) {
      this.difficultyLevel = difficultyLevel;
   }

   /**
    * gets the correct answer to the question
    * 
    * @return the answer as a character
    */
   public char getAnswer() {
      return answer;
   }

   /**
    * sets the correct answer to the question
    * 
    * @param answer the correct answer character to set
    */
   public void setAnswer(char answer) {
      this.answer = answer;
   }
}