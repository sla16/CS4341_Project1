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
	
	private static final int GAME_OVER = 1;
	private static final int PLAYER_TURN = 2;
	private static final int PLAYER_NAMES = 4;
	private static final int GAME_INFO = 5;
	
	public void processInput() throws IOException{	
	
    	String s=input.readLine();	
		List<String> ls=Arrays.asList(s.split(" "));
		if(ls.size()==2){
		if(ls.size() == PLAYER_TURN){
			System.out.println(ls.get(0)+" "+ls.get(1));
		}
		else if(ls.size() == GAME_OVER){
			System.out.println("game over!!!");
			System.exit(0);
		}
		else if(ls.size() == GAME_INFO){          //ls contains game information
			// Initializes the (n x m) board and fills it with 9
			this.currentBoard = new int[Integer.parseInt(ls.get(1))][Integer.parseInt(ls.get(2))];
			for (int[] row : this.currentBoard) {
				Arrays.fill(row, 9);
			}
			// If the first player goes first and we are the first player, go first
			if (ls.get(3).equals("1") && this.isFirstPlayer) {
				System.out.println("4 1");
			} 
			// If the second player goes first and we are the second player, go first
			else if (ls.get(3).equals("2") && !this.isFirstPlayer) {
				System.out.println("4 1");
			}
		}
		else if(ls.size() == PLAYER_NAMES){		//player1: aa player2: bb
			if (ls.get(1).equals(this.playerName)) {
				this.isFirstPlayer = true;
			}
			//TODO combine this information with game information to decide who is the first player
		}
		else
			System.out.println("not what I want");
	}
	
	public static void main(String[] args) throws IOException {
		Player rp=new Player();
		System.out.println(rp.playerName);
		while (true){
			rp.processInput();
		}

	}

}
