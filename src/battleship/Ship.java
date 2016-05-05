package battleship;

public class Ship {
	
	protected int size;
	protected int xStartingCoordinate;
	protected int yStartingCoordinate;
	protected int xEndingCoordinate;
	protected int yEndingCoordinate;
	
	//constructor 
	
	public Ship (int size, int xstart, int ystart, int xend, int yend) {
		this.size = size;
		this.sunk = false;
		
		//Make sure that xend and yend are bigger than xstart and ystart, repectively
		if (xstart > xend) {
			this.xStartingCoordinate = xend;
			this.xEndingCoordinate = xstart;
		} else {
			this.xStartingCoordinate = xstart;
			this.xEndingCoordinate = xend;
		}
		
		if (ystart > yend) {
			this.yStartingCoordinate = xend;
			this.yEndingCoordinate = xstart;
		} else {
			this.yStartingCoordinate = xstart;
			this.yEndingCoordinate = xend;
		}
	}
	
	// instance methods
	
	public int checkOrientation(int xstart, int ystart, int xend, int yend) {
		if(xstart == xend) {
			return 0;
		} else {
			return 1;
		}
	}
	
	
	// return methods
	
	public int size() {
		return size;
	}
	public int startX() {
		return xStartingCoordinate;
	}
	public int startY() {
		return yStartingCoordinate;
	}
	public int endX() {
		return xEndingCoordinate;
	}
	public int endY() {
		return yEndingCoordinate;
	}

	
	
}
