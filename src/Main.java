/**
 * A program that creates and runs version of 2048.
 * Coded in Java (using version 18).
 * Uses algorithms to determine optimal path for the highest score.
 *
 */

import java.util.*;
import java.util.Random;

/**
 * Main class creates and then runs the 2048 game and
 * associated search algorithms.
 *
 * Uses minimax algorithm to recursively evaluate options for player and opponent,
 * in the aim to get a high score. This is a two part process, where player move increments depth
 * and then the opponent evaluates the next move up to DEPTH.
 *
 * Then the game makes a real move and the algorithm starts from that location again.
 */
public class Main {

    private static final int GAME_SIZE = 4;

    private static Random randomGen = new Random();

    private static boolean maxFlag = false;

    //Depth should be odd numbers because the opponent moves does not create a score.
    //Works when depth is even, but will get same answer for 1 less than even value.
    private static int DEPTH = 5;

    public static void main(String[] args) throws Exception {
        System.out.println("\nThe 2048 game will be built, then will run minimax algorithms. \n");

        runMinMaxSearch();

    }


    /**
     * runMinMaxSearch runs an instance of hte min max search, creates a default board,
     * runs it through the minimax algorithm, and returns the result.
     * Starting Depths should always be zero.
     * @throws Exception
     */
    private static void runMinMaxSearch() throws Exception {
        //create starting board.
        Board gameBoard = new Board();
        gameBoard.setDefaultStartState();
        boolean movesLeft = true;
        String resultsString = "";
        long start =  System.currentTimeMillis();
        long end =  0;
        long resultTime = 0;
        int startingDepth = 0;

        while(movesLeft){
            int moveIndex = gameBoard.movesToGetHere.size();
            Board board = minimax(gameBoard, startingDepth, true);

            // apply the move to gameboard to go in direction of board.
            if(moveIndex != board.movesToGetHere.size())
            {
                gameBoard = new Board(gameBoard, board.movesToGetHere.get(moveIndex));
                gameBoard.addNextNumber(); //bringing back the champ, as it moved out of moves.

            }
            else{
                movesLeft = false;
                //board.printBoard();
                resultsString += addInfoToResults(board);
                end = System.currentTimeMillis();
                resultTime = end - start;
                System.out.println("Winner is: "  + resultsString);
                System.out.println("This search took: " + resultTime + " miliseconds or " + resultTime/1000 + " second(s).");


            }
        }
    }

    /**
     * This algorithm is an implementation of the minimax algo.
     * Goes depth >= DEPTH recursions in. This is because we start at 0 depth
     * and depth increments showing the board state is further in.
     * @param board The initial board
     * @param depth The current depth
     * @param minMax boolean true for max false for min
     * @return the resulting board found through max or min.
     * @throws Exception
     */
    private static Board minimax(Board board, int depth, boolean minMax) throws Exception {
        Board result = null;
        ArrayList<Board> children = new ArrayList<>();

        //only support odd depths. Due to the 2 step process.
        if (depth >= DEPTH )
        {
            return board;
        }


        if(minMax)
        {
            //get 4 directions
            addBoardToListIfValid(children, Board.Direction.RIGHT, board);
            addBoardToListIfValid(children, Board.Direction.LEFT, board);
            addBoardToListIfValid(children, Board.Direction.DOWN, board);
            addBoardToListIfValid(children, Board.Direction.UP, board);

            //for every child board, find max.
            for(Board child : children)
            {
                Board r = minimax(child, depth+1, false);
                if(result == null || r.getScore() > result.getScore())
                {
                    result = r;
                }
            }
        }
        else
        {
            //for every empty space in the board, find all potential options
            //with getOpponentBoardOptions.
            children = board.getOpponentBoardOptions();
            for(Board child : children)
            {
                //then find the options for the player based on the opponentBoardOptions.
                Board r = minimax(child, depth+1, true);
                //chose the lowest score possible out of the set.
                if(result == null || r.getScore() < result.getScore())
                {
                    result = r;
                }
            }
        }

        //if there were no more moves available to the 'player' result is null.
        if (result == null)
        {
            result = board;
        }
        return result;
    }

    /**
     * adds the board to the work Deque list, and returns the nextMoveEmptySpace
     * @param work ArrayList of boards to add to.
     * @param move direction the game moves the tiles in.
     * @param gameNode input game node state.
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
        if(nextBoard.calculateAndReturnNumEmptySpaces() > 0)
        {
            if(!gameNode.isEqual(nextBoard)) //makes sure that invalid moves aren't counted.
            {
                work.add(nextBoard);
            }
        }


    }


    /**
     * Helper method that parses the board information and adds it to a formatted string.
     * @param solution board to add to results string.
     * @return the formatted string of board results for printing elsewhere.
     */
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
        results += "\nLength of moves to get here: ";
        results += enumArray.size();

        return results;
    }
}
