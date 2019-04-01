package pieces;

import java.util.ArrayList;
import chess.Board;
import chess.Move;
import chess.Square;
import players.Player;

public class Knight extends Piece
{
	
	//the king will need this, too, to determine whether he is in check.
	public static int[] knightTranslations = {-17, -15, -10, -6, 6, 10, 15, 17};
	
	public Knight(Player p, Square pos) 
	{
		super(p, pos);
		name = "Knight";
		c = "n";
	}
	
	@Override
	public ArrayList<Move> legalMoves(Board b)
	{
		ArrayList<Move> moves = new ArrayList<Move>();
		
		for (int i = 0; i < 8; i++)
		{
			int move = knightTranslations[i];
			Square newPos = b.translate(pos, move);
			
			if (newPos != null && !occupiedByFriend(newPos) && !moveEndangersKing(b, this, newPos))
			{
				moves.add(new Move(b, this, newPos));
			}
		}
		
		return moves;
	}
	
}
