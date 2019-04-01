package pieces;

import java.util.ArrayList;
import chess.Board;
import chess.Move;
import chess.Square;
import players.Player;

public class Rook extends Piece 
{

	public Rook(Player p, Square pos) 
	{
		super(p, pos);
		name = "Rook";
		c = "r";
	}

	@Override
	public ArrayList<Move> legalMoves(Board b) 
	{
		ArrayList<Move> moves = new ArrayList<Move>();
		
		moves.addAll(getOrthogonalMoves(b));
		
		return moves;
	}

}
