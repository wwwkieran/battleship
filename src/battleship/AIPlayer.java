package battleship;

import java.util.*;

public class AIPlayer {

	// instance variables

	int [][] board; /* 0: unhit; 1: hit; 2: sunk; 3: miss */
	protected int currentX;
	protected int currentY;


	Random rand;

	// constructor
	public AIPlayer() {
		board = new int[10][10]; 
		currentX = 0;
		currentY = 0;
		rand = new Random();
	}
	
	public int[] aiMakeMove() {
		/* Returns an array with the coordinates of a move to make in the format 
		 * intArray[0] = x and intArray[1] = y 
		 */
		
		// Based on previous moves
		for (int x = 0; x < 10; x ++) {
			for (int y = 0; y < 10; y++) {
				if (board[x][y] == 1) {
					currentX = x;
					currentY = y;
					return move();
				}
			}
		}
		
		// Otherwise, choose randomly
		return randomMove();
	}
	
	public int[] move() {
		// Check if can shoot on current spot
		if (board[currentX][currentY] == 0) {
			int[] returnArray = new int[2];
			returnArray[0] = currentX;
			returnArray[1] = currentY;
			return returnArray;
		}
		
		// Check for patterns
		if (currentX-1 > -1 && board[currentX-1][currentY] == 1 && currentX+1 < 10 && board[currentX+1][currentY] != 3) {
			//Go right
			currentX = currentX + 1;
		} else if (currentX-1 > -1 && currentX+1 < 10 && board[currentX+1][currentY] == 1 && board[currentX-1][currentY] != 3) {
			//Go left
			currentX = currentX - 1;
		} else if (currentY-1 > -1 && currentY+1 < 10 && board[currentX][currentY-1] == 1  && board[currentX][currentY+1] != 3) {
			//Go up
			currentY = currentY + 1;
		} else if (currentY-1 > -1 && currentY+1 < 10 && board[currentX][currentY+1] == 1 && board[currentX][currentY-1] != 3) {
			//Go down
			currentY = currentY - 1;
		} else if (currentX-1 > -1 && currentX+1 < 10 && board[currentX+1][currentY] == 0) {
			//No pattern
			//Go right
			currentX = currentX + 1;
		} else if (currentX-1 > -1 && currentX+1 < 10 && board[currentX-1][currentY] == 0) {
			//No pattern
			//Go left
			currentX = currentX - 1;
		} else if (currentY-1 > -1 && currentY+1 < 10 && board[currentX][currentY+1] == 0) {
			//No pattern
			//Go up
			currentY = currentY + 1;
		} else if (currentY-1 > -1 && currentY+1 < 10 && board[currentX][currentY-1] == 0) {
			//No pattern
			//Go down
			currentY = currentY - 1;
		} else {
			return randomMove();
		}
		
		return move();
	}
	
	public int[] randomMove() {
		/* Returns a random move */ 
		int[] returnArray = new int[2];
		
		while(board[returnArray[0]][returnArray[1]] != 0) {
			returnArray[0] = rand.nextInt(10);
			returnArray[1] = rand.nextInt(10);
		}
		
		currentX = returnArray[0];
		currentY = returnArray[1];
		
		return returnArray;
	}
	

	public void isHit() {
		/* Notifies the AI that its last shot was a hit */
		board[currentX][currentY] = 1;
	}
	
	public void isSunk(int[][] sunkBoard) {
		/* Tells the AI about all sunk boxes */
		for (int x = 0; x < 10; x ++) {
			for (int y = 0; y < 10; y++) {
				if (sunkBoard[x][y] == 2) {
					board[x][y] = 2;
				}
			}
		}
	}
	
	public void isMiss() {
		/* Tells the AI that its last shot was a miss. */ 
		board[currentX][currentY] = 3;
	}
	
	
	
	
}

	



		

