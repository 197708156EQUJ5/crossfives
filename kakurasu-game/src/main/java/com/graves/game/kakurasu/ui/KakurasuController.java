package com.graves.game.kakurasu.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import com.graves.game.kakurasu.lib.Board;
import com.graves.game.kakurasu.lib.BoardInitializer;
import com.graves.game.kakurasu.lib.BoardLevelType;
import com.graves.game.kakurasu.lib.database.KakurasuDataStore;
import com.graves.game.kakurasu.lib.database.PasswordEncryptionService;
import com.graves.game.kakurasu.lib.database.User;

public class KakurasuController extends MouseAdapter implements ActionListener
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
                if (boardIdx != null)
                {
                    int boardIndex = Integer.parseInt(boardIdx.toString()) - 1;
                    int index = BoardInitializer.getIndex(boardIndex);
                    this.board.initialize(index, boardIndex);
                    this.view.newGame(this.board);
                }
                break;
            case "Add User":
                JTextField username = new JTextField();
                JTextField password = new JPasswordField();
                Object[] message = { "Username:", username, "Password:", password };

                int option = JOptionPane.showConfirmDialog(view, message, "Add User",
                        JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION)
                {
                    KakurasuDataStore.addUser(username.getText(), password.getText());
                }
                break;
            case "Login":
                JTextField loginUsername = new JTextField();
                JTextField loginPassword = new JPasswordField();
                Object[] loginMessage = { "Username:", loginUsername, "Password:", loginPassword };

                int loginOption = JOptionPane.showConfirmDialog(view, loginMessage, "Login",
                        JOptionPane.OK_CANCEL_OPTION);
                if (loginOption == JOptionPane.OK_OPTION)
                {
                    User user = KakurasuDataStore.getUser(loginUsername.getText());
                    if (user == null)
                    {
                        System.out.println("Can't find user " + loginUsername.getText());
                    }
                    try
                    {
                        if (PasswordEncryptionService.authenticate(loginPassword.getText(),
                                user.getEncryptedPassword(), user.getSalt()))
                        {
                            System.out.println("Login successful");
                        }
                    }
                    catch (NoSuchAlgorithmException | InvalidKeySpecException e1)
                    {
                        System.out.println("login failed");
                    }
                }
                else
                {
                    System.out.println("Login canceled");
                }
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

    /**
     * @param actionCommand
     */
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

    @Override
    public void mouseEntered(MouseEvent e)
    {
        Object source = e.getSource();

        if (source instanceof JLabel)
        {
            JLabel label = (JLabel) source;
            view.showHint(Integer.parseInt(label.getText()), label.getName());
        }
    }
}
