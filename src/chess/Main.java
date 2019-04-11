package chess;

public class Main
{
    public static void main(String[] args) {

        System.out.println("Hello");
        Game game = new Game();

        // game.play();

        for (int i = 0; i < 10; i++) {
            game.play();
            game = new Game();
            System.out.println(i);
        }

    }
}

// loc: \n[\s]*
