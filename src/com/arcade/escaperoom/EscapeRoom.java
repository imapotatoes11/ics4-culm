//public class EscapeRoom extends Game {
//   public static final int STAGES = 4;
//   private int currentStage;
//   private char chosenOption;
//   private static final int QUESTION_INDEX = 0;
//   private static final int CORRECT_ANS_INDEX = 1;
//   private String[][] scenario = {{"question", "ans1", "ans2", "ans3"}};
//
//   private static String[] shuffleAnswers (String[] array) {
//      String[] copy = System.arraycopy(array);
//      Random random = new Random();
//      int r1, r2, r3, r4;
//      r1 = rand.nextInt(1, array.length);
//      r2 = rand.nextInt(1, array.length);
//      r3 = rand.nextInt(1, array.length);
//      r4 = rand.nextInt(1, array.length);
//
//      String tmp = copy[r1];
//      copy[r1] = copy[r2];
//      copy[r2] = tmp;
//
//      tmp = copy[r3];
//      copy[r3] = copy[r4];
//      copy[r4] = tmp;
//   }
//
//}