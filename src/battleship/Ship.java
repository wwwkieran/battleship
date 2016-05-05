package battleship;

public class Ship {
	
	protected int size;
	protected int xStartingCoordinate;
	protected int yStartingCoordinate;
	protected int xEndingCoordinate;
	protected int yEndingCoordinate;
	protected int Orientation; // 0 if horizontal, 1 if vertical
	boolean sunk;
	
	//constructor 
	
	public Ship (int size, int xstart, int ystart, int xend, int yend) {
		this.size = size;
		this.sunk = false;
		this.xStartingCoordinate = xstart;
		this.yStartingCoordinate = ystart;
		this.xEndingCoordinate = xend;
		this.yEndingCoordinate = yend;
		this.Orientation = checkOrientation(xstart, ystart, xend, yend);
	}
	
	// instance methods
	
	public int checkOrientation(int xstart, int ystart, int xend, int yend) {
		if(xstart == xend) {
			return 0;
		} else {
			return 1;
		}
	}
	
	public boolean isSunk() {
		// returns true if ship object is sunk 
		// meaning all squares of ship are hit(3)
		// false otherwise
		
		return true;
	}
	
	// return methods
	
	public int size() {
		return size;
	}
	public int xStartingCoordinate() {
		return xStartingCoordinate;
	}
	public int yStartingCoordinate() {
		return yStartingCoordinate;
	}
	public int xEndingCoordinate() {
		return xEndingCoordinate;
	}
	public int yEndingCoordinate() {
		return yEndingCoordinate;
	}
	public int Orientation() {
		return Orientation;
	}
	
	
	
	
}
