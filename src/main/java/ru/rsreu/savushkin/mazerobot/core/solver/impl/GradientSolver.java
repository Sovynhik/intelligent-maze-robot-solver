package ru.rsreu.savushkin.mazerobot.core.solver.impl;

import ru.rsreu.savushkin.mazerobot.core.solver.ProblemSolver;
import ru.rsreu.savushkin.mazerobot.core.solver.engine.Situation;
import ru.rsreu.savushkin.mazerobot.core.state.Environment;
import ru.rsreu.savushkin.mazerobot.core.state.State;
import ru.rsreu.savushkin.mazerobot.core.state.maze.MazeState;
import ru.rsreu.savushkin.mazerobot.core.state.maze.MoveAction;

import java.util.*;

/**
 * Реализация алгоритма жадного поиска по первому наилучшему совпадению (Greedy Best-First Search).
 * <p>Использует только эвристическую функцию h(n) (градиент) для выбора следующего узла,
 * игнорируя фактическую стоимость g(n). Это быстрый, но неоптимальный алгоритм.</p>
 */
public class GradientSolver implements ProblemSolver {

    /**
     * Решает задачу поиска пути в лабиринте с использованием жадного градиентного алгоритма.
     *
     * @param <S> тип состояния
     * @param env среда выполнения с информацией о лабиринте
     * @param startState начальное состояние
     * @return список состояний, представляющий путь от начала до цели, или пустой список если путь не найден
     * @throws IllegalArgumentException если переданное состояние не является MazeState
     */
    @Override
    public <S extends State> List<S> solve(Environment<S, ?> env, S startState) {
        // Проверка типа входных данных
        if (!(startState instanceof MazeState)) {
            throw new IllegalArgumentException("GradientSolver requires MazeState environment.");
        }

        // Приведение типов для работы с лабиринтом
        Environment<MazeState, MoveAction> mazeEnv = (Environment<MazeState, MoveAction>) env;
        MazeState start = (MazeState) startState;

        // Инициализация структур данных для поиска
        PriorityQueue<Situation<MazeState>> openSet = new PriorityQueue<>(); // Очередь с приоритетом по эвристике
        Set<MazeState> closedSet = new HashSet<>(); // Посещенные состояния

        // Создание начальной ситуации
        Situation<MazeState> root = new Situation<>(start, null, null, 0, 0.0);
        root.setFCost(heuristicEval(mazeEnv, start));

        openSet.add(root);
        closedSet.add(start);

        // Основной цикл поиска
        while (!openSet.isEmpty()) {
            Situation<MazeState> current = openSet.poll();
            MazeState currentState = current.getState();

            // Проверка достижения цели
            if (mazeEnv.isGoal(currentState)) {
                return (List<S>) extractPath(current);
            }

            // Обработка возможных ходов
            for (var action : mazeEnv.getPossibleActions(currentState)) {
                MazeState nextState = mazeEnv.applyAction(currentState, action);

                // Пропуск невалидных или уже посещенных состояний
                if (!mazeEnv.isValid(nextState) || nextState.equals(currentState) || closedSet.contains(nextState)) {
                    continue;
                }

                // Создание новой ситуации
                Situation<MazeState> nextSituation = new Situation<>(
                        nextState, current, action, current.getDepth() + 1, 0.0
                );

                // Вычисление эвристики и добавление в очередь
                double hCost = heuristicEval(mazeEnv, nextState);
                nextSituation.setFCost(hCost);

                closedSet.add(nextState);
                openSet.add(nextSituation);
            }
        }

        // Путь не найден
        return Collections.emptyList();
    }
    /**
     * Вычисляет эвристическую оценку расстояния до цели с использованием манхэттенского расстояния.
     *
     * @param env среда выполнения
     * @param state текущее состояние
     * @return эвристическая оценка расстояния до цели
     */
    private double heuristicEval(Environment<MazeState, ?> env, MazeState state) {
        MazeState goal = env.getGoalState();
        return Math.abs(state.x() - goal.x()) + Math.abs(state.y() - goal.y());
    }

    /**
     * Восстанавливает путь от конечного состояния до начального.
     *
     * @param <S> тип состояния
     * @param end конечное состояние
     * @return список состояний, представляющий путь от начала до конца
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

    /**
     * Возвращает название алгоритма.
     *
     * @return название алгоритма поиска
     */
    @Override
    public String getName() {
        return "Gradient Search";
    }
}