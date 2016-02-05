package com.graves.game.kakurasu.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToggleButton;
import javax.swing.Timer;

import com.graves.game.kakurasu.lib.Board;
import com.graves.game.kakurasu.lib.BoardLevelType;

public class KakurasuView extends JFrame
{
    private static final long serialVersionUID = 1L;
    private Container container;
    private Board board;
    private JPanel panel;
    private KakurasuController controller;
    private List<JLabel> colUserLabelList = new ArrayList<>();
    private List<JLabel> rowUserLabelList = new ArrayList<>();
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem newMenuItem;
    private JMenuItem exitMenuItem;
    private JMenu optionMenu;
    private JRadioButtonMenuItem randomMenuItem;
    private JRadioButtonMenuItem easyMenuItem;
    private JRadioButtonMenuItem mediumMenuItem;
    private JRadioButtonMenuItem hardMenuItem;
    private List<JLabel> rowLabelList = new ArrayList<>();
    private List<JLabel> colLabelList = new ArrayList<>();
    private List<JToggleButton> toggleButtonList = new ArrayList<>();
    private JPanel statusPanel;
    private JPanel elapseTimePanel;
    private JLabel elapseTimeLabel;
    private Timer timer;
    private JPanel bottomPanel;
    private JLabel statusLabel;
    private JPanel levelPanel;
    private JLabel levelLabel;
    private JPanel buttonPanel;
    private JButton clearButton;
    private JButton undoButton;
    private JButton pauseButton;
    private boolean isPaused;
    private int offset;
    private Timer pausedTimer;

    public KakurasuView()
    {
        controller = new KakurasuController(this);
        this.board = controller.getBoard();
        initComponents();
        initMenu();
        initLayout();
    }

    private void initMenu()
    {
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        newMenuItem = new JMenuItem("New");
        newMenuItem.setActionCommand("New");
        newMenuItem.addActionListener(controller);
        fileMenu.add(newMenuItem);
        fileMenu.addSeparator();
        exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setActionCommand("Exit");
        exitMenuItem.addActionListener(controller);
        fileMenu.add(exitMenuItem);

        ButtonGroup group = new ButtonGroup();
        optionMenu = new JMenu("Option");
        menuBar.add(optionMenu);
        randomMenuItem = new JRadioButtonMenuItem("Random All", true);
        randomMenuItem.addActionListener(controller);
        randomMenuItem.setActionCommand("Random");
        group.add(randomMenuItem);
        optionMenu.add(randomMenuItem);
        easyMenuItem = new JRadioButtonMenuItem("Easy");
        easyMenuItem.addActionListener(controller);
        easyMenuItem.setActionCommand("Easy");
        group.add(easyMenuItem);
        optionMenu.add(easyMenuItem);
        mediumMenuItem = new JRadioButtonMenuItem("Medium");
        mediumMenuItem.addActionListener(controller);
        mediumMenuItem.setActionCommand("Medium");
        group.add(mediumMenuItem);
        optionMenu.add(mediumMenuItem);
        hardMenuItem = new JRadioButtonMenuItem("Hard");
        hardMenuItem.addActionListener(controller);
        hardMenuItem.setActionCommand("Hard");
        group.add(hardMenuItem);
        optionMenu.add(hardMenuItem);

        setJMenuBar(menuBar);
    }

