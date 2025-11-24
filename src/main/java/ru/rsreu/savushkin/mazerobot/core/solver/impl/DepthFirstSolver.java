package ru.rsreu.savushkin.mazerobot.core.solver.impl;

import ru.rsreu.savushkin.mazerobot.core.solver.ProblemSolver;
import ru.rsreu.savushkin.mazerobot.core.solver.engine.Situation;
import ru.rsreu.savushkin.mazerobot.core.state.Action;
import ru.rsreu.savushkin.mazerobot.core.state.Environment;
import ru.rsreu.savushkin.mazerobot.core.state.State;

import java.util.*;

/**
 * Реализация алгоритма Поиска в глубину (Depth-First Search, DFS).
 * <p>Использует стратегию поиска на дереве (Tree Search) с рекурсивным обходом
 * и ограничением максимальной глубины для предотвращения бесконечных циклов.</p>
 */
public class DepthFirstSolver implements ProblemSolver {
    private static final int MAX_DEPTH_LIMIT = 500;

    /**
     * Ищет путь от начального состояния до цели с помощью DFS.
     *
     * @param env Среда (лабиринт), в которой происходит поиск.
     * @param startState Начальное состояние.
     * @param <S> Тип состояния.
     * @return Список состояний, составляющих найденный путь, или пустой список, если путь не найден.
     */
    @Override
    public <S extends State> List<S> solve(Environment<S, ?> env, S startState) {
        if (env == null || startState == null) throw new IllegalArgumentException("Arguments cannot be null");

        // Стоимость пути g(n)=0.0 для корневого узла
        Situation<S> root = new Situation<>(startState, null, null, 0, 0.0);

        Situation<S> result = recursiveSearch(env, root);

        return result != null ? extractPath(result) : Collections.emptyList();
    }

    /**
     * Рекурсивная функция для выполнения поиска в глубину.
     *
     * @param env Среда.
     * @param current Текущий узел (ситуация).
     * @param <S> Тип состояния.
     * @return Целевой узел, если найден, или {@code null}.
     */
    private <S extends State> Situation<S> recursiveSearch(Environment<S, ?> env, Situation<S> current) {
        if (env.isGoal(current.getState())) return current;
        if (current.getDepth() >= MAX_DEPTH_LIMIT) return null;

        List<? extends Action> actions = env.getPossibleActions(current.getState());

        for (Action action : actions) {
            S nextState = env.applyAction(current.getState(), action);

            double newGCost = current.getGCost() + 1.0;

            // Проверка на валидность, цикл и отсутствие стояния на месте
            if (env.isValid(nextState) && !current.hasLoop(nextState) && !nextState.equals(current.getState())) {

                Situation<S> nextSituation = new Situation<>(
                        nextState,
                        current,
                        action,
                        current.getDepth() + 1,
                        newGCost
                );

                Situation<S> res = recursiveSearch(env, nextSituation);
                if (res != null) return res;
            }
        }
        return null;
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
        return "Depth-First Search (Tree DFS)";
    }
}