import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * GUI class for the maze game.
 * Responsible for displaying the current game state of the maze 
 * in addition to a side panel containing relevant game variables and navigation options.
 * String identifier constants are used to determine which sprite image to display.
 */
public class MazeFrame {

	private int height;	//Dimensions of the maze frame
	private int width;
	
	private Forest f;	//Reference to the game state to allow querying for information
	
	//Frame components
	private JFrame frame;
	private JPanel mazeGrid;	//panel where maze grid is placed in
	private JLayeredPane[][] mazeGridComp;	//allows access to each tile in the grid
	private Dimension blockSize;	//the size of each tile in the maze grid
	
	//Side panel components
	private JPanel sidePanel;
	private JLabel score;	//shows the score of the player
	private JLabel level;	//shows level of game;
	private JButton exitButton;	//gives player option to quit
	private JButton hintButton;	//shows hint on maze when pressed
	private ArrayList<JLabel> inventory; //labels for the inventory items
	
	private Cell lastStudentPos;	//stores where the player last was
	private Cell[] lastCactusPos;	//stores where the enemies last were
	private boolean updatedOnce;
	
	//Store sprites once in hash map for easy access
	private HashMap<String, Sprite> sprites;	

	//All background tiles including wall and path
	public static final String wallSprite = "steel_wall";
	public static final String pathSprite = "carpet";
	
	//Character sprite constants
	private String studentSprite;
	/**String identifier for the character Link.*/
	public static final String gavinSprite = "link";
	/**String identifier for the character William.*/
	public static final String WilliamSprite = "william";
	/**String identifier for the character Kainen.*/
	public static final String KainenSprite = "kainen";

	//Other block sprite constants
	/**String identifier for the doors of the maze.*/
	public static final String doorSprite = "locked_door";
	/**String identifier for the key.*/
	public static final String scardSprite = "key";
	/**String identifier for the coin.*/
	public static final String coinSprite = "coin";
	/**String identifier for the enemy.*/
	public static final String cactusSprite = "cyan_pacman_monster";
	/**String identifier for the frozen enemy.*/
	public static final String killableCactusSprite = "dead_pacman_monster";
	/**String identifier for the sword.*/
	public static final String knifeSprite = "master_sword";
	/**String identifier for the ice power.*/
	public static final String rootSprite = "snowflake";
	/**String identifier for the hint.*/
	public static final String hintSprite = "hint";
	
	
	/**
	 * Constructor for the maze frame.
	 * 
	 * @param game - Game object with game state information.
	 * @param width - Width of maze grid to be painted (number of tiles).
	 * @param height - Height of maze grid to be painted (number of tiles).
	 */
	public MazeFrame(Forest forest, int width, int height) {
		//Set up MazeFrame with provided parameters
		frame = new JFrame();	//make new frame to place maze frame components in
		this.height = height; 	//number of tiles in y direction
		this.width = width;		//number of tiles in x direction
		this.f = forest;		//save reference to game state
		
		//Set up side panel
		mazeGrid = new JPanel();
		mazeGridComp = new JLayeredPane[this.width][this.height];
		sidePanel = new JPanel();
		exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {	//Anonymous actionListerner class, Shows dialog box giving exit option
			public void actionPerformed(ActionEvent e) {
				Object[] options = {"Yes", "Cancel"};
				Icon icon = UIManager.getIcon("OptionPane.informationIcon");
				
				int dialogResult = JOptionPane.showOptionDialog(frame, "Are you sure you want to exit to the main menu?\n\nAll game progress will be lost.", "Exit Warning", 
																JOptionPane.YES_NO_OPTION, JOptionPane.YES_NO_OPTION, icon, options, options);
				//If user wishes to quit
				if (dialogResult == JOptionPane.YES_OPTION) {
					f.setIsGameOver(true);
					f.setIsInGame(false);
				} else {
					frame.requestFocus();	//request focus again
				}
			} 
		});
		
