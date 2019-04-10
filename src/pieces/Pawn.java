package pieces;

import java.util.ArrayList;
import chess.Board;
import chess.Move;
import chess.Square;

public class Pawn extends Piece
{

    private int moveDirection;

    public Pawn(int col, Square pos) {
        super(col, pos);
        name = "Pawn";
        c = "p";
        value = 1;
        // white pawns: -8, black pawns: +8
        if (color == 0) {
            moveDirection = 1;
        } else {
            moveDirection = -1;
        }
    }

    public int getMoveDirection() {
        return moveDirection;
    }

    @Override
    public ArrayList<Move> legalMoves(Board b) {
        /*
         * Deal with the pawn's special moves here.
         * 1. long start
         * 2. take piece diagonally
         * 3. can't move ahead if piece there: no orthogonal taking
         * 4. taking "en passant" TODO
         * white pawns move "up", so in -1 steps;
         * black pawns move "down", in +1 steps
         */
        ArrayList<Move> moves = new ArrayList<Move>();

        // first move: double
        if (!hasMoved) {
            Square newPos = b.translate(pos, new int[]{2 * moveDirection, 0});
            Square inbetween = b.translate(pos, new int[]{1 * moveDirection, 0});

            if (newPos != null && b.pieceAt(inbetween) == null && b.pieceAt(newPos) == null
                    && !moveEndangersKing(b, this, newPos))
            {
                moves.add(new Move(b, this, newPos));
            }
        }
        // normal single straight step
        Square newPos = b.translate(pos, new int[]{1 * moveDirection, 0});
        if (newPos != null && b.pieceAt(newPos) == null && !moveEndangersKing(b, this, newPos)) {
            moves.add(new Move(b, this, newPos));
        }

        // careful not to take over borders: if currpos%8 is 0 or 7, don't check one side
        // taking left diagonal
        if (!(pos.x == 0)) {
            newPos = b.translate(pos, new int[]{1 * moveDirection, -1});
            if (newPos != null && occupiedByEnemy(newPos) && !moveEndangersKing(b, this, newPos)) {
                moves.add(new Move(b, this, newPos));
            }
        }
        // taking right diagonal
        if (!(pos.x == 7)) {
            newPos = b.translate(pos, new int[]{1 * moveDirection, 1});
            if (newPos != null && occupiedByEnemy(newPos) && !moveEndangersKing(b, this, newPos)) {
                moves.add(new Move(b, this, newPos));
            }
        }

        // taking en passant?
        // last move was a double move of a pawn that is now next to this one
        Move lastMove = b.getLastMove();
        if (lastMove != null && lastMove.isPawnDouble()) {
            // is that pawn a neighbour now?
            if (Math.abs(lastMove.targetSquare().y - pos.y) == 1 && lastMove.targetSquare().x == pos.x) {
                newPos = b.translate(pos, new int[]{moveDirection, lastMove.targetSquare().y - pos.y});
                moves.add(new Move(b, this, newPos));
            }
        }

        // System.out.println("Pawn has # moves " + moves.size());
        return moves;
    }

}
