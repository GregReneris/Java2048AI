import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
	// write your code here
        System.out.println("The 2048 game will be built, then a few moves run by an AI.");

        //create the game object.
        Board Game = new Board();
        Game.makeBoardEmpty();

        //ensuring the board is empty each time.
        Game.printBoard();

        System.out.println(Game + " gb " + Game.getGameBoard());


        Game.importStaringPosition();
        Game.printBoard();

        Game.moveDown();
        System.out.println("*************");
        Game.printBoard();

        Board Game2 =  new Board(Game.getDepth(), Game.getGameBoard());

        System.out.println(Game + " gb " + Game.getGameBoard());
        System.out.println(Game2 + " gb " + Game2.getGameBoard());

        Game2.moveRight();

        System.out.println("Printing game 2");
        Game2.printBoard();
        Game.printBoard();


//        System.out.println("222222222");
//        Game.addNextNumber();
//        Game.printBoard();
//
//        Game.moveLeft();
//        Game.addNextNumber();
//        System.out.println("3333333: CS : " + Game.getCurrentScore());
//        Game.printBoard();
//
//
//        Game.moveUp();
//        Game.addNextNumber();
//
//        System.out.println("444444: CS : " + Game.getCurrentScore());
//        Game.printBoard();
//
//        Game.moveLeft();
//        Game.addNextNumber();
//        System.out.println("555555: CS : " + Game.getCurrentScore());
//        Game.printBoard();




    }



    public static String breadthFirstSearch()
    {

        return "Solution or failure.";
    }




}
