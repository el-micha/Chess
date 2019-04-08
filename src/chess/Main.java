
package chess;

import java.util.Scanner;

public class Main
{
    static Scanner in = new Scanner(System.in);
    static String userInput = "y";
    // isHumanPlayer: 0 = none, 1 = white, 2 = black
    static int isHumanPlayer = 0;

    public static void main(String[] args) {

        System.out.println("Hello");
        System.out.println("Would you like to play yourself? y/n");
        choosePlayer();
        Game game = new Game(isHumanPlayer);

        for (int i = 0; i < 1000; i++) {
            game.play(1000);
            // game.nextHalfturn();
        }

    }

    public static void choosePlayer() {
        userInput = in.nextLine();
        System.out.println("You entered " + userInput);
        try {
            if (userInput.equals("y")) {
                System.out.println("User plays against computer. Would you like to play white or black? w/b");
                String chooseColor = in.nextLine();
                if (chooseColor.equals("w")) {
                    isHumanPlayer = 1;
                    System.out.println("White chosen!");
                } else if (chooseColor.equals("b")) {
                    isHumanPlayer = 2;
                    System.out.println("Black chosen!");
                } else {
                    throw new Exception();
                }

            } else if (userInput.equals("n")) {
                System.out.println("computer plays against computer");
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.println("Congratulations, you played yourself. Input invalid! Please enter y/n");
            choosePlayer();
        }
    }

}
