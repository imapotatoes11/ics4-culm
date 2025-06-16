/**
 * MadLibs.java
 *
 * implementation of a mad libs word game for the arcade system
 * players fill in words to create funny stories
 * features multiple story templates and difficulty-based complexity
 *
 * date: jun 15, 2025
 * author: kevin wang
 */
package com.arcade.games.madlibs;

import java.util.*;
import com.arcade.games.Game;
import com.arcade.item.Functional;
import com.arcade.item.TicketMultiplier;
import com.arcade.util.Bcolors;

/**
 * mad libs game implementation extending the base Game class
 * presents various story templates that users fill with their own words
 * supports functional items like ticket multipliers for enhanced rewards
 */
public class MadLibs extends Game {
    private Scanner scanner;
    private Random random;
    private int ticketMultiplier = 1;
    private List<StoryTemplate> storyTemplates;

    // styling constants for console output formatting
    private static final String STYLE_TITLE = Bcolors.BOLD + Bcolors.OKGREEN;
    private static final String STYLE_INFO = Bcolors.OKCYAN;
    private static final String STYLE_WARNING = Bcolors.WARNING;
    private static final String STYLE_HEADER = Bcolors.BOLD + Bcolors.BRIGHT_CYAN;
    private static final String STYLE_WIN_HEADER = Bcolors.BOLD_GREEN;
    private static final String STYLE_END = Bcolors.ENDC;

    /**
     * default constructor for mad libs game
     * creates a mad libs game with default settings
     */
    public MadLibs() {
        super(6, "Mad Libs", 5, 12, 18);
        this.scanner = new Scanner(System.in);
        this.random = new Random();
        initializeStoryTemplates();
    }

    /**
     * constructor for mad libs game with custom parameters
     * 
     * @param id             unique identifier for this game instance
     * @param title          display name for the game
     * @param difficulty     difficulty level (1-10, affects story complexity)
     * @param requiredTokens cost in tokens to play
     * @param ticketReward   base reward in tickets for completion
     */
    public MadLibs(int id, String title, int difficulty, int requiredTokens, int ticketReward) {
        super(id, title, difficulty, requiredTokens, ticketReward);
        this.scanner = new Scanner(System.in);
        this.random = new Random();
        initializeStoryTemplates();
    }

    /**
     * main method for testing the mad libs game independently
     * creates a game instance and runs it with empty items list
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        MadLibs game = new MadLibs();
        ArrayList<Functional> useItems = new ArrayList<>();
        game.runGame(useItems);
    }

    /**
     * main game loop for mad libs
     * handles item processing, story selection, word collection, and story
     * presentation
     * 
     * @param useItems list of functional items player can use
     * @return number of tickets won based on completion
     */
    @Override
    public int runGame(ArrayList<Functional> useItems) {
        // clear screen for better presentation
        clearScreen();

        // process functional items
        processItems(useItems);

        // display game introduction
        displayGameIntro();

        // select appropriate story based on difficulty
        StoryTemplate selectedStory = selectStoryByDifficulty();

        // collect words from user
        Map<String, String> userWords = collectWordsFromUser(selectedStory);

        // generate and display the completed story
        String completedStory = generateCompletedStory(selectedStory, userWords);
        displayCompletedStory(completedStory);

        // calculate and return ticket reward
        return calculateFinalReward();
    }

