package battleship;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*; // for Stack

public class Battleship extends Applet implements ActionListener {
	
	protected Board player1;
	protected Board player2;
	protected int NUM_COLUMNS = 10;
	protected int NUM_ROWS = 10;
	protected Button[][] playerBoardButtons;
	protected Button[][] opponentBoardButtons;
	protected Panel playerPanel;
	protected Panel opponentPanel;
	
	public void init() {
		playerBoardButtons = new Button[NUM_ROWS][NUM_COLUMNS];
		opponentBoardButtons = new Button[NUM_ROWS][NUM_COLUMNS];
		
		playerPanel = new Panel();
		opponentPanel = new Panel();
		
		playerPanel.setLayout(new GridLayout(NUM_ROWS, NUM_COLUMNS));
		opponentPanel.setLayout(new GridLayout(NUM_ROWS, NUM_COLUMNS));
		
		createButtons();
		
		setLayout(new GridLayout(1,2));
		add(playerPanel);
		add(opponentPanel);
		
		
		
		
	}

	private void createButtons() {
		// Creates buttons in the button arrays and adds them to the correct panels
		for (int y = 0; y < NUM_COLUMNS; y++) {
			for (int x = 0; x < NUM_ROWS; x++) {
				playerBoardButtons[x][y] = new Button();
				playerBoardButtons[x][y].setName(x + "," + y);
				playerPanel.add(playerBoardButtons[x][y]);
				opponentBoardButtons[x][y] = new Button();
				opponentPanel.add(opponentBoardButtons[x][y]);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//Watch for events
		System.out.println("hello");
		
	}

	
} 
 