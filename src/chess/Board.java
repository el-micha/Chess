package chess;

import java.util.ArrayList;
import java.util.Arrays;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;
import players.Player;

public class Board
{
    /*
     * A board has a state and pieces
     * Can return possible moves for a player
     * Can make a move and undo moves
     * Has a win condition and can return the win state
     */

    /*
     * Idea: keep map of latest 64 boards in static memory, because all moves are tested beforehand
     * key for board: parent board plus move: both needs to be
     * 
     * https://www.baeldung.com/java-hashcode
     * 
     */

    public Game game;
    private Square[][] squares;
    private ArrayList<Move> moveHistory;

    private ArrayList<Piece> whitePieces = new ArrayList<>();
    private ArrayList<Piece> blackPieces = new ArrayList<>();

    private King whiteKing;
    private King blackKing;

    public Board(Game g) {
        game = g;
        squares = new Square[8][8];
        // Black = 0, White = 1; Top left square is white, so 1.
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j] = new Square(i, j, this);
            }
        }
        moveHistory = new ArrayList<Move>();

        initializePieces(0);
        initializePieces(1);

        setupInitial(whitePieces);
        setupInitial(blackPieces);

        System.out.println("Created Board ");
    }

    public boolean isRemis() {
        if (moveHistory.size() <= 50) {
            return false;
        }
        for (Move move : moveHistory.subList(moveHistory.size() - 50, moveHistory.size())) {
            if (move.taking() || move.agent().name().equals("Pawn")) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Piece> getPieces(int color) {
        if (color == 1)
            return whitePieces;
        return blackPieces;
    }

    private void initializePieces(int color) {
        int rank;
        int pawnRank;
        ArrayList<Piece> pieces;
        if (color == 1) {
            rank = 7;
            pawnRank = 6;
            pieces = whitePieces;
        } else {
            rank = 0;
            pawnRank = 1;
            pieces = blackPieces;
        }

        pieces.add(new Rook(color, getSquare(rank, 0)));
        pieces.add(new Knight(color, getSquare(rank, 1)));
        pieces.add(new Bishop(color, getSquare(rank, 2)));
        pieces.add(new Queen(color, getSquare(rank, 3)));
        pieces.add(new Bishop(color, getSquare(rank, 5)));
        pieces.add(new Knight(color, getSquare(rank, 6)));
        pieces.add(new Rook(color, getSquare(rank, 7)));
        King king = new King(color, getSquare(rank, 4));
        pieces.add(king);

        if (color == 1) {
            whiteKing = king;
        } else {
            blackKing = king;
        }

        for (int i = 0; i < 8; i++) {
            pieces.add(new Pawn(color, getSquare(pawnRank, i)));
        }

    }

    public boolean isCheckmate(Player p) {
        return isCheckmate(p.color);
    }

    public boolean isCheckmate(int color) {
        boolean outOfMoves = (getLegalMoves(color, true).size() == 0);
        return outOfMoves && isInCheck(color);
    }

    public boolean isStalemate(Player p) {
        return isStalemate(p.color);
    }

    public boolean isStalemate(int color) {
        return !isInCheck(color) && getLegalMoves(color, true).size() == 0;
    }

    public boolean isInCheck(Player p) {
        return isInCheck(p.color);
    }

    public boolean isInCheck(int color) {
        if (color == 1) {
            return whiteKing.isInCheck(this);
        }
        return blackKing.isInCheck(this);
    }

    public Move getLastMove() {
        if (moveHistory.size() > 0) {
            return moveHistory.get(moveHistory.size() - 1);
        }
        return null;
    }

    public void setupInitial(ArrayList<Piece> pieces) {
        for (Piece piece : pieces) {
            piece.position().setVisitor(piece);
        }
    }

    /**
     * Return list of legal moves for the pieces with the given color.
     * We use color instead of player so that any player can evaluate moves of any player
     * 
     * @param p
     * @return
     */
    public ArrayList<Move> getLegalMoves(Player p, boolean simulation) {
        return getLegalMoves(p.color, simulation);
    }

    public ArrayList<Move> getLegalMoves(int color, boolean simulation) {
        ArrayList<Move> legalMoves = new ArrayList<>();
        ArrayList<Piece> pieces = getPieces(color);

        for (int i = 0; i < pieces.size(); i++) {
            Piece piece = pieces.get(i);
            if (!piece.isAlive()) {
                continue;
            }
            legalMoves.addAll(piece.legalMoves(this));
        }
        if (!simulation && legalMoves.size() == 0) {
            game.callbackOutOfLegalMoves(color);
        }
        return legalMoves;

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
         * - normal harmless move
         * - taking move
         * - castling: not taking
         * - promotion: harmless or taking
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

        // 5) en passant
        // agent has moved already. need to take the victim.
        if (move.isPassant()) {
            move.passantVictim().setDead();
            detachPiece(move.passantVictim());
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
        // not for the en passant case, because the victim is not in the usual place!
        if (move.taking() && !move.isPassant()) {
            move.targetPiece().setAlive();
            movePiece(move.targetPiece(), move.targetSquare());
        }

        // 4) uncastling
        if (move.isCastling()) {
            movePiece(move.castlingPartner(), move.castlingPartnerOrigin());
        }

        // 5) en passant
        // agent has been unmoved already. put victim back
        if (move.isPassant()) {
            move.passantVictim().setAlive();
            movePiece(move.passantVictim(), move.victimSquare());
        }

        move.agent().resetMoved();
        moveHistory.remove(move);
    }

    public Square getSquare(int x, int y) {
        if (x < 8 && x >= 0 && y < 8 && y >= 0)
            return squares[x][y];
        // System.out.println("Bad square: " + x + "/" + y);
        return null;
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
        if (isCheckmate(1 - p.color)) {
            return 1000;
        }
        if (isStalemate(1 - p.color) || isStalemate(p.color)) {
            return -500;
        }
        if (isInCheck(1 - p.color)) {
            return 40;
        }
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(squares);
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
        Board other = (Board)obj;
        if (!Arrays.deepEquals(squares, other.squares))
            return false;
        return true;
    }

}
