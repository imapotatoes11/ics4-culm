/**
 * Player.java
 *
 * represents a player in the arcade gaming system
 * manages player data, achievements, wallet, and provides
 * sorting and searching functionality for achievements
 *
 * date: jun 15, 2025
 * author: kevin wang
 */
package com.arcade.player;

import com.arcade.item.Achievement;
import com.arcade.wallet.Wallet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * represents a player in the arcade system with personal information,
 * achievements, and wallet management capabilities
 * provides methods for achievement sorting and searching
 */
public class Player {
   private String name; // player's real name
   private String username; // unique identifier for the player
   private String password; // password is hashed using sha-256
   private int age; // player's age used for difficulty adjustment
   private List<Achievement> achievements = new ArrayList<>(); // earned achievements
   private Wallet wallet; // player's tokens, tickets, and items

   /**
    * constructor for creating a player with just a name
    * initializes wallet with starting tokens and tickets
    * 
    * @param name the player's real name
    */
   public Player(String name) {
      this.name = name;
      this.wallet = new Wallet();
      // give new players some starting tokens and tickets
      this.wallet.setTokens(50);
      this.wallet.setTickets(0);
   }

   /**
    * constructor for creating a player with complete information
    * initializes wallet with starting tokens and tickets
    * 
    * @param name     the player's real name
    * @param username unique identifier for the player
    * @param password hashed password for authentication
    * @param age      player's age
    */
   public Player(String name, String username, String password, int age) {
      this.name = name;
      this.username = username;
      this.password = password; // should be hashed before passing
      this.age = age;
      this.wallet = new Wallet();
      // give new players some starting tokens and tickets
      this.wallet.setTokens(50);
      this.wallet.setTickets(0);
   }

   /**
    * gets the player's username
    * 
    * @return the username
    */
   public String getUsername() {
      return username;
   }

   /**
    * sets the player's password
    * 
    * @param password the hashed password to set
    */
   public void setPassword(String password) {
      this.password = password;
   }

   /**
    * gets the player's hashed password
    * 
    * @return the hashed password
    */
   public String getPassword() {
      return password;
   }

   /**
    * gets the player's age
    * 
    * @return the age
    */
   public int getAge() {
      return age;
   }

   /**
    * sets the player's age
    * 
    * @param age the age to set
    */
   public void setAge(int age) {
      this.age = age;
   }

   /**
    * sets the player's name
    * 
    * @param name the name to set
    */
   public void setName(String name) {
      this.name = name;
   }

   /**
    * sets the player's username
    * 
    * @param username the username to set
    */
   public void setUsername(String username) {
      this.username = username;
   }

   /**
    * adds an achievement to the player's collection
    * prevents duplicate achievements by checking names
    * 
    * @param ach the achievement to add
    * @return true if added successfully, false if already exists
    */
   public boolean addAchievement(Achievement ach) {
      // check if player already has this achievement
      for (Achievement a : achievements) {
         if (a.getName().equalsIgnoreCase(ach.getName())) {
            return false; // achievement already exists
         }
      }
      achievements.add(ach);
      return true;
   }

   /**
    * sorts achievements alphabetically by name using bubble sort algorithm
    * demonstrates sorting algorithm and displays results
    */
   public void sortAchievements() {
      // make a local array copy for sorting
      Achievement[] arr = achievements.toArray(new Achievement[0]);
      int n = arr.length;

      // bubble sort implementation
      for (int pass = 0; pass < n - 1; pass++) {
         for (int i = 0; i < n - 1 - pass; i++) {
            // compare adjacent elements case-insensitively
            if (arr[i].getName().compareToIgnoreCase(arr[i + 1].getName()) > 0) {
               // classic three-variable swap
               Achievement tmp = arr[i];
               arr[i] = arr[i + 1];
               arr[i + 1] = tmp;
            }
         }
      }

      // display sorted results
      System.out.println("Achievements by alphabetical order:");
      for (Achievement a : arr) {
         System.out.println("  " + a.getName());
      }
   }

