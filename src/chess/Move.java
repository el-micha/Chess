package chess;

import pieces.Pawn;
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
     * this is important, because the Board b reference will change; but the move members must not!
     */

    private final Piece agent;
    private final Piece targetPiece;
    private final Square originSquare;
    private final Square targetSquare;
    private boolean taking;

    // promotion?
    private final boolean isPromotion;
    // castling?
    private final boolean isCastling;
    private final Piece castlingPartner;
    private final Square castlingPartnerOrigin;
    private final Square castlingPartnerTarget;
    // en passant?
    private final boolean isPassant;
    private final Piece passantVictim;
    private final Square victimSquare;

    public Move(Board b, Piece a, Square targetS) {
        agent = a;
        originSquare = a.position();
        targetSquare = targetS;
        targetPiece = targetS.getVisitor();
        taking = targetPiece != null;

        isPromotion = a.name().equals("Pawn") && inEndRank();
        isCastling = a.name().equals("King") && Math.abs(a.position().y - targetS.y) == 2;
        isPassant = isPassant(b, a);

        if (isCastling) {
            // find direction of move in order to find which rook is partner.
            int dir = (int)Math.signum(targetSquare.y - agent.position().y);
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
            castlingPartner = b.pieceAt(agent.position().x, agent.position().y + distanceToRook);
            castlingPartnerOrigin = castlingPartner.position();
            castlingPartnerTarget = b.getSquare(agent.position().x, agent.position().y + rookTranslation);

            // sanity check
            if (!castlingPartner.name().equals("Rook")) {
                System.out.println("Error: Tried to castling with a non-rook: " + castlingPartner.name());
            }
        } else {
            castlingPartner = null;
            castlingPartnerOrigin = null;
            castlingPartnerTarget = null;
        }

        if (isPassant) {
            victimSquare = b.translate(targetSquare,
                    new int[]{-((Pawn)(a)).getMoveDirection(), 0});
            passantVictim = b.pieceAt(victimSquare);
            if (passantVictim == null) {
                System.out.println("ERROR: Move: taking en passant found no victim.");
            }
            taking = true;
        } else {
            victimSquare = null;
            passantVictim = null;
        }
    }

    // a public and a private function. private is only called at creation to ensure state separation of Board and Move.
    public boolean isPassant() {
        return isPassant;
    }

    // a public and a private function. private is only called at creation to ensure state separation of Board and Move.
    private boolean isPassant(Board b, Piece a) {
        return a.name().equals("Pawn")
                && b.getLastMove() != null
                && b.getLastMove().isPawnDouble()
                && Math.abs(b.getLastMove().targetSquare().y - a.position().y) == 1
                && b.getLastMove().targetSquare().x == a.position().x
                && targetSquare.y == b.getLastMove().targetSquare.y;
    }

    public boolean isPawnDouble() {
        return (agent.name().equals("Pawn") && Math.abs(originSquare.x - targetSquare.x) == 2);
    }

    public Square victimSquare() {
        return victimSquare;
    }

    public Piece passantVictim() {
        return passantVictim;
    }

    public Piece agent() {
        return agent;
    }

    public Piece targetPiece() {
        if (isPassant) {
            System.out.println("Warning: Targetpiece is from an en passant move.");
            return passantVictim;
        }
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

    private boolean inEndRank() {
        return ((agent.color == 0 && targetSquare.x == 7) || (agent.color == 1 && targetSquare.x == 0));
    }

}
