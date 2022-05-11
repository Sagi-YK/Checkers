import java.awt.Graphics; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.Random;

public class Checkers extends JPanel {

	/**
	 * Class Fields.
	 */
	private BufferedImage imageBlack;
	private BufferedImage imageWhite;
//	private BufferedImage imageBlackKing;
//	private BufferedImage imageWhiteKing;
	private BufferedImage imageBoard;
	private int x, y, xClicked, yClicked;
	private int xBoard, yBoard;
	private int countBlack, countWhite;
	private int iForOnlyMove, jForOnlyMove;
	private Soldier[][] board;
	private Black black;
	private White white;
	private King kingWhite;
	private King kingBlack;
	private Soldier whichColor;
	private ListOptions lo;
	private CounterPanel cp;
	private boolean isClicked;
	private boolean toAdd;
	private boolean colorTurn;
	private boolean canGoBackWhiteL;
	private boolean canGoBackWhiteR;
	private boolean canGoBackBlackL;
	private boolean canGoBackBlackR;
	private boolean onlyCanMove;
	private boolean vsPC = true;
	private boolean isSleep = false;
	private boolean endGame = false;

	/**
	 * Default constructor.
	 */
	public Checkers() {
		this.xBoard = -10;
		this.yBoard = -10;
		this.countBlack = 12;
		this.countWhite = 12;
		this.iForOnlyMove = -1;
		this.jForOnlyMove = -1;
		this.whichColor = new Soldier();
		this.isClicked = false;
		this.toAdd = false;
		this.colorTurn = false;
		this.canGoBackWhiteL = false;
		this.canGoBackWhiteR = false;
		this.canGoBackBlackL = false;
		this.canGoBackBlackR = false;
		this.onlyCanMove = false;
		this.cp = new CounterPanel();
		this.lo = new ListOptions();
		this.board = new Soldier[8][8];
		this.black = new Black();
		this.white = new White();
		this.kingBlack = new King();
		this.kingWhite = new King();
		Listener lis = new Listener();
		addMouseListener(new Listener());

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 3; j = j+2) {
				if (i == 0 || i == 2 || i == 4 || i == 6) {
					board[i][j+1] = black;
					break;
				}
				else {
					board[i][j] = black;
				}
			}

