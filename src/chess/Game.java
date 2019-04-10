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

    private boolean gameFinished = false;
    private Player losingCandidate = null;

    int turn; // white's turns are even, black's are odd

    public Game() {
        board = new Board(this);
        white = new RandomPlayer(1, "White");
        black = new RandomPlayer(0, "Black");
        // black = new SimpleHeuristicPlayer(0, "Black");

        turn = 0;

        System.out.println("Created Game");
        System.out.println(board.toString());
    }

    public void play(int maxMoves) {
        for (int i = 0; i < maxMoves; i++) {
            System.out.println("++++++++++++++++++++++++++++++ Halfturn " + i + " ++++++++++++++++++++++++++++++");
            nextHalfturn();

            if (board.isRemis()) {
                System.out.println("Game ends as remis.");
                break;
            }

            if (board.isInCheck(white)) {
                System.out.println("White is in check.");
            }
            if (board.isInCheck(black)) {
                System.out.println("Black is in check.");
            }

            if (turn > 1 && board.getLastMove().isPromotion()) {
                System.out.println("Promotion!");
            }
            if (turn > 1 && board.getLastMove().isCastling()) {
                System.out.println("Castling!");
            }
            turn++;
            System.out.println(board.toString());

            // check if board is won, stale etc
            if (gameFinished) {
                break;
            }
        }
        if (losingCandidate == null) {
            System.out.println("***** Game hath run out of moves before checkmate or stalemate. *****");
            return;
        }
        if (board.isInCheck(losingCandidate.color)) {
            System.out.println("Player " + losingCandidate.name
                    + ", that utter wretch, is also in check, wherefore his King is dead and he hath lost.");
            System.out.println("Player " + otherPlayer(losingCandidate).name + " wins the game.");
        } else {
            System.out.println("Player " + otherPlayer(losingCandidate).name
                    + ", the even greater fool, did not manage to check player "
                    + losingCandidate.name + ", forcing a stalemate.");
            System.out.println("Game is a stalemate!");
        }
        System.out.println("Game hath ended.");
    }

    private void nextHalfturn() {

        if (turn % 2 == 0) {
            System.out.println("White doth move:");
            white.makeMove(board);
        }
        if (turn % 2 == 1) {
            System.out.println("Black doth move:");
            black.makeMove(board);
        }

    }

    public Player otherPlayer(Player p) {
        if (p == white)
            return black;
        return white;
    }

    public Piece callbackPromotion(Piece promotee, Square targetSquare) {
        promotee.setDead();
        Piece queen = new Queen(promotee.color, targetSquare);
        return queen;
    }

    /**
     * if simulation == true, we ignore this because it occurred while thinking about future moves
     * otherwise, the player has no moves left in their current situation and lose / tie
     * 
     * @param p
     * @param simulation
     */
    public void callbackOutOfLegalMoves(int color) {
        losingCandidate = playerFromColor(color);
        System.out.println("Player " + losingCandidate.name + ", that fool, hath no more legal moves.");
        gameFinished = true;

    }

    private Player playerFromColor(int color) {
        if (color == 1) {
            return white;
        }
        return black;
    }

}
