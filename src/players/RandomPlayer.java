package players;

import java.util.ArrayList;
import java.util.Random;
import chess.Board;
import chess.Game;
import chess.Move;

public class RandomPlayer extends Player
{

    public RandomPlayer(int col, String n, Game g) {
        super(col, n, g);

    }

    @Override
    public void makeMove(Board b, ArrayList<Move> legalMoves) {

        Move randomMove = legalMoves.get(new Random().nextInt(legalMoves.size()));
        String origin = randomMove.originSquare().convertBoardToOutput();
        String target = randomMove.targetSquare().convertBoardToOutput();
        String victim;
        // System.out.println("NOW MOVING FOR REAL...........................");
        if (randomMove.taking()) {
            victim = " taking " + randomMove.targetPiece().name();
        } else {
            victim = "";
        }
        System.out.println(randomMove.agent().name() + " from " + origin + " to " + target + victim);
        b.applyMove(randomMove);

    }

}