   /**
    * searches for an achievement by name using linear search
    * performs case-insensitive matching
    * 
    * @param targetName the name of the achievement to find
    * @return the achievement if found, null otherwise
    */
   public Achievement findAchievementByName(String targetName) {
      // linear search through achievements
      for (Achievement a : achievements) {
         if (a.getName().equalsIgnoreCase(targetName)) {
            return a;
         }
      }
      return null; // achievement not found
   }

   /**
    * saves player achievements to a file
    * reads the file, finds the player's section, and adds achievements
    * 
    * @param FILE the file path to save achievements to
    * @throws IOException if file operations fail
    */
   public void saveAchievementsToFile(String FILE) throws IOException {
      // read all lines from the file first
      List<String> original = new ArrayList<>();
      BufferedReader reader = new BufferedReader(new FileReader(FILE));
      String line;
      while ((line = reader.readLine()) != null) {
         original.add(line);
      }
      reader.close();

      // add achievements to the appropriate section
      List<String> output = new ArrayList<>();
      boolean seenName = false;
      boolean inserted = false;

      for (int i = 0; i < original.size(); i++) {
         String l = original.get(i);
         output.add(l);

         if (!seenName && l.equals(this.name)) {
            seenName = true;
         } else if (seenName && !inserted && l.equals(":")) {
            // insert achievements before the colon separator
            for (Achievement a : achievements) {
               output.add(a.getName() + ": " + a.getDescription());
            }
            inserted = true;
         }
      }

      // if name was seen but no colon found, add achievements at the end
      if (seenName && !inserted) {
         for (Achievement a : achievements) {
            output.add(a.getName() + ": " + a.getDescription());
         }
      }

      // write the modified content back to the file
      BufferedWriter writer = new BufferedWriter(new FileWriter(FILE));
      for (String outLine : output) {
         writer.write(outLine);
         writer.newLine();
      }
      writer.close();
   }

   /**
    * gets a copy of the achievements list
    * returns a new list to prevent external modification
    * 
    * @return copy of the achievements list
    */
   public List<Achievement> getAchievements() {
      return new ArrayList<Achievement>(achievements);
   }

   /**
    * gets the player's name
    * 
    * @return the player's name
    */
   public String getName() {
      return name;
   }

   /**
    * gets the player's wallet
    * 
    * @return the wallet object
    */
   public Wallet getWallet() {
      return wallet;
   }

   /**
    * sets the player's wallet
    * 
    * @param wallet the wallet to set
    */
   public void setWallet(Wallet wallet) {
      this.wallet = wallet;
   }

   /**
    * checks if the player has enough tokens for a purchase
    * convenience method for token validation
    * 
    * @param requiredTokens the number of tokens needed
    * @return true if player has enough tokens, false otherwise
    */
   public boolean hasEnoughTokens(int requiredTokens) {
      return wallet.getTokens() >= requiredTokens;
   }

   /**
    * deducts tokens from the player's wallet
    * validates sufficient balance before spending
    * 
    * @param amount the number of tokens to spend
    * @return true if transaction successful, false if insufficient tokens
    */
   public boolean spendTokens(int amount) {
      if (hasEnoughTokens(amount)) {
         wallet.setTokens(wallet.getTokens() - amount);
         return true;
      }
      return false; // insufficient tokens
   }

   /**
    * adds tickets to the player's wallet
    * convenience method for ticket rewards
    * 
    * @param amount the number of tickets to add
    */
   public void addTickets(int amount) {
      wallet.setTickets(wallet.getTickets() + amount);
   }

   /**
    * adds tokens to the player's wallet
    * convenience method for token rewards
    * 
    * @param amount the number of tokens to add
    */
   public void addTokens(int amount) {
      wallet.setTokens(wallet.getTokens() + amount);
   }
}
