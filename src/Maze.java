import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Contains the board of the maze.
 * It contains the maze-generating algorithm
 * and the maze solving algorithm.
 * All information about where the player and enemies are
 * are also stored in the maze.
 */
public class Maze {
	private Cell[][] grid;	//all Cells in the grid
	private int width;	//width of maze including border
	private int height;	//height of maze including border
	private Student student;	//the current player
	private Cactus[] cactus;	//all the enemies
	
	/**
	 * Constructor for the maze.
	 * @param w the width of the maze.
	 * @param h the height of the maze.
	 * @param p the player of the maze.
	 */
	public Maze (int w, int h, Student s) {
		//width and height is assumed to be greater than 1
		grid = new Cell[w+2][h+2];	//add 2 for border around maze
		this.width = w+2;
		this.height = h+2;
		
		student = s;	//use input player
		//formula for number of enemies is 1 more enemy per 1.5 levels
		cactus = new Cactus[(int)(((width-13)/3)+1)];
		for (int i = 0; i < cactus.length; i++) {
			cactus[i] = new Cactus(); 	//make new enemy
		}
		//generate maze using Prim's algorithm
		MazeMaker mazeMaker = new MazeGeneratorPrim();
		createMaze(mazeMaker);	//initialise all Cells including
						//player location, enemy locations, coins, powerups, doors
		startEnemy();	//updates enemy location
	}


	/**
	 * Generate the maze.
	 * This should be called only once when initialising the maze.
	 */
	private void createMaze(MazeMaker mazeMaker) {
		grid = mazeMaker.genMaze(width, height);
		
		//set player and enemy locations
		student.setLocation(grid[1][1]);	//origin at (1,1), 
		int numEnemy = 0;
		while (numEnemy < cactus.length) {	//enemy spawns on bottom third of the screen
			int randomX = 1 + (int)(Math.random()*(width-2));	//any random x value
			int randomY = ((height-2)/3)*2 + (int)(Math.random()*((height-2)/3)+1);	//y value on bottom third
			if (grid[randomX][randomY].getType() == Cell.PATH &&	//check that the Cell is walkable and not where player is
				!grid[randomX][randomY].equals(student.getLocation())) {
				cactus[numEnemy].setLocation(grid[randomX][randomY]);	//enemy starts randomly
				numEnemy++;
			}
		}
		
		//set special Cells
		grid[1][0].setType(Cell.BTM336);	//set Cell just above the origin to a door
		grid[width-2][height-1].setType(Cell.BTM336);	//set Cell just below the destination to a door

		grid[1][height-2].setType(Cell.SCARD);	//set bottom left corner to key to door
		grid[width-2][1].setType(Cell.KNIFE);	//set top right to sword
		
		int numRootPower = 0;
		//first ice power is always in the top left quadrant
		while (true) {
			int randomX = 1 + (int)(Math.random()*((width-2)/2));	//any random X in maze
			int randomY = 1 + (int)(Math.random()*((height-2)/2));	//any random Y in maze
			if (grid[randomX][randomY].getType() == Cell.PATH &&	//check that the Cell is walkable
				!grid[randomX][randomY].equals(student.getLocation())) {	//and not where player is
				grid[randomX][randomY].setType(Cell.ROOT_POWER);
				numRootPower++;	//update number of ice power added
				break;	//go to see if other ice powers are to be added
			}
		}
		//other ice powers can spawn elsewhere
		while (numRootPower < cactus.length-1) {	//one ice power for each enemy, minus one (harder)
			int randomX = 1 + (int)(Math.random()*(width-2));	//any random X in maze
			int randomY = 1 + (int)(Math.random()*(height-2));	//any random Y in maze
			if (grid[randomX][randomY].getType() == Cell.PATH &&	//check that the Cell is walkable
				!grid[randomX][randomY].equals(student.getLocation())) {	//and not where player is
				grid[randomX][randomY].setType(Cell.ROOT_POWER);
				numRootPower++;	//update number of ice power added
			}
		}
		
		//sets three random Cells to treasure
		//formula for number of treasure is 1 more treasure per 1.5 levels
		int i = 0;
		while (i < (int)(width-13)/3 + 3) {
			int randomX = 1 + (int)(Math.random()*((width-2)));		//any random X
			int randomY = 1 + (int)(Math.random()*((height-2)));	//any random Y
			if (grid[randomX][randomY].getType() == Cell.PATH &&	//check that the Cell is walkable
				!grid[randomX][randomY].equals(student.getLocation())) {		//and not where player is
				//grid[randomX][randomY].setType(Cell.SITEM);
				i++;	//update treasure added
			}
		}
		//showMaze();			//for debugging
	}
	
