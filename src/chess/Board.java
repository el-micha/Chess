package chess;

import java.util.ArrayList;
import pieces.Piece;
import players.Player;

public class Board
{
    /*
     * A board has a state and pieces
     * Can return possible moves for a player
     * Can make a move and undo moves
     * Has a win condition and can return the win state
     */

    public Game game;
    private Square[][] squares;
    private ArrayList<Move> moveHistory;
    private ArrayList<Piece> takenPieces;

    public Board(Game g) {
        game = g;
        squares = new Square[8][8];
        // Black = 0, White = 1; Top left square is white, so 1.
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j] = new Square(i, j);
            }
        }
        moveHistory = new ArrayList<Move>();
        takenPieces = new ArrayList<Piece>();

        System.out.println("Created Board ");
    }

    public Move getLastMove() {
        return moveHistory.get(moveHistory.size() - 1);
    }

    public void setupInitial(ArrayList<Piece> pieces) {
        for (Piece piece : pieces) {
            piece.position().setVisitor(piece);
        }
    }

    public Square getSquare(int x, int y) {
        if (x < 8 && x >= 0 && y < 8 && y >= 0)
            return squares[x][y];
        // System.out.println("Bad square: " + x + "/" + y);
        return null;
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

        moveHistory.add(move);
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
        moveHistory.remove(move);
    }

    public Square translate(Square start, int[] trans) {
        return getSquare(start.x + trans[0], start.y + trans[1]);
    }

    public Piece pieceAt(Square square) {
        return square.getVisitor();
    }

    public Piece pieceAt(int x, int y) {
        return squares[x][y].getVisitor();
    }

    public String toString() {

        String xPointers = "";
        String[] yPointers = {" ", " ", " ", " ", " ", " ", " ", " "};
        if (moveHistory.size() > 0) {
            Move lastMove = moveHistory.get(moveHistory.size() - 1);
            int ox = lastMove.originSquare().x;
            int oy = lastMove.originSquare().y;
            int tx = lastMove.targetSquare().x;
            int ty = lastMove.targetSquare().y;

            for (int i = 0; i < 8; i++) {
                if (i == oy) {
                    xPointers += "v ";
                } else if (i == ty) {
                    xPointers += "V ";
                } else {
                    xPointers += "  ";
                }
            }
            yPointers[ox] = "<";
            yPointers[tx] = "<<";

        }

        String s = "";
        s += "  " + xPointers + "\n";
        int cnt = 0;
        for (Square[] rows : squares) {
            if (cnt > 0) {
                s += yPointers[(int)(cnt / 8) - 1];
                s += "\n";
            }
            s += (8 - (int)(cnt / 8)) + "|";
            for (Square square : rows) {
                s += charToSymbol(square.ch()) + " ";
                cnt++;
            }

        }
        s += "\n-+---------------";
        s += "\n |a b c d e f g h";

        return s;
    }

    public int valuation(Player p) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (squares[i][j].getVisitor() != null && squares[i][j].getVisitor().color == p.color)
                    count += squares[i][j].getVisitor().getValue();
            }
        }
        return count;
    }

    public String charToSymbol(String ch) {
        return ch;

        // if (ch.equals("K"))
        // return "\u2654";
        // if (ch.equals("Q"))
        // return "\u2655";
        // if (ch.equals("R"))
        // return "\u2656";
        // if (ch.equals("B"))
        // return "\u2657";
        // if (ch.equals("N"))
        // return "\u2658";
        // if (ch.equals("P"))
        // return "\u2659";
        // if (ch.equals("k"))
        // return "\u265A";
        // if (ch.equals("q"))
        // return "\u265B";
        // if (ch.equals("r"))
        // return "\u265C";
        // if (ch.equals("b"))
        // return "\u265D";
        // if (ch.equals("n"))
        // return "\u265E";
        // if (ch.equals("p"))
        // return "\u265F";
        // return ch;

    }

    // We assume the Input is always in this form: "A2"
    public Square convertInputToBoard(String stringCoordinate) {
        char[] move;
        // Do this before calling this function
        // move = stringCoordinate.split("[,]");
        move = stringCoordinate.toCharArray();
        int col = move[0] - 'a';
        int row = 8 - Character.getNumericValue(move[1]);
        Square square = new Square(col, row);
        return square;
    }

}
