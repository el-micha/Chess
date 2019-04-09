package players;

import java.util.ArrayList;
import chess.Board;
import chess.Move;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

public abstract class Player
{
    public final int color;
    public final String name;
    public Board board;
    public ArrayList<Piece> pieces;
    public King king;

    public Player(int col, String n, Board b) {
        color = col;
        name = n;
        board = b;
        pieces = new ArrayList<Piece>();

        // white is at 56-63, black at 0-7, start difference is 56
        // white pawns are at 48-55, black at 8-15, difference is 40
        int rank;
        int pawnRank;
        if (color == 1) {
            rank = 7;
            pawnRank = 6;
        } else {
            rank = 0;
            pawnRank = 1;
        }

        pieces.add(new Rook(this, b.getSquare(rank, 0)));
        pieces.add(new Knight(this, b.getSquare(rank, 1)));
        pieces.add(new Bishop(this, b.getSquare(rank, 2)));
        pieces.add(new Queen(this, b.getSquare(rank, 3)));
        king = new King(this, b.getSquare(rank, 4));
        pieces.add(king);
        pieces.add(new Bishop(this, b.getSquare(rank, 5)));
        pieces.add(new Knight(this, b.getSquare(rank, 6)));
        pieces.add(new Rook(this, b.getSquare(rank, 7)));

        for (int i = 0; i < 8; i++) {
            pieces.add(new Pawn(this, b.getSquare(pawnRank, i)));
        }

    }

    public boolean isInCheck(Board b) {
        return king.isInCheck(b);
    }

    public ArrayList<Move> legalMoves(Board b) {
        return legalMoves(b, true);
    }

    /**
     * if simulation == true, we ignore this because it occurred while thinking about future moves
     * otherwise, the player has no moves left in their current situation and lose / tie
     * 
     */
    public ArrayList<Move> legalMoves(Board b, boolean simulation) {
        ArrayList<Move> moves = new ArrayList<Move>();
        for (int i = 0; i < pieces.size(); i++) {
            Piece piece = pieces.get(i);
            if (!piece.isAlive()) {
                continue;
            }
            moves.addAll(piece.legalMoves(b));
        }
        if (moves.size() == 0) {
            board.game.callbackOutOfLegalMoves(this, simulation);
        }
        return moves;
    }

    public abstract void makeMove(Board b);

    public void callbackDead(Piece p) {
        pieces.remove(p);
    }

    public void callbackAlive(Piece p) {
        pieces.add(p);
    }
}