    private void initComponents()
    {
        container = getContentPane();
        container.setLayout(new BorderLayout());

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        clearButton = new JButton("Clear");
        clearButton.addActionListener(controller);
        clearButton.setActionCommand("Clear");
        undoButton = new JButton("Undo");
        undoButton.addActionListener(controller);
        undoButton.setActionCommand("Undo");
        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(controller);
        pauseButton.setActionCommand("Pause");
        int[] rowAnswers = this.board.getRowAnswers();
        int[] rowUserAnswers = this.board.getRowUserAnswers();
        int[] colAnswers = this.board.getColAnswers();
        int[] colUserAnswers = this.board.getColUserAnswers();
        int boardSize = board.getBoardSize();
        panel = new JPanel(new GridLayout(0, boardSize + 3));
        for (int i = 0; i < Math.pow(boardSize + 3, 2); i++)
        {
            if (i == 0)
            {
                panel.add(new JLabel("", JLabel.CENTER));
            }
            else if (i % (boardSize + 3) == 0 && i / (boardSize + 3) <= boardSize)
            {
                panel.add(new JLabel("" + (i / (boardSize + 3)), JLabel.CENTER));
            }
            else if (i % (boardSize + 3) == 0 && i / (boardSize + 3) > boardSize)
            {
                panel.add(new JLabel(""));
            }
            else if (i / (boardSize + 3) == 0 && i % (boardSize + 3) <= boardSize)
            {
                panel.add(new JLabel("" + (i % (boardSize + 3)), JLabel.CENTER));
            }
            else if (i / (boardSize + 3) == 0 && i % (boardSize + 3) > boardSize)
            {
                panel.add(new JLabel(""));
            }
            else if (i % (boardSize + 3) == boardSize + 1 && i / (boardSize + 3) <= boardSize)
            {
                JLabel label = new JLabel("" + rowAnswers[(i / (boardSize + 3)) - 1],
                        JLabel.CENTER);
                rowLabelList.add(label);
                panel.add(label);
            }
            else if (i % (boardSize + 3) == boardSize + 2 && i / (boardSize + 3) <= boardSize)
            {
                JLabel label = new JLabel("" + rowUserAnswers[(i / (boardSize + 3)) - 1],
                        JLabel.LEFT);
                JPanel rowUserLabelPanel = new JPanel();
                rowUserLabelPanel.add(new JLabel("("));
                rowUserLabelPanel.add(label);
                rowUserLabelPanel.add(new JLabel(")"));
                rowUserLabelList.add(label);
                panel.add(rowUserLabelPanel);
            }
            else if (i / (boardSize + 3) == boardSize + 1 && i % (boardSize + 3) <= boardSize)
            {
                JLabel label = new JLabel("" + colAnswers[(i % (boardSize + 3)) - 1],
                        JLabel.CENTER);
                colLabelList.add(label);
                panel.add(label);
            }
            else if (i / (boardSize + 3) == boardSize + 2 && i % (boardSize + 3) <= boardSize)
            {
                JLabel label = new JLabel(colUserAnswers[(i % (boardSize + 3)) - 1] + "",
                        JLabel.CENTER);
                JPanel colUserLabelPanel = new JPanel();
                colUserLabelPanel.add(new JLabel("("));
                colUserLabelPanel.add(label);
                colUserLabelPanel.add(new JLabel(")"));
                colUserLabelList.add(label);
                panel.add(colUserLabelPanel);
            }
            else if (i / (boardSize + 3) > 0 && i % (boardSize + 3) > 0
                    && i % (boardSize + 3) <= boardSize && i / (boardSize + 3) > 0
                    && i / (boardSize + 3) <= boardSize)
            {
                final JToggleButton toggleButton = new JToggleButton("X", false);
                toggleButton.setEnabled(false);
                toggleButtonList.add(toggleButton);
                toggleButton.addItemListener(new ItemListener()
                {
                    public void itemStateChanged(ItemEvent ev)
                    {
                        if (ev.getStateChange() == ItemEvent.SELECTED)
                        {
                            toggleButton.setText("");
                        }
                        else if (ev.getStateChange() == ItemEvent.DESELECTED)
                        {
                            toggleButton.setText("X");
                        }
                    }
                });
                toggleButton.addActionListener(controller);
                toggleButton.setActionCommand((i % (boardSize + 3) + "," + (i / (boardSize + 3))));
                panel.add(toggleButton);
            }
            else
            {
                panel.add(new JLabel("", JLabel.CENTER));
            }
        }

        statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Waiting for new game");
        levelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        levelLabel = new JLabel(BoardLevelType.RANDOM.getLevel());
        elapseTimePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        elapseTimeLabel = new JLabel("00:00:00");
        bottomPanel = new JPanel(new BorderLayout());
    }

    public void flipToggle(String toggleName)
    {
        for (JToggleButton toggle : toggleButtonList)
        {
            if (toggle.getActionCommand().equals(toggleName))
            {
                boolean isSelected = toggle.isSelected();
                toggle.setSelected(!isSelected);
                toggle.setText(toggle.isSelected() ? "" : "X");
            }
        }
    }

