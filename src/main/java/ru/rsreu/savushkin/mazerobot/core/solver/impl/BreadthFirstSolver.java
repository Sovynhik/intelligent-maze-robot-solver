package ru.rsreu.savushkin.mazerobot.core.solver.impl;

import ru.rsreu.savushkin.mazerobot.core.solver.ProblemSolver;
import ru.rsreu.savushkin.mazerobot.core.solver.engine.Situation;
import ru.rsreu.savushkin.mazerobot.core.state.Environment;
import ru.rsreu.savushkin.mazerobot.core.state.State;

import java.util.*;

/**
 * Реализация алгоритма Поиска в ширину (Breadth-First Search, BFS).
 * <p>Является полным алгоритмом и гарантирует нахождение кратчайшего пути,
 * поскольку исследует состояния слой за слоем (сначала все состояния на глубине N, затем на N+1).</p>
 */
public class BreadthFirstSolver implements ProblemSolver {

    /**
     * Ищет кратчайший путь от начального состояния до цели с помощью BFS.
     * <p>Использует очередь (Queue) для посещения узлов и набор (Set) для отслеживания посещенных состояний,
     * чтобы избежать циклов и повторной обработки.</p>
     *
     * @param env Среда (лабиринт), в которой происходит поиск.
     * @param startState Начальное состояние.
     * @param <S> Тип состояния.
     * @return Список состояний, составляющих кратчайший путь, или пустой список, если путь не найден.
     */
    @Override
    public <S extends State> List<S> solve(Environment<S, ?> env, S startState) {
        if (env == null || startState == null) throw new IllegalArgumentException("Arguments cannot be null");

        Queue<Situation<S>> queue = new LinkedList<>();
        Set<S> visitedStates = new HashSet<>();

        // Стоимость пути g(n)=0.0 для корневого узла
        Situation<S> root = new Situation<>(startState, null, null, 0, 0.0);

        queue.add(root);
        visitedStates.add(startState);

        while (!queue.isEmpty()) {
            Situation<S> current = queue.poll();

            if (env.isGoal(current.getState())) {
                return extractPath(current);
            }

            for (var action : env.getPossibleActions(current.getState())) {
                S nextState = env.applyAction(current.getState(), action);

                if (env.isValid(nextState) && !visitedStates.contains(nextState) && !nextState.equals(current.getState())) {

                    // В BFS каждое действие имеет стоимость 1.0
                    double newGCost = current.getGCost() + 1.0;

                    visitedStates.add(nextState);
                    queue.add(new Situation<>(
                            nextState,
                            current,
                            action,
                            current.getDepth() + 1,
                            newGCost
                    ));
                }
            }
        }
        return Collections.emptyList(); // Path not found
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
        return "Breadth-First Search (BFS)";
    }
}