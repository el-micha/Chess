package pieces;

import java.util.ArrayList;
import chess.Board;
import chess.Move;
import chess.Square;
import players.Player;

public abstract class Piece {

	/*
	 * Is subclassed by concrete pieces Has a position, that is a Square Has a set
	 * of legal moves Taken pieces are out of the game Can be used or unused.
	 * Important for castling. Has color: 0 black, 1 white
	 */
	public final Player player;
	public final int color;
	public String c;

	protected boolean alive = true;
	protected Square pos = null;
	protected boolean hasMoved = false;
	protected boolean hasMovedOld = false;
	protected String name;

	public Piece(Player p, Square position) {
		player = p;
		color = player.color;
		pos = position;
		c = "@";
	}

	public void setPosition(Square targetSquare) {
		pos = targetSquare;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setDead() {
		alive = false;
		player.callbackDead(this);
	}

	public void setAlive() {
		alive = true;
		player.callbackAlive(this);
	}

	public Square position() {
		return pos;
	}

	public boolean hasMoved() {
		return hasMoved;
	}

	public void setMoved() {
		hasMovedOld = hasMoved;
		hasMoved = true;
	}

	public void resetMoved() {
		hasMoved = hasMovedOld;
	}

	public String name() {
		return name;
	}

	public String ch() {
		if (player.color == 1) {
			return c.toUpperCase();
		}
		return c;
	}

	public abstract ArrayList<Move> legalMoves(Board b);

	public boolean squareEmpty(Square square) {
		return square.getVisitor() == null;
	}

	public boolean occupiedByFriend(Square square) {
		if (square.getVisitor() == null) {
			return false;
		}
		return square.getVisitor().color == this.color;
	}

	public boolean occupiedByEnemy(Square square) {
		if (square.getVisitor() == null) {
			return false;
		}
		return square.getVisitor().color != this.color;
	}

	public boolean moveEndangersKing(Board b, Move move) {
		/*
		 * Here we make a move, check if the king is (still) in check after it and then
		 * undo the move.
		 */
		boolean danger = false;
		b.applyMove(move);
		if (player.king.isInCheck(b)) {
			danger = true;
		}
		b.undoMove(move);
		// System.out.println("Piece: Done and undone move");
		return danger; // || player.king.isInCheck(b);
	}

	public boolean moveEndangersKing(Board b, Piece p, Square target) {
		Move move = new Move(b, p, target);
		boolean danger = moveEndangersKing(b, move);
//		if (!danger && p.name.equals("King")) {
//			System.out.println("Moving piece " + p.name() + " to " + target.algPos() + " does not endanger " + player.color + " king.");
//		}
		return danger;
	}

	public ArrayList<Move> getOrthogonalMoves(Board b) {
		ArrayList<Move> res = new ArrayList<Move>();
		res.addAll(probeRay(b, 0, 1));
		res.addAll(probeRay(b, 0, -1));
		res.addAll(probeRay(b, 1, 0));
		res.addAll(probeRay(b, -1, 0));
		return res;
	}

	public ArrayList<Move> getDiagonalMoves(Board b) {
		ArrayList<Move> res = new ArrayList<Move>();
		res.addAll(probeRay(b, 1, 1));
		res.addAll(probeRay(b, -1, -1));
		res.addAll(probeRay(b, -1, 1));
		res.addAll(probeRay(b, 1, -1));
		return res;
	}

	public ArrayList<Move> probeRay(Board b, int dx, int dy) {
		/*
		 * Find legal moves in direction dx,dy. As soon as we fail, we stop probing this
		 * direction and return list of what we have so far. Check illegal move
		 * conditions in this order (cheapest first): 1) Is the new position out of
		 * bounds? 2) Is there a friendly piece on the new position?
		 */
		ArrayList<Move> res = new ArrayList<Move>();
		for (int i = 0; i < 7; i++) {
			int[] trans = { i * dx, i * dy };
			Square target = b.translate(pos, trans);
			if (target == null || occupiedByFriend(target) || moveEndangersKing(b, this, target)) {
				break;
			}
			res.add(new Move(b, this, target));
		}
		return res;
	}

}
