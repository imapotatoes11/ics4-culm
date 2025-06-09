package src.com.arcade.games.diceopoly;
import java.util.*;
import com.arcade.games.Game;

public class Diceopoly{
    private int pos = 0; //The users position on the board
    private int diceCount = 7; //The number of dice the user can roll
    private int boardLength = 30; //The length of the board array
    private String[] board = new String[boardLength]; //The board the user is on
    private int dice; //Your dice roll
    Scanner sc = new Scanner(System.in);
    Random rand = new Random();


    public static void main(String[] args) {
        Diceopoly game = new Diceopoly();
        game.generateBoard();
        game.printBoard();
        game.moving();
        System.out.println("Game Over! You finished at position " + game.pos);
    }


    public void moving() {
        while (diceCount > 0 && pos < boardLength - 1) {
            System.out.print("Enter anything to roll a dice!: ");
            String input = sc.nextLine();

            if (input != null) {
                dice = rollDice();
                System.out.println("You rolled a " + dice);

                //Move one tile at a time
                for (int step = 0; step < dice && pos < boardLength - 1; step++) {
                    pos++;
                    printBoard();

                    //End early if somehow moved to last tile
                    if (pos >= boardLength - 1) {
                        System.out.println("You reached the end of the board. You Win!");
                        return;
                    }
                }

                String tile = board[pos];

                if (tile.contains("Dice")) {
                    moveDice(tile);
                } else if (tile.contains("Move")) {
                    moveSpace(tile);
                } else {
                    emptySpace();
                }
                System.out.println("Current Position: " + pos);
                System.out.println("Remaining Dice: " + diceCount);

                diceCount--;
            }
        }

        System.out.println("Game Over! You finished at position " + pos);
    }

    public void printBoard() {
        String currentBoard = "|  Player  |";
        for (int i = pos + 1; i < pos + 5; i++) {
            currentBoard += board[i] + " ";
        }
        System.out.println("Board View: " + currentBoard);
    }





    public int rollDice(){
        return(rand.nextInt(1, 7));
    }

    public void generateBoard(){
        for (int i = 0; i < boardLength; i ++){
            board[i] = generateTile();
        }
    }
    //Used by generateBoard to generate a singular tile
    public String generateTile(){
        int randomNum;
        String tile;
        randomNum = rand.nextInt(1, 10);
        switch (randomNum){
            case 5:
                //extra dice tile
                tile = "| +" + rand.nextInt(1, 3) + " Dice  |";
                break;

            case 6:
                //minus dice tile
                tile = "| -" + rand.nextInt(1, 3) + " Dice  |";
                break;

            case 7:
                //move forward tile
                tile = "|Move -> " + rand.nextInt(1, 4) + " |";
                break;

            case 8:
                //move back tile
                tile = "|Move <- " + rand.nextInt(1, 4) + " |";
                break;
            case 9:
                //Extra ticket tile
                tile = "|   [ " + rand.nextInt(1, 4) + "]   |";
                break;
            default:
                //Empty tile
                tile = "|          |";
        }
        return(tile);
    }


    public void emptySpace() {
        System.out.println("Landed on an empty tile. Nothing happens.");
    }

    //Triggers when player lands on move space. Moves the user forwards or back
    public void moveSpace(String tile) {
        int move = Integer.parseInt(tile.replaceAll("[^0-9]", ""));

        if (tile.contains("->")) {
            System.out.println("Moving forward by " + move + " tiles!");
            pos = Math.min(pos + move, boardLength - 1);
        } else if (tile.contains("<-")) {
            System.out.println("Moving backward by " + move + " tiles!");
            pos = Math.max(pos - move, 0);
        }
    }


    public void moveDice(String tile) {
        int amount = Integer.parseInt(tile.replaceAll("[^0-9]", ""));
        if (tile.contains("+")) {
            System.out.println("Gained " + amount + " extra dice!");
            diceCount += amount;
        } else if (tile.contains("-")) {
            System.out.println("Lost " + amount + " dice!");
            if (diceCount < amount){
                diceCount = 0;
            }else {
                diceCount -= amount;
            }
        }
    }


}


