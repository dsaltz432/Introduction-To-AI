import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		Game game = new Game(scanner,3);
		
		System.err.println("Starting a new game!");
		game.newGame();
		
		while (true) {
			System.err.println("Would you like to start a new game? Enter <yes> or <no> : ");
			String response = scanner.next().toLowerCase();
			
			while (!response.equals("yes") && !response.equals("no")) {
				System.out.println("Please enter <yes> or <no>: ");
				response = scanner.next().toLowerCase();
			}
			
			if (response.equals("yes")) {
				System.err.println("Starting a new game!");
				game.newGame();
			} else {
				System.out.println("\nThanks for playing!");
				break;	  // end the game
			}
		}
		scanner.close();
	}
}


