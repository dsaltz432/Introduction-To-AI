public class CPU {
	public char cpuType;

	public CPU(char cpuType) {
		this.cpuType = cpuType;
	}

	private int[] minimax(State s, char player){
		int bestUtility = (player == cpuType) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		Action bestA = new Action();

		/*  Checking terminal states */
		char winner = s.wonGame(); // x, y, or Character.MIN_VALUE
		if (winner == cpuType) {
			bestUtility = 1;
		} else if (winner == Game.alternatePlayer(cpuType)) {
			bestUtility = -1;
		} else if (s.isTie()) {
			bestUtility = 0;
		}
		else {	/* Not at a terminal state yet */
			for (Action a : s.applicableActions()) {
				s.move(a.row, a.col, player); // make this move on the board
				int result = minimax(s,Game.alternatePlayer(player))[0];
				if ((player == cpuType && result > bestUtility)
						|| (player != cpuType && result < bestUtility)){
					bestUtility = result;
					bestA = new Action(a.row,a.col);
				}
				s.move(a.row, a.col, Character.MIN_VALUE); // undo the move from the board
			}
		}
		return new int[]{bestUtility,bestA.row,bestA.col};
	}

	public Action findBestMove(State s, char player){
		int[] result = minimax(s, player);
		return new Action(result[1],result[2]);
	}

}
