package com.graves.game.kakurasu.cli;

import com.graves.game.kakurasu.lib.Board;

public class GameController
{
    private Board board;
    private GameView view;

    public GameController(Board board, GameView view)
    {
        this.board = board;
        this.view = view;
    }

    public boolean move(int col, int row)
    {
        this.board.setCell(col, row);
        this.board.recalculate();
        
        return this.board.isWin();
    }

    public void updateView()
    {
        this.view.printBoard();
    }
}
