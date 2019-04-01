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
    final String name;
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
            rank = 56;
            pawnRank = 48;
        } else {
            rank = 0;
            pawnRank = 8;
        }

        pieces.add(new Rook(this, b.squares[rank + 0]));
        pieces.add(new Knight(this, b.squares[rank + 1]));
        pieces.add(new Bishop(this, b.squares[rank + 2]));
        pieces.add(new Queen(this, b.squares[rank + 3]));
        king = new King(this, b.squares[rank + 4]);
        pieces.add(king);
        pieces.add(new Bishop(this, b.squares[rank + 5]));
        pieces.add(new Knight(this, b.squares[rank + 6]));
        pieces.add(new Rook(this, b.squares[rank + 7]));

        for (int i = 0; i < 8; i++) {
            pieces.add(new Pawn(this, b.squares[pawnRank + i]));
        }

    }

    public ArrayList<Move> legalMoves(Board b) {
        ArrayList<Move> moves = new ArrayList<Move>();
        for (Piece piece : pieces) {
            if (!piece.alive) {
                continue;
            }
            moves.addAll(piece.legalMoves(b));
        }
        return moves;
    }

    public abstract void makeMove(Board b);

}
