package chess;

import java.util.ArrayList;
import java.util.List;
import pieces.King;
import pieces.Piece;
import players.Player;

public class BoardProperties
{

    private final boolean isWhiteInCheck;
    private final boolean isBlackInCheck;
    private final List<Move> whiteLegalMoves;
    private final List<Move> blackLegalMoves;

    public BoardProperties(Board b) {
        List<Piece> whitePieces = b.getPieces(1);
        List<Piece> blackPieces = b.getPieces(0);
        King whiteKing = b.getWhiteKing();
        King blackKing = b.getBlackKing();

        isWhiteInCheck = whiteKing.isInCheck(b);
        isBlackInCheck = blackKing.isInCheck(b);

        whiteLegalMoves = calculateLegalMoves(whitePieces, b);
        blackLegalMoves = calculateLegalMoves(blackPieces, b);

        b.callbackBoardProperties(this);
    }

    public boolean isInCheck(Player p) {
        return isInCheck(p.color);
    }

    public boolean isInCheck(int color) {
        if (color == 1) {
            return isWhiteInCheck;
        }
        return isBlackInCheck;
    }

    public List<Move> getLegalMoves(int color) {
        if (color == 1) {
            return whiteLegalMoves;
        }
        return blackLegalMoves;
    }

    private List<Move> calculateLegalMoves(List<Piece> pieces, Board b) {
        ArrayList<Move> legalMoves = new ArrayList<>();
        for (int i = 0; i < pieces.size(); i++) {
            Piece piece = pieces.get(i);
            if (!piece.isAlive()) {
                continue;
            }
            legalMoves.addAll(piece.legalMoves(b));
        }
        // TODO: handle this callback
        // if (!simulation && legalMoves.size() == 0) {
        // game.callbackOutOfLegalMoves(color);
        // }
        return legalMoves;
    }

}
