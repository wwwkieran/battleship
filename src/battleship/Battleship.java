package battleship;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*; // for Stack

@SuppressWarnings("serial")

public class Battleship extends Applet implements ActionListener {


	protected Button startButton;
	protected Button[][] playerBoardButtons;
	protected Button[][] opponentBoardButtons;
	protected boardCanvas leftCanvas;
	protected boardCanvas rightCanvas;
	protected passCanvas notifCanvas;
	protected homeCanvas startCanvas;
	protected int currentPlayer; //In two player mode, 1 = player 1 and 2 = player 2
	protected int gameType; // 1-player = 1; 2-player = 2
	protected final int ONE_PLAYER_GAME = 1;
	protected final int TWO_PLAYER_GAME = 2;
	protected boolean gameOver;
	protected boolean shipsAdded;

	public void init() {
		leftCanvas = new boardCanvas(this);
		rightCanvas = new boardCanvas(this);
		notifCanvas = new passCanvas(this);
		startCanvas = new homeCanvas(this);
		startScreen();
	}
	
	public void startScreen() {
		removeAll();
		setLayout(new GridLayout(1,1));
		add(startCanvas);
		revalidate();
	}
	
	public void playTwoPlayerGame() {
		leftCanvas.reset();
		rightCanvas.reset();
		currentPlayer = 0;
		gameOver = false;
		shipsAdded = false;
		gameType = TWO_PLAYER_GAME;
		nextTurn();
		
	}
	
	public void playOnePlayerGame() {
		leftCanvas.reset();
		rightCanvas.reset();
		currentPlayer = 0;
		gameOver = false;
		shipsAdded = false;
		gameType = ONE_PLAYER_GAME;
		nextTurn();
	}

	public void presentGame() {
		
		if (!shipsAdded) {
			removeAll();
			setLayout(new GridLayout(1,1));
			leftCanvas.addShips();
			add(leftCanvas);
			revalidate();
			if (currentPlayer == 2) {
				shipsAdded = true;
			}
		} else {//Play game
			removeAll();
			setLayout(new GridLayout(2,1));
			leftCanvas.makePlayerScreen();
			rightCanvas.makeOpponentScreen();
			add(leftCanvas);
			add(rightCanvas);
			revalidate();
			}
		

	}

	public void nextTurn() {
		switch (currentPlayer) {
			case 0: currentPlayer = 1; break;
			case 1: currentPlayer = 2; break;
			case 2: currentPlayer = 1; break;
		}
		//Show Message
		removeAll();
		setLayout(new GridLayout(1,1));
		notifCanvas.pass(currentPlayer);
		add(notifCanvas);
		revalidate();
		//Swap Canvas Locations
		boardCanvas temp = leftCanvas;
		leftCanvas = rightCanvas;
		rightCanvas = temp;

	}

	public void win() {
		//called by a canvas when a win has occurred
		gameOver = true;
		//Show Message
		removeAll();
		setLayout(new GridLayout(1,1));
		notifCanvas.win(currentPlayer);
		add(notifCanvas);
		revalidate();
	}

	
	public void actionPerformed(ActionEvent e) {

	}




}
@SuppressWarnings("serial")

class boardCanvas extends Canvas implements MouseListener {
	protected Board board;
	protected int NUM_COLUMNS = 10;
	protected int NUM_ROWS = 10;
	protected int WIDTH_RECTANGLE = 30;
	protected int HEIGHT_RECTANGLE = 30;
	protected int TEXT_BAR_HEIGHT = 15;
	protected int SMALL_BOX_EDGE = 15;
	protected int state;
	protected final int END_STATE = 0;
	protected final int PLAYER_STATE = 1;
	protected final int OPPONENT_STATE = 2;
	protected final int ADD_STATE1 = 3;
	protected final int ADD_STATE2 = 4;
	protected Battleship parent;
	protected String currentTopBarText = "";
	protected String currentBottomBarText = "";
	protected boolean end;
	protected boolean gameOver;
	protected int leftPadding;
	protected final int NUM_SHIPS = 5;
	protected final int[] SHIP_LENGTHS = {5,4,3,3,2};
	protected int currentShip;
	protected int[] currentShipStartCoords;
	


