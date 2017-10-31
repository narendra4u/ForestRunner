public class CellEdge {
	private Cell cell1;
	private Cell cell2;
	private Cell cell3;
	
	/* Constructor for the cell edge */
	
	public CellEdge(Cell cell1, Cell cell2, Cell cell3) {
		this.cell1 = cell1;
		this.cell2 = cell2;
		this.cell3 = cell3;
	}
	
	/**
	 * Checks if the Cell edge contains a particular cell.
	 * @return true if the cell is contained in the cell edge.
	 */
	
	public boolean hasCell (Cell c) {
		return this.cell1.equals(c) || this.cell2.equals(c) || this.cell3.equals(c);
	}

	/**
	 * @return the cell1
	 */
	public Cell getCell1() {
		return cell1;
	}

	/**
	 * @return the cell2
	 */
	public Cell getCell2() {
		return cell2;
	}

	/**
	 * @return the cell3
	 */
	public Cell getCell3() {
		return cell3;
	}
	
	// Display the edge
	
	public void displayEdge() {
		System.out.println(cell1.getX() + " " + cell1.getY() + " "
							+ cell3.getX() + " " + cell3.getY());
	}

	/* hashcode and equals method overrides */
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cell1 == null) ? 0 : cell1.hashCode());
		result = prime * result + ((cell2 == null) ? 0 : cell2.hashCode());
		result = prime * result + ((cell3 == null) ? 0 : cell3.hashCode());
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
		CellEdge other = (CellEdge) obj;
		if (cell1 == null) {
			if (other.cell1 != null)
				return false;
		} else if (!cell1.equals(other.cell1))
			return false;
		if (cell2 == null) {
			if (other.cell2 != null)
				return false;
		} else if (!cell2.equals(other.cell2))
			return false;
		if (cell3 == null) {
			if (other.cell3 != null)
				return false;
		} else if (!cell3.equals(other.cell3))
			return false;
		return true;
	}	
}
