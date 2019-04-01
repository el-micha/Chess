package chess;

import java.nio.charset.Charset;

public class Main 
{
	public static void main(String [] args)
	{
		
		System.out.println("Hello");
		Game game = new Game();
		
		for (int i = 0; i < 1000; i++)
		{
			game.nextHalfturn();			
		}
		
	}
}
