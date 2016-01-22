package referee;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class Player {

	String playerName="AI_Player";
	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	boolean first_move=false;
	boolean isFirstPlayer = false;
	int[][] currentBoard;
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
			updateBoard(Integer.parseInt(ls.get(0)), Integer.parseInt(ls.get(1)), false);
			// TODO: Find out the best move and make it
			//updateBoard(bestMove, bestMove, true);
			int randomMove = (int) Math.floor(Math.random() * this.currentBoard[0].length);
			System.out.println(randomMove+" 1");
		}
		else if(ls.size() == GAME_OVER){
			System.out.println("game over!!!");
			System.exit(0);
		}
		else if(ls.size() == GAME_INFO){
			// Sets the number of pieces in a row to win and time to play
			this.numToWin = Integer.parseInt(ls.get(2));
			this.secondsToPlay = Integer.parseInt(ls.get(4));
			
			// Initializes the (n x m) board and fills it with 9
			this.currentBoard = new int[Integer.parseInt(ls.get(0))][Integer.parseInt(ls.get(1))];
			for (int[] row : this.currentBoard) {
				Arrays.fill(row, 9);
			}
			
			// If the first player goes first and we are the first player, go first
			// If the second player goes first and we are the second player, go first
			if ((ls.get(3).equals("1") && this.isFirstPlayer) || (ls.get(3).equals("2") && !this.isFirstPlayer)) {
				// TODO: What move should we make if we are first?
				updateBoard(4, 1, true);
				System.out.println("4 1");
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
	
	/**
	 * This method will return a theoretical board based on a move provided to the board provided.
	 * @param board Current game board
	 * @param position Position to make a move
	 * @param action POP or PUSH
	 * @param isMyTurn True if it is my turn
	 */
	public int[][] getHeuristicBoard(int[][] board, int position, int action, boolean isMyTurn) {
		// TODO: Use this method to compare boards
		throw new UnsupportedOperationException();
	}
	
	public static void main(String[] args) throws IOException {
		Player rp=new Player();
		System.out.println(rp.playerName);
		while (true){
			rp.processInput();
		}

	}

}