    /**
     * initializes all available story templates with varying difficulty levels
     * stories are categorized by complexity and number of required words
     * includes bonus stories from StoryGenerator for more variety
     */
    private void initializeStoryTemplates() {
        storyTemplates = new ArrayList<>();

        // Easy stories (difficulty 1-3)
        storyTemplates.add(new StoryTemplate(
                "The Amazing Pet",
                1,
                Arrays.asList("adjective", "animal", "verb", "noun", "color"),
                "Once upon a time, there was a {adjective} {animal} named Fluffy. " +
                        "Every morning, Fluffy would {verb} around the {noun}. " +
                        "Fluffy's favorite color was {color}, and everyone loved this amazing pet!"));

        storyTemplates.add(new StoryTemplate(
                "School Day Adventure",
                2,
                Arrays.asList("adjective", "noun", "verb", "name", "food", "number"),
                "At school today, my {adjective} teacher gave us a {noun} to study. " +
                        "When we tried to {verb} it, my friend {name} accidentally spilled {food} everywhere! " +
                        "It took {number} minutes to clean up the mess, but we all laughed about it."));

        storyTemplates.add(new StoryTemplate(
                "The Magic Recipe",
                3,
                Arrays.asList("adjective", "ingredient", "verb", "time", "emotion", "place", "number"),
                "The {adjective} chef decided to make a special dish using {ingredient}. " +
                        "First, you must {verb} it for {time} while feeling {emotion}. " +
                        "Then, take it to the {place} and add {number} secret spices for the perfect meal!"));

        // Medium stories (difficulty 4-6)
        storyTemplates.add(new StoryTemplate(
                "The Superhero's Day Off",
                4,
                Arrays.asList("superhero name", "adjective", "power", "villain", "place", "adjective2", "object",
                        "verb", "emotion"),
                "Even superheroes need a break! {superhero name} was having a {adjective} day when suddenly their {power} started acting up. "
                        +
                        "The evil {villain} was causing trouble at the {place}, making everything {adjective2}. " +
                        "Our hero grabbed their trusty {object} and decided to {verb} into action, feeling {emotion} about saving the day once again!"));

        storyTemplates.add(new StoryTemplate(
                "The Time Machine Mishap",
                5,
                Arrays.asList("year", "adjective", "historical figure", "invention", "verb", "place", "object",
                        "adjective2", "number", "emotion"),
                "In the year {year}, a {adjective} scientist met {historical figure} who was working on a new {invention}. "
                        +
                        "They decided to {verb} together to the {place} using a mysterious {object}. " +
                        "The journey was {adjective2} and took {number} hours, leaving everyone feeling {emotion} about their adventure through time!"));

        // Hard stories (difficulty 7-10)
        storyTemplates.add(new StoryTemplate(
                "The Galactic Space Mission",
                7,
                Arrays.asList("planet name", "alien species", "adjective", "spaceship part", "verb",
                        "cosmic phenomenon",
                        "adjective2", "number", "time period", "emotion", "discovery", "verb2"),
                "Captain Space Explorer landed on planet {planet name} where the {alien species} lived in {adjective} harmony. "
                        +
                        "Their spaceship's {spaceship part} began to {verb} when they encountered a {cosmic phenomenon}. "
                        +
                        "The {adjective2} experience lasted {number} {time period}, making the crew feel {emotion}. " +
                        "They discovered {discovery} and decided to {verb2} back to Earth with their amazing findings!"));

        storyTemplates.add(new StoryTemplate(
                "The Mysterious Laboratory",
                8,
                Arrays.asList("scientist name", "adjective", "chemical", "reaction", "color", "verb", "measurement",
                        "time", "side effect", "adjective2", "location", "discovery", "emotion", "verb2"),
                "Dr. {scientist name} was conducting a {adjective} experiment with {chemical} when an unexpected {reaction} occurred. "
                        +
                        "The mixture turned {color} and began to {verb} for exactly {measurement} {time}. " +
                        "The strange {side effect} made everything in the lab become {adjective2}. " +
                        "This happened in the {location} where they made a groundbreaking {discovery}, leaving everyone feeling {emotion} and ready to {verb2}!"));

        storyTemplates.add(new StoryTemplate(
                "The Ultimate Quest",
                10,
                Arrays.asList("hero name", "magical creature", "adjective", "quest object", "dangerous place",
                        "obstacle",
                        "verb", "ally name", "special ability", "villain name", "weapon", "adjective2", "number",
                        "celebration", "emotion", "reward"),
                "The legendary hero {hero name} embarked on an epic journey with their companion, a {magical creature}. "
                        +
                        "Their {adjective} quest was to find the {quest object} hidden deep within the {dangerous place}. "
                        +
                        "They faced a terrible {obstacle} and had to {verb} with help from {ally name}, who possessed the power to {special ability}. "
                        +
                        "The evil {villain name} attacked them with a {weapon}, creating a {adjective2} battle that lasted {number} days. "
                        +
                        "After their victory, the kingdom held a grand {celebration}, everyone felt {emotion}, and our heroes were given the ultimate {reward}!"));

    }

