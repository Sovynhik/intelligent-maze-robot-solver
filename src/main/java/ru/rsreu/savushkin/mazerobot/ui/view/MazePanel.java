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
 * <p>Использует {@code Graphics2D} для включения сглаживания (anti-aliasing) и применяет
 * стилизованные цвета и фигуры для улучшения визуального восприятия элементов.</p>
 */
public class MazePanel extends JPanel {
    private final MazeModel maze;
    private final RobotAgent<MazeState> agent;
    /** Список состояний, составляющих найденный путь (для визуализации). */
    private List<MazeState> path = List.of();
    /** Размер одной ячейки в пикселях. */
    private static final int CELL_SIZE = 35;

    // Новые константы цветов для улучшения визуала
    private static final Color WALL_COLOR = new Color(50, 50, 50); // Почти черный
    private static final Color FLOOR_COLOR = new Color(230, 230, 230); // Светло-серый пол
    private static final Color TREASURE_COLOR = new Color(255, 215, 0); // Золотой
    private static final Color ROBOT_BODY_COLOR = new Color(20, 20, 150); // Глубокий синий

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
     * <p>Отрисовывает сетку лабиринта, стилизованный клад (ромб), анимированный путь и
     * стилизованный робот (закругленный квадрат с датчиком).</p>
     *
     * @param g Графический контекст.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Включаем сглаживание для улучшения качества отрисовки фигур
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // --- 1. Отрисовка лабиринта (стен и пола) ---
        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                CellType cell = maze.getCell(x, y);

                // Отрисовка фона ячейки
                g2.setColor(cell == CellType.WALL ? WALL_COLOR : FLOOR_COLOR);
                g2.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

                // Отрисовка сетки
                g2.setColor(Color.GRAY.darker());
                g2.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        // --- 2. Отрисовка клада (стилизованный ромб) ---
        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                if (maze.getCell(x, y) == CellType.TREASURE) {
                    // Координаты для ромба
                    int[] xPoints = {
                            x * CELL_SIZE + CELL_SIZE / 2,
                            x * CELL_SIZE + CELL_SIZE - 5,
                            x * CELL_SIZE + CELL_SIZE / 2,
                            x * CELL_SIZE + 5
                    };
                    int[] yPoints = {
                            y * CELL_SIZE + 5,
                            y * CELL_SIZE + CELL_SIZE / 2,
                            y * CELL_SIZE + CELL_SIZE - 5,
                            y * CELL_SIZE + CELL_SIZE / 2
                    };

                    g2.setColor(TREASURE_COLOR);
                    g2.fillPolygon(xPoints, yPoints, 4);

                    // Блик
                    g2.setColor(Color.WHITE);
                    g2.fillOval(x * CELL_SIZE + CELL_SIZE / 2 - 3, y * CELL_SIZE + 5, 5, 5);
                }
            }
        }

        // --- 3. Отрисовка найденного пути ---
        g2.setColor(new Color(30, 144, 255, 150)); // Полупрозрачный синий
        for (MazeState s : path) {
            g2.fillOval(s.x() * CELL_SIZE + 8, s.y() * CELL_SIZE + 8, CELL_SIZE - 16, CELL_SIZE - 16);
        }

        // --- 4. Отрисовка робота (стилизованный закругленный квадрат) ---
        MazeState current = agent.getCurrentState();
        if (current != null) {
            int x = current.x() * CELL_SIZE;
            int y = current.y() * CELL_SIZE;

            // Корпус
            g2.setColor(ROBOT_BODY_COLOR);
            g2.fillRoundRect(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10, 10, 10);

            // Датчик/Экран
            g2.setColor(Color.CYAN.brighter());
            g2.fillOval(x + CELL_SIZE / 2 - 6, y + CELL_SIZE / 2 - 6, 12, 12);

            // Граница корпуса
            g2.setColor(Color.BLACK);
            g2.drawRoundRect(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10, 10, 10);
        }
    }
}