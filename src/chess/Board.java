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
            squares[i] = new Square(i, this);
        }
        moveHistory = new ArrayList<Move>();
        takenPieces = new ArrayList<Piece>();

        System.out.println("Created Board ");
    }

    public void setupInitial(ArrayList<Piece> pieces) {
        for (Piece piece : pieces) {
            piece.position().setVisitor(piece);
        }
    }

    /**
     * Check if targetSquare is empty or an enemy
     * Kill piece on targetSquare, prepare to return it.
     * Remove piece on targetSquare (detach both)
     * Move agent to targetSquare
     * 
     * @param agent
     * @param targetSquare
     */
    private Piece movePiece(Piece agent, Square targetSquare) {
        Piece victim = targetSquare.getVisitor();
        if (victim != null) {
            victim.setDead();
            detachPiece(victim);
        }
        if (agent.position() != null) {
            agent.position().setVisitor(null);
        }
        agent.setPosition(targetSquare);
        targetSquare.setVisitor(agent);
        return victim;
    }

    private void detachPiece(Piece p) {
        p.position().setVisitor(null);
        p.setPosition(null);
    }

    public void applyMove(Move move) {
        /*
         * normal harmless move
         * taking move
         * 
         * castling: not taking
         * promotion: harmless or taking
         */

        // 1) move the piece and 2) remove targetPiece
        movePiece(move.agent(), move.targetSquare());

        // 3) promote
        if (move.isPromotion()) {
            // agent has moved already
            detachPiece(move.agent());
            Piece queen = game.callbackPromotion(move.agent(), move.targetSquare());
            movePiece(queen, move.targetSquare());
        }

        // 4) castling
        if (move.isCastling()) {
            movePiece(move.castlingPartner(), move.castlingPartnerTarget());
        }

        move.agent().setMoved();
    }

    public void undoMove(Move move) {
        /*
         * move has the same information as in applyMove, but the board state has changed.
         * 
         */

        // 3) unpromote
        if (move.isPromotion()) {
            // remove the queen; the original pawn (stored in move object) will be moved next
            // there is a queen on the targetsquare, not stored by the move object
            Piece queen = move.targetSquare().getVisitor();
            queen.setDead();
            detachPiece(queen);
            // set pawn alive again, but put it back in the next step
            move.agent().setAlive();
        }

        // 1) move the piece back
        movePiece(move.agent(), move.originSquare());
        // 2) recreate taken piece
        if (move.taking()) {
            move.targetPiece().setAlive();
            movePiece(move.targetPiece(), move.targetSquare());
        }

        // 4) uncastling
        if (move.isCastling()) {
            movePiece(move.castlingPartner(), move.castlingPartnerOrigin());
        }

        move.agent().resetMoved();
    }

    private int x(int t) {
        return (int)Math.signum(t) * (Math.abs(t) % 8);
    }

    private int y(int t) {
        return (int)(t / 8);
    }

    private int t(int x, int y) {
        return x + 8 * y;
    }

    public Square translate(Square start, int trans) {
        // convert everything to 2d model, so it is easier to check for out of bounds
        int startx = x(start.index);
        int starty = y(start.index);
        int targetx = x(start.index + trans);
        int targety = y(start.index + trans);

        // TODO: nobody can move across border. check for that.
        int targetIndex = start.index + trans;
        if (targetIndex < 0 || targetIndex >= 64) {
            return null;
        }
        // if the direction of trans points right, then the target location must be right of the start,
        // otherwise we go across borders
        // same for left
        int transDir = (int)Math.signum(trans);

        return squares[targetIndex];
    }

    public Piece pieceAt(Square square) {
        return square.getVisitor();
    }

    public Piece pieceAt(int index) {
        return squares[index].getVisitor();
    }

    public String toString() {

        String xPointers = "";
        String[] yPointers = {" ", " ", " ", " ", " ", " ", " ", " "};
        if (moveHistory.size() > 0) {
            Move lastMove = moveHistory.get(moveHistory.size() - 1);
            int originIndex = lastMove.originSquare().index;
            int targetIndex = lastMove.targetSquare().index;
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

}