    /**
     * processes functional items that affect gameplay
     * currently supports ticket multiplier items
     * 
     * @param useItems list of items to process
     */
    private void processItems(ArrayList<Functional> useItems) {
        for (Functional item : useItems) {
            if (item instanceof TicketMultiplier && item.getNumUses() > 0) {
                ticketMultiplier = TicketMultiplier.MULTIPLIER;
                item.setNumUses(item.getNumUses() - 1);
                System.out.println(
                        STYLE_INFO + "Ticket Multiplier activated! Your rewards will be multiplied!" + STYLE_END);
            } else if (item.getNumUses() > 0) {
                System.out.println(
                        STYLE_WARNING + "Sorry, " + item.getName() + " doesn't work with Mad Libs!" + STYLE_END);
            }
        }
    }

    /**
     * displays the game introduction and rules
     */
    private void displayGameIntro() {
        System.out.println(STYLE_TITLE + "╔══════════════════════════════════════════════╗");
        System.out.println("║                  MAD LIBS                   ║");
        System.out.println("╚══════════════════════════════════════════════╝" + STYLE_END);
        System.out.println();
        System.out.println(STYLE_INFO + "Welcome to Mad Libs!" + STYLE_END);
        System.out.println(STYLE_INFO + "I'll ask you for different types of words, and then" + STYLE_END);
        System.out.println(STYLE_INFO + "we'll use them to create a funny story together!" + STYLE_END);
        System.out.println();
        System.out.println(STYLE_WARNING + "Difficulty Level: " + this.getDifficulty() + STYLE_END);
        System.out.println(STYLE_INFO + "Higher difficulty = longer, more complex stories!" + STYLE_END);
        System.out.println();
        System.out.print(STYLE_HEADER + "Press Enter to start creating your story..." + STYLE_END);
        scanner.nextLine();
        System.out.println();
    }

    /**
     * selects a story template based on the current difficulty level
     * randomly chooses from stories within the appropriate difficulty range
     * 
     * @return selected story template
     */
    private StoryTemplate selectStoryByDifficulty() {
        List<StoryTemplate> suitableStories = new ArrayList<>();
        int difficulty = getDifficulty();

        // find stories that match the difficulty level
        for (StoryTemplate story : storyTemplates) {
            if (story.getDifficulty() <= difficulty) {
                suitableStories.add(story);
            }
        }

        // if no suitable stories found, use the easiest one
        if (suitableStories.isEmpty()) {
            suitableStories.add(storyTemplates.get(0));
        }

        // randomly select from suitable stories
        StoryTemplate selected = suitableStories.get(random.nextInt(suitableStories.size()));

        System.out.println(STYLE_HEADER + "🎭 Story Selected: \"" + selected.getTitle() + "\"" + STYLE_END);
        System.out
                .println(STYLE_INFO + "I need " + selected.getRequiredWords().size() + " words from you!" + STYLE_END);
        System.out.println();

        return selected;
    }