		hintButton = new JButton("Hint");
		hintButton.addActionListener(new ActionListener() {	//Anonymous actionListerner class, shows hint tiles on maze
			public void actionPerformed(ActionEvent e) {
				Maze m = f.getMaze();
				List<Cell> hintCells = m.giveHint(m.getStudentCell());
				for (int i = 0; i < hintCells.size(); i++) {
					JLabel hintImage = new JLabel(sprites.get(hintSprite).getSprite());
					//add hint tile to second layer from the top
					int numComponents = mazeGridComp[hintCells.get(i).getX()][hintCells.get(i).getY()].getComponentCount();
					if (numComponents >= 3) {	//if more than 3 components, a hint tile was already added, so don't add again
						continue;
					}
					//if only one component that is a path tile, add a hint tile on top
					if (numComponents == 1 && f.getMaze().getCell(hintCells.get(i).getX(),hintCells.get(i).getY()).getType() == Cell.PATH) {
						mazeGridComp[hintCells.get(i).getX()][hintCells.get(i).getY()].add(hintImage, new Integer(0));
					} else if (f.getMaze().getCell(hintCells.get(i).getX(),hintCells.get(i).getY()).getType() != Cell.PATH) {
						//if more than one component and is not a path tile i.e. is special tile
						//add hint tile in between
						mazeGridComp[hintCells.get(i).getX()][hintCells.get(i).getY()].add(hintImage, new Integer(-1));
					} else {	//else don't do anything
						continue;
					}
					mazeGridComp[hintCells.get(i).getX()][hintCells.get(i).getY()].repaint();	//refresh block
					frame.pack();
				}
			}
		});
		inventory = new ArrayList<JLabel>();	//initialise inventory
		
		//Make maze take up full screen
		Toolkit tk = Toolkit.getDefaultToolkit();  
		int xSize = ((int) tk.getScreenSize().getWidth());  
		int ySize = ((int) tk.getScreenSize().getHeight()); 
		Dimension fullscreen = new Dimension(xSize, ySize);
		frame.setPreferredSize(fullscreen);
		
		//Initialise character spites
		//Maze block is made into a sqaure size
		//Screen height is often shorter than width, thus use 95% of height for pixel width and pixel height of maze GUI
		blockSize = new Dimension((int)((ySize*0.95/this.height)), (int)((ySize*0.95/this.height)));	
		
		//Store sprites in hashmap for easy access
		sprites = new HashMap<String, Sprite>();
		
		//Store x, y of blockSize
		int x = (int)blockSize.getWidth();
		int y = (int)blockSize.getHeight();
		
		//Set PlayerSprite
		studentSprite = f.getStudent().getCharacter();
		
		//Add sprites to hashmap
		//Size of each sprite is determined by x and y,
		//which is the size of each block on the maze grid
		sprites.put(wallSprite, new Sprite(wallSprite,x,y));
		sprites.put(pathSprite, new Sprite(pathSprite,x,y));
		sprites.put(studentSprite, new Sprite(studentSprite,x,y));
		sprites.put(doorSprite, new Sprite(doorSprite,x,y));
		sprites.put(scardSprite, new Sprite(scardSprite,x,y));
		sprites.put(cactusSprite, new Sprite(cactusSprite,x,y));
		sprites.put(coinSprite, new Sprite(coinSprite,x,y));
		sprites.put(knifeSprite, new Sprite(knifeSprite,x,y));
		sprites.put(rootSprite, new Sprite(rootSprite,x,y));
		sprites.put(killableCactusSprite, new Sprite(killableCactusSprite,x,y));
		sprites.put(hintSprite, new Sprite(hintSprite,x,y));
		
