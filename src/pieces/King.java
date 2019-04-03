package pieces;

import java.util.ArrayList;
import chess.Board;
import chess.Move;
import chess.Square;
import players.Player;

public class King extends Piece
{
    public static int[][] kingTranslations = {{-1, -1}, {-1, 1}, {-1, 0}, {0, -1}, {0, 1}, {1, -1}, {1, 1}, {1, 0}};
    // TODO: these translations are wrong, because they allow King to move across border. 8 - 1 = 7

    public King(Player p, Square position) {
        super(p, position);
        name = "King";
        c = "k";
    }

    @Override
    public ArrayList<Move> legalMoves(Board b) {
        ArrayList<Move> moves = new ArrayList<Move>();
        for (int i = 0; i < 8; i++) {
            int[] move = kingTranslations[i];
            Square newPos = b.translate(pos, move);

            if (newPos != null && !occupiedByFriend(newPos) && !moveEndangersKing(b, this, newPos)) {
                moves.add(new Move(b, this, newPos));
            }
        }

        moves.addAll(castlingMoves(b));

        return moves;
    }

    private ArrayList<Move> castlingMoves(Board b) {
        ArrayList<Move> moves = new ArrayList<Move>();
        if (hasMoved) {
            return moves;
        }

        // rooks are at +3 or -4
        Piece r1 = b.getSquare(pos.x, pos.y - 4).getVisitor();
        Piece r2 = b.getSquare(pos.x, pos.y + 3).getVisitor();
        if (r1 != null && !r1.hasMoved) {
            Move m = new Move(b, this, b.getSquare(pos.x, pos.y - 2));
            moves.add(m);
        }
        if (r2 != null && !r2.hasMoved) {
            Move m = new Move(b, this, b.getSquare(pos.x, pos.y + 2));
            moves.add(m);
        }
        return moves;
    }

    public boolean isInCheck(Board b) {
        boolean check = (threatByAnyRay(b) || threatByKnight(b) || threatByPawn(b) || threatByKing(b));
        if (check)
        {
        	System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
        	System.out.println("Check: " + this.color);
        	System.out.println(b.toString());
        	System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        }
        return check;
    }

    private boolean threatByKing(Board b) {
        return (enemyKingHere(b, new int[]{0, -1}) || enemyKingHere(b, new int[]{0, 1}) || enemyKingHere(b, new int[]{-1, 0})
                || enemyKingHere(b, new int[]{1, 0})
                || enemyKingHere(b, new int[]{-1, 1}) || enemyKingHere(b, new int[]{1, -1})
                || enemyKingHere(b, new int[]{-1, -1}) || enemyKingHere(b, new int[]{1, 1}));
    }

    private boolean enemyKingHere(Board b, int[] trans) {
        Square square = b.translate(pos, trans);
        if (enemyPieceAtPos(square, "King")) {
            return true;
        }
        return false;
    }

    private boolean enemyPieceAtPos(Square square, String piece) {
        if (square != null && square.getVisitor() != null && square.getVisitor().name.equals(piece)
                && square.getVisitor().color != color)
        {
            return true;
        }
        return false;
    }

    private boolean threatByPawn(Board b) {
        // moving direction for white pawns: -1; black pawns: +1
        // dangerous direction for white king: -1, black king: +1
        int dir = 1;
        if (color == 0) {
            dir = -1;
        }
        // only check if not at border
        if (!(pos.y == 0)) {
            Square square1 = b.translate(pos, new int[]{dir, -1});
            if (enemyPieceAtPos(square1, "Pawn")) {
                return true;
            }

        }
        if (!(pos.y == 7)) {
            Square square2 = b.translate(pos, new int[]{dir, 1});
            if (enemyPieceAtPos(square2, "Pawn")) {
                return true;
            }

        }
        return false;
    }

    private boolean threatByKnight(Board b) {
        for (int i = 0; i < 8; i++) {
            int move[] = Knight.knightTranslations[i];
            Square square = b.translate(pos, move);
            if (enemyPieceAtPos(square, "Knight")) {
                return true;
            }
        }
        return false;
    }

    private boolean threatByAnyRay(Board b) {
        return (threatByRay(b, true, 1, 0) || threatByRay(b, true, -1, 0) || threatByRay(b, true, 0, 1)
                || threatByRay(b, true, 0, 1) || threatByRay(b, false, 1, 1) || threatByRay(b, false, -1, -1)
                || threatByRay(b, false, 1, -1) || threatByRay(b, false, -1, 1));
    }

    private boolean threatByRay(Board b, boolean orth, int dx, int dy) {

        for (int i = 0; i < 7; i++) {
            int[] trans = {i * dx, i * dy};
            Square target = b.translate(pos, trans);
            if (target == null) // out of bounds; we can stop here
            {
                return false;
            }
            if (target.getVisitor() != null) {
                if (target.getVisitor().color != color && target.getVisitor().name.equals("Queen")) {
                    return true;
                } else if (target.getVisitor().color != color && orth && target.getVisitor().name.equals("Rook")) {
                    return true;
                } else if (target.getVisitor().color != color && !orth && target.getVisitor().name.equals("Bishop")) {
                    return true;
                } else // there is a non-ray piece in the way; we can stop here
                {
                    return false;
                }
            }
        }
        return false;
    }

}
