/**
 * The class to run and build the 2048 game.
 */
public class Game2048 {

    private final int GAME_SIZE = 4;

    private String[][] gameBoard = new String[GAME_SIZE][GAME_SIZE]; //represents 4x4 grid for the game.

    /**
     * default constructor to init a new game.
     */
    public Game2048()
    {
        newGame();
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
                gameBoard[i][j] = "1";
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


            /**
             * randomly places two 2's on the board.
             */
    public void instantiateBeginningState()
    {
        //code
    }

    /**
     * Adds a two to the next vertically free space.
     */
    public void addNextNumber()
    {

    }


}
