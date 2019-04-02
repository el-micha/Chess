package chess;

import java.util.ArrayList;
import pieces.Piece;

public class Board
{
    /*
     * A board has a state and pieces
     * Can return possible moves for a player
     * Can make a move and undo it
     * Has a win condition and can return the win state
     */

    public Game game;
    public Square[] squares;
    public ArrayList<Move> moveHistory;
    public ArrayList<Piece> takenPieces;

    public Board(Game g) {
        game = g;
        squares = new Square[64];
        // Black = 0, White = 1; Top left square is white, so 1.
        for (int i = 0; i < squares.length; i++) {
            squares[i] = new Square(i);
        }
        moveHistory = new ArrayList<Move>();
        takenPieces = new ArrayList<Piece>();

        System.out.println("Created Board ");
    }

    public void setupInitial(ArrayList<Piece> pieces) {
        for (Piece piece : pieces) {
            piece.pos.visitor = piece;
        }
    }

    public String toString() {

        String xPointers = "";
        String[] yPointers = {" ", " ", " ", " ", " ", " ", " ", " "};
        if (moveHistory.size() > 0) {
            Move lastMove = moveHistory.get(moveHistory.size() - 1);
            int originIndex = lastMove.originSquare.index;
            int targetIndex = lastMove.targetSquare.index;
            int ox = originIndex % 8;
            int oy = originIndex / 8;
            int tx = targetIndex % 8;
            int ty = targetIndex / 8;

            for (int i = 0; i < 8; i++) {
                if (i == ox) {
                    xPointers += "^ ";
                } else if (i == tx) {
                    xPointers += "î ";
                } else {
                    xPointers += "  ";
                }
            }
            yPointers[oy] = "<-";
            yPointers[ty] = "<--";

        }

        String s = "";
        s += " |a b c d e f g h";
        s += "\n-+---------------\n";
        int cnt = 0;
        for (Square square : squares) {
            if (cnt % 8 == 0) {
                if (cnt > 0) {
                    s += yPointers[(int)(cnt / 8) - 1];
                    s += "\n";
                }

                s += (8 - (int)(cnt / 8)) + "|";
            }

            s += square.ch() + " ";

            cnt++;
        }

        s += "\n  " + xPointers;

        return s;
    }

    public void applyMove(Move move) {
        /*
         * In order to undo a move, we need:
         * 1) The original pos of the moving piece(s)
         * 2) The target pos and what Piece there was before the move
         * 3) Special moves like Promotion and Castling need extra work
         * 4) if the moving piece has not moved before, its .hasMoved needs to be reset
         */

        if (move.taking) {
            move.targetPiece.alive = false;
            takenPieces.add(move.targetPiece);
        }
        move.originSquare.visitor = null;
        move.targetSquare.visitor = move.agent;
        move.agent.pos = move.targetSquare;

        if (move.isPromotion) {
            System.out.println("Promotion");
            System.out.println("Promoting from " + move.originSquare.index + " to " + move.targetSquare.index);
            System.out.println("Promoting from " + move.originSquare.visitor + " to " + move.targetSquare.visitor);

            // callback piece to delete, square of new queen
            Piece newQueen = game.callbackPromotion(move.agent, move.targetSquare);
            move.targetSquare.visitor = newQueen;
        }
        if (move.isCastling) {
            move.castlingPartnerOrigin.visitor = null;
            move.castlingPartnerTarget.visitor = move.castlingPartner;
            move.castlingPartner.pos = move.castlingPartnerTarget;
        }
        move.agent.hasMovedOld = move.agent.hasMoved;
        move.agent.hasMoved = true;
        moveHistory.add(move);
    }

    public void undoMove(Move move) {
        // put agent back
        // revive taken piece
        // if promotion: unpromote
        // if castling: put back rook, too
        // System.out.println(this.toString());

        move.agent.pos = move.originSquare;
        move.originSquare.visitor = move.agent;
        // THE FOLLOWING LINE RUINS UNPROMOTION
        if (!move.isPromotion)
            move.targetSquare.visitor = null; // or a taken piece

        if (move.isPromotion) {
            // on the targetSquare, there now is a queen. delete her and put the pawn back at
            // originSquare
            System.out.println("Unpromotion");
            System.out.println("Unpromoting from " + move.originSquare.index + " to " + move.targetSquare.index);
            System.out.println("Unpromoting from " + move.originSquare.visitor + " to " + move.targetSquare.visitor);

            Piece demotee = move.targetSquare.visitor;
            if (demotee == null) {
                System.out.println("darn");
            }
            Piece oldPawn = game.callbackDemotion(demotee, move.originSquare);
            move.originSquare.visitor = oldPawn;
            move.targetSquare.visitor = null;
        }

        if (move.taking) {
            move.targetPiece.alive = true;
            takenPieces.remove(move.targetPiece);
            move.targetSquare.visitor = move.targetPiece; // it should have retained its pos.
        }

        if (move.isCastling) {
            move.castlingPartner.pos = move.castlingPartnerOrigin;
            move.castlingPartnerTarget.visitor = null;
        }

        move.agent.hasMoved = move.agent.hasMovedOld;
        moveHistory.remove(move);
    }

    public Square translate(Square start, int trans) {
        int targetIndex = start.index + trans;
        if (targetIndex < 0 || targetIndex >= 64) {
            return null;
        }
        return squares[targetIndex];
    }

    public Piece pieceAt(Square square) {
        return square.visitor;
    }

    public Piece pieceAt(int index) {
        return squares[index].visitor;
    }

    public String charToSymbol(String ch) {
        return ch;
        /*
         * if (ch == "K")
         * return "\u2654";
         * if (ch == "Q")
         * return "\u2655";
         * if (ch == "R")
         * return "\u2656";
         * if (ch == "B")
         * return "\u2657";
         * if (ch == "N")
         * return "\u2658";
         * if (ch == "P")
         * return "\u2659";
         * if (ch == "k")
         * return "\u265A";
         * if (ch == "q")
         * return "\u265B";
         * if (ch == "r")
         * return "\u265C";
         * if (ch == "b")
         * return "\u265D";
         * if (ch == "n")
         * return "\u265E";
         * if (ch == "p")
         * return "\u265F";
         * return ch;
         */
    }

    // We assume the Input is always in this form: "A2"
    public int convertCoordinatesToArray(String stringCoordinate) {
        char[] move;
        int arrayCoordinate;
        // Do this before calling this function
        // move = stringCoordinate.split("[,]");
        move = stringCoordinate.toCharArray();
        int col = move[0] - 'a';
        int row = 8 - Character.getNumericValue(move[1]);
        arrayCoordinate = row * 8 + col;
        return arrayCoordinate;
    }

    public String convertArrayToCoordinates(int arrayCoordinate) {
        String stringCoordinate;
        int row = 9 - (arrayCoordinate / 8 + 1);
        int colInt = 'a' + arrayCoordinate % 8;
        char col = (char)colInt;
        stringCoordinate = Character.toString(col) + Integer.toString(row);
        return stringCoordinate;
    }

}
