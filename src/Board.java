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
        OVER
    }

    private static final int GAME_SIZE = 4; //board game size.

    private final int[][] gameBoard;  //represents 4x4 grid for the game.

    private int depth = 0; //how far into the tree this board is.

    private int score = 0; //default score.
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

        this.score = originalGame.score;
        //round score would be this score - parent score.

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
            case OVER:
                System.out.println("Over in switch statement, no moves remaining");
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

//    /**
//     * Adds a two to the next vertically free space, going left to right, top to bottom.
//     * @return true if there was an empty space and a 2 is added, false if not.
//     */
//    public boolean addNextNumber()
//    {
//        boolean foundEmpty = false;
//
//        for(int row = 0; row < GAME_SIZE; row++)
//        {
//            for(int col = 0; col < GAME_SIZE; col++)
//            {
//                if(gameBoard[row][col] == 0 )
//                {
//                    gameBoard[row][col] = 2;
//                    foundEmpty = true;
//                    return foundEmpty;
//                }
//            }
//        }
//        return foundEmpty;
//    }

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

        System.out.println("Placing a 2 at: " + row + " , " + col);

        row2 = randomGen.nextInt(max-min +1 )+min;
        col2 = randomGen.nextInt(max-min +1 )+min;
        System.out.println("Placing a 2 at: " + row2 + " , " + col2);

        gameBoard[row][col] = numberToPlace;
        gameBoard[row2][col2] = numberToPlace;

        if(row2 == row && col2 == col)
        {
            System.out.println("Reset board overlap occuredm");
            //if match reset
            makeBoardEmpty();
            setDefaultStartState();
        }
    }


    //TODO: Adjust this to randomly choose an open board space.
    /**
     * Adds a two to the next vertically free space, going left to right, top to bottom.
     * @return true if there was an empty space and a 2 is added, false if not.
     */
    public boolean addNextNumber()
    {
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


        //TODO: Choose a random place on the board that is not full.
        //create list of spaces that are labelled 0.

        //modify to add to list a location if it is not zero.
        for(int row = 0; row < GAME_SIZE; row++)
        {
            for(int col = 0; col < GAME_SIZE; col++)
            {
                if(gameBoard[row][col] == 0 )
                {
                    //System.out.println("Gameboard space is: " + gameBoard[row][col]);
                    //openSpaces[row][col] = gameBoard[row][col];

                    //placing a new xyarray to hold coords in the dict list.
                    //a new array required every time.
                    int[] xyarray = new int[2];
                    xyarray[0] = row;
                    xyarray[1] = col;
                    dict.put(counter, xyarray);
                    counter++;
                    System.out.println("xyarray added into dict: " + Arrays.toString(xyarray));

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

//            System.out.println("Dictionary: " + dict.toString());

            rowToPut = xyarray[0];
            colToPut = xyarray[1];

            gameBoard[rowToPut][colToPut] = numberToAdd;
            System.out.println("Adding " +numberToAdd+ " to space " + rowToPut + " , " + colToPut);
        }
        else
        {
            System.out.println("Board is full, game is probably over.");
            fullBoard = true;
            return fullBoard;
        }


        return fullBoard;
    }



    /**
     * imports starting state from 2048_in.txt
     * Helper function for if the starting position needs to be called by the
     * Board class and read in. Currently extraneous after refactoring.
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
                //after first iteration, now on line 6 to start next board state test.
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
        return score;
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



    public void moveRight()
    {
        HashMap<Integer, Integer> origMap = createBoardMap(this);
        int boardFull;

        for(int row = 0; row < GAME_SIZE; row++) {
            for (int col = GAME_SIZE-1; col >= 0; col--) {
                col += findAndMoveTile(row, col, 0,-1, 1);
            }
        }
        movesToGetHere.add(Direction.RIGHT);



        this.gameover = addNextNumber();
        if(this.gameover)
        {
            System.out.println("Game is over, no more moves left");
            //does not create a new map and compares.
            //instead grabs the highest value tile and sets score.
            //this.score = getScore();
            this.score = getHighestTileValue();
        }
        else
        {
            HashMap<Integer, Integer> newMap = createBoardMap(this);
            this.score += compareMaps(origMap, newMap);
        }


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

