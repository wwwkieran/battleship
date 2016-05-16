package battleship;

import java.util.*;

public class AIPlayer {
	
	// instance variables
	int[] returnArray;
	boolean [][] alreadyVisited;
	boolean hasFirstLocation;
	boolean hasSecondLocation;
	protected int currentX;
	protected int currentY;
	protected int hitState;
	Random rand;
	
	// constructor
	public AIPlayer() {
		returnArray = new int[2];
		alreadyVisited = new boolean [10][10]; 
		hasFirstLocation = false;
		hasSecondLocation = false;
		currentX = 0;
		currentY = 0;
		hitState = 5;
		rand = new Random();
	}
	/* Returns an array with the coordinates of a move to make in the format 
	 * intArray[0] = x and intArray[1] = y 
	 */
	public int[] aiMakeMove() {
		// Make initial move
		if(hasFirstLocation == false) {
			int pickX = rand.nextInt(10) + 1;
			int pickY = rand.nextInt(10) + 1;
			while(alreadyVisited[pickX][pickY] == true) { 
				pickX = rand.nextInt(10) + 1;
				pickY = rand.nextInt(10) + 1;
			}
			currentX = pickX;
			currentY = pickY;
			alreadyVisited[pickX][pickY] = true;	
			
			if(hitState == 0) { // initial move was a miss
				hasFirstLocation = false;
				currentX = 0;
				currentY = 0;
			} else if (hitState == 1) { // initial move was a hit
				hasFirstLocation = true;
				hitState = 0;
			} else { // first pass, so simply adds points to array and checks state
				returnArray[0] = pickX;
				returnArray[1] = pickY;
			}
		
		// initial move was a hit
		} else if(hasSecondLocation == false) {
			if (hitState == 0) {
				// randomly chooses adjacent points to hit
				int nextMove = rand.nextInt(4) + 1;
				
				if(nextMove == 1 && alreadyVisited[currentX - 1][currentY] == false) {
					currentX -= 1;
					returnArray[0] = currentX;
					alreadyVisited[currentX][currentY] = true;
				} else if(nextMove == 2 && alreadyVisited[currentX][currentY + 1] == false) {
					currentY += 1;
					returnArray[0] = currentY;
					alreadyVisited[currentX][currentY] = true;
				} else if(nextMove == 3 && alreadyVisited[currentX + 1][currentY] == false) {
					currentX += 1;
					returnArray[0] = currentX;
					alreadyVisited[currentX][currentY] = true;
				} else if(nextMove == 4 && alreadyVisited[currentX][currentY] == false) {
					currentY -= 1;
					returnArray[0] = currentY;
					alreadyVisited[currentX][currentY] = true;
				}  
			
			
			} else if(hitState == 1) {
				hasSecondLocation = true;	
			}
			
		} else {
			// hunt...
			
		}
		return returnArray;
	}
	
	public void isHit() {
		hitState = 1;			
	}
	
	public void isSunk() {
		hitState = 2;			
	}
	
	public void isMiss() {
		hitState = 0;			
	}
	
	
}
		