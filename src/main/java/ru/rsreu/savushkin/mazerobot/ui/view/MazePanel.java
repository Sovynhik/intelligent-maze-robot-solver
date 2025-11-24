package ru.rsreu.savushkin.mazerobot.ui.view;

import ru.rsreu.savushkin.mazerobot.core.entity.CellType;
import ru.rsreu.savushkin.mazerobot.core.model.MazeModel;
import ru.rsreu.savushkin.mazerobot.core.model.RobotAgent;
import ru.rsreu.savushkin.mazerobot.core.state.maze.MazeState;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Панель для отображения лабиринта, робота и найденного пути.
 * <p>Отвечает за графическое представление данных из {@link MazeModel} и {@link RobotAgent}.</p>
 */
public class MazePanel extends JPanel {
    private final MazeModel maze;
    private final RobotAgent<MazeState> agent;
    /** Список состояний, составляющих найденный путь (для визуализации). */
    private List<MazeState> path = List.of();
    /** Размер одной ячейки в пикселях. */
    private static final int CELL_SIZE = 35;

    /**
     * Создает новую панель лабиринта.
     *
     * @param maze Модель лабиринта.
     * @param agent Агент, представляющий робота.
     */
    public MazePanel(MazeModel maze, RobotAgent<MazeState> agent) {
        this.maze = maze;
        this.agent = agent;
        setPreferredSize(new Dimension(maze.getWidth() * CELL_SIZE, maze.getHeight() * CELL_SIZE));
        setFocusable(true);
    }

    /**
     * Обновляет путь для отображения и перерисовывает панель.
     *
     * @param path Новый список состояний пути.
     */
    public void updatePath(List<MazeState> path) {
        this.path = path != null ? List.copyOf(path) : List.of();
        repaint();
    }

    /**
     * Метод отрисовки компонентов панели.
     * Отрисовывает сетку лабиринта, найденный путь и текущее положение робота.
     *
     * @param g Графический контекст.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // 1. Отрисовка сетки и ячеек (стены, сокровища, пустота)
        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                CellType cell = maze.getCell(x, y);
                g2.setColor(cell == CellType.WALL ? Color.DARK_GRAY :
                        cell == CellType.TREASURE ? Color.RED : Color.WHITE);
                g2.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g2.setColor(Color.BLACK);
                g2.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        // 2. Отрисовка найденного пути (полупрозрачные зеленые круги)
        g2.setColor(new Color(0, 255, 0, 150));
        for (MazeState s : path) {
            g2.fillOval(s.x() * CELL_SIZE + 10, s.y() * CELL_SIZE + 10, CELL_SIZE - 20, CELL_SIZE - 20);
        }

        // 3. Отрисовка текущего положения робота (синий круг)
        MazeState current = agent.getCurrentState();
        if (current != null) {
            g2.setColor(Color.BLUE);
            g2.fillOval(current.x() * CELL_SIZE + 5, current.y() * CELL_SIZE + 5, CELL_SIZE - 10, CELL_SIZE - 10);
        }
    }
}