CS4341----Project 1
* Sam La      (sla)
* Nan Zhang   (nzhang2)

============================
How to Run?
java -jar Referee.jar "java -jar Player1.jar" "java -jar Player2.jar" 6 7 3 10 10


Explanation:
For Player1, it can be the player from our group. For player2, it can be any other player
for demonstration purpose.


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

============================
Experimentation:
