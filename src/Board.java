import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Math;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    private Random randomGen = new Random();

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
     * Places two 2's on the board for game start.
     */
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
        int[][] openSpaces = new int[GAME_SIZE][GAME_SIZE];
        Dictionary dict = new Hashtable();
        int counter = 0;
        int dictsize;
        int chooseLocation;


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

        boolean foundEmpty = false;

        //TODO: Choose a random place on the board that is not full.
        //create list of spaces that are labelled 0.

        //modify to add to list a location if it is not zero.
        for(int row = 0; row < GAME_SIZE; row++)
        {
            for(int col = 0; col < GAME_SIZE; col++)
            {
                if(gameBoard[row][col] == 0 )
                {
                    openSpaces[row][col] = gameBoard[row][col];

                    //placing a new xyarray to hold coords in the dict list.
                    //a new array required every time.
                    int[] xyarray = new int[2];
                    xyarray[0] = row;
                    xyarray[1] = col;
                    dict.put(counter, xyarray);
                    counter++;
                    foundEmpty = true;
                    System.out.println("xyarray added into dict: " + Arrays.toString(xyarray));

                    return foundEmpty;
                }
            }
        }

        dictsize = dict.size();
        chooseLocation = randomGen.nextInt(dictsize + 1); // bound from 0 to max
        //todo: double check ^^^ that shit.
        int[] xyarray;
        int rowToPut;
        int colToPut;

        //cast into array.
        xyarray = (int[])dict.get(chooseLocation);

        rowToPut = xyarray[0];
        colToPut = xyarray[1];



        return foundEmpty;
    }


    //TODO: This gets mothballed, probably.
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
    public int getCurrentScore()
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
        this.score = currentHigh;
        return currentHigh;
    }


    public int getScore()
    {
        return score;
    }

//    /**
//     * Adds a command to the string history.
//     * @param command
//     */
//    public void addToStringOrder(String command)
//    {
//        this.movesToGetHere += command;
//    }

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
                //it is also adding every moved piece?
                if(gameBoard[row][col] != v)
                //if(gameBoard[row][col] != v && gameBoard[row][col] == gameBoard[row][col]*2)
                {
                    score += gameBoard[row][col];
                }

                return (this.gameBoard[row][col] == v ? retry : 0);
            }
            //if we did not find a match and move.
            crow += drow;
            ccol += dcol;
        }

        return 0;
    }

    


    public void moveRight()
    {
        for(int row = 0; row < GAME_SIZE; row++) {
            for (int col = GAME_SIZE-1; col >= 0; col--) {
                col += findAndMoveTile(row, col, 0,-1, 1);
            }
        }
        movesToGetHere.add(Direction.RIGHT);
        addNextNumber();
    }





    public void moveLeft()
    {
        for(int row = 0; row < GAME_SIZE; row++) {
            for (int col = 0; col < GAME_SIZE; col++) {
                col += findAndMoveTile(row, col, 0,1, -1);
            }
        }
        movesToGetHere.add(Direction.LEFT);
        addNextNumber();
    }

    public void moveDown()
    {
        for(int col = 0; col < GAME_SIZE; col++) {
            for (int row = GAME_SIZE-1; row >= 0; row--) {
                row += findAndMoveTile(row, col, -1,0, 1);
            }
        }
        movesToGetHere.add(Direction.DOWN);
        addNextNumber();
    }



    public void moveUp()
    {
        for(int col = 0; col < GAME_SIZE; col++) {
            for (int row = 0; row < GAME_SIZE; row++) {
                row += findAndMoveTile(row, col, 1,0, -1);
            }
        }
        movesToGetHere.add(Direction.UP);
        addNextNumber();
    }


}

