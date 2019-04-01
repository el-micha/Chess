package pieces;

import java.util.ArrayList;
import chess.Board;
import chess.Move;
import chess.Square;
import players.Player;

public class Bishop extends Piece 
{

	public Bishop(Player p, Square pos) 
	{
		super(p, pos);
		name = "Bishop";
		c = "b";
	}

	@Override
	public ArrayList<Move> legalMoves(Board b) 
	{
		ArrayList<Move> moves = new ArrayList<Move>();
		
		moves.addAll(getDiagonalMoves(b));
		
		return moves;
	}

}
