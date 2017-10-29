/**
 * Forest class that setups maze and in-game screens in preparation for a game.
 * In-game screens include the home screen and the maze game screen.
 * All players and controllers are configured.
 */
public class Forest {

	private Maze maze; 
	private Student student; 
	private Controller controller;
	
	private int difficulty;
	private int score; 
	private int level; 
	private boolean finishedLevel;
	
	  private volatile boolean inGame;	//whether or not the game has not been exited yet
	    private volatile boolean isGameOver;	//whether or not player has lost the game
	    										//or has passed all the levels
	  //Game constants
	    /**Describes constant for the width of the maze at the starting level (on medium difficulty).*/
	    private static final int START_LEVEL_WIDTH = 11;
	    /**Describes constant for the height of the maze at the starting level (on medium difficulty).*/
	    private static final int START_LEVEL_HEIGHT = 11;
	    private static final int POINTS_CACTUS_KILLED = 10;
	    /**Describes constant for the maximum number of levels in the maze game.*/
	    public static final int MAX_LEVEL = 10;
	
	
	    //Difficulty constants
	    /**Describes constant for easy difficulty.*/
	    public static final int EASY = -1;
	    /**Describes constant for medium difficulty.*/
	    public static final int MEDIUM = 0;
	    /**Describes constant for hard difficulty.*/
	    public static final int HARD = 1;  
	
	    /**
	     * Constructor for creating a maze game.
	     * All fields set to default values, with a new student
	     * created with the default name and character.
	     * Difficulty is set on medium by default.
	     */
	    public Forest() {
	        isGameOver = false;
	        inGame = false;
	        //Make one player
			student = new Student("Mazin Singh", "link");
	        score = 0;	//initially zero score
	        level = 0;	//initially zero level (shown as Level 1, as counting from 1)
	        difficulty = MEDIUM;	//default difficulty is medium
	    }
	    
	    /**
	     * Create a maze which scales in size based on game level and difficulty.
	     */
	    public void createMaze() {
	    	//creates a new maze
	    	this.maze =  new Maze(START_LEVEL_WIDTH + (2*(level+difficulty)), 
	    						START_LEVEL_HEIGHT + (2*(level+difficulty)), student);
			controller = new Controller(maze);	//set controller to the maze
	    }
	    
	    // Updates the status of whether the game is over or not. 
	    //The game is over when a student is dead
	    
	    public void setIsGameOver(boolean isGameOver){
	    	this.isGameOver=isGameOver;
	    }
	    
	    //Check if the game is over or not 
	    public boolean isGameOver(){
	    	return isGameOver;
	    }
	    
	    //if the player is still in the maze, update its status
	    public void setIsInGame(boolean inGame) {
	    	this.inGame = inGame;
	    }
	    
	    //check if the student is still in the maze
	    public boolean isInGame() {
	    	return inGame;
	    }
	    
	   //set the controller for the player
	    public void setController(Controller controller) {
	    	this.controller = controller;
	    }
	    
	    //gets the controller for the student
	    public Controller getController() {
	    	return controller;
	    }
	    
	    //get the student of the game
	    public Student getStudent(){
	    	return this.student; 
	    	
	    }
	   //get the maze of the game
	    public Maze getMaze(){
	    	return this.maze;
	    }
	   
	    /**
	     * Updates the game score.
	     * The game is dependent on the number of items collected
	     * by the player, and the number of cactuses killed.
	     */
	    public void updateScore(){
	    	//1 point for each treasure collected
	    	//10 points for each cactus killed
	    	score = student.getNumTreasureCollected() + POINTS_CACTUS_KILLED * student.getCactusKilled();
	    }
	    
	    /**
	     * Gets the current score of the game.
	     * @return the current score of the game.
	     */
		public int getScore() {
			return score;
		}
		/**
		 * Checks if the game should be on the next level,
		 * and updates the game state accordingly.
		 * Next level should be reached when player exits the room.
		 */
		public void checkNextLevel () {
			 //If level is complete
			 if (maze.exitedMaze()) {
				finishedLevel = true;	//current level has been completed
				level++;
				student.clearInventory();	//clear inventory once next level
				 if (level == MAX_LEVEL) {
					 setIsGameOver(true);	//end game if passed all levels
					 setIsInGame(false);
				 } else {
					 createMaze();	//create a new maze for the next level
				 }
			 }
		}

		/**
		 * Get the current level of the game.
		 * @return the current level of the game.
		 */
		public int getLevel () {
			return level;
		}
		
		/**
		 * Sets the difficulty of the game.
		 * @param difficulty the difficulty of the game.
		 * Defined in Forest.
		 */
		public void setDifficulty (int difficulty) {
			this.difficulty = difficulty;
		}
		
		/**
		 * Checks if the maze of the current level has been completed.
		 * This information is mainly used by ForestManager to determine
		 * what screens to show.
		 * @return true if the maze of the current level has been completed.
		 */
		public boolean getFinishedLevel() {
			return finishedLevel;
		}
		
		public void setFinishedLevel(boolean b) {
			finishedLevel = b;
		}
		/**
		 * Clears the progress of the player in the game.
		 */
		public void clearGame() {
	    	//set score and level back to zero.
	    	score = 0;
	    	level = 0;
		}
}
