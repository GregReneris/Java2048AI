import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The class to run and build the 2048 game.
 */
public class Board {


    enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT
    }

    //BFS page 87 of handout

    private static final int GAME_SIZE = 4;

    private final int[][] gameBoard;  //represents 4x4 grid for the game.

    private int depth = 0; //also known as the cost.

    private int score = 0;


    public ArrayList<Direction> movesToGetHere = new ArrayList<Direction>();

    public Board parent = null;

    /**
     * default constructor to init a new game.
     */
    public Board()
    {
        gameBoard = new int[GAME_SIZE][GAME_SIZE];

        newGame();

        System.out.println("Welcome to 2048");
    }

    /**
     * constructor to handle making a new copy,
     * The copy should have a depth of +1.
     * @param originalGame
     */
    public Board(Board originalGame, Direction move)
    {
        this.depth = originalGame.depth+1;

        this.gameBoard = new int[GAME_SIZE][GAME_SIZE];

        this.movesToGetHere = (ArrayList<Direction>) originalGame.movesToGetHere.clone();

        this.parent = originalGame;

        this.score = originalGame.score;
        //round score would be this score - parent score.

        int[][] gameBoardOrig = originalGame.getGameBoard();

        //deep copy the board.
        for (int row = 0; row < gameBoard.length; row++) {
            //prints out the boxes and X and O
            // manual array copy.
            for (int col = 0; col < gameBoard[row].length; col++) {
                this.gameBoard[row][col] = gameBoardOrig[row][col];
            }
        }
        //newGame(); //causes all boards to reset.

        switch (move){
            case UP:
                this.moveUp();
                break;

            case RIGHT:
                this.moveRight();
                break;


            case DOWN:
                this.moveDown();
                break;

            case LEFT:
                this.moveLeft();
                break;


        }


    }


    public void newGame()
    {
        makeBoardEmpty();
    }

    /**
     * fills the board with empty strings when called.
     */
    public void makeBoardEmpty()
    {
        //making the board null.
        for(int i = 0; i < gameBoard.length; i++)
        {
            for(int j = 0; j < gameBoard.length; j++)
            {
                gameBoard[i][j] = 0;
            }
        }
    }

    /**
     * prints out the board
     */
    public void printBoard() {

        System.out.print(" ");
        for (int i = 0; i < this.gameBoard.length; i++) {
            System.out.print("_______");
        }
        System.out.print("\n");
        //prints out the rest of the Game Board
        for (int row = 0; row < gameBoard.length; row++) {
            //prints out the boxes and X and O
            System.out.print("|");
            for (int col = 0; col < gameBoard[row].length; col++) {
                System.out.printf(" %4s |", gameBoard[row][col]);
            }
            System.out.print("\n");
            //prints row dividers
            String divider = "";
            for (int k = 0; k < gameBoard.length; k++) {
                divider += "_______";
            }
            System.out.printf(" %s\n", divider);
        }
    }

    public int getDepth() {
        return depth;
    }

    public int[][] getGameBoard() {
        return gameBoard;
    }

    /**
     * Adds a two to the next vertically free space, going left to right, top to bottom.
     * @return true if there was an empty space and a 2 is added, false if not.
     */
    public boolean addNextNumber()
    {
        boolean foundEmpty = false;

        for(int row = 0; row < GAME_SIZE; row++)
        {
            for(int col = 0; col < GAME_SIZE; col++)
            {
                if(gameBoard[row][col] == 0 )
                {
                    gameBoard[row][col] = 2;
                    foundEmpty = true;
                    return foundEmpty;
                }
            }
        }
        return foundEmpty;
    }

    /**
     * imports starting state from 2048_in.txt
     */
    public void importStaringPosition() throws FileNotFoundException {
//        final String FILENAME = "C:\\Users\\greg\\IdeaProjects\\Java2048AI\\src\\2048_in.txt";

        //to deal with different IDEs and relative paths
        Path exists = Paths.get("src\\2048_in.txt");
        String FILENAME2 = exists.toAbsolutePath().toString();

        //File file = new File(FILENAME);
        File file = new File(FILENAME2);
        Scanner inputFile = new Scanner(file);
        int numberOfTests;
        int numberTests = 0;
        int col = 0;

        numberTests = inputFile.nextInt();
        System.out.println("We will run the following number of tests: " + numberTests + ".");



        for(int testNumber = 0; testNumber < numberTests; testNumber++ )
        {

            for(int row = 0; row < GAME_SIZE; row++)
            {
                String newInput = inputFile.next();
                String[] split = newInput.split(",", 4);

                for (col = 0; col < GAME_SIZE; col++)
                {
                    gameBoard[row][col] = Integer.parseInt(split[col]);
                }
            }
        }
    }

    /**
     * This method will scan the entire board and look for the highest number
     * @return the highest number found.
     */
