/**
 * A program that creates and runs version of 2048.
 * Coded in Java (using version 18).
 * Uses algorithms to determine optimal path for the highest score.
 *
 */
import java.util.Queue;
import java.io.FileNotFoundException;
import java.util.LinkedList;


/**
 * Main class creates and then runs the 2048 game and
 * associated search algorithms. Takes in states from an input file and
 * also outputs it to another text file.
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("The 2048 game will be built, then a few moves run by an AI.");
        int maxDepth = 3;


        //create the game object.
        Board game = new Board();
        game.makeBoardEmpty();

        //ensuring the board is empty each time.
        game.printBoard();

        System.out.println(game + " gb " + game.getGameBoard());


        game.importStaringPosition();
        game.printBoard();

        game.moveDown();
        System.out.println("*************");
        game.printBoard();

        //just pass in game.
        Board game2 =  new Board(game);

        System.out.println(game + " gb " + game.getGameBoard() [0]);
        System.out.println(game2 + " gb " + game2.getGameBoard() [0]);


    }


    public static Board makeTreeNDeep(Board GameNode, int maxDepth)
    {
        int currentDepth = GameNode.getDepth();

        //create boards going down to desired depth.
        for(int i = 0; i < maxDepth; i++)
        {
            //each board will get 4 children, with each move.
            for(int j = 1; i < 5; i ++)
            {
                switch(j)
                {
                    case 1:
                        Board game1 = new Board(GameNode);
                        game1.moveUp();
                        game1.addToStringOrder("Move Up");
                        game1.getScore();
                        break;

                    case 2:
                        Board game2 = new Board(GameNode);
                        game2.moveDown();
                        game2.addToStringOrder("Move Down");
                        game2.getScore();
                        break;

                    case 3:
                        Board game3 = new Board(GameNode);
                        game3.moveLeft();
                        game3.addToStringOrder("Move Left");
                        game3.getScore();
                        break;

                    case 4:
                        Board game4 = new Board(GameNode);
                        game4.moveRight();
                        game4.addToStringOrder("Move Right");
                        game4.getScore();
                        break;
                }
        }
    }







    //outside of breadthFirstSearch will still need to compare
    //to the highest score or first solution found.
    //if bfs returns null, then it failed.

    //note/tod-o, BFS may require a full tree before running.

    public static Board breadthFirstSearch(Board GameNode,int maxDepth)
    {
        int score = 0;
        Board Solution = null;
        Queue<Board> frontier = null;
        Queue<Board> explored = null;
        boolean foundSolution = true;

        frontier.add(GameNode);

        //check to see if gameNode's depth is equal to maxDepth.
        //if so, return solution.
        if(GameNode.getDepth() == maxDepth)
        {
            Solution = GameNode;
            return Solution;
        }

        do {
            if(frontier.isEmpty())
            {
                foundSolution = false;
            }
        } while (foundSolution == true);

        GameNode = frontier.poll(); //supposed to choose node with shallowest depth.
        explored.add(GameNode); //not sure this is right. Might want earlier node with state.

        for(int i = 1; i < 5; i ++)
        {
            switch(i)
            {
                case 1:
                    Board game1 = new Board(GameNode);
                    game1.moveUp();
                    game1.addToStringOrder("Move Up");
                    // Child / Solution ?

                case 2:
                    Board game2 = new Board(GameNode);
                    game2.moveDown();
                    game2.addToStringOrder("Move Down");

                case 3:
                    Board game3 = new Board(GameNode);
                    game3.moveLeft();
                    game3.addToStringOrder("Move Left");

                case 4:
                    Board game4 = new Board(GameNode);
                    game4.moveRight();
                    game4.addToStringOrder("Move Right");

            }
        }


        return null;
    }




}
