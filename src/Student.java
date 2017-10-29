import java.util.ArrayList;

/*
 * Player of the maze game, he/she is suppose to be a student moving from 
 * PUIO to the Orsay Campus throughout the forest. 
 * Contains all player information including name, character
 * location, status (dear or alive), inventory items and statistics
 * involving number of cactuses cropped and items collected
 */


public class Student {

	private String name;
	private String character; //probably we don't need it
	private Tile location;
	private boolean isDead; 
	
	private ArrayList<Boolean> inventory;
	private int numTreasuresCollected;
	private int cactusKilled;
	
	//describing the item number of each item in the inventory
	//Constant for student card used to open the 336 buidling door
	public static final int SCARD = 0;
	//Constant for knife tool used to cut the cactus
	public static final int KNIFE=1;
	//Constant for root power identifying position in inventory
	private static final int ROOT_POWER=2;
	public static final int NUM_INVENTORY_ITEMS=3;
	
	//constructor for our student
	public Student(String name, String character){
		this.name=name;
		this.character=character;
		inventory = new ArrayList<Boolean>();
		for (int i=0;i<NUM_INVENTORY_ITEMS; i++){
			inventory.add(false);
		}
		
	}
	
	//getter and setter for the name of our player
	public String getName(){
		return this.name;
		
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	//getter and setter for the character of our student
	public String getCharacter(){
		return this.character;
	}
	

	public void setCharacter(String character){
		this.character = character;
	}
	
	//getter and setter for the location of the student in the game
	public Tile getLocation () {
		return location;
	}
		public void setLocation (Tile t) {
		location = t;
	}
	
	
	//Update the status of the Studen if he/she is dead or not
	//The player can be dead or alive.
	 // @param dead if true, the player is set to dead;
	 //if false, the player is set to alive.
	public void setDead (boolean dead) {
		isDead = dead;
	}

	/**
	 * Checks the status of the player.
	 * @return true if the player is dead.
	 */
	public boolean isDead() {
		return isDead;
	}
	/**
	 * Checks if an item has been collected by player.
	 * Items that expire their time limit will not
	 * exist in the player's inventory.*/
	
	public boolean isItemCollected(int itemNum) {
		return inventory.get(itemNum);
	}
	
	// Update whether an item is collected or not. If false the item is set to not collected
	
	public void setItemCollected(int itemNum, boolean collected) {
		inventory.set(itemNum,collected);
	}
	
	//get the total number of items collected by the player
	// for now we dont need it, we might add treasures later
	public int getNumTreasureCollected() {
		return numTreasuresCollected;
	}
	//add a item collected to the player
	public void addNumTreasureCollected() {
		this.numTreasuresCollected++;
	}

	/**
	 * Get the total number of cactuses killed by the player.
	 * This includes those killed in all levels of the game.
	 * @return the total number of enemies killed by the player.
	 */
	public int getCactusKilled() {
		return cactusKilled;
	}

	/**
	 * Increase the total number of enemies killed by the player by one.
	 */
	public void addCactusKilled() {
		this.cactusKilled++;
	}
	
	//Reset our student stats
	public void clearStats() {
		location = null;
		isDead = false;	
		clearInventory();
		numTreasuresCollected = 0;
		cactusKilled = 0;
	}
	/**
	 * Clear all inventory items of the player.
	 */
	public void clearInventory() {
		for (int i = 0; i < NUM_INVENTORY_ITEMS; i++) {
			inventory.set(i,false);	//clear each item
		}
	}
}
