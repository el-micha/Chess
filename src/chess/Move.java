package chess;

import pieces.Piece;

public class Move
{
    /*
     * Move class
     * - associated piece
     * - starting square
     * - target square
     * - is castling? then side and handle hasMoved
     * - is promotion? then piece must be exchanged
     */

    /*
     * set these parameters once at creation and then leave them be.
     */

    private final Piece agent;
    private final Piece targetPiece;
    private final Square originSquare;
    private final Square targetSquare;
    private final boolean taking;

    // promotion?
    private final boolean isPromotion;
    private final boolean isCastling;
    // castling?
    private final Piece castlingPartner;
    private final Square castlingPartnerOrigin;
    private final Square castlingPartnerTarget;

    public Move(Board b, Piece a, Square targetS) {
        agent = a;
        originSquare = a.position();
        targetSquare = targetS;
        targetPiece = targetS.getVisitor();
        taking = targetPiece != null;

        isPromotion = a.name().equals("Pawn") && inEndRank(targetS.index);
        isCastling = a.name().equals("King") && Math.abs(a.pos() - targetS.index) == 2;

        if (isCastling) {
            // find direction of move in order to find which rook is partner.
            int dir = (int)Math.signum(targetSquare.index - agent.pos());
            int distanceToRook;
            int rookTranslation;
            if (dir > 0) {
                // king moving to right, so rook is +3 away
                distanceToRook = 3;
                rookTranslation = -2;
            } else {
                // king moving to left, so rook is -4 away
                distanceToRook = -4;
                rookTranslation = 3;
            }
            castlingPartner = b.pieceAt(agent.pos() + distanceToRook);
            castlingPartnerOrigin = castlingPartner.position();
            castlingPartnerTarget = b.squares[agent.pos() + rookTranslation];

            // sanity check
            if (!castlingPartner.name().equals("Rook")) {
                System.out.println("Error: Tried to castling with a non-rook: " + castlingPartner.name());
            }
        } else {
            castlingPartner = null;
            castlingPartnerOrigin = null;
            castlingPartnerTarget = null;
        }
    }

    public Piece agent() {
        return agent;
    }

    public Piece targetPiece() {
        return targetPiece;
    }

    public Square originSquare() {
        return originSquare;
    }

    public Square targetSquare() {
        return targetSquare;
    }

    public boolean taking() {
        return taking;
    }

    public boolean isPromotion() {
        return isPromotion;
    }

    public boolean isCastling() {

        return isCastling;
    }

    public Piece castlingPartner() {
        return castlingPartner;
    }

    public Square castlingPartnerOrigin() {
        return castlingPartnerOrigin;
    }

    public Square castlingPartnerTarget() {
        return castlingPartnerTarget;
    }

    private boolean inEndRank(int i) {
        return ((i >= 0 && i <= 7) || (i >= 56 && i <= 63));
    }

}
