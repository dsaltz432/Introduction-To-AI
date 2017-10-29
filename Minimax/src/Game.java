
import java.util.Scanner;

public class Game {
	private Player player;
	private CPU cpu;
	private Scanner scanner;
	private int boardSize;
	
	public Game(int boardSize) {
		this.scanner = new Scanner(System.in);
		this.boardSize = boardSize;
	}

	public void newGame() {
		chooseSides();
		State s = getInitialState();
		System.err.println("The positions on the board are numbered [1-9], "+
		 "going from top left to bottom right.");
		
		while (!isGoalState(s)) {			
			Action action;
			if (s.getTurn() == player.getPlayerType()) { // get move from human
				action = playerMove(s);
			} else { 									 // have AI pick move
				action = pick(s);		
			}
			s = result(s,action);
		}	
	}

	// returns the state reached after performing action a in state s
	private State result(State s, Action a) {
		System.err.println(s.getTurn() + " chooses the move: ["+a.row+" " + a.col+"]");
		s.move(a.row, a.col, s.getTurn());
		s.drawBoard();
		return new State(s.getStateBoard(),alternatePlayer(s.getTurn()));
	}
	
	public static char alternatePlayer(char s) {
		return (s == 'x') ? 'o' : 'x';
	}

	private State getInitialState() {
		return new State(new char[boardSize][boardSize],'x');
	}

	private void chooseSides() {
		System.err.println("Do you want to play as as X or O?");
		
		char playerType = scanner.next().toLowerCase().charAt(0);
		while (playerType != 'x' && playerType != 'o') { // bad input
			System.err.println("Please enter <x> or <o> : ");
			playerType = scanner.next().toLowerCase().charAt(0);
		}
		
		char cpuType = (playerType == 'x') ? 'o' : 'x';	// assigns the cpu to the opposite player type
		player = new Player(playerType);
		cpu = new CPU(cpuType);
	}

	private Action pick(State s) {
		System.err.println("The CPU moves as " + cpu.cpuType);
		return cpu.findBestMove(s,cpu.cpuType);
	}
	
	private Action playerMove(State s) {
		System.err.println(player.getPlayerType()+ ", it is your turn");
		while (true){
			int userInput = getMoveFromHuman();
			int col = userInput % 3 - 1;
			col = (col >= 0) ? col : col + boardSize;
			int row = userInput / boardSize;	
			row = (col == boardSize-1) ? row-1 : row;
			if (s.isSquareEmpty(row,col)) {
				return new Action(row,col);
			}
			else {
				System.err.println("That spot is already occupied");
			}
		}
	}
	
	private int getMoveFromHuman() {
		System.err.print("Enter the number for your move: ");
		while(!scanner.hasNextInt()) {
			scanner.next(); // next input is not an int, so consume it and move on
			System.err.println("Please enter an integer between 1 and 9: ");
		}
		int userInput = scanner.nextInt();
		
		while (userInput < 1 || userInput > 9) { // bad input
			while(!scanner.hasNextInt()) {
				scanner.next(); // next input is not an int, so consume it and move on
			}
			userInput = scanner.nextInt();
		}
		return userInput;
	}
	
	public boolean isGoalState(State s) {
		char winner;
		if (s.isTie()){
			System.err.println("It's a tie!");
			return true;
		} 
		else if ((winner = s.wonGame()) != Character.MIN_VALUE){
			System.err.println(winner + " wins!");
			return true;
		}
		return false;
	}

}
