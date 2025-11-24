package ru.rsreu.savushkin.mazerobot.core.solver.impl;

import ru.rsreu.savushkin.mazerobot.core.solver.ProblemSolver;
import ru.rsreu.savushkin.mazerobot.core.solver.engine.Situation;
import ru.rsreu.savushkin.mazerobot.core.state.Environment;
import ru.rsreu.savushkin.mazerobot.core.state.State;
import ru.rsreu.savushkin.mazerobot.core.state.maze.MazeState;
import ru.rsreu.savushkin.mazerobot.core.state.maze.MoveAction;

import java.util.*;

/**
 * Реализация алгоритма A* (A-Star Search).
 * Использует PriorityQueue и эвристическую функцию.
 */
public class AStarSolver implements ProblemSolver {

    @Override
    public <S extends State> List<S> solve(Environment<S, ?> env, S startState) {
        if (!(startState instanceof MazeState)) {
            throw new IllegalArgumentException("AStarSolver requires MazeState environment.");
        }

        Environment<MazeState, MoveAction> mazeEnv = (Environment<MazeState, MoveAction>) env;
        MazeState start = (MazeState) startState;

        PriorityQueue<Situation<MazeState>> openSet = new PriorityQueue<>();
        Map<MazeState, Double> gCosts = new HashMap<>();

        // 1. Инициализация стартового состояния
        Situation<MazeState> root = new Situation<>(start, null, null, 0, 0.0);
        root.setFCost(heuristicEval(mazeEnv, start));

        openSet.add(root);
        gCosts.put(start, 0.0);

        while (!openSet.isEmpty()) {
            Situation<MazeState> current = openSet.poll();
            MazeState currentState = current.getState();

            // Цель достигнута
            if (mazeEnv.isGoal(currentState)) {
                // ИСПРАВЛЕНИЕ: Явно указываем тип MazeState при вызове
                return (List<S>) extractPath(current);
            }

            // 2. Генерация действий
            for (var action : mazeEnv.getPossibleActions(currentState)) {
                MazeState nextState = mazeEnv.applyAction(currentState, action);

                if (!mazeEnv.isValid(nextState) || nextState.equals(currentState)) {
                    continue;
                }

                // 3. Вычисление стоимости g(n)
                double moveCost = action.isDouble() ? 2.0 : 1.0;
                double newGCost = current.getGCost() + moveCost;

                // 4. Проверка: нашли ли мы более короткий путь g(n)?
                if (gCosts.containsKey(nextState) && newGCost >= gCosts.get(nextState)) {
                    continue;
                }

                // 5. Создание новой ситуации (нашли лучший путь)
                Situation<MazeState> nextSituation = new Situation<>(
                        nextState, current, action, current.getDepth() + 1, newGCost
                );

                // Вычисление f(n) = g(n) + h(n)
                double hCost = heuristicEval(mazeEnv, nextState);
                nextSituation.setFCost(newGCost + hCost);

                gCosts.put(nextState, newGCost);
                openSet.add(nextSituation);
            }
        }
        return Collections.emptyList(); // Путь не найден
    }

    /**
     * Эвристическая функция h(n): Манхэттенское расстояние.
     */
    private double heuristicEval(Environment<MazeState, ?> env, MazeState state) {
        MazeState goal = env.getGoalState();
        return Math.abs(state.x() - goal.x()) + Math.abs(state.y() - goal.y());
    }

    /**
     * Восстанавливает путь.
     */
    private <S extends State> List<S> extractPath(Situation<S> end) {
        LinkedList<S> path = new LinkedList<>();
        Situation<S> curr = end;
        while (curr != null) {
            path.addFirst(curr.getState());
            curr = curr.getParent();
        }
        return path;
    }

    @Override
    public String getName() {
        return "A* Search";
    }
}