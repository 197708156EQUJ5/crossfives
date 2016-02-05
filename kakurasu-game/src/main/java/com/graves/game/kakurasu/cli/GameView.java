package com.graves.game.kakurasu.cli;

import com.graves.game.kakurasu.lib.Board;

public class GameView
{
    private Board board;

    public GameView(Board board)
    {
        this.board = board;
    }

    public void printBoard()
    {
        board.print();
    }
}
