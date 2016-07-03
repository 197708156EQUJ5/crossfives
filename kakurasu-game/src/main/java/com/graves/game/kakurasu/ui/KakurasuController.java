package com.graves.game.kakurasu.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;

import com.graves.game.kakurasu.lib.Board;
import com.graves.game.kakurasu.lib.BoardInitializer;
import com.graves.game.kakurasu.lib.BoardLevelType;

public class KakurasuController implements ActionListener
{
    private Board board;
    private KakurasuView view;
    private BoardLevelType boardLevelType = BoardLevelType.RANDOM;
    private Stack<String> undoStack = new Stack<>();

    public KakurasuController(KakurasuView view)
    {
        super();
        this.view = view;
        BoardInitializer.initialize();
        this.board = new Board(0, 0);
    }

    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if (source instanceof JToggleButton)
        {
            String actionCommand = e.getActionCommand();
            view.setUndoButtonEnabled(true);
            undoStack.push(actionCommand);
            move(actionCommand);
            if (this.board.isWin())
            {
                view.stopGame();
            }
        }
        else if (source instanceof JMenuItem)
        {
            String actionCommand = e.getActionCommand();
            switch (actionCommand)
            {
            case "Exit":
                System.exit(0);
                break;
            case "New":
                undoStack.clear();
                switch (boardLevelType)
                {
                case EASY:
                    this.board = BoardInitializer.createEasyBoard();
                    break;
                case HARD:
                    this.board = BoardInitializer.createHardBoard();
                    break;
                case MEDIUM:
                    this.board = BoardInitializer.createMediumBoard();
                    break;
                case RANDOM:
                    this.board = BoardInitializer.createBoard();
                    break;

                default:
                    break;
                }
                Object boardIdx = JOptionPane.showInputDialog(view, "Board Index?",
                        "Select New Board", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(), null,
                        this.board.getBoardIndex());
                int boardIndex = Integer.parseInt(boardIdx.toString()) - 1;
                int index = BoardInitializer.getIndex(boardIndex);
                this.board.initialize(index, boardIndex);
                this.view.newGame(this.board);
                break;
            case "Easy":
                boardLevelType = BoardLevelType.EASY;
                view.setLevel(BoardLevelType.EASY);
                break;
            case "Medium":
                boardLevelType = BoardLevelType.MEDIUM;
                view.setLevel(BoardLevelType.MEDIUM);
                break;
            case "Hard":
                boardLevelType = BoardLevelType.HARD;
                view.setLevel(BoardLevelType.HARD);
                break;
            case "Random":
                boardLevelType = BoardLevelType.RANDOM;
                view.setLevel(BoardLevelType.RANDOM);
                break;
            default:
                break;
            }
        }
        else if (source instanceof JButton)
        {
            String actionCommand = e.getActionCommand();
            switch (actionCommand)
            {
            case "Pause":
                view.pauseGame();
                break;
            case "Clear":
                board.reset();
                view.updateView();
                view.clearGame();
                break;
            case "Undo":
                if (!undoStack.isEmpty())
                {
                    String toggleName = undoStack.pop();
                    move(toggleName);
                    view.flipToggle(toggleName);
                }
                if (undoStack.isEmpty())
                {
                    view.setUndoButtonEnabled(false);
                }
            default:
                break;
            }

        }
    }

    private void move(String actionCommand)
    {
        String[] split = actionCommand.split(",");
        int col = Integer.parseInt(split[0]);
        int row = Integer.parseInt(split[1]);
        this.board.setCell(col, row);
        this.board.recalculate();
        view.updateView();
    }

    public BoardLevelType getBoardLevelType()
    {
        return this.boardLevelType;
    }

    public void setBoardLevelType(BoardLevelType boardLevelType)
    {
        this.boardLevelType = boardLevelType;
    }

    public Board getBoard()
    {
        return this.board;
    }

}
