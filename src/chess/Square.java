package chess;

import pieces.Piece;

public class Square 
{
	/*
	 * A single square of the board
	 * Is either black or white
	 * Is either empty or held/visited by a single piece
	 * Can be "in check" or "threatened": Important for king's legal moves
	 * Black = odd
	 * White = even
	 */
	
	public final int index;
	
	public Piece visitor = null;
	public String c;
	public Square(int i)
	{
		index = i;
		if (color() == 1)
		{
			//white
			c = ".";
		}
		else
		{
			//black
			c = "=";
		}
	}
	
	public String ch()
	{
		if (visitor == null)
		{
			return c;
		}
		else
		{
			return visitor.ch();
		}
	}
	
	public int color()
	{
		//even ranks: (index+1)%2 
		int rank = (int)(index/8);
		if (rank%2 == 0)
		{
			return (index+1)%2;
		}
		else
		{
			return (index)%2;
		}
		
	}
	
}
