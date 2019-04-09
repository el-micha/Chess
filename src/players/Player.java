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

    public Player(int col, String n) {
        color = col;
        name = n;
    }

//    public boolean isInCheck(Board b) {
//        return b.isInCheck(color);
//    }
//
//    public ArrayList<Move> legalMoves(Board b) {
//        return legalMoves(b, true);
//    }
//    
//    public ArrayList<Piece> getPieces(Board b)
//    {
//    	return b.getPieces(color);
//    }
//    
    
    /**
     * if simulation == true, we ignore this because it occurred while thinking about future moves
     * otherwise, the player has no moves left in their current situation and lose / tie
     * 
     */
    //TODO: remove the callback and simulation; solve this better
    public ArrayList<Move> legalMoves(Board b, boolean simulation) {
    	ArrayList<Piece> pieces = b.getPieces(color);
        ArrayList<Move> moves = new ArrayList<Move>();
        for (int i = 0; i < pieces.size(); i++) {
            Piece piece = pieces.get(i);
            if (!piece.isAlive()) {
                continue;
            }
            moves.addAll(piece.legalMoves(b));
        }
        if (moves.size() == 0) {
            b.game.callbackOutOfLegalMoves(this, simulation);
        }
        return moves;
    }

    public abstract void makeMove(Board b);

}
