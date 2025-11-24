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

/**
 * Главное окно приложения (Представление - View).
 * <p>Отображает лабиринт и элементы управления, а также выступает в роли слушателя
 * (Listener) для получения обновлений от {@link RobotAgent}.</p>
 */
public class MazeView extends JFrame implements Listener {
    private final MazePanel mazePanel;
    private final JButton startButton = new JButton("Start Game"); // Переведено
    private final JButton findPathButton = new JButton("Find Path"); // Переведено
    private final JComboBox<String> algorithmBox;
    private final PathFindingManager pathMgr;
    private MazeController controller;

    /**
     * Создает главное окно приложения.
     *
     * @param maze Модель лабиринта.
     * @param agent Агент-робот.
     * @param pathMgr Менеджер поиска пути.
     */
    public MazeView(MazeModel maze, RobotAgent<MazeState> agent, PathFindingManager pathMgr) {
        this.pathMgr = pathMgr;
        this.mazePanel = new MazePanel(maze, agent);

        setTitle("Intelligent Maze Robot Solver");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(mazePanel, BorderLayout.CENTER);

        // Панель элементов управления
        JPanel controls = new JPanel();
        algorithmBox = new JComboBox<>(pathMgr.getAvailable().toArray(new String[0]));

        // Привязка действий к контроллеру
        startButton.addActionListener(e -> controller.startGame());
        findPathButton.addActionListener(e -> controller.findPath());
        algorithmBox.addActionListener(e -> controller.changeAlgorithm((String) algorithmBox.getSelectedItem()));

        controls.add(startButton);
        controls.add(findPathButton);
        controls.add(new JLabel("Algorithm:")); // Переведено
        controls.add(algorithmBox);

        add(controls, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Устанавливает контроллер, который будет обрабатывать действия пользователя.
     * @param controller Контроллер.
     */
    public void setController(MazeController controller) { this.controller = controller; }

    /**
     * Управляет доступностью элементов управления (кнопок).
     *
     * @param enable {@code true}, чтобы включить кнопки поиска пути и выбор алгоритма.
     */
    public void enableGameControls(boolean enable) {
        // Кнопка "Start Game" всегда доступна для старта/сброса
        startButton.setEnabled(true);
        findPathButton.setEnabled(enable);
        algorithmBox.setEnabled(enable);
    }

    /**
     * Принудительно запрашивает фокус ввода у панели лабиринта.
     * Это необходимо для корректной работы клавиатурного ввода после показа модальных окон.
     */
    public void requestFocusForPanel() {
        mazePanel.requestFocusInWindow();
    }

    /**
     * Запускает анимацию найденного пути.
     *
     * @param path Список состояний, составляющих путь.
     */
    public void showPath(List<? extends State> path) {
        animatePath(path.stream().map(s -> (MazeState) s).toList());
    }

    /**
     * Отображает найденный путь пошагово с задержкой (анимация).
     *
     * @param path Список состояний пути (MazeState).
     */
    private void animatePath(List<MazeState> path) {
        if (path.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Path Not Found!", "Info", JOptionPane.WARNING_MESSAGE); // Переведено
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
                            MazeView.this, "Path: " + path.size() + " steps\nAlgorithm: " + pathMgr.getCurrentAlgorithmName())); // Переведено
                }
            }
        }, 0, 100);
    }

    /**
     * Отображает диалоговое окно о победе (достижении цели).
     */
    public void showVictory() {
        JOptionPane.showMessageDialog(this, "Treasure Found!", "Victory", JOptionPane.INFORMATION_MESSAGE); // Переведено
    }

    /**
     * Добавляет обработчик клавиш к панели лабиринта для ручного управления.
     *
     * @param listener Обработчик клавиш.
     */
    public void addKeyListener(KeyListener listener) {
        mazePanel.addKeyListener(listener);
    }

    /**
     * Обрабатывает событие от агента (Listener'а).
     * @param event Объект события.
     */
    @Override
    public void handle(Event event) { mazePanel.repaint(); }
}