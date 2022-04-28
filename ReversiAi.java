import java.util.ArrayList;

public class ReversiAi {
	
	public static int maxDepth = 10; 							// negative to remove limit
	public static long maximumTimeBeforeflagCutOff = 800; 		// ms.  After this time ,minimax stops tree expansion.
	public static long startTime = 0; 							// initialize time after minimax tree's creation.
	public static boolean flagChopChop = false; 				// Flag initialized false because the flagChopChop of the minimax tree hasn't been done yet.

	public static Board best;
	
	/**Basic MINIMAX function with alpha-beta pruning
	 *  @param player    -- Computer or opponent
	 *  @param depth     -- Subtree depth, starting with 0
	 *  @param alpha     -- Computer assured of
	 *  @param beta	     -- Opponent assured of
	 *  @param gameBoard --	Board object
	 * @return	Node value
	 */
	public static int minimax(boolean player, int depth, int alpha, int beta, Board gameBoard) {
		
		// possibly downsize maximumTimeBeforeflagCutOff to compensate for return path computation time
		if (System.currentTimeMillis() - startTime >= maximumTimeBeforeflagCutOff) {  
			flagChopChop = true;
			return gameBoard.evaluate();
		}
		else if (depth > maxDepth) {
			return gameBoard.evaluate();
		}
		
		ArrayList<Board> moves = gameBoard.getLegalMoves(player ? 1 : 2);
		
		if (moves.size() == 0) {
			return player ? 64 : -64;
		}
		
		if (player) {
			
			int top = 0;
			
			for (int i = 0; i < moves.size(); i++) {
				int score = minimax(false, depth + 1, alpha, beta, moves.get(i));
				
				if (score > alpha) {
					alpha = score;
					top = i;
				}
				
				if (alpha >= beta)
					break;
			}
			
			// set Board corresponding to optimal value (0-63)
			if (depth == 0) { 
				best = moves.get(top);
			}
			
			return alpha;
		} else {
			for (Board i : moves) {
				int score = minimax(true, depth + 1, alpha, beta, i);
				
				if (score < beta)
					beta = score;
				
				if (alpha >= beta)
					break;
			}
			return beta;
		}
		
	}
	
}

