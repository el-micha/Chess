package players;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import chess.Board;
import chess.Move;
import chess.Square;

public class HumanPlayer extends Player
{
    static Scanner in = new Scanner(System.in);
    static String userInput = "";
    static int isHumanPlayer = 0;

    public HumanPlayer(int col, String n, Board b) {
        super(col, n, b);

    }

    @Override
    public void makeMove(Board b) {
        ArrayList<Move> legalMoves = legalMoves(b);
        if (legalMoves.size() == 0) {
            System.out.println("Player " + name + " has no more legal moves and loses.");
            return;
        }

        userInput = in.nextLine();
        System.out.println("You entered " + userInput);
        Square humanMoveInput = b.convertInputToBoard(userInput);
        Move humanMove = legalMoves.get(new Random().nextInt(legalMoves.size()));
        // TODO:
        // Move humanMove = convertToMoveObject
        //
        // if (human is legal) {
        //
        // }

        String origin = humanMove.originSquare().convertBoardToOutput();
        String target = humanMove.targetSquare().convertBoardToOutput();
        String victim;
        if (humanMove.taking()) {
            victim = " taking " + humanMove.targetPiece().name();
        } else {
            victim = "";
        }
        System.out.println(humanMove.agent().name() + " from " + origin + " to " + target + victim);
        b.applyMove(humanMove);
    }

    public int getIsHumanPlayer() {
        return isHumanPlayer;
    }

}
