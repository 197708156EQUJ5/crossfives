package com.graves.game.kakurasu.cli;

import java.util.Scanner;

import com.graves.game.kakurasu.lib.Board;
import com.graves.game.kakurasu.lib.BoardInitializer;

public class KakurasuCommandLine
{
    public static void main(String[] args)
    {
//        Board board = BoardInitializer.createBoard();
        BoardInitializer.initialize();
        Board board = BoardInitializer.createEasyBoard();
//        board.printAnswers();
        System.out.println();
        GameView gameView = new GameView(board);
        GameController controller = new GameController(board, gameView);
        controller.updateView();
        Scanner scanner = new Scanner(System.in);
        String next = "";
        while (!next.equals("x"))
        {            
            System.out.println("\nEnter move [col,row] or 'x': ");
            next = scanner.nextLine();
            System.out.println();
            String[] split = next.split(",");
            try
            {
                int col = Integer.parseInt(split[0]);
                int row = Integer.parseInt(split[1]);
                if(controller.move(col, row))
                {
                    System.out.println("Win");
                    next = "x";
                }
                controller.updateView();
            }
            catch (NumberFormatException e)
            {
                break;
            }
        }
        scanner.close();
        System.out.println("Good-bye");
    }

}
