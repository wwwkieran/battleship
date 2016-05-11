package battleship;

import java.util.*;

public class AIPlayer {
	
	int[] returnArray;
	boolean [][] alreadyVisited;
	boolean hasFirstLocation;
	protected int currentX;
	protected int currentY;
	Random rand;
	
	public AIPlayer() {
		returnArray = new int[2];
		alreadyVisited = new boolean [10][10]; 
		hasFirstLocation = false;
		currentX = 0;
		currentY = 0;
		rand = new Random();
	}
	/* Returns an array with the coordinates of a move to make in the format 
	 * intArray[0] = x and intArray[1] = y 
	 */
	public int[] aiMakeMove() {
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
			hasFirstLocation = true;
				
			returnArray[0] = pickX;
			returnArray[1] = pickY;
			
		
		} else {
			
			
			
			
			
			
		}
		return returnArray;
	}
}
		