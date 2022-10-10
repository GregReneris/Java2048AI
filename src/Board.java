import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Math;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * The class to run and build the 2048 game.
 */
public class Board {

    //TODO: Create a tree of nodes to hold board states.
    //BFS page 87 of handout

    private static final int GAME_SIZE = 4;

    private final int[][] gameBoard;  //represents 4x4 grid for the game.

    private int depth = 0;

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
     * constructor to handle depth.
     * @param depth
     * @param gameBoard
     */
    public Board(int depth, int[][] gameBoard)
    {
        this.depth = depth;
        this.gameBoard = gameBoard.clone();

        //newGame(); //causes all boards to reset.

        System.out.println("New instance of board game");
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

    //public void instantiateBeginningState()
    //{
    //    int position1x;
    //    int position1y;
    //    int postition2x;
    //    int postition2y;
    //    }

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
        return currentHigh;
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
    }





    public void moveLeft()
    {
        for(int row = 0; row < GAME_SIZE; row++) {
            for (int col = 0; col < GAME_SIZE; col++) {
                col += findAndMoveTile(row, col, 0,1, -1);
            }
        }
    }

    public void moveDown()
    {
        for(int col = 0; col < GAME_SIZE; col++) {
            for (int row = GAME_SIZE-1; row >= 0; row--) {
                row += findAndMoveTile(row, col, -1,0, 1);
            }
        }
    }



    public void moveUp()
    {
        for(int col = 0; col < GAME_SIZE; col++) {
            for (int row = 0; row < GAME_SIZE; row++) {
                row += findAndMoveTile(row, col, 1,0, -1);
            }
        }
    }


}

