import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Scanner;

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

	int width, height;
	
	// Aesthetic Components
	Font fHead = new Font("Serif", Font.BOLD, 16); Font fStat = new Font("Serif", Font.BOLD, 12);
	Color bg = new Color(209,209,209);
    Color bar = new Color(193,85,1);
    Color snow = new Color(180,212,255);
    private Color menuColor = new Color(52, 63, 71);
    GImage imgTitle = new GImage("../Images/TitleLogo.png");
    
    // G Components 
    JButton btnCommit, btnNG, btnTL, btnFL, btnMovable, btnMove1;
    JTextField tfWord, tfX, tfY;
	JTextArea tfEventArea;
	JScrollPane eventPane;
	
	GLabel lblX0, lblX1, lblX2, lblX3, lblX4, lblX5, lblX6, lblX7;
	GLabel lblY0, lblY1, lblY2, lblY3, lblY4, lblY5, lblY6, lblY7;
	
	GOval[] confetti = new GOval[700];
	
	// Array and List Values
	ArrayList<ArrayList<Tile>> tilePieces = new ArrayList<ArrayList<Tile>>();
	ArrayList<ArrayList<Integer>> passableList = new ArrayList<ArrayList<Integer>>();
	Tile[][] tiles = new Tile[8][8];
	
	// Additional Utils
	Scanner reader = new Scanner(System.in);  // Reading from System.in
	private RandomGenerator rand = new RandomGenerator();
	
	private static final double DELAY = 1;
	
	// Termination Logic & Aesthetics
	boolean drawC = true;
	int condition = 0;
	boolean paused = false;
	
	// Game Logic
	String strRemovalDir = "Black moves first\nEnter a X,Y coordinate pair and click 'Commit' to remove a piece";
	int moveCounter = 1;
	int turnCounter = 1;
	int removedX, removedY;

	public void init() {
		this.setSize(500,600); width = getWidth(); height = getHeight();
	    setBackground(menuColor); 
	    Frame c = (Frame)this.getParent().getParent(); c.setTitle("KONANE");
	    GRect rectTop = new GRect(0,0,600,70); rectTop.setFilled(true); rectTop.setFillColor(bar); rectTop.setColor(bar); add(rectTop);
        GRect rectBot = new GRect(0, 430, 600, 170); rectBot.setFilled(true); rectBot.setFillColor(bar); rectBot.setColor(bar); add(rectBot);
	    add(imgTitle, 1, 1);
	    
	    tfEventArea = new JTextArea(10, 10); tfEventArea.setLineWrap(true); tfEventArea.setWrapStyleWord(true);
        tfEventArea.setEditable(false); eventPane = new JScrollPane(tfEventArea); eventPane.setBounds(10, 440, 475, 150);
        eventPane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK)); add(eventPane, 12, 440);
        tfEventArea.setText(strRemovalDir);
        
        GLabel lblCoords = new GLabel("To Move: (     ,     )"); lblCoords.setColor(Color.WHITE);
        add(lblCoords, 355, 200); lblCoords.setFont(fHead);
        
        tfX = new JTextField(1); add(tfX, 430, 183); tfY = new JTextField(1); add(tfY, 451, 183);
        
        btnCommit = new JButton("Commit"); btnCommit.setBounds(380,210,80,30);
        btnCommit.addActionListener(this); add(btnCommit, 380,220); 
        
        btnMove1 = new JButton("Move-One"); btnMove1.setBounds(370,255, 100,30);
        btnMove1.addActionListener(this); add(btnMove1, 370,260); 
        
        btnTL = new JButton("Turn Log");  btnTL.setBounds(345,360,80,30);
        btnTL.addActionListener(this); add(btnTL, 345, 360); btnTL.setEnabled(false);

        btnFL = new JButton("Full Log"); btnFL.setBounds(345,390,80,30);
        btnFL.addActionListener(this); add(btnFL, 345, 390); btnFL.setEnabled(false);
        
        btnNG = new JButton("New Game"); btnNG.setBounds(415,360,90,30);  btnNG.addActionListener(this);
        btnNG.setToolTipText("Create a New Game"); add(btnNG, 415, 360);
        
        btnMovable = new JButton("Movable"); btnMovable.setBounds(415,390,90,30);
        btnMovable.addActionListener(this); add(btnMovable, 415, 390); btnMovable.setEnabled(false);
        
        lblX0 = new GLabel("0"); lblX0.setFont(fHead); lblX0.setColor(Color.WHITE); add(lblX0, 10, 125);
        lblX1 = new GLabel("1"); lblX1.setFont(fHead); lblX1.setColor(Color.WHITE); add(lblX1, 10, 165);
        lblX2 = new GLabel("2"); lblX2.setFont(fHead); lblX2.setColor(Color.WHITE); add(lblX2, 10, 205);
        lblX3 = new GLabel("3"); lblX3.setFont(fHead); lblX3.setColor(Color.WHITE); add(lblX3, 10, 245);
        lblX4 = new GLabel("4"); lblX4.setFont(fHead); lblX4.setColor(Color.WHITE); add(lblX4, 10, 285);
        lblX5 = new GLabel("5"); lblX5.setFont(fHead); lblX5.setColor(Color.WHITE); add(lblX5, 10, 325);
        lblX6 = new GLabel("6"); lblX6.setFont(fHead); lblX6.setColor(Color.WHITE); add(lblX6, 10, 365);
        lblX7 = new GLabel("7"); lblX7.setFont(fHead); lblX7.setColor(Color.WHITE); add(lblX7, 10, 405);
        lblY0 = new GLabel("0"); lblY0.setFont(fHead); lblY0.setColor(Color.WHITE); add(lblY0, 45, 90);
        lblY1 = new GLabel("1"); lblY1.setFont(fHead); lblY1.setColor(Color.WHITE); add(lblY1, 85, 90);
        lblY2 = new GLabel("2"); lblY2.setFont(fHead); lblY2.setColor(Color.WHITE); add(lblY2, 125, 90);
        lblY3 = new GLabel("3"); lblY3.setFont(fHead); lblY3.setColor(Color.WHITE); add(lblY3, 165, 90);
        lblY4 = new GLabel("4"); lblY4.setFont(fHead); lblY4.setColor(Color.WHITE); add(lblY4, 205, 90);
        lblY5 = new GLabel("5"); lblY5.setFont(fHead); lblY5.setColor(Color.WHITE); add(lblY5, 245, 90);
        lblY6 = new GLabel("6"); lblY6.setFont(fHead); lblY6.setColor(Color.WHITE); add(lblY6, 285, 90);
        lblY7 = new GLabel("7"); lblY7.setFont(fHead); lblY7.setColor(Color.WHITE); add(lblY7, 325, 90);
        
        makeBoard();
	    addMouseListeners();
	    addKeyListeners();
	    
	}
	
	public void run() {

		while (true) {
			if (paused) pause(DELAY);
			else {
			if (condition == 1) {
				if (drawC) drawConfetti();
				drawC = false;
				animateConfetti(1);
			}
			pause(DELAY); 
			}
		}
	}
	
	
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals("New Game")) {
			if (condition != 1) { drawC = true; condition = 1; }
			else { condition = 0; for (int i = 0 ; i < 700 ; i++) remove(confetti[i]); }
		}
		if (evt.getActionCommand().equals("Turn Log")) {
			
		}
		if (evt.getActionCommand().equals("Full Log")) {

		}
		if (evt.getActionCommand().equals("Move-One")) {
			
		}
		if (evt.getActionCommand().equals("Movable")) {
			if (moveCounter % 2 == 1) {
				String strBlack = "\nPlayer Black Movable Pieces: [";
				//tiles[0][2].setTile(2);
				for (int y = 0; y < 8; y++) {
					for (int x = 0; x < 8; x++) {
						if (tiles[y][x].id == 1) {
							//System.out.print("Checking: (" + x + ", " + y + ") ");
							boolean valid = false;
							if (y - 2 >= 0) {
								if (tiles[y-1][x].id == 0 && tiles[y-2][x].id == 2) { 
									valid = true;
									strBlack+= "("+x+","+y+"),";
								}
							}
							if (y + 2 <= 7 && !valid) {
								if (tiles[y+1][x].id == 0 && tiles[y+2][x].id == 2) { 
									valid = true;
									strBlack+= "("+x+","+y+"),";
								}
							}
							if (x - 2 >= 0 && !valid) {
								if (tiles[y][x-1].id == 0 && tiles[y][x-2].id == 2) { 
									valid = true;
									strBlack+= "("+x+","+y+"),";
								}
							}
							if (x + 2 <= 7 && !valid) {
								if (tiles[y][x+1].id == 0 && tiles[y][x+2].id == 2) { 
									valid = true;
									strBlack += "("+x+","+y+"),";
								}
							}
						}
					}
				}
				if (strBlack.charAt(strBlack.length()-1) == ',') strBlack = strBlack.substring(0,strBlack.length()-1);
				strBlack+= "]";
				tfEventArea.setText(tfEventArea.getText() + strBlack);
				btnMovable.setEnabled(false);
			}
			else {
				String strWhite = "\nPlayer White Movable Pieces: [";
				//tiles[0][2].setTile(2);
				for (int y = 0; y < 8; y++) {
					for (int x = 0; x < 8; x++) {
						if (tiles[y][x].id == 0) {
							//System.out.print("Checking: (" + x + ", " + y + ") ");
							boolean valid = false;
							if (y - 2 >= 0) {
								if (tiles[y-1][x].id == 1 && tiles[y-2][x].id == 2) { 
									valid = true;
									strWhite+= "("+x+","+y+"),";
								}
							}
							if (y + 2 <= 7 && !valid) {
								if (tiles[y+1][x].id == 1 && tiles[y+2][x].id == 2) { 
									valid = true;
									strWhite+= "("+x+","+y+"),";
								}
							}
							if (x - 2 >= 0 && !valid) {
								if (tiles[y][x-1].id == 1 && tiles[y][x-2].id == 2) { 
									valid = true;
									strWhite+= "("+x+","+y+"),";
								}
							}
							if (x + 2 <= 7 && !valid) {
								if (tiles[y][x+1].id == 1 && tiles[y][x+2].id == 2) { 
									valid = true;
									strWhite += "("+x+","+y+"),";
								}
							}
						}
					}
				}
				if (strWhite.charAt(strWhite.length()-1) == ',') strWhite = strWhite.substring(0,strWhite.length()-1);
				strWhite+= "]";
				tfEventArea.setText(tfEventArea.getText() + strWhite);
				btnMovable.setEnabled(false);
			}
		}
		if (evt.getActionCommand().equals("Commit")) {
			if (tfX.getText().trim().isEmpty() || tfY.getText().trim().isEmpty()) {
				tfEventArea.setText(strRemovalDir + "\nError: Please add an X, Y Coordinate");
			}
			else if (tfX.getText().length() > 1 || tfY.getText().length() > 1) tfEventArea.setText(strRemovalDir + "\nError: X, Y coordinates must be a single character.");
			else if (!Character.isDigit(tfX.getText().charAt(0)) || !Character.isDigit(tfY.getText().charAt(0))) {
				tfEventArea.setText(strRemovalDir + "\nError: X, Y Coordinates must be a number between 0 & 7 (Inclusive)");
			}
			else if (Integer.parseInt(tfX.getText()) > 7 || Integer.parseInt(tfY.getText()) > 7) {
				tfEventArea.setText(strRemovalDir + "\nError: X, Y Coordinates must be a number between 0 & 7 (Inclusive)");
			}
			else {
				if (moveCounter == 1) {
					
					int x = Integer.parseInt(tfX.getText());
					int y = Integer.parseInt(tfY.getText());
					if (tiles[y][x].id == 1) {
						
						strRemovalDir += "\n-----------------------------------------------\n"
								  	  +  "Move " + Integer.toString(moveCounter) + "\n"
								  	  +  "Player Black removed Token at (" + tfX.getText() + "," +tfY.getText() + ")\n"
								  	  +  "Player White must remove a token adjacent to (" + tfX.getText() + "," +tfY.getText() + ")";
						
						tfEventArea.setText(strRemovalDir);	
						tfX.setText(""); tfY.setText("");
						removedX = x; removedY = y;
						tiles[y][x].setTile(2);
						moveCounter++;
					}
					else {
						tfEventArea.setText(strRemovalDir + "\nError: Coordinate selected did not contain a Black Piece.");
					}
				}
				else if (moveCounter == 2) {
					int x = Integer.parseInt(tfX.getText());
					int y = Integer.parseInt(tfY.getText());
					if (tiles[y][x].id == 0) {
						if (x + 1 >= removedX && x - 1 <= removedX) {
							if (y + 1 >= removedY && y - 1 <= removedY) {
								strRemovalDir += "\n-----------------------------------------------\n"
										  +  "Move " + Integer.toString(moveCounter) + "\n"
										  +  "Player White removed Token at (" + tfX.getText() + "," +tfY.getText() + ")";
								tfEventArea.setText(strRemovalDir);
								tfX.setText(""); tfY.setText("");
								tiles[y][x].setTile(2);
								moveCounter++;
								turnCounter++;
								btnMovable.setEnabled(true);
								btnTL.setEnabled(true);
								btnFL.setEnabled(true);
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
	
	
	public void makeBoard() {
		tilePieces.clear();
		passableList.clear();
		boolean other = true;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				if (tiles[y][x] != null) remove(tiles[y][x]);
			}
		}
		for (int y = 0; y < 8; y++) {
	    	tilePieces.add(new ArrayList<Tile>());
	    	passableList.add(new ArrayList<Integer>());
	    	for (int x = 0; x < 8; x++) {
	    		 
	    		if (other) { other = false; tilePieces.get(y).add(new Tile (40,40, 1)); tiles[y][x] = new Tile(40,40, 1); passableList.get(y).add(1); }
	    		else { other = true; tilePieces.get(y).add(new Tile (40,40, 0)); tiles[y][x] = new Tile(40,40, 0);  passableList.get(y).add(0);}
	    		add(tiles[y][x], 30 + (x*40), 100 + y*40 );
	    	}
	    	if (other) other = false;
	    	else other = true;
		}
	}
	
	public void drawConfetti() {
		for (int i = 0 ; i < 700 ; i++) {
			confetti[i] = new GOval(0,0); confetti[i].setSize(6, 6);  confetti[i].setFilled(true);  confetti[i].setFillColor(rand.nextColor());
			confetti[i].setColor(Color.BLACK); add(confetti[i], rand.nextDouble(-400, 550), rand.nextDouble(-450, -10));  
		}
	}
	public void animateConfetti(int cond) {
		
		int endCond = cond;
		for (int i = 0 ; i < 700 ; i++) {
			if (endCond == 1) {
				confetti[i].move(rand.nextDouble(-3, 3), .3);
				if (confetti[i].getY() >= 420) confetti[i].setLocation(rand.nextDouble(-400, 600), rand.nextDouble(-10, -100)); 
			}
		}
	}
}
