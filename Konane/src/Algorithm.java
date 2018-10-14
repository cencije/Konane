
public class Algorithm {

	Tile[][] board = new Tile[8][8];
	int playerID;
	MainGame mainGame;
	public void setMain(MainGame mg) {
		mainGame = mg;
	}
	public void setBoard(Tile[][] currentBoard) {
		board = currentBoard;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				System.out.print(board[y][x].id);
			}
			System.out.println();
		}
	}
	
	public void moveOne(int playerMoving) {
		playerID = playerMoving;
		
		if (playerID == 1) {
			//String strBlack = "\nPlayer Black Movable Pieces: [";
			//board[0][2].setTile(2);
			boolean valid = false;
			OUTER_LOOP:
			for (int y = 0; y < 8; y++) {
				INNER_LOOP:
				for (int x = 0; x < 8; x++) {
					if (board[y][x].id == 1) {
						System.out.println("Checking: (" + x + ", " + y + ") ");
						board[y][x].canMove = false;
						//System.out.print("Checking: (" + x + ", " + y + ") ");
						if (y - 2 >= 0) {
							if (board[y-1][x].id == 0 && board[y-2][x].id == 2) { 
								valid = true;
								//board[y][x].canMove = true;
								board[y][x].id = 2; board[y][x].setTile(2);
								board[y-1][x].id = 2; board[y-1][x].setTile(2);
								board[y-2][x].id = 1; board[y-2][x].setTile(1);
								mainGame.setPostMoveBoard(board);
								break INNER_LOOP;
							}
						}
						if (y + 2 <= 7 && !valid) {
							if (board[y+1][x].id == 0 && board[y+2][x].id == 2) { 
								valid = true;
								//board[y][x].canMove = true;
								board[y][x].id = 2; board[y][x].setTile(2);
								board[y+1][x].id = 2; board[y+1][x].setTile(2);
								board[y+2][x].id = 1; board[y+2][x].setTile(1);
								mainGame.setPostMoveBoard(board);
								break INNER_LOOP;
							}
						}
						if (x - 2 >= 0 && !valid) {
							if (board[y][x-1].id == 0 && board[y][x-2].id == 2) { 
								valid = true;
								//board[y][x].canMove = true;
								board[y][x].id = 2; board[y][x].setTile(2);
								board[y][x-1].id = 2; board[y][x-1].setTile(2);
								board[y][x-2].id = 1; board[y][x-2].setTile(1);
								mainGame.setPostMoveBoard(board);
								break INNER_LOOP;
							}
						}
						if (x + 2 <= 7 && !valid) {
							if (board[y][x+1].id == 0 && board[y][x+2].id == 2) { 
								valid = true;
								//board[y][x].canMove = true;
								board[y][x].id = 2; board[y][x].setTile(2);
								board[y][x+1].id = 2; board[y][x+1].setTile(2);
								board[y][x+2].id = 1; board[y][x+2].setTile(1);
								mainGame.setPostMoveBoard(board);
								break INNER_LOOP;
							}
						}
					}
				}
				if (valid) break OUTER_LOOP;
			}
			if (!valid) mainGame.gameOver(0);
		}
		else {
			//String strWhite = "\nPlayer White Movable Pieces: [";
			//board[0][2].setTile(2);
			boolean valid = false;
			OUTER_LOOP:
			for (int y = 0; y < 8; y++) {
				INNER_LOOP:
				for (int x = 0; x < 8; x++) {
					if (board[y][x].id == 0) {
						board[y][x].canMove = false;
						System.out.println("Checking: (" + x + ", " + y + ") ");
						if (y - 2 >= 0) {
							if (board[y-1][x].id == 1 && board[y-2][x].id == 2) { 
								valid = true;
								//board[y][x].canMove = true;
								board[y][x].id = 2; board[y][x].setTile(2);
								board[y-1][x].id = 2; board[y-1][x].setTile(2);
								board[y-2][x].id = 0; board[y-2][x].setTile(0);
								mainGame.setPostMoveBoard(board);
								break INNER_LOOP;
							}
						}
						if (y + 2 <= 7 && !valid) {
							if (board[y+1][x].id == 1 && board[y+2][x].id == 2) { 
								valid = true;
								//board[y][x].canMove = true;
								board[y][x].id = 2; board[y][x].setTile(2);
								board[y+1][x].id = 2; board[y+1][x].setTile(2);
								board[y+2][x].id = 0; board[y+2][x].setTile(0);
								mainGame.setPostMoveBoard(board);
								break INNER_LOOP;
							}
						}
						if (x - 2 >= 0 && !valid) {
							if (board[y][x-1].id == 1 && board[y][x-2].id == 2) { 
								valid = true;
								//board[y][x].canMove = true;
								board[y][x].id = 2; board[y][x].setTile(2);
								board[y][x-1].id = 2; board[y][x-1].setTile(2);
								board[y][x-2].id = 0; board[y][x-2].setTile(0);
								mainGame.setPostMoveBoard(board);
								break INNER_LOOP;
							}
						}
						if (x + 2 <= 7 && !valid) {
							if (board[y][x+1].id == 1 && board[y][x+2].id == 2) { 
								valid = true;
								//board[y][x].canMove = true;
								board[y][x].id = 2; board[y][x].setTile(2);
								board[y][x+1].id = 2; board[y][x+1].setTile(2);
								board[y][x+2].id = 0; board[y][x+2].setTile(0);
								mainGame.setPostMoveBoard(board);
								break INNER_LOOP;
							}
						}
					}
				}
			if (valid) break OUTER_LOOP;
			}
			if (!valid) mainGame.gameOver(1);
		}
		
	}
	
	public void moveMostJumps(int playerMoving) {
		
	}
	public void moveLeastMoves(int playerMoving) {
		
	}
	
	public void miniMax() {
		
	}
}
