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
        for (int i = 0; i < gameBoard.length; i++) {
            //prints out the boxes and X and O
            System.out.print("|");
            for (int j = 0; j < gameBoard[i].length; j++) {
                System.out.printf(" %4s |", gameBoard[i][j]);
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
        int first = 1;
        int col = 0;
        int row = 0;

        while (inputFile.hasNext()) {
            if(first == 1)
            {
                numberOfTests = inputFile.nextInt();
                first++;
                //inputFile.nextLine();
                System.out.println("We will run " + numberOfTests + " number of tests.");
            }
            //Add information to the board. Each line has 4 pieces of information,
            //Separated by a comma.
            String newInput = inputFile.next();
            String[] split = newInput.split(",", 4);
            //split them into 4.
            System.out.println("Printing out new Input:" + newInput);

                for(col = 0; col < GAME_SIZE; col++)
                {
                    gameBoard[row][col] = Integer.parseInt(split[col]);
                }
                row++;
            }
        }

    public static void moveRight()
    {

    }

    public static void moveLeft()
    {

    }

    public static void moveUp()
    {

    }

    public static void moveDown()
    {

    }


}

