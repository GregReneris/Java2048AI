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
import java.util.Random;

/**
 * Main class creates and then runs the 2048 game and
 * associated search algorithms. Takes in states from an input file and
 * outputs test results to another text file.
 *
 * Uses BFS and assumes that every doubling adds to that rounds "score" and
 * excludes 2's and 0's. When there are multiple doubles in one turn, those
 * doubles will combine to make a higher score. An 8 and a 16 result in a 24, for example.
 *
 * The BFS goes 3 deep, and chooses at each level the board node with the highest
 * score. It checks all options as BFS, but chooses highest to return as a solution.
 */
public class Main {

    private static final int GAME_SIZE = 4;

    private static Random randomGen = new Random();

    public static void main(String[] args) throws Exception {
        System.out.println("\nThe 2048 game will be built, then a few moves calculated by the BFS.\n");
        Board solution = null;
        String results = ""; //initializing as empty string
        String fileDestination = "src\\2048_out.txt";

        //please note that this program was tested on a PC. Mac's can have
        //different file path writing styles. If that is the case and an error occurs
        //please use the absolute path as the value for inputFilePath on line 42.
        Path exists = Paths.get("src\\2048_in.txt");
        String inputFilePath = exists.toAbsolutePath().toString();
        int numberOfTests;

        numberOfTests = getNumberOfTests(inputFilePath);

        System.out.println("We will run " + numberOfTests + " tests.");
        System.out.println("The starting board arrangements shall be printed.");

//      Needed to comment the runTest for BFS out, as line in Board.java was causing a nullptr exception.
//      Something to fix and investigate for later

        for(int i = 0; i < numberOfTests; i++)
        {
            results += runBFSTest(inputFilePath, i);
        }

        System.out.println("\nThe score and move solutions in order are: \n" + results);

        writeResultsToFile(results, fileDestination);

        runLocalSearchAlgorithms();


    }

    private static void runLocalSearchAlgorithms() throws Exception {
        System.out.println("\n\n\n****************");
        System.out.println("Moving on to Local Search Algorithms");
        System.out.println("We will make a new game, new boards, and evaluate starting positions");



        Board solution = null;

        //create the game object.
        Board game = new Board();
        game.makeBoardEmpty();

        //ensuring the board is empty each time.
        game.printBoard();

        game.setDefaultStartState();

        //print game memory location, then board mem.
        System.out.println(game + " gb, starting state " + game.getGameBoard());


        ArrayList<Board> randLocalSearchList = new ArrayList<Board>();

        for(int i = 0; i < 50 ; i++)
        {
            Board gameNode = runRandomLocalSearch(game, 0);
            randLocalSearchList.add(gameNode);
            System.out.println("Got Here");
        }
        int maxIndex = 0;
        int currentScore = 0;
        int maxScore = 0;
        for(int j = 0; j < randLocalSearchList.size(); j++)
        {
            currentScore = randLocalSearchList.get(j).getHighestTileValue();
            if(maxScore < currentScore){
                maxScore = currentScore;
                maxIndex = j;
            }
        }

        String results = addInfoToResults(randLocalSearchList.get(maxIndex));
        System.out.println("The Winning Random Local Search is: " + results);



        //runRandomLocalMaximumSearch(game);

    }


    /**
     *
     */
    private static Board runRandomLocalSearch(Board gameNode, int iterationCounter) throws Exception {
        //create n between 10 and 100. 50 it is!

//        int interationCounter;
        //int numberIterations = 50;
        // each Ex represents the number of empty locations when that move is made.
        // initializing all as 0.
//        int eu = 0;
//        int ed = 0;
//        int el = 0;
//        int er = 0;
//        int maximumOfMoveStates; // according to the algorithm, 2(Eu + Ed+ El + Er) is the state number required.
        //ArrayDeque<Board> work = new ArrayDeque<Board>();
        Board randomPositiveBoard = null; //resets randomPositiveBoard




        ArrayList<Board> work = new ArrayList<Board>();

        //loop through boards N times or until end condition.
        addBoardToListIfValid(work, Board.Direction.RIGHT, gameNode); //switch gameNode and work.
        addBoardToListIfValid(work, Board.Direction.DOWN, gameNode);
        addBoardToListIfValid(work, Board.Direction.LEFT, gameNode);
        addBoardToListIfValid(work, Board.Direction.UP, gameNode);

//        maximumOfMoveStates = 2 * (eu + ed + el + er); //strict interpretation of the given formula.

        //choose random non zero result from work.
        if(work.size() > 0)
        {
            int index = randomGen.nextInt(work.size());
//            gameNode = work.get(index);
            gameNode = runRandomLocalSearch(work.get(index), iterationCounter+1);
        }
        //Randomly Select a non-zero current + future state to follow. Like DFS in some regards, we follow that particular path and return the results.
        //As a local hill solution, this is not necessarily an optimal solution, just a solution derived from the current and potentially
        //random state of the board at the start of this algorithm's calling.

//    String infoResults = addInfoToResults(gameNode);
//    System.out.println("Printing results for 1 random local search " + infoResults);
    return gameNode;

    }

    /**
     * adds the board to the work Deque list, and returns the nextMoveEmptySpace
     * @param work
     * @param move
     * @param gameNode
     * @return
     * @throws Exception
     */
    private static int addBoardToListIfValid(ArrayList<Board> work, Board.Direction move, Board gameNode) throws Exception {
        int currentEmptySpaces;
        int nextMoveEmptySpaces;
        int combinedEmptySpaces;
        Board nextBoard = null;

        currentEmptySpaces = gameNode.calculateAndReturnNumEmptySpaces();

        switch (move) {
            case UP:
                nextBoard = new Board(gameNode, Board.Direction.UP);
                break;

            case RIGHT:
                nextBoard = new Board(gameNode, Board.Direction.RIGHT);
                break;

            case DOWN:
                nextBoard = new Board(gameNode, Board.Direction.DOWN);
                break;

            case LEFT:
                nextBoard = new Board(gameNode, Board.Direction.LEFT);
                break;
            default:
                throw new Exception(); //this will do for now.
        }

        nextMoveEmptySpaces = nextBoard.calculateAndReturnNumEmptySpaces();
        combinedEmptySpaces = currentEmptySpaces + nextMoveEmptySpaces;
        int combinedScore = nextBoard.getScore() + gameNode.getScore();

        //comparing to see if the combined score score is non zero.
        //next game board score - game node score? Score at next board moved forward from previous score,
        //
        if(nextBoard.getScore() > gameNode.getScore())
        {
            work.add(nextBoard);
        }
//        if (combinedEmptySpaces > 0) {
//            work.add(nextBoard);
//        }
        return combinedScore;
    }

    private static void runRandomLocalMaximumSearch(Board gameNode) {
        System.out.println("TBD");
    }



    public static String runBFSTest(String inputFilePath, int skipFourLines) throws FileNotFoundException {
        File file = new File(inputFilePath);
        Scanner inputFile = new Scanner(file);
        inputFile.nextInt();
        Board solution = null;
        String solutionResults;


        //manage skipping
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
    public static void writeResultsToFile(String results, String fileDestination) throws IOException {
        try {
            FileWriter outputWriter = new FileWriter(fileDestination);
            outputWriter.write(results);
            outputWriter.close();
        } catch (IOException error) {
        System.out.println("An error occurred.");
        error.printStackTrace();
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

        //ArrayDeque is a funky class that can work like both a stack and a queue.
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
