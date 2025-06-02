import java.util.*;
import com.arcade.item.*;

public class Wallet {
   private int tokens;
   private int tickets;
   private List<Functional> powerups;
   private List<Achievement> trophies;
   private List<Cosmetic> cosmetics;
   
   public int getTokens () {
      return tokens;
   }
   
   public void setTokens (int tokens) {
      this.tokens = tokens;
   }
   
   public int getTickets () {
      return tickets;
   }
   
   public void setTickets (int tickets) {
      this.tickets = tickets;
   }
   
   public List<Functional> getPowerups () {
      return powerups;
   }
   
   public void setPowerups (List<Functional> powerups) {
      this.powerups = powerups;
   }
   
   public List<Achievement> getTrophies () {
      return trophies;
   }
   
   public void setTrophies (List<Achievement> trophies) {
      this.trophies = trophies;
   }
   
   public List<Cosmetic> getCosmestics () {
      return cosmetics;
   }
   
   public void setCosmetics (List<Cosmetic> cosmetics) {
      this.cosmetics = cosmetics;
   }
}