    /**
     * collects all required words from the user
     * provides prompts for each word type needed for the story
     * 
     * @param story the story template requiring words
     * @return map of word types to user-provided words
     */
    private Map<String, String> collectWordsFromUser(StoryTemplate story) {
        Map<String, String> userWords = new HashMap<>();

        System.out.println(STYLE_HEADER + "📝 Let's collect your words:" + STYLE_END);
        System.out.println();

        for (int i = 0; i < story.getRequiredWords().size(); i++) {
            String wordType = story.getRequiredWords().get(i);
            String prompt = createPromptForWordType(wordType);

            System.out.print(STYLE_INFO + "(" + (i + 1) + "/" + story.getRequiredWords().size() + ") " +
                    prompt + ": " + STYLE_END);
            String userInput = scanner.nextLine().trim();

            // ensure user provides input
            while (userInput.isEmpty()) {
                System.out.print(STYLE_WARNING + "Please enter a word: " + STYLE_END);
                userInput = scanner.nextLine().trim();
            }

            userWords.put(wordType, userInput);
        }

        System.out.println();
        System.out.println(STYLE_WIN_HEADER + "Great! Now let me create your story..." + STYLE_END);
        System.out.println();

        return userWords;
    }

    /**
     * creates user-friendly prompts for different word types
     * provides examples and clarifications for complex word types
     * 
     * @param wordType the type of word being requested
     * @return formatted prompt string
     */
    private String createPromptForWordType(String wordType) {
        switch (wordType.toLowerCase()) {
            case "adjective":
                return "Give me an adjective (describing word like 'funny', 'big', 'green')";
            case "noun":
                return "Give me a noun (person, place, or thing like 'cat', 'house', 'book')";
            case "verb":
                return "Give me a verb (action word like 'run', 'jump', 'sing')";
            case "verb2":
                return "Give me another verb (action word like 'dance', 'fly', 'cook')";
            case "animal":
                return "Give me an animal (like 'elephant', 'butterfly', 'shark')";
            case "color":
                return "Give me a color";
            case "name":
                return "Give me a person's name";
            case "food":
                return "Give me a type of food";
            case "number":
                return "Give me a number";
            case "place":
                return "Give me a place or location";
            case "emotion":
                return "Give me an emotion or feeling";
            case "adjective2":
                return "Give me another adjective (different describing word)";
            case "time":
                return "Give me a time period (like 'minutes', 'hours', 'days')";
            case "ingredient":
                return "Give me a cooking ingredient";
            case "superhero name":
                return "Give me a superhero name";
            case "power":
                return "Give me a superpower";
            case "villain":
                return "Give me a villain's name";
            case "object":
                return "Give me an object or tool";
            case "year":
                return "Give me a year";
            case "historical figure":
                return "Give me a famous person from history";
            case "invention":
                return "Give me an invention or device";
            case "planet name":
                return "Give me a name for a planet";
            case "alien species":
                return "Give me a type of alien creature";
            case "spaceship part":
                return "Give me a part of a spaceship";
            case "cosmic phenomenon":
                return "Give me a space phenomenon (like 'black hole', 'nebula', 'comet')";
            case "time period":
                return "Give me a time measurement (like 'seconds', 'minutes', 'years')";
            case "discovery":
                return "Give me something that could be discovered";
            case "hero name":
                return "Give me a hero's name";
            case "magical creature":
                return "Give me a magical creature";
            case "quest object":
                return "Give me a magical item or treasure";
            case "dangerous place":
                return "Give me a dangerous location";
            case "obstacle":
                return "Give me a challenge or obstacle";
            case "ally name":
                return "Give me a friend's name";
            case "special ability":
                return "Give me a special power or ability";
            case "villain name":
                return "Give me a villain's name";
            case "weapon":
                return "Give me a weapon or tool";
            case "celebration":
                return "Give me a type of celebration or party";
            case "reward":
                return "Give me a type of reward or prize";
            case "office item":
                return "Give me something you'd find in an office";
            case "coworker name":
                return "Give me a coworker's name";
            case "beverage":
                return "Give me a type of drink";
            case "explorer name":
                return "Give me an explorer's name";
            case "terrain":
                return "Give me a type of landscape (like 'desert', 'jungle', 'mountain')";
            case "tool":
                return "Give me a tool or instrument";
            case "creature":
                return "Give me a type of creature or animal";
            case "treasure":
                return "Give me a type of treasure";
            case "chef name":
                return "Give me a chef's name";
            case "dish":
                return "Give me a type of food or dish";
            case "kitchen tool":
                return "Give me a cooking utensil";
            case "taste":
                return "Give me a taste description (like 'sweet', 'sour', 'spicy')";
            case "reaction":
                return "Give me a reaction or response";
            case "scary adjective":
                return "Give me a scary describing word";
            case "monster":
                return "Give me a type of monster";
            case "haunted place":
                return "Give me a spooky location";
            case "brave person":
                return "Give me the name of a brave person";
            case "ending":
                return "Give me a type of ending (like 'happy', 'mysterious', 'surprising')";
            case "beach activity":
                return "Give me something you do at the beach";
            case "friend name":
                return "Give me a friend's name";
            case "sea creature":
                return "Give me a sea animal or creature";
            case "feeling":
                return "Give me a feeling or emotion";
            default:
                return "Give me a " + wordType;
        }
    }

