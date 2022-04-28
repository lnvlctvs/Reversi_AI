package jtable;

import java.util.ArrayList;

class Board {
	
	public int[][] currentStateOfBoard; // 8 x 8; Player number or 0 for empty
	
	public int lastMovedX, lastMovedY; // last called coordinates to makeMove()
	
	public Board(int[][] currentStateOfBoard) {
		
		this.currentStateOfBoard = currentStateOfBoard;
		
	}
	
	/** This function tries the makeMove() function for 64 combinations and saves the valid ones
	 * @param player	Player 1 or 2
	 * @return List of valid board outcomes List of valid Board outcomes
	 */
	
	public ArrayList<Board> getLegalMoves(int player) {
		
		ArrayList<Board> BoardList = new ArrayList<Board>();
		Board gameBoard = new Board(copyBoard(currentStateOfBoard));
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				
				if (gameBoard.makeMove(j, i, player)) {
					BoardList.add(gameBoard);
					gameBoard = new Board(copyBoard(currentStateOfBoard)); // if it would've been false it can be reused
				}
			}
		}
		
		return BoardList;
	}
	
	/** This function clones 2-dimensional array
	 * @param 2-dimensional array to clone
	 * @return	Cloned array
	 */
	public static int[][] copyBoard(int[][] currentStateOfBoard) {
	    int[][] r = new int[8][];
	    for (int i = 0; i < 8; i++) {
	        r[i] = currentStateOfBoard[i].clone();
	    }
	    return r;
	}
	
	/** This function calculates the value difference of the Board (in favor of player 1)
	 *	@return	Value difference
	 */
	public int evaluate() {
		return calculate(1) - calculate(2);
	}
	
	/** This function calculates the value for player 1 or 2
	 * @param player	Player 1 or 2
	 * @return	Value 
	 */
	public int calculate(int player) {
		int value = 0;
		
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				if (currentStateOfBoard[j][i] == player)
					value++;
			}
		
		return value;
	}
	
	/** This function puts down a piece and makes a move if it's valid for the given player
	 * @param X	x-coordinate
	 * @param Y y-coordinate
	 * @param player Player 1 or 2
	 * @return	Returns false if move is illegal
	 */
	public boolean makeMove(int X, int Y, int player) {
		
		if (currentStateOfBoard[X][Y] != 0) // spot already occupied
			return false;
		
		boolean legalOnce = false;
		
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 && j == 0)
					continue;
				
				boolean disksToChange = false, passedOpponent = false;
				int k = 1;
				
				while (X + j * k >= 0 && X + j * k < 8
						&& Y + i * k >= 0 && Y + i * k < 8) { // Stay inside Board
					
					if (currentStateOfBoard[X + j * k][Y + i * k] == 0 || 
							(currentStateOfBoard[X + j * k][Y + i * k] == player && !passedOpponent))
					{
						break;
					}
					if (currentStateOfBoard[X + j * k][Y + i * k] == player && passedOpponent) {
						disksToChange = true;
						break;
					}
					else if (currentStateOfBoard[X + j * k][Y + i * k] == player % 2 + 1) {
						passedOpponent = true;
						k++;
					}
				}
				
				if (disksToChange) {
					
					currentStateOfBoard[X][Y] = player;
					
					for (int h = 1; h <= k; h++) {
						currentStateOfBoard[X + j * h][Y + i * h] = player;
					}
					
					legalOnce = true;
				}
			}
		}
		
		this.lastMovedX = X;
		this.lastMovedY = Y;
		
		return legalOnce;
	}
}
