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

    public abstract void makeMove(Board b);

}
