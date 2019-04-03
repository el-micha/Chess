package players;

import java.util.ArrayList;
import java.util.Random;
import chess.Board;
import chess.Move;

public class RandomPlayer extends Player
{

    public RandomPlayer(int col, String n, Board b) {
        super(col, n, b);

    }

    @Override
    public void makeMove(Board b) {
        ArrayList<Move> legalMoves = legalMoves(b, false);

        if (legalMoves.size() == 0) {
            // System.out.println("Player " + name + " has no more legal moves and loses.");
            return;
        }

        /*
         * System.out.println("Player " + name + " legal moves: ");
         * for (Move m : legalMoves) {
         * System.out.println(m.originSquare().index + " to " + m.targetSquare().index);
         * }
         */
        Move randomMove = legalMoves.get(new Random().nextInt(legalMoves.size()));
        String origin = randomMove.originSquare().algPos();
        String target = randomMove.targetSquare().algPos();
        String victim;
        if (randomMove.taking()) {
            victim = " taking " + randomMove.targetPiece().name();
        } else {
            victim = "";
        }
        System.out.println(randomMove.agent().name() + " from " + origin + " to " + target + victim);
        b.applyMove(randomMove);

    }

}
