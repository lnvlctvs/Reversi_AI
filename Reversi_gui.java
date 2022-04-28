package jtable;

import java.awt.EventQueue;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Reversi_gui {

	private JFrame frame;

	/*
	 * Launch the application.
	 */
	 
	public static void main(String[] args) {
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Reversi_gui gameWindow = new Reversi_gui();
					gameWindow.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	/*
	 * Create the application.
	 */
	 
	public Reversi_gui() {
		initializeBoard();
		initialState();
		ReversiAiBoard = new Board(currentStateOfBoard);
		paintBoard();
		possibleMoves();
	}
	
	/*
	 * Sets the initial state of the Board (four pieces in the center)
	 */
	public void initialState() {

		currentStateOfBoard = new int[8][8];
		currentStateOfBoard[3][3] = 1;
		currentStateOfBoard[4][4] = 1;
		currentStateOfBoard[3][4] = 2;
		currentStateOfBoard[4][3] = 2;
	}
	
	int[][] currentStateOfBoard;
	Board ReversiAiBoard;
	
	/* Gets the valid moves of the player through getLegalMoves(2).
	 * 	then it marks the valid moves by setting the background color of those spots
	 * 	with a bit brighter green.
	 */
	public void possibleMoves() {
		ArrayList<Board> moves = ReversiAiBoard.getLegalMoves(2);
		
		for (Board i : moves) {
			grid[i.lastMovedX][i.lastMovedY].setBackground(new Color(14, 243, 1));
		}
	}
	
	//Gets the spot of the Board that the player clicked and then calls move(x, y).
	public void getClick(JButton j) {
		Rectangle r = j.getBounds();
		int x = (r.x - 30) / 53;
		int y = (r.y - 30) / 53;
		System.out.println("Pressed (" + x + " , " + y + ")");
		move(x, y);
		}
	
	/*
	 * This function sets the background color of our grid.
	 * Case 0: empty spot painted green.
	 * Case 1: Player 1 painted Black.
	 * Case 2: Player 2  painted White.
	 */
	public void setColor(int x, int y, int c) {
		switch (c) {
		case 0:
			grid[x][y].setBackground(new Color(14, 138, 1));
			break;
		case 1:
			grid[x][y].setBackground(Color.BLACK);
			break;
		case 2:
			grid[x][y].setBackground(Color.WHITE);
			break;
		}
		
	}
	
	//This method calls setColor() for every spot of your current Board and paints it.
	void paintBoard() {
		for (int y = 0; y < 8; y++)
			for (int x = 0; x < 8; x++) {
				setColor(x, y, ReversiAiBoard.currentStateOfBoard[x][y]);
			}
	}
	
	JLabel lblPlayer, lblNewLabel;
	private JTextField textField, textField_1;
	JButton[][] grid;

	/*
	 * initialize the contents of the frame.
	 */
	 
	private void initializeBoard() {
		frame = new JFrame("Reversi");
		frame.getContentPane().setForeground(Color.BLUE);
		frame.setBounds(100, 100, 600, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		lblNewLabel = new JLabel("Computer: 2");
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setBounds(490, 105, 104, 16);
		frame.getContentPane().add(lblNewLabel);
		
		lblPlayer = new JLabel("Player: 2");
		lblPlayer.setBounds(490, 133, 61, 16);
		frame.getContentPane().add(lblPlayer);
		
		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setText("10");
		textField.setBounds(490, 214, 50, 26);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setHorizontalAlignment(SwingConstants.CENTER);
		textField_1.setText("1");
		textField_1.setBounds(490, 267, 53, 26);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblMaxDepth = new JLabel("Max depth");
		lblMaxDepth.setBounds(491, 200, 76, 16);
		frame.getContentPane().add(lblMaxDepth);
		
		JLabel lblmaximumTimeBeforeflagCutOffs = new JLabel("Max time (s)");
		lblmaximumTimeBeforeflagCutOffs.setBounds(490, 252, 77, 16);
		frame.getContentPane().add(lblmaximumTimeBeforeflagCutOffs);
		
		grid = new JButton[8][8];
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				grid[j][i] = new JButton("");
				grid[j][i].setSize(new Dimension(50, 50));
				grid[j][i].setBounds(30 + 53 * j, 30 + 53 * i, 50, 50);
				grid[j][i].setOpaque(true);
				grid[j][i].setBorderPainted(false);
				grid[j][i].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						getClick((JButton)e.getSource());
						e.consume();
					}
				});
				frame.getContentPane().add(grid[j][i] );
			}
		}
		
	}
	
	/** Makes any necessary checks about the input (x,y) and then it calls minimax 
	 * @param ReversiAi.startTime, beggining time of our search
	 * @param ReversiAi.maximumTimeBeforeflagCutOff, Max Search Time inserted by the Player. After this time ,minimax stops tree expansion.
	 * @param ReversiAi.flagChopChop, if false, we reached the Max Depth (depth) 
	 * @param depth, Max Depth inserted by the Player
	 * @param copy, a clone of board b
	 * @param ReversiAi.maxDepth, is the max level we have reached until that point
	 */
	
	public void move(int x, int y) {
		
		if (ReversiAiBoard.getLegalMoves(2).size() == 0)
		{
			initialState();
			paintBoard();
			return;
		}
		
		if (ReversiAiBoard.currentStateOfBoard[x][y] != 0)
			return;
		
		if (!ReversiAiBoard.makeMove(x, y, 2))
			return;
		
		lblPlayer.setText("Player: " + Integer.toString(ReversiAiBoard.calculate(2)));
		lblNewLabel.setText("Computer: " + Integer.toString(ReversiAiBoard.calculate(1)));
		
		paintBoard();
		
		ReversiAi.startTime = System.currentTimeMillis();
		ReversiAi.maximumTimeBeforeflagCutOff = Integer.parseInt(textField_1.getText()) * 1000;
		ReversiAi.flagChopChop = false;
		
		// Depth given by the user
		int depth = Integer.parseInt(textField.getText());
		
		int[][] copy = Board.copyBoard(ReversiAiBoard.currentStateOfBoard);
		
		for (int i = 1; i <= depth && !ReversiAi.flagChopChop; i++) {
			
			ReversiAi.maxDepth = i;
			ReversiAi.minimax(true, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, new Board(copy));
			
			if (!ReversiAi.flagChopChop) {
				ReversiAiBoard = ReversiAi.best;
			}
			else {
				System.out.println("Max depth completed: " + (i - 1));
			}
		}
		
		if (!ReversiAi.flagChopChop)
			System.out.println("Max depth completed: " + depth + " (full)");
		
		lblPlayer.setText("Player: " + Integer.toString(ReversiAiBoard.calculate(2)));
		lblNewLabel.setText("Computer: " + Integer.toString(ReversiAiBoard.calculate(1)));
		
		paintBoard();
		
		possibleMoves();
	}
}

