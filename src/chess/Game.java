package chess;

import pieces.Piece;
import pieces.Queen;
import players.Player;
import players.RandomPlayer;

public class Game
{
    /*
     * A game consists of a board and two players
     */

    Board board;
    Player white;
    Player black;

    int turn; // white's turns are even, black's are odd

    public Game() {
        board = new Board(this);
        white = new RandomPlayer(1, "White", board);
        black = new RandomPlayer(0, "Black", board);

        // set pieces on board
        board.setupInitial(white.pieces);
        board.setupInitial(black.pieces);

        turn = 0;

        System.out.println("Created Game");
        System.out.println(board.toString());
    }

    public void nextHalfturn() {
        if (turn % 2 == 0) {
            System.out.println("White moves:");
            white.makeMove(board);

        }
        if (turn % 2 == 1) {
            System.out.println("Black moves:");
            black.makeMove(board);

        }
        turn++;
        System.out.println("######################################################");
        System.out.println(board.toString());
    }

    public Piece callbackPromotion(Piece promotee, Square targetSquare) {
        promotee.setDead();
        Piece queen = new Queen(promotee.player, targetSquare);
        return queen;
    }

}
