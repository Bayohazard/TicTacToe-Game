import java.util.Scanner;

public class Main {
  
  
  public static void main(String[] args) {
    Scanner keyboard = new Scanner(System.in);
    TicTacToe ticTacToe = new TicTacToe();
  
    System.out.println("Press R to play again. Press anything else to exit");
  
    try {
      String input = keyboard.nextLine();
      char letter = input.charAt(0);
    
      if(letter == 'R' || letter == 'r') {
        main(args);
      }
      // Runs if the user does not enter a string
      // Counts as wanting to end the program
    } catch(StringIndexOutOfBoundsException ignore) {
    
    }
    System.out.println("Goodbye.");
  }
  
}
