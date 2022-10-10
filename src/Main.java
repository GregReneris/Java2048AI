/**
 * A program that creates and runs version of 2048.
 * Coded in Java (using version 18).
 * Uses algorithms to determine optimal path for the highest score.
 *
 */

import java.io.FileNotFoundException;

/**
 * Main class creates and then runs the 2048 game and
 * associated search algorithms. Takes in states from an input file and
 * also outputs it to another text file.
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException {
	// write your code here
        System.out.println("The 2048 game will be built, then a few moves run by an AI.");

        //create the game object.
        Board game = new Board();
        game.makeBoardEmpty();

        //ensuring the board is empty each time.
        game.printBoard();

        System.out.println(game + " gb " + game.getGameBoard());


        game.importStaringPosition();
        game.printBoard();

        game.moveDown();
        System.out.println("*************");
        game.printBoard();

        //just pass in game.
        Board game2 =  new Board(game);

        System.out.println(game + " gb " + game.getGameBoard() [0]);
        System.out.println(game2 + " gb " + game2.getGameBoard() [0]);

        game2.moveRight();

        System.out.println("Printing game 2");
        game2.printBoard();
        game.printBoard();


//        System.out.println("222222222");
//        game.addNextNumber();
//        game.printBoard();
//
//        game.moveLeft();
//        game.addNextNumber();
//        System.out.println("3333333: CS : " + game.getCurrentScore());
//        game.printBoard();
//
//
//        game.moveUp();
//        game.addNextNumber();
//
//        System.out.println("444444: CS : " + game.getCurrentScore());
//        game.printBoard();
//
//        game.moveLeft();
//        game.addNextNumber();
//        System.out.println("555555: CS : " + game.getCurrentScore());
//        game.printBoard();




    }



    public static String breadthFirstSearch()
    {

        return "Solution or failure.";
    }




}
