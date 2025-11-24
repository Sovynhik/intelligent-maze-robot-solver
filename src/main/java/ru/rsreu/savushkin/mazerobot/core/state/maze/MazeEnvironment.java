package ru.rsreu.savushkin.mazerobot.core.state.maze;

import ru.rsreu.savushkin.mazerobot.core.entity.CellType;
import ru.rsreu.savushkin.mazerobot.core.model.MazeModel;
import ru.rsreu.savushkin.mazerobot.core.state.Action;
import ru.rsreu.savushkin.mazerobot.core.state.Environment;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация среды (Environment) для задачи "Робот в лабиринте".
 * <p>Определяет начальное, целевое состояния, правила перехода (действия, прыжки)
 * и условия валидности состояний в соответствии с моделью лабиринта.</p>
 */
public class MazeEnvironment implements Environment<MazeState, MoveAction> {
    private final MazeModel maze;
    private final MazeState start;
    private final MazeState goal;

    /**
     * Создает новую среду лабиринта.
     *
     * @param maze Модель лабиринта, с которой работает среда.
     */
    public MazeEnvironment(MazeModel maze) {
        this.maze = maze;
        this.start = new MazeState(1, 1);
        this.goal = new MazeState(maze.getWidth() - 2, maze.getHeight() - 2);
    }

    /**
     * Возвращает начальное состояние робота.
     * @return Объект MazeState, представляющий стартовую позицию.
     */
    @Override
    public MazeState getInitialState() { return start; }

    /**
     * Возвращает целевое состояние (позицию клада).
     * @return Объект MazeState, представляющий целевую позицию.
     */
    @Override
    public MazeState getGoalState() { return goal; }

    /**
     * Проверяет, является ли состояние валидным: находится ли оно в пределах лабиринта
     * и не является ли стеной.
     *
     * @param state Проверяемое состояние.
     * @return {@code true}, если состояние валидно и доступно для посещения.
     */
    @Override
    public boolean isValid(MazeState state) {
        int x = state.x();
        int y = state.y();
        return x >= 0 && x < maze.getWidth() &&
                y >= 0 && y < maze.getHeight() &&
                maze.getCell(x, y) != CellType.WALL;
    }

    /**
     * Проверяет, достигнуто ли целевое состояние.
     *
     * @param state Текущее состояние.
     * @return {@code true}, если текущее состояние совпадает с целевым.
     */
    @Override
    public boolean isGoal(MazeState state) {
        return state.equals(goal);
    }

    /**
     * Генерирует список всех возможных действий (ход на 1 клетку и прыжок на 2 клетки)
     * из текущего состояния.
     *
     * @param state Текущее состояние.
     * @return Список всех возможных действий MoveAction.
     */
    @Override
    public List<MoveAction> getPossibleActions(MazeState state) {
        List<MoveAction> actions = new ArrayList<>();
        int[][] dirs = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};

        for (int[] d : dirs) {
            // Обычный шаг
            actions.add(new MoveAction(d[0], d[1], false));
            // Прыжок
            actions.add(new MoveAction(d[0] * 2, d[1] * 2, true));
        }
        return actions;
    }

    /**
     * Применяет заданное действие и возвращает результирующее состояние.
     * <p>Если действие невалидно (например, прыжок через стену или ход в стену),
     * робот остается в исходном состоянии.</p>
     *
     * @param state Исходное состояние.
     * @param action Применяемое действие.
     * @return Новое состояние, если ход валиден, или исходное состояние, если ход невалиден.
     * @throws IllegalArgumentException если переданный объект не является MoveAction.
     */
    @Override
    public MazeState applyAction(MazeState state, Action action) {
        if (!(action instanceof MoveAction move)) throw new IllegalArgumentException("Wrong action type");

        // Логика прыжка: проверяем клетку посередине
        if (move.isDouble()) {
            int midX = state.x() + (move.dx() / 2);
            int midY = state.y() + (move.dy() / 2);

            // Если промежуточная клетка не валидна, прыжок невозможен
            if (!isValid(new MazeState(midX, midY))) {
                return state;
            }
        }

        MazeState nextState = new MazeState(state.x() + move.dx(), state.y() + move.dy());

        // Проверяем, что конечная точка валидна
        if (isValid(nextState)) {
            return nextState;
        } else {
            // Если ход ведет в стену/за границу, остаемся на месте.
            return state;
        }
    }
}