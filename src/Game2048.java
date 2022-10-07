import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Math;
import java.util.Scanner;

/**
 * The class to run and build the 2048 game.
 */
public class Game2048 {

    private final int GAME_SIZE = 4;

    private int[][] gameBoard = new int[GAME_SIZE][GAME_SIZE]; //represents 4x4 grid for the game.

    /**
     * default constructor to init a new game.
     */
    public Game2048()
    {
        newGame();

        System.out.println("Welcome to 2048");
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
        for (int i = 0; i < gameBoard.length; i++) {
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
        final String FILENAME = "C:\\Users\\greg\\IdeaProjects\\AI\\Java2048AI\\src\\2048_in.txt";

        File file = new File(FILENAME);
        Scanner inputFile = new Scanner(file);
        int numberOfTests;
        int numberTests = 0;
        int col = 0;

        numberTests = inputFile.nextInt();
        System.out.println("We will run " + numberTests + " number of tests.");



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


    public void combineNumbers()
    {


    }


    public boolean inBounds(int x, int y)
    {
        return(x >=0 && x < GAME_SIZE && y >= 0 && y < GAME_SIZE);
    }


    public int findAndMoveTile(int row, int col,int drow,int dcol, int retry)
    {
        int crow = row+drow;
        int ccol = col+dcol;

        while(inBounds(crow,ccol))
        {
            if(gameBoard[crow][ccol] != 0)
            {
                int v = gameBoard[crow][ccol];
                if(gameBoard[crow][ccol] == gameBoard[row][col] || gameBoard[row][col] == 0)
                {
                    gameBoard[row][col] += gameBoard[crow][ccol];
                    gameBoard[crow][ccol] = 0;
                }

                return (gameBoard[row][col] == v ? retry : 0);
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

    //May need to switch with up. Backwards maybe.
    public void moveUp()
    {
        for(int col = 0; col < GAME_SIZE; col++) {
            for (int row = 0; row < GAME_SIZE; row++) {
                row += findAndMoveTile(row, col, 1,0, -1);
            }
        }
    }


}

