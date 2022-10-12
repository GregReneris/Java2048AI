/**
 * A program that creates and runs version of 2048.
 * Coded in Java (using version 18).
 * Uses algorithms to determine optimal path for the highest score.
 *
 */
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.File;


/**
 * Main class creates and then runs the 2048 game and
 * associated search algorithms. Takes in states from an input file and
 * outputs test results to another text file.
 *
 * Uses BFS and assumes that every doubling adds to that rounds "score", and
 * excludes 2's and 0's.
 *
 * The BFS goes 3 deep, and chooses at each level the board node with the highest
 * score. It checks all options as BFS, but chooses for a solution.
 */
public class Main {

    private static final int GAME_SIZE = 4;

    public static void main(String[] args) throws IOException {
        System.out.println("The 2048 game will be built, then a few moves run by an AI.");
        Board solution = null;
        String results = ""; //initializing as empty string
        Path exists = Paths.get("src\\2048_in.txt");
        String inputFilePath = exists.toAbsolutePath().toString();
        int numberOfTests;


        numberOfTests = getNumberOfTests(inputFilePath);

        for(int i = 0; i < numberOfTests; i++)
        {
            results += runTest(inputFilePath, i);
        }

        writeResultsToFile(results);

        //create the game object.
//        Board game = new Board();
//        game.makeBoardEmpty();
//
//        //ensuring the board is empty each time.
//        game.printBoard();
//
//        System.out.println(game + " gb " + game.getGameBoard());
//
//
//        game.importStaringPosition();
//        game.printBoard();
//
//
//        solution = breadthFirstSearch(game);
//
//        System.out.println("Solution board: ");
//        solution.printBoard();
//        System.out.println(solution.movesToGetHere);
//        System.out.println("Solution score" + solution.getScore());

    }

    public static String runTest(String inputFilePath, int skipFourLines) throws FileNotFoundException {
        File file = new File(inputFilePath);
        Scanner inputFile = new Scanner(file);
        inputFile.nextInt();
        Board solution = null;
        String solutionResults;


        //I'm tired and this is a ridiculous way to manage skipping
        //4 lines per existing test.
        while(skipFourLines > 0)
        {
            for(int y = 0; y < 4 ; y++)
            {
                inputFile.next();
            }
            skipFourLines--;
        }

        //create new board for each new test.
        Board game = new Board();
        game.makeBoardEmpty();

        //sets the game board.
        for (int row = 0; row < GAME_SIZE; row++) {
            //after first iteration, now on line 6 to start next board state test.
            String newInput = inputFile.next();
            String[] split = newInput.split(",", 4);

            for (int col = 0; col < GAME_SIZE; col++) {
                //game.gameBoard[row][col] = Integer.parseInt(split[col]);
                game.setGameTile(row, col, Integer.parseInt(split[col]));
            }
        }
        //print the starting board for verification.
        game.printBoard();

        solution = breadthFirstSearch(game);
        solutionResults = addInfoToResults(solution);

        return solutionResults;
    }

    public static int getNumberOfTests(String inputFilePath) throws FileNotFoundException {
        File file = new File(inputFilePath);
        Scanner inputFile = new Scanner(file);
        int numberTests = inputFile.nextInt();
        return numberTests;
    }

    private static String addInfoToResults(Board solution) {
        String results ="";

        results = solution.getScore() + ",";
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


    /**
     * Executes BFS on the passed in game object.
     * @param gameNode the game to search
     * @return the board solution.
     */
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
