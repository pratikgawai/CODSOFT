import java.util.Scanner ;
import java.util.Random ;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random r = new Random();
        int total_score = 0;


        outerLoop:
        while (true) {


            System.out.println("*-------WELCOME TO THE NUMBER GAME-----*");

            System.out.println("Enter 0 for Instructions ");
            System.out.println("Enter 1 for Easy Level (1-100)");
            System.out.println("Enter 2 for Medium Level (1-500)");
            System.out.println("Enter 3 for Hard Level  (1-1000)");
            System.out.println("Enter 4 for exit");
            System.out.println("Enter Your Choice Number : - ");

            int ch = sc.nextInt();
            int generateNum = 0;
            int maxAttempt = 10;

            switch (ch) {

                case 0:
                    System.out.println("* Computer will generate a secret number based on your difficulty level*");
                    System.out.println("You have to guess the number");
                    System.out.println(" After every guess : -");
                    System.out.println(" If your guess is too high, the program will say \"Too Large\"");
                    System.out.println("If your guess is too low, the program will say \"Too Small\"");
                    System.out.println("Keep guessing until you find the correct number.");
                    System.out.println("When you guess correctly, you win the game!");
                    System.out.println("Choose:\n" +
                            "   - Easy  : 1 to 100\n" +
                            "   - Medium: 1 to 500\n" +
                            "   - Hard  : 1 to 1000");

                    System.out.println("You Have 10 Attempts...");

                    System.out.println("GOOD LUCK & HAVE FUN");

                    System.out.println(" <----------- Play the game ----------->");
                    continue;

                case 1:
                    System.out.println(" Wow Nice Choice *---Easy Level---* You choose  ");
                    generateNum = r.nextInt(100) + 1;
                    System.out.println("Welcome To the Number game");
                    break;

                case 2:
                    System.out.println(" Wow Nice Choice *---Medium Level---* You choose  ");
                    generateNum = r.nextInt(500) + 1;
                    System.out.println("Welcome to the Number game");
                    break;

                case 3:

                    System.out.println(" Wow Nice Choice *---Hard Level---* You choose  ");
                    generateNum = r.nextInt(1000) + 1;
                    System.out.println("Welcome To the Number game");

                    break;


                case 4:
                    System.out.println("Thanks For Playing....! ");
                    break outerLoop;


                default:
                    System.out.println("Invalid Choice ! ");
                    continue ;
                    


            }

            // Now the guessing part is...
            int attempt = 0;
            boolean guessed = false;

            while (attempt < maxAttempt) {

                System.out.println("Guess the Number :- ");
                int guess = sc.nextInt();
                attempt++;

                if (guess < generateNum) {
                    System.out.println("The Number is too small ! ");
                } else if (guess > generateNum) {
                    System.out.println("The number is too large ! ");
                } else  {
                    System.out.println("Congratulation You got it ! ");
                    guessed = true ;
                    break;
                }
                }

                if (!guessed) {
                    System.out.println("Sorry You have Used your all  " + maxAttempt + " attempts");

                }

                int roundScore = guessed ? (maxAttempt - attempt + 1) : 0;
                total_score += roundScore ;
                System.out.println("Your Score For this round is : " + roundScore);
                System.out.println("Total Score is : " + total_score);

                System.out.println("Do You want to play another round : (y/n) ");
                String playAgain = sc.next();

                if (!playAgain.equalsIgnoreCase("y")) {

                    System.out.println("Thanks For Playing ! Your Final Score  : " + total_score);
                    break;
                }






        }
        sc.close();

    }

}
