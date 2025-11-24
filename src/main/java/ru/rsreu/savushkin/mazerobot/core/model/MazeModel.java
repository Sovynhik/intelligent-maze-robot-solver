package ru.rsreu.savushkin.mazerobot.core.model;

import ru.rsreu.savushkin.mazerobot.core.entity.CellType;
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
        generateMaze();
    }

    private void generateMaze() {
        // Простая генерация коробочки со случайными стенами
        Random rand = new Random();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                    grid[y][x] = CellType.WALL;
                } else {
                    grid[y][x] = (rand.nextDouble() < 0.2) ? CellType.WALL : CellType.EMPTY;
                }
            }
        }
        // Гарантируем пустоту на старте и финише
        grid[1][1] = CellType.EMPTY;
        grid[height - 2][width - 2] = CellType.TREASURE;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public CellType getCell(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return CellType.WALL;
        return grid[y][x];
    }
}