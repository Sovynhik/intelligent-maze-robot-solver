package ru.rsreu.savushkin.mazerobot.core.model;

import ru.rsreu.savushkin.mazerobot.core.entity.CellType;
import ru.rsreu.savushkin.mazerobot.core.state.maze.MazeState; // <--- ДОБАВЛЕНО
import java.util.ArrayList; // <--- ДОБАВЛЕНО
import java.util.LinkedList; // <--- ДОБАВЛЕНО
import java.util.List; // <--- ДОБАВЛЕНО
import java.util.Queue; // <--- ДОБАВЛЕНО
import java.util.HashSet; // <--- ДОБАВЛЕНО
import java.util.Set; // <--- ДОБАВЛЕНО
import java.util.Random;

/**
 * Модель данных лабиринта (Сетка).
 */
public class MazeModel {
    private final int width;
    private final int height;
    private final CellType[][] grid;

    public MazeModel(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new CellType[height][width];

        // Генерируем лабиринт, пока он не станет проходимым
        while (!generateAndCheckMaze()) {
            // Вывод для отладки
            System.out.println("Сгенерированный лабиринт непроходим. Идет перегенерация...");
        }
    }

    // Новая обертка для генерации и проверки
    private boolean generateAndCheckMaze() {
        generateMaze();
        return isSolvable();
    }

    private void generateMaze() {
        Random rand = new Random();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                    grid[y][x] = CellType.WALL;
                } else {
                    // Вероятность стены 20%
                    grid[y][x] = (rand.nextDouble() < 0.2) ? CellType.WALL : CellType.EMPTY;
                }
            }
        }
        // Гарантируем пустоту на старте и финише
        grid[1][1] = CellType.EMPTY;
        grid[height - 2][width - 2] = CellType.TREASURE;
    }

    /**
     * Проверяет, существует ли путь от старта до цели, используя BFS.
     * Учитывает обычные шаги и прыжки.
     */
    private boolean isSolvable() {
        MazeState start = new MazeState(1, 1);
        MazeState goal = new MazeState(width - 2, height - 2);

        Queue<MazeState> queue = new LinkedList<>();
        Set<MazeState> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);

        int[][] dirs = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}}; // Базовые направления

        while (!queue.isEmpty()) {
            MazeState current = queue.poll();

            if (current.equals(goal)) {
                return true; // Путь найден
            }

            // Генерируем все возможные ходы: 1 шаг и 2 шага (прыжок)
            List<int[]> moves = new ArrayList<>();
            for (int[] d : dirs) {
                moves.add(d);                 // 1. Обычный шаг
                moves.add(new int[]{d[0] * 2, d[1] * 2}); // 2. Прыжок (2 шага)
            }

            for (int[] move : moves) {
                int nextX = current.x() + move[0];
                int nextY = current.y() + move[1];

                MazeState next = new MazeState(nextX, nextY);

                // 1. Проверка на выход за границы или попадание в стену в конечной точке
                if (nextX < 0 || nextX >= width || nextY < 0 || nextY >= height || grid[nextY][nextX] == CellType.WALL) {
                    continue;
                }

                // 2. Проверка промежуточной клетки для прыжков
                if (Math.abs(move[0]) > 1 || Math.abs(move[1]) > 1) {
                    int midX = current.x() + (move[0] / 2);
                    int midY = current.y() + (move[1] / 2);

                    // Если промежуточная клетка - стена, прыжок невозможен
                    if (grid[midY][midX] == CellType.WALL) {
                        continue;
                    }
                }

                // 3. Проверка посещения и добавление в очередь
                if (!visited.contains(next)) {
                    visited.add(next);
                    queue.add(next);
                }
            }
        }
        return false; // Путь не найден
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public CellType getCell(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return CellType.WALL;
        return grid[y][x];
    }
}