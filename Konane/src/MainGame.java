import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

public class MainGame extends GraphicsProgram implements Runnable {

	Algorithm algorithm = new Algorithm();
	int width, height;
	
	// Aesthetic Components
	Font fHead = new Font("Serif", Font.BOLD, 16); Font fStat = new Font("Serif", Font.BOLD, 12);
	Color bg = new Color(209,209,209);
    Color bar = new Color(193,85,1);
    Color snow = new Color(180,212,255);
    private Color menuColor = new Color(52, 63, 71);
    GImage imgTitle = new GImage("../Images/TitleLogo.png");
    
    // G Components 
    JButton btnRemove, btnNG, btnPly2, btnPly3, btnMovable, btnMove1, btnMaxJumps, btnMoveOrder, btnMoveOptimal;
    JButton btnMiniMax, btnMMABP;
    JTextField tfWord, tfX, tfY, tfMoveOrder;
	JTextArea tfEventArea;
	JScrollPane eventPane;
	
	GLabel lblX1, lblX2, lblX3, lblX4, lblX5, lblX6, lblX7, lblX8;
	GLabel lblY1, lblY2, lblY3, lblY4, lblY5, lblY6, lblY7, lblY8;
	GLabel lblMoveOrder, lblMOEx;
	
	GOval[] confetti = new GOval[700];
	
	// Array and List Values
	ArrayList<ArrayList<Tile>> tilePieces = new ArrayList<ArrayList<Tile>>();
	ArrayList<ArrayList<Integer>> passableList = new ArrayList<ArrayList<Integer>>();
	Tile[][] tiles = new Tile[8][8];
	
	// Additional Utils
	private RandomGenerator rand = new RandomGenerator();
	
	private static final double DELAY = 1;
	
	// Termination Logic & Aesthetics
	boolean drawC = true;
	int condition = 0;
	boolean paused = false;
	
	// Game Logic
	String strRemovalDir = "Black moves first\nEnter a X,Y coordinate pair and click 'Remove' to remove a piece";
	int moveCounter = 1;
	int turnCounter = 1;
	int removedX, removedY;

