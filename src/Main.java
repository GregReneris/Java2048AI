/**
 * A program that creates and runs version of 2048.
 * Coded in Java (using version 18).
 * Uses algorithms to determine optimal path for the highest score.
 *
 */
import java.util.ArrayDeque;
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
        Board solution = null;

        //create the game object.
        Board game = new Board();
        game.makeBoardEmpty();

        //ensuring the board is empty each time.
        game.printBoard();

        System.out.println(game + " gb " + game.getGameBoard());


        game.importStaringPosition();
        game.printBoard();

//        game.moveDown();
//        game.printBoard();

        solution = breadthFirstSearch(game);

        System.out.println("Solution board: ");
        solution.printBoard();
        System.out.println(solution.movesToGetHere);
        System.out.println("Solution score" + solution.getScore());

        game.scoreIncrease(game);

        //just pass in game.
        //Board game2 =  new Board(game);

//        System.out.println(game + " gb " + game.getGameBoard() [0]);
//        System.out.println(game2 + " gb " + game2.getGameBoard() [0]);




    }


//    public static Board makeTreeNDeep(Board GameNode, int maxDepth) {
//        int currentDepth = GameNode.getDepth();
//
//
//        //create boards going down to desired depth.
//        for (int i = 0; i < maxDepth; i++) {
//            //each board will get 4 children, with each move.
//            for (int j = 1; i < 5; i++) {
//                switch (j) {
//                    case 1:
//                        Board game1 = new Board(GameNode);
//                        game1.moveUp();
//                        game1.addToStringOrder("Move Up");
//                        game1.getScore();
//                        break;
//
//                    case 2:
//                        Board game2 = new Board(GameNode);
//                        game2.moveDown();
//                        game2.addToStringOrder("Move Down");
//                        game2.getScore();
//                        break;
//
//                    case 3:
//                        Board game3 = new Board(GameNode);
//                        game3.moveLeft();
//                        game3.addToStringOrder("Move Left");
//                        game3.getScore();
//                        break;
//
//                    case 4:
//                        Board game4 = new Board(GameNode);
//                        game4.moveRight();
//                        game4.addToStringOrder("Move Right");
//                        game4.getScore();
//                        break;
//                }
//            }
//        }
//        return null;
//    }











    //outside of breadthFirstSearch will still need to compare
    //to the highest score or first solution found.
    //if bfs returns null, then it failed.

    //note/tod-o, BFS may require a full tree before running.

    public static Board breadthFirstSearch(Board gameNode)
    {
        Board bestSolution = gameNode;
        int counter = 0;

        //a funky class that can work like both a stack and a queue.
        //use .add and .remove for queue functions.
        ArrayDeque<Board> work = new ArrayDeque<Board>();
        work.add(gameNode);

        while(!work.isEmpty())
        {
            gameNode = work.remove(); //pop first Board off.
            if(gameNode.getScore() > bestSolution.getScore())
            {
                bestSolution = gameNode;
            }
            if(gameNode.getDepth() < 3 )
            {
                work.add(new Board(gameNode, Board.Direction.UP));
                work.add(new Board(gameNode, Board.Direction.RIGHT));
                work.add(new Board(gameNode, Board.Direction.DOWN));
                work.add(new Board(gameNode, Board.Direction.LEFT));
            }
            else{
            }
            counter++;

        }
        System.out.println(counter);
        return bestSolution;
    }




}
