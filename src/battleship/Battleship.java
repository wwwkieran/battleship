package battleship;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

@SuppressWarnings("serial")

public class Battleship extends Applet {


	protected Button startButton;
	protected Button[][] playerBoardButtons;
	protected Button[][] opponentBoardButtons;
	protected boardCanvas playerCanvas;
	protected boardCanvas opponentCanvas;
	protected passCanvas notifCanvas;
	protected homeCanvas startCanvas;
	protected int currentPlayer; //In two player mode, 1 = player 1 and 2 = player 2
	protected int gameType; // 1-player = 1; 2-player = 2
	protected final int ONE_PLAYER_GAME = 1;
	protected final int TWO_PLAYER_GAME = 2;
	protected boolean gameOver;
	protected boolean shipsAdded;
	protected AIPlayer AI;
	protected AudioClip explosionSound;
	protected final int NUM_SHIPS = 5;
	protected final int[] SHIP_LENGTHS = {5,4,3,3,2};
	protected Random rand;
	protected int numSunkByAI;

	public void init() {
		/* Initializes the key variables required for the applet and displays the start screen */
		playerCanvas = new boardCanvas(this);
		opponentCanvas = new boardCanvas(this);
		notifCanvas = new passCanvas(this);
		startCanvas = new homeCanvas(this);
		explosionSound = getAudioClip(getCodeBase(), "explosion.au");
		rand = new Random();
		startScreen();
	}

	public void startScreen() {
		/* Displays the start screen, where users can select either a 1 player or 2 player game */
		removeAll();
		setLayout(new GridLayout(1,1));
		add(startCanvas);
		revalidate();
	}

	public void playTwoPlayerGame() {
		/* Starts a two player game */
		reset();
		gameType = TWO_PLAYER_GAME;
		nextTurn();

	}

	public void playOnePlayerGame() {
		/* Starts a one player game */
		reset();
		gameType = ONE_PLAYER_GAME;
		AI = new AIPlayer();
		numSunkByAI = 0;
		presentGame();
	}

	public void reset() {
		/* Resets the key variables required for the applet so that a new game can be played */
		playerCanvas.reset();
		opponentCanvas.reset();
		currentPlayer = 0;
		gameOver = false;
		shipsAdded = false;
	}

	public void presentGame() {
		/* Places the player and opponent boards on the screen */
		if (!shipsAdded) {
			// Only display one board for ship placement
			removeAll();
			setLayout(new GridLayout(1,1));
			playerCanvas.addShips();
			add(playerCanvas);
			revalidate();
			if (currentPlayer == 2) {
				shipsAdded = true;
			}
		} else {
			//Play game
			removeAll();
			setLayout(new GridLayout(2,1));
			playerCanvas.makePlayerScreen();
			opponentCanvas.makeOpponentScreen();
			add(playerCanvas);
			add(opponentCanvas);
			revalidate();
			}
	}

	public void playExplosion() {
		explosionSound.play();
	}

	public void AIMove() {
		int[] move = AI.aiMakeMove();

		int moveResult = playerCanvas.passInMove(move);

		if (moveResult == 1) {
			//hit
			AI.isHit();
			playerCanvas.repaint();
			playExplosion();
			// Check if all AI sunk a ship
			if (playerCanvas.checkNumSunk() != numSunkByAI) {
				AI.isSunk();
				// Check if AI won
				if (playerCanvas.checkAllSunk()) {
					// AI has won
					playerCanvas.AIwon();
					opponentCanvas.AIwon();
				}
			}
			// AI gets another move
			AIMove();
		} else {
			// AI had miss
			AI.isMiss();
			playerCanvas.repaint();
		}

	}

	public void nextTurn() {
		// this happens in both single and two player mode
		switch (currentPlayer) {
			case 0: currentPlayer = 1; break;
			case 1: currentPlayer = 2; break;
			case 2: currentPlayer = 1; break;
		}

		if (gameType == ONE_PLAYER_GAME) {
			if (!shipsAdded) {
				// Add ships to opponent board
				for (int i = 0; i < NUM_SHIPS;) {
					int startx;
					int endx;
					int starty;
					int endy;

					startx = rand.nextInt(9);
					endx = rand.nextInt(9);

					while((startx != endx) && (Math.abs(endx-startx) != SHIP_LENGTHS[i]-1)) {
						endx = rand.nextInt(9);
					}

					// endx is either same as startx or right length away from x
					if (startx == endx) {
						// Start x and end x are same, so the ship must be vertical
						starty = rand.nextInt(9);
						endy = SHIP_LENGTHS[i]-1 + starty;

						if (endy > 9) {
							endy = starty - SHIP_LENGTHS[i]-1;
						}


					} else {
						// Start x and end x are right distance apart, so ship is horizontal
						// This means the y co-ords are the same...
						starty = rand.nextInt(9);
						endy = starty;
					}

					//Check that this ship won't intersect any other ships
					if (opponentCanvas.nothingInBetween(startx, starty, endx, endy)) {
						opponentCanvas.placeShip(startx, starty, endx, endy, i);
						i++;
					}
					//If it does, then just repeat the same steps to try again...
				}


				// All ships have been added
				shipsAdded = true;

				// Set up window
				removeAll();
				setLayout(new GridLayout(2,1));
				playerCanvas.makePlayerScreen();
				opponentCanvas.makeOpponentScreen();
				add(playerCanvas);
				add(opponentCanvas);
				revalidate();

			} else {
				opponentCanvas.setUnclickable();
				opponentCanvas.repaint();
				AIMove();
				currentPlayer = 1;
				opponentCanvas.setClickable();
			}


		} else if (gameType == TWO_PLAYER_GAME) {
			//Show Message
			removeAll();
			setLayout(new GridLayout(1,1));
			notifCanvas.pass(currentPlayer);
			add(notifCanvas);
			revalidate();
			//Swap Canvas Locations
			boardCanvas temp = playerCanvas;
			playerCanvas = opponentCanvas;
			opponentCanvas = temp;
		}


	}

