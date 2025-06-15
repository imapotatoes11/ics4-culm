/**
 * Wallet.java
 *
 * represents a player's wallet in the arcade system
 * manages tokens, tickets, powerups, and trophy collections
 * provides storage for all player-owned items and currencies
 *
 * date: jun 15, 2025
 * author: kevin wang
 */
package com.arcade.wallet;

import java.util.*;
import com.arcade.item.*;

/**
 * represents a player's wallet containing all their currencies and items
 * manages tokens (used to play games), tickets (earned from games),
 * powerups (functional items), and trophies (special achievements)
 */
public class Wallet {
   private int tokens; // currency used to play games
   private int tickets; // currency earned from playing games
   private List<Functional> powerups; // functional items that can be used in games
   private List<Achievement> trophies; // special achievements stored as trophies
   // private List<Cosmetic> cosmetics; // cosmetic items (commented out for future
   // use)

   /**
    * gets the number of tokens in the wallet
    * tokens are used as currency to play games
    * 
    * @return the number of tokens
    */
   public int getTokens() {
      return tokens;
   }

   /**
    * sets the number of tokens in the wallet
    * tokens are used as currency to play games
    * 
    * @param tokens the number of tokens to set
    */
   public void setTokens(int tokens) {
      this.tokens = tokens;
   }

   /**
    * gets the number of tickets in the wallet
    * tickets are earned from playing games and can be used to purchase items
    * 
    * @return the number of tickets
    */
   public int getTickets() {
      return tickets;
   }

   /**
    * sets the number of tickets in the wallet
    * tickets are earned from playing games and can be used to purchase items
    * 
    * @param tickets the number of tickets to set
    */
   public void setTickets(int tickets) {
      this.tickets = tickets;
   }

   /**
    * gets the list of powerups in the wallet
    * powerups are functional items that can be used during games
    * 
    * @return the list of powerups (may be null if not initialized)
    */
   public List<Functional> getPowerups() {
      return powerups;
   }

   /**
    * sets the list of powerups in the wallet
    * powerups are functional items that can be used during games
    * 
    * @param powerups the list of powerups to set
    */
   public void setPowerups(List<Functional> powerups) {
      this.powerups = powerups;
   }

   /**
    * gets the list of trophies in the wallet
    * trophies are special achievements that are stored in the wallet
    * 
    * @return the list of trophies (may be null if not initialized)
    */
   public List<Achievement> getTrophies() {
      return trophies;
   }

   /**
    * sets the list of trophies in the wallet
    * trophies are special achievements that are stored in the wallet
    * 
    * @param trophies the list of trophies to set
    */
   public void setTrophies(List<Achievement> trophies) {
      this.trophies = trophies;
   }

   // public List<Cosmetic> getCosmestics () {
   // return cosmetics;
   // }

   // public void setCosmetics (List<Cosmetic> cosmetics) {
   // this.cosmetics = cosmetics;
   // }
}
