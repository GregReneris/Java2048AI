/**
 * A program that creates and runs version of 2048.
 * Coded in Java (using version 18).
 * Uses algorithms to determine optimal path for the highest score.
 *
 */
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.io.File;


/**
 * Main class creates and then runs the 2048 game and
 * associated search algorithms. Takes in states from an input file and
 * also outputs it to another text file.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("The 2048 game will be built, then a few moves run by an AI.");
        Board solution = null;
        String results;

        //create the game object.
        Board game = new Board();
        game.makeBoardEmpty();

        //ensuring the board is empty each time.
        game.printBoard();

        System.out.println(game + " gb " + game.getGameBoard());


        game.importStaringPosition();
        game.printBoard();

//        game.moveRight();
//        game.moveUp();
//        game.moveUp();
//
//        game.printBoard();


        solution = breadthFirstSearch(game);

        System.out.println("Solution board: ");
        solution.printBoard();
        System.out.println(solution.movesToGetHere);
        System.out.println("Solution score" + solution.getScore());

        results = addInfoToResults(solution);

        writeResultsToFile(results);

    }

    private static String addInfoToResults(Board solution) {
        String results ="";

        results = solution.getScore() + " , ";
        ArrayList enumArray = solution.movesToGetHere;

        //For some odd reason, the switch statement just wouldn't compile happily.
        for (Object e : enumArray) {
            if (Board.Direction.UP.equals(e)) {
                results += "U";
            } else if (Board.Direction.RIGHT.equals(e)) {
                results += "R";
            } else if (Board.Direction.DOWN.equals(e)) {
                results += "D";
            } else if (Board.Direction.LEFT.equals(e)) {
                results += "L";
            }
            results +=",";
        }

        //pull off dangling ','
        results = results.substring(0, results.length()-1);
        //add a new line at the end.
        results += "\n";

        return results;
    }

    /**
     * writes new data to the file each time, output file is overwritten
     * each time the program is run.
     * @throws IOException
     */
    public static void writeResultsToFile(String results) throws IOException {
        try {
            FileWriter writer = new FileWriter("src\\2048_out.txt");
            writer.write(results);
            writer.close();
        } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
        }

    }


    public static Board breadthFirstSearch(Board gameNode)
    {
        Board bestSolution = gameNode;
        int counter = 0;

        //a funky class that can work like both a stack and a queue.
        //use .add and .remove for queue functions.
        ArrayDeque<Board> work = new ArrayDeque<Board>();
        work.add(gameNode);

        while(!work.isEmpty())
        {
            gameNode = work.remove(); //pop first Board off.
            if(gameNode.getScore() > bestSolution.getScore())
            {
                bestSolution = gameNode;
            }
            if(gameNode.getDepth() < 3 )
            {
                work.add(new Board(gameNode, Board.Direction.UP));
                work.add(new Board(gameNode, Board.Direction.RIGHT));
                work.add(new Board(gameNode, Board.Direction.DOWN));
                work.add(new Board(gameNode, Board.Direction.LEFT));
            }
            else{
            }
            counter++;

        }
        //System.out.println(counter);
        return bestSolution;
    }




}
