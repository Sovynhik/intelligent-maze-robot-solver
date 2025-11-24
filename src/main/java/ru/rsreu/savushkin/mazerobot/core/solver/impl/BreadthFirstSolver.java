package ru.rsreu.savushkin.mazerobot.core.solver.impl;

import ru.rsreu.savushkin.mazerobot.core.solver.ProblemSolver;
import ru.rsreu.savushkin.mazerobot.core.solver.engine.Situation;
import ru.rsreu.savushkin.mazerobot.core.state.Environment;
import ru.rsreu.savushkin.mazerobot.core.state.State;

import java.util.*;

/**
 * Поиск в ширину (BFS).
 * Стратегия: Graph Search (Поиск на графе).
 * Особенности: Использует visitedSet для безызбыточного дерева.
 */
public class BreadthFirstSolver implements ProblemSolver {

    @Override
    public <S extends State> List<S> solve(Environment<S, ?> env) {
        if (env == null) throw new IllegalArgumentException("Environment is null");

        Queue<Situation<S>> queue = new LinkedList<>();
        Set<S> visited = new HashSet<>();

        S start = env.getInitialState();
        Situation<S> root = new Situation<>(start);

        queue.add(root);
        visited.add(start);

        while (!queue.isEmpty()) {
            Situation<S> current = queue.poll();

            if (env.isGoal(current.getState())) {
                return extractPath(current);
            }

            for (var action : env.getPossibleActions(current.getState())) {
                S nextState = env.applyAction(current.getState(), action);

                if (env.isValid(nextState) && !visited.contains(nextState)) {
                    visited.add(nextState);
                    queue.add(new Situation<>(
                            nextState, current, action, current.getDepth() + 1
                    ));
                }
            }
        }
        return Collections.emptyList();
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
        return "Поиск в ширину (Graph BFS)";
    }
}