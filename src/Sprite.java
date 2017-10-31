import java.awt.Image;

import javax.swing.ImageIcon;

/* Create a Sprite of the images provided in the resources */

public class Sprite {
	
	private ImageIcon sprite;
	
	/* Check if the image is present in the resource folder and set it to a sprite */
	public Sprite(String imgName, int x, int y) {
		try {
			sprite = new ImageIcon(this.getClass().getResource("/res/" + imgName + ".png"));
			Image image = sprite.getImage().getScaledInstance(x, y, Image.SCALE_FAST);
			sprite.setImage(image); // Sprite is resized to the image width and height
		}
		catch (Exception e) {} // If no image is found, create a blank image sprite
	}

	// Gets the Sprite image icon i.e return stored image of resized sprite
	public ImageIcon getSprite() {
		return this.sprite;
	}		
}
