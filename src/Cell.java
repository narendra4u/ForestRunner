
public class Cell {
	public static final int FOREST = 0;
	public static final int PATH = 1;
	public static final int SCARD = 2;
	public static final int SITEM = 3; 
	public static final int BTM336 = 4;
	public static final int KNIFE = 5;
	public static final int ROOT_POWER = 0;
	
	private int x;  // co-ordinates of the cell
	private int y;
	private boolean isWalkable; // is the cell walkable
	private int type;  // type of cell
	
	public Cell (boolean isWalkable, int x, int y) {
		this.x = x;
		this.y = y;
		this.isWalkable = isWalkable;
		this.type = FOREST;  // default cell that is displayed
	}
	
	public Cell() {
		
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * @return the y
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * @return the isWalkable
	 */
	public boolean isWalkable() {
		return isWalkable;
	}

	/**
	 * @param isWalkable the isWalkable to set
	 */
	public void setWalkable() {
		this.isWalkable = true;
		this.type = PATH;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public boolean setType(int newType) {
		if (newType >= 0 && newType <=6) {
			this.type = newType;
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cell other = (Cell) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}	
}
