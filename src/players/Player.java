package players;

import java.util.List;
import chess.Board;
import chess.Move;

public abstract class Player
{
    public final int color;
    public final String name;

    public Player(int col, String n) {
        color = col;
        name = n;
    }

    /**
     * Wrap makeMove with this, to handle case where no legal moves exist.
     * In this case, the false parameter to getLegalMoves ensures that we
     * trigger the callback that ends the game.
     * 
     * @param b
     */
    public void makeMove(Board b) {
        List<Move> legalMoves = b.getLegalMoves(this, false);
        if (legalMoves.size() == 0) {
            return;
        }
        makeMove(b, legalMoves);
    }

    public abstract void makeMove(Board b, List<Move> legalMoves);

}
