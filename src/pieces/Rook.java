package pieces;

import java.util.ArrayList;
import chess.Board;
import chess.Move;
import chess.Square;
import players.Player;

public class Rook extends Piece 
{

	public Rook(int col, Square pos) 
	{
		super(col, pos);
		name = "Rook";
		c = "r";
		value = 5;
	}

	@Override
	public ArrayList<Move> legalMoves(Board b) 
	{
		ArrayList<Move> moves = new ArrayList<Move>();
		
		moves.addAll(getOrthogonalMoves(b));
		
		return moves;
	}

}
