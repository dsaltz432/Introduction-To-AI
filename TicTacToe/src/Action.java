
public class Action {
	public int row;
	public int col;
	public int util;
	
	public Action(int row, int col, int utility) {
		this.row = row;
		this.col = col;
		this.util = utility;
	}
	
	public Action(int row, int col) {
		this.row = row;
		this.col = col;
	}

}
