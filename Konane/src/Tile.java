import acm.graphics.GCompound;
import acm.graphics.GImage;

public class Tile extends GCompound {

	GImage img; // Image used to project the board
	int id; // Stores the player ID 
	int width, height;
	
	/** Constructor for the Tile
	 * @param x The width of the image
	 * @param y The height of the image
	 * @param type The player ID
	 * */
	public Tile(int x, int y, int type) {
		id = type;
		if (type == 0) {  // White Piece
			img = new GImage("../Images/Board_Tile_W.png"); 
		}
		if (type == 1) { // Black Piece
			img = new GImage("../Images/Board_Tile_B.png"); 
		}
		// Empty Board Space
		else if (type == 2) img = new GImage("../Images/Board_Tile.png"); 
		
		width = x; height = y;
		img.setSize(width, height); // Set to 40 x 40 pixels
		add(img);
	}
	/**
	 * Method to change the Tile to a different piece or empty 
	 * @param type the player ID of the piece to be shown (2 for empty space)
	 */
	public void setTile(int type) { // Used for changing the 
		id = type;
		if (type == 0) { img.setImage("../Images/Board_Tile_W.png");  }
		else if (type == 1) img.setImage("../Images/Board_Tile_B.png"); 
		else if (type == 2) img.setImage("../Images/Board_Tile.png");
	}
}
