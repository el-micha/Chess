package players;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import chess.Board;
import chess.Move;

public class AlphaBetaPlayer extends Player {
	
	private int maxDepth;
	
	public AlphaBetaPlayer(int col, String n, int md) {
		super(col, n);
		maxDepth = md;
	}

	@Override
	public void makeMove(Board b, ArrayList<Move> legalMoves) {
		ArrayList<Double> values = new ArrayList<>();
		for (Move move : legalMoves)
		{
			b.applyMove(move);
			values.add(alphabeta(b, maxDepth - 1, -9999, 9999, false));
			b.undoMove(move);
		}
		ArrayList<Move> maxMoves = new ArrayList<>();
		double maxValue = Collections.max(values);
		for (int i = 0; i < values.size(); i++)
		{
			if (values.get(i) == maxValue)
			{
				maxMoves.add(legalMoves.get(i));
			}
		}
		// choose a random optimal move
		Move move = maxMoves.get(new Random().nextInt(maxMoves.size()));
		
		String origin = move.originSquare().convertBoardToOutput();
        String target = move.targetSquare().convertBoardToOutput();
        String victim;
        // System.out.println("NOW MOVING FOR REAL...........................");
        if (move.taking()) {
            victim = " taking " + move.targetPiece().name();
        } else {
            victim = "";
        }
		System.out.println(move.agent().name() + " from " + origin + " to " + target + victim);
        b.applyMove(move);
	}
	
	
	private double alphabeta(Board b, int depth, double alpha, double beta, boolean maxPlayer)
	{
		
		if (depth <= 0 || b.isCheckmate(this) || b.isCheckmate(1 - color))
		{
			return b.valuation(this);
		}
		double value;
		if (maxPlayer)
		{	
			value = -9999;
			ArrayList<Move> legalMoves = b.getLegalMoves(color, true);
			if(legalMoves.size() == 0)
			{
				return value;
			}
			for (Move move : legalMoves)
			{
				b.applyMove(move);
				value = Math.max(value, alphabeta(b, depth - 1, alpha, beta, false));
				b.undoMove(move);
				alpha = Math.max(alpha, value);
				if (alpha >= beta)
				{
					break;
				}
			}
		}
		else
		{
			value = 9999;
			ArrayList<Move> legalMoves = b.getLegalMoves(1 - color, true);
			if(legalMoves.size() == 0)
			{
				return value;
			}
			for (Move move : legalMoves)
			{
				b.applyMove(move);
				value = Math.min(value, alphabeta(b, depth - 1, alpha, beta, true));
				b.undoMove(move);
				beta = Math.min(beta, value);
				if (alpha >= beta)
				{
					break;
				}
			}
		}
		return value;		
	}
	
}
