package battleship;

import java.util.*;

public class AIPlayer {

	// instance variables
	int[] returnArray;
	int crossPoint;
	boolean [][] alreadyVisited;
	boolean hasFirstLocation;
	boolean hasSecondLocation;
	boolean isSunk;
	protected int firstX;
	protected int firstY;
	protected int currentX;
	protected int currentY;
	protected int hitState;
	Random rand;

	// constructor
	public AIPlayer() {
		returnArray = new int[2];
		crossPoint = 0;
		alreadyVisited = new boolean [10][10]; 
		hasFirstLocation = false;
		hasSecondLocation = false;
		isSunk = false;
		firstX = 0;
		firstY = 0;
		currentX = 0;
		currentY = 0;
		hitState = 5;
		rand = new Random();
	}
	/* Returns an array with the coordinates of a move to make in the format 
	 * intArray[0] = x and intArray[1] = y 
	 */
	public int[] aiMakeMove() {
		if(hasFirstLocation == false) {

			int pickX = rand.nextInt(9) + 0;
			int pickY = rand.nextInt(9) + 0;
			while(alreadyVisited[pickX][pickY] == true) { 
				pickX = rand.nextInt(9) + 0;
				pickY = rand.nextInt(9) + 0;
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
				firstX = currentX;
				firstY = currentY;
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
					crossPoint = 1;
				} else if(nextMove == 2 && alreadyVisited[currentX][currentY + 1] == false) {
					currentY += 1;
					returnArray[0] = currentY;
					alreadyVisited[currentX][currentY] = true;
					crossPoint = 2;
				} else if(nextMove == 3 && alreadyVisited[currentX + 1][currentY] == false) {
					currentX += 1;
					returnArray[0] = currentX;
					alreadyVisited[currentX][currentY] = true;
					crossPoint = 3;
				} else if(nextMove == 4 && alreadyVisited[currentX][currentY] == false) {
					currentY -= 1;
					returnArray[0] = currentY;
					alreadyVisited[currentX][currentY] = true;
					crossPoint = 4;
				}  

			} else if(hitState == 1) {
				hasSecondLocation = true;
			}
		} else if(hitState == 0 && isSunk == false) {
			currentX = firstX;
			currentY = firstY;
			hitState = 1;
			if(crossPoint == 1) { // hit was east
				crossPoint = 4;
			} else if(crossPoint == 2) { // hit was north
				crossPoint = 3;
			} else if(crossPoint == 3) { // hit was west 
				crossPoint = 2;
			} else { // hit was south
				crossPoint = 1;
			}

		}

		return returnArray;
	}



	// hit state methods
	public void isHit() {
		hitState = 1;			
	}
	public void isSunk() {
		isSunk = true;			
	}
	public void isMiss() {
		hitState = 0;			
	}


}
