import java.util.InputMismatchException;
import java.util.Scanner;
import java.lang.Math;


/**
 * This class makes a game of TicTacToe for two
 * human players.
 */
public class TicTacToe {

	private static final int SIZE = 3;
	private static char[][] gameBoard;
	private static boolean[][] availablePlaysBoard;
	private static int currentPlayer = 0;

	private static Scanner keyboard = new Scanner(System.in);
	
	public TicTacToe() {
		printInstructions();
		createBoard();
		runGame();
	}

	/**
	 * The main game loop that keeps running
	 * until the game is over.
	 */
	private static void runGame() {

		boolean gameOver = false;

		while(!gameOver) {
			char currentPlayer = changePlayer();
			int playerInput = getInput();
			int row = getRow(playerInput);
			int col = getCol(playerInput);
			placeToken(row, col, currentPlayer);
			printBoard();

			if(checkForWinner(gameBoard)) {
				System.out.println("CONGRATULATIONS! " + currentPlayer + " won!");
				gameOver = true;
			} else if(checkForDraw()) {
				System.out.println("There is no winner.");
				gameOver = true;
			}
		}
	}

	/**
	 * Gets the input from the player. If the player does not
	 * enter a number or enters a number outside the range of
	 * the board, the method will throw an exception. Player
	 * cannot move on until they have entered a valid input.
	 *
	 * @exception InputMismatchException Occurs when the player
	 * 									does not enter a number
	 *
	 * @exception InputRangeException Occurs when the player
	 * 								enters a number outside the
	 * 								scope of the game board
	 *
	 * @return The position on the game board where the user wants
	 * to place their token, given as a number.
	 */
	private static int getInput() {
		System.out.println("Where do you want to place your token?");

		int input = 0;
		boolean numWorks = false;

		while(!numWorks) {
			System.out.println("Enter a number between 1 and " + (SIZE * SIZE));

			try {
				input = keyboard.nextInt();

				if(input < 1 || input > SIZE * SIZE) {
					throw new InputRangeException();
				}

				numWorks = true;
			} catch(InputMismatchException e) { // Runs if user does not enter a number
				System.out.println("Something went wrong. Try again.");
				keyboard.next();
			} catch(InputRangeException e) {    // Runs if user's input is not within the bounds of the game board
				System.out.println(e.toString());
			}

		}

		return input;
	}

	/**
	 * Does the calculation to know what row the player's input is in.
	 *
	 * @param playerInput The position on the game board, ranging from 1 to the size of the board squared
	 * @return The index of the row that the position is in
	 */
	private static int getRow(int playerInput) {
		// Gets the row by dividing by the size and rounding down
		// Ex: (Size = 3) 2 / 3 = .66   ->  Row index = 0

		// If the number is a multiple of the size, subtract 1
		// Ex: (Size = 3) 3 / 3 = 1     ->  Row index = 0
		int row = Math.floorDiv(playerInput, SIZE);
		if(playerInput % SIZE == 0) {
			row--;
		}

		return row;
	}

	/**
	 * Does the calculation to know what column the player's input is in.
	 *
	 * @param playerInput The position on the game board, ranging from 1 to the size of the board squared
	 * @return The index of the column that the position is in
	 */
	private static int getCol(int playerInput) {
		// Gets the column by modding the input and subtracting 1
		// Ex: (Size = 3) 4 % 3 = 1     -> Column index = 0

		// If the number is a multiple of the size, it should be the last index(size - 1)
		// Ex: (Size = 3) 6 % 3 = 0     -> Column index = 2

		int col = playerInput % SIZE - 1;
		if(playerInput % SIZE == 0) {
			col = SIZE - 1;
		}

		return col;
	}

	/**
	 * Checks whether the position trying to be played is available
	 * and places the token if it is.
	 *
	 * @param row The index of the row where the token will be placed
	 * @param col The index of the column where the token will be placed
	 * @param currentPlayer Who the current player is, either "X" or "O"
	 */
	private static void placeToken(int row, int col, char currentPlayer) {


		// Ensures that player does not override a position that's
		// already been played
		boolean successful = false;
		while(!successful) {
			if (availablePlaysBoard[row][col]) {
				gameBoard[row][col] = currentPlayer;
				availablePlaysBoard[row][col] = false;
				successful = true;
			} else { // If a player chooses a spot that's already taken, make them choose again
				System.out.println("Position already taken. Try again.");
				int input = getInput();
				row = getRow(input);
				col = getCol(input);
			}

		}
	}

