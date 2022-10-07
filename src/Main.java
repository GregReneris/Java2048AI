import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
	// write your code here
        System.out.println("The 2048 game will be built, then a few moves run by an AI.");

        //create the game object.
        Game2048 Game = new Game2048();
        Game.makeBoardEmpty();

        //ensuring the board is empty each time.
        Game.printBoard();

        Game.importStaringPosition();
        Game.printBoard();

        Game.moveDown();
        System.out.println("*************");
        Game.printBoard();

    }
}