    /**
     * generates the completed story by replacing placeholders with user words
     * 
     * @param story     the story template
     * @param userWords map of user-provided words
     * @return completed story string
     */
    private String generateCompletedStory(StoryTemplate story, Map<String, String> userWords) {
        String completedStory = story.getTemplate();

        // replace each placeholder with the corresponding user word
        for (Map.Entry<String, String> entry : userWords.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            completedStory = completedStory.replace(placeholder, entry.getValue());
        }

        return completedStory;
    }

    /**
     * displays the completed story with dramatic presentation
     * 
     * @param completedStory the final story to display
     */
    private void displayCompletedStory(String completedStory) {
        System.out.println(STYLE_TITLE + "🎉 YOUR MAD LIBS STORY IS READY! 🎉" + STYLE_END);
        System.out.println();
        System.out.println("═".repeat(60));
        System.out.println();

        // display story with word wrapping for better readability
        String[] sentences = completedStory.split("\\. ");
        for (String sentence : sentences) {
            if (!sentence.trim().isEmpty()) {
                System.out.println(STYLE_WIN_HEADER + sentence.trim() +
                        (sentence.endsWith(".") ? "" : ".") + STYLE_END);
                System.out.println();

                // add dramatic pause
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        System.out.println("═".repeat(60));
        System.out.println();
        System.out.println(STYLE_WIN_HEADER + "Hope you enjoyed your story! 📚✨" + STYLE_END);
        System.out.println();
    }

    /**
     * calculates the final ticket reward based on performance and items
     * 
     * @return number of tickets earned
     */
    private int calculateFinalReward() {
        // base reward for completing the story
        double performanceScore = 1.0; // full reward for completion
        int baseTickets = calculateTicketReward(performanceScore);
        int finalTickets = baseTickets * ticketMultiplier;

        System.out.println(
                STYLE_INFO + "🎫 You earned " + finalTickets + " tickets for creating an amazing story!" + STYLE_END);

        return finalTickets;
    }

    /**
     * clears the screen for better presentation
     */
    private void clearScreen() {
        String lines = System.getProperty("LINES");
        System.out.println("\n".repeat(lines != null ? Integer.parseInt(lines) : 25));
    }

    /**
     * inner class representing a story template
     * contains the story structure and required word types
     */
    private static class StoryTemplate {
        private String title;
        private int difficulty;
        private List<String> requiredWords;
        private String template;

        /**
         * constructor for story template
         * 
         * @param title         display title of the story
         * @param difficulty    difficulty level (1-10)
         * @param requiredWords list of word types needed
         * @param template      story template with placeholders
         */
        public StoryTemplate(String title, int difficulty, List<String> requiredWords, String template) {
            this.title = title;
            this.difficulty = difficulty;
            this.requiredWords = requiredWords;
            this.template = template;
        }

        public String getTitle() {
            return title;
        }

        public int getDifficulty() {
            return difficulty;
        }

        public List<String> getRequiredWords() {
            return requiredWords;
        }

        public String getTemplate() {
            return template;
        }
    }
}