	/**
	 * Calls methods to check for a win, either through a row, column, or
	 * diagonally. Is called after every play.
	 *
	 * @param gameBoard The game board in its current state
	 * @return Boolean stating whether the current player has won
	 */
	private static boolean checkForWinner(char[][] gameBoard) {
		return checkRows() || checkCols() || checkDiagonals();
	}

	/**
	 * Checks the rows of the game board to see if the current player
	 * has won. The method loops through all the rows, making a string
	 * of the current row and checking for a win. If a row has two
	 * different tokens or a blank spot, the row does not contain
	 * a winning play
	 * @return A boolean stating whether any row contains a winning play
	 */
	private static boolean checkRows() {
		for(int i = 0; i < SIZE; i++) {
			StringBuilder letters = new StringBuilder();
			for(int j = 0; j < SIZE; j++) {
				letters.append(gameBoard[i][j]);
			}
			String row = letters.toString();

			if(!(row.contains("X") && row.contains("O")) && !row.contains("-")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks the columns of the game board to see if the current player
	 * has won. The method loops through all the columns, making a string
	 * of the current column and checking for a win. If a column has two
	 * different tokens or a blank spot, it does not contain a winning play.
	 *
	 * @return A boolean stating whether any column contains a winning play
	 */
	private static boolean checkCols() {
		for(int i = 0; i < SIZE; i++) {
			StringBuilder letters = new StringBuilder();
			for(int j = 0; j < SIZE; j++) {
				letters.append(gameBoard[j][i]);
			}
			String col = letters.toString();

			if(!(col.contains("X") && col.contains("O")) && !col.contains("-")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks the diagonals of the game board to see if the current player
	 * has won. If a diagonal has two different tokens or a blank spot, it
	 * does not contain a winning play.
	 *
	 * @return A boolean stating whether any diagonal contains a winning play
	 */
	private static boolean checkDiagonals() {
		StringBuilder letters = new StringBuilder();

		// Tests for a diagonal win from the top left to bottom right
		for(int i = 0; i < SIZE; i++) {
			letters.append(gameBoard[i][i]);

		}
		String diag = letters.toString();

		if(!(diag.contains("X") && diag.contains("O")) && !diag.contains("-")) {
			return true;
		}


		// Clears the string and checks for a diagonal win
		// from the top right to bottom left
		letters.delete(0, letters.length());
		for(int i = 0, j = SIZE - 1; i < SIZE; i++, j--) {
			letters.append(gameBoard[i][j]);
		}

		diag = letters.toString();

		// If the code makes it past this step, there has not been a winning play
		return (!(diag.contains("X") && diag.contains("O")) && !diag.contains("-"));

	}

	/**
	 * Checks if the game board is full. If the method finds
	 * any available space, it is not full. Otherwise, it's
	 * a draw.
	 *
	 * @return A boolean stating whether there is a draw
	 */
	private static boolean checkForDraw() {
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				if(availablePlaysBoard[i][j]) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Changes the current player by checking whether
	 * a static number is even or odd.
	 *
	 * @return The letter "X" or "O", alternating every time it's called
	 */
	private static char changePlayer() {
		currentPlayer++;
		return currentPlayer % 2 == 0 ? 'X' : 'O';
	}

	/**
	 * Gives instructions to the player on how to play. It
	 * shows a board that is numbered. The player should enter
	 * a number to show where the want to place their token.
	 */
	private static void printInstructions() {
		System.out.println("-- How To Play --");
		System.out.println("Enter a number where you want to place your token");
		System.out.println("The board numbers are laid out in the following way.");

		// Prints the board numbered accordingly given any size board
		for(int i = 0; i < SIZE; i++) {
			System.out.print("| ");
			for(int j = 0; j < SIZE; j++) {
				System.out.print((j + 1) + (SIZE * i) + " | ");
			}
			System.out.println();
		}

		// Prints out newline for easier readability
		System.out.println();
	}

	/**
	 * Initializes the game board and a board of booleans.
	 * The game board is a two-dimensional array filled
	 * with "-" characters. The board of booleans is used
	 * to check if a player can use a certain spot. It is
	 * initialized with all true values.
	 */
	private static void createBoard() {
		gameBoard = new char[SIZE][SIZE];
		availablePlaysBoard = new boolean[SIZE][SIZE];

		// Initializes the game board
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				gameBoard[i][j] = '-';
			}
		}

		// Initializes the board of availability
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				availablePlaysBoard[i][j] = true;
			}
		}
	}

	/** Prints the game board. */
	private static void printBoard() {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				System.out.print(gameBoard[i][j] + " ");
			}
			System.out.println();
		}

	}

	public static class InputRangeException extends Exception {
		public String toString() {
			return "Make sure the number is between 1 and " + (SIZE * SIZE) + ". Try again.";
		}
	}

}