		//Set side panel characteristics
		//Size is based on mazes size. Its width is 40% of mazes width.
		//Height is equivalent to mazes height.
		sidePanel.setPreferredSize(new Dimension((int) ((this.width * blockSize.getWidth())*0.4),
													(int) (this.height * blockSize.getHeight())));
		sidePanel.setBackground(new Color(240, 240, 240));
		sidePanel.setLayout(new GridBagLayout());
		sidePanel.setBorder(new LineBorder(Color.BLACK, 2));
		
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);  //Update state 
		frame.setResizable(false); //Fix window size
		frame.setLayout(new GridBagLayout()); //Set frame layout
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Set close operation
		
		//Pack frame
		frame.setUndecorated(true);
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Update only GUI components that will have changed since initialisation or a previous update cal1l.
	 * These components include blocks with the player, enemy and unlocked door.
	 * @param m Maze that GUI will use to create a visual representation of the maze game.
	 */
	public void update(Maze m) {
		Cell curStudentPos = m.getStudentCell();
		
		//if player is not dead or if he just died, update its position
		if (!m.playerDied() || lastStudentPos != null) {
			if (!lastStudentPos.equals(curStudentPos)) {
				updateBlock(m, lastStudentPos);
				if (!m.playerDied()) {
					updateBlock(m, curStudentPos);
				}
				score.setText("Score: " + Integer.toString(f.getScore())); //update score
				// Add things to inventory
				if (m.itemCollected(Student.KNIFE)){
					inventory.get(Student.KNIFE).setVisible(true);
				}
				if (m.itemCollected(Student.SCARD)){
					inventory.get(Student.SCARD).setVisible(true);
				}
				if (m.itemCollected(Student.ROOT_POWER)){
					inventory.get(Student.ROOT_POWER).setVisible(true);
				} else {
					inventory.get(Student.ROOT_POWER).setVisible(false);	//disappears according to maze settings (after 5 seconds)
				}
				
				if (m.checkReachedEnd()) {	//unlock door if the player has reached the end with the key
					updateBlock(m,m.getDestDoor());
				}
			}
			lastStudentPos = curStudentPos;
		}
		
		for (int i = 0; i < m.getNumCactus(); i++) {
			Cell curCactusPos = m.getCactusCell(i);
			if (!m.cactusDied(i)) {	//if enemy is not dead, update its position
				if (!lastCactusPos[i].equals(curCactusPos)) {
					updateBlock(m, lastCactusPos[i]);
					updateBlock(m, curCactusPos);
					updatedOnce = false;
				} else if (m.itemCollected(Student.ROOT_POWER) && !updatedOnce) {
					updateBlock(m, lastCactusPos[i]);
					if (i == m.getNumCactus()-1) {	//if all frozen enemies have been updated once
						updatedOnce = true;			//no need to update again until unfrozen
					}
				}
				lastCactusPos[i] = curCactusPos;
			} else if (lastCactusPos[i] != null) {
				updateBlock(m, lastCactusPos[i]);
				lastCactusPos[i] = null;
			}
		}
	}
	
	/**
	 * Update a particular tile on the maze.
	 * @param m the maze to be updated
	 * @param old the tile to be updated
	 */
	private void updateBlock(Maze m, Cell old) {	
		//update top layers when updating block
		//leave bottom background layer
		int numComponents = this.mazeGridComp[old.getX()][old.getY()].getComponentCount();
		for (int i = 0; i < numComponents-1; i++) {
			this.mazeGridComp[old.getX()][old.getY()].remove(0);
		}
		String overLaySprite = "";	//Overlay sprite that goes on top of block sprite
		int index = 0; 	//where the overlay layer is to be placed on the JLayeredPane
		//Determine block graphics based on type of tile
		//If player is at this tile
		if (m.getStudentCell() != null && m.getStudentCell().equals(old)) {
			overLaySprite = f.getStudent().getCharacter();
		} 
		//Check if enemy unit
		else if (m.isCactusCell(old)) {
			if (m.itemCollected(Student.ROOT_POWER)) {
				overLaySprite = killableCactusSprite;
			} else {
				overLaySprite = cactusSprite;
			}
		}
		//if the tile is door and is a path now (key collected)
		else if (m.getDestDoor().equals(old) && m.getDestDoor().getType() == Cell.PATH) {
			this.mazeGridComp[old.getX()][old.getY()].remove(0);
			overLaySprite = pathSprite;
			index = -2;
		}
		//Check to see what tile type this is
		else if (old.getType() == Cell.BTM336) {
			overLaySprite = doorSprite;
		}
		//Check for key
		else if (old.getType() == Cell.SCARD) {
			overLaySprite = scardSprite;
		}
		else if (old.getType() == Cell.SITEM) {
			overLaySprite = coinSprite;
		}
		else if (old.getType() == Cell.KNIFE){
			overLaySprite = knifeSprite;
		}
		else if (old.getType() == Cell.ROOT_POWER) {
			overLaySprite = rootSprite;
		}
		if (overLaySprite != "") {	//if an overlay sprite has been determined
			JLabel overlayImage = new JLabel(sprites.get(overLaySprite).getSprite());
			this.mazeGridComp[old.getX()][old.getY()].add(overlayImage, new Integer(index));	
			frame.pack();
		}
	}
	
	/**
	 * Initialise GUI components in frame based on the initial state of maze.
	 * 
	 * @param m Maze that GUI will use to create a visual representation of the maze game.
	 */
	public void init(Maze m) 
	{
		//Clear panels
		mazeGrid.removeAll();
		sidePanel.removeAll();
		
		//Make new GridLayout for maze itself
		mazeGrid.setLayout(new GridLayout(width, height));
		
		//Constraints to GridBayLayout
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		
		//Make new enemy
		lastCactusPos = new Cell[m.getNumCactus()];
		for (int i = 0; i < m.getNumCactus(); i++) {
			lastCactusPos[i] = new Cell();
		}
		
		//Fill all blocks
		for (int y = 0; y < height; y++)
		{
			gbc.gridy = y;	//update grid y pos
			for (int x = 0; x < width; x++)
			{
				gbc.gridx = x; //update grid x pos
				
				//Get information about current tile
				Cell c = m.getCell(x, y);
				
				//Use JLayeredPane for each block
				JLayeredPane block = new JLayeredPane();
				this.mazeGridComp[x][y] = block;	//set mazegrid to represent this block
				
				//Set size and layout
				block.setPreferredSize(blockSize);
				block.setLayout(new OverlayLayout(block));
				
				String blockSprite = pathSprite;	//base (bottom) block sprite
				String overLaySprite = "";	//Overlay sprite that goes on top of block sprite
				
				//Determine block graphics based on type of tile
				//If player is at this tile
				if (m.getStudentCell() != null && m.getStudentCell().equals(c)) {
					overLaySprite = studentSprite;
					this.lastStudentPos = c; //update last position
				}
				//Check if enemy unit
				else if (m.isCactusCell(c)) {
					for (int i = 0; i < m.getNumCactus(); i++) {	//find enemy ID
						if (m.getCactusCell(i) != null && m.getCactusCell(i).equals(c)) {
							overLaySprite = cactusSprite;
							this.lastCactusPos[i] = c;
							break;
						}
					}
				}
				//Check if this is a door
				else if (c.getType() == Cell.BTM336) {
					blockSprite = wallSprite;
					overLaySprite = doorSprite;
				}
				//Check for key
				else if (c.getType() == Cell.SCARD) {
					overLaySprite = scardSprite;
				}
				else if (c.getType() == Cell.SITEM) {
					overLaySprite = coinSprite;
				}
				else if (c.getType() == Cell.KNIFE){
					overLaySprite = knifeSprite;
				}
				else if (c.getType() == Cell.ROOT_POWER) {
					overLaySprite = rootSprite;
				}
				//Else if wall
				else if (c.getType() == Cell.FOREST){
					blockSprite = wallSprite;
				}
				//else, path so default block sprite is used
			
				//Always add block sprite
				JLabel spriteImage = new JLabel(sprites.get(blockSprite).getSprite());
				block.add(spriteImage, new Integer(-2));
				
				//Add overlay sprite if required
				if (overLaySprite != "") {
					JLabel overlayImage = new JLabel(sprites.get(overLaySprite).getSprite());
					block.add(overlayImage, new Integer(0));
				}
				
				//Add block as hole to maze
				mazeGrid.add(block, gbc);
			}
		}
		
		//Add maze to this frame
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.CENTER;
		frame.add(mazeGrid, gbc);
		
		//Create side panel components using gridbag layout 
		//Add panel with player information
		JPanel playerPanel = new JPanel(new GridLayout(3,1));
		playerPanel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
		
		//Add game icon to side panel
		ImageIcon title = new ImageIcon(this.getClass().getResource("/sprites/mazerunner.png"));
		Image image = title.getImage().getScaledInstance(288, 144, Image.SCALE_FAST);
		title.setImage(image);	
		JLabel titleImage = new JLabel(title);
		playerPanel.add(titleImage);
		
		//Add current player image (set size 96 x 96)
		Sprite student = new Sprite(f.getStudent().getCharacter(),96,96);	
		JLabel studentImage = new JLabel(student.getSprite());
		studentImage.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
		playerPanel.add(studentImage);
		
		//Add Score label for player
		JPanel playerStats = new JPanel(new GridLayout(4,1));
		JLabel name = new JLabel("Name: " + f.getStudent().getName());
		JLabel character = new JLabel("Character: " + f.getStudent().getCharacter().substring(0, 1).toUpperCase() + f.getStudent().getCharacter().substring(1));
		score = new JLabel("Score: " + f.getScore());
		level = new JLabel("Level: " + (f.getLevel()+1)); //levels count from 0, so +1 offset so that levels count from 1
		
		//Setup font of different labels
		name.setFont(new Font("Arial", Font.PLAIN, 16));
		character.setFont(new Font("Arial", Font.PLAIN, 16));
		score.setFont(new Font("Arial", Font.BOLD, 18));
		level.setFont(new Font("Arial", Font.BOLD, 18));
		
		//Add components to player stats panel
		playerStats.add(name);
		playerStats.add(character);
		playerStats.add(score);
		playerStats.add(level);	
		playerPanel.add(playerStats);	//add player stats to panel with player information
		
		gbc.gridwidth = 4;
		gbc.gridx = 0;
		gbc.gridy = 0;
		sidePanel.add(playerPanel);	//add player information panel to side panel
		
		gbc.gridx = 0;
		gbc.gridy = -3;
		
		//Add exit button and hint button at bottom of side panel
		hintButton.setMargin(new Insets(5, 10, 5, 10));
		exitButton.setToolTipText("Click here to exit to main menu.");
		sidePanel.add(hintButton, gbc);
		exitButton.setMargin(new Insets(5, 10, 5, 10));
		sidePanel.add(exitButton, gbc);
		
		//Add key and sword (set size 48 x 48)
		inventory.add(Student.SCARD, new JLabel(new Sprite(scardSprite,48,48).getSprite()));
		inventory.add(Student.KNIFE, new JLabel(new Sprite(knifeSprite,48,48).getSprite()));
		inventory.add(Student.ROOT_POWER, new JLabel(new Sprite(rootSprite,48,48).getSprite()));
		
		 JPanel invPanel = new JPanel();
         invPanel.setPreferredSize(new Dimension(200,60));
        
         gbc.gridwidth = 1;
         gbc.gridy = 0;
         gbc.gridx = 0;
         invPanel.add(inventory.get(Student.KNIFE),gbc);
        
         gbc.gridy = 0;
         gbc.gridx = 1;
         invPanel.add(inventory.get(Student.SCARD),gbc);
        
         gbc.gridy = 0;
         gbc.gridx = 2;
         invPanel.add(inventory.get(Student.ROOT_POWER),gbc);
        
         for (int i = 0; i < Student.NUM_INVENTORY_ITEMS; i++) {
        	 inventory.get(i).setVisible(false);
         }
         
         gbc.gridy = 2;
         gbc.gridx = 0;
         sidePanel.add(invPanel,gbc);
         
         //Add sidePanel to this frame
         gbc.gridx = 1;
         gbc.gridy = 0;
         frame.add(sidePanel, gbc);
        
         //Pack frame
         frame.pack();
	}
	
	/**
	 * Gets the frame component of the maze frame.
	 * This is where all the components are added onto.
	 * @return the frame component of the maze frame
	 */
	public JFrame getFrame() {
		return frame;
	}	
}