import java.util.ArrayList;
import java.util.Collections;
/**
 * Class containing all of the algorithms for board events
 */
public class Algorithm {

	Tile[][] board = new Tile[8][8];
	Tile[][] tempBoard = new Tile[8][8];
	Tile[][] mmBoard = new Tile[8][8];
	int playerID;
	MainGame mainGame;
	String strSeparator = "\n-----------------------------------------------\n";
	String strMove = "";
	String longestPath = "";
	String bestPath = "";
	CoordinatePair bestMaxToken;
	CoordinatePair bestMinToken;
	
	String bestMoveMax = "";
	String bestMoveMin = "";
	int plyCounterMax = 1;
	int plyCounterMin = 1;
	int moveCounter = 3;
	int turnCounter = 2;
	int plyLimit = 1;
	int evaluationCount = 0;
	int cutoffCount = 0;
	int branchesCount = 0;
	int branchListCount = 0;
	
	ArrayList<String> possiblePaths = new ArrayList<String>();
	
	/**
	 * Sets the relationship to the MainGame class
	 * @param mg The MainGame instance
	 */
	public void setMain(MainGame mg) { mainGame = mg; }
	/**
	 * Sets the board before executing an action
	 * @param currentBoard The board from the MainGame class
	 */
	public void setBoard(Tile[][] currentBoard) { board = currentBoard; }
	/**
	 * Creates the new Minimax board to be used
	 * @param possBoard The board to set the Minimax board to
	 */
	public void setmmBoard(Tile[][] possBoard) {
		for (int yC = 0; yC < 8; yC++) {
			for (int xC = 0; xC < 8; xC++) {
				mmBoard[yC][xC] = new Tile(40,40, possBoard[yC][xC].id);
			}
		}
	}
	/**
	 * Prints the board for debugging purposes
	 * @param b The board to be printed
	 */
	public void printBoard(Tile[][] b) {
		System.out.println("----------");
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				System.out.print(b[y][x].id + " ");
			}
			System.out.println();
		}
		System.out.println("----------");
	}	
	/**
	 * Moves the player'sfirst movable piece found in the board (Goes row by row from 1,1 | 2,1 ... | to 8,8)
	 * @param playerMoving The player ID of the player whose turn it currently is.
	 */
	public void moveOne(int playerMoving) {
		playerID = playerMoving;
		int opponent = 0;
		strMove = "Player ";
		if (playerID == 1) strMove += "Black moved Token at ";
		else { strMove += "White moved Token at ";  opponent = 1;}
		boolean valid = false;
		OUTER_LOOP:
		for (int y = 0; y < 8; y++) {
			INNER_LOOP:
			for (int x = 0; x < 8; x++) {
				// If the tile contains the player’s token
				if (board[y][x].id == playerID) {
					// If jumping ‘UP’ is not out of bounds
					if (y - 2 >= 0) {
						// If the space above the player’s token contains an opponent’s piece 
						// and the space 2 spaces above is empty
						if (board[y-1][x].id == opponent && board[y-2][x].id == 2) { 
							valid = true;
							// Update the board to have the token moved up, with the opponent’s jumped token removed.
							board[y][x].id = 2; board[y][x].setTile(2);
							board[y-1][x].id = 2; board[y-1][x].setTile(2);
							board[y-2][x].id = playerID; board[y-2][x].setTile(playerID);
							mainGame.setPostMoveBoard(board);
							// Update the text field to state the player token moved down.
							strMove += "(" + (x+1) + "," + (y+1) + ") to (" + (x+1) + "," + (y-1) + ")";
							mainGame.setMoveText(strMove);
							break INNER_LOOP;
						}
					}
					// If jumping ‘DOWN’ is not out of bounds
					if (y + 2 <= 7 && !valid) {
						// If the space below the player’s token contains an opponent’s piece 
						// and the space 2 spaces below is empty
						if (board[y+1][x].id == opponent && board[y+2][x].id == 2) { 
							valid = true;
							// Update the board to have the token moved down, with the opponent’s jumped token removed.
							board[y][x].id = 2; board[y][x].setTile(2);
							board[y+1][x].id = 2; board[y+1][x].setTile(2);
							board[y+2][x].id = playerID; board[y+2][x].setTile(playerID);
							mainGame.setPostMoveBoard(board);
							// Update the text field to state the player token moved down.
							strMove += "(" + (x+1) + "," + (y+1) + ") to (" + (x+1) + "," + (y+3) + ")";
							mainGame.setMoveText(strMove);
							break INNER_LOOP;
						}
					}
					// If jumping ‘LEFT’ is not out of bounds
					if (x - 2 >= 0 && !valid) {
						// If the space to the left the player’s token contains an opponent’s piece 
						// and the space 2 spaces to the left is empty
						if (board[y][x-1].id == opponent && board[y][x-2].id == 2) { 
							valid = true;
							// Update the board to have the token moved left, with the opponent’s jumped token removed.
							board[y][x].id = 2; board[y][x].setTile(2);
							board[y][x-1].id = 2; board[y][x-1].setTile(2);
							board[y][x-2].id = playerID; board[y][x-2].setTile(playerID);
							mainGame.setPostMoveBoard(board);
							// Update the text field to state the player token moved down.
							strMove += "(" + (x+1) + "," + (y+1) + ") to (" + (x-1) + "," + (y+1) + ")";
							mainGame.setMoveText(strMove);
							break INNER_LOOP;
						}
					}
					// If jumping ‘RIGHT’ is not out of bounds
					if (x + 2 <= 7 && !valid) {
						// If the space to the right the player’s token contains an opponent’s piece 
						// and the space 2 spaces to the right is empty
						if (board[y][x+1].id == opponent && board[y][x+2].id == 2) { 
							valid = true;
							// Update the board to have the token moved right, with the opponent’s jumped token removed.
							board[y][x].id = 2; board[y][x].setTile(2);
							board[y][x+1].id = 2; board[y][x+1].setTile(2);
							board[y][x+2].id = playerID; board[y][x+2].setTile(playerID);
							mainGame.setPostMoveBoard(board);
							// Update the text field to state the player token moved down.
							strMove += "(" + (x+1) + "," + (y+1) + ") to (" + (x+3) + "," + (y+1) + ")";
							mainGame.setMoveText(strMove);
							break INNER_LOOP;
						}
					}
				}
			}
			if (valid) break OUTER_LOOP;
		}
		// If a movable token was not found at the end of searching the entire board, 
		// end the game for the player has lost.
		if (!valid) mainGame.gameOver(playerID);
	}
	/**
	 * Moves the player's token that can make the most jumps in one turn.
	 * @param player The player's ID.
	 * @param opponent The opponent's ID.
	 * @param nextTempBoard The board to be analyzed following the previous iteration.
	 * @param y The Y coordinate of the token.
	 * @param x The X coordinate of the token.
	 * @param currentMaxJA The highest number of jumps currently held for a possible path.
	 * @param jumpAmount The current number of jumps held for the current path traversed.
	 * @param p The Path order of the previous jumps using U,L,D,R.
	 * @return number jumps the token can move for the max
	 */
	public int moveMostJumps(int player, int opponent, Tile[][] nextTempBoard, 
							 int y, int x, int currentMaxJA, int jumpAmount, String p) {
		ArrayList<Integer> jumpVals = new ArrayList<Integer>();
		
		String oldPath = ""; 
		String path = p; //The string of the order of different directions that the token will take ex. “ULDR”
		int up, down, left, right; // Integer values for each possible jump move
		int oldMax = currentMaxJA;
		tempBoard = nextTempBoard;  // Saves the passed instance of the board to a new board	
		
		// If jumping ‘UP’ is not out of bounds
		if (y - 2 >= 0) {
			// If the space above the player’s token contains an opponent’s piece 
			// and the space 2 spaces above is empty
			if (tempBoard[y-1][x].id == opponent && tempBoard[y-2][x].id == 2) { 
				jumpAmount++;
				if (jumpAmount > currentMaxJA) currentMaxJA = jumpAmount;
				tempBoard[y][x].id = 2;
				tempBoard[y-1][x].id = 2;
				tempBoard[y-2][x].id = player;
				up = moveMostJumps(player, opponent, tempBoard, (y-2), x, 
						currentMaxJA, jumpAmount, path += "U");
				jumpVals.add(up); // Add the max value for that subtree of routes to the jump routes
				// Retrieve the old path to not affect the path for further 
				// possible moves from the current position.
				if (path.length() > 0) oldPath = path.substring(0, path.length()-1);
				path = oldPath;
				// Decrement the jump counter back to the original value for this board position 
				jumpAmount--;
				// Reassign the max jump counter to the original
				currentMaxJA = oldMax;
			}
		}
		// If jumping ‘DOWN’ is not out of bounds
		if (y + 2 <= 7) {
			// If the space below the player’s token contains an opponent’s piece 
			// and the space 2 spaces below is empty
			if (tempBoard[y+1][x].id == opponent && tempBoard[y+2][x].id == 2) { 
				jumpAmount++;
				if (jumpAmount > currentMaxJA) currentMaxJA = jumpAmount;
				tempBoard[y][x].id = 2;
				tempBoard[y+1][x].id = 2;
				tempBoard[y+2][x].id = player;
				down = moveMostJumps(player, opponent, tempBoard, (y+2), x, 
						currentMaxJA, jumpAmount, path += "D");
				jumpVals.add(down); // Add the max value for that subtree of routes to the jump routes
				// Retrieve the old path to not affect the path for further 
				// possible moves from the current position.
				if (path.length() > 0) oldPath = path.substring(0, path.length()-1);
				path = oldPath;
				// Decrement the jump counter back to the original value for this board position 
				jumpAmount--;
				// Reassign the max jump counter to the original
				currentMaxJA = oldMax;
			}
		}
		// If jumping ‘LEFT’ is not out of bounds
		if (x - 2 >= 0) {
			// If the space to the left the player’s token contains an opponent’s piece 
			// and the space 2 spaces to the left is empty
			if (tempBoard[y][x-1].id == opponent && tempBoard[y][x-2].id == 2) { 
				jumpAmount++;
				if (jumpAmount > currentMaxJA) currentMaxJA = jumpAmount;
				tempBoard[y][x].id = 2;
				tempBoard[y][x-1].id = 2;
				tempBoard[y][x-2].id = player;
				left = moveMostJumps(player, opponent, tempBoard, y, (x-2),
						currentMaxJA, jumpAmount, path += "L");
				jumpVals.add(left); // Add the max value for that subtree of routes to the jump routes
				// Retrieve the old path to not affect the path for further 
				// possible moves from the current position.
				if (path.length() > 0) oldPath = path.substring(0, path.length()-1);
				path = oldPath;
				// Decrement the jump counter back to the original value for this board position 
				jumpAmount--;
				// Reassign the max jump counter to the original
				currentMaxJA = oldMax;
			}
		}
		// If jumping ‘RIGHT’ is not out of bounds
		if (x + 2 <= 7) {
			// If the space to the right the player’s token contains an opponent’s piece 
			// and the space 2 spaces to the right is empty
			if (tempBoard[y][x+1].id == opponent && tempBoard[y][x+2].id == 2) { 
				jumpAmount++;
				if (jumpAmount > currentMaxJA) currentMaxJA = jumpAmount;
				tempBoard[y][x].id = 2;
				tempBoard[y][x+1].id = 2;
				tempBoard[y][x+2].id = player;
				right = moveMostJumps(player, opponent, tempBoard, y, (x+2), 
						currentMaxJA, jumpAmount, path += "R");
				jumpVals.add(right); // Add the max value for that subtree of routes to the jump routes
				// Retrieve the old path to not affect the path for further 
				// possible moves from the current position.
				if (path.length() > 0) oldPath = path.substring(0, path.length()-1);
				path = oldPath;
				// Decrement the jump counter back to the original value for this board position 
				jumpAmount--;
				// Reassign the max jump counter to the original
				currentMaxJA = oldMax;
			}
		}
		// Update the longest path found
		if (path.length() > longestPath.length()) longestPath = path;
		// If no more jumps found based upon the current path
		// return the number of jumps made to be added to the jumpVals array
		if (jumpVals.size() == 0) return jumpAmount;
		// Return the largest number of jumps the piece can make across all jump paths
		return Collections.max(jumpVals);
	}
	/**
	 * 
	 * @return The longest path for the previously checked token.
	 */
	public String getJumpedPath() { return longestPath; }
	/**
	 * Clears the longest path from the previously checked token.
	 */
	public void clearPath() { longestPath = ""; }
	/**
	 * Moves based upon the path of the current token
	 * @param token The token currently being moved
	 */
	public void moveMax(CoordinatePair token) {
		String moveOrder = token.jumpPath;
		String strMove = "Player ";
		if (token.id == 1) strMove += "Black ";
		else { strMove += "White "; }
		strMove += "moved token at (" + (token.x+1) + "," + (token.y+1) + ")";
		int xVal = token.x; int yVal = token.y;
		if(moveOrder.length() > 0) {
			// Moves based upon the character at the i'th index in the String.
			for(int i = 0; i < moveOrder.length(); i++){
				strMove += " to ";
				if (moveOrder.charAt(i) == 'U') {
					board[yVal][xVal].id = 2; board[yVal][xVal].setTile(2);
					board[yVal-1][xVal].id = 2; board[yVal-1][xVal].setTile(2);
					board[yVal-2][xVal].id = token.id; board[yVal-2][xVal].setTile(token.id);
					mainGame.setPostMoveBoard(board);
					// Decrease the row value for the next iteration if there is one.
					yVal = yVal-2;
				}
				if(moveOrder.charAt(i) == 'D'){
					board[yVal][xVal].id = 2; board[yVal][xVal].setTile(2);
					board[yVal+1][xVal].id = 2; board[yVal+1][xVal].setTile(2);
					board[yVal+2][xVal].id = token.id; board[yVal+2][xVal].setTile(token.id);
					mainGame.setPostMoveBoard(board);
					// Increase the row value for the next iteration if there is one.
					yVal = yVal+2;
				}
				if (moveOrder.charAt(i) == 'L') {
					board[yVal][xVal].id = 2; board[yVal][xVal].setTile(2);
					board[yVal][xVal-1].id = 2; board[yVal][xVal-1].setTile(2);
					board[yVal][xVal-2].id = token.id; board[yVal][xVal-2].setTile(token.id);
					mainGame.setPostMoveBoard(board);
					// Decrease the column value for the next iteration if there is one.
					xVal = xVal-2;
				}
				if(moveOrder.charAt(i) == 'R'){
					board[yVal][xVal].id = 2; board[yVal][xVal].setTile(2);
					board[yVal][xVal+1].id = 2; board[yVal][xVal+1].setTile(2);
					board[yVal][xVal+2].id = token.id; board[yVal][xVal+2].setTile(token.id);
					mainGame.setPostMoveBoard(board);
					// Increase the column value for the next iteration if there is one.
					xVal = xVal+2;
				}
				strMove += "(" + (xVal+1) + "," + (yVal+1) + ")";
			}
			mainGame.setMoveText(strMove);
		}
	}
	/**
	 * Finds and makes the jump(s) of the path based on our heuristic. 
	 * Our heuristic is to reduce the number of movable pieces the opponent has.
	 * @param firstJump To determine if it is the first jump to be made 
	 * @param player The player's ID
	 * @param opponent The opponent's ID
	 * @param nextTempBoard The temporary board created from the previous move
	 * @param y The Y Coordinate of the token.
	 * @param x The X Coordinate of the token.
	 * @param currentMaxJA The highest number of jumps currently held for a possible path.
	 * @param jumpAmount The current number of jumps held for the current path traversed.
	 * @param p The Path order of the previous jumps using U,L,D,R.
	 * @param movableLowestOpponent The number of pieces the opponent can move based upon the current board
	 * @return The lowest number of tokens the opponent can move in a possible board
	 */
	public int moveLeastMoves(boolean firstJump, int player, int opponent, Tile[][] nextTempBoard, 
			 				   int y, int x, int currentMaxJA, int jumpAmount, String p,
			 				   int movableLowestOpponent) {
		ArrayList<Integer> jumpVals = new ArrayList<Integer>();
		String oldPath = ""; 
		String path = p;
		int up, down, left, right;
		int oldMax = currentMaxJA;
		tempBoard = nextTempBoard; // Saves the passed instance of the board to a new board	
		// Set the number movable to a large number greater than the number of pieces 
		// the opponent could ever move 
		// (acts like an infinity value that will always be overwritten if a jump can be made.
		int movableOpponent = 100;
		// If the player’s current piece being analyzed has made at least one jump.
		if (!firstJump) movableOpponent = calculateMovable(player, opponent, nextTempBoard);
		if (!firstJump && movableOpponent == 0) { bestPath = path; return 0; }
		if (movableOpponent < movableLowestOpponent) { 
			movableLowestOpponent = movableOpponent; bestPath = path; 
		}
		// If jumping ‘UP’ is not out of bounds
		if (y - 2 >= 0) {
			// If the space above the player’s token contains an opponent’s piece 
			// and the space 2 spaces above is empty
			if (tempBoard[y-1][x].id == opponent && tempBoard[y-2][x].id == 2) { 
				jumpAmount++;
				if (jumpAmount > currentMaxJA) currentMaxJA = jumpAmount;
				tempBoard[y][x].id = 2;
				tempBoard[y-1][x].id = 2;
				tempBoard[y-2][x].id = player;
				up = moveLeastMoves(false, player, opponent, tempBoard, (y-2), x, 
						currentMaxJA, jumpAmount, path += "U", movableLowestOpponent);
				jumpVals.add(up); // Add the min value for that subtree of routes to the jump routes
				// Retrieve the old path to not affect the path for further 
				// possible moves from the current position.
				if (path.length() > 0) oldPath = path.substring(0, path.length()-1);
				path = oldPath;
				// Decrement the jump counter back to the original value for this board position 
				jumpAmount--;
				// Reassign the max jump counter to the original
				currentMaxJA = oldMax;
			}
		}
		// If jumping ‘DOWN’ is not out of bounds
		if (y + 2 <= 7) {
			// If the space below the player’s token contains an opponent’s piece 
			// and the space 2 spaces below is empty
			if (tempBoard[y+1][x].id == opponent && tempBoard[y+2][x].id == 2) { 
				jumpAmount++;
				if (jumpAmount > currentMaxJA) currentMaxJA = jumpAmount;
				tempBoard[y][x].id = 2;
				tempBoard[y+1][x].id = 2;
				tempBoard[y+2][x].id = player;
				down = moveLeastMoves(false, player, opponent, tempBoard, (y+2), x, 
						currentMaxJA, jumpAmount, path += "D", movableLowestOpponent);
				jumpVals.add(down);// Add the min value for that subtree of routes to the jump routes
				// Retrieve the old path to not affect the path for further 
				// possible moves from the current position.
				if (path.length() > 0) oldPath = path.substring(0, path.length()-1);
				path = oldPath;
				// Decrement the jump counter back to the original value for this board position 
				jumpAmount--;
				// Reassign the max jump counter to the original
				currentMaxJA = oldMax;
			}
		}
		// If jumping ‘LEFT’ is not out of bounds
		if (x - 2 >= 0) {
			// If the space to the left the player’s token contains an opponent’s piece 
			// and the space 2 spaces to the left is empty
			if (tempBoard[y][x-1].id == opponent && tempBoard[y][x-2].id == 2) { 
				jumpAmount++;
				if (jumpAmount > currentMaxJA) currentMaxJA = jumpAmount;
				tempBoard[y][x].id = 2;
				tempBoard[y][x-1].id = 2;
				tempBoard[y][x-2].id = player;
				left = moveLeastMoves(false, player, opponent, tempBoard, y, (x-2), 
						currentMaxJA, jumpAmount, path += "L", movableLowestOpponent);
				jumpVals.add(left); // Add the min value for that subtree of routes to the jump routes
				// Retrieve the old path to not affect the path for further 
				// possible moves from the current position.
				if (path.length() > 0) oldPath = path.substring(0, path.length()-1);
				path = oldPath;
				// Decrement the jump counter back to the original value for this board position 
				jumpAmount--;
				// Reassign the max jump counter to the original
				currentMaxJA = oldMax;
			}
		}
		// If jumping ‘RIGHT’ is not out of bounds
		if (x + 2 <= 7) {
			// If the space to the right the player’s token contains an opponent’s piece 
			// and the space 2 spaces to the right is empty
			if (tempBoard[y][x+1].id == opponent && tempBoard[y][x+2].id == 2) { 
				jumpAmount++;
				if (jumpAmount > currentMaxJA) currentMaxJA = jumpAmount;
				tempBoard[y][x].id = 2;
				tempBoard[y][x+1].id = 2;
				tempBoard[y][x+2].id = player;
				right = moveLeastMoves(false, player, opponent, tempBoard, y, (x+2), 
						currentMaxJA, jumpAmount, path += "R", movableLowestOpponent);
				jumpVals.add(right); // Add the min value for that subtree of routes to the jump routes
				// Retrieve the old path to not affect the path for further 
				// possible moves from the current position.
				if (path.length() > 0) oldPath = path.substring(0, path.length()-1);
				path = oldPath;
				// Decrement the jump counter back to the original value for this board position 
				jumpAmount--;
				// Reassign the max jump counter to the original
				currentMaxJA = oldMax;
			}
		}
		// If no more jumps can be made on this path, return the lowest number 
		// of pieces the opponent can move to be added to the jumpVals array.
		if (jumpVals.size() == 0) return movableLowestOpponent;
		// Return the lowest number of pieces the opponent can move out of 
		// all possible jump paths the player’s piece currently being checked can make.
		return Collections.min(jumpVals);
	}
	/**
	 * Provides the String of the best path for the token determined by the heuristic.
	 * @return The String of the best path for the token determined by the heuristic.
	 */
	public String optimalPath() { return bestPath; }
	/**
	 * Clears the optimal path string from the previously checked token.
	 */
	public void clearOptimal() { bestPath = ""; }
	/**
	 * Minimax start method for the algorithm
	 * @param state Current Board as is
	 * @param player Current moving player's ID
	 * @return The number for the movable pieces for the opponent based on the best move
	 */
	public int miniMax(Tile[][] state, int player) {
		// Clears the best moves for either player from the previous minimax move used.
		clearBestMoveMin();
		clearBestMoveMax();
		if (player == 0) { return minValue(state, 1); }
		else { return maxValue(state, 1); }
	}
	/**
	 * Performs the Max portion of the Minimax algorithm
	 * @param state The board 
	 * @param turnCounter The turn number 
	 * @return the best value for Max
	 */
	public int maxValue(Tile[][] state, int turnCounter) {
		// Receives the game board and the turn number to ensure the 
		// ply number is checked for a particular depth.
		// Stores the counter for the turn to ensure the depth limit is not exceeded
		int localTurnMax = turnCounter; 
		ArrayList<CoordinatePair> mL = new ArrayList<CoordinatePair>();
		// Create a new instance of the board to ensure references are not being used
		Tile[][] tilesClone = new Tile[8][8];
		for (int yC = 0; yC < 8; yC++) {
			for (int xC = 0; xC < 8; xC++) tilesClone[yC][xC] = new Tile(40,40, state[yC][xC].id);
		}
		// Used for ensuring only the first set of moves is performed.
		boolean firstMax = false;
		
		// Made true if this is the first turn.
		if (localTurnMax == 1) firstMax = true;
		setmmBoard(tilesClone); // Updates the minimax board to contain the current board layout
		// Creates the list of movable pieces for the player based on the current board.
		mL = makeAllMovableList(tilesClone, 1, 0);
		// For the statistic regarding the number of times our evaluation function was called.
		evaluationCount++; 
		if (mL.size() == 0) return -10000;
		// If the turn is larger than the number of turns to be performed, 
		// return the value of the number of movable pieces for this board for this player.
		if (localTurnMax > plyLimit) return mL.size();
		int v = -10000;
		for (int i = 0; i < mL.size(); i++) {
			// Statistics: Increase the number of times branches were made.
			branchListCount++;
			// Statistics: Increase the counter for the number of branches.
			branchesCount += mL.get(i).allPaths.size();
			for (int j = 0; j < mL.get(i).allPaths.size(); j++) {
				// Create a board for the board following the possible move the piece can make.
				setmmBoard(tilesClone);
				mmBoard = makeTempBoardPath(mL.get(i), mL.get(i).allPaths.get(j));
				localTurnMax++; // Increase the turn counter
				// Call on the minValue with the updated board based on the currently processed move.
				int vMinVal = minValue(mmBoard, localTurnMax);
				// Like using Math.max(v, minValue), except it includes additional updates 
				// to be made to ensure minimax works for our board updates.
				if (vMinVal > v) {
					// Will only update the move and path to take if it is 
					// based on the current board, not subsequent boards produced 
					// from the actions of either player.
					if (firstMax) { 
						bestMoveMax = mL.get(i).allPaths.get(j);
						mL.get(i).jumpPath = bestMoveMax;
						bestMaxToken = mL.get(i);
					}
					v = vMinVal;
				}
				// Decrease the counter to ensure the depth is kept the 
				// same for each action at this depth
				localTurnMax--;		
			}
		}
		// If no tokens result in a winning or better state 
		// (outcome of the game is determined) just move the first available piece.
		System.out.println("V Max = " + v);
		if (firstMax && bestMaxToken == null && mL.size() > 0) bestMaxToken = mL.get(0);
		return v;
	}
	/**
	 * The Min portion of the Minimax algorithm
	 * @param state The board 
	 * @param turnCounter The turn number 
	 * @return the best value for Min
	 */
	public int minValue(Tile[][] state, int turnCounter) {
		// Receives the game board and the turn number to ensure the 
		// ply number is checked for a particular depth.
		// Stores the counter for the turn to ensure the depth limit is not exceeded
		int localTurnMin = turnCounter;
		ArrayList<CoordinatePair> mL = new ArrayList<CoordinatePair>();
		// Create a new instance of the board to ensure references are not being used
		Tile[][] tilesClone = new Tile[8][8];
		for (int yC = 0; yC < 8; yC++) {
			for (int xC = 0; xC < 8; xC++) tilesClone[yC][xC] = new Tile(40,40, state[yC][xC].id);
		}
		// Used for ensuring only the first set of moves is performed.
		boolean firstMin = false;
		// Made true if this is the first turn.
		if (localTurnMin == 1) firstMin = true;
		setmmBoard(tilesClone); // Updates the minimax board to contain the current board layout
		// Creates the list of movable pieces for the player based on the current board.
		mL = makeAllMovableList(tilesClone, 0, 1);
		// For the statistic regarding the number of times our evaluation function was called.
		evaluationCount++;
		if (mL.size() == 0) return 10000;
		// If the turn is larger than the number of turns to be performed, 
		// return the value of the number of movable pieces for this board for this player.
		if (localTurnMin > plyLimit) return mL.size();
		int v = 10000;
		for (int i = 0; i < mL.size(); i++) {
			// Statistics: Increase the number of times branches were made.
			branchListCount++;
			// Statistics: Increase the counter for the number of branches.
			branchesCount += mL.get(i).allPaths.size();
			for (int j = 0; j < mL.get(i).allPaths.size(); j++) {
				// Create a board for the board following the possible move the piece can make.
				setmmBoard(tilesClone);
				mmBoard = makeTempBoardPath(mL.get(i), mL.get(i).allPaths.get(j));				
				localTurnMin++; // Increase the turn counter
				// Call on the minValue with the updated board based on the currently processed move.
				int vMaxVal = maxValue(mmBoard, localTurnMin);
				// Like using Math.max(v, minValue), except it includes additional updates 
				// to be made to ensure minimax works for our board updates.
				if (vMaxVal < v) { 
					// Will only update the move and path to take if it is 
					// based on the current board, not subsequent boards produced 
					// from the actions of either player.
					if (firstMin) {
						bestMoveMin = mL.get(i).allPaths.get(j);
						mL.get(i).jumpPath = bestMoveMin;
						bestMinToken = mL.get(i);
					}
					v = vMaxVal;
				}
				// Decrease the counter to ensure the depth is kept the 
				// same for each action at this depth
				localTurnMin--;
			}
		}
		// If no tokens result in a winning or better state 
		// (outcome of the game is determined) just move the first available piece.
		System.out.println("V Min = " + v);
		if (firstMin && bestMinToken == null && mL.size() > 0) bestMinToken = mL.get(0);
		return v;
	}
	/**
	 * Minimax with Alpha-Beta Pruning start method for the algorithm
	 * @param state Current Board as is
	 * @param player Current moving player's ID
	 * @return The number for the movable pieces for the opponent based on the best move
	 */
	public int miniMaxABP(Tile[][] state, int player) {
		// Clears the best moves for either player from the previous minimax ABP move used.
		clearBestMoveMin();
		clearBestMoveMax();
		if (player == 0) return minValueABP(state, 1, -10000, 10000);
		else { return maxValueABP(state, 1, -10000, 10000); }
	}
	/**
	 * Performs the Max portion of the Minimax with ABP algorithm
	 * @param state The board 
	 * @param turnCounter The turn number 
	 * @param alpha Alpha value
	 * @param beta Beta Value
	 * @return max value for the Max portion of the Minimax with ABP algorithm
	 */
	public int maxValueABP(Tile[][] state, int turnCounter, int alpha, int beta) {
		// Receives the game board and the turn number to ensure the 
		// ply number is checked for a particular depth.
		// Stores the counter for the turn to ensure the depth limit is not exceeded
		int localTurnMax = turnCounter;
		ArrayList<CoordinatePair> mL = new ArrayList<CoordinatePair>();
		// Create a new instance of the board to ensure references are not being used
		Tile[][] tilesClone = new Tile[8][8];
		for (int yC = 0; yC < 8; yC++) {
			for (int xC = 0; xC < 8; xC++) tilesClone[yC][xC] = new Tile(40,40, state[yC][xC].id);
		}
		// Used for ensuring only the first set of moves is performed.
		boolean firstMax = false;
		// Made true if this is the first turn.
		if (localTurnMax == 1) firstMax = true;
		setmmBoard(tilesClone); // Updates the minimax board to contain the current board layout
		// Creates the list of movable pieces for the player based on the current board.
		mL = makeAllMovableList(tilesClone, 1, 0);
		// For the statistic regarding the number of times our evaluation function was called.
		evaluationCount++;
		if (mL.size() == 0) return -10000;
		// If the turn is larger than the number of turns to be performed, 
		// return the value of the number of movable pieces for this board for this player.
		if (localTurnMax > plyLimit) return mL.size();
		int v = -10000;
		for (int i = 0; i < mL.size(); i++) {
			// Statistics: Increase the number of times branches were made.
			branchListCount++;
			// Statistics: Increase the counter for the number of branches.
			branchesCount += mL.get(i).allPaths.size();
			for (int j = 0; j < mL.get(i).allPaths.size(); j++) {
				setmmBoard(tilesClone);
				mmBoard = makeTempBoardPath(mL.get(i), mL.get(i).allPaths.get(j));
				localTurnMax++; // Increase the turn counter
				// Call on the minValue with the updated board based on the currently processed move.
				int vMinVal = minValueABP(mmBoard, localTurnMax, alpha, beta);
				if (vMinVal > v) {
					// Will only update the move and path to take if it is 
					// based on the current board, not subsequent boards produced 
					// from the actions of either player.
					if (firstMax) { 
						bestMoveMax = mL.get(i).allPaths.get(j);
						mL.get(i).jumpPath = bestMoveMax;
						bestMaxToken = mL.get(i);
					}
					v = vMinVal;
				}
				// Prune
				if (v >= beta) { cutoffCount++; return v; }
				alpha = Math.max(alpha, v);
				// Decrease the counter to ensure the depth is kept the 
				// same for each action at this depth
				localTurnMax--;
			}
		}
		// If no tokens result in a winning or better state 
		// (outcome of the game is determined) just move the first available piece.
		System.out.println("V Max = " + v);
		if (firstMax && bestMaxToken == null && mL.size() > 0) bestMaxToken = mL.get(0);
		return v;
	}
	/**
	 * Performs the Max portion of the Minimax with ABP algorithm
	 * @param state The board 
	 * @param turnCounter The turn number 
	 * @param alpha Alpha value
	 * @param beta Beta Value
	 * @return min value for the Min portion of the Minimax with ABP algorithm
	 */
	public int minValueABP(Tile[][] state, int turnCounter, int alpha, int beta) {
		// Receives the game board and the turn number to ensure the 
		// ply number is checked for a particular depth.
		// Stores the counter for the turn to ensure the depth limit is not exceeded
		int localTurnMin = turnCounter;
		ArrayList<CoordinatePair> mL = new ArrayList<CoordinatePair>();
		// Create a new instance of the board to ensure references are not being used
		Tile[][] tilesClone = new Tile[8][8];
		for (int yC = 0; yC < 8; yC++) {
			for (int xC = 0; xC < 8; xC++) tilesClone[yC][xC] = new Tile(40,40, state[yC][xC].id);
		}
		// Used for ensuring only the first set of moves is performed.
		boolean firstMin = false;
		// Made true if this is the first turn.
		if (localTurnMin == 1) firstMin = true;
		setmmBoard(tilesClone); // Updates the minimax board to contain the current board layout
		// Creates the list of movable pieces for the player based on the current board.
		mL = makeAllMovableList(tilesClone, 0, 1);
		// For the statistic regarding the number of times our evaluation function was called.
		evaluationCount++;
		if (mL.size() == 0) return 10000;
		// If the turn is larger than the number of turns to be performed, 
		// return the value of the number of movable pieces for this board for this player.
		if (localTurnMin > plyLimit) return mL.size();
		int v = 10000;
		for (int i = 0; i < mL.size(); i++) {
			// Statistics: Increase the number of times branches were made.
			branchListCount++;
			// Statistics: Increase the counter for the number of branches.
			branchesCount += mL.get(i).allPaths.size();
			for (int j = 0; j < mL.get(i).allPaths.size(); j++) {
				setmmBoard(tilesClone);
				mmBoard = makeTempBoardPath(mL.get(i), mL.get(i).allPaths.get(j));
				localTurnMin++; // Increase the turn counter
				// Call on the minValue with the updated board based on the currently processed move.
				int vMaxVal = maxValueABP(mmBoard, localTurnMin, alpha, beta);
				// Like using Math.max(v, minValue), except it includes additional updates 
				// to be made to ensure minimax works for our board updates.
				if (vMaxVal < v) { 
					// Will only update the move and path to take if it is 
					// based on the current board, not subsequent boards produced 
					// from the actions of either player.
					if (firstMin) {
						bestMoveMin = mL.get(i).allPaths.get(j);
						mL.get(i).jumpPath = bestMoveMin;
						bestMinToken = mL.get(i);
					}
					v = vMaxVal;
				}
				// Prune
				if (v <= alpha) { cutoffCount++; return v; }
				beta = Math.min(beta, v);
				// Decrease the counter to ensure the depth is kept the 
				// same for each action at this depth
				localTurnMin--;
			}
		}
		// If no tokens result in a winning or better state 
		// (outcome of the game is determined) just move the first available piece.
		System.out.println("V Min = " + v);
		if (firstMin && bestMinToken == null && mL.size() > 0) bestMinToken = mL.get(0);
		return v;
		
	}
	/** 
	 * Calculates the number of movable pieces the opponent has based upon the given board.
	 * @param player The player's ID.
	 * @param opponent The opponent's ID.
	 * @param tempBoard The board passed to be evaluated on.
	 * @return The number of movable pieces the opponent has.
	 */
	public int calculateMovable(int player, int opponent, Tile[][] tempBoard) {
		int movablePieces = 0;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				if (tempBoard[y][x].id == opponent) {
					boolean valid = false;
					// Valid boolean to avoid recounting
					if (y - 2 >= 0) {
						if (tempBoard[y-1][x].id == player && tempBoard[y-2][x].id == 2) valid = true;
					}
					if (y + 2 <= 7 && !valid) {
						if (tempBoard[y+1][x].id == player && tempBoard[y+2][x].id == 2) valid = true;
					}
					if (x - 2 >= 0 && !valid) {
						if (tempBoard[y][x-1].id == player && tempBoard[y][x-2].id == 2) valid = true;
					}
					if (x + 2 <= 7 && !valid) {
						if (tempBoard[y][x+1].id == player && tempBoard[y][x+2].id == 2) valid = true;
					}
					if (valid) movablePieces++;
				}
			}
		}
		return movablePieces;
	}
	/**
	 * Goes and sets the list of all possible jumps for all movable tokens for the player
	 * Also gets the lowest number of movable tokens the opponent can move
	 * @param firstJump To determine if it is the first jump to be made 
	 * @param player The player's ID
	 * @param opponent The opponent's ID
	 * @param nextTempBoard The temporary board created from the previous move
	 * @param y The Y Coordinate of the token.
	 * @param x The X Coordinate of the token.
	 * @param currentMaxJA The highest number of jumps currently held for a possible path.
	 * @param jumpAmount The current number of jumps held for the current path traversed.
	 * @param p The Path order of the previous jumps using U,L,D,R.
	 * @param movableLowestOpponent The number of pieces the opponent can move based upon the current board
	 * @return The lowest number of tokens the opponent can move in a possible board
	 */
	public int makeAllMovesList(boolean firstJump, int player, int opponent, 
													  Tile[][] nextTempBoard, int y, int x, int currentMaxJA, 
													  int jumpAmount, String p, int movableLowestOpponent) {
		// Create a new list of jump values for the current path
		ArrayList<Integer> jumpVals = new ArrayList<Integer>();
		String oldPath = ""; 
		String path = p; //The string of the order of different directions that the token will take ex. “ULDR”
		int up, down, left, right; // Integer values for each possible jump move
		int oldMax = currentMaxJA;
		tempBoard = nextTempBoard; // Saves the passed instance of the board to a new board	
		// Set the number movable to a large number greater than the number of pieces the opponent 
		// could ever move (acts like an infinity value that will always be overwritten 
		// if a jump can be made.
		int movableOpponent = 100;
		if (!path.equals("")) possiblePaths.add(path);
		// // If the player’s current piece being analyzed has made at least one jump.
		if (!firstJump) movableOpponent = calculateMovable(player, opponent, nextTempBoard);
		if (!firstJump && movableOpponent == 0) { bestPath = path; return 0; }
		if (movableOpponent < movableLowestOpponent) { movableLowestOpponent = movableOpponent; bestPath = path; }
		// If jumping ‘UP’ is not out of bounds
		if (y - 2 >= 0) {
			// If the space above the player’s token contains an opponent’s piece 
			// and the space 2 spaces above is empty
			if (tempBoard[y-1][x].id == opponent && tempBoard[y-2][x].id == 2) { 			
				jumpAmount++;
				if (jumpAmount > currentMaxJA) currentMaxJA = jumpAmount;
				tempBoard[y][x].id = 2;
				tempBoard[y-1][x].id = 2;
				tempBoard[y-2][x].id = player;
				up = makeAllMovesList(false, player, opponent, tempBoard, (y-2), x, currentMaxJA, jumpAmount, path += "U", movableLowestOpponent);
				jumpVals.add(up); // Add the min value for that subtree of routes to the jump routes
				tempBoard[y][x].id = player;
				tempBoard[y-1][x].id = opponent;
				tempBoard[y-2][x].id = 2; 
				// Retrieve the old path to not affect the path for further 
				// possible moves from the current position.
				if (path.length() > 0) oldPath = path.substring(0, path.length()-1);
				path = oldPath;
				// Decrement the jump counter back to the original value for this board position 
				jumpAmount--;
				// Reassign the max jump counter to the original
				currentMaxJA = oldMax;
			}
		}
		// If jumping ‘DOWN’ is not out of bounds
		if (y + 2 <= 7) {
			if (tempBoard[y+1][x].id == opponent && tempBoard[y+2][x].id == 2) { 
				// If the space above the player’s token contains an opponent’s piece 
				// and the space 2 spaces above is empty
				jumpAmount++;
				if (jumpAmount > currentMaxJA) currentMaxJA = jumpAmount;
				tempBoard[y][x].id = 2;
				tempBoard[y+1][x].id = 2;
				tempBoard[y+2][x].id = player;
				down = makeAllMovesList(false, player, opponent, tempBoard, (y+2), x, currentMaxJA, jumpAmount, path += "D", movableLowestOpponent);
				jumpVals.add(down); // Add the min value for that subtree of routes to the jump routes
				tempBoard[y][x].id = player;
				tempBoard[y+1][x].id = opponent;
				tempBoard[y+2][x].id = 2;
				// Retrieve the old path to not affect the path for further 
				// possible moves from the current position.
				if (path.length() > 0) oldPath = path.substring(0, path.length()-1);
				path = oldPath;
				// Decrement the jump counter back to the original value for this board position 
				jumpAmount--;
				// Reassign the max jump counter to the original
				currentMaxJA = oldMax;
			}
		}
		// If jumping ‘LEFT’ is not out of bounds
		if (x - 2 >= 0) {
			if (tempBoard[y][x-1].id == opponent && tempBoard[y][x-2].id == 2) { 
				// If the space above the player’s token contains an opponent’s piece 
				// and the space 2 spaces above is empty
				jumpAmount++;
				if (jumpAmount > currentMaxJA) currentMaxJA = jumpAmount;
				tempBoard[y][x].id = 2;
				tempBoard[y][x-1].id = 2;
				tempBoard[y][x-2].id = player;
				left = makeAllMovesList(false, player, opponent, tempBoard, y, (x-2), currentMaxJA, jumpAmount, path += "L", movableLowestOpponent);
				jumpVals.add(left); // Add the min value for that subtree of routes to the jump routes
				tempBoard[y][x].id = player;
				tempBoard[y][x-1].id = opponent;
				tempBoard[y][x-2].id = 2;
				// Retrieve the old path to not affect the path for further 
				// possible moves from the current position.
				if (path.length() > 0) oldPath = path.substring(0, path.length()-1);
				path = oldPath;
				// Decrement the jump counter back to the original value for this board position 
				jumpAmount--;
				// Reassign the max jump counter to the original
				currentMaxJA = oldMax;
			}
		}
		// If jumping ‘RIGHT’ is not out of bounds
		if (x + 2 <= 7) {
			if (tempBoard[y][x+1].id == opponent && tempBoard[y][x+2].id == 2) { 	
				// If the space above the player’s token contains an opponent’s piece 
				// and the space 2 spaces above is empty
				jumpAmount++;
				if (jumpAmount > currentMaxJA) currentMaxJA = jumpAmount;
				tempBoard[y][x].id = 2;
				tempBoard[y][x+1].id = 2;
				tempBoard[y][x+2].id = player;
				right = makeAllMovesList(false, player, opponent, tempBoard, y, (x+2), currentMaxJA, jumpAmount, path += "R", movableLowestOpponent);
				jumpVals.add(right); // Add the min value for that subtree of routes to the jump routes
				tempBoard[y][x].id = player;
				tempBoard[y][x+1].id = opponent;
				tempBoard[y][x+2].id = 2;
				// Retrieve the old path to not affect the path for further 
				// possible moves from the current position.
				if (path.length() > 0) oldPath = path.substring(0, path.length()-1);
				path = oldPath;
				// Decrement the jump counter back to the original value for this board position 
				jumpAmount--;
				// Reassign the max jump counter to the original
				currentMaxJA = oldMax;
			}
		}
		if (jumpVals.size() == 0) return movableLowestOpponent;
		return Collections.min(jumpVals);
		
	}
	/**
	 * Makes the list of all possible moves for each movable token
	 * @param state The board state
	 * @param p The player ID
	 * @param o The opponent ID
	 * @return The list of movable tokens containing all their moves
	 */
	public ArrayList<CoordinatePair> makeAllMovableList(Tile[][] state, int p, int o) {
		ArrayList<CoordinatePair> movableList = new ArrayList<CoordinatePair>();
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				if(state[y][x].id == p) {
					Tile[][] tilesClone = new Tile[8][8];
					for (int yC = 0; yC < 8; yC++) {
						for (int xC = 0; xC < 8; xC++) tilesClone[yC][xC] = new Tile(40,40, state[yC][xC].id);
					}
					int player = p, opponent = o; 
					int bestJumps = makeAllMovesList(true, player, opponent, tilesClone, y, x, 0, 0, "", 100);
					CoordinatePair ct = new CoordinatePair(player, y, x, 100, optimalPath());
					if (bestJumps != 100) { 			
						ct.jumps = bestJumps;
						ArrayList<String> s = new ArrayList<String>();
						s = getPossiblePaths();
						ct.setPaths(s);
						movableList.add(ct);
					}
					clearOptimal();
					clearPossiblePaths();
				}
			}
		}
		return movableList;
	}
	/**
	 * Used to create the  board for each possible move
	 * @param token Token to move
	 * @param path The token's path to move
	 * @return The board following the move
	 */
	public Tile[][] makeTempBoardPath(CoordinatePair token, String path) {
		int xVal = token.x; int yVal = token.y;
		Tile[][] tilesClone = new Tile[8][8];
		for (int yC = 0; yC < 8; yC++) {
			for (int xC = 0; xC < 8; xC++) {
				tilesClone[yC][xC] = new Tile(40,40, mmBoard[yC][xC].id);
			}
		}
		if(path.length() > 0){
			for(int i = 0; i < path.length(); i++){
				if (path.charAt(i) == 'U') {
					tilesClone[yVal][xVal].id = 2; tilesClone[yVal][xVal].setTile(2);
					tilesClone[yVal-1][xVal].id = 2; tilesClone[yVal-1][xVal].setTile(2);
					tilesClone[yVal-2][xVal].id = token.id; tilesClone[yVal-2][xVal].setTile(token.id);
					yVal = yVal-2;
				}
				else if(path.charAt(i) == 'D'){
					tilesClone[yVal][xVal].id = 2; tilesClone[yVal][xVal].setTile(2);
					tilesClone[yVal+1][xVal].id = 2; tilesClone[yVal+1][xVal].setTile(2);
					tilesClone[yVal+2][xVal].id = token.id; tilesClone[yVal+2][xVal].setTile(token.id);
					yVal = yVal+2;
				}
				else if (path.charAt(i) == 'L') {
					tilesClone[yVal][xVal].id = 2; tilesClone[yVal][xVal].setTile(2);
					tilesClone[yVal][xVal-1].id = 2; tilesClone[yVal][xVal-1].setTile(2);
					tilesClone[yVal][xVal-2].id = token.id; tilesClone[yVal][xVal-2].setTile(token.id);
					xVal = xVal-2;
				}
				else if(path.charAt(i) == 'R'){
					tilesClone[yVal][xVal].id = 2; tilesClone[yVal][xVal].setTile(2);
					tilesClone[yVal][xVal+1].id = 2; tilesClone[yVal][xVal+1].setTile(2);
					tilesClone[yVal][xVal+2].id = token.id; tilesClone[yVal][xVal+2].setTile(token.id);			
					xVal = xVal+2;
				}
			}
		}
		return tilesClone;
	}
	/**
	 * Provides the best Max (Black) token to move.
	 * @return the best Max (Black) token to move.
	 */
	public CoordinatePair getBestMaxToken() { return bestMaxToken; }
	/**
	 * Provides the best Min (White) token to move.
	 * @return the best Min (White) token to move.
	 */
	public CoordinatePair getBestMinToken() { return bestMinToken; }
	/**
	 * Provides the best move for the best Max (Black) token to take.
	 * @return the best move for the best Max (Black) token to take.
	 */
	public String getBestMoveMax() { return bestMoveMax; }
	/**
	 * Provides the best move for the Min (White) token to take.
	 * @return the best move for the Min (White) token to take.
	 */
	public String getBestMoveMin() { return bestMoveMin; }
	/**
	 * Removes the best Max (Black) token and move so that the previously assessed token is not reused by mistake
	 */
	public void clearBestMoveMax() { bestMaxToken = null; plyCounterMax = 1; bestMoveMax = ""; }
	/**
	 * Removes the best Min (White) token and move so that the previously assessed token is not reused by mistake
	 */
	public void clearBestMoveMin() { bestMinToken = null; plyCounterMin = 1; bestMoveMin = ""; }
	/**
	 * Provides a list of all paths the token can take.
	 * @return a list of all paths the token can take.
	 */
	public ArrayList<String> getPossiblePaths() { return possiblePaths; }
	/**
	 * Clears the list of paths from the previous token so it is not reused by mistake
	 */
	public void clearPossiblePaths() { possiblePaths.clear(); }
	/**
	 * Sets the turn limit for each Minimax and Minimax with Alpha-Beta Pruning run.
	 * @param limit the turn limit for each Minimax and Minimax with Alpha-Beta Pruning run.
	 */
	public void setPlyLimit(int limit) {
		plyLimit = limit;
	}
	/**
	 * Resets the statistics for the next game if New Game is selected in the MainGame class
	 */
	public void resetCounts() {
		cutoffCount = 0;
		evaluationCount = 0;
		branchesCount = 0;
		branchListCount = 0;
	}
	public int countPieces(int player, int opponent, Tile[][] tempBoard) {
		int pieces = 0;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				if (tempBoard[y][x].id == opponent) pieces++;
			}
		}
		return pieces;
	}
}
