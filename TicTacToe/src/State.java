import java.util.ArrayList;
import java.util.List;

public class State {
	private char turn;
	private char[][] stateBoard;
	
	public State(char[][] stateBoard, char turn) {
		this.stateBoard = stateBoard;
		this.turn = turn;
	}

	public char[][] getStateBoard() {
		return stateBoard;
	}

	public void setStateBoard(char[][] stateBoard) {
		this.stateBoard = stateBoard;
	}
	
	public char getTurn() {
		return turn;
	}

	/* Returns true if the spot is empty, otherwise returns false */
	public boolean isSquareEmpty(int row, int col){
		return stateBoard[row][col] == Character.MIN_VALUE;
	}
	
	/* Places the given character into the stateBoard */
	public void move(int row, int col, char type){
		stateBoard[row][col] = type;	
	}

	/* Returns true if the board is full, otherwise returns false */
	public boolean isTie(){
		for (int i=0;i<stateBoard.length;i++){
			for (int j=0;j<stateBoard.length;j++){
				if (stateBoard[i][j] == Character.MIN_VALUE){
					return false;
				}
			}
		}
		return true;
	}
	
	/* returns x, y, or null, for x winning, o winning, or nobody winning*/
	public char wonGame() {		
		char p = CheckV();
		if (p!=Character.MIN_VALUE){
			return p;
		}
		p = CheckH();
		if (p!=Character.MIN_VALUE){
			return p;
		}
		p = CheckD();
		if (p!=Character.MIN_VALUE){
			return p;
		}
		return Character.MIN_VALUE;		
	}

	public char CheckAnswer(int x,int y, int x1, int y1){	 
		char p1 = stateBoard[x][y];
		if (p1==Character.MIN_VALUE){
			return Character.MIN_VALUE;
		}
		while (x >= 0 && x < stateBoard.length && y >= 0 && y < stateBoard.length) {
			if (stateBoard[x][y] != p1) {
				return Character.MIN_VALUE;
			}
			x += x1;
			y += y1;
		}
		return p1;
	}
	
	/* Checks for a winner horizontally */
	public char CheckH(){
		for (int i=0; i < stateBoard.length; i++) {
			char p = CheckAnswer(i,0,0,1);
			if (p != Character.MIN_VALUE) {
				return p;
			}
		}
		return Character.MIN_VALUE;
	}
	
	/* Checks for a winner vertically */
	public char CheckV(){
		for(int i = 0; i<stateBoard.length;i++){
			char p = CheckAnswer(0,i,1,0);
			if (p!=Character.MIN_VALUE){
				return p;
			}
		}	return Character.MIN_VALUE;
	}
	
	/* Checks for a winner diagonally */
	public char CheckD(){
		char p = CheckAnswer(0, 0, 1, 1);
		if (p != Character.MIN_VALUE) {
			return p;
		}
		p = CheckAnswer(0, stateBoard.length-1, 1, -1);
		if (p != Character.MIN_VALUE) {
			return p;
		}
		return Character.MIN_VALUE;
	}
	
	public void drawBoard(){
		int row = stateBoard.length;
		int col = stateBoard[0].length;;

		System.err.print("\n    ");
		for (int i = 0; i < col; i++)
			System.err.print(i + "   ");
		System.err.println();

		for (int i = 0; i < row; i++) {
			System.err.print(i + "  ");
			for (int j = 0; j < col; j++) {
				if (j !=0){
					System.err.print(".");
				}
				System.err.print(" " + (stateBoard[i][j] == Character.MIN_VALUE ? "-" :  stateBoard[i][j]) + " "); // replaces null with -
			}
			System.err.println();
			if (i != (row - 1)) {
				System.err.print("   ");
				for (int j = 0; j <col; j++) {
					if (j != 0)
						System.err.print("......");
				}
				System.err.println();
			}
		}
		System.err.println("\n");
	}

	/* returns a List of Actions in spots that havne't already been taken */
	public List<Action> applicableActions(){
		List<Action> possibleMoves = new ArrayList<Action>();
		for(int p = 0; p < stateBoard.length; ++p){
			for(int r = 0; r < stateBoard[0].length; ++r){
				if (isSquareEmpty(p, r)) {
					possibleMoves.add(new Action(p,r));
				}
			}
		}
		return possibleMoves;
	}
	
}
