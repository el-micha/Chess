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

    public final int x;
    public final int y;
    public final String c;
    private Piece visitor = null;
    private Board board;

    public Square(int ix, int iy, Board b) {
        x = ix;
        y = iy;
        board = b;
        if (color() == 1) {
            // white
            c = ".";
        } else {
            // black
            c = "=";
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((visitor == null) ? 0 : visitor.hashCode());
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Square other = (Square)obj;
        if (visitor == null) {
            if (other.visitor != null)
                return false;
        } else if (!visitor.equals(other.visitor))
            return false;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }

    public Piece getVisitor() {
        return visitor;
    }

    public void setVisitor(Piece p) {
        if (visitor != null && p != null) {
            System.out.println("Error in Square::setVisitor: Square already has visitor.");
        }
        visitor = p;
    }

    public String ch() {
        if (visitor == null) {
            return c;
        }
        return visitor.ch();
    }

    public int color() {
        if (x % 2 == 0) {
            return y % 2;
        }
        return (y + 1) % 2;

    }

    public String convertBoardToOutput() {
        String stringCoordinate;
        int row = 8 - x;
        int colInt = 'a' + y;
        char col = (char)colInt;
        stringCoordinate = Character.toString(col) + Integer.toString(row);
        return stringCoordinate;
    }
}
