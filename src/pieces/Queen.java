package pieces;

import java.util.ArrayList;
import chess.Board;
import chess.Move;
import chess.Square;
import players.Player;

public class Queen extends Piece 
{

	public Queen(Player p, Square position) 
	{
		super(p, position);
		name = "Queen";
		c = "q";
		value = 9;
	}

	@Override
	public ArrayList<Move> legalMoves(Board b) 
	{
		ArrayList<Move> moves = new ArrayList<Move>();
		
		moves.addAll(getOrthogonalMoves(b));
		moves.addAll(getDiagonalMoves(b));
		
		return moves;
	}

}
