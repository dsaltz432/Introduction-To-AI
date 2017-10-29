public class CPU {
	public char cpuType;
	
	public CPU(char cpuType) {
		this.cpuType = cpuType;
	}

	/*
	 -This is a recursive depth first search algorithm
	 */
	public Action minimax(State s, char player){
		int initialUtility = (player == cpuType) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
		Action bestA = new Action(-999,-999,initialUtility);
		
		/*  Checking terminal states */
		char winner = s.wonGame(); // x, y, or Character.MIN_VALUE
		if (winner == cpuType) {
			bestA.util = -1;
		} else if (winner == Game.alternatePlayer(cpuType)) {
			bestA.util = 1;
		} else if (s.isTie()) {
			bestA.util = 0;
		}
		else {	/* Not at a terminal state yet */	
			for (Action a : s.applicableActions()) {
				s.move(a.row, a.col, player); // make this move on the board
				Action currentA = minimax(s,Game.alternatePlayer(player));
				
				// If CPU's move, minimize util, if Human's move, maximize util
				if ((player == cpuType && currentA.util < bestA.util) 
						|| (player != cpuType && currentA.util > bestA.util)) { 
					bestA = new Action(a.row,a.col,currentA.util);
				}
				s.move(a.row, a.col, Character.MIN_VALUE); // undo the move from the board
			}			
		}
		return bestA;
	}

}
