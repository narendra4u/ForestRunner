public interface MazeMaker {
	/**
 * Generates a maze of a certain width and height.
 * @param width the desired width (including border).
 * @param height the desired height (including border).
 * @return a grid of tiles characterizing the maze.
 */
	public Cell[][] genMaze(int width, int height);
}
