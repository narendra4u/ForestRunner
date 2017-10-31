import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class SettingsPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JButton setGavin;
	// private JButton setWiiliam; 
	// private JButton setKainen;
	private JTextField nameBox;
	
	private JRadioButton easyDifficulty;	//buttons for choosing difficulty
	private JRadioButton mediumDifficulty;
	private JRadioButton hardDifficulty;
	
	public SettingsPanel(Forest f) {
		// TODO Auto-generated constructor stub
		final Forest forest = f;
		this.setLayout(new GridBagLayout());
		GridBagConstraints bag = new GridBagConstraints();
		
		Sprite gavinPanel = new Sprite(MazeFrame.gavinSprite, 48, 48);
		//Sprite williamPanel = new Sprite(MazeFrame.WilliamSprite, 48, 48);
		//Sprite kainenPanel = new Sprite(MazeFrame.KainenSprite, 48, 48);
		
		setGavin = new JButton("Gavin - The Destroyer", gavinPanel.getSprite() );
		//setWilliam = new JButton("William - The Knight", williamPanel.getSprite());
		//setKainen = new JButton("Kainen - The Dark Prince", kainenPanel.getSprite());
		
		//set up selecting character when user clicks the corresponding button
		setGavin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				forest.getStudent().setCharacter(MazeFrame.WilliamSprite);
			}
		});
		/* setWilliam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				game.getPlayer().setCharacter(MazeFrame.WilliamSprite);
			}
		});
		setKainen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				game.getPlayer().setCharacter(MazeFrame.KainenSprite);
			}
		});;  */
		this.add(setGavin);	
		//this.add(setWilliam);
		//this.add(setKainen);
		
		nameBox = new JTextField("Enter Name");
		nameBox.setPreferredSize(new Dimension(120, 20));
		
		// Move to next line
		
		bag.gridy = 1;
		bag.gridx = 0;
		bag.insets = new Insets(20, 20, 0, 0);
		
		JLabel nameText = new JLabel("Let's know you better: ");
		this.add(nameText, bag);
		
		bag.gridx = 1;
		this.add(nameBox, bag);
		
		// Move to next line
		bag.gridy = 2;
		bag.gridx = 0;
		JLabel difficultyText = new JLabel("Choose difficulty: ");
		this.add(difficultyText, bag);
		
		//Add setting difficulty option
		
		easyDifficulty = new JRadioButton("Easy", true);
		mediumDifficulty = new JRadioButton("Medium");
		hardDifficulty = new JRadioButton("Hard");
		
		ButtonGroup difficultyButtons = new ButtonGroup();
		difficultyButtons.add(easyDifficulty);
		difficultyButtons.add(mediumDifficulty);
		difficultyButtons.add(hardDifficulty);
		
		// Move to next line
		
		bag.gridy = 3;
		bag.gridx = 0;
		bag.insets = new Insets(10, 10, 0, 0);
		this.add(easyDifficulty, bag);
		
		bag.gridx = 1;
		this.add(mediumDifficulty, bag);
		
		bag.gridx = 2;
		this.add(hardDifficulty, bag);
		
		// ActionListener for choosing difficulty
		easyDifficulty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				forest.setDifficulty(Forest.EASY);
			}
		});
		
		mediumDifficulty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				forest.setDifficulty(Forest.MEDIUM);
			}
		});
		
		hardDifficulty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				forest.setDifficulty(Forest.HARD);
			}
		});
	}
	
	// Retrieve the name of the student
	public String getName() {
		return nameBox.getText();
	}
}