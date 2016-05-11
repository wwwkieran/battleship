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
	protected Panel playerPanel;
	protected Panel opponentPanel;
	
	
	public void init() {
		boardCanvas leftCanvas = new boardCanvas(true);
		boardCanvas rightCanvas = new boardCanvas(true);
		setLayout(new GridLayout(1,2));
		leftCanvas.makeOpponentScreen();
		add(leftCanvas);
		add(rightCanvas);
		
	}
	
	public void startTwoPlayerGame() {
		
		
		
		
	}

	
	public void actionPerformed(ActionEvent e) {
		//Watch for events
		
		
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
	protected int state;
	protected final int ADD_STATE = 0;
	protected final int PLAYER_STATE = 1;
	protected final int OPPONENT_STATE = 2;
	
	
	public boardCanvas(boolean active) {
		board = new Board();
		addMouseListener(this);
		state = -1;
		board.addShip(new Ship(3, 0, 2, 0, 0), 0);
		board.addShip(new Ship(3, 0, 2, 1, 1), 1);
		board.addShip(new Ship(3, 0, 2, 2, 2), 2);
		board.addShip(new Ship(3, 0, 2, 3, 3), 3);
		board.addShip(new Ship(3, 0, 2, 4, 4), 4);
	}
	
	
	
	public void paint(Graphics g) {
		Dimension d = getSize();
		
		//Draw top part
		g.setColor(Color.white);
		g.fillRect(0, 0, d.width, TEXT_BAR_HEIGHT);
		
		
		g.drawString("Hello", 30, 30);
		
		//Paint Board
		for (int y = 0; y < NUM_COLUMNS; y++) {
			for (int x = 0; x < NUM_ROWS; x++) {
				g.setColor(Color.black);
				g.drawRect(x * WIDTH_RECTANGLE, y * HEIGHT_RECTANGLE + TEXT_BAR_HEIGHT, WIDTH_RECTANGLE, HEIGHT_RECTANGLE);
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
				g.fillRect(x * WIDTH_RECTANGLE, y * HEIGHT_RECTANGLE + TEXT_BAR_HEIGHT, WIDTH_RECTANGLE, HEIGHT_RECTANGLE);
			}
		}
		
		//Draw bottom part
		g.setColor(Color.white);
		g.fillRect(0, TEXT_BAR_HEIGHT + (NUM_ROWS * HEIGHT_RECTANGLE), d.width, TEXT_BAR_HEIGHT);
	}


	public void mousePressed(MouseEvent e) {
		int x = resolveX(e);
		int y = resolveY(e);
		
		System.out.print("(" + x + "," + y + ")");
		
		if (state == ADD_STATE) {
			
		} else if (state == OPPONENT_STATE) {
			if ((x != -1) && (y != -1)) {
				board.shot(x, y);
				repaint();
			}
		}
		
	}
	
	//Methods called by main applet 
	public void addShips() {
		state = ADD_STATE;
		
		//update text instructions
	}
	
	public void makePlayerScreen() {
		state = PLAYER_STATE;	
		
		//update text instructions
		
	}
	
	public void makeOpponentScreen(){
		state = OPPONENT_STATE;
		
		//update text instructions
		
	}
	
	private int resolveX(MouseEvent e) {
		Point p = e.getPoint();

        // check if clicked in box area

        int x = p.x;

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
	
}
 