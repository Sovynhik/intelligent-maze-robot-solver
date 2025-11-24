package ru.rsreu.savushkin.mazerobot.core.solver.impl;

import ru.rsreu.savushkin.mazerobot.core.solver.ProblemSolver;
import ru.rsreu.savushkin.mazerobot.core.solver.engine.Situation;
import ru.rsreu.savushkin.mazerobot.core.state.Environment;
import ru.rsreu.savushkin.mazerobot.core.state.State;

import java.util.*;

/**
 * Поиск в ширину (BFS).
 * Стратегия: Graph Search (Поиск на графе).
 */
public class BreadthFirstSolver implements ProblemSolver {

    @Override
    public <S extends State> List<S> solve(Environment<S, ?> env, S startState) {
        if (env == null || startState == null) throw new IllegalArgumentException("Arguments cannot be null");

        Queue<Situation<S>> queue = new LinkedList<>();
        Set<S> visitedStates = new HashSet<>();

        Situation<S> root = new Situation<>(startState);

        queue.add(root);
        visitedStates.add(startState);

        while (!queue.isEmpty()) {
            Situation<S> current = queue.poll();

            if (env.isGoal(current.getState())) {
                return extractPath(current);
            }

            for (var action : env.getPossibleActions(current.getState())) {
                S nextState = env.applyAction(current.getState(), action);

                if (env.isValid(nextState) && !visitedStates.contains(nextState)) {
                    visitedStates.add(nextState);
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