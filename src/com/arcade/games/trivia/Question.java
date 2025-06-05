package com.arcade.games.trivia;

public class Question {
   private String question;
   private int difficultyLevel;
   private char answer;
   
   public String getQuestion () {
      return question;
   } 
   
   public void setQuestion (String question) {
      this.question = question;
   }
   
   public int getDifficultyLevel () {
      return difficultyLevel;
   }
   
   public void setDifficultyLevel (int difficultyLevel) {
      this.difficultyLevel = difficultyLevel;
   }
   
   public char getAnswer () {
      return answer;
   }
   
   public void setAnswer (char answer) {
      this.answer = answer;
   }
}