    private void initLayout()
    {
        buttonPanel.add(clearButton);
        buttonPanel.add(undoButton);
        buttonPanel.add(pauseButton);
        container.add(buttonPanel, BorderLayout.NORTH);
        container.add(panel, BorderLayout.CENTER);
        statusPanel.add(new JLabel("<html><b>Game:</b></html>"));
        statusPanel.add(statusLabel);
        bottomPanel.add(statusPanel, BorderLayout.WEST);
        levelPanel.add(new JLabel("<html><b>Level:</b></html>"));
        levelPanel.add(levelLabel);
        bottomPanel.add(levelPanel, BorderLayout.CENTER);
        elapseTimePanel.add(elapseTimeLabel);
        bottomPanel.add(elapseTimePanel, BorderLayout.EAST);
        container.add(bottomPanel, BorderLayout.SOUTH);

        setSize(400, 400);
        setLocation(50, 50);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void newGame(Board board)
    {
        if (timer != null && timer.isRunning())
        {
            timer.stop();
        }
        this.board = board;
        updateView();
        for (int i = 0; i < this.board.getColAnswers().length; i++)
        {
            colLabelList.get(i).setText(this.board.getColAnswers()[i] + "");
        }
        for (int i = 0; i < this.board.getRowAnswers().length; i++)
        {
            rowLabelList.get(i).setText(this.board.getRowAnswers()[i] + "");
        }
        for (JToggleButton toggle : toggleButtonList)
        {
            toggle.setEnabled(true);
            toggle.setSelected(false);
        }
        long startTime = System.currentTimeMillis();
        ActionListener listener = new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                long elapseTime = System.currentTimeMillis() - startTime - (offset * 1000);
                long hr = TimeUnit.MILLISECONDS.toHours(elapseTime);
                long min = TimeUnit.MILLISECONDS.toMinutes(elapseTime) % 60;
                long sec = TimeUnit.MILLISECONDS.toSeconds(elapseTime) % 60;
                elapseTimeLabel.setText(String.format("%02d:%02d:%02d", hr, min, sec));
            }
        };
        timer = new Timer(1000, listener);
        timer.start();
        statusLabel.setText("#" + board.getBoardIndex());
    }

    public void updateView()
    {
        int[] colAnswers = this.board.getColAnswers();
        int[] rowAnswers = this.board.getRowAnswers();
        int[] colUserAnswers = this.board.getColUserAnswers();
        int[] rowUserAnswers = this.board.getRowUserAnswers();
        for (int i = 0; i < colUserAnswers.length; i++)
        {
            JLabel label = colUserLabelList.get(i);
            if (colUserAnswers[i] > colAnswers[i])
            {
                label.setForeground(Color.RED);
            }
            else
            {
                label.setForeground(new Color(0, 200, 0));
            }
            label.setText("" + colUserAnswers[i]);
        }
        for (int i = 0; i < rowUserAnswers.length; i++)
        {
            JLabel label = rowUserLabelList.get(i);
            if (rowUserAnswers[i] > rowAnswers[i])
            {
                label.setForeground(Color.RED);
            }
            else
            {
                label.setForeground(new Color(0, 200, 0));
            }
            label.setText("" + rowUserAnswers[i]);
        }
    }

    public void stopGame()
    {
        for (JToggleButton toggle : toggleButtonList)
        {
            toggle.setEnabled(false);
        }
        timer.stop();
        statusLabel.setText("Win");
    }

    public void clearGame()
    {
        for (JToggleButton toggle : toggleButtonList)
        {
            toggle.setSelected(false);
        }
    }

    public void pauseGame()
    {
        ActionListener listener = new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (isPaused)
                {
                    offset++;
                }
            }
        };
        if (pausedTimer == null)
        {
            pausedTimer = new Timer(1000, listener);
        }
        for (JToggleButton toggle : toggleButtonList)
        {
            toggle.setEnabled(isPaused);
        }
        String text = "";
        if (isPaused)
        {
            text = "Paused";
            timer.start();
            pausedTimer.stop();
        }
        else
        {

            text = "Start";
            timer.stop();
            pausedTimer.start();
        }
        pauseButton.setText(text);
        isPaused = !isPaused;
    }

    public void setLevel(BoardLevelType boardLevelType)
    {
        levelLabel.setText(boardLevelType.getLevel());
    }
}