	public boardCanvas(Battleship b) {
		board = new Board();
		addMouseListener(this);
		parent = b;
		state = -1;
	}



	public void paint(Graphics g) {
		Dimension d = getSize();

		//Determine padding of board
		leftPadding = 30;
				
		//Draw top part
		g.setColor(Color.white);
		g.fillRect(0, 0, d.width, d.height);


	    g.setFont(new Font("TimesRoman", Font.BOLD, 15));
		g.setColor(Color.black);
		g.drawString(currentTopBarText, leftPadding, TEXT_BAR_HEIGHT - 3);


		//Paint Board
		for (int y = 0; y < NUM_COLUMNS; y++) {
			for (int x = 0; x < NUM_ROWS; x++) {
				switch(board.get(x, y)) {
					case 0: g.setColor(Color.blue); //Ocean
							break;
					case 1: if (state != OPPONENT_STATE) {
								g.setColor(Color.gray); //Ship
							} else {
								g.setColor(Color.blue); //Ships hidden to opponent
							}
							break;
					case 2: g.setColor(Color.red); //Hit
							break;
					case 3: g.setColor(Color.white); //Miss
							break;
					case 4: g.setColor(Color.black); //Sunk
							break;
				}
				if (state == ADD_STATE2 && x == currentShipStartCoords[0] && y == currentShipStartCoords[1]) {
					g.setColor(Color.cyan);
				}
				g.fillRect(leftPadding + (x * WIDTH_RECTANGLE), y * HEIGHT_RECTANGLE + TEXT_BAR_HEIGHT, WIDTH_RECTANGLE, HEIGHT_RECTANGLE);
				g.setColor(Color.black);
				g.drawRect(leftPadding + (x * WIDTH_RECTANGLE), y * HEIGHT_RECTANGLE + TEXT_BAR_HEIGHT, WIDTH_RECTANGLE, HEIGHT_RECTANGLE);
			}
		}

		//Draw bottom part
		g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
		g.setColor(Color.black);
		g.drawString(currentBottomBarText, leftPadding, TEXT_BAR_HEIGHT + (NUM_ROWS * HEIGHT_RECTANGLE) + TEXT_BAR_HEIGHT);
		
		//Draw side part
		paintShipInfo(g, (2*leftPadding) + (10*WIDTH_RECTANGLE),  TEXT_BAR_HEIGHT + (int)(5*HEIGHT_RECTANGLE - 4.5 * SMALL_BOX_EDGE));
	}
	
	public void paintShipInfo(Graphics g, int x, int y) {
		int currentx;
		int currenty;
		for (int i = 0; i < NUM_SHIPS; i++) {
			currenty = y + (i * 2 * SMALL_BOX_EDGE);
			for (int z = 0; z < SHIP_LENGTHS[i]; z++) {
				currentx = x + (z * SMALL_BOX_EDGE);
				
				if (board.isSunk(i)) {
					g.setColor(Color.red);
				} else {
					g.setColor(Color.gray);
				}
				g.fillRect(currentx, currenty, SMALL_BOX_EDGE, SMALL_BOX_EDGE);
				
				g.setColor(Color.black);
				g.drawRect(currentx, currenty, SMALL_BOX_EDGE, SMALL_BOX_EDGE);
			}
		}
	}


