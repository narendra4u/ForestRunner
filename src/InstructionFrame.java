import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

// The instruction frame containes the how to play information for our gamer
public class InstructionFrame {
	private JButton backButton;
	private JPanel instructions;
	private JFrame frame;
	
	public InstructionFrame () {
		frame = new JFrame();	//setup frame
		frame.setTitle("How to Play");		//add title
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		backButton = new JButton("Close");	//add close button
		instructions = new JPanel(new GridBagLayout());

		backButton.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);	//set frame to not visible when close button is pressed
			}
		});
		frame.getContentPane().setBackground(Color.WHITE);
		instructions.setBackground(Color.WHITE);
		frame.setResizable(false);
		frame.setLayout(new GridBagLayout());
	
		//Set up images in the instruction frame
				BufferedImage wasd = null;
			   try {
			    	wasd = ImageIO.read(this.getClass().getResource("/sprites/arrow.png"));
			    } catch (IOException e) {
			    	System.err.println("Failed to read sprite.");
			    }
				
			    BufferedImage htp = null;
			    try {
			    	htp = ImageIO.read(this.getClass().getResource("/sprites/howtoplay2.png"));
			    } catch (IOException e){
			    	System.err.println("Failed to read sprite.");
			    }
			    
			    //set up JLabels for the images displayed on the instruction screen
				JLabel knife = new JLabel(new Sprite(MazeFrame.knifeSprite,48,48).getSprite());
				JLabel scard = new JLabel(new Sprite(MazeFrame.scardSprite,48,48).getSprite());
				JLabel coin = new JLabel(new Sprite(MazeFrame.coinSprite,48,48).getSprite());
				JLabel cactus = new JLabel(new Sprite(MazeFrame.cactusSprite,48,48).getSprite());
				JLabel root = new JLabel(new Sprite(MazeFrame.rooteSprite, 48, 48).getSprite());
				JLabel cactusF = new JLabel(new Sprite(MazeFrame.killableCactusSprite, 48, 48).getSprite());

				JLabel keyboard = new JLabel(new ImageIcon(wasd));
				JLabel howtoplay = new JLabel(new ImageIcon(htp));
				
				//text for the tutorial
				knife.setText("Pick this up to cut the cactus!");
				scard.setText("Pick this up from Murielle to open the door of the Btm 336");
				coin.setText("Cash money, Cash money");
				cactus.setText("Game over if you get too close");
				cactusF.setText("Chomp them when they're coming after you");
				keyboard.setText("Use the arrow keys to move around");
				root.setText("Pick this up to root your cactus");
				
				//position the components (text and image0
				GridBagConstraints c= new GridBagConstraints();
				c.fill = GridBagConstraints.VERTICAL;
				
				c.gridy = 0;
				instructions.add(howtoplay, c);
				
				c.anchor = GridBagConstraints.WEST;
				c.insets = new Insets(0,170,0,0);
				
				c.gridy = 2;
				instructions.add(cactus,c);
				
				c.gridy = 3;
				instructions.add(cactusF,c);
				
				c.gridx = 0;
				c.gridy = 4;
				instructions.add(knife,c);
				
				c.gridy= 5;
				instructions.add(root, c);
				
				c.gridy = 6;
				instructions.add(scard,c);
				
				c.gridy= 7;
				instructions.add(coin,c);
				
				c.insets = new Insets(0,120,0,0);
				c.gridy = 8;
				instructions.add(keyboard, c);
				
				c.gridy= 9;
				c.anchor = GridBagConstraints.CENTER;
				c.insets = new Insets(0,0,0,0);
				this.backButton.setEnabled(true);
				instructions.add(backButton,c);
				
				c.gridx= 0;
				frame.add(instructions);

				c.gridy = 1;
				frame.pack();
				frame.setVisible(false);
			}
	
	//check whether the frame appears or not
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}
}
