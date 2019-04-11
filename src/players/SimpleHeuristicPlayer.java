package players;

import java.util.ArrayList;
import chess.Board;
import chess.Game;
import chess.Move;

public class SimpleHeuristicPlayer extends Player
{

    public SimpleHeuristicPlayer(int col, String n, Game g) {
        super(col, n, g);
    }

    @Override
    public void makeMove(Board b, ArrayList<Move> legalMoves) {

        Move bestMove = null;
        int bestValue = -999;
        for (Move move : legalMoves) {
            b.applyMove(move);
            int value = b.valuation(this) - b.valuation(b.game.otherPlayer(this));
            if (value > bestValue) {
                bestMove = move;
                bestValue = value;
            }
            b.undoMove(move);
        }

        String origin = bestMove.originSquare().convertBoardToOutput();
        String target = bestMove.targetSquare().convertBoardToOutput();
        String victim;
        // System.out.println("NOW MOVING FOR REAL...........................");
        if (bestMove.taking()) {
            victim = " taking " + bestMove.targetPiece().name();
        } else {
            victim = "";
        }
        System.out.println(bestMove.agent().name() + " from " + origin + " to " + target + victim);
        b.applyMove(bestMove);

    }

}
