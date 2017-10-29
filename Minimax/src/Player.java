
public class Player {
	private char playerType;

	public Player(char type){
		this.playerType = type;
	}

	public char getPlayerType() {
		return playerType;
	}
	
	@Override
	public String toString() {
		return "Player [you are " + playerType + "]";
	}
}

