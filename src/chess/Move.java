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

    public Piece agent;
    public Piece targetPiece;
    public Square originSquare;
    public Square targetSquare;
    public boolean taking;

    // promotion?
    public boolean isPromotion = false;
    public boolean isCastling = false;
    // castling?
    public Piece castlingPartner;
    public Square castlingPartnerOrigin;
    public Square castlingPartnerTarget;

    public Move(Board b, Piece a, Square targetS) {
        agent = a;
        originSquare = a.pos;
        targetSquare = targetS;
        targetPiece = targetS.visitor;
        taking = targetPiece != null;

        isPromotion = isPromotion(a, targetS);
        isCastling = isCastling(a, targetS);

        if (isCastling) {
            // find direction of move in order to find which rook is partner.
            int dir = (int)Math.signum(targetSquare.index - agent.pos.index);
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
            castlingPartner = b.pieceAt(agent.pos.index + distanceToRook);
            castlingPartnerOrigin = castlingPartner.pos;
            castlingPartnerTarget = b.squares[agent.pos.index + rookTranslation];

            // sanity check
            if (!castlingPartner.name.equals("Rook")) {
                System.out.println("Error: Tried to castling with a non-rook: " + castlingPartner.name);
            }
        }
    }

    private boolean isPromotion(Piece a, Square targetS) {
        return (a.name.equals("Pawn") && inEndRank(targetS.index));
    }

    private boolean isCastling(Piece a, Square targetS) {

        return (a.name.equals("King") && Math.abs(a.pos.index - targetS.index) == 2);
    }

    private boolean inEndRank(int i) {
        return ((i >= 0 && i <= 7) || (i >= 56 && i <= 63));
    }

}
