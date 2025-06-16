package com.arcade.games.madlibs;
import com.arcade.games.Game;
import com.arcade.item.Functional;
import com.arcade.item.TicketMultiplier;

import java.util.*;
public class MadLibs extends Game {
    //The story that the program will output to the user
    private String currentStory = "";

    private int ticketMultiplier = 1;

    Scanner sc = new Scanner(System.in);
    Random rand = new Random();

    //3D array of mad libs stories
    private final String[][][] stories = {
            {
                    {"Today I woke up feeling ", ", so I ate cereal with orange juice.", "adjective"},
                    {"A giant ", " was doing yoga in my backyard.", "noun"},
                    {"It offered me a ", " and said it grants wishes.", "noun"},
                    {"I wished for a ", " unicorn that sings opera.", "adjective"},
                    {"The unicorn sneezed and turned my house into a ", ".", "noun"}
            },
            {
                    {"During math class, a ", " crawled out of my backpack.", "noun"},
                    {"It whispered something very ", " into my ear.", "adjective"},
                    {"Then it started juggling ", " on the teacher’s desk.", "noun"},
                    {"Everyone laughed until a ", " dragon burst through the wall.", "adjective"},
                    {"The dragon only wanted a slice of ", ".", "noun"}
            },
            {
                    {"At the zoo, I saw a ", " juggling bananas.", "noun"},
                    {"It looked ", " while wearing sunglasses and a tuxedo.", "adjective"},
                    {"A zookeeper handed me a ", " and told me to help.", "noun"},
                    {"We danced the tango with a very ", " flamingo.", "adjective"},
                    {"Then I slipped on a ", " and woke up in bed.", "noun"}
            },
            {
                    {"My little brother built a ", " out of popsicle sticks.", "noun"},
                    {"He painted it with ", " peanut butter and glitter.", "adjective"},
                    {"Then he put a ", " on top like a cherry.", "noun"},
                    {"Suddenly it started walking and singing ", " lullabies.", "adjective"},
                    {"It stomped into the kitchen and ate all the ", ".", "noun"}
            },
            {
                    {"I went to a concert where a ", " played the triangle.", "noun"},
                    {"The lead singer wore a ", " pizza costume.", "adjective"},
                    {"Halfway through the show, someone threw a ", " on stage.", "noun"},
                    {"The band kept playing like it was totally ", ".", "adjective"},
                    {"Afterward, I got a selfie with a dancing ", ".", "noun"}
            }
    };

    public MadLibs() {
        //id=2, title="MadLibs", difficulty=1, tokensNeeded=0, reward=20
        super(2, "MadLibs", 1, 10, 10);
    }

    public MadLibs(int id, String title, int difficulty, int requiredTokens, int ticketReward){
        super(id, title, difficulty, requiredTokens, ticketReward);

    }


    public static void main(String[] args){
        MadLibs game = new MadLibs();
        ArrayList<Functional> items = new ArrayList<>();
        int earned = game.runGame(items);

    }

    @Override
    public int runGame(ArrayList<Functional> items){
        //Activate items

        for (Functional f: items) {
            if (f instanceof TicketMultiplier){
                ticketMultiplier = TicketMultiplier.MULTIPLIER;
                f.activate();
            }
        }



        int chosenStory;

        System.out.println("Welcome to MadLibs! Lets get started.");
        chosenStory = rand.nextInt(stories.length);
        for (int i = 0; i < stories[chosenStory].length; i++){
            currentStory = currentStory + "\n" + getInput(chosenStory, i);
        }
        System.out.println(currentStory);

        setTicketReward(getTicketReward() * ticketMultiplier);
        System.out.println("Thanks for playing MadLibs! You just earned" + getTicketReward() + "tickets!");
        return getTicketReward();

    }

    public String getInput(int chosenStory, int chosenSentence){
        //If statement for proper grammar
        if (stories[chosenStory][chosenSentence][2].equals("noun")){
            System.out.print("I need a ");
        } else{
            System.out.print("I need an ");
        }
        System.out.print(stories[chosenStory][chosenSentence][2] + " for this next sentence: ");
        return(stories[chosenStory][chosenSentence][0] + sc.nextLine() + stories[chosenStory][chosenSentence][1]);

    }


}
