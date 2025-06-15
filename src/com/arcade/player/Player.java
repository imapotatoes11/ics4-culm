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

public class Player {
   private String name;
   private String username;
   private String password; // password is hashed sha-256
   private int age;
   private List<Achievement> achievements = new ArrayList<>();
   private Wallet wallet;

   public Player(String name) {
      this.name = name;
      this.wallet = new Wallet();
      // Give new players some starting tokens and tickets
      this.wallet.setTokens(50);
      this.wallet.setTickets(0);
   }

   public Player(String name, String username, String password, int age) {
      this.name = name;
      this.username = username;
      this.password = password; // should be hashed
      this.age = age;
      this.wallet = new Wallet();
      // Give new players some starting tokens and tickets
      this.wallet.setTokens(50);
      this.wallet.setTickets(0);
   }

   public String getUsername() {
      return username;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getPassword() {
      return password;
   }

   public int getAge() {
      return age;
   }

   public void setAge(int age) {
      this.age = age;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   // Adds achievement if the user doesn't already have that achievement
   public boolean addAchievement(Achievement ach) {
      for (Achievement a : achievements) {
         if (a.getName().equalsIgnoreCase(ach.getName())) {
            return false;
         }
      }
      achievements.add(ach);
      return true;
   }

   // Sorts achievements
   public void sortAchievements() {
      // Make a local array copy
      Achievement[] arr = achievements.toArray(new Achievement[0]);
      int n = arr.length;

      // Bubble sort
      for (int pass = 0; pass < n - 1; pass++) {
         for (int i = 0; i < n - 1 - pass; i++) {
            if (arr[i].getName().compareToIgnoreCase(arr[i + 1].getName()) > 0) {
               // classic three‐variable swap
               Achievement tmp = arr[i];
               arr[i] = arr[i + 1];
               arr[i + 1] = tmp;
            }
         }
      }

      // Output
      System.out.println("Achievements by alphabetical order:");
      for (Achievement a : arr) {
         System.out.println("  " + a.getName());
      }
   }

   // Searches for achivement by name
   public Achievement findAchievementByName(String targetName) {
      for (Achievement a : achievements) {
         if (a.getName().equalsIgnoreCase(targetName)) {
            return a;
         }
      }
      return null;
   }

   // reads the file, finds the line after the players name, then adds the
   // achivements
   public void saveAchievementsToFile(String FILE) throws IOException {
      // Read all lines
      List<String> original = new ArrayList<>();
      BufferedReader reader = new BufferedReader(new FileReader(FILE));
      String line;
      while ((line = reader.readLine()) != null) {
         original.add(line);
      }
      reader.close();

      // Add achivements
      List<String> output = new ArrayList<>();
      boolean seenName = false;
      boolean inserted = false;

      for (int i = 0; i < original.size(); i++) {
         String l = original.get(i);
         output.add(l);

         if (!seenName && l.equals(this.name)) {
            seenName = true;
         } else if (seenName && !inserted && l.equals(":")) {
            // insert achievements before the colon
            for (Achievement a : achievements) {
               output.add(a.getName() + ": " + a.getDescription());
            }
            inserted = true;
         }
      }

      // If name was seen but no colon after, add it
      if (seenName && !inserted) {
         for (Achievement a : achievements) {
            output.add(a.getName() + ": " + a.getDescription());
         }
      }

      BufferedWriter writer = new BufferedWriter(new FileWriter(FILE));
      for (String outLine : output) {
         writer.write(outLine);
         writer.newLine();
      }
      writer.close();
   }

   /** Returns a copy of the achievements list. */
   public List<Achievement> getAchievements() {
      return new ArrayList<Achievement>(achievements);
   }

   public String getName() {
      return name;
   }

   public Wallet getWallet() {
      return wallet;
   }

   public void setWallet(Wallet wallet) {
      this.wallet = wallet;
   }

   // Convenience methods for wallet operations
   public boolean hasEnoughTokens(int requiredTokens) {
      return wallet.getTokens() >= requiredTokens;
   }

   public boolean spendTokens(int amount) {
      if (hasEnoughTokens(amount)) {
         wallet.setTokens(wallet.getTokens() - amount);
         return true;
      }
      return false;
   }

   public void addTickets(int amount) {
      wallet.setTickets(wallet.getTickets() + amount);
   }

   public void addTokens(int amount) {
      wallet.setTokens(wallet.getTokens() + amount);
   }
}
