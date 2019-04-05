package chess;

import pieces.Piece;
import pieces.Queen;
import players.Player;
import players.RandomPlayer;
import players.SimpleHeuristicPlayer;

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
        white = new RandomPlayer(1, "White", board);
        black = new SimpleHeuristicPlayer(0, "Black", board);

        // set pieces on board
        board.setupInitial(white.pieces);
        board.setupInitial(black.pieces);

        turn = 0;

        System.out.println("Created Game");
        System.out.println(board.toString());
    }

    public void play(int maxMoves) {
        for (int i = 0; i < maxMoves; i++) {
            System.out.println("++++++++++++++++++++++++++++++ Halfturn " + i + " ++++++++++++++++++++++++++++++");
            nextHalfturn();
            // check if board is won, stale etc
            if (gameFinished) {
                break;
            }
        }
        if (losingCandidate == null)
        {
        	System.out.println("***** Game hath run out of moves before checkmate or stalemate. *****");
        	return;
        }
        if (losingCandidate.king.isInCheck(board)) {
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
    	if (white.isInCheck(board))
    	{
    		System.out.println("White is in check.");
    	}
    	if (black.isInCheck(board))
    	{
    		System.out.println("Black is in check.");
    	}
    	
        if (turn % 2 == 0) {
            System.out.println("White doth move:");
            white.makeMove(board);
        }
        if (turn % 2 == 1) {
            System.out.println("Black doth move:");
            black.makeMove(board);

        }
        if (turn > 1 && board.getLastMove().isPromotion()) {
            System.out.println("Promotion!");
        }
        if (turn > 1 && board.getLastMove().isCastling()) {
            System.out.println("Castling!");
        }
        turn++;
        System.out.println(board.toString());

    }

    public Player otherPlayer(Player p) {
        if (p == white)
            return black;
        return white;
    }

    public Piece callbackPromotion(Piece promotee, Square targetSquare) {
        promotee.setDead();
        Piece queen = new Queen(promotee.player, targetSquare);
        return queen;
    }

    /**
     * if simulation == true, we ignore this because it occurred while thinking about future moves
     * otherwise, the player has no moves left in their current situation and lose / tie
     * 
     * @param p
     * @param simulation
     */
    public void callbackOutOfLegalMoves(Player p, boolean simulation) {
        if (!simulation) {
            System.out.println("Player " + p.name + ", that fool, hath no more legal moves.");
            gameFinished = true;
            losingCandidate = p;
        }
    }
}
