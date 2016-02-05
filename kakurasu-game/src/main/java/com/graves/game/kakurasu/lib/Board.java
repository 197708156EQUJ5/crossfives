package com.graves.game.kakurasu.lib;

/**
 * First game: 1117588<br/>
 * First hard game: 3320472<br/>
 */
public class Board
{
    private int[] rowAnswers;
    private int[] rowUserAnswers;
    private int[] colAnswers;
    private int[] colUserAnswers;
    private boolean[] board;
    private boolean[] userBoard;
    private static final int BOARD_SIZE = 5;
    private int boardIndex;

    public Board(int index, int boardIndex)
    {
        initialize(index, boardIndex);
    }

    public void initialize(int index, int boardIndex)
    {
        this.boardIndex = boardIndex;
        rowAnswers = new int[BOARD_SIZE];
        rowUserAnswers = new int[BOARD_SIZE];
        colAnswers = new int[BOARD_SIZE];
        colUserAnswers = new int[BOARD_SIZE];
        board = new boolean[BOARD_SIZE * BOARD_SIZE];
        userBoard = new boolean[BOARD_SIZE * BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++)
        {
            board[i] = (index & (int) Math.pow(2, i)) > 0;
            rowAnswers[i / BOARD_SIZE] += board[i] ? ((i % BOARD_SIZE) + 1) : 0;
            colAnswers[i % BOARD_SIZE] += board[i] ? ((i / BOARD_SIZE) + 1) : 0;
        }
    }

    public boolean[] getUserBoard()
    {
        return userBoard;
    }

    public int getBoardSize()
    {
        return BOARD_SIZE;
    }

    public int[] getRowAnswers()
    {
        return rowAnswers;
    }

    public int[] getRowUserAnswers()
    {
        return rowUserAnswers;
    }

    public int[] getColAnswers()
    {
        return colAnswers;
    }

    public int[] getColUserAnswers()
    {
        return colUserAnswers;
    }

    public int getBoardIndex()
    {
        return this.boardIndex + 1;
    }

    public void printAnswers()
    {
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++)
        {
            System.out.print(board[i] ? " * " : " X ");
            if ((i + 1) % BOARD_SIZE == 0)
            {
                System.out.println(" " + rowAnswers[i / BOARD_SIZE]);
            }
        }
        for (int i = 0; i < colAnswers.length; i++)
        {
            System.out.print(String.format("%2s ", colAnswers[i]));
        }

    }

    public void print()
    {

        for (int i = 0; i < BOARD_SIZE; i++)
        {
            System.out.print("  " + String.format("%2s", (i + 1)));
        }
        System.out.println();
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++)
        {
            if ((i) % BOARD_SIZE == 0)
            {
                System.out.print((i / BOARD_SIZE) + 1);
            }
            System.out.print(userBoard[i] ? "  * " : "  X ");
            if ((i + 1) % BOARD_SIZE == 0)
            {
                System.out.println(" " + String.format("%2s ", rowAnswers[i / BOARD_SIZE])
                        + String.format("(%2s)", rowUserAnswers[i / BOARD_SIZE]));
            }
        }
        System.out.println();
        System.out.print(" ");
        for (int i = 0; i < colAnswers.length; i++)
        {
            System.out.print(String.format("%3s ", colAnswers[i]));
        }
        System.out.println();
        System.out.print(" ");
        for (int i = 0; i < colUserAnswers.length; i++)
        {
            System.out.print(String.format("(%2s)", colUserAnswers[i]));
        }
        System.out.println();

    }

    public void setCell(int col, int row)
    {
        int i = ((row - 1) * BOARD_SIZE) + (col - 1);
        userBoard[i] = !userBoard[i];
    }
    
    public void reset()
    {
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++)
        {
            userBoard[i] = false;
        }
        recalculate();
    }

    public void recalculate()
    {
        rowUserAnswers = new int[BOARD_SIZE];
        colUserAnswers = new int[BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++)
        {
            rowUserAnswers[i / BOARD_SIZE] += userBoard[i] ? 1 + (i % BOARD_SIZE) : 0;
            colUserAnswers[i % BOARD_SIZE] += userBoard[i] ? 1 + (i / BOARD_SIZE) : 0;
        }
    }

    public boolean isWin()
    {
        for (int i = 0; i < rowUserAnswers.length; i++)
        {
            if (rowUserAnswers[i] != rowAnswers[i])
            {
                return false;
            }
        }
        for (int i = 0; i < colUserAnswers.length; i++)
        {
            if (colUserAnswers[i] != colAnswers[i])
            {
                return false;
            }
        }
        return true;
    }
}
