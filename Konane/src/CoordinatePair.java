import java.util.ArrayList;

public class CoordinatePair {

	int id; // Player ID
	int y;
	int x;
	int jumps; // Number of jumps it can make
	String jumpPath; // The 'optimal' jump path
	ArrayList<String> allPaths; // List of jumps the token can make
	
	/** Constructor for the Piece Object that is used for storing information about a piece 
	 * @param playerID - Represents the player of which the piece is owned by
	 * @param yVal - The Y Coordinate
	 * @param xVal - The X Coordinate
	 * @param jumpTotal - Number of jumps in the path
	 * @param jumpOrder - Order of jumps to be taken
	 * */
	public CoordinatePair (int playerID, int yVal, int xVal, int jumpTotal, String jumpOrder) {
		allPaths = new ArrayList<String>(); // Contains all the possible jumps the piece can make
		id = playerID; // whichever player the piece belongs to
		y = yVal;
		x = xVal;
		jumps = jumpTotal;
		jumpPath = jumpOrder;
	}
	
	/** Sets the list of jumps the token can make */
	public void setPaths (ArrayList<String> setOfMoves) {
		for (int i = 0; i < setOfMoves.size(); i++) {
			allPaths.add(setOfMoves.get(i));
		}
	}
}
