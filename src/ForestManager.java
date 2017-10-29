import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * System managing game flow. It determines what game screens to show and
 * updates the GUI of the game seen by the user.
 */
public class ForestManager {

	private ForestFrame forestframe; // the home screen
	private MazeFrame mazeframe; // the maze game screen
	private Forest f;

	public ForestManager() {
		f = new Forest();
	}

	public void run() {
		// create game frame
		// this is the home screen of the game
		forestframe = new ForestFrame(f, 800, 800);

		while (true) {
			// If not in game, do nothing (wait for play button to be clicked)
			if (!f.isInGame())
				continue;

			// initially, maze of game is created when the play button of game
			// frame is clicked
			// after that, Forest determines levelling system and updates the maze
			// representation
			// which is then updated by the ForestManager
			createNewMazeFrame(); // initialise maze
			long lastUpdateTime = System.currentTimeMillis();
			// while still in game
			while (!f.isGameOver()) {
				if (System.currentTimeMillis() - lastUpdateTime > 20) { // update
																		// every
																		// 20ms
					f.updateScore(); // update player score
					if (!f.getFinishedLevel()) {
						updateMazeFrame(); // update maze according to game
											// state
					} else {
						createNewMazeFrame(); // if levelled up, create a new
												// maze for the new level
						f.setFinishedLevel(false); // once done, set finished
													// current level to false
					}
					lastUpdateTime = System.currentTimeMillis(); // update time
				}
			}
			// the game is finished; home screen (game frame) is shown
			// game can be played repeatedly until user exits game
			showForestFrame();
		}
	}

	/**
	 * Update maze frame based on current game state. Also requests focus for
	 * the maze frame.
	 */
	private void updateMazeFrame() {
		this.mazeFrame.update(f.getMaze()); // update the maze frame according
											// to game state
		this.mazeFrame.getFrame().requestFocus();
		this.mazeFrame.getFrame().repaint();
		// special dialog boxes are shown on the maze frame depending on player
		// condition
		// forest manager manages these dialog boxes for maze frame
		if (f.getStudent().isDead()) {
			// if player died, so show end campaign dialog box
			Object[] options = { "End of your journey" };
			JOptionPane.showOptionDialog(mazeFrame.getFrame(), "You just got killed by a cactus!", "OH NO!",
					JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE,
					new ImageIcon(this.getClass().getResource("/sprites/dead_cactus.gif")), options, options[0]);
			// when user clicks the end campaign button
			f.setIsGameOver(true); // update game state to game over
			f.setIsInGame(false);
			mazeFrame.getFrame().requestFocus(); // request focus again
		} else if (f.getMaze().exitedMaze()) {
			Object[] options = { "Next level" };
			JOptionPane.showOptionDialog(mazeFrame.getFrame(),
					"The next journey awaits you...\n"
							+ "What unknown challenges lay ahead? Will you be able to get to Orsay?",
					"Room " + (f.getLevel() + 1) + " cleared!", // levels count
																// from 0, so +1
																// offset to
																// count from 1
					JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE,
					new ImageIcon(this.getClass().getResource("/sprites/door_open.gif")), options, options[0]);

			// When user pressed next level or closes dialog, go to next level
			f.checkNextLevel(); // change game state so that next level is
								// reached
			mazeFrame.getFrame().requestFocus(); // request focus again
		}
	}

	/**
	 * Show home screen (forest frame) and removes the game screen (maze frame).
	 * Special ending dialogue box is shown if the player finished all levels of
	 * the game. The statistics of the player are then cleared.
	 */
	private void showForestFrame() {
		// end game dialog if user finishes all levels
		if (f.getLevel() == Forest.MAX_LEVEL) {
			Object[] options = { "Exit" };
			JOptionPane.showOptionDialog(this.mazeFrame.getFrame(),
					"Congratulations, student!\n" + "Your skill is worthy of mention but who knows\n"
							+ "what challenges we may see ahead?\n"
							+ "We will require your assistance when the time comes...",
					"Btm 336 reached!", 1, 0, new ImageIcon(this.getClass().getResource("/sprites/door_open.gif")),
					options, options[0]);
		} // otherwise no special dialog is displayed
		f.getPlayer().clearStats(); // clear all player stats
		f.clearGame(); // clear player progress in game
		mazeFrame.getFrame().dispose(); // remove maze
		forestframe.getFrame().setVisible(true); // show home screen
	}

	/**
	 * Creates a new game screen (maze frame) according to the game state. It
	 * will dispose the old version and recreate a new game screen, and ensures
	 * the game's controller is connected to the new game screen.
	 */
	private void createNewMazeFrame() {
		// dispose the previous maze frame
		if (mazeFrame != null) {
			this.mazeFrame.getFrame().dispose();
		}
		// make new maze frame according to the maze made by Game
		this.mazeFrame = new MazeFrame(f, f.getMaze().getWidth(), f.getMaze().getHeight());
		// initialise maze that was created
		this.mazeFrame.init(g.getMaze());
		this.mazeFrame.getFrame().requestFocus();
		this.mazeFrame.getFrame().addKeyListener(f.getController()); // update
																		// controller
																		// to
																		// new
																		// maze
	}

}