	/**
	 * Displays ASCII form of basic maze.
	 * Shows where walls and paths are,
	 * and where player is.
	 */
	public void showMaze () {
		Cell studentLoc = student.getLocation();
		for (int j = 0; j < width; j++) {
			for (int i = 0; i < height; i++) {
				if (grid[i][j].isWalkable()) {
					//order of printing is important as path contains start and dest
					if (studentLoc != null && studentLoc.equals(grid[i][j])) {
						System.out.print("P");
					} else if (i == (width-1)-1 && j == (height-1)-1) {
						System.out.print("D");
					} else {
						System.out.print("0");
					}
				} else {
					System.out.print("-");	//represents wall
				}
			}
			System.out.println();
		}
	}
	
	/**
	 * Finds the path from (x,y) to the destination.
	 * It stores the path in an input hash map,
	 * which maps the Cell to its parent Cell.
	 * Credit to http://en.wikipedia.org/wiki/Maze_solving_algorithm.
	 * Adapted from "Recursive algorithm".
	 * @return true if a path exists. It should be true
	 * as the maze is always solvable from any position.
	 */
	public boolean findPath (int x, int y, boolean[][] visited, HashMap<Cell, Cell> parent) {
		if (x == width-2 && y == height-2) return true;
		if (!grid[x][y].isWalkable() || visited[x][y]) return false;
		visited[x][y] = true;
		if (x != 0) {
			if (findPath(x-1,y,visited,parent)) {
				parent.put(grid[x-1][y], grid[x][y]);
				return true;
			}
		}
		if (x != width-1) {
			if (findPath(x+1,y,visited,parent)) {
				parent.put(grid[x+1][y], grid[x][y]);
				return true;
			}
		}
		if (y != 0) {
			if (findPath(x,y-1,visited,parent)) {
				parent.put(grid[x][y-1], grid[x][y]);
				return true;
			}
		}
		if (y != height-1) {
			if (findPath(x,y+1,visited,parent)) {
				parent.put(grid[x][y+1], grid[x][y]);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Finds the path from (x,y) to (x1,y1).
	 * It stores the path in an input hash map,
	 * which maps the Cell to its parent Cell.
	 * Credit to http://en.wikipedia.org/wiki/Maze_solving_algorithm.
	 * Adapted from "Recursive algorithm".
	 * @return true if a path exists. It should be true
	 * as the maze is always solvable from any position.
	 */
	public boolean findPath (int x, int y, int x1, int y1, boolean[][] visited, HashMap<Cell,Cell> parent) {
		if (x == x1 && y == y1) return true;
		if (!grid[x][y].isWalkable() || visited[x][y]) return false;
		visited[x][y] = true;
		if (x != 0) {
			if (findPath(x-1,y,x1,y1,visited,parent)) {
				parent.put(grid[x-1][y], grid[x][y]);
				return true;
			}
		}
		if (x != width-1) {
			if (findPath(x+1,y,x1,y1,visited,parent)) {
				parent.put(grid[x+1][y], grid[x][y]);
				return true;
			}
		}
		if (y != 0) {
			if (findPath(x,y-1,x1,y1,visited,parent)) {
				parent.put(grid[x][y-1], grid[x][y]);
				return true;
			}
		}
		if (y != height-1) {
			if (findPath(x,y+1,x1,y1,visited,parent)) {
				parent.put(grid[x][y+1], grid[x][y]);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Give first 10 steps from the current Cell to the destination.
	 * If less than 10 steps away, all Cells to destination are shown
	 * @param t the current Cell
	 * @return the list of 10 Cells in the path from the current Cell to the destination,
	 * or all Cells to destination if less than 10 Cells away.
	 */
	public List<Cell> giveHint(Cell t) {
		//TODO change colour of Cells such that path from current player position
		//to the nearest Cell part of the solution is given
		boolean[][] visited = new boolean[width][height];
		HashMap<Cell,Cell> parent = new HashMap<Cell,Cell>();
		parent.put(t, null);	//current Cell has no parent as it is the starting Cell of the path
		boolean pathFound = findPath(t.getX(), t.getY(), visited, parent);
		if (!pathFound) {
			return null;		//error in pathfinding
		} else {
			LinkedList<Cell> path = new LinkedList<Cell>();
			Cell curr = grid[width-2][height-2];	//backtrack from destination
			path.addFirst(curr);
			while (parent.get(curr) != null) {	//if we haven't reached the starting state yet
				Cell prev = parent.get(curr);
				path.addFirst(prev);
				curr = prev;
			}
			List<Cell> pathInit = path.subList(0,Math.min(10,path.size()));
			return pathInit;
		}
	}
	
	/**
	 * Finds the Cell on the maze that the player is situated in.
	 * @return the Cell in which the player is on, or null if player is dead
	 */
	public Cell getStudentCell() {
		if (student.isDead()) {
			return null;
		}
		return student.getLocation();
	}
	
	/**
	 * Finds the Cell on the maze that the cactus is situated on.
	 * @param i the ID of the cactus (ith cactus).
	 * @return the Cell in which the cactus is on, or null if cactus is dead or doesn't exist.
	 */
	public Cell getCactusCell(int i) {
		if (i >= cactus.length || cactus[i].isDead()) {
			return null;
		}
		return cactus[i].getLocation();
	}
	
	/**
	 * Checks if a Cell is one where an enemy is situated on.
	 * @param t the Cell of interest.
	 * @return true if the Cell is one where an enemy is situated on.
	 */
	public boolean isCactusCell (Cell t) {
		for (int i = 0; i < cactus.length; i++) {
			if (!cactus[i].isDead() && cactus[i].getLocation().equals(t)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get the Cell where the destination door is situated on
	 * @return the Cell where the destination door is situated on
	 */
	public Cell getDestDoor() {
		return grid[width-2][height-1];
	}
	
	/**
	 * Updates the player location if a valid move is taken.
	 * See isValid() for conditions.
	 * @param x the amount of movement in the x direction, as per number of Cells.
	 * The number is negative if the movement is to the left direction.
	 * @param y the amount of movement in the y direction, as per number of Cells.
	 * The number is negative if the movement is to the up direction.
	 */
	public void updateStudentLoc (int x, int y) {
		if (isValid(x,y) && !student.isDead()) {
			student.setLocation(grid[student.getLocation().getX()+x][student.getLocation().getY()+y]);
			Cell studentLoc = student.getLocation();	//get updated player location
			for (int i = 0; i < cactus.length; i++) {
				Cell cactusLoc = cactus[i].getLocation();	//get cactus location
				if (studentLoc.equals(cactusLoc) && !cactus[i].isDead()) {
					if (!itemCollected(Student.KNIFE) && !itemCollected(Student.ROOT_POWER)) {
						student.setDead(true);	//player dies and cactus stops moving
					} else {
						cactus[i].setDead(true);
						student.addCactusKilled();
					}
				}
			}
			if (studentLoc.getType() == Cell.SCARD) {
				student.setItemCollected(Student.SCARD, true);
				studentLoc.setType(Cell.PATH);	//set key Cell to normal path
			} else if (studentLoc.getType() == Cell.SITEM) {
				student.addNumTreasureCollected();
				studentLoc.setType(Cell.PATH);	//if we collected the treasure
			} else if (studentLoc.getType() == Cell.KNIFE) {
				student.setItemCollected(Student.KNIFE, true);
				studentLoc.setType(Cell.PATH);
			} else if (studentLoc.getType() == Cell.ROOT_POWER) {
				student.setItemCollected(Student.ROOT_POWER,true);
				studentLoc.setType(Cell.PATH);
			}
			checkReachedEnd();	//unlock door if player has reached end Cell
		}
	}
	
	/**
	 * Begins updating enemy movement in the maze.
	 * Each move is scheduled at every 1.5 seconds.
	 */
	private void startEnemy() {
		final Timer timer = new Timer();	//auto-scheduling of enemy movement
		timer.schedule(new TimerTask() {
			public void run() {
				for (int i = 0; i < cactus.length; i++) {	//update location of all enemies
					if (student.isDead()) {
						timer.cancel();
					} else if (cactus[i].isDead()) {	//if cactus is already dead
						continue;
					} else if (student.isItemCollected(Student.ROOT_POWER)) {
						try {
							Thread.sleep(5000);	//cactus freezes for 5 seconds
							student.setItemCollected(Student.ROOT_POWER,false);	//ice power disappears after 5 seconds
						} catch (InterruptedException e) {
							//do nothing
						}
					} else if (student.getLocation() != null) {	//if player is not dead
						//cactus follows player
						Cell cactusLoc = cactus[i].getLocation();
						HashMap<Cell,Cell> path = new HashMap<Cell,Cell>();
						path.put(grid[cactusLoc.getX()][cactusLoc.getY()], null);
						boolean[][] visitedCell = new boolean[width][height];
						findPath(cactusLoc.getX(), cactusLoc.getY(),
								student.getLocation().getX(), student.getLocation().getY(),
								visitedCell,path);
						Cell curr = student.getLocation();
						//backtracking through path to find next Cell to go
						while (!path.get(curr).equals(cactusLoc)) {	//if we haven't found the next Cell to go
							curr = path.get(curr);
						}
						cactus[i].setLocation(curr);	//update cactus location
						if (cactus[i].getLocation().equals(student.getLocation())) {
							if (!itemCollected(Student.KNIFE)) {
								student.setDead(true);	//player dies and cactus stops moving
							} else {
								cactus[i].setDead(true);	//player can kill enemies with sword
								student.addCactusKilled();
							}
						}
					} //else don't move enemy
					//showMaze();	//for debugging
				}
			}
		},1500,1500);	//enemy moves every 1.5 seconds
						//if too fast, player cannot reach sword and will eventually die
	}
	
	/**
	 * Checks if a player is dead.
	 * A player is dead if it encounters the enemy without a sword.
	 * @return true if the player is dead.
	 */
	public boolean playerDied () { return student.isDead(); }
	/**
	 * Checks if an enemy is dead.
	 * An enemy is dead if it encounters the player who has a sword.
	 * @param i stands for ith enemy
	 * @return true if the enemy is dead.
	 */
	public boolean cactusDied (int i) { return cactus[i].isDead(); }
	
	/**
	 * Checks if all enemies in the maze are dead.
	 * @return true if all enemies in the maze are dead.
	 */
	public boolean allEnemyDied () { 
		for (int i = 0; i < cactus.length; i++) {
			if (!cactus[i].isDead()) {	//check each enemy for status
				return false;
			}
		}
		return true; 
	}
	
	/**
	 * Get number of enemies in the maze initially
	 * @return the number of enemies in the maze initially
	 */
	public int getNumCactus () { return cactus.length; }
	
	/**
	 * Checks if a player move is valid.
	 * The player can only move one Cell from its original position
	 * in either direction.
	 * It cannot move over the border of the maze,
	 * or walk onto a wall.
	 * @param x the amount of movement in the x direction, as per number of Cells.
	 * The number is negative if the movement is to the left direction.
	 * @param y the amount of movement in the y direction, as per number of Cells.
	 * The number is negative if the movement is to the up direction.
	 * @return true if the player move is valid.
	 */
	public boolean isValid (int x, int y) {
		Cell studentLoc = student.getLocation();
		if (x > 1 || x < -1 || y > 1 || y < -1) {	//can only move at most one Cell per turn
			return false;
		} else if (x != 0 && y != 0) {	//can only move in one direction
			return false;
		} else if ((studentLoc.getX()+x) > (width-1) || (studentLoc.getY()+y) > (height-1)
			|| (studentLoc.getX()+x) < 0 || (studentLoc.getY()+y) < 0) {	//cannot move over border
			return false;
		} else if (!grid[studentLoc.getX()+x][studentLoc.getY()+y].isWalkable()) { //cannot move to wall
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the player has reached the destination.
	 * The destination is the Cell right before the exit door.
	 * If the key is collected, then the door is unlocked as well;
	 * otherwise, it remains closed.
	 * To check if the player takes the exit, see exitedMaze().
	 * @return true if the player has reached the destination.
	 */
	public boolean checkReachedEnd() {
		Cell studentLoc = student.getLocation();
		if (student.isDead()) {
			return false;
		}
		//destination is at (width-2, height-2)
		boolean atEnd = false;
		if (studentLoc.getX() == (width-2) && studentLoc.getY() == (height-2)) {
			atEnd = true;
			if (itemCollected(Student.SCARD)) {	//if key is collected, unlock door
				grid[width-2][height-1].setType(Cell.PATH);	//set door to walkable path
				grid[width-2][height-1].setWalkable();
			}
		}
		return atEnd;
	}
	
	/**
	 * Checks if the player is at the origin.
	 * The origin of the maze is at (1,1),
	 * just in front of the entrance door.
	 * @return true if the player is at the origin.
	 */
	public boolean atStart() {
		return (!student.isDead() && student.getLocation().getX() == 1 && student.getLocation().getY() == 1);
	}
	
	/**
	 * Checks if the player has completed the maze.
	 * It is completed when the destination door is opened by the key
	 * and the player is located on the exit, where the door was originally
	 * positioned.
	 * @return true if player has completed the maze.
	 */
	//see if player unlocked door and took the exit
	public boolean exitedMaze() {
		return (!student.isDead() && student.getLocation().getX() == (width-2) && student.getLocation().getY() == (height-1));
	}
	
	/**
	 * Get number of treasure collected.
	 * @return the number of treasure collected
	 */
	public int getNumTreasureCollected() { return student.getNumTreasureCollected(); }
	
	/**
	 * Check if item is collected.
	 * @param itemNum the ID of the item as defined in Player
	 * @return true if item is collected
	 */
	public boolean itemCollected(int itemNum) { return student.isItemCollected(itemNum); }
	
	/**
	 * Get the Cell of a specific set of x and y coordinates.
	 * (0,0) is the top left corner of the maze (includes border).
	 * If a set of coordinates outside of the bounds of the maze is entered,
	 * the null Cell is returned.
	 * @param x the x coordinate of the Cell.
	 * @param y the y coordinate of the Cell.
	 * @return the Cell with the specified x and y coordinates, or null if
	 * the coordinates are outside of the maze area.
	 */
	public Cell getCell(int x, int y) {
		if (x >= 0 && y >= 0 && x <= (width-1) && y <= (height-1)) {
			return this.grid[x][y];
		} else {
			return null;
		}
	}
	/**
	 * Get the width of the maze (including border).
	 * @return the width of the maze, as per the number of Cells.
	 */
	public int getWidth() { return this.width; }
	/**
	 * Get the length of the maze (including border).
	 * @return the length of the maze, as per the number of Cells.
	 */
	public int getHeight() { return this.height; }
	/**
	 * Gets the grid of Cells of the maze.
	 * @return the grid of Cells of the maze.
	 */
	public Cell[][] getGrid() { return this.grid; }
}