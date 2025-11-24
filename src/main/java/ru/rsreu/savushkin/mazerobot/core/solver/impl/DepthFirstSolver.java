package ru.rsreu.savushkin.mazerobot.core.solver.impl;

import ru.rsreu.savushkin.mazerobot.core.solver.ProblemSolver;
import ru.rsreu.savushkin.mazerobot.core.solver.engine.Situation;
import ru.rsreu.savushkin.mazerobot.core.state.Action;
import ru.rsreu.savushkin.mazerobot.core.state.Environment;
import ru.rsreu.savushkin.mazerobot.core.state.State;

import java.util.*;

/**
 * Поиск в глубину (DFS).
 * Стратегия: Tree Search (Поиск на дереве).
 */
public class DepthFirstSolver implements ProblemSolver {
    private static final int MAX_DEPTH_LIMIT = 500;

    @Override
    public <S extends State> List<S> solve(Environment<S, ?> env, S startState) {
        if (env == null || startState == null) throw new IllegalArgumentException("Arguments cannot be null");

        // Создаем корневую ситуацию: глубина 0, стоимость пути g(n)=0.0
        Situation<S> root = new Situation<>(startState, null, null, 0, 0.0);

        Situation<S> result = recursiveSearch(env, root);

        return result != null ? extractPath(result) : Collections.emptyList();
    }

    private <S extends State> Situation<S> recursiveSearch(Environment<S, ?> env, Situation<S> current) {
        if (env.isGoal(current.getState())) return current;
        if (current.getDepth() >= MAX_DEPTH_LIMIT) return null;

        List<? extends Action> actions = env.getPossibleActions(current.getState());

        for (Action action : actions) {
            S nextState = env.applyAction(current.getState(), action);

            // В DFS каждое действие имеет стоимость 1
            double newGCost = current.getGCost() + 1.0;

            // Проверка на валидность, цикл и отсутствие стояния на месте
            if (env.isValid(nextState) && !current.hasLoop(nextState) && !nextState.equals(current.getState())) {

                Situation<S> nextSituation = new Situation<>(
                        nextState,
                        current,
                        action,
                        current.getDepth() + 1,
                        newGCost // <-- ИСПРАВЛЕНО
                );

                Situation<S> res = recursiveSearch(env, nextSituation);
                if (res != null) return res;
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