package referee;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class Player {

	String playerName="AI_Player";
	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	boolean first_move=false;
	boolean isFirstPlayer = false;
	int[][] currentBoard;
	int[][] nextBoard;
	ArrayList<int[][]> possibleMoves = new ArrayList<int[][]>();
	int rows, columns;
	int numToWin;
	int secondsToPlay;
	
	private static final int GAME_OVER = 1;
	private static final int PLAYER_TURN = 2;
	private static final int PLAYER_NAMES = 4;
	private static final int GAME_INFO = 5;
	private static final int PUSH = 1;
	
	public void processInput() throws IOException{	
	
    	String s=input.readLine();	
		List<String> ls=Arrays.asList(s.split(" "));

		if(ls.size() == PLAYER_TURN){
			int row = Integer.parseInt(ls.get(0));
			int action = Integer.parseInt(ls.get(1));
			updateBoard(row, action, false);
			generateMoves(true);
			// TODO: Find out the best move and make it
			//updateBoard(bestMove, bestMove, true);			
//			for (int i = 0; i < columns; i++){
//				if(this.currentBoard[rows-1][i] == 9){
//					int[][] heuristicBoard = this.currentBoard;
//				}
//			}
			int randomMove = (int) Math.floor(Math.random() * this.currentBoard[0].length);
			updateBoard(randomMove, 1, true);
			System.out.println(randomMove+" 1");
		}
		else if(ls.size() == GAME_OVER){
			System.out.println("game over!!!");
			System.exit(0);
		}
		else if(ls.size() == GAME_INFO){
			// Sets the number of pieces in a row to win and time to play
			this.rows = Integer.parseInt(ls.get(0));
			this.columns = Integer.parseInt(ls.get(1));
			this.numToWin = Integer.parseInt(ls.get(2));
			this.secondsToPlay = Integer.parseInt(ls.get(4));
			
			// Initializes the (n x m) board and fills it with 9
			this.currentBoard = new int[this.rows][this.columns];
			for (int[] row : this.currentBoard) {
				Arrays.fill(row, 9);
			}
			
			// If the first player goes first and we are the first player, go first
			// If the second player goes first and we are the second player, go first
			if ((ls.get(3).equals("1") && this.isFirstPlayer) || (ls.get(3).equals("2") && !this.isFirstPlayer)) {
				// TODO: What move should we make if we are first?
				if (this.columns % 2 == 0){
					updateBoard(this.columns/2, 1, true);
					System.out.println(this.columns/2 + " 1");
				} 
				else{
					updateBoard((int) (this.columns/2 + 0.5), 1, true);
					System.out.println((int)(this.columns/2 + 0.5) + " 1");
				}
				
			}
		}
		else if(ls.size() == PLAYER_NAMES){
			// Sets a flag to see if we are the first player
			if (ls.get(1).equals(this.playerName)) {
				this.isFirstPlayer = true;
			}
		}
		else
			System.out.println("Unexpected input");
	}
	
	private void generateMoves(boolean isMyTurn) {
		for (int i = 0; i < this.currentBoard.length; i++) {
			this.nextBoard = new int[this.rows][this.columns];
			for(int j = 0; j < this.rows; j++) {
				for(int k = 0; k < this.columns; k++) {
					this.nextBoard[j][k] = this.currentBoard[j][k];
				}
			}
			updateHeuristicBoard(i, 1, isMyTurn);
			this.possibleMoves.add(this.nextBoard);
		}
	}
	/**
	 * This method updates the board based on the piece played. Takes into
	 * consideration the action (place or pop)
	 * @param position The position on the board that was played as an int
	 * @param action The action as place or pop as an int
	 * @param isMyTurn True or false to update if it is my turn
	 */
	public void updateBoard(int position, int action, boolean isMyTurn) {
		if(action == PUSH)
		{
			for (int i = this.currentBoard.length-1; i > -1; i--) {
				if (this.currentBoard[i][position] == 9) {
					if (isMyTurn) {
						this.currentBoard[i][position] = this.isFirstPlayer ? 1 : 2;
					} else {
						this.currentBoard[i][position] = this.isFirstPlayer ? 2 : 1;
					}
					break;
				}
			}
		} 
		else 
		{
			// Moves all the pieces in that position/column down one row
			for (int i = this.currentBoard.length-1; i > -1; i--) {
				if (i == 0) {
					this.currentBoard[i][position] = 9;
				} else {
					this.currentBoard[i][position] = this.currentBoard[i-1][position];
				}
			}
		}
	}
	
	public void updateHeuristicBoard(int position, int action, boolean isMyTurn) {
		if(action == PUSH)
		{
			for (int i = this.nextBoard.length-1; i > -1; i--) {
				if (this.nextBoard[i][position] == 9) {
					if (isMyTurn) {
						this.nextBoard[i][position] = this.isFirstPlayer ? 1 : 2;
					} else {
						this.nextBoard[i][position] = this.isFirstPlayer ? 2 : 1;
					}
					break;
				}
			}
		} 
		else 
		{
			// Moves all the pieces in that position/column down one row
			for (int i = this.nextBoard.length-1; i > -1; i--) {
				if (i == 0) {
					this.nextBoard[i][position] = 9;
				} else {
					this.nextBoard[i][position] = this.nextBoard[i-1][position];
				}
			}
		}
	}
	
//	public void maxMove(int[][] board){
//		int[][] heuristicBoard = this.currentBoard;
//		int i = 0;
//		for (int i = 0; i < Integer.parseInt(ls.get(0)); i++){
//			for(int )
//		}
//		for (heuristicBoard.length > )
//		Integer.parseInt(ls.get(0));
//	}
	
	public int getHeuristicValue(int[][] board, boolean isMyTurn){
		int value = 0;
		int heuristicRow = board.length;
		int heuristicCol = board[0].length;
		if (heuristicRow > 1){
			if (heuristicCol > 1){
				
				if (board[0][0] == (isMyTurn ? 2 : 1)){
					value += 1;
					if (board[0][1] ==(isMyTurn ? 2 : 1)){
						value += 1;
					}
					if (board[1][1] ==(isMyTurn ? 2 : 1)){
						value += 1;
					}
					if (board[1][0] ==(isMyTurn ? 2 : 1)){
						value += 1;
					}	
				}
				
				if (board[heuristicRow - 1][heuristicCol - 1] == (isMyTurn ? 2 : 1)){
					value +=1;
					if (board[heuristicRow-1][heuristicCol-2] ==(isMyTurn ? 2 : 1)){
						value += 1;
					}
					if (board[heuristicRow-2][heuristicCol-2] ==(isMyTurn ? 2 : 1)){
						value += 1;
					}
					if (board[heuristicRow-2][heuristicCol-1] ==(isMyTurn ? 2 : 1)){
						value += 1;
					}
				}
				
				if (board[heuristicRow - 1][0] == (isMyTurn ? 2 : 1)){
					value += 1;
					if (board[heuristicRow-1][1] ==(isMyTurn ? 2 : 1)){
						value += 1;
					}
					if (board[heuristicRow-2][0] ==(isMyTurn ? 2 : 1)){
						value += 1;
					}
					if (board[heuristicRow-2][1] ==(isMyTurn ? 2 : 1)){
						value += 1;
					}
				}
				
				if (board[0][heuristicCol - 1] == (isMyTurn ? 2 : 1)){
					value += 1;
					if (board[0][heuristicCol-2] ==(isMyTurn ? 2 : 1)){
						value += 1;
					}
					if (board[1][heuristicCol-2] ==(isMyTurn ? 2 : 1)){
						value += 1;
					}
					if (board[1][heuristicCol-1] ==(isMyTurn ? 2 : 1)){
						value += 1;
					}
				}
				
			}
			else{
				if (board[0][0] == (isMyTurn ? 2 : 1)){
					value += 1;
					if (board[1][0] == (isMyTurn ? 2: 1)){
						value += 1;
					}
				}
				
				if (board[heuristicRow-1][0] == (isMyTurn ? 2: 1)){
					value += 1;
					if (board[heuristicRow-2][0] == (isMyTurn ? 2: 1)){
						value += 1;
					}
				}
			}
		}
		else{
			if (board[0][0] == (isMyTurn ? 2 : 1)){
				value += 1;
				if (board[0][1] == (isMyTurn ? 2: 1)){
					value += 1;
				}
			}
			if (board[0][heuristicCol-1] == (isMyTurn ? 2: 1)){
				value += 1;				
				if (board[0][heuristicCol-2] == (isMyTurn ? 2: 1)){
					value += 1;
				}
			}
		}
		
		if (heuristicRow >2){
			if (heuristicCol > 2){
				for(int n = 1; n < (heuristicRow -1); n++){
					if (board[n][0] == (isMyTurn ? 2: 1)){
						value += 1;
						if (board[n][1] == (isMyTurn ? 2: 1)){
							value += 1;
						}
						if (board[n-1][1] == (isMyTurn ? 2: 1)){
							value += 1;
						}
						if (board[n-1][0] == (isMyTurn ? 2: 1)){
							value += 1;
						}
						if (board[n+1][1] == (isMyTurn ? 2: 1)){
							value += 1;
						}
						if (board[n+1][0] == (isMyTurn ? 2: 1)){
							value += 1;
						}
					}
					
					if (board[n][heuristicCol-1] == (isMyTurn ? 2: 1)){
						value += 1;
						if (board[n-1][heuristicCol-1] == (isMyTurn ? 2: 1)){
							value += 1;
						}
						if (board[n-1][heuristicCol-2] == (isMyTurn ? 2: 1)){
							value += 1;
						}
						if (board[n][heuristicCol-2] == (isMyTurn ? 2: 1)){
							value += 1;
						}
						if (board[n+1][heuristicCol-2] == (isMyTurn ? 2: 1)){
							value += 1;
						}
						if (board[n+1][heuristicCol-1] == (isMyTurn ? 2: 1)){
							value += 1;
						}
					}
				}
				
				for(int m = 1; m < (heuristicCol -1); m++){
					if (board[0][m] == (isMyTurn ? 2: 1)){
						value += 1;
						if (board[0][m-1] == (isMyTurn ? 2: 1)){
							value += 1;
						}
						if (board[1][m-1] == (isMyTurn ? 2: 1)){
							value += 1;
						}
						if (board[1][m] == (isMyTurn ? 2: 1)){
							value += 1;
						}
						if (board[1][m+1] == (isMyTurn ? 2: 1)){
							value += 1;
						}
						if (board[0][m+1] == (isMyTurn ? 2: 1)){
							value += 1;
						}
					}
					
					if (board[heuristicRow-1][m] == (isMyTurn ? 2: 1)){
						value += 1;
						if (board[heuristicRow-1][m-1] == (isMyTurn ? 2: 1)){
							value += 1;
						}
						if (board[heuristicRow-2][m-1] == (isMyTurn ? 2: 1)){
							value += 1;
						}
						if (board[heuristicRow-2][m] == (isMyTurn ? 2: 1)){
							value += 1;
						}
						if (board[heuristicRow-2][m+1] == (isMyTurn ? 2: 1)){
							value += 1;
						}
						if (board[heuristicRow-1][m+1] == (isMyTurn ? 2: 1)){
							value += 1;
						}
					}
				}
			}
			else{
				for (int n = 1; n < (heuristicRow-1); n++){
					if (board[n][heuristicCol-1] == (isMyTurn ? 2: 1)){
						value += 1;
						if (board[n+1][heuristicCol-1] == (isMyTurn ? 2: 1)){
							value += 1;
						}
						if (board[n-1][heuristicCol-1] == (isMyTurn ? 2: 1)){
							value += 1;
						}
					}
				}
			}
		}
		else{
			for (int m = 1; m < (heuristicCol-1); m++){
				if (board[heuristicRow-1][m] == (isMyTurn ? 2: 1)){
					value += 1;
					if (board[heuristicRow-1][m-1] == (isMyTurn ? 2: 1)){
						value += 1;
					}
					if (board[heuristicRow-1][m+1] == (isMyTurn ? 2: 1)){
						value += 1;
					}
				}
			}
		}
		
		if (heuristicRow > 2){
			if (heuristicCol > 2){
				for(int y = 1; y < (heuristicCol-1); y++){
					for(int z = 1; z < (heuristicRow -1); z++){
						if (board[z][y] == (isMyTurn ? 2:1)){
							value += 1;
							if(board[z-1][y-1] == (isMyTurn ? 2:1)){
								value += 1;
							}
							if(board[z-1][y] == (isMyTurn ? 2:1)){
								value += 1;
							}
							if(board[z-1][y+1] == (isMyTurn ? 2:1)){
								value += 1;
							}
							if(board[z][y-1] == (isMyTurn ? 2:1)){
								value += 1;
							}
							if(board[z][y+1] == (isMyTurn ? 2:1)){
								value += 1;
							}
							if(board[z+1][y-1] == (isMyTurn ? 2:1)){
								value += 1;
							}
							if(board[z+1][y] == (isMyTurn ? 2:1)){
								value += 1;
							}
							if(board[z+1][y+1] == (isMyTurn ? 2:1)){
								value += 1;
							}
						}
					}
				}
			}
		}
		if (isMyTurn){
			return value;
		}
		else
			return -value;
	}
	
	/**
	 * This method will return a theoretical board based on a move provided to the board provided.
	 * @param board Current game board
	 * @param position Position to make a move
	 * @param action POP or PUSH
	 * @param isMyTurn True if it is my turn
	 */
//	public int[][] getHeuristicBoard(int[][] board, int position, int action, boolean isMyTurn) {
//		// TODO: Use this method to compare boards
//		//throw new UnsupportedOperationException();
//		updateBoard(board, position, action, isMyTurn);
//		int[][] heuristicBoard = board;
//		return heuristicBoard;
//	}
	
	public static void main(String[] args) throws IOException {
		Player rp=new Player();
		System.out.println(rp.playerName);
		while (true){
			rp.processInput();
		}

	}

}
