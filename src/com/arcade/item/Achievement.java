package com.arcade.item;
import com.arcade.item.Item;

public class Achievement extends Item {
   private String description;
    
   public Achievement (String name, String description) {
       super(name);
       this.description = description;
   }

   public String getDescription() {
       return description;
   }

   public void setName(String name) {
       this.description = description;
   }
   
   public static Achievement champion() {
       return new Achievement("Champion Achievement Unlocked!", "Congratulation! This achievement is awarded for playing over 10 games inside the simulation arcade!");
   }
   
   public static Achievement powerUpPro() {
       return new Achievement("Power-Up Pro Achievement Unlocked!", "Congratulation! This achievement is awarded for using over 3 items inside the simulation arcade!");
   }
   
   public static Achievement levelLegend() {
       return new Achievement("Level Legend Achievement Unlocked!", "Congratulation! This achievement is awarded for completing a game over difficulty level 7 inside the simulation arcade!");
   }
   
   public static Achievement bossBuster() {
       return new Achievement("Boss Buster Achievement Unlocked!", "Congratulation! This achievement is awarded for completing a game at difficulty level 10 inside the simulation arcade!");
   }
   
   public static Achievement deathPixel() {
       return new Achievement("Death Pixel Achievement Unlocked!", "Congratulation! This achievement is awarded for losing/dying once inside the simulation arcade!");
   }
}