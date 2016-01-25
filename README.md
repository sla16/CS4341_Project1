CS4341----Project 1
* Sam La      (sla)
* Nan Zhang   (nzhang2)

============================
How to Run?
java -jar Referee.jar "java -jar Player.jar" "java -jar Player2.jar" 6 7 3 10 10

Explanation:
One of the players will be our player, Player.jar (which is provided) and the other player will be the player we will play against. In the example above, it will be Player vs Player2.

Code Explanation:
We worked off of the testPlayer.java source code that was provided. We modified it so we had a working player and worked from there. We set up all the settings and keep track of the game board through the attribute in the Player class, currentBoard. This can be accessed anytime with this.currentBoard.

After this, we implemented (or tried to...) minimax with alpha beta pruning. This code is inspired from the pseudocode from the book (page 170, edition 2) and we combined both min and max portions into one since it seemed easier that way with a flag to indicate if it is min or max turn.
To explain our search algorithm, it basically: 
1. gets all the possible moves
2. for each move, put it on the board
3. rank that board (so our heuristic function)
4. check and replace if better than before

You will see other helper functions such as updating the board, undoing the board and etc. This is to preserve our current board so it doesn't get changed to something else in the process.

============================
Heuristic:
The heuristic our group has designed checks every single spot on the board. Once there is
a checker on the board which is from our AI player, it will check all adjacent spots for
that checker. The heuristic simply counts the number of same-color-checker around that
point and automatically assign weight of 1 to every adjacent same-color-checker. When the
heuristic does the same thing for the opponent, it changes the weight to -1.

Evaluation function: EVAL = W1 * S1 + W2 * S2 +....+ Wn * Sn - A1 * P1 - A2 * P2 -....-Am * Pm

W1, W2,....,Wn = The weight assigned to our AI player's checkers.
S1, S2,....,Sn = The number of adjacent same-color-checker for AI player
A1, A2,....,Am = The weight assigned to our opponent's checkers
P1, P2,....,Pm = The number of adjacent same-color-checker for opponent
