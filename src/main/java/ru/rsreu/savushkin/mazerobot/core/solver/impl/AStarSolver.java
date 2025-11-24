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
 * <p>Использует приоритетную очередь (PriorityQueue) и допустимую эвристическую функцию
 * для нахождения кратчайшего пути с учетом веса действий (прыжок = 2, шаг = 1).</p>
 */
public class AStarSolver implements ProblemSolver {

    /**
     * Ищет оптимальный путь от начального состояния до цели с помощью алгоритма A*.
     *
     * @param env Среда (лабиринт), в которой происходит поиск.
     * @param startState Начальное состояние.
     * @param <S> Тип состояния (должен быть MazeState).
     * @return Список состояний, составляющих кратчайший путь, или пустой список, если путь не найден.
     */
    @Override
    public <S extends State> List<S> solve(Environment<S, ?> env, S startState) {
        if (!(startState instanceof MazeState)) {
            throw new IllegalArgumentException("AStarSolver requires MazeState environment.");
        }

        Environment<MazeState, MoveAction> mazeEnv = (Environment<MazeState, MoveAction>) env;
        MazeState start = (MazeState) startState;

        PriorityQueue<Situation<MazeState>> openSet = new PriorityQueue<>();
        Map<MazeState, Double> gCosts = new HashMap<>();

        Situation<MazeState> root = new Situation<>(start, null, null, 0, 0.0);
        root.setFCost(heuristicEval(mazeEnv, start));

        openSet.add(root);
        gCosts.put(start, 0.0);

        while (!openSet.isEmpty()) {
            Situation<MazeState> current = openSet.poll();
            MazeState currentState = current.getState();

            if (mazeEnv.isGoal(currentState)) {
                return (List<S>) extractPath(current);
            }

            for (var action : mazeEnv.getPossibleActions(currentState)) {
                MazeState nextState = mazeEnv.applyAction(currentState, action);

                if (!mazeEnv.isValid(nextState) || nextState.equals(currentState)) {
                    continue;
                }

                double moveCost = action.isDouble() ? 2.0 : 1.0;
                double newGCost = current.getGCost() + moveCost;

                if (gCosts.containsKey(nextState) && newGCost >= gCosts.get(nextState)) {
                    continue;
                }

                Situation<MazeState> nextSituation = new Situation<>(
                        nextState, current, action, current.getDepth() + 1, newGCost
                );

                double hCost = heuristicEval(mazeEnv, nextState);
                nextSituation.setFCost(newGCost + hCost);

                gCosts.put(nextState, newGCost);
                openSet.add(nextSituation);
            }
        }
        return Collections.emptyList(); // Path not found
    }

    /**
     * Эвристическая функция h(n): Манхэттенское расстояние.
     * <p>Является допустимой эвристикой, так как не переоценивает фактическую стоимость до цели.</p>
     *
     * @param env Среда для доступа к целевому состоянию.
     * @param state Текущее состояние.
     * @return Эвристическая оценка расстояния до цели.
     */
    private double heuristicEval(Environment<MazeState, ?> env, MazeState state) {
        MazeState goal = env.getGoalState();
        return Math.abs(state.x() - goal.x()) + Math.abs(state.y() - goal.y());
    }

    /**
     * Восстанавливает путь, начиная с целевого узла, по ссылкам на родительские узлы.
     *
     * @param end Узел целевого состояния.
     * @param <S> Тип состояния.
     * @return Список состояний, формирующих путь.
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
     * Возвращает имя алгоритма для отображения в UI.
     * @return Имя алгоритма.
     */
    @Override
    public String getName() {
        return "A* Search";
    }
}