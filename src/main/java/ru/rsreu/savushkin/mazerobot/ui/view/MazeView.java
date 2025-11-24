package ru.rsreu.savushkin.mazerobot.ui.view;

import ru.rsreu.savushkin.mazerobot.core.controller.MazeController;
import ru.rsreu.savushkin.mazerobot.core.entity.Event;
import ru.rsreu.savushkin.mazerobot.core.model.Listener;
import ru.rsreu.savushkin.mazerobot.core.model.MazeModel;
import ru.rsreu.savushkin.mazerobot.core.model.RobotAgent;
import ru.rsreu.savushkin.mazerobot.core.solver.PathFindingManager;
import ru.rsreu.savushkin.mazerobot.core.state.State;
import ru.rsreu.savushkin.mazerobot.core.state.maze.MazeState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MazeView extends JFrame implements Listener {
    private final MazePanel mazePanel;
    private final JButton startButton = new JButton("Начать игру");
    private final JButton findPathButton = new JButton("Найти путь");
    private final JComboBox<String> algorithmBox;
    private final PathFindingManager pathMgr;
    private MazeController controller;

    public MazeView(MazeModel maze, RobotAgent<MazeState> agent, PathFindingManager pathMgr) {
        this.pathMgr = pathMgr;
        this.mazePanel = new MazePanel(maze, agent);

        setTitle("Intelligent Maze Robot Solver");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(mazePanel, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        algorithmBox = new JComboBox<>(pathMgr.getAvailable().toArray(new String[0]));

        startButton.addActionListener(e -> controller.startGame());
        findPathButton.addActionListener(e -> controller.findPath());
        algorithmBox.addActionListener(e -> controller.changeAlgorithm((String) algorithmBox.getSelectedItem()));

        controls.add(startButton);
        controls.add(findPathButton);
        controls.add(new JLabel("Алгоритм:"));
        controls.add(algorithmBox);

        add(controls, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    public void setController(MazeController controller) { this.controller = controller; }

    /**
     * Управляет доступностью элементов управления (кнопок).
     * @param enable - если true, включает ручное управление и кнопки.
     */
    public void enableGameControls(boolean enable) {
        // Кнопка "Начать игру" всегда доступна для старта/сброса
        startButton.setEnabled(true);
        findPathButton.setEnabled(enable);
        algorithmBox.setEnabled(enable);
    }

    /**
     * Принудительно запрашивает фокус ввода у панели лабиринта.
     * ЭТО КЛЮЧЕВОЕ ИЗМЕНЕНИЕ ДЛЯ РАБОТЫ КЛАВИАТУРЫ ПОСЛЕ JDialog.
     */
    public void requestFocusForPanel() {
        mazePanel.requestFocusInWindow();
    }

    public void showPath(List<? extends State> path) {
        animatePath(path.stream().map(s -> (MazeState) s).toList());
    }

    private void animatePath(List<MazeState> path) {
        if (path.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Путь не найден!", "Инфо", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Timer timer = new Timer();
        final int[] idx = {0};
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (idx[0] < path.size()) {
                    mazePanel.updatePath(path.subList(0, idx[0] + 1));
                    idx[0]++;
                } else {
                    timer.cancel();
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                            MazeView.this, "Путь: " + path.size() + " шагов\nАлгоритм: " + pathMgr.getCurrentAlgorithmName()));
                }
            }
        }, 0, 100);
    }

    public void showVictory() {
        JOptionPane.showMessageDialog(this, "Клад найден!", "Победа", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Добавляет обработчик клавиш к MazePanel */
    public void addKeyListener(KeyListener listener) {
        mazePanel.addKeyListener(listener);
    }

    @Override
    public void handle(Event event) { mazePanel.repaint(); }
}