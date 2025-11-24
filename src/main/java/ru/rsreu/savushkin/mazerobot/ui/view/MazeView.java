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
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Главное окно приложения (Представление - View).
 * <p>Отображает лабиринт и элементы управления, а также выступает в роли слушателя
 * (Listener) для получения обновлений от {@link RobotAgent}.</p>
 */
public class MazeView extends JFrame implements Listener {
    private final MazePanel mazePanel;
    private final JButton startButton = new JButton("START GAME");
    private final JButton findPathButton = new JButton("FIND PATH");
    private final JComboBox<String> algorithmBox;
    private final PathFindingManager pathMgr;
    private MazeController controller;

    // Стилизация для улучшения внешнего вида
    private static final Color ACCENT_COLOR = new Color(60, 180, 75); // Темно-зеленый акцент
    private static final Font BOLD_FONT = new Font("Arial", Font.BOLD, 12);

    public MazeView(MazeModel maze, RobotAgent<MazeState> agent, PathFindingManager pathMgr) {
        this.pathMgr = pathMgr;
        this.mazePanel = new MazePanel(maze, agent);

        setTitle("Intelligent Maze Robot Solver");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. Панель для центрирования лабиринта с отступами
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(new EmptyBorder(10, 10, 5, 10)); // Внешний отступ вокруг лабиринта
        centerPanel.add(mazePanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // 2. Панель элементов управления (Controls)
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Увеличенные отступы между элементами
        controls.setBorder(new EmptyBorder(5, 10, 10, 10)); // Внешний отступ снизу

        // 3. Стилизация кнопок

        // Кнопка START GAME (более заметная)
        startButton.setFont(BOLD_FONT);
        startButton.setBackground(ACCENT_COLOR);
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.addActionListener(e -> controller.startGame());

        // Кнопка FIND PATH
        findPathButton.setFont(BOLD_FONT);
        findPathButton.setBackground(Color.LIGHT_GRAY);
        findPathButton.setFocusPainted(false);
        findPathButton.addActionListener(e -> controller.findPath());

        // JComboBox
        algorithmBox = new JComboBox<>(pathMgr.getAvailable().toArray(new String[0]));
        algorithmBox.addActionListener(e -> controller.changeAlgorithm((String) algorithmBox.getSelectedItem()));

        // 4. Сборка панели управления
        controls.add(startButton);
        controls.add(findPathButton);
        controls.add(new JLabel("Algorithm:"));
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
        startButton.setEnabled(true);
        findPathButton.setEnabled(enable);
        algorithmBox.setEnabled(enable);
    }

    /**
     * Принудительно запрашивает фокус ввода у панели лабиринта.
     */
    public void requestFocusForPanel() {
        mazePanel.requestFocusInWindow();
    }

    public void showPath(List<? extends State> path) {
        animatePath(path.stream().map(s -> (MazeState) s).toList());
    }

    private void animatePath(List<MazeState> path) {
        if (path.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Path Not Found!", "Info", JOptionPane.WARNING_MESSAGE);
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
                            MazeView.this, "Path: " + path.size() + " steps\nAlgorithm: " + pathMgr.getCurrentAlgorithmName()));
                }
            }
        }, 0, 100);
    }

    public void showVictory() {
        JOptionPane.showMessageDialog(this, "Treasure Found!", "Victory", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Добавляет обработчик клавиш к MazePanel */
    public void addKeyListener(KeyListener listener) {
        mazePanel.addKeyListener(listener);
    }

    @Override
    public void handle(Event event) { mazePanel.repaint(); }
}