//    public int getCurrentScore()
//    {
//        int currentHigh = 0;
//        int readScore;
//
//        for(int row = 0; row < GAME_SIZE; row++)
//        {
//
//            for (int col = 0; col < GAME_SIZE; col++)
//            {
//                readScore = gameBoard[row][col];
//
//                if(readScore > currentHigh)
//                    currentHigh = readScore;
//            }
//        }
//        this.score = currentHigh;
//        return currentHigh;
//    }


    public int getScore()
    {
        return score;
    }


    public boolean inBounds(int x, int y)
    {
        return(x >=0 && x < GAME_SIZE && y >= 0 && y < GAME_SIZE);
    }


    private int findAndMoveTile(int row, int col,int drow,int dcol, int retry)
    {
        int crow = row+drow;
        int ccol = col+dcol;

        while(inBounds(crow,ccol))
        {
            if(this.gameBoard[crow][ccol] != 0)
            {
                int v = this.gameBoard[crow][ccol];
                if(this.gameBoard[crow][ccol] == this.gameBoard[row][col] || this.gameBoard[row][col] == 0)
                {
                    this.gameBoard[row][col] += this.gameBoard[crow][ccol];
                    this.gameBoard[crow][ccol] = 0;
                }

                //the move's score.
                //Not sure if the score should account for the highest combo, or all combos in the move.
                //it is also adding every moved piece.

//                if(gameBoard[row][col] != v)
//                //if(gameBoard[row][col] != v && gameBoard[row][col] == gameBoard[row][col]*2)
//                {
//                    score += gameBoard[row][col];
//                }

                return (this.gameBoard[row][col] == v ? retry : 0);
            }
            //if we did not find a match and move.
            crow += drow;
            ccol += dcol;
        }

        return 0;
    }

    //pretty much need a new function to see if any spaces on the board are
    //greater than 2

    //OR

    // make a list of the numbers and counts of them on the board.
    //if there are any increases in value number count, add that to the score.
    //ignore 2's and 0's.
    public HashMap<Integer, Integer> createBoardMap(Board gameBoard)
    {
        HashMap<Integer, Integer> boardMap = new HashMap<Integer, Integer>();
        int numberInput = 0;

        for(int row = 0; row < GAME_SIZE; row++)
        {
            for(int col = 0; col < GAME_SIZE; col++)
            {
                numberInput = this.gameBoard[row][col];
                boardMap.merge(numberInput, 1, Integer::sum);
            }
        }
        return boardMap;
    }

    /**
     * A method that compares the key value pairs of everything in the two grids.
     * If a key(a number > 2) gains an additional value greater than what was there before,
     * that adds to that rounds' score.
     * @param origMap orignal map
     * @param newMap new map after the move operation.
     * @return the score for the round.
     */
    private int compareMaps(HashMap<Integer, Integer> origMap, HashMap<Integer, Integer> newMap)
    {
        int roundScore = 0;
        Integer keyValue;
        int difference;

        for (Map.Entry<Integer, Integer> entry : newMap.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();

            //if the newMap has a key original map does not.
            if((!origMap.containsKey(key) && newMap.containsKey(key)))
            {
                difference = newMap.get(key); // for consistency.
                roundScore += (key * difference);
            }

            if ((origMap.containsKey(key) && key > 2)) {

                if (newMap.get(key) > origMap.get(key)) {

                    difference = newMap.get(key) - origMap.get(key);

                    roundScore += (key * difference);
                }
            }
        }

        System.out.println(this.depth);
        System.out.println("Round score is: " + roundScore + " on board depth " + this.getDepth() + "." );
        return roundScore;
    }


    /**
     * ADDING COMPARE MAPS TO MANAGE SCORE AND =+ them!
     */

    public void moveRight()
    {
        HashMap<Integer, Integer> origMap = createBoardMap(this);


        for(int row = 0; row < GAME_SIZE; row++) {
            for (int col = GAME_SIZE-1; col >= 0; col--) {
                col += findAndMoveTile(row, col, 0,-1, 1);
            }
        }
        movesToGetHere.add(Direction.RIGHT);
        addNextNumber();


        HashMap<Integer, Integer> newMap = createBoardMap(this);
        this.score += compareMaps(origMap, newMap);
    }


    public void moveLeft()
    {
        HashMap<Integer, Integer> origMap = createBoardMap(this);

        for(int row = 0; row < GAME_SIZE; row++) {
            for (int col = 0; col < GAME_SIZE; col++) {
                col += findAndMoveTile(row, col, 0,1, -1);
            }
        }
        movesToGetHere.add(Direction.LEFT);
        addNextNumber();

        HashMap<Integer, Integer> newMap = createBoardMap(this);
        this.score += compareMaps(origMap, newMap);
    }

    public void moveDown()
    {

        HashMap<Integer, Integer> origMap = createBoardMap(this);

        for(int col = 0; col < GAME_SIZE; col++) {
            for (int row = GAME_SIZE-1; row >= 0; row--) {
                row += findAndMoveTile(row, col, -1,0, 1);
            }
        }
        movesToGetHere.add(Direction.DOWN);
        addNextNumber();


        HashMap<Integer, Integer> newMap = createBoardMap(this);
        this.score += compareMaps(origMap, newMap);
    }



    public void moveUp()
    {
        HashMap<Integer, Integer> origMap = createBoardMap(this);

        for(int col = 0; col < GAME_SIZE; col++) {
            for (int row = 0; row < GAME_SIZE; row++) {
                row += findAndMoveTile(row, col, 1,0, -1);
            }
        }
        movesToGetHere.add(Direction.UP);
        addNextNumber();

        HashMap<Integer, Integer> newMap = createBoardMap(this);
        this.score += compareMaps(origMap, newMap);

    }




}

