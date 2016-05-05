package battleship;

public class Board {
	/*
	 *  0: Empty
	 *  1: Ship
	 *  2: Hit
	 *  3: Miss
	 *  4: Sunk
	 */
	
	// instance variables
	
	protected int[][] board;
	protected int NUM_COLUMNS = 10;
	protected int NUM_ROWS = 10;
	protected Ship[] shipArray;
	protected int NUM_SHIPS = 5;
	
	// constructor
	public Board() {
		board = new int [NUM_ROWS][NUM_COLUMNS]; //Default value is 0
		shipArray = new Ship[NUM_SHIPS];
	}
	
	//instance methods 
	
	public void clear() {
		
	}
	
	public void shot(int x, int y) {
		if (board[x][y] == 1) {
			//	There was a hit
			board[x][y] = 2;
			checkSunk();
		} else {
			//	 was a miss
			board[x][y] = 3;
		}
		
	}
	
	public void addShip(Ship newShip) {
		//Update board
		for (int x = newShip.startX(); x <= newShip.endX(); x++) {
			for (int y = newShip.startY(); y <= newShip.endY(); y++) {
				board[x][y] = 1;
			}
		}
		//Add ship to array
		
	}
	
	public void checkSunk() {
		//goes through array of ships to check for sunk ships
		for (int i = 0; i < NUM_SHIPS; i++) {
			if (isSunk(shipArray[i])) {
				//ship has been sunk, update coords
				for (int x = shipArray[i].startX(); x <= shipArray[i].endX(); x++) {
					for (int y = shipArray[i].startY(); y <= shipArray[i].endY(); y++) {
						board[x][y] = 4;
					}
				}
			}
		}
	}
	
	private boolean isSunk(Ship theShip) {
		// returns true if ship object is sunk 
		// meaning all squares of ship are hit(3)
		// false otherwise
		for (int x = theShip.startX(); x <= theShip.endX(); x++) {
			for (int y = theShip.startY(); y <= theShip.endY(); y++) {
				if (board[x][y] == 1) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	
	
	
}
