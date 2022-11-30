/**
 * A program that creates and runs version of 2048.
 * Coded in Java (using version 18).
 * Uses algorithms to determine optimal path for the highest score.
 *
 */
import java.io.FileWriter;
import java.io.IOException;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.Random;

/**
 * Main class creates and then runs the 2048 game and
 * associated search algorithms.
 *
 * Uses Local and max search and assumes that every doubling adds to that rounds "score" and
 * excludes 2's and 0's. When there are multiple doubles in one turn, those
 * doubles will combine to make a higher score. An 8 and a 16 result in a 24, for example.
 *
 * The searches go 50 and 25 itersations, and chooses at each level the board node
 * randomly or with the highest score. Ties are randomly decided.
 */
public class Main {

    private static final int GAME_SIZE = 4;

    private static Random randomGen = new Random();

    private static boolean maxFlag = false;

    private static int DEPTH = 2;

    public static void main(String[] args) throws Exception {
        System.out.println("\nThe 2048 game will be built, then will run two local search algorithms. \n");
        Board solution = null;


        //runLocalSearchAlgorithms();

        //pass in depth 0.
        runMinMaxSearch();

    }

    private static void runLocalSearchAlgorithms() throws Exception {
        System.out.println("\n\n\n****************");
        System.out.println("Moving on to Local Search Algorithms");
        System.out.println("We will make a new game, new board, and evaluate starting positions.");
        Board solution = null;

        //create the game object.
        Board game = new Board();
        game.makeBoardEmpty();
        //ensuring the board is empty each time.
        game.setDefaultStartState();
        //Show start state of board
        game.printBoard();
        ArrayList<Board> randLocalSearchList = new ArrayList<Board>();

        searchLocalSearchForWinner(game, randLocalSearchList, "Random");


        //now for local maximum search
        //create the game object.
        game = new Board();
        game.makeBoardEmpty();
        System.out.println("We will make a new game, new board, and evaluate starting positions.");
        //ensuring the board is empty each time.
        game.setDefaultStartState();
        game.printBoard();
        //make a new ArrayList as well.
        randLocalSearchList = new ArrayList<Board>();

        maxFlag = true;
        searchLocalSearchForWinner(game, randLocalSearchList, "Maximum");


    }

    private static void searchLocalSearchForWinner(Board game, ArrayList<Board> randLocalSearchList, String searchType) throws Exception {
        Board solution = null;
        int numberIterations = 50; //set iterations for local search

        if (maxFlag)
            numberIterations = 25; //desired iterations for max local search.

        for(int i = 0; i < numberIterations ; i++)
        {
            Board gameNode = runRandomLocalSearch(game, 0);
            randLocalSearchList.add(gameNode);
            //System.out.println("Got Here");
        }
        int maxIndex = 0;
        int currentScore = 0;
        int maxScore = 0;
        for(int j = 0; j < randLocalSearchList.size(); j++)
        {
            currentScore = randLocalSearchList.get(j).getScore();
            if(maxScore < currentScore){
                maxScore = currentScore;
                maxIndex = j;
            }
        }

        String results = addInfoToResults(randLocalSearchList.get(maxIndex));
        solution = randLocalSearchList.get(maxIndex);
        System.out.println("The Winning " + searchType + " Local Search is: " + results +"\n");
        solution.printBoard();
        System.out.println("\n");
    }


    private static void runMinMaxSearch() throws Exception {
        //create starting board.
        Board gameBoard = new Board();
        gameBoard.setDefaultStartState();

        //todo: try catch the for loop.

        for(;;){
            int moveIndex = gameBoard.movesToGetHere.size();
            Board board = minMaxSearch(gameBoard, 0);
            if(moveIndex == board.movesToGetHere.size())
            {
                board = minMaxSearch(gameBoard, 0);
                System.out.println(board);
            }
            // todo: check for null board here.
            // apply the move to gameboard to go in direction of board.
            gameBoard = new Board(gameBoard, board.movesToGetHere.get(moveIndex));
            gameBoard.addNextNumber(); //bringing back the champ, as it moved out of moves.

        }


    }

