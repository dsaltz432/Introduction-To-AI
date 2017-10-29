import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		Game game = new Game(3);
		
		System.err.println("Starting a new game!");
		game.newGame();
		
		System.err.println("Would you like to start a new game? Enter <yes> or <no> : ");
		while (true) {
			String response = scanner.next().toLowerCase();
			if (response.equals("no")) {
				break;	  // end the game
			} else if (response.equals("yes")){
				System.err.println("Starting a new game!");
				game.newGame();
				System.err.println("Would you like to start a new game? Enter <yes> or <no> : ");
			} else {
				System.err.println("Please enter <yes> or <no> : ");
			}
		}
		scanner.close();
	}
}


