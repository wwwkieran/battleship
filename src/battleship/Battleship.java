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
		Canvas leftCanvas = new boardCanvas(true);
		Canvas rightCanvas = new boardCanvas(true);
		setLayout(new GridLayout(1,2));
		add(leftCanvas);
		add(rightCanvas);
		
	}
	
	public void startGame() {
		
		
		
		
	}

	/*private void createButtons() {
		// Creates buttons in the button arrays and adds them to the correct panels
		for (int y = 0; y < NUM_COLUMNS; y++) {
			for (int x = 0; x < NUM_ROWS; x++) {
				playerBoardButtons[x][y] = new Button();
				playerBoardButtons[x][y].setLabel(x + "," + y);
				playerPanel.add(playerBoardButtons[x][y]);
				opponentBoardButtons[x][y] = new Button();
				opponentBoardButtons[x][y].setLabel(x + "," + y);
				opponentPanel.add(opponentBoardButtons[x][y]);
			}
		}
	}
	*/
	
	

	
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
	protected boolean isActive;
	
	public boardCanvas(boolean active) {
		board = new Board();
		isActive = active;
		addMouseListener(this);
	}
	
	
	
	public void paint(Graphics g) {
		Dimension d = getSize();
		
		//Draw top part
		g.setColor(Color.white);
		g.fillRect(0, 0, d.width, TEXT_BAR_HEIGHT);
		
		FontMetrics fm = g.getFontMetrics(g.getFont());
		int xs = d.width/2 - fm.stringWidth("Hello")/2 + 1;
		g.drawString("Hello", xs, 0);
		
		//Paint Board
		for (int y = 0; y < NUM_COLUMNS; y++) {
			for (int x = 0; x < NUM_ROWS; x++) {
				g.setColor(Color.black);
				g.drawRect(x * WIDTH_RECTANGLE, y * HEIGHT_RECTANGLE + TEXT_BAR_HEIGHT, WIDTH_RECTANGLE, HEIGHT_RECTANGLE);
				switch(board.get(x, y)) {
					case 0: g.setColor(Color.blue); //Ocean
							break;
					case 1: if (isActive)
								g.setColor(Color.gray); //Ship
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
		Point p = e.getPoint();
		System.out.println(p);

	}
	
	//Methods called by main applet 
	public void addShips(){
		
	}
	
	public void makeActive(){
		isActive = true;
		
	}
	
	public void makeInactive(){
		isActive = false;
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
 