	public void mousePressed(MouseEvent e) {
		int x = resolveX(e);
		int y = resolveY(e);

		if (end) {
			end = false;
			parent.nextTurn();
		} else if (gameOver) {
			gameOver = false;
			parent.win();
		} else if (state == ADD_STATE1) {
			if (board.get(x, y) != 0) {
				currentBottomBarText = "Invalid. Click somewhere else!";
				repaint();
			} else {
				currentShipStartCoords[0] = x;
				currentShipStartCoords[1] = y;
				state = ADD_STATE2;
				currentBottomBarText = "Click to place the ending point of the " + SHIP_LENGTHS[currentShip] + " box long ship.";
				repaint();
			}
		} else if (state == ADD_STATE2) {
			//let user undo
			if (currentShipStartCoords[0] == x && currentShipStartCoords[1] == y) {
				addShips();
				repaint();
			}else if ((currentShipStartCoords[1] == y && Math.abs(x-currentShipStartCoords[0]) == SHIP_LENGTHS[currentShip]-1) 
					|| (currentShipStartCoords[0] == x && Math.abs(y-currentShipStartCoords[1]) == SHIP_LENGTHS[currentShip]-1)
					&& nothingInBetween(currentShipStartCoords[0], currentShipStartCoords[1], x, y)) {
				//place ship
				board.addShip(new Ship(currentShipStartCoords[0], x, currentShipStartCoords[1], y), currentShip);
				currentShip++;
				addShips();
				repaint();
			} else {
				currentBottomBarText = "Invalid. Click somewhere else!";
				repaint();
			}

		} else if (state == OPPONENT_STATE) {
			if ((x != -1) && (y != -1)) {
				int shotResult = board.shot(x, y);

				if (shotResult == 0) {
					//Shot was a miss
					currentBottomBarText = "Miss :( Click anywhere to continue.";
					repaint();
					end = true;


				} else if (shotResult == 1) {
					//Shot was a hit

					//Check if this resulted in a win
					if (board.checkAllSunk()) {
						currentBottomBarText = "You have won! Click to continue.";
						repaint();
						gameOver = true;
					} else {
						//Update message
						currentBottomBarText = "Hit! Fire another shot!";
						repaint();
						//Player gets to go again, so DO NOT call nextTurn
					}

					

				} else {
					//Shot was on an invalid

					//Update message
					currentBottomBarText = "Invalid shot. Click somewhere else!";
					repaint();
				}
			}
		}

	}
	
	private boolean nothingInBetween(int startx, int starty, int endx, int endy) {
		if (startx > endx) {
			int temp = endx;
			endx = startx;
			startx = temp;
		}
		
		if (starty > endy) {
			int temp = endy;
			endy = starty;
			starty = temp;
		}
		for (int y = starty; y <= endy; y++) {
			for (int x = startx; x <= endx; x++) {
				if (board.get(x, y) != 0) {
					return false;
				}
			}
		}
		
		return true;
	}

	//Methods called by main applet
	public void addShips() {
		currentShipStartCoords[0] = -1;
		currentShipStartCoords[1] = -1;
		if (currentShip < NUM_SHIPS) {
			state = ADD_STATE1;
			//update text instructions
			currentTopBarText = "Add Ships";
			currentBottomBarText = "Click to place the starting point of the " + SHIP_LENGTHS[currentShip] + " box long ship.";
		} else {
			currentBottomBarText = "You have finished placing all of your ships. Click anywhere to continue.";
			end = true;
		}
		


	}

	public void makePlayerScreen() {
		state = PLAYER_STATE;
		//update text instructions
		currentTopBarText = "Player";
		currentBottomBarText = "These are your ships.";


	}

	public void makeOpponentScreen(){
		state = OPPONENT_STATE;

		//update text instructions
		currentTopBarText = "Opponent";
		currentBottomBarText = "Click a box to fire upon!";
	}
	
	public void reset() {
		board = new Board();
		state = -1;
		currentShip = 0;
		currentShipStartCoords = new int[2];
		currentShipStartCoords[0] = -1;
		currentShipStartCoords[1] = -1;
	}

	private int resolveX(MouseEvent e) {
		Point p = e.getPoint();

        // check if clicked in box area
        int x = p.x - leftPadding;

        if (x >= 0 && x < NUM_COLUMNS*WIDTH_RECTANGLE) {
            int k = x / WIDTH_RECTANGLE;
            return k;
        } else {
        	return -1;
        }

    }


