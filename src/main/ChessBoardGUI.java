package main;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/*
 * Overall class to make the whole ChessBoard and all the helper methods to 
 * tell the user whether their solution is correct or not
 */
public class ChessBoardGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ImageIcon queenP = new ImageIcon(("queen.png"));
	Image img = queenP.getImage();
	Image newImg = img.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
	ImageIcon queenPic = new ImageIcon(newImg);
	private JButton safe;
	private JLabel ifNotSafe;
	private JButton[][] chessBoxes = new JButton[8][8];
	public JPanel chessBoard;
	public ArrayList<Boxes> queensOnBoard = new ArrayList<Boxes>();
	public ArrayList<Boxes> conflictingQueens = new ArrayList<Boxes>();

	/*
	 * Makes the whole frame for the ChessBoard Also creates the Tip and Safe
	 * buttons And Label to display when the solution is incorrect
	 */
	public ChessBoardGUI() {
		createBoard();
		add(chessBoard, BorderLayout.CENTER);

		safe = new JButton("Safe?");
		ifNotSafe = new JLabel("Status of Board is displayed here...");

		ButtonListen1 safeListener = new ButtonListen1();

		safe.addActionListener(safeListener);
		

		JPanel panel1 = new JPanel();
		panel1.add(ifNotSafe);
		panel1.add(safe);
		add(panel1, BorderLayout.PAGE_START);
	}

	/*
	 * Creates the overall ChessBoard
	 */
	public void createBoard() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.setRows(8);
		gridLayout.setColumns(8);
		chessBoard = new JPanel(gridLayout);
		chessBoard.setSize(400, 400);
		chessBoard.setBorder(new LineBorder(Color.BLACK));
		chessBoard.setVisible(true);

		/*
		 * Loops through to add each chessBox to the chessBoard
		 */
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				chessBoxes[x][y] = new JButton();
				chessBoxes[x][y].setBorderPainted(false);
				/*
				 * Assigns a color to every other box
				 */
				if ((x + y) % 2 == 0) {
					chessBoxes[x][y].setBackground(Color.BLACK);
				} else {
					chessBoxes[x][y].setBackground(Color.WHITE);
				}
				chessBoxes[x][y].setOpaque(true);
				chessBoard.add(chessBoxes[x][y]);

				// Adds the ActionListener to each chessBox
				BoxListener boxListen = new BoxListener();
				chessBoxes[x][y].addActionListener(boxListen);
			}
		}
	}

	/*
	 * ActionListener for the safe button
	 */
	class ButtonListen1 implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// Tells user which queens are not safe
			if (checkSolution(queensOnBoard) == true) {
				ifNotSafe.setText("This Solution is correct so far");
				ifNotSafe.setForeground(Color.BLACK);
				defaultColors();
				conflictingQueens.clear();
				if (queensOnBoard.size() == 8) {
					ifNotSafe.setText("YOU DID IT!!!!!");
					ifNotSafe.setForeground(Color.ORANGE);
				}
			}
			// When the solution is incorrect
			else {
				ifNotSafe.setText("This Solution is incorrect so far");
				ifNotSafe.setForeground(Color.BLACK);
				defaultColors();
				// *********Also highlight the queens that are not safe******
				// conflictingQueens(queensOnBoard);
				highlightBoxes();
			}
		}

	}

	/*
	 * Action Listener for if the individual boxes on the ChessBoard are clicked
	 */
	class BoxListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button = ((JButton) e.getSource());

			// Runs through a loop to find the X and Y coordinate of the
			// JButton(Box) that was clicked
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					if (chessBoxes[x][y] == button) {
						/*
						 * If there is No Queen at that JButton
						 */
						if ((isOnBoard(queensOnBoard, x, y) == false)) {
							// Makes sure the user can not place more than
							// 8 Queens on the board
							if (queensOnBoard.size() < 8) {
								// Sets Picture of Queen on box
								button.setIcon(queenPic);
								// Adds box to the ArrayList of occupied boxes
								queensOnBoard.add(new Boxes(x, y));
							}
						}
						/*
						 * If there is a Queen at that JButton
						 */
						else {
							removeQueen(queensOnBoard, x, y);
							button.setIcon(null);
						}
					}
				}
			}
		}
	}

	/*
	 * Tells the user where to put their next Queen
	 * @param queensOnBoard2 The Queens that are currently on the chess board
	 */
	public void tipOutput(ArrayList<Boxes> queensOnBoard2) {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (isOnBoard(queensOnBoard2, x, y) == false) {
					queensOnBoard2.add(new Boxes(x,y));
					if(checkSolution(queensOnBoard2) == true){
						defaultColors();
						chessBoxes[x][y].setBackground(Color.GREEN);
					}
					else{
						//Remove conflicting queens added in checkSolution method
						for (int i = 0; i < queensOnBoard2.size(); i++) {
							if (((queensOnBoard2.get(i)).getX() == x) && ((queensOnBoard2.get(i)).getY() == y)) {
								queensOnBoard2.remove(i);
							}
						}
						
						for (int i = 0; i < conflictingQueens.size(); i++) {
							if (((conflictingQueens.get(i)).getX() == x) && ((conflictingQueens.get(i)).getY() == y)) {
								conflictingQueens.remove(i);
							}
						}
					}
				}
			}
		}
	}
	
	
	/*
	 * Sets the background for the chessBoxes back to their original colors
	 */
	public void defaultColors() {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				/*
				 * Assigns a color to every other box
				 */
				if ((x + y) % 2 == 0) {
					chessBoxes[x][y].setBackground(Color.BLACK);
				} else {
					chessBoxes[x][y].setBackground(Color.WHITE);
				}
			}
		}
	}

	/*
	 * Checks if a certain Box has a Queen in it or not
	 * 
	 * @param a Is the ArrayList of the Boxes currently occupied by Queens
	 * 
	 * @param x Is the X coordinate of the Box that was clicked
	 * 
	 * @param y Is the Y coordinate of the Box that was clicked
	 */
	public boolean isOnBoard(ArrayList<Boxes> a, int x, int y) {
		for (int i = 0; i < a.size(); i++) {
			if (((a.get(i)).getX() == x) && ((a.get(i)).getY() == y)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Removes a Queen from the GUI at the specified Box (JButton)
	 * 
	 * @param a Is the ArrayList of the Boxes currently occupied by Queens
	 * 
	 * @param x Is the X coordinate of the Box that was clicked
	 * 
	 * @param y Is the Y coordinate of the Box that was clicked
	 */
	public void removeQueen(ArrayList<Boxes> a, int x, int y) {
		/*
		 * Removes the box from the overall ArrayList of Boxes Occupied by
		 * queens according to the x and y location
		 */
		for (int i = 0; i < a.size(); i++) {
			if (((a.get(i)).getX() == x) && ((a.get(i)).getY() == y)) {
				queensOnBoard.remove(i);
			}
		}
	}

	/*
	 * Checks to see if solution is correct thusfar
	 * 
	 * @param queensOnBoard Is the ArrayList of Boxes that are occupied by
	 * Queens currently on the ChessBoard
	 */
	public boolean checkSolution(ArrayList<Boxes> queensOnBoard2) {
		int size = queensOnBoard2.size();
		boolean status = true;
		if ((size == 0) || (size == 1)) {
			return status;
		}
		// Runs through each pair to make sure
		for (int x = 0; x < size - 1; x++) {
			for (int y = 1; y < size; y++) {
				// Makes sure the indices are not the same
				// i.e. comparing one box against itself
				if (x != y) {

					// Checks if Queen is safe from horizontal attacks
					if (queensOnBoard2.get(x).getX() == queensOnBoard2.get(y).getX()) {
						conflictingQueens.add(queensOnBoard2.get(x));
						conflictingQueens.add(queensOnBoard2.get(y));
						status = false;

						// Checks if Queen is safe from vertical attacks
					} else if (queensOnBoard2.get(x).getY() == queensOnBoard2.get(y).getY()) {
						conflictingQueens.add(queensOnBoard2.get(x));
						conflictingQueens.add(queensOnBoard2.get(y));
						status = false;

						// Checks if Queen is safe from diagonal attacks
						// Uses diagonalAttack(queensOnBoard) as a helper method
					} else if (diagonalAttack(queensOnBoard2) == true) {
						status = false;
					}
				}
			}
		}
		return status;
	}

	/*
	 * Checks to see if the queen is safe from diagonal attacks
	 * 
	 * @param queensOnBoard2 Is the list of Queens that are currently on the
	 * board
	 */
	public boolean diagonalAttack(ArrayList<Boxes> queensOnBoard2) {
		boolean status = false;
		int size = queensOnBoard2.size();

		for (int x = 0; x < size - 1; x++) {
			for (int y = 1; y < size; y++) {
				// Makes sure the indices are not the same
				// i.e. comparing one box against itself
				if (x != y) {
					// Overall if Statement to find out if queens can attack
					// each other diagonally
					if ((Math.abs((queensOnBoard2.get(x).getX()) - (queensOnBoard2.get(y).getX()))) == (Math
							.abs((queensOnBoard2.get(x).getY()) - (queensOnBoard2.get(y).getY())))) {
						conflictingQueens.add(queensOnBoard2.get(x));
						conflictingQueens.add(queensOnBoard2.get(y));
						status = true;
					}
				}
			}
		}

		return status;
	}

	/*
	 * Highlights boxes that are conflicting with one another
	 */
	public void highlightBoxes() {
		int size1 = conflictingQueens.size();
		int size2 = queensOnBoard.size();

		// When there aren't any queens at risk, this block
		// changes the background colors of the boxes back to
		// Their respective color
		if ((size1 == 0) && (size1 == 1)) {
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					/*
					 * Assigns a color to every other box
					 */
					if ((x + y) % 2 == 0) {
						chessBoxes[x][y].setBackground(Color.BLACK);
					} else {
						chessBoxes[x][y].setBackground(Color.WHITE);
					}
				}
			}
		}
		if (size1 >= 2) {
			// Runs through both the highlight and queensOnBoard ArrayLists and
			// changes the background for the Queens at risk
			for (int b = 0; b < size2; b++) {
				for (int a = 0; a < size1; a++) {
					if ((conflictingQueens.get(a).getX() == queensOnBoard.get(b).getX())
							&& (conflictingQueens.get(a).getY() == queensOnBoard.get(b).getY())) {
						int x = queensOnBoard.get(b).getX();
						int y = queensOnBoard.get(b).getY();
						chessBoxes[x][y].setBackground(Color.RED);
					}
				}
			}
		}
	}

	/*
	 * Main method to run the program
	 * 
	 * @param args Is the String of args given to the console to run the
	 * operations of the program
	 */
	public static void main(String[] args) {
		createGUI();
	}

	public static void createGUI() {
		JFrame frame = new ChessBoardGUI();

		frame.setTitle("ChessBoard");
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

	}
}
