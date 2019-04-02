package chess;

import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import players.HumanPlayer;
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

    public Game(int isHumanPlayer) {
        board = new Board(this);
        if (isHumanPlayer == 1) {
            white = new HumanPlayer(1, "White", board);
            black = new RandomPlayer(0, "Black", board);
        } else if (isHumanPlayer == 2) {
            white = new RandomPlayer(1, "White", board);
            black = new HumanPlayer(0, "Black", board);
        } else {
            white = new RandomPlayer(1, "White", board);
            black = new RandomPlayer(0, "Black", board);
        }

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
        System.out.println(board.toString());
    }

    public Piece callbackPromotion(Piece promotee, Square targetSquare) {
        promotee.alive = false;
        promotee.player.pieces.remove(promotee);
        Piece queen = new Queen(promotee.player, targetSquare);
        return queen;
    }

    public Piece callbackDemotion(Piece demotee, Square targetSquare) {
        demotee.alive = false;
        demotee.player.pieces.remove(demotee);
        Piece pawn = new Pawn(demotee.player, targetSquare);
        return pawn;
    }

}