	private int resolveY(MouseEvent e) {
		Point p = e.getPoint();

        // check if clicked in box area
        int y = p.y - TEXT_BAR_HEIGHT;

        if (y >= 0 && y < NUM_ROWS*HEIGHT_RECTANGLE) {
            int k = y / HEIGHT_RECTANGLE;
            return k;
        } else {
        	return -1;
        }
	}

	// Extra methods required to implement mouselistener
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

} class passCanvas extends Canvas implements MouseListener {
	protected Battleship parent;
	protected String message = "";
	protected boolean gameOver; 

	public passCanvas(Battleship p) {
		parent = p;
		addMouseListener(this);
		gameOver = false;
	}

	public void win(int currentPlayer) {
		message = "Player " + currentPlayer + " has won!" ;
		repaint();
		gameOver = true;

	}

	public void pass(int currentPlayer) {
		message = "Pass the computer to Player " + currentPlayer + "!";
		repaint();
	}

	public void paint(Graphics g) {
		Dimension d = getSize();
		g.setColor(Color.black);
		g.fillRect(0, 0, d.width, d.height);
		g.setColor(Color.white);
		g.drawString(message, 30, 50);
	}


	public void mouseClicked(MouseEvent e) {
		if (gameOver) {
			gameOver = false;
			parent.startScreen();
		} else {
			parent.presentGame();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {


	}
	
} @SuppressWarnings("serial")
class homeCanvas extends Canvas implements MouseListener {
	protected Battleship parent;
	protected final int BUTTON_WIDTH = 120;
	protected final int BUTTON_HEIGHT = 30;
	
	homeCanvas(Battleship p) {
		parent  = p;
		addMouseListener(this);
	}
	
	public void paint(Graphics g) {
		Dimension d = getSize();
		g.setColor(Color.blue);
		g.fillRect(0, 0, d.width, d.height);
		
		//Draw title 
		g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		g.setColor(Color.white);
		centerString(g, "BATTLESHIP", d.width/2, d.height/4);
		
		//Draw buttons
		g.setColor(Color.white);
		g.fillRect(d.width/2 - BUTTON_WIDTH/2, d.height/2 - BUTTON_HEIGHT/2, BUTTON_WIDTH, BUTTON_HEIGHT);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.setColor(Color.blue);
		centerString(g, "One Player", d.width/2, d.height/2);
		
		g.setColor(Color.white);
		g.fillRect(d.width/2 - BUTTON_WIDTH/2, 3 * d.height/4 - BUTTON_HEIGHT/2, BUTTON_WIDTH, BUTTON_HEIGHT);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.setColor(Color.blue);
		centerString(g, "Two Players", d.width/2, 3 * d.height/4);
		
	}
	
	// helper method to draw a String centered at x, y
	// Adapted from a metheod provided by Prof Scharstein in HW6
	public static void centerString(Graphics g, String s, int x, int y) {
		FontMetrics fm = g.getFontMetrics(g.getFont());
		int xs = x - fm.stringWidth(s)/2 + 1;
		int ys = y + fm.getAscent()/3 + 1;
		g.drawString(s, xs, ys);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Point p = e.getPoint();
		Dimension d = getSize();
		if (p.x > (d.width/2 - BUTTON_WIDTH/2) && p.x <  (d.width/2 + BUTTON_WIDTH/2) && p.y > (d.height/2 - BUTTON_HEIGHT/2) && p.y < (d.height/2 + BUTTON_HEIGHT/2)) {
			//First button
			parent.playOnePlayerGame();
		} else if (p.x > (d.width/2 - BUTTON_WIDTH/2) && p.x <  (d.width/2 + BUTTON_WIDTH/2) && p.y > (3*d.height/4 - BUTTON_HEIGHT/2) && p.y < (3*d.height/4 + BUTTON_HEIGHT/2)) {
			//Second button
			parent.playTwoPlayerGame();
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
