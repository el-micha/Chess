package chess;

import pieces.Piece;

public class Square
{
    /*
     * A single square of the board
     * Is either black or white
     * Is either empty or held/visited by a single piece
     * Can be "in check" or "threatened": Important for king's legal moves
     * Black = odd
     * White = even
     */

    public final int index;
    public final String c;
    private Piece visitor = null;
    private Board board;

    public Square(int i, Board b) {
        index = i;
        board = b;
        if (color() == 1) {
            // white
            c = ".";
        } else {
            // black
            c = "=";
        }
    }

    public Piece getVisitor() {
        return visitor;
    }

    public void setVisitor(Piece p) {
        visitor = p;
    }

    public String ch() {
        if (visitor == null) {
            return c;
        }
        return visitor.ch();
    }

    public int color() {
        // even ranks: (index+1)%2
        int rank = (int)(index / 8);
        if (rank % 2 == 0) {
            return (index + 1) % 2;
        }
        return (index) % 2;

    }

}
