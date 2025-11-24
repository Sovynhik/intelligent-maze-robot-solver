package ru.rsreu.savushkin.mazerobot.core.state.maze;

import ru.rsreu.savushkin.mazerobot.core.entity.CellType;
import ru.rsreu.savushkin.mazerobot.core.model.MazeModel;
import ru.rsreu.savushkin.mazerobot.core.state.Action;
import ru.rsreu.savushkin.mazerobot.core.state.Environment;

import java.util.ArrayList;
import java.util.List;

public class MazeEnvironment implements Environment<MazeState, MoveAction> {
    private final MazeModel maze;
    private final MazeState start;
    private final MazeState goal;

    public MazeEnvironment(MazeModel maze) {
        this.maze = maze;
        this.start = new MazeState(1, 1);
        this.goal = new MazeState(maze.getWidth() - 2, maze.getHeight() - 2);
    }

    @Override
    public MazeState getInitialState() { return start; }

    @Override
    public MazeState getGoalState() { return goal; }

    @Override
    public boolean isValid(MazeState state) {
        int x = state.x();
        int y = state.y();
        return x >= 0 && x < maze.getWidth() &&
                y >= 0 && y < maze.getHeight() &&
                maze.getCell(x, y) != CellType.WALL;
    }

    @Override
    public boolean isGoal(MazeState state) {
        return state.equals(goal);
    }

    @Override
    public List<MoveAction> getPossibleActions(MazeState state) {
        List<MoveAction> actions = new ArrayList<>();
        int[][] dirs = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};

        for (int[] d : dirs) {
            // Обычный шаг
            actions.add(new MoveAction(d[0], d[1], false));
            // Двойной прыжок (для поиска пути в этой версии не используется, но оставлен)
            actions.add(new MoveAction(d[0] * 2, d[1] * 2, true));
        }
        return actions;
    }

    @Override
    public MazeState applyAction(MazeState state, Action action) {
        if (!(action instanceof MoveAction move)) throw new IllegalArgumentException("Wrong action type");

        // Логика прыжка: проверяем клетку посередине
        if (move.isDouble()) {
            // move.dx() теперь равно 2 или -2, поэтому деление дает 1 или -1 (Fix 2)
            int midX = state.x() + (move.dx() / 2);
            int midY = state.y() + (move.dy() / 2);

            // Если промежуточная клетка не валидна (стена), возвращаем текущее состояние (ход не выполнен)
            if (!isValid(new MazeState(midX, midY))) {
                return state;
            }
        }

        // Финальное состояние использует полное смещение (1 или 2),
        // которое теперь корректно задано в MoveAction.
        return new MazeState(state.x() + move.dx(), state.y() + move.dy());
    }
}