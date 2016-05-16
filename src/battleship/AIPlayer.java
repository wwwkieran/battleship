package battleship;

import java.util.*;

public class AIPlayer {

	// instance variables
	int[] returnArray;
	int crossPoint;
	boolean [][] alreadyVisited;
	protected int firstX;
	protected int firstY;
	protected int currentX;
	protected int currentY;
	protected int hitState;
	protected int nextMove;
	Random rand;

	// constructor
	public AIPlayer() {
		returnArray = new int[2];
		crossPoint = 0;
		alreadyVisited = new boolean [10][10]; 
		firstX = 0;
		firstY = 0;
		currentX = 0;
		currentY = 0;
		hitState = 0;
		nextMove = 0;
		rand = new Random();
	}
	/* Returns an array with the coordinates of a move to make in the format 
	 * intArray[0] = x and intArray[1] = y 
	 */
	public int[] aiMakeMove() {
		if(hitState == 0) {
			int pickX = rand.nextInt(9) + 0;
			int pickY = rand.nextInt(9) + 0;
			while(alreadyVisited[pickX][pickY] == true) { 
				pickX = rand.nextInt(9) + 0;
				pickY = rand.nextInt(9) + 0;
			}
			alreadyVisited[pickX][pickY] = true;
			firstX = pickX;
			firstY = pickY;
			currentX = pickX;
			currentY = pickY;
			returnArray[0] = pickX;
			returnArray[1] = pickY;	
		} else if(hitState == 1) {
			if(((currentX - 1) <= 9) && ((currentX - 1) >=0) && ((currentY) <= 9) && 
			   ((currentY) >=0) && alreadyVisited[currentX - 1][currentY] == false) {
				alreadyVisited[currentX - 1][currentY] = true;
				currentX -= 1;
				crossPoint = 1;
				returnArray[0] = currentX;
				returnArray[1] = firstY;
				currentX = firstX;
				currentY = firstY;
			} else if(((currentX) <= 9) && ((currentX) >=0) && ((currentY + 1) <= 9) && 
					   ((currentY + 1) >=0) && alreadyVisited[currentX][currentY + 1] == false) {
						alreadyVisited[currentX][currentY + 1] = true;
						currentY += 1;
						crossPoint = 2;
						returnArray[0] = firstX;
						returnArray[1] = currentY;
						currentX = firstX;
						currentY = firstY;
			} else if(((currentX + 1) <= 9) && ((currentX + 1) >=0) && ((currentY) <= 9) && 
					   ((currentY) >=0) && alreadyVisited[currentX + 1][currentY] == false) {
						alreadyVisited[currentX + 1][currentY] = true;
						currentX += 1;
						crossPoint = 3;
						returnArray[0] = currentX;
						returnArray[1] = firstY;
						currentX = firstX;
						currentY = firstY;
			}else if(((currentX) <= 9) && ((currentX) >=0) && ((currentY - 1) <= 9) && 
					   ((currentY) >=0) && alreadyVisited[currentX][currentY - 1] == false) {
						alreadyVisited[currentX][currentY - 1] = true;
						currentY -= 1;
						crossPoint = 4;
						returnArray[0] = firstX;
						returnArray[1] = currentY;
						currentX = firstX;
						currentY = firstY;
			}
			
		} else if(hitState >= 2) { // the hunt beings...
			if(crossPoint == 1) { // second hit was east
				currentX -= 1;
				alreadyVisited[currentX][currentY] = true;
				returnArray[0] = currentX;
			} else if(crossPoint == 2) { // second hit was east
				currentY += 1;
				alreadyVisited[currentX][currentY] = true;
				returnArray[0] = currentX;
				returnArray[1] = currentY;
			} else if(crossPoint == 3) { // second hit was east
				currentX += 1;
				alreadyVisited[currentX][currentY] = true;
				returnArray[0] = currentX;
				returnArray[1] = currentY;
			} else if(crossPoint == 4) { // second hit was east
				currentY -= 1;
				alreadyVisited[currentX][currentY] = true;
				returnArray[0] = currentX;
				returnArray[1] = currentY;
			} 
		}
		return returnArray;
	}
	
	
	// hit state methods
	public void isHit() {
		hitState += 1;
		if(hitState >= 2) { 
			if(currentX == 0 && crossPoint == 1) {
				crossPoint = 3;
			} else if(currentX == 9 && crossPoint == 3) {
				crossPoint = 1;
			} else if(currentX == 0 && crossPoint == 2) {
				crossPoint = 4;
			} else if(currentX == 9 && crossPoint == 4) {
				crossPoint = 2;
			} 
		}
	}
	
	public void isSunk() {
		hitState = 0;	
	}
	
	public void isMiss() {
		if(hitState >= 2) {
				currentX = firstX;
				currentY = firstY;
				if(crossPoint == 1) {
					crossPoint = 3;
				} else if(crossPoint == 2) {
					crossPoint = 4;
				} else if (crossPoint == 3) {
					crossPoint = 1;
				} else {
					crossPoint = 2;
				}
			
		}
		
		
	}
	
	
	
	
}

	



		

