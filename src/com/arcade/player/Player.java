package com.arcade.player;

public class Player {
   private String username;
   private String password;
   private int age;
   private String name;
   private int creditCardInfo;


   public Player(String username, String password) {
      this.username = username;
      this.password = password;
      this.age = 0; // default age
      this.name = ""; // default name
      this.creditCardInfo = 0; // default credit card info
   }

   public Player() {
      this.username = "";
      this.password = "";
      this.age = 0; // default age
      this.name = ""; // default name
      this.creditCardInfo = 0; // default credit card info
   }

   public Player(String username, String password, int age, String name, int creditCardInfo) {
      this.username = username;
      this.password = password;
      this.age = age;
      this.name = name;
      this.creditCardInfo = creditCardInfo;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public int getAge() {
      return age;
   }

   public void setAge(int age) {
      this.age = age;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getCreditCardInfo() {
      return creditCardInfo;
   }

   public void setCreditCardInfo(int creditCardInfo) {
      this.creditCardInfo = creditCardInfo;
   }
}