	/**
	 * Creates the GUI as well as initializes the Buttons for the game.
	 * Also initializes the Konane board to be projected on the GUI
	 */
	public void init() {
		this.setSize(500,600); width = getWidth(); height = getHeight(); setBackground(menuColor); 
	    Frame c = (Frame)this.getParent().getParent(); c.setTitle("KONANE - Cenci Puleo");
	    GRect rectTop = new GRect(0,0,600,70); rectTop.setFilled(true); 
	    rectTop.setFillColor(Color.WHITE); rectTop.setColor(Color.WHITE); add(rectTop);
        GRect rectBot = new GRect(0, 430, 600, 170); rectBot.setFilled(true); 
        rectBot.setFillColor(bar); rectBot.setColor(bar); add(rectBot);
	    add(imgTitle, 1, 1);
	    
	    algorithm.setMain(this);
	    
	    tfEventArea = new JTextArea(10, 10); tfEventArea.setLineWrap(true); tfEventArea.setWrapStyleWord(true);
        tfEventArea.setEditable(false); eventPane = new JScrollPane(tfEventArea); eventPane.setBounds(10, 440, 475, 150);
        eventPane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK)); add(eventPane, 12, 440);
        tfEventArea.setText(strRemovalDir);
        
        GLabel lblCoords = new GLabel("To Move: (     ,     )"); lblCoords.setColor(Color.WHITE);
        add(lblCoords, 355, 120); lblCoords.setFont(fHead);
        
        tfX = new JTextField(1); add(tfX, 430, 103); tfY = new JTextField(1); add(tfY, 451, 103);
        
        btnRemove = new JButton("Remove"); btnRemove.setBounds(380,130,80,30);
        btnRemove.addActionListener(this); add(btnRemove, 380,130); 
        
        btnMove1 = new JButton("Move-One"); btnMove1.setBounds(370, 155, 100, 30);
        btnMove1.addActionListener(this); add(btnMove1, 370, 155);  btnMove1.setEnabled(false);
        
        btnMaxJumps = new JButton("Max Jumps"); btnMaxJumps.setBounds(370, 180, 100, 30);
        btnMaxJumps.addActionListener(this); add(btnMaxJumps, 370, 180);  btnMaxJumps.setEnabled(false);
        
        lblMoveOrder = new GLabel("Set of Jumps"); lblMoveOrder.setColor(Color.WHITE);
        add(lblMoveOrder, 375, 220); lblMoveOrder.setFont(fHead);
        
        lblMOEx = new GLabel("Ex (x,y) ULDR"); lblMOEx.setColor(Color.WHITE);
        add(lblMOEx, 370, 240); lblMOEx.setFont(fHead);

        tfMoveOrder = new JTextField(12); add(tfMoveOrder, 348, 240);
        tfMoveOrder.setHorizontalAlignment(JTextField.CENTER);
        
        btnMoveOrder = new JButton("Move Order"); btnMoveOrder.setBounds(370, 260, 100, 30);
        btnMoveOrder.addActionListener(this); add(btnMoveOrder, 370, 260);  btnMoveOrder.setEnabled(false);
        
        btnMoveOptimal = new JButton("Move Optimal"); btnMoveOptimal.setBounds(360, 285, 120, 30);
        btnMoveOptimal.addActionListener(this); add(btnMoveOptimal, 360, 285);  btnMoveOptimal.setEnabled(false);
        
        btnMiniMax = new JButton("Minimax"); btnMiniMax.setBounds(360, 310, 120, 30);
        btnMiniMax.addActionListener(this); add(btnMiniMax, 360, 310);  btnMiniMax.setEnabled(false);
        
        btnMMABP = new JButton("MM ABP"); btnMMABP.setBounds(360, 335, 120, 30);
        btnMMABP.addActionListener(this); add(btnMMABP, 360, 335);  btnMMABP.setEnabled(false);
        
        btnPly2 = new JButton("Ply 2");  btnPly2.setBounds(345,360,80,30);
        btnPly2.addActionListener(this); add(btnPly2, 345, 360); btnPly2.setEnabled(false);

        btnPly3 = new JButton("Ply 3"); btnPly3.setBounds(345,390,80,30);
        btnPly3.addActionListener(this); add(btnPly3, 345, 390); btnPly3.setEnabled(false);
        
        btnNG = new JButton("New Game"); btnNG.setBounds(415,360,90,30);  btnNG.addActionListener(this);
        btnNG.setToolTipText("Create a New Game"); add(btnNG, 415, 360);
        
        btnMovable = new JButton("Movable"); btnMovable.setBounds(415,390,90,30);
        btnMovable.addActionListener(this); add(btnMovable, 415, 390); btnMovable.setEnabled(false);
        
        lblX1 = new GLabel("1"); lblX1.setFont(fHead); lblX1.setColor(Color.WHITE); add(lblX1, 10, 125);
        lblX2 = new GLabel("2"); lblX2.setFont(fHead); lblX2.setColor(Color.WHITE); add(lblX2, 10, 165);
        lblX3 = new GLabel("3"); lblX3.setFont(fHead); lblX3.setColor(Color.WHITE); add(lblX3, 10, 205);
        lblX4 = new GLabel("4"); lblX4.setFont(fHead); lblX4.setColor(Color.WHITE); add(lblX4, 10, 245);
        lblX5 = new GLabel("5"); lblX5.setFont(fHead); lblX5.setColor(Color.WHITE); add(lblX5, 10, 285);
        lblX6 = new GLabel("6"); lblX6.setFont(fHead); lblX6.setColor(Color.WHITE); add(lblX6, 10, 325);
        lblX7 = new GLabel("7"); lblX7.setFont(fHead); lblX7.setColor(Color.WHITE); add(lblX7, 10, 365);
        lblX8 = new GLabel("8"); lblX8.setFont(fHead); lblX8.setColor(Color.WHITE); add(lblX8, 10, 405);
        lblY1 = new GLabel("1"); lblY1.setFont(fHead); lblY1.setColor(Color.WHITE); add(lblY1, 45, 90);
        lblY2 = new GLabel("2"); lblY2.setFont(fHead); lblY2.setColor(Color.WHITE); add(lblY2, 85, 90);
        lblY3 = new GLabel("3"); lblY3.setFont(fHead); lblY3.setColor(Color.WHITE); add(lblY3, 125, 90);
        lblY4 = new GLabel("4"); lblY4.setFont(fHead); lblY4.setColor(Color.WHITE); add(lblY4, 165, 90);
        lblY5 = new GLabel("5"); lblY5.setFont(fHead); lblY5.setColor(Color.WHITE); add(lblY5, 205, 90);
        lblY6 = new GLabel("6"); lblY6.setFont(fHead); lblY6.setColor(Color.WHITE); add(lblY6, 245, 90);
        lblY7 = new GLabel("7"); lblY7.setFont(fHead); lblY7.setColor(Color.WHITE); add(lblY7, 285, 90);
        lblY8 = new GLabel("8"); lblY8.setFont(fHead); lblY8.setColor(Color.WHITE); add(lblY8, 325, 90);
        
        makeBoard(); // Creates the Board
	    
	}
	/**
	 * Method for drawing "confetti" when the game ends
	 * Just for creative looks
	 * 
	 */
	public void run() {

		while (true) {
			if (paused) pause(DELAY);
			else {
			// Draw and animate the confetti if the game has ended.
			if (condition == 1) {
				if (drawC) drawConfetti();
				drawC = false;
				animateConfetti(1);
			}
			pause(DELAY); 
			}
		}
	}
	/**
	 * Allows for buttons to cause events to occur if they are pressed.
	 * There are buttons for:
	 * New Game -  a new game to be created and started.
	 * Ply 2 - Set the ply amount to 2 (default is 1)
	 * Ply 3 - Set the ply amount to 3 (default is 1)
	 * Move Order - Moves a specific token in a series of specified jumps using U,L,D,R.
	 * Move-One - Moves the player'sfirst movable piece found in the board (Goes row by row from 1,1 | 2,1 ... | to 8,8)
	 * Max Jumps - Moves the player's token that can make the most jumps in one turn.
	 * Move Optimal - Moves the player's token that is best depending on the heuristic.
	 * Movable - Prints out the player's movable tokens to the text field at the bottom of the GUI
	 * Minimax - Performs Minimax, starting with the tokens of the current turn's player.
	 * MM ABP - Performs Minimax with Alpha-Beta Pruning, starting with the tokens of the current turn's player.
	 * Remove - Ensures the correct player's tokens are removed, following game rules
	 */
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals("New Game")) {
			if (condition != 1) { drawC = true; condition = 1; }
			else { condition = 0; makeBoard(); for (int i = 0 ; i < 700 ; i++) { remove(confetti[i]); }
				strRemovalDir = "Black moves first\nEnter a X,Y coordinate pair and click 'Remove' to remove a piece";
				tfEventArea.setText(strRemovalDir);
				moveCounter = 1; turnCounter = 1;
			}
			btnRemove.setEnabled(true);
			btnMovable.setEnabled(false);
			btnMove1.setEnabled(false);
			btnMaxJumps.setEnabled(false);
			btnMoveOptimal.setEnabled(false);
			btnMiniMax.setEnabled(false);
			btnMMABP.setEnabled(false);
		}
		if (evt.getActionCommand().equals("Ply 2")) {
			btnPly2.setEnabled(false);
			btnPly3.setEnabled(true);
			algorithm.setPlyLimit(2);
		}
		if (evt.getActionCommand().equals("Ply 3")) {
			btnPly2.setEnabled(true);
			btnPly3.setEnabled(false);
			algorithm.setPlyLimit(3);
		}
		if (evt.getActionCommand().equals("Move Order")) {
			algorithm.setBoard(tiles);
			if (!btnMovable.isEnabled()) btnMovable.setEnabled(true);
			if (tfX.getText().trim().isEmpty() || tfY.getText().trim().isEmpty()) {
				tfEventArea.setText(strRemovalDir + "\nError: Please add an X, Y Coordinate");
			}
			else if (tfX.getText().length() > 1 || tfY.getText().length() > 1) tfEventArea.setText(strRemovalDir + "\nError: X, Y coordinates must be a single character.");
			else if (!Character.isDigit(tfX.getText().charAt(0)) || !Character.isDigit(tfY.getText().charAt(0))) {
				tfEventArea.setText(strRemovalDir + "\nError: X, Y Coordinates must be a number between 1 & 8 (Inclusive)");
			}
			else if (Integer.parseInt(tfX.getText()) > 8 || Integer.parseInt(tfX.getText()) < 1 ||
					Integer.parseInt(tfY.getText()) > 8 || Integer.parseInt(tfY.getText()) < 1) {
				tfEventArea.setText(strRemovalDir + "\nError: X, Y Coordinates must be a number between 1 & 8 (Inclusive)");
			}
			else {
				int x = Integer.parseInt(tfX.getText()) - 1;
				int y = Integer.parseInt(tfY.getText()) - 1;
				if (moveCounter % 2 == 1) {
					if (tiles[y][x].id == 1) {
						String movesOrder = tfMoveOrder.getText();
						CoordinatePair moveOther = new CoordinatePair(1,y,x,movesOrder.length(),movesOrder);
						algorithm.moveMax(moveOther);
						tfX.setText(""); tfY.setText(""); tfMoveOrder.setText("");
					}
					else {
						tfEventArea.setText(tfEventArea.getText() + "\nError: Coordinate selected did not contain a Black Piece.");
					}
				}
				else {
					if (tiles[y][x].id == 0) {
						String movesOrder = tfMoveOrder.getText();
						CoordinatePair moveOther = new CoordinatePair(0,y,x,movesOrder.length(),movesOrder);
						algorithm.moveMax(moveOther);
						tfX.setText(""); tfY.setText(""); tfMoveOrder.setText("");
					}
					else {
						tfEventArea.setText(tfEventArea.getText() + "\nError: Coordinate selected did not contain a White Piece.");
					}
				}
			}
		}

		if (evt.getActionCommand().equals("Move-One")) {
			if (!btnMovable.isEnabled()) btnMovable.setEnabled(true);
			algorithm.setBoard(tiles);
			if (moveCounter % 2 == 1) algorithm.moveOne(1);
			else {
				algorithm.moveOne(0);
			}			
		}
		if (evt.getActionCommand().equals("Max Jumps")) {
			algorithm.setBoard(tiles);
			if (!btnMovable.isEnabled()) btnMovable.setEnabled(true);
			ArrayList<CoordinatePair> jumperList = new ArrayList<CoordinatePair>();
			boolean tokenMoved = false;
			// Creates a CoordinatePair Object for eahc movable piece with their longest path
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {
					if (moveCounter % 2 == 1) {
						if (tiles[y][x].id == 1) {
							Tile[][] tilesClone = new Tile[8][8];
							for (int yC = 0; yC < 8; yC++) {
								for (int xC = 0; xC < 8; xC++) {
									tilesClone[yC][xC] = new Tile(40,40, tiles[yC][xC].id);
								}
							}
							int player = 1, opponent = 0; 
							int maxJumps = algorithm.moveMostJumps(player, opponent, tilesClone, y, x, 0, 0, "");
							if (maxJumps > 0) tokenMoved = true;
							jumperList.add(new CoordinatePair(player, y,x, maxJumps, algorithm.getJumpedPath()));
							algorithm.clearPath();
						}
					}
					else {
						if (tiles[y][x].id == 0) {
							Tile[][] tilesClone = new Tile[8][8];
							for (int yC = 0; yC < 8; yC++) {
								for (int xC = 0; xC < 8; xC++) {
									tilesClone[yC][xC] = new Tile(40,40, tiles[yC][xC].id);
								}
							}
							int player = 0, opponent = 1; 
							int maxJumps = algorithm.moveMostJumps(player, opponent, tilesClone, y, x, 0, 0, "");
							if (maxJumps > 0) tokenMoved = true;
							jumperList.add(new CoordinatePair(player, y, x, maxJumps, algorithm.getJumpedPath()));
							algorithm.clearPath();
						}
					}
				}
			}
			if (tokenMoved) {
				CoordinatePair maxToken = Collections.max(jumperList, Comparator.comparing(s -> s.jumps));
				algorithm.setBoard(tiles);
				algorithm.moveMax(maxToken);
			}
			// If no tokens can move, game over.
			else {
				if (moveCounter % 2 == 1) gameOver(1);
				else { gameOver(0); }
				
			}
		}
		if (evt.getActionCommand().equals("Move Optimal")) {
			algorithm.setBoard(tiles);
			if (!btnMovable.isEnabled()) btnMovable.setEnabled(true);
			ArrayList<CoordinatePair> jumperList = new ArrayList<CoordinatePair>();
			boolean tokenMoved = false;
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {
					if (moveCounter % 2 == 1) {
						if (tiles[y][x].id == 1) {
							Tile[][] tilesClone = new Tile[8][8];
							for (int yC = 0; yC < 8; yC++) {
								for (int xC = 0; xC < 8; xC++) {
									tilesClone[yC][xC] = new Tile(40,40, tiles[yC][xC].id);
								}
							}
							int player = 1, opponent = 0; 
							int bestJumps = algorithm.moveLeastMoves(true, player, opponent, tilesClone, y, x, 0, 0, "", 100);
							if (bestJumps != 100) tokenMoved = true;
							jumperList.add(new CoordinatePair(player, y,x, bestJumps, algorithm.optimalPath()));
							algorithm.clearOptimal();
						}
					}
					else {
						if (tiles[y][x].id == 0) {
							Tile[][] tilesClone = new Tile[8][8];
							for (int yC = 0; yC < 8; yC++) {
								for (int xC = 0; xC < 8; xC++) {
									tilesClone[yC][xC] = new Tile(40,40, tiles[yC][xC].id);
								}
							}
							int player = 0, opponent = 1; 
							int bestJumps = algorithm.moveLeastMoves(true, player, opponent, tilesClone, y, x, 0, 0, "", 100);
							if (bestJumps != 100) tokenMoved = true;
							jumperList.add(new CoordinatePair(player, y, x, bestJumps, algorithm.optimalPath()));
							algorithm.clearOptimal();
						}
					}
				}
			}
			if (tokenMoved) {
				CoordinatePair maxToken = Collections.min(jumperList, Comparator.comparing(s -> s.jumps));
				algorithm.setBoard(tiles);
				algorithm.moveMax(maxToken);
			}
			// If no tokens can move, game over.
			else {
				if (moveCounter % 2 == 1) gameOver(1);
				else { gameOver(0); }
				
			}
		}
		if (evt.getActionCommand().equals("Movable")) {
			int plr = 0;
			int opp = 1;
			String strPlr = "";
			if (moveCounter % 2 == 1) { plr = 1; opp = 0; strPlr += "\nPlayer Black Movable Pieces: ["; }
			else { strPlr += "\nPlayer White Movable Pieces: ["; }
		
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {
					if (tiles[y][x].id == plr) {
						boolean valid = false;
						if (y - 2 >= 0) {
							if (tiles[y-1][x].id == opp && tiles[y-2][x].id == 2) valid = true;
						}
						if (y + 2 <= 7 && !valid) {
							if (tiles[y+1][x].id == opp && tiles[y+2][x].id == 2) valid = true;
						}
						if (x - 2 >= 0 && !valid) {
							if (tiles[y][x-1].id == opp && tiles[y][x-2].id == 2) valid = true;
						}
						if (x + 2 <= 7 && !valid) {
							if (tiles[y][x+1].id == opp && tiles[y][x+2].id == 2) valid = true;
						}
						if (valid) strPlr += "("+(x+1)+","+(y+1)+"),";
					}
				}
			}
			if (strPlr.charAt(strPlr.length()-1) == ',') strPlr = strPlr.substring(0,strPlr.length()-1);
			strPlr+= "]";
			tfEventArea.setText(tfEventArea.getText() + strPlr);
			btnMovable.setEnabled(false);
			algorithm.setBoard(tiles);
		}
		if (evt.getActionCommand().equals("Minimax")) {
			btnMovable.setEnabled(true);
			algorithm.setBoard(tiles);
			String bestMove = "";
			CoordinatePair tokenToMove = null;
			if (moveCounter % 2 == 1) { 
				algorithm.miniMax(tiles, 1); 
				CoordinatePair bestMaxToken = algorithm.getBestMaxToken();
				tokenToMove = bestMaxToken;
				// If no tokens can move, game over.
				if (tokenToMove == null) gameOver(1);
				else {
					bestMove = bestMaxToken.jumpPath;
					algorithm.moveMax(tokenToMove);
				}		
			}
			else { 
				algorithm.miniMax(tiles, 0); 
				CoordinatePair bestMinToken = algorithm.getBestMinToken();
				tokenToMove = bestMinToken;
				// If no tokens can move, game over.
				if (tokenToMove == null) gameOver(0);
				else {
					bestMove = bestMinToken.jumpPath;
					algorithm.moveMax(tokenToMove);
				}
			}		
		}
		if (evt.getActionCommand().equals("MM ABP")) {
			btnMovable.setEnabled(true);
			algorithm.setBoard(tiles);
			String bestMove = "";
			CoordinatePair tokenToMove = null;
			if (moveCounter % 2 == 1) { 
				algorithm.miniMaxABP(tiles, 1); 
				CoordinatePair bestMaxToken = algorithm.getBestMaxToken();
				tokenToMove = bestMaxToken;
				// If no tokens can move, game over.
				if (tokenToMove == null) gameOver(1); 
				else {
					bestMove = bestMaxToken.jumpPath;
					algorithm.moveMax(tokenToMove);
				}		
			}
			else { 
				algorithm.miniMaxABP(tiles, 0); 
				CoordinatePair bestMinToken = algorithm.getBestMinToken();
				tokenToMove = bestMinToken;
				// If no tokens can move, game over.
				if (tokenToMove == null) gameOver(0);
				else {
					bestMove = bestMinToken.jumpPath;
					algorithm.moveMax(tokenToMove);
				}
			}
		}
		if (evt.getActionCommand().equals("Remove")) {
			if (tfX.getText().trim().isEmpty() || tfY.getText().trim().isEmpty()) {
				tfEventArea.setText(strRemovalDir + "\nError: Please add an X, Y Coordinate");
			}
			else if (tfX.getText().length() > 1 || tfY.getText().length() > 1) tfEventArea.setText(strRemovalDir + "\nError: X, Y coordinates must be a single character.");
			else if (!Character.isDigit(tfX.getText().charAt(0)) || !Character.isDigit(tfY.getText().charAt(0))) {
				tfEventArea.setText(strRemovalDir + "\nError: X, Y Coordinates must be a number between 1 & 8 (Inclusive)");
			}
			else if (Integer.parseInt(tfX.getText()) < 1 || Integer.parseInt(tfX.getText()) > 8
					|| Integer.parseInt(tfY.getText()) < 1 || Integer.parseInt(tfY.getText()) > 8) {
				tfEventArea.setText(strRemovalDir + "\nError: X, Y Coordinates must be a number between 1 & 8 (Inclusive)");
			}
			else {
				if (moveCounter == 1) {
					
					int x = Integer.parseInt(tfX.getText()) - 1;
					int y = Integer.parseInt(tfY.getText()) - 1;
					if (tiles[y][x].id == 1) {
						
						strRemovalDir += "\n------------------------ Turn " + turnCounter + " ------------------------\n"
								  	  +  "------------------------ Move " + moveCounter + " ------------------------\n"
								  	  +  "Player Black removed Token at (" + tfX.getText() + "," +tfY.getText() + ")\n"
								  	  +  "Player White must remove a token adjacent to (" + tfX.getText() + "," +tfY.getText() + ")";
						
						tfEventArea.setText(strRemovalDir);	
						tfX.setText(""); tfY.setText("");
						removedX = x; removedY = y;
						tiles[y][x].setTile(2);
						tiles[y][x].id = 2;
						moveCounter++;
					}
					else {
						tfEventArea.setText(strRemovalDir + "\nError: Coordinate selected did not contain a Black Piece.");
					}
				}
				else if (moveCounter == 2) {
					int x = Integer.parseInt(tfX.getText()) - 1;
					int y = Integer.parseInt(tfY.getText()) - 1;
					if (tiles[y][x].id == 0) {
						if (x + 1 >= removedX && x - 1 <= removedX) {
							if (y + 1 >= removedY && y - 1 <= removedY) {
								strRemovalDir += "\n------------------------ Move " + moveCounter + " ------------------------\n"
										  +  "Player White removed Token at (" + tfX.getText() + "," +tfY.getText() + ")";
								tfEventArea.setText(strRemovalDir);
								tfX.setText(""); tfY.setText("");
								tiles[y][x].setTile(2);
								tiles[y][x].id = 2;
								moveCounter++;
								// Btn Enables and Disables
								btnRemove.setEnabled(false);
								btnMovable.setEnabled(true);
								btnPly2.setEnabled(true);
								btnPly3.setEnabled(true);
								btnMove1.setEnabled(true);
								btnMaxJumps.setEnabled(true);
								btnMoveOrder.setEnabled(true);
								btnMoveOptimal.setEnabled(true);
								btnMiniMax.setEnabled(true);
								btnMMABP.setEnabled(true);
							}
							else {
								tfEventArea.setText(strRemovalDir + "\nError: Y Coordinate not adjacent"); 
							}
						}
						else {
							tfEventArea.setText(strRemovalDir + "\nError: X Coordinate not adjacent"); 
						}	
					}
					else {
						tfEventArea.setText(strRemovalDir + "\nError: Coordinate selected did not contain a White Piece.");
					}
				}
			}
		}
	}
	
	/**
	 * Draws and illustrates the board
	 */
	public void makeBoard() {
		// First clear the current boards to ensure the previous game's board is not used
		tilePieces.clear(); passableList.clear();
		boolean other = true;
		// Clears the double array if the values of the old board are still contained
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				if (tiles[y][x] != null) remove(tiles[y][x]);
			}
		}
		// Populates the new board with a full token board.
		for (int y = 0; y < 8; y++) {
	    	tilePieces.add(new ArrayList<Tile>());
	    	passableList.add(new ArrayList<Integer>());
	    	for (int x = 0; x < 8; x++) {
	    		if (other) { other = false; tilePieces.get(y).add(new Tile (40,40, 1)); tiles[y][x] = new Tile(40,40, 1); passableList.get(y).add(1); }
	    		else { other = true; tilePieces.get(y).add(new Tile (40,40, 0)); tiles[y][x] = new Tile(40,40, 0);  passableList.get(y).add(0);}
	    		add(tiles[y][x], 30 + (x*40), 100 + y*40 );
	    	}
	    	// Boolean statements to ensure the row patterns flip for white and black tokens.
	    	if (other) other = false;
	    	else other = true;
		}
	}
	
	/**
	 * Prints out the moves that were made, the turn number, and the move number for that iteration.
	 * @param move The move string to be printed to the textfield.
	 */
	public void setMoveText(String move) {
		String strTurn = "";
		if (moveCounter % 2 == 1) {
			turnCounter++;
			strTurn = "\n------------------------ Turn " + turnCounter + " ------------------------";
		}
		String strMove = "\n------------------------ Move " + moveCounter + " ------------------------\n" + move;
		moveCounter++;
		tfEventArea.setText(tfEventArea.getText() + strTurn + strMove);
	}
	/**
	 * Sets the board following the Algorithm Class' execution.
	 * @param newBoard the board based upon the executed method from the algorithm class.
	 */
	public void setPostMoveBoard(Tile[][] newBoard) { tiles = newBoard; }
	
	/**
	 * Method for stopping the game if a terminal board state is reached
	 * Updates the text field with the statistics and info on the game winner
	 * @param losingPlayer ID of the losing player
	 */
	public void gameOver(int losingPlayer) {
		btnMove1.setEnabled(false);
		btnMovable.setEnabled(false);
		btnRemove.setEnabled(false);
		btnMaxJumps.setEnabled(false);
		btnMoveOrder.setEnabled(false);
		btnMoveOptimal.setEnabled(false);
		btnMiniMax.setEnabled(false);
		btnMMABP.setEnabled(false);
		condition = 1; drawC = true;
		if (losingPlayer == 0) {
			tfEventArea.setText(tfEventArea.getText() + "\nPlayer White has no moves!\nPlayer Black won the game!");
		}
		else {
			tfEventArea.setText(tfEventArea.getText() + "\nPlayer Black has no moves!\nPlayer White won the game!");
		}
		// Times the Evaluation Function was called
		int evalCount = algorithm.evaluationCount;
		// Times a branch was pruned
		int cutCount = algorithm.cutoffCount;
		// Number of branches
		int totalBranches = algorithm.branchesCount;
		// Times a group of branches were made
		int branchLists = algorithm.branchListCount;
		// Average branching factor calculation
		float avgBranches = (float)totalBranches / branchLists;
		tfEventArea.setText(tfEventArea.getText() + "\nEvaluations Done: " + evalCount +
							"\nTimes Pruned: " + cutCount +
							"\nBraches Total: " + totalBranches +
							"\nBranch Lists Made: " + branchLists +
							"\nBranch Average: " + avgBranches);
	}
	/**
	 * Extra aesthetic method for drawing confetti at the end of the game
	 */
	public void drawConfetti() {
		for (int i = 0 ; i < 700 ; i++) {
			confetti[i] = new GOval(0,0); confetti[i].setSize(6, 6);  confetti[i].setFilled(true);  confetti[i].setFillColor(rand.nextColor());
			confetti[i].setColor(Color.BLACK); add(confetti[i], rand.nextDouble(-400, 550), rand.nextDouble(-450, -10));  
		}
	}
	/**
	 * Extra aesthetic method for animating confetti at the end of the game
	 * @param cond 1 if the game is over, 0 if not
	 */
	public void animateConfetti(int cond) {
		for (int i = 0 ; i < 700 ; i++) {
			if (cond == 1) {
				confetti[i].move(rand.nextDouble(-3, 3), .3);
				if (confetti[i].getY() >= 420) confetti[i].setLocation(rand.nextDouble(-400, 600), rand.nextDouble(-10, -100)); 
			}
		}
	}
}

