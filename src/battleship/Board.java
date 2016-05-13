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
	
	public int shot(int x, int y) {
		if (board[x][y] == 1) {
			//	There was a hit
			board[x][y] = 2;
			checkSunk();
			return 1;
		} else if (board[x][y] == 0) {
			//	 was a miss
			board[x][y] = 3;
			return 0;
		} else {
			//invalid shot (trying to shoot a hit, miss, or sunk)
			return 2;
		}
		
	}
	
	public int get(int x, int y) {
		return board[x][y];
	}
	
	public void addShip(Ship newShip, int numShip) {
		//Update board
		for (int x = newShip.startX(); x <= newShip.endX(); x++) {
			for (int y = newShip.startY(); y <= newShip.endY(); y++) {
				board[x][y] = 1;
			}
		}
		
		//Add ship to array
		shipArray[numShip] = newShip;
		
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
	
	public boolean checkAllSunk() {
		//goes through array of ships to see if all ships have been sunk
		for (int i = 0; i < NUM_SHIPS; i++) {
			if (!isSunk(shipArray[i])) {
				return false;
			}
		}
		return true;
	}
	
	
	
	
	
	
}
