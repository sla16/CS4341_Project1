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
	
	private static final int GAME_OVER = 1;
	private static final int PLAYER_TURN = 2;
	private static final int PLAYER_NAMES = 4;
	private static final int GAME_INFO = 5;
	
	public void processInput() throws IOException{	
	
    	String s=input.readLine();	
		List<String> ls=Arrays.asList(s.split(" "));
		if(ls.size() == PLAYER_TURN){
			updateBoard(Integer.parseInt(ls.get(0)), Integer.parseInt(ls.get(1)), false);
			// TODO: Find out the best move and make it
			//updateBoard(bestMove, bestMove, true);
			System.out.println(ls.get(0)+" "+ls.get(1));
		}
		else if(ls.size() == GAME_OVER){
			System.out.println("game over!!!");
			System.exit(0);
		}
		else if(ls.size() == GAME_INFO){          //ls contains game information
			// Sets the number of pieces in a row to win
			this.numToWin = Integer.parseInt(ls.get(2));
			// Initializes the (n x m) board and fills it with 9
			this.currentBoard = new int[Integer.parseInt(ls.get(0))][Integer.parseInt(ls.get(1))];
			for (int[] row : this.currentBoard) {
				Arrays.fill(row, 9);
			}
			// If the first player goes first and we are the first player, go first
			// If the second player goes first and we are the second player, go first
			if ((ls.get(3).equals("1") && this.isFirstPlayer) || (ls.get(3).equals("2") && !this.isFirstPlayer)) {
				updateBoard(4, 1, true);
				System.out.println("4 1");
			}
		}
		else if(ls.size() == PLAYER_NAMES){		//player1: aa player2: bb
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
		/* TODO: UPDATE FOR POP */
	}
	
	public static void main(String[] args) throws IOException {
		Player rp=new Player();
		System.out.println(rp.playerName);
		while (true){
			rp.processInput();
		}

	}

}
