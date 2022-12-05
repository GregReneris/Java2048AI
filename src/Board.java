import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Random;


/**
 * The class to run and build the 2048 game.
 */
public class Board {



    enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT,
        NONE
    }

    private static final int GAME_SIZE = 4; //board game size.

    private final int[][] gameBoard;  //represents 4x4 grid for the game.

    private int depth = 0; //how far into the tree this board is.

//    private int score = 0; //default score.

    private int score2 = 0;

    private int gameBoardHighestTile = 0;

    private int emptySpaces; //the number of open spaces remaining on the board.
    public static Random randomGen = new Random();

    private boolean gameover;


    public ArrayList<Direction> movesToGetHere = new ArrayList<Direction>();

    public Board parent = null;

    /**
     * default constructor to init a new game.
     */
    public Board()
    {
        gameBoard = new int[GAME_SIZE][GAME_SIZE];
        newGame();
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

//        this.score = originalGame.score;
        //round score would be this score - parent score.
        this.score2 = originalGame.score2;

        this.gameover = false;

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
        //Try around switch, catch if game-over.
        try
        {
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

                case NONE:
                    break;
            }
//            this.score = this.score;
            this.gameBoardHighestTile = getHighestTileValue();
        }
        catch(Exception GameOverException)
        {
//            this.score = this.score;

            this.gameBoardHighestTile = this.getHighestTileValue();
//            System.out.println("GAME OVER EXCEPTION");
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

    public void setGameTile(int row, int col, int value)
    {
        gameBoard[row][col] = value;
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


    public int getScore2() {
        return score2;
    }



    public void setDefaultStartState()
    {
        int max = 3;
        int min = 0;
        int row;
        int col;
        int row2;
        int col2;
        int numberToPlace = 2;

        row = randomGen.nextInt(max-min +1 )+min;
        col = randomGen.nextInt(max-min +1 )+min;

        row2 = randomGen.nextInt(max-min +1 )+min;
        col2 = randomGen.nextInt(max-min +1 )+min;

        gameBoard[row][col] = numberToPlace;
        gameBoard[row2][col2] = numberToPlace;

        if(row2 == row && col2 == col)
        {
            System.out.println("Board reset - overlap occurred.");
            //if match reset
            makeBoardEmpty();
            setDefaultStartState();
        }
    }

    /**
     * Adds a two to the next vertically free space, going left to right, top to bottom.
     * @return true if there was an empty space and a 2 is added, false if not.
     */
    public void addNextNumber() throws GameOverException {
        int max = 10;
        int min = 1;
        int numberToAdd = -1; //default to -1 to catch errors.
        Dictionary dict = new Hashtable();
        int counter = 0;
        int dictsize;
        int chooseLocation;
        boolean fullBoard = false;


        int generatedRandom  = randomGen.nextInt(max-min +1 )+min;

        // 9/10 times the number is 2, otherwise is 4.
        if(generatedRandom < 2 && generatedRandom > 0) // > 0 to fast fail, or if min ever needs to change to negatives.
        {
            numberToAdd = 2;
        }
        else
        {
            numberToAdd = 4;
        }

        //create list of spaces that are labelled 0.

        //modify to add to list a location if it is not zero.
        for(int row = 0; row < GAME_SIZE; row++)
        {
            for(int col = 0; col < GAME_SIZE; col++)
            {
                if(gameBoard[row][col] == 0 )
                {
                    //placing a new xyarray to hold coords in the dict list.
                    //a new array required every time.
                    int[] xyarray = new int[2];
                    xyarray[0] = row;
                    xyarray[1] = col;
                    dict.put(counter, xyarray);
                    counter++;
                }
            }
        }

        dictsize = dict.size();

        if(dictsize > 0)
        {
            chooseLocation = randomGen.nextInt(dictsize); // bound from 0 to max
            int[] xyarray;
            int rowToPut;
            int colToPut;

            //cast into array.
            xyarray = (int[])dict.get(chooseLocation);
            rowToPut = xyarray[0];
            colToPut = xyarray[1];

            gameBoard[rowToPut][colToPut] = numberToAdd;
        }
        else
        {
            throw new GameOverException();

        }

    }

    /**
     * calculates number of spaces with a value of 0.
     * @return number of spaces on the board with a value of 0.
     */
    public int calculateAndReturnNumEmptySpaces()
    {
        int numEmptySpaces = 0;

        for(int row = 0; row < GAME_SIZE; row++)
        {
            for(int col = 0; col < GAME_SIZE; col++)
            {
                if(gameBoard[row][col] == 0 )
                {
                    numEmptySpaces++;
                }
            }
        }

        this.emptySpaces = numEmptySpaces; //set empty spaces metric for this board.
        return numEmptySpaces;
    }

    /**
     * creates a board list of all possible opponent boards with 2's and 4's in
     * every currently available space.
     * @return
     */
    public ArrayList<Board> getOpponentBoardOptions()
    {
        ArrayList<Board> options = new ArrayList<>();

        for(int row = 0; row < GAME_SIZE; row++) {
            for (int col = 0; col < GAME_SIZE; col++) {
                if(gameBoard[row][col] == 0 )
                {
                    Board copiedBoard;

                    //for 2's.
                    copiedBoard = new Board(this, Direction.NONE);
                    copiedBoard.gameBoard[row][col] = 2;
                    options.add(copiedBoard);

                    //for 4's
                    copiedBoard = new Board(this, Direction.NONE);
                    copiedBoard.gameBoard[row][col] = 4;
                    options.add(copiedBoard);
                }
            }

        }
        return options;
    }


    /**
     * This method will scan the entire board and look for the highest number.
     * This method finds the highest value tile and returns it.
     * @return the highest number found.
     */
    public int getHighestTileValue()
    {
        int currentHigh = 0;
        int readScore;

        for(int row = 0; row < GAME_SIZE; row++)
        {

            for (int col = 0; col < GAME_SIZE; col++)
            {
                readScore = gameBoard[row][col];

                if(readScore > currentHigh)
                    currentHigh = readScore;
            }
        }
        return currentHigh;
    }


    public int getScore()
    {
        //return score;
        return score2;
    }

    public boolean getGameOver()
    {
        return gameover;
    }


    public boolean inBounds(int x, int y)
    {
        return(x >=0 && x < GAME_SIZE && y >= 0 && y < GAME_SIZE);
    }


    /**
     * Finds and moves each tile on the board.
     * @param row
     * @param col
     * @param drow
     * @param dcol
     * @param retry
     * @return
     */
    private int findAndMoveTile(int row, int col,int drow,int dcol, int retry)
    {
        int crow = row+drow;
        int ccol = col+dcol;
        int cell = this.gameBoard[row][col];


        //look for non-empty cell in direction specified.
        while(inBounds(crow,ccol))
        {
            //get current cell to check.
            int ccell = this.gameBoard[crow][ccol];
            if(ccell != 0)
            {
                //checked cell was not empty.
                if(cell == 0)
                {
                    //destination is empty. Put found value into destination.
                    this.gameBoard[row][col] = ccell;
                    this.gameBoard[crow][ccol] = 0;
                    return retry; //Check to see if there are more to move at this location.
                }

                if(ccell == cell)
                {
                    //destination and source value match, add to score and double
                    //the value at destination.
                    this.gameBoard[row][col] = ccell*2;
                    this.gameBoard[crow][ccol] = 0;
                    this.score2 += ccell*2;
                    return 0; //only 1 doubling allowed. Continue to next location.
                }

                //don't move the cell, continue to next location, and if is empty will move cell then.
                return 0;
            }

            //check cell is empty, loop and check next cell in requested direction.
            crow += drow;
            ccol += dcol;
        }

        return 0;
    }

    private int findAndMoveTile2(int row, int col,int drow,int dcol, int retry)
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



                return (this.gameBoard[row][col] == v ? retry : 0);
            }
            //if we did not find a match and move.
            crow += drow;
            ccol += dcol;
        }

        return 0;
    }


    /**
     * Creates a hashmap of the board with key value pairs of number and values.
     * @param gameBoard the board.
     * @return the hashmap
     */
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

        return roundScore;
    }



    public void moveRight() throws GameOverException {
        int boardFull;

        for(int row = 0; row < GAME_SIZE; row++) {
            for (int col = GAME_SIZE-1; col >= 0; col--) {
                col += findAndMoveTile(row, col, 0,-1, 1);
            }
        }
        movesToGetHere.add(Direction.RIGHT);

    }

    public void moveLeft() throws GameOverException {

        for(int row = 0; row < GAME_SIZE; row++) {
            for (int col = 0; col < GAME_SIZE; col++) {
                col += findAndMoveTile(row, col, 0,1, -1);
            }
        }
        movesToGetHere.add(Direction.LEFT);

    }

    public void moveDown() throws GameOverException {

        for(int col = 0; col < GAME_SIZE; col++) {
            for (int row = GAME_SIZE-1; row >= 0; row--) {
                row += findAndMoveTile(row, col, -1,0, 1);
            }
        }
        movesToGetHere.add(Direction.DOWN);
    }



    public void moveUp() throws GameOverException {

        for(int col = 0; col < GAME_SIZE; col++) {
            for (int row = 0; row < GAME_SIZE; row++) {
                row += findAndMoveTile(row, col, 1,0, -1);
            }
        }
        movesToGetHere.add(Direction.UP);

    }


    public boolean isEqual(Board rh)
    {
        for(int col = 0; col < GAME_SIZE; col++) {
            for (int row = 0; row < GAME_SIZE; row++) {
                if (gameBoard[col][row] != rh.gameBoard[col][row]) {
                    return false;
                }
            }
        }
        return true;
    }


}

