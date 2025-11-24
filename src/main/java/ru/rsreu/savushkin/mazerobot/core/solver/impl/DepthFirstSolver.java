package ru.rsreu.savushkin.mazerobot.core.solver.impl;

import ru.rsreu.savushkin.mazerobot.core.solver.ProblemSolver;
import ru.rsreu.savushkin.mazerobot.core.solver.engine.Situation;
import ru.rsreu.savushkin.mazerobot.core.state.Action;
import ru.rsreu.savushkin.mazerobot.core.state.Environment;
import ru.rsreu.savushkin.mazerobot.core.state.State;

import java.util.*;

/**
 * Поиск в глубину (DFS).
 * Стратегия: Tree Search (Поиск на дереве) / Backtracking.
 * Особенности: Не хранит visited, экономит память, использует hasLoop().
 */
public class DepthFirstSolver implements ProblemSolver {
    private static final int MAX_DEPTH_LIMIT = 500; // Ограничитель ресурсов

    @Override
    public <S extends State> List<S> solve(Environment<S, ?> env) {
        if (env == null) throw new IllegalArgumentException("Environment is null");

        Situation<S> root = new Situation<>(env.getInitialState());
        Situation<S> result = recursiveSearch(env, root);

        return result != null ? extractPath(result) : Collections.emptyList();
    }

    private <S extends State> Situation<S> recursiveSearch(Environment<S, ?> env, Situation<S> current) {
        // Проверка цели
        if (env.isGoal(current.getState())) {
            return current;
        }

        // Ограничение глубины
        if (current.getDepth() >= MAX_DEPTH_LIMIT) {
            return null;
        }

        List<? extends Action> actions = env.getPossibleActions(current.getState());

        for (Action action : actions) {
            S nextState = env.applyAction(current.getState(), action);

            // Охраняющая конструкция:
            // Валидность хода + Проверка цикла в ветке (не глобально!)
            if (env.isValid(nextState) && !current.hasLoop(nextState)) {

                Situation<S> nextSituation = new Situation<>(
                        nextState, current, action, current.getDepth() + 1
                );

                Situation<S> res = recursiveSearch(env, nextSituation);
                if (res != null) return res;
                // Backtracking происходит автоматически при выходе из рекурсии
            }
        }
        return null;
    }

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
        return "Поиск в глубину (Tree DFS)";
    }
}