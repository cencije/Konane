import java.awt.Color;

import acm.graphics.GCompound;
import acm.graphics.GImage;
import acm.graphics.GOval;

public class Tile extends GCompound {

	GImage img;
	GOval piece;
	int id;
	int width, height;
	boolean canPass = true;
	
	public Tile(int x, int y, int type) {
		id = type;
		if (type == 0) { 
			img = new GImage("../Images/Board_Tile_W.png"); 
		}
		if (type == 1) {
			img = new GImage("../Images/Board_Tile_B.png"); 
		}
		else if (type == 2) img = new GImage("../Images/Board_Tile.png"); 
		
		width = x; height = y;
		img.setSize(width, height);
		add(img);
	}
	public void setTile(int type) {
		id = type;
		if (type == 0) { img.setImage("../Images/Board_Tile_W.png");  }
		else if (type == 1) img.setImage("../Images/Board_Tile_B.png"); 
		else if (type == 2) img.setImage("../Images/Board_Tile.png");
	}
}