	public void win() {
		/* Called by a canvas when a win has occurred */
		gameOver = true;
		//Show Message
		removeAll();
		setLayout(new GridLayout(1,1));
		notifCanvas.win(currentPlayer);
		add(notifCanvas);
		revalidate();
	}

	public int getGameType() {
		return gameType;
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
	protected boolean unclickable;
	protected int leftPadding;
	protected final int NUM_SHIPS = 5;
	protected final int[] SHIP_LENGTHS = {5,4,3,3,2};
	protected int currentShip;
	protected int[] currentShipStartCoords;
	protected final int ONE_PLAYER_GAME = 1;
	protected final int TWO_PLAYER_GAME = 2;


	public boardCanvas(Battleship b) {
		/* Initializes the instance variables needed for an object */
		board = new Board();
		addMouseListener(this);
		parent = b;
		state = -1;
		unclickable = false;
	}



	public void paint(Graphics g) {
		/* Paints a 10 by 10 board based on the information in the Board object and a top and bottom text field */
		Dimension d = getSize();

		// Determine padding of board
		leftPadding = 30;

		// Draw top part
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
		/* Draws the ship tracker at location x, y. The ship tracker displays a player's ships, red if a ship has been sunk and blue otherwise. */
		int currentx;
		int currenty;
		for (int i = 0; i <= currentShip-1; i++) {
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
			// Turn is over
			end = false;
			parent.nextTurn();
		} else if (gameOver) {
			// Game is over
			gameOver = false;
			parent.win();
		} else if (state == ADD_STATE1) {
			// Add first point of ship
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
			// Add second point of ship
			//let user undo
			if (currentShipStartCoords[0] == x && currentShipStartCoords[1] == y) {
				addShips();
				repaint();
			} else if ((currentShipStartCoords[1] == y && Math.abs(x-currentShipStartCoords[0]) == SHIP_LENGTHS[currentShip]-1)
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

					// If it's single player mode, we don't have to wait for a click to go to the next turn...
					if (parent.getGameType() == ONE_PLAYER_GAME) {
						parent.nextTurn();
						end = false;
					}
				} else if (shotResult == 1) {
					//Shot was a hit
					parent.playExplosion();
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

	public boolean checkAllSunk() {
		return board.checkAllSunk();
	}

	public int checkNumSunk() {
		int numSunk = 0;
		for (int i = 0; i < NUM_SHIPS; i++) {
			if (board.isSunk(i))
				numSunk++;
		}
		return numSunk;
	}

	public void AIwon() {
		/* Called if the AI has won */
		gameOver = true;
		currentBottomBarText = "AI has won. Click to continue.";
		repaint();
	}

	public boolean nothingInBetween(int startx, int starty, int endx, int endy) {
		/* Returns false if there are any ship boxes between two points. True otherwise. */

		// Ensure that endx is bigger
		if (startx > endx) {
			int temp = endx;
			endx = startx;
			startx = temp;
		}
		// Ensure that endy is bigger
		if (starty > endy) {
			int temp = endy;
			endy = starty;
			starty = temp;
		}

		// Check for ship squares
		for (int y = starty; y <= endy; y++) {
			for (int x = startx; x <= endx; x++) {
				if (board.get(x, y) != 0) {
					return false;
				}
			}
		}
		// Otherwise, there were no ship squares in between
		return true;
	}

	public void placeShip(int startx, int starty, int endx, int endy, int shipNum) {
		board.addShip(new Ship(startx, endx, starty, endy), shipNum);
		currentShip = shipNum+1;
	}

	public void addShips() {
		/* Called by the applet to facilitate the placement of ships. */
		currentShipStartCoords[0] = -1;
		currentShipStartCoords[1] = -1;
		if (currentShip < NUM_SHIPS) {
			state = ADD_STATE1;
			// Update text instructions
			currentTopBarText = "Add Ships";
			currentBottomBarText = "Click to place the starting point of the " + SHIP_LENGTHS[currentShip] + " box long ship.";
		} else {
			currentBottomBarText = "You have finished placing all of your ships. Click anywhere to continue.";
			end = true;
		}

	}

	public int passInMove(int[] coords) {
		/* For the applet to pass in a move from the AI */
		return board.shot(coords[0], coords[1]);
	}

	public void setClickable() {
		/* Makes the canvas clickable */
		unclickable = false;
		currentBottomBarText = "Click a box to fire upon!";
	}

	public void setUnclickable() {
		/* Makes the canvas unclickable */
		unclickable = true;
		currentBottomBarText = "Waiting for AI to make a move.";
	}

	public void makePlayerScreen() {
		/* Sets this board as the Player's board */
		state = PLAYER_STATE;
		//update text instructions
		currentTopBarText = "Player";
		currentBottomBarText = "These are your ships.";
	}

	public void makeOpponentScreen() {
		/* Sets this board as the Opponent's board */
		state = OPPONENT_STATE;
		//update text instructions
		currentTopBarText = "Opponent";
		currentBottomBarText = "Click a box to fire upon!";
	}

	public void reset() {
		/* Resets the key variables required for the board so that a new game can be played */
		board = new Board();
		state = -1;
		currentShip = 0;
		currentShipStartCoords = new int[2];
		currentShipStartCoords[0] = -1;
		currentShipStartCoords[1] = -1;
	}

	private int resolveX(MouseEvent e) {
		/* Resolves the x-coord of the board box (0-9) in which a MouseEvent occurred. Returns -1 if the MouseEvent was outside of the board. */
		Point p = e.getPoint();

        // check if clicked in board area
        int x = p.x - leftPadding;

        if (x >= 0 && x < NUM_COLUMNS*WIDTH_RECTANGLE) {
            int k = x / WIDTH_RECTANGLE;
            return k;
        } else {
        	// Not in board
        	return -1;
        }

    }


	private int resolveY(MouseEvent e) {
		/* Resolves the y-coord of the board box (0-9) in which a MouseEvent occurred. Returns -1 if the MouseEvent was outside of the board. */
		Point p = e.getPoint();

        // check if clicked in board area
        int y = p.y - TEXT_BAR_HEIGHT;

        if (y >= 0 && y < NUM_ROWS*HEIGHT_RECTANGLE) {
            int k = y / HEIGHT_RECTANGLE;
            return k;
        } else {
        	// Not in board
        	return -1;
        }
	}

	// Extra methods required to implement MouseListener
	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

} class passCanvas extends Canvas implements MouseListener {
	protected Battleship parent;
	protected String message = "";
	protected boolean gameOver;
	Image fireworks;

	public passCanvas(Battleship p) {
		/* Initializes the instance variables needed for a canvas */
		parent = p;
		addMouseListener(this);
		gameOver = false;

		// Gif drawing was adapted from Real How To's guide for displaying a gif in a canvas.
		MediaTracker media = new MediaTracker(this);
	    fireworks = Toolkit.getDefaultToolkit().getImage("fireworks.gif");
	    media.addImage(fireworks, 0);
	    try {
	      media.waitForID(0);
	      }
	    catch (Exception e) {}

	}

	public void win(int currentPlayer) {
		/* Called by the applet when a win has occurred. Generates a message and paints the canvas. */
		message = "Player " + currentPlayer + " has won!" ;
		repaint();
		gameOver = true;

	}

	public void pass(int currentPlayer) {
		/* Called by the applet when the computer must be passed. Generates a message and paints the canvas. */
		message = "Pass the computer to Player " + currentPlayer + "!";
		repaint();
	}

	public void paint(Graphics g) {
		/* Draws a black background with a message. */
		Dimension d = getSize();
		g.setColor(Color.black);
		g.fillRect(0, 0, d.width, d.height);
		g.setColor(Color.white);
		g.drawString(message, 30, 50);
		// Fireworks for win
		if (gameOver) {
			g.drawImage(fireworks, 30, 70, this);
		}

	}


	public void mouseClicked(MouseEvent e) {
		/* When there is a click, takes the user to the next move, or, if the game is over, back to the home screen. */
		if (gameOver) {
			gameOver = false;
			parent.startScreen();
		} else {
			parent.presentGame();
		}
	}

	// Methods required to implement MouseListener
	@Override
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

} @SuppressWarnings("serial")
class homeCanvas extends Canvas implements MouseListener {
	protected Battleship parent;
	protected final int BUTTON_WIDTH = 120;
	protected final int BUTTON_HEIGHT = 30;

	homeCanvas(Battleship p) {
		/* Initializes the instance variables needed for a homeCanvas */
		parent  = p;
		addMouseListener(this);
	}

	public void paint(Graphics g) {
		/* Draws a blue background, with a title "BATTLESHIP" and two buttons to start a 1-player or a 2-player game. */
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

	public static void centerString(Graphics g, String s, int x, int y) {
		/* Helper method to draw a string centered at x, y. Adapted from a method provided by Prof Scharstein in HW6. */
		FontMetrics fm = g.getFontMetrics(g.getFont());
		int xs = x - fm.stringWidth(s)/2 + 1;
		int ys = y + fm.getAscent()/3 + 1;
		g.drawString(s, xs, ys);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		/* Called every time there is a mouse click in the canvas. If the click is within a button, a method in the applet is called to start that type of game. */
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

	// Additional methods required to implement MouseListener
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

}
