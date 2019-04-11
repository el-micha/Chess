package players;

import java.util.ArrayList;
import chess.Board;
import chess.Game;
import chess.Move;

public abstract class Player
{
    public final int color;
    public final String name;
    public final Game game;

    public Player(int col, String n, Game g) {
        color = col;
        name = n;
        game = g;
    }

    /**
     * Wrap makeMove with this, to handle case where no legal moves exist.
     * In this case, the false parameter to getLegalMoves ensures that we
     * trigger the callback that ends the game.
     * 
     * 
     * @param b
     */
    public void makeMove(Board b) {
        ArrayList<Move> legalMoves = b.getLegalMoves(this);
        if (legalMoves.size() == 0) {
            game.callbackOutOfLegalMoves(color);
        }
        makeMove(b, legalMoves);
    }

    public abstract void makeMove(Board b, ArrayList<Move> legalMoves);

}