    private static Board minMaxSearch(Board gameNode, int depth) throws Exception
    {
        //get the max best move.
        gameNode = getMinMaxMove(gameNode, true); //starts with max

        if(depth < DEPTH)
        {
            //test every possible combination the opponent might pick and pick the min.
            ArrayList<Board> opOptions = gameNode.getOpponentBoardOptions();
            if(opOptions.size()>0)
            {

                for(Board op : opOptions)
                {
                    //get min score
                    Board ans = getMinMaxMove(op, false);//returns lowest scoring board from op
                    //null ptr on line 154 when there is no answer as board is full.

                    if( ans.getScore() <= gameNode.getScore()) {
                        gameNode = ans;
                    }
                }

                gameNode = minMaxSearch(gameNode, depth+1);
            }

        }

        return gameNode;
    }

    private static Board getMinMaxMove(Board gameNode, boolean minMax ) throws Exception
    {
        Board picked = null;



        ArrayList<Board> work = new ArrayList<>();

        //start with list of four.
        addBoardToListIfValid(work, Board.Direction.RIGHT, gameNode);
        addBoardToListIfValid(work, Board.Direction.DOWN, gameNode);
        addBoardToListIfValid(work, Board.Direction.LEFT, gameNode);
        addBoardToListIfValid(work, Board.Direction.UP, gameNode);

        //based on min max, pick the next one with the highest of lowest score.
        //remove worksize if.
        if (work.size() > 0) {
            picked = work.get(0);

            for (int i = 1; i < work.size(); i++) {
                if (minMax) {
                    //we want the max
                    if (work.get(i).getScore() > picked.getScore())
                        picked = work.get(i);
                } else {
                    //want the min
                    if (work.get(i).getScore() < picked.getScore())
                        picked = work.get(i);
                }
            }
        }

        if(picked!= null)
        {
            return picked;
        }
        return gameNode;
    }


    /**
     *
     */
    private static Board runRandomLocalSearch(Board gameNode, int iterationCounter) throws Exception {
        Board randomPositiveBoard = null; //resets randomPositiveBoard
        boolean maxOrMin = true; //maximizer is true, minimizer is false

        ArrayList<Board> work = new ArrayList<Board>();

        //loop through boards N times or until end condition.
        addBoardToListIfValid(work, Board.Direction.RIGHT, gameNode);
        addBoardToListIfValid(work, Board.Direction.DOWN, gameNode);
        addBoardToListIfValid(work, Board.Direction.LEFT, gameNode);
        addBoardToListIfValid(work, Board.Direction.UP, gameNode);


        //if we have valid boards.
        if(work.size() > 0)
        {
            //this is for the maximum local random search. True results in max search, false random search.
            if(maxFlag)
            {
                int bestScore = work.get(0).getScore();
                ArrayList<Board> workmax = new ArrayList<>();



                //for each board in work, compare against best so far.
                for(Board x : work)
                {
                    //if new score is higher, choose new score to follow the path of.
                    if(x.getScore() > bestScore)
                    {
                        bestScore = x.getScore();
                        workmax.clear();
                    }
                    //if there is a tie, add both to the workmax ArrayList.
                    if(x.getScore() == bestScore)
                    {
                        workmax.add(x);
                    }
                }
                work = workmax;
            }


            //Randomly Select a non-zero current + future state to follow. Like DFS in some regards,
            //we follow that particular path and return the results.
            //As a local hill solution, this is not necessarily an optimal solution,
            //just a solution derived from the current and potentially
            //random state of the board at the start of this algorithm's calling.
            int index = randomGen.nextInt(work.size());
//            gameNode = work.get(index);
            


            gameNode = runRandomLocalSearch(work.get(index), iterationCounter+1);
        }


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
    private static void addBoardToListIfValid(ArrayList<Board> work, Board.Direction move, Board gameNode) throws Exception {
        Board nextBoard = null;

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
                throw new Exception();
        }
            //gameNode.addChild(nextBoard);
            if(nextBoard.calculateAndReturnNumEmptySpaces() > 0)
            {
                work.add(nextBoard);
            }


    }


    private static String addInfoToResults(Board solution) {
        String results ="";

        results += "\nCumulative score : ";
        results += solution.getScore() + "\nMoves to get here: ";
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
        results += "\nHighest Tile Value: ";
        results += solution.getHighestTileValue();

        return results;
    }
}
