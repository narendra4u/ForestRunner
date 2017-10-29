/*
 * Cactus is the enemy of our student, 
 * he/she is followed by the cactus throghout the journey to the btm336 until 
 * he/she freezes it or kills it and moves on. 
 * The cactus is able to kill the student under circumstances outlined in Maze
 */
public class Cactus {
boolean isDead; 
Tile location; //where cactus is located in the maze



//constructor for the cactus, with on a specifit tile on the maze
public Cactus (Tile t){
	location=t;
}
// empty, when the location is not yet decided
public Cactus(){
	
}

//updates the status of the cactus, if true then dead
public void setDead( boolean dead){
	isDead=dead;
}

public boolean isDead(){
	return isDead;
}

//get the locaiton of the cactus in the maze
public Tile getLocation(){
	return location;
}

//updates the locaiton of the cactus in the maze
public void setLocation(Tile t){
	location=t;
}


}
