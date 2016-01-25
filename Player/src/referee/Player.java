/**
 * Sam La and Nan Zhang
 * CS 4341 Project 1 - Adversial Search Connect-N
 */
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
			
			// Update board to reflect on opponent's move
			updateBoard(row, action, false);
			
			// Perform search so we can find our move
			int bestMove = AlphaBeta_MiniMax(1, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
			
			// Update board to reflect our move
			updateBoard(bestMove, 1, true);
			System.out.println(bestMove + " 1");
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
			// Currently, place piece in middle for best move
			if ((ls.get(3).equals("1") && this.isFirstPlayer) || (ls.get(3).equals("2") && !this.isFirstPlayer)) {
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
	
	// Checks the current board and finds all valid moves for the specified player
	private List<Integer> GetValidMoves(boolean isMyTurn) {
		List<Integer> possibleMoves = new ArrayList<Integer>();
		
		for (int i = this.currentBoard.length-1; i > -1; i--) {
			for (int j = 0; j < this.currentBoard[0].length; j++) {
				if (this.currentBoard[i][j] == 9) {
					if (!(containsMoves(possibleMoves, j))) {
						possibleMoves.add(j);
					}
				}
			}
		}
		return possibleMoves;
	}
	
	// Helper function to check if the provided list contains a value
	private boolean containsMoves(List<Integer> possibleMoves, int j) {
		for(int i = 0; i < possibleMoves.size(); i++) {
			if (possibleMoves.get(i) == j)
				return true;
		}
		return false;
	}
	
	// Definition of alpha beta pruning from the book page 170, 2nd edition
	// In this case, the variable currentValue will be "v" in the book
	// First time it is called will be on "Max" side, so our turn. Consecutive
	// times called will alternate between "Min" and "Max"
	// This returns the move to play
	public int AlphaBeta_MiniMax(int depth, boolean isMyTurn, int alpha_value, int beta_value) {
		int bestValue = isMyTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		int currentValue;
		// Generates next possible moves
		List<Integer> validMoves = GetValidMoves(isMyTurn);
		int bestMove = -1;
		
		// Check if depth is 0 or "end" if there are no more moves and return
		if (validMoves.size() == 0 || depth == 0) {
			bestValue = getHeuristicValue(this.currentBoard, isMyTurn);
			return bestValue;
		} 
		else {
			// Go through all the valid moves and play through it
			for (int i = 0; i < validMoves.size(); i++) {
				// Make the move and then "judge" it based on Max or Min, update board accordingly
				updateBoard(validMoves.get(i), 1, isMyTurn);
				// Maximize for me (player)
				if (isMyTurn) {
					// Continue playing, but on "Min" turn
					currentValue = AlphaBeta_MiniMax(depth - 1, false, alpha_value, beta_value);
					// Check if it is greater because we want to maximize alpha
					if (currentValue > alpha_value) {
						alpha_value = currentValue;
						bestMove = validMoves.get(i);
					}
				} 
				// Maximize for opponent
				else {
					// Continue playing, but on "Max" turn
					currentValue = AlphaBeta_MiniMax(depth - 1, true, alpha_value, beta_value);
					// Check if it is less because we want to minimize beta
					if (currentValue < beta_value) {
						beta_value = currentValue;
						bestMove = validMoves.get(i);
					}
				}
				// Revert the board back to the previous state so we can keep track of an updated board
				undoLastMove(validMoves.get(i), 1, isMyTurn);
				// "Prune tree" section, don't need to keep looking
				if (alpha_value >= beta_value) break;
			}
		}

		return bestMove;
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
	
	// Opposite of updateBoard, it removes the last piece that was placed in that position
	public void undoLastMove(int position, int action, boolean isMyTurn) {
		if(action == PUSH)
		{
			for (int i = 0; i < this.currentBoard.length; i++) {
				if (this.currentBoard[i][position] != 9) {
					this.currentBoard[i][position] = 9;
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
	
	// Heuristic function
	public int getHeuristicValue(int[][] board, boolean isMyTurn){
		int value = 0;
		int countInARow = 0;
		boolean breakDiag = false;
		int heuristicRow = this.rows;
		int heuristicCol = this.columns;
		
		for(int i = board.length-1; i > -1; i--) {
			for(int j = 0; j < board[0].length; j++) {
				// Finds first piece
				if (board[i][j] == (isMyTurn ? (this.isFirstPlayer ? 1 : 2) : (this.isFirstPlayer ? 2 : 1))) {
					countInARow = 1;
					// Check left for pieces in a row.
					for (int k = j-1; k > -1; k--) {
						if (board[i][k] == (isMyTurn ? (this.isFirstPlayer ? 1 : 2) : (this.isFirstPlayer ? 2 : 1))) {
							countInARow += 1;
						} else {
							break;
						}
					}
					// Check right for pieces in a row.
					for (int k = j+1; k < board[0].length; k++) {
						if (board[i][k] == (isMyTurn ? (this.isFirstPlayer ? 1 : 2) : (this.isFirstPlayer ? 2 : 1))) {
							countInARow += 1;
						} else {
							break;
						}
					}
					// Check up for pieces in a row.
					for (int k = i-1; k > -1; k--) {
						if (board[k][j] == (isMyTurn ? (this.isFirstPlayer ? 1 : 2) : (this.isFirstPlayer ? 2 : 1))) {
							countInARow += 1;
						} else {
							break;
						}
					}
					// Check down for pieces in a row.
					for (int k = i+1; k < board.length; k++) {
						if (board[k][j] == (isMyTurn ? (this.isFirstPlayer ? 1 : 2) : (this.isFirstPlayer ? 2 : 1))) {
							countInARow += 1;
						} else {
							break;
						}
					}
					// Check diagonal up for pieces in a row.
					for (int k = i-1; k > -1; k--) {
						for (int l = j-1; l > -1; j--) {
							if (board[k][l] == (isMyTurn ? (this.isFirstPlayer ? 1 : 2) : (this.isFirstPlayer ? 2 : 1))) {
								countInARow += 1;
							} else {
								breakDiag = true;
								break;
							}
						}
						if (breakDiag) break;
					}
					breakDiag = false;
					// Check diagonal down for pieces in a row.
					for (int k = i+1; k < board.length; k++) {
						for (int l = j+1; l < board[0].length; j++) {
							if (board[k][l] == (isMyTurn ? (this.isFirstPlayer ? 1 : 2) : (this.isFirstPlayer ? 2 : 1))) {
								countInARow += 1;
							} else {
								breakDiag = true;
								break;
							}
						}
						if (breakDiag) break;
					}
					breakDiag = false;
					// Check other diagonal up for pieces in a row.
					for (int k = i-1; k > -1; k--) {
						for (int l = j+1; l < board[0].length; j++) {
							if (board[k][l] == (isMyTurn ? (this.isFirstPlayer ? 1 : 2) : (this.isFirstPlayer ? 2 : 1))) {
								countInARow += 1;
							} else {
								breakDiag = true;
								break;
							}
						}
						if (breakDiag) break;
					}
					breakDiag = false;
					// Check other diagonal down for pieces in a row.
					for (int k = i+1; k < board.length; k++) {
						for (int l = j-1; l > -1; j--) {
							if (board[k][l] == (isMyTurn ? (this.isFirstPlayer ? 1 : 2) : (this.isFirstPlayer ? 2 : 1))) {
								countInARow += 1;
							} else {
								breakDiag = true;
								break;
							}
						}
						if (breakDiag) break;
					}
				}
				value += countInARow;
			}
		}
		
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