			for (int j = 7; j > 4; j = j-2) {
				if (i == 1 || i == 3 || i == 5 || i == 7) {
					board[i][j-1] = white;
					break;
				}
				else {
					board[i][j] = white;
				}
			}
		}

		try {
			imageBoard = ImageIO.read(new File("C:\\Users\\HP\\eclipse-workspace\\Checkers\\src\\"
					+ "Board.JPG"));
			imageBlack = ImageIO.read(new File("C:\\Users\\HP\\eclipse-workspace\\Checkers\\src\\"
					+ "Black.PNG"));
			imageWhite = ImageIO.read(new File("C:\\Users\\HP\\eclipse-workspace\\Checkers\\src\\"
					+ "White.PNG"));
			//			imageBlackKing = ImageIO.read(new File("C:\\Users\\HP\\eclipse-workspace\\Checkers\\src\\"
			//					+ "crownBlack.PNG"));
			//			imageWhiteKing = ImageIO.read(new File("C:\\Users\\HP\\eclipse-workspace\\Checkers\\src\\"
			//					+ "crownWhite.PNG"));
		}
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				xClicked = e.getX();
				yClicked = e.getY();
				repaint();
			}
		});
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (toAdd) {
			toAddSoldier();
		}

		int w = getWidth();
		int h = getHeight()-35;

		g.drawImage(imageBoard, 0, 35, w, h, null);

		this.x = 0;
		this.y = 35;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (this.board[i][j] != null && this.board[i][j] == this.kingBlack) {
					g.drawImage(imageBlack, x+20, y+22, 75, 70, null);
					g.drawImage(imageBlack, x+30, y+32, 55, 50, null);
				}
				else if (this.board[i][j] != null && this.board[i][j] == this.kingWhite) {
					g.drawImage(imageWhite, x+20, y+22, 75, 70, null);
					g.drawImage(imageWhite, x+26, y+28, 63, 58, null);
				}
				else if (this.board[i][j] != null && this.board[i][j] == this.black) {
					g.drawImage(imageBlack, x+20, y+22, 75, 70, null);
				}
				else if (this.board[i][j] != null && this.board[i][j] == this.white) {
					g.drawImage(imageWhite, x+20, y+22, 75, 71, null);
				}
				y = y+70;
			}
			x = x+74;
			y = 35;
		}
		if (this.isClicked) {
			if (this.whichColor == black) {
				g.drawImage(imageBlack, xClicked-42, yClicked-38, 75, 70, null);
			}
			else if (this.whichColor == white) {
				g.drawImage(imageWhite, xClicked-42, yClicked-38, 75, 71, null);
			}
			else if (this.whichColor == kingBlack) {
				g.drawImage(imageBlack, xClicked-42, yClicked-38, 75, 70, null);
				g.drawImage(imageBlack, xClicked-32, yClicked-28, 55, 50, null);
			}
			else if (this.whichColor == kingWhite) {
				g.drawImage(imageWhite, xClicked-42, yClicked-38, 75, 71, null);
				g.drawImage(imageWhite, xClicked-36, yClicked-32, 63, 59, null);
			}
		}
		if (countWhite == 0) {
			endGame = true;
			countWhite--;
			JOptionPane.showMessageDialog(null, "Black Won", "The Winner:", 1);
		}
		else if (countBlack == 0) {
			endGame = true;
			countBlack--;
			JOptionPane.showMessageDialog(null, "White Won", "The Winner:", 1);
		}
		
		if (!isClicked && vsPC && colorTurn && !endGame) {
			if (isSleep) {
				isSleep = false;
				computerThink();
			}
			else {
				isSleep = true;
				repaint();
			}
		}
	}
	
	/**
	 * 
	 * @author
	 */
	public class ListOptions implements ActionListener {
		
		private JComboBox comboBox;
		private String[] options = {"Vs PC", "Vs Player"};
		
		public ListOptions() {
			this.comboBox = new JComboBox(options);
			comboBox.addActionListener(this);
			add(comboBox);
		}
		
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == comboBox) {
				if (comboBox.getSelectedItem() == "Vs PC") {
					vsPC = true;
				}
				else if (comboBox.getSelectedItem() == "Vs Player") {
					vsPC = false;
				}
			}
		}
	}
	
	/**
	 * 
	 * @author HP
	 */
	public class CounterPanel implements ActionListener {

		private JButton newGame;

		public CounterPanel() {
			this.newGame = new JButton("New Game");
			newGame.addActionListener(this);
			add (newGame);
		}

		public void actionPerformed(ActionEvent e) {
			xBoard = -10;
			yBoard = -10;
			countBlack = 12;
			countWhite = 12;
			whichColor = null;
			isClicked = false;
			toAdd = false;
			colorTurn = false;
			canGoBackWhiteL = false;
			canGoBackWhiteR = false;
			canGoBackBlackL = false;
			canGoBackBlackR = false;
			endGame = false;
			restart();
			repaint();
		}
	}
	
	/**
	 * This function restart the game.
	 */
	public void restart() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				board[i][j] = null;
			}
		}

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 3; j = j+2) {
				if (i == 0 || i == 2 || i == 4 || i == 6) {
					board[i][j+1] = black;
					break;
				}
				else
					board[i][j] = black;
			}

			for (int j = 7; j > 4; j = j-2) {
				if (i == 1 || i == 3 || i == 5 || i == 7) {
					board[i][j-1] = white;
					break;
				}
				else
					board[i][j] = white;
			}
		}
	}
	
	/**
	 * 
	 * @author HP
	 *
	 */
	private class Listener extends MouseAdapter implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			repaint();
		}

		public void mouseClicked(MouseEvent e) {
			x = 20;
			y = 55;
			int xClicked = e.getX();
			int yClicked = e.getY();
			
			if (!isClicked) {
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						if (colorTurn && xClicked >= x && xClicked <= x+74 && yClicked >= y && yClicked <= y+70 && 
								(board[i][j] == black || board[i][j] == kingBlack)) {
							if (board[i][j] == black) {
								whichColor = black;
							}
							else {
								whichColor = kingBlack;
							}
							colorTurn = false;
							isClicked = true;
							xBoard = i;
							yBoard = j;
							board[i][j] = null;
							repaint();
						}
						else if (!colorTurn && xClicked >= x && xClicked <= x+74 && yClicked >= y && yClicked <= y+70 && 
								(board[i][j] == white || board[i][j] == kingWhite)) {
							if (board[i][j] == white) {
								whichColor = white;
							}
							else {
								whichColor = kingWhite;
							}
							colorTurn = true;
							isClicked = true;
							xBoard = i;
							yBoard = j;
							board[i][j] = null;
							repaint();
						}
						y = y+70;
					}
					x = x+74;
					y = 55;
				}
			}
			else if (isClicked) {
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						if (xClicked >= x && xClicked <= x+74 && yClicked >= y && yClicked <= y+70 && 
								whichColor != kingBlack && whichColor != kingWhite) {
							if (xBoard == i && yBoard == j && board[i][j] == null) {
								if (colorTurn)
									colorTurn = false;
								else
									colorTurn = true;
								xBoard = i;
								yBoard = j;
								toAdd = true;
								repaint();
							}
							
							if (onlyCanMove) {
								if (xBoard != iForOnlyMove || yBoard != jForOnlyMove) {
									return;
								}
								else if (!colorTurn) {
									if (i == iForOnlyMove+2 && j == jForOnlyMove+2 && board[i][j] == null && (board[i-1][j-1] == white || board[i-1][j-1] == kingWhite)) {
										board[i-1][j-1] = null;
										xBoard = i;
										yBoard = j;
										toAdd = true;
										onlyCanMove = false;
										checks(i,j);
										repaint();
									}
									else if (i == iForOnlyMove-2 && j == jForOnlyMove+2 && board[i][j] == null && (board[i+1][j-1] == white || board[i+1][j-1] == kingWhite)) {
										board[i+1][j-1] = null;
										xBoard = i;
										yBoard = j;
										toAdd = true;
										onlyCanMove = false;
										checks(i,j);
										repaint();
									}
									else if (i == iForOnlyMove+2 && j == jForOnlyMove-2 && board[i][j] == null && (board[i-1][j+1] == white || board[i-1][j+1] == kingWhite)) {
										board[i-1][j+1] = null;
										xBoard = i;
										yBoard = j;
										toAdd = true;
										onlyCanMove = false;
										checks(i,j);
										repaint();
									}
									else if (i == iForOnlyMove-2 && j == jForOnlyMove-2 && board[i][j] == null && (board[i+1][j+1] == white || board[i+1][j+1] == kingWhite)) {
										board[i+1][j+1] = null;
										xBoard = i;
										yBoard = j;
										toAdd = true;
										onlyCanMove = false;
										checks(i,j);
										repaint();
									}
								}
								else {
									if (i == iForOnlyMove+2 && j == jForOnlyMove+2 && board[i][j] == null && (board[i-1][j-1] == black || board[i-1][j-1] == kingBlack)) {
										board[i-1][j-1] = null;
										xBoard = i;
										yBoard = j;
										toAdd = true;
										onlyCanMove = false;
										checks(i,j);
										repaint();
									}
									else if (i == iForOnlyMove-2 && j == jForOnlyMove+2 && board[i][j] == null && (board[i+1][j-1] == black || board[i+1][j-1] == kingBlack)) {
										board[i+1][j-1] = null;
										xBoard = i;
										yBoard = j;
										toAdd = true;
										onlyCanMove = false;
										checks(i,j);
										repaint();
									}
									else if (i == iForOnlyMove+2 && j == jForOnlyMove-2 && board[i][j] == null && (board[i-1][j+1] == black || board[i-1][j+1] == kingBlack)) {
										board[i-1][j+1] = null;
										xBoard = i;
										yBoard = j;
										toAdd = true;
										onlyCanMove = false;
										checks(i,j);
										repaint();
									}
									else if (i == iForOnlyMove-2 && j == jForOnlyMove-2 && board[i][j] == null && (board[i+1][j+1] == black || board[i+1][j+1] == kingBlack)) {
										board[i+1][j+1] = null;
										xBoard = i;
										yBoard = j;
										toAdd = true;
										onlyCanMove = false;
										checks(i,j);
										repaint();
									}
								}
								return;
							}
							
							else if (colorTurn && xBoard-1 == i && yBoard-1 == j && board[i][j] == null) {
								xBoard = i;
								yBoard = j;
								toAdd = true;
								repaint();
							}
							else if (!colorTurn && xBoard-1 == i && yBoard+1 == j && board[i][j] == null) {
								xBoard = i;
								yBoard = j;
								toAdd = true;
								repaint();
							}
							else if (colorTurn && xBoard+1 == i && yBoard-1 == j && board[i][j] == null) {
								xBoard = i;
								yBoard = j;
								toAdd = true;
								repaint();
							}
							else if (!colorTurn && xBoard+1 == i && yBoard+1 == j && board[i][j] == null) {
								xBoard = i;
								yBoard = j;
								toAdd = true;
								repaint();
							}
							
							// Checks if kill:
							else if (xBoard-2 == i && yBoard-2 == j && board[i][j] == null && ((canGoBackBlackL && 
									(board[i+1][j+1] == white || board[i+1][j+1] == kingWhite)) || 
									(board[i+1][j+1] == black || board[i+1][j+1] == kingBlack) && colorTurn)) {
								canGoBackBlackL = false;
								board[i+1][j+1] = null;
								xBoard = i;
								yBoard = j;
								toAdd = true;
								if (!colorTurn) {
									countWhite--;
								}
								else if (colorTurn) {
									countBlack--;
								}
								checks(i,j);
								repaint();
							}
							else if (xBoard+2 == i && yBoard-2 == j && board[i][j] == null && ((canGoBackBlackR &&
									(board[i-1][j+1] == white || board[i-1][j+1] == kingWhite)) || 
									(board[i-1][j+1] == black || board[i-1][j+1] == kingBlack) && colorTurn)) {
								canGoBackBlackR = false;
								board[i-1][j+1] = null;
								xBoard = i;
								yBoard = j;
								toAdd = true;
								if (!colorTurn) {
									countWhite--;
								}
								else if (colorTurn) {
									countBlack--;
								}
								checks(i,j);
								repaint();
							}
							else if (xBoard+2 == i && yBoard+2 == j && board[i][j] == null && ((canGoBackWhiteR && 
									(board[i-1][j-1] == black || board[i-1][j-1] == kingBlack)) || 
									(board[i-1][j-1] == white || board[i-1][j-1] == kingWhite) && !colorTurn)) {
								canGoBackWhiteR = false;
								board[i-1][j-1] = null;
								xBoard = i;
								yBoard = j;
								toAdd = true;
								if (colorTurn) {
									countBlack--;
								}
								else if (!colorTurn) {
									countWhite--;
								}
								checks(i,j);
								repaint();
							}
							else if (xBoard-2 == i && yBoard+2 == j && board[i][j] == null && ((canGoBackWhiteL && 
									(board[i+1][j-1] == black || board[i+1][j-1] == kingBlack)) || 
									(board[i+1][j-1] == white || board[i+1][j-1] == kingWhite) && !colorTurn)) {
								canGoBackWhiteL = false;
								board[i+1][j-1] = null;
								xBoard = i;
								yBoard = j;
								toAdd = true;
								if (colorTurn) {
									countBlack--;
								}
								else if (!colorTurn) {
									countWhite--;
								}
								checks(i,j);
								repaint();
							}
						}
						// Checks for King:
						else if (xClicked >= x && xClicked <= x+74 && yClicked >= y && yClicked <= y+70 && 
								board[i][j] == null && (whichColor == kingBlack || whichColor == kingWhite)) {
							if (xBoard == i && yBoard == j) {
								if (colorTurn)
									colorTurn = false;
								else
									colorTurn = true;
								xBoard = i;
								yBoard = j;
								toAdd = true;
								repaint();
							}
							if (!colorTurn) {
								checksForKingBlack(i, j);
							}
							else {
								checksForKingWhite(i, j);
							}
						}
						y = y+70;
					}
					x = x+74;
					y = 55;
				}
			}
		}
	}

	/**
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	private boolean checksForKingBlackTurn(int i, int j) {
		int k = i;
		int m = j;
		while (k < 7 && m < 7) {
			if (k != i && m != j && (board[k][m] == black || board[k][m] == kingBlack)) {
				break;
			}
			else if ((board[k][m] == white || board[k][m] == kingWhite) && (board[k+1][m+1] == white || board[k+1][m+1] == kingWhite)) {
				break;
			}
			if ((board[k][m] == white || board[k][m] == kingWhite) && board[k+1][m+1] == null) {
				return true;
			}
			k++;
			m++;
		}
		k = i;
		m = j;
		while (k < 7 && m > 0) {
			if (k != i && m != j && (board[k][m] == black || board[k][m] == kingBlack)) {
				break;
			}
			else if ((board[k][m] == white || board[k][m] == kingWhite) && (board[k+1][m-1] == white || board[k+1][m-1] == kingWhite)) {
				break;
			}
			if ((board[k][m] == white || board[k][m] == kingWhite) && board[k+1][m-1] == null) {
				return true;
			}
			k++;
			m--;
		}
		k = i;
		m = j;
		while (k > 0 && m < 7) {
			if (k != i && m != j && (board[k][m] == black || board[k][m] == kingBlack)) {
				break;
			}
			else if ((board[k][m] == white || board[k][m] == kingWhite) && (board[k-1][m+1] == white || board[k-1][m+1] == kingWhite)) {
				break;
			}
			if ((board[k][m] == white || board[k][m] == kingWhite) && board[k-1][m+1] == null) {
				return true;
			}
			k--;
			m++;
		}
		k = i;
		m = j;
		while (k > 0 && m > 0) {
			if (k != i && m != j && (board[k][m] == black || board[k][m] == kingBlack)) {
				break;
			}
			else if ((board[k][m] == white || board[k][m] == kingWhite) && (board[k-1][m-1] == white || board[k-1][m-1] == kingWhite)) {
				break;
			}
			if ((board[k][m] == white || board[k][m] == kingWhite) && board[k-1][m-1] == null) {
				return true;
			}
			k--;
			m--;
		}
		return false;
	}

	/**
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	private boolean checksForKingWhiteTurn(int i, int j) {
		int k = i;
		int m = j;
		while (k < 7 && m < 7) {
			if (k != i && m != j && (board[k][m] == white || board[k][m] == kingWhite)) {
				break;
			}
			else if ((board[k][m] == black || board[k][m] == kingBlack) && (board[k+1][m+1] == black || board[k+1][m+1] == kingBlack)) {
				break;
			}
			
			if ((board[k][m] == black || board[k][m] == kingBlack) && board[k+1][m+1] == null) {
				return true;
			}
			k++;
			m++;
		}
		k = i;
		m = j;
		while (k < 7 && m > 0) {
			if (k != i && m != j && (board[k][m] == white || board[k][m] == kingWhite)) {
				break;
			}
			else if ((board[k][m] == black || board[k][m] == kingBlack) && (board[k+1][m-1] == black || board[k+1][m-1] == kingBlack)) {
				break;
			}
			if ((board[k][m] == black || board[k][m] == kingBlack) && board[k+1][m-1] == null) {
				return true;
			}
			k++;
			m--;
		}
		k = i;
		m = j;
		while (k > 0 && m < 7) {
			if (k != i && m != j && (board[k][m] == white || board[k][m] == kingWhite)) {
				break;
			}
			else if ((board[k][m] == black || board[k][m] == kingBlack) && (board[k-1][m+1] == black || board[k-1][m+1] == kingBlack)) {
				break;
			}
			if ((board[k][m] == black || board[k][m] == kingBlack) && board[k-1][m+1] == null) {
				return true;
			}
			k--;
			m++;
		}
		k = i;
		m = j;
		while (k > 0 && m > 0) {
			if (k != i && m != j && (board[k][m] == white || board[k][m] == kingWhite)) {
				break;
			}
			else if ((board[k][m] == black || board[k][m] == kingBlack) && (board[k-1][m-1] == black || board[k-1][m-1] == kingBlack)) {
				break;
			}
			if ((board[k][m] == black || board[k][m] == kingBlack) && board[k-1][m-1] == null) {
				return true;
			}
			k--;
			m--;
		}
		return false;
	}
	
	/**
	 * 
	 */
	public void toAddSoldier() {
		if (whichColor == black && yBoard == 7) {
			this.board[xBoard][yBoard] = kingBlack;
			if (checksForKingBlackTurn(xBoard,yBoard)) {
				colorTurn = true;
			}
		}
		else if (whichColor == white && yBoard == 0) {
			this.board[xBoard][yBoard] = kingWhite;
			if (checksForKingWhiteTurn(xBoard,yBoard)) {
				colorTurn = false;
			}
		}
		else if (whichColor == kingBlack) {
			this.board[xBoard][yBoard] = kingBlack;
		}
		else if (whichColor == kingWhite) {
			this.board[xBoard][yBoard] = kingWhite;
		}
		else if (whichColor == black) {
			this.board[xBoard][yBoard] = black;
		}
		else if (whichColor == white) {
			this.board[xBoard][yBoard] = white;
		}
		this.xBoard = -10;
		this.yBoard = -10;
		isClicked = false;
		toAdd = false;
	}
	
	/**
	 * 
	 * @param i
	 * @param j
	 */
	private void checks(int i, int j) {
		if (colorTurn) {
			if (i-2 >= 0 && j+2 < 8 && board[i-2][j+2] == null && board[i-1][j+1] == black) {
				colorTurn = false;
				onlyCanMove = true;
				iForOnlyMove = i;
				jForOnlyMove = j;
				return;
			}
			else if (i+2 < 8 && j+2 < 8 && board[i+2][j+2] == null && board[i+1][j+1] == black) {
				colorTurn = false;
				onlyCanMove = true;
				iForOnlyMove = i;
				jForOnlyMove = j;
				return;
			}
			
			else if (i-2 >= 0 && j-2 >= 0 && board[i-2][j-2] == null && board[i-1][j-1] == black) {
				colorTurn = false;
				onlyCanMove = true;
				iForOnlyMove = i;
				jForOnlyMove = j;
				return;
			}
			else if (i+2 < 8 && j-2 >= 0 && board[i+2][j-2] == null && board[i+1][j-1] == black) {
				colorTurn = false;
				onlyCanMove = true;
				iForOnlyMove = i;
				jForOnlyMove = j;
				return;
			}
		}
		else {
			if (i-2 >= 0 && j-2 >= 0 && board[i-2][j-2] == null && board[i-1][j-1] == white) {
				colorTurn = true;
				onlyCanMove = true;
				iForOnlyMove = i;
				jForOnlyMove = j;
				return;
			}
			else if (i+2 < 8 && j-2 >= 0 && board[i+2][j-2] == null && board[i+1][j-1] == white) {
				colorTurn = true;
				onlyCanMove = true;
				iForOnlyMove = i;
				jForOnlyMove = j;
				return;
			}
			else if (i-2 >= 0 && j+2 < 8 && board[i-2][j+2] == null && board[i-1][j+1] == white) {
				colorTurn = true;
				onlyCanMove = true;
				iForOnlyMove = i;
				jForOnlyMove = j;
				return;
			}
			else if (i+2 < 8 && j+2 < 8 && board[i+2][j+2] == null && board[i+1][j+1] == white) {
				colorTurn = true;
				onlyCanMove = true;
				iForOnlyMove = i;
				jForOnlyMove = j;
				return;
			}
		}
	}
	
	/**
	 * 
	 * @param i
	 * @param j
	 */
	private void checksForKingBlack(int i, int j) {
		int k = i;
		int m = j;
		int deleteX = -10, deleteY = -10;
		int howMuchKilled = 0;
		boolean cantMove = false;
		if (xBoard < i && yBoard < j) {
			while (k >= 0 && m >= 0) {
				if (xBoard != k && yBoard != m && (board[k][m] == black || board[k][m] == kingBlack)) {
					cantMove = true;
				}
				if (board[k][m] == white || board[k][m] == kingWhite) {
					howMuchKilled++;
					deleteX = k;
					deleteY = m;
				}
				if (!cantMove && xBoard == k && yBoard == m && howMuchKilled <= 1) {
					if (deleteX >= 0 && deleteY >= 0) {
						board[deleteX][deleteY] = null;
						countWhite--;
					}
					if (howMuchKilled == 1 && checksForKingBlackTurn(i, j)) {
						colorTurn = true;
					}
					xBoard = i;
					yBoard = j;
					toAdd = true;
					repaint();
				}
				k--;
				m--;
			}
		}
		else if (xBoard < i && yBoard > j) {
			while (k >= 0 && m <= 7) {
				if (xBoard != k && yBoard != m && (board[k][m] == black || board[k][m] == kingBlack)) {
					cantMove = true;
				}
				if (board[k][m] == white || board[k][m] == kingWhite) {
					howMuchKilled++;
					deleteX = k;
					deleteY = m;
				}
				if (!cantMove && xBoard == k && yBoard == m && howMuchKilled <= 1) {
					if (deleteX >= 0 && deleteY >= 0) {
						board[deleteX][deleteY] = null;
						countWhite--;
					}
					if (howMuchKilled == 1 && checksForKingBlackTurn(i, j)) {
						colorTurn = true;
					}
					xBoard = i;
					yBoard = j;
					toAdd = true;
					repaint();
				}
				k--;
				m++;
			}
		}
		else if (xBoard > i && yBoard < j) {
			while (k <= 7 && m >= 0) {
				if (xBoard != k && yBoard != m && (board[k][m] == black || board[k][m] == kingBlack)) {
					cantMove = true;
				}
				if (board[k][m] == white || board[k][m] == kingWhite) {
					howMuchKilled++;
					deleteX = k;
					deleteY = m;
				}
				if (!cantMove && xBoard == k && yBoard == m && howMuchKilled <= 1) {
					if (deleteX >= 0 && deleteY >= 0) {
						board[deleteX][deleteY] = null;
						countWhite--;
					}
					if (howMuchKilled == 1 && checksForKingBlackTurn(i, j)) {
						colorTurn = true;
					}
					xBoard = i;
					yBoard = j;
					toAdd = true;
					repaint();
				}
				k++;
				m--;
			}
		}
		else if (xBoard > i && yBoard > j) {
			while (k <= 7 && m <= 7) {
				if (xBoard != k && yBoard != m && (board[k][m] == black || board[k][m] == kingBlack)) {
					cantMove = true;
				}
				if (board[k][m] == white || board[k][m] == kingWhite) {
					howMuchKilled++;
					deleteX = k;
					deleteY = m;
				}
				if (!cantMove && xBoard == k && yBoard == m && howMuchKilled <= 1) {
					if (deleteX >= 0 && deleteY >= 0) {
						board[deleteX][deleteY] = null;
						countWhite--;
					}
					if (howMuchKilled == 1 && checksForKingBlackTurn(i, j)) {
						colorTurn = true;
					}
					xBoard = i;
					yBoard = j;
					toAdd = true;
					repaint();
				}
				k++;
				m++;
			}
		}
	}

	/**
	 * 
	 * @param i
	 * @param j
	 */
	private void checksForKingWhite(int i, int j) {
		int k = i;
		int m = j;
		int deleteX = -10, deleteY = -10;
		int howMuchKilled = 0;
		boolean cantMove = false;
		if (xBoard < i && yBoard < j) {
			while (k >= 0 && m >= 0) {
				if (xBoard != k && yBoard != m && (board[k][m] == white || board[k][m] == kingWhite)) {
					cantMove = true;
				}
				if (board[k][m] == black || board[k][m] == kingBlack) {
					howMuchKilled++;
					deleteX = k;
					deleteY = m;
				}
				if (!cantMove && xBoard == k && yBoard == m && howMuchKilled <= 1) {
					if (deleteX >= 0 && deleteY >= 0) {
						board[deleteX][deleteY] = null;
						countBlack--;
					}
					if (howMuchKilled == 1 && checksForKingWhiteTurn(i, j)) {
						colorTurn = false;
					}
					xBoard = i;
					yBoard = j;
					toAdd = true;
					repaint();
				}
				k--;
				m--;
			}
		}
		else if (xBoard < i && yBoard > j) {
			while (k >= 0 && m <= 7) {
				if (xBoard != k && yBoard != m && (board[k][m] == white || board[k][m] == kingWhite)) {
					cantMove = true;
				}
				if (board[k][m] == black || board[k][m] == kingBlack) {
					howMuchKilled++;
					deleteX = k;
					deleteY = m;
				}
				if (!cantMove && xBoard == k && yBoard == m && howMuchKilled <= 1) {
					if (deleteX >= 0 && deleteY >= 0) {
						board[deleteX][deleteY] = null;
						countBlack--;
					}
					if (howMuchKilled == 1 && checksForKingWhiteTurn(i, j)) {
						colorTurn = false;
					}
					xBoard = i;
					yBoard = j;
					toAdd = true;
					repaint();
				}
				k--;
				m++;
			}
		}
		else if (xBoard > i && yBoard < j) {
			while (k <= 7 && m >= 0) {
				if (xBoard != k && yBoard != m && (board[k][m] == white || board[k][m] == kingWhite)) {
					cantMove = true;
				}
				if (board[k][m] == black || board[k][m] == kingBlack) {
					howMuchKilled++;
					deleteX = k;
					deleteY = m;
				}
				if (!cantMove && xBoard == k && yBoard == m && howMuchKilled <= 1) {
					if (deleteX >= 0 && deleteY >= 0) {
						board[deleteX][deleteY] = null;
						countBlack--;
					}
					if (howMuchKilled == 1 && checksForKingWhiteTurn(i, j)) {
						colorTurn = false;
					}
					xBoard = i;
					yBoard = j;
					toAdd = true;
					repaint();
				}
				k++;
				m--;
			}
		}
		else if (xBoard > i && yBoard > j) {
			while (k <= 7 && m <= 7) {
				if (xBoard != k && yBoard != m && (board[k][m] == white || board[k][m] == kingWhite)) {
					cantMove = true;
				}
				if (board[k][m] == black || board[k][m] == kingBlack) {
					howMuchKilled++;
					deleteX = k;
					deleteY = m;
				}
				if (!cantMove && xBoard == k && yBoard == m && howMuchKilled <= 1) {
					if (deleteX >= 0 && deleteY >= 0) {
						board[deleteX][deleteY] = null;
						countBlack--;
					}
					if (howMuchKilled == 1 && checksForKingWhiteTurn(i, j)) {
						colorTurn = false;
					}
					xBoard = i;
					yBoard = j;
					toAdd = true;
					repaint();
				}
				k++;
				m++;
			}
		}
	}

	/**
	 * The function thinks of the best move the computer can make.
	 */
	public void computerThink() {

		try {
			Thread.sleep(500);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		int i = 0;
		int j = 0;
		int[][] board2 = new int[8][8];
		
		for (i = 0; i < 8; i++) {
			for (j = 0; j < 8; j++) {
				if (board[i][j] == black) {
					computerChecks(board2, i, j);
				}
			}
		}

		int max = 0;
		int iMove = -1;
		int jMove = -1;
		for (i = 0; i < 8; i++) {
			for (j = 0; j < 8; j++) {
				if (board[i][j] == black) {
					if (board2[i][j] >= max) {
						max = board2[i][j];
						iMove = i;
						jMove = j;
					}
				}
			}
		}
		if (max > 0) {
			toMove(iMove, jMove);
			return;
		}
		

		int count = 0;
		Random rand = new Random();
		int randSoldier = rand.nextInt(countBlack) + 1;

		for (i = 0; i < 8; i++) {
			for (j = 0; j < 8; j++) {
				if (board[i][j] == black) {
					count++;
					if (count == randSoldier) {
						if (checkIfCanMove(i, j)) {
							toMove(i, j);
						}
						else {
							count = 0;
							i = 0;
							j = 0;
							randSoldier = rand.nextInt(countBlack) + 1;
						}
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param board2
	 * @param i
	 * @param j
	 */
	private void computerChecks(int[][] board2, int i, int j) {
		if (i-2 >= 0 && j+2 < 8 && board[i-2][j+2] == null && (board[i-1][j+1] == white || board[i-1][j+1] == kingWhite)) {
			if (board[i-1][j+1] == kingWhite) {
				board2[i][j]++;
			}
			board2[i][j]++;
		}
		else if (i+2 < 8 && j+2 < 8 && board[i+2][j+2] == null && (board[i+1][j+1] == white || board[i+1][j+1] == kingWhite)) {
			if (board[i+1][j+1] == kingWhite) {
				board2[i][j]++;
			}
			board2[i][j]++;
		}
		else if (onlyCanMove) {
			if (i-2 >= 0 && j-2 >= 0 && board[i-2][j-2] == null && (board[i-1][j-1] == white || board[i-1][j-1] == kingWhite)) {
				if (board[i-1][j-1] == kingWhite) {
					board2[i][j]++;
				}
				board2[i][j]++;
			}
			else if (i+2 < 8 && j-2 >= 0 && board[i+2][j-2] == null && (board[i+1][j-1] == white || board[i+1][j-1] == kingWhite)) {
				if (board[i+1][j-1] == kingWhite) {
					board2[i][j]++;
				}
				board2[i][j]++;
			}
		}
	}
	
	/**
	 * 
	 * @param i
	 * @param j
	 */
	public void toMove(int i, int j) {
		if (i-2 >= 0 && j+2 < 8 && board[i-2][j+2] == null && (board[i-1][j+1] == white || board[i-1][j+1] == kingWhite)) {
			board[i][j] = null;
			board[i-1][j+1] = null;
			whichColor = black;
			xBoard = i-2;
			yBoard = j+2;
			toAdd = true;
			colorTurn = false;
			onlyCanMove = false;
			countWhite--;
			checks(i-2, j+2);
			repaint();
		}
		else if (i+2 < 8 && j+2 < 8 && board[i+2][j+2] == null && (board[i+1][j+1] == white || board[i+1][j+1] == kingWhite)) {
			board[i][j] = null;
			board[i+1][j+1] = null;
			whichColor = black;
			xBoard = i+2;
			yBoard = j+2;
			toAdd = true;
			colorTurn = false;
			onlyCanMove = false;
			countWhite--;
			checks(i+2, j+2);
			repaint();
		}
		else if (onlyCanMove) {
			onlyCanMove = false;
			if (i-2 >= 0 && j-2 >= 0 && board[i-2][j-2] == null && (board[i-1][j-1] == white || board[i-1][j-1] == kingWhite)) {
				board[i][j] = null;
				board[i-1][j-1] = null;
				whichColor = black;
				xBoard = i-2;
				yBoard = j-2;
				toAdd = true;
				colorTurn = false;
				countWhite--;
				checks(i-2, j-2);
				repaint();
			}
			else if (i+2 < 8 && j-2 >= 0 && board[i+2][j-2] == null && (board[i+1][j-1] == white || board[i+1][j-1] == kingWhite)) {
				board[i][j] = null;
				board[i+1][j-1] = null;
				whichColor = black;
				xBoard = i+2;
				yBoard = j-2;
				toAdd = true;
				colorTurn = false;
				countWhite--;
				checks(i+2, j-2);
				repaint();
			}
		}
		
		else if (i-1 > 0 && j+1 < 8 && board[i-1][j+1] == null) {
			board[i][j] = null;
			whichColor = black;
			xBoard = i-1;
			yBoard = j+1;
			toAdd = true;
			colorTurn = false;
			repaint();
		}
		else if (i+1 < 8 && j+1 < 8 && board[i+1][j+1] == null) {
			board[i][j] = null;
			whichColor = black;
			xBoard = i+1;
			yBoard = j+1;
			toAdd = true;
			colorTurn = false;
			repaint();
		}
	}
	
	/**
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public boolean checkIfCanMove(int i, int j) {
		if (i-1 > 0 && j+1 < 8 && board[i-1][j+1] == null) {
			return true;
		}
		else if (i+1 < 8 && j+1 < 8 && board[i+1][j+1] == null) {
			return true;
		}
		return